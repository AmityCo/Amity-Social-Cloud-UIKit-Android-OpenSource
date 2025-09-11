package com.amity.socialcloud.uikit.community.compose.post.composer

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityFileInfo
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia.Type
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.core.net.toUri
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.metadata.AmityPostMetadataCreator
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.core.file.AmityFileType
import com.amity.socialcloud.sdk.model.core.file.AmityRawFile
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import kotlin.apply


class AmityPostComposerPageViewModel : AmityMediaAttachmentViewModel() {

    private val MAX_CHAR_LIMIT = 50000
    private val MAX_ATTACHMENTS = 10

    private var options: AmityPostComposerOptions? = null

    var community: AmityCommunity? = null

    private val _post by lazy {
        MutableStateFlow<AmityPost?>(null)
    }
    val post get() = _post

    private val mediaMap = java.util.LinkedHashMap<String, AmityPostMedia>()
    private val uploadedMediaMap = LinkedHashMap<String, AmityFileInfo>()
    private val deletedImageIds = mutableListOf<String>()
    private val uploadFailedMediaMap = LinkedHashMap<String, Boolean>()
    private val showAltTextConfigSheet = mutableStateOf(false)
    private val altTextMedia = mutableStateOf<AltTextMedia?>(null)

    fun showAltTextConfigSheet() {
        showAltTextConfigSheet.value = true
    }

    fun hideAltTextConfigSheet() {
        showAltTextConfigSheet.value = false
    }

    fun shouldShowAltTextConfigSheet(): Boolean {
        return showAltTextConfigSheet.value
    }

    fun setAltTextMedia(media: AltTextMedia?) {
        altTextMedia.value = media
    }

    fun getAltTextMedia(): AltTextMedia? {
        return altTextMedia.value
    }

    private val _postCreationEvent by lazy {
        MutableStateFlow<AmityPostCreationEvent>(AmityPostCreationEvent.Initial)
    }
    val postCreationEvent get() = _postCreationEvent

    private val _selectedMediaFiles by lazy {
        MutableStateFlow<List<AmityPostMedia>>(emptyList())
    }
    val selectedMediaFiles get() = _selectedMediaFiles

    private val _isAllMediaSuccessfullyUploaded by lazy {
        MutableStateFlow(true)
    }
    val isAllMediaSuccessfullyUploaded get() = _isAllMediaSuccessfullyUploaded

    fun setComposerOptions(options: AmityPostComposerOptions) {
        this.options = options
        when (options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                this.community = options.community
            }

            is AmityPostComposerOptions.AmityPostComposerCreateClipOptions -> {
                this.community = options.community
            }

            is AmityPostComposerOptions.AmityPostComposerEditOptions -> {
                preparePostData(options.post.getPostId())
            }

            is AmityPostComposerOptions.AmityPostComposerEditClipOptions -> {
                preparePostData(options.post.getPostId())
            }
        }
    }

    private fun preparePostData(postId: String) {
        getPostDetails(
            postId = postId,
            onPostLoading = {

            },
            onPostLoaded = { post ->
                this.community = (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
                this._post.value = post

                when (post.getData()) {
                    is AmityPost.Data.TEXT -> prepareTextPost(post)
                    is AmityPost.Data.IMAGE -> prepareImagePost(post)
                    is AmityPost.Data.VIDEO -> prepareVideoPost(post)
                    is AmityPost.Data.CLIP -> prepareClipPost(post)
//            is AmityPost.Data.FILE -> prepareFilePost(post)
                    else -> {}
                }

                setPostAttachmentAllowedPickerType(
                    when (post.getChildren().firstOrNull()?.getData()) {
                        is AmityPost.Data.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(
                            canAddMore = post.getChildren().size < MAX_ATTACHMENTS
                        )

                        is AmityPost.Data.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(
                            canAddMore = post.getChildren().size < MAX_ATTACHMENTS
                        )

                        else -> AmityPostAttachmentAllowedPickerType.All
                    }
                )
                checkAllMediaUploadedSuccessfully()
            },
            onPostLoadFailed = {

            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun getPostDetails(
        postId: String,
        onPostLoading: () -> Unit,
        onPostLoaded: (AmityPost) -> Unit,
        onPostLoadFailed: (Throwable) -> Unit,
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                onPostLoading.invoke()
            }
            .doOnSuccess {
                onPostLoaded.invoke(it)
            }
            .doOnError {
                onPostLoadFailed.invoke(it)
            }
            .ignoreElement()
    }

    private fun prepareTextPost(post: AmityPost) {
        setUpPostTextWithImagesOrFiles(post)
    }

    private fun prepareImagePost(post: AmityPost) {
        getPostImage(post)?.let {
            val feedImage = mapImageToFeedImage(it, Type.IMAGE)
            mediaMap[feedImage.url.toString()] = feedImage
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun prepareVideoPost(post: AmityPost) {
        val thumbnail = getPostVideoThumbnail(post)
        val videoPost = if (thumbnail != null) {
            mapImageToFeedImage(thumbnail, Type.VIDEO)
        } else {
            // Create placeholder for video without thumbnail
            createPlaceholderVideoMedia(post)
        }
        mediaMap[videoPost.url.toString()] = videoPost
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun prepareClipPost(post: AmityPost) {
        getPostVideoThumbnail(post)?.let {
            val videoPost = mapImageToFeedImage(it, Type.VIDEO)
            mediaMap[videoPost.url.toString()] = videoPost
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun setUpPostTextWithImagesOrFiles(post: AmityPost) {
        val postChildren = post.getChildren()
        if (postChildren.isNotEmpty()) {
            when (postChildren.first().getData()) {
                is AmityPost.Data.IMAGE -> setupImagePost(postChildren)
                is AmityPost.Data.VIDEO -> setupVideoPost(postChildren)
//                is AmityPost.Data.FILE -> setupFilePost(postChildren)
                else -> {}
            }
        }
    }

    private fun setupImagePost(postChildren: List<AmityPost>) {
        postChildren
            .map { getPostImage(it) }
            .forEach { image ->
                image?.let {
                    val feedImage = mapImageToFeedImage(it, Type.IMAGE)
                    mediaMap[feedImage.url.toString()] = feedImage
                }
            }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun setupVideoPost(postChildren: List<AmityPost>) {
        postChildren.forEach { post ->
            val thumbnail = getPostVideoThumbnail(post)
            val videoMedia = if (thumbnail != null) {
                mapImageToFeedImage(thumbnail, Type.VIDEO)
            } else {
                createPlaceholderVideoMedia(post)
            }
            mediaMap[videoMedia.url.toString()] = videoMedia
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun mapImageToFeedImage(image: AmityImage, type: Type): AmityPostMedia {
        return AmityPostMedia(
            id = image.getFileId(),
            uploadId = null,
            url = image.getUrl(AmityImage.Size.MEDIUM).toUri(),
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = type,
            media = AmityPostMedia.Media.Image(image)
        )
    }

    private fun createPlaceholderVideoMedia(post: AmityPost): AmityPostMedia {
        val videoData = post.getData() as? AmityPost.Data.VIDEO

        return AmityPostMedia(
            id = videoData?.getThumbnailImage()?.getFileId() ?: post.getPostId(),
            uploadId = null,
            url = Uri.EMPTY, // Use empty URI as placeholder
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = Type.VIDEO,
            media = null // No thumbnail available
        )
    }

    private fun getPostImage(newsFeed: AmityPost): AmityImage? {
        val imageData = newsFeed.getData() as? AmityPost.Data.IMAGE
        return imageData?.getImage()
    }

    private fun getPostVideoThumbnail(post: AmityPost): AmityImage? {
        val videoData = post.getData() as? AmityPost.Data.VIDEO
        return videoData?.getThumbnailImage()
    }

    private fun getPostFile(newsFeed: AmityPost): AmityFile? {
        val fileData = newsFeed.getData() as? AmityPost.Data.FILE
        return fileData?.getFile()
    }

    fun updatePost(
        postText: String,
        postTitle: String?,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList()
    ) {
        if (postText.length > MAX_CHAR_LIMIT) {
            setPostCreationEvent(
                AmityPostCreationEvent.Failed(
                    TextPostExceedException(
                        MAX_CHAR_LIMIT
                    )
                )
            )
            return
        }

        setPostCreationEvent(AmityPostCreationEvent.Updating)

        val postId = _post.value!!.getPostId()

        // Add special handling for clip posts
        val isClipPost = options is AmityPostComposerOptions.AmityPostComposerEditClipOptions

        if (isClipPost) {
            // For clip posts, only update the text without modifying attachments
            val postEditor = AmitySocialClient.newPostRepository()
                .editPost(postId = postId)
                .text(postText.trim())

            val metadata = mentionedUsers.takeIf { it.isNotEmpty() }?.let {
                AmityMentionMetadataCreator(mentionedUsers).create()
            }
            val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

            postEditor.apply {
                metadata?.let {
                    this.metadata(metadata)
                    this.mentionUsers(mentionUserIds.toList())
                }
            }

            postEditor.build().apply()
                .andThen(Single.defer {
                    AmitySocialClient.newPostRepository()
                        .getPost(postId)
                        .firstOrError()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                        setPostCreationEvent(AmityPostCreationEvent.Pending)
                    } else {
                        AmityPostComposerHelper.updatePost(postId)
                        setPostCreationEvent(AmityPostCreationEvent.Success)
                    }
                }
                .doOnError {
                    setPostCreationEvent(AmityPostCreationEvent.Failed(it))
                }
                .subscribe()
        } else {
            // Original handling for non-clip postsdeleteImageOrFileInPost()
            deleteImageOrFileInPost()
                .andThen(Completable.defer {
                    val attachments = _post.value?.getChildren()
                        ?.map { it.getData() }
                        ?: emptyList()
                    val newAttachments = uploadedMediaMap.values.toList()
                    updateParentPost(postId,
                        postText.trim(),
                        postTitle,
                        attachments,
                        newAttachments,
                        mentionedUsers,
                        hashtags,
                    )
                })
                .andThen(Single.defer {
                    AmitySocialClient.newPostRepository()
                        .getPost(postId)
                        .firstOrError()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                        setPostCreationEvent(AmityPostCreationEvent.Pending)
                    } else {
                        AmityPostComposerHelper.updatePost(postId)
                        setPostCreationEvent(AmityPostCreationEvent.Success)
                    }
                }
                .doOnError {
                    setPostCreationEvent(AmityPostCreationEvent.Failed(it))
                }
                .subscribe()
        }
    }

    private fun deleteImageOrFileInPost(): Completable {
        return _post.value?.getChildren()?.let { ekoPostItems ->
            Observable.fromIterable(ekoPostItems)
                .map { ekoPostItem ->
                    when (val postData = ekoPostItem.getData()) {
                        is AmityPost.Data.IMAGE -> {
                            if (postData.getImage()?.getFileId() in deletedImageIds) {
                                AmitySocialClient.newPostRepository()
                                    .hardDeletePost(ekoPostItem.getPostId())
                            } else {
                                Completable.complete()
                            }
                        }

                        is AmityPost.Data.VIDEO -> {
                            if (postData.getThumbnailImage()?.getFileId() in deletedImageIds) {
                                AmitySocialClient.newPostRepository()
                                    .hardDeletePost(ekoPostItem.getPostId())
                            } else {
                                Completable.complete()
                            }
                        }

                        else -> {
                            Completable.complete()
                        }
                    }
                }.ignoreElements()
        } ?: run {
            Completable.complete()
        }
    }

    private fun updateParentPost(
        postId: String,
        postText: String,
        postTitle: String?,
        attachments: List<AmityPost.Data>,
        newAttachments: List<AmityFileInfo>,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList()
    ): Completable {
        // Determine attachment type by examining all attachments, not just the first one
        val attachmentType: Type? = run {
            // Check if there are any non-deleted IMAGE attachments
            val hasImages = attachments.any {
                it is AmityPost.Data.IMAGE &&
                        !(it.getImage()?.getFileId() in deletedImageIds)
            }

            if (hasImages) return@run Type.IMAGE

            // Check if there are any non-deleted VIDEO attachments
            val hasVideos = attachments.any {
                it is AmityPost.Data.VIDEO &&
                        !(it.getThumbnailImage()?.getFileId() in deletedImageIds)
            }

            if (hasVideos) return@run Type.VIDEO

            // If no existing attachments remain, check new attachments
            if (newAttachments.isNotEmpty()) {
                when (newAttachments.first()) {
                    is AmityImage -> Type.IMAGE
                    is AmityVideo -> Type.VIDEO
                    else -> null
                }
            } else {
                null // No attachments at all
            }
        }

        val postEditor = AmitySocialClient.newPostRepository()
            .editPost(postId = postId)
            .text(postText)

        if (postTitle != null) {
            postEditor.title(postTitle)
        }

        when (attachmentType) {
            Type.VIDEO -> {
                val videos = attachments.mapNotNull {
                    it as? AmityPost.Data.VIDEO
                }.filter {
                    !(it.getThumbnailImage()?.getFileId()?.let(deletedImageIds::contains)
                        ?: false)
                }.map {
                    it.getVideo().blockingGet()
                } + newAttachments.map { it as AmityVideo }

                postEditor.attachments(*videos.toTypedArray())
            }

            Type.IMAGE -> {
                val images = attachments
                    .mapNotNull { (it as? AmityPost.Data.IMAGE)?.getImage() }
                    .filter {
                        !deletedImageIds.contains(it.getFileId())
                    } + newAttachments.map { it as AmityImage }

                postEditor.attachments(*images.toTypedArray())
            }

            else -> {
                postEditor.text(postText)
                if (postTitle != null) {
                    postEditor.title(postTitle)
                }
                postEditor.attachments(*emptyList<AmityImage>().toTypedArray())
            }
        }

        val metadata = createMetadata(mentionedUsers, hashtags)
        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()
        postEditor
            .apply {
                metadata?.let {
                    this.metadata(metadata)
                    this.mentionUsers(mentionUserIds.toList())
                    this.hashtags(hashtags.map { it.getText() })
                }
            }

        return postEditor
            .build()
            .apply()
    }

    fun createPost(
        postText: String,
        postTitle: String?,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList(),
    ) {
        if (postText.length > MAX_CHAR_LIMIT) {
            setPostCreationEvent(
                AmityPostCreationEvent.Failed(
                    TextPostExceedException(
                        MAX_CHAR_LIMIT
                    )
                )
            )
            return
        }

        setPostCreationEvent(AmityPostCreationEvent.Creating)
        val targetType = getTargetTypeForPostCreation()
        val targetId = getTargetIdForPostCreation()
        // Create metadata with both mentions and hashtags
        val metadata = createMetadata(mentionedUsers, hashtags)


        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

        when {
            isUploadedImageMedia() -> {
                val orderById =
                    mediaMap.values.withIndex().associate { it.value.id to it.index }
                val sortedImages =
                    uploadedMediaMap.values
                        .filter {
                            mediaMap.values.any { postMedia -> postMedia.id == it.getFileId() }
                        }.sortedBy {
                            orderById[it.getFileId()]
                        }.map {
                            it as AmityImage
                        }.toSet()

                createPostTextAndImages(
                    postText = postText,
                    images = sortedImages,
                    targetType = targetType,
                    title = postTitle,
                    targetId = targetId,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                )
            }

            isUploadedVideoMedia() -> {
                val videos = uploadedMediaMap.values.toList().map {
                    it as AmityVideo
                }.toSet()

                createPostTextAndVideos(
                    postText = postText,
                    videos = videos,
                    targetType = targetType,
                    targetId = targetId,
                    title = postTitle,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                )
            }

            isClipMedia() -> {
                val data = (options as AmityPostComposerOptions.AmityPostComposerCreateClipOptions)
                createPostTextAndClip(
                    postText = postText,
                    clip = data.clip,
                    targetType = AmityPost.TargetType.enumOf(data.targetType.name.lowercase()),
                    targetId = data.targetId ?: AmityCoreClient.getUserId(),
                    aspectRatio = data.aspectRatio ?: AmityClip.DisplayMode.FILL,
                    isMute = data.isMute == true,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                )
            }
            // TODO: 16/6/24 create file post
            else -> {
                createPostText(
                    postText = postText,
                    targetType = targetType,
                    targetId = targetId,
                    title = postTitle,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                )
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                    setPostCreationEvent(AmityPostCreationEvent.Pending)
                } else {
                    AmityPostComposerHelper.addNewPost(it)
                    setPostCreationEvent(AmityPostCreationEvent.Success)
                }
            }
            .doOnError {
                setPostCreationEvent(AmityPostCreationEvent.Failed(it))
            }
            .subscribe()
    }

    private fun createPostText(
        postText: String,
        targetType: AmityPost.TargetType,
        targetId: String,
        title: String?,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createTextPost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
            )
    }

    private fun createPostTextAndImages(
        title: String?,
        postText: String,
        images: Set<AmityImage>,
        targetType: AmityPost.TargetType,
        targetId: String,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createImagePost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                images = images,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
            )
    }

    private fun createPostTextAndVideos(
        title: String?,
        postText: String,
        videos: Set<AmityVideo>,
        targetType: AmityPost.TargetType,
        targetId: String,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createVideoPost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                videos = videos,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
            )
    }

    private fun createPostTextAndClip(
        postText: String,
        clip: AmityClip,
        targetType: AmityPost.TargetType,
        targetId: String,
        isMute: Boolean,
        aspectRatio: AmityClip.DisplayMode,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createClipPost(
                targetType = targetType,
                targetId = targetId,
                clip = clip,
                text = postText,
                displayMode = aspectRatio,
                isMuted = isMute,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
            )
    }

    fun addMedia(medias: List<Uri>, mediaType: Type) {
        if (mediaType == Type.IMAGE && medias.size + mediaMap.size > MEDIA_IMAGE_UPLOAD_LIMIT) {
            setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.MaxUploadLimitReached(mediaType))
            return
        } else if (mediaType == Type.VIDEO && medias.size + mediaMap.size > MEDIA_VIDEO_UPLOAD_LIMIT) {
            setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.MaxUploadLimitReached(mediaType))
            return
        }

        medias.filter {
            !mediaMap.containsKey(it.toString())
        }.map { uri ->
            val postMedia = AmityPostMedia(UUID.randomUUID().toString(), uri, mediaType)
            mediaMap[uri.toString()] = postMedia
            uploadMedia(postMedia)
        }

        if (medias.isNotEmpty()) {
            setPostAttachmentAllowedPickerType(
                when (mediaType) {
                    Type.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    Type.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    else -> {
                        AmityPostAttachmentAllowedPickerType.All
                    }
                }
            )
        }
    }


    fun removeMedia(postMedia: AmityPostMedia) {
        mediaMap.remove(postMedia.url.toString())
        uploadFailedMediaMap.remove(postMedia.url.toString())
        cancelUpload(postMedia.uploadId)

        if (postMedia.id != null) {
            //In case update post we want to keep track of the images to delete
            if (_post.value != null) {
                deletedImageIds.add(postMedia.id!!)
            } else {
                uploadedMediaMap.remove(postMedia.id!!)
            }
        }
        _selectedMediaFiles.value = mediaMap.values.toList()

        checkAllMediaUploadedSuccessfully()

        if (mediaMap.isEmpty()) {
            setPostAttachmentAllowedPickerType(AmityPostAttachmentAllowedPickerType.All)
        } else {
            val mediaType = mediaMap.values.first().type
            setPostAttachmentAllowedPickerType(
                when (mediaType) {
                    Type.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    Type.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    else -> {
                        AmityPostAttachmentAllowedPickerType.All
                    }
                }
            )
        }
    }

    fun isUploadedImageMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityImage }
    }

    fun isUploadedVideoMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityVideo }
    }

    fun isClipMedia(): Boolean {
        return options is AmityPostComposerOptions.AmityPostComposerCreateClipOptions
    }

    fun isUploadingImageMedia(): Boolean {
        return mediaMap.values.any { it.type == Type.IMAGE }
    }

    fun isUploadingVideoMedia(): Boolean {
        return mediaMap.values.any { it.type == Type.VIDEO }
    }

    private fun checkAllMediaUploadedSuccessfully() {
        viewModelScope.launch {
            _isAllMediaSuccessfullyUploaded.value = if (mediaMap.isEmpty()) {
                true
            } else {
                mediaMap.values.all { it.uploadState == AmityFileUploadState.COMPLETE }
            }
        }
    }

    private fun setPostCreationEvent(event: AmityPostCreationEvent) {
        viewModelScope.launch {
            _postCreationEvent.value = event
            delay(500)
            _postCreationEvent.value = AmityPostCreationEvent.Initial
        }
    }

    private fun cancelUpload(uploadId: String?) {
        uploadId?.let {
            AmityFileService().cancelUpload(uploadId)
        }
    }


    private fun uploadMedia(postMedia: AmityPostMedia) {
        updateMediaTransCodingStatus(postMedia)
        AmityFileService()
            .run {
                when (postMedia.type) {
                    Type.IMAGE -> uploadImage(postMedia.url)
                    Type.VIDEO -> {
                        uploadVideo(
                            postMedia.url,
                            AmityContentFeedType.POST
                        )
                    }

                    Type.ClIP -> uploadClip(
                        postMedia.url,
                        AmityContentFeedType.POST
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        updateMediaUploadStatus(postMedia, it)
                    }
                    .ignoreElements()
                    .subscribe()
            }
    }

    private fun updateMediaTransCodingStatus(
        postMedia: AmityPostMedia,
    ) {
        val pm = AmityPostMedia(
            id = postMedia.id,
            uploadId = postMedia.uploadId,
            url = postMedia.url,
            uploadState = AmityFileUploadState.UPLOADING,
            currentProgress = 1,
            type = postMedia.type
        )
        updateList(pm)
    }

    private fun updateMediaUploadStatus(
        postMedia: AmityPostMedia,
        result: AmityUploadResult<AmityFileInfo>,
    ) {
        when (result) {
            is AmityUploadResult.PROGRESS -> {
                val pm = AmityPostMedia(
                    id = postMedia.id,
                    uploadId = postMedia.uploadId,
                    url = postMedia.url,
                    uploadState = AmityFileUploadState.UPLOADING,
                    currentProgress = result.getUploadInfo().getProgressPercentage(),
                    type = postMedia.type
                )
                updateList(pm)
            }

            is AmityUploadResult.COMPLETE -> {
                val file = result.getFile()
                uploadFailedMediaMap.remove(postMedia.url.toString())
                uploadedMediaMap[file.getFileId()] = file
                val media = if (file is AmityImage) {
                    AmityPostMedia.Media.Image(file)
                } else {
                    null
                }
                val pm = AmityPostMedia(
                    id = result.getFile().getFileId(),
                    uploadId = postMedia.uploadId,
                    url = postMedia.url,
                    uploadState = AmityFileUploadState.COMPLETE,
                    currentProgress = 100,
                    type = postMedia.type,
                    media = media,
                )
                updateList(pm)
            }

            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                if (mediaMap.containsKey(postMedia.url.toString())) {
                    val pm = AmityPostMedia(
                        postMedia.id,
                        postMedia.uploadId,
                        postMedia.url,
                        AmityFileUploadState.FAILED,
                        0,
                        postMedia.type
                    )
                    updateList(pm)

                    val firstTimeFailedToUpload =
                        !uploadFailedMediaMap.containsKey(pm.url.toString())
                    uploadFailedMediaMap[pm.url.toString()] = firstTimeFailedToUpload
                }
            }
        }
    }

    private fun updateList(postMedia: AmityPostMedia) {
        viewModelScope.launch {
            if (mediaMap.containsKey(postMedia.url.toString())) {
                mediaMap[postMedia.url.toString()] = postMedia
                _selectedMediaFiles.value = mediaMap.values.toList()
            }
        }
        checkAllMediaUploadedSuccessfully()
    }

    private fun getTargetForPostCreation(): AmityPost.Target {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                when (option.targetType) {
                    AmityPostTargetType.USER -> {
                        option.targetId?.let { AmityPost.Target.USER.create(it) }
                            ?: AmityPost.Target.USER.create(AmityCoreClient.getUserId())
                    }

                    AmityPostTargetType.COMMUNITY -> {
                        option.targetId?.let { AmityPost.Target.COMMUNITY.create(it) }
                    }
                }
            }

            else -> AmityPost.Target.USER.create(AmityCoreClient.getUserId())
        } ?: AmityPost.Target.USER.create(AmityCoreClient.getUserId())
    }

    private fun getTargetTypeForPostCreation(): AmityPost.TargetType {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                when (option.targetType) {
                    AmityPostTargetType.USER -> AmityPost.TargetType.USER
                    AmityPostTargetType.COMMUNITY -> AmityPost.TargetType.COMMUNITY
                }
            }
            else -> AmityPost.TargetType.USER
        }
    }

    private fun getTargetIdForPostCreation(): String {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                option.targetId ?: AmityCoreClient.getUserId()
            }
            else -> AmityCoreClient.getUserId()
        }
    }

    fun updateAltText(
        fileId: String,
        altText: String,
        onSuccess: (AmityImage) -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityCoreClient.newFileRepository().let { fileRepository ->
            fileRepository.updateAltText(fileId, altText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    refreshMedia(fileId, onSuccess)
                }
                .doOnError {
                    onError.invoke(it)
                }
                .subscribe()
        }
    }

    private fun refreshMedia(fileId: String, onSuccess: (AmityImage) -> Unit) {
        AmityCoreClient.newFileRepository().getFile(fileId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { rawFile: AmityRawFile ->
                if (rawFile.getFileType() == AmityFileType.IMAGE) {
                    val imageFromRaw: AmityImage? =
                        rawFile.asAmityImage() // null for incorrect file type
                    imageFromRaw?.let { image ->
                        updateMedia(image)
                        onSuccess.invoke(image)
                    }
                }
            }
            .subscribe()
    }

    private fun updateMedia(image: AmityImage) {
        val media = mediaMap.values.firstOrNull { it.id == image.getFileId() }
        media?.let {
            val pm = AmityPostMedia(
                id = image.getFileId(),
                uploadId = it.uploadId,
                url = it.url,
                uploadState = AmityFileUploadState.COMPLETE,
                currentProgress = 100,
                type = it.type,
                media = AmityPostMedia.Media.Image(image)
            )
            updateList(pm)
        }
    }
}

sealed class AmityPostAttachmentPickerEvent {
    object Initial : AmityPostAttachmentPickerEvent()
    object OpenImageOrVideoSelectionSheet : AmityPostAttachmentPickerEvent()
    object OpenImageCamera : AmityPostAttachmentPickerEvent()
    object OpenVideoCamera : AmityPostAttachmentPickerEvent()
    object OpenImagePicker : AmityPostAttachmentPickerEvent()
    object OpenVideoPicker : AmityPostAttachmentPickerEvent()
    object OpenFilePicker : AmityPostAttachmentPickerEvent()
    data class MaxUploadLimitReached(val mediaType: Type) : AmityPostAttachmentPickerEvent()
}

sealed class AmityPostAttachmentAllowedPickerType(
    val isEnabled: Boolean,
) {
    object All : AmityPostAttachmentAllowedPickerType(true)
    data class Image(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
    data class Video(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
    data class File(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
}

sealed class AmityPostCreationEvent {
    object Initial : AmityPostCreationEvent()
    object Creating : AmityPostCreationEvent()
    object Updating : AmityPostCreationEvent()
    class Failed(val throwable: Throwable) : AmityPostCreationEvent()
    object Success : AmityPostCreationEvent()
    object Pending : AmityPostCreationEvent()
}

class TextPostExceedException(val charLimit: Int) : Exception()

const val MEDIA_VIDEO_UPLOAD_LIMIT = 10
const val MEDIA_IMAGE_UPLOAD_LIMIT = 10

/**
 * Creates a combined metadata object containing both mentions and hashtags
 */
fun createMetadata(
    mentionedUsers: List<AmityMentionMetadata.USER>,
    hashtags: List<AmityHashtag>
): JsonObject? {
    // If no mentions or hashtags, return null
    if (mentionedUsers.isEmpty() && hashtags.isEmpty()) {
        return null
    }

    // Create hashtag metadata if hashtags exist
    val validHashtags = hashtags.takeIf { it.isNotEmpty() }?.let {
        // Check if we exceed the maximum of 30 hashtags
        if (hashtags.size > 30) hashtags.take(30) else hashtags
    }

    return AmityPostMetadataCreator(
        hashtags = validHashtags,
        mentionMetaData = mentionedUsers
    ).create()
}