package com.amity.socialcloud.uikit.community.compose.post.composer

import android.net.Uri
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

class AmityPostComposerPageViewModel : AmityMediaAttachmentViewModel() {

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

    private val _postCreationEvent by lazy {
        MutableStateFlow<AmityPostCreationEvent>(AmityPostCreationEvent.Initial)
    }
    val postCreationEvent get() = _postCreationEvent

    private val _selectedMediaFiles by lazy {
        MutableStateFlow<List<AmityPostMedia>>(emptyList())
    }
    val selectedMediaFiles get() = _selectedMediaFiles

    private val _isAllMediaSuccessfullyUploaded by lazy {
        MutableStateFlow(false)
    }
    val isAllMediaSuccessfullyUploaded get() = _isAllMediaSuccessfullyUploaded

    fun setComposerOptions(options: AmityPostComposerOptions) {
        this.options = options
        when (options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                this.community = options.community
            }

            is AmityPostComposerOptions.AmityPostComposerEditOptions -> {
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
//            is AmityPost.Data.FILE -> prepareFilePost(post)
                    else -> {}
                }

                setPostAttachmentAllowedPickerType(
                    when (post.getChildren().firstOrNull()?.getData()) {
                        is AmityPost.Data.IMAGE -> AmityPostAttachmentAllowedPickerType.Image
                        is AmityPost.Data.VIDEO -> AmityPostAttachmentAllowedPickerType.Video
                        else -> AmityPostAttachmentAllowedPickerType.All
                    }
                )
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
        onPostLoadFailed: (Throwable) -> Unit
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
        postChildren
            .map { getPostVideoThumbnail(it) }
            .forEach { image ->
                image?.let {
                    val videoThumbnail = mapImageToFeedImage(it, Type.VIDEO)
                    mediaMap[videoThumbnail.url.toString()] = videoThumbnail
                }
            }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun mapImageToFeedImage(image: AmityImage, type: Type): AmityPostMedia {
        return AmityPostMedia(
            id = image.getFileId(),
            uploadId = null,
            url = Uri.parse(image.getUrl(AmityImage.Size.MEDIUM)),
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = type
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

//    fun getImages(): MutableLiveData<MutableList<AmityPostMedia>> {
//        return postMediaLiveData
//    }

    fun updatePost(
        postText: String,
        mentionedUsers: List<AmityMentionMetadata.USER>,
    ) {
        setPostCreationEvent(AmityPostCreationEvent.Updating)

        val postId = _post.value!!.getPostId()
        val textData = _post.value!!.getData() as AmityPost.Data.TEXT

        deleteImageOrFileInPost()
            .andThen(Completable.defer {
                val attachments = _post.value?.getChildren()
                    ?.map { it.getData() }
                    ?: emptyList()
                val newAttachments = uploadedMediaMap.values.toList()
                updateParentPost(postId, postText, attachments, newAttachments, mentionedUsers)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                setPostCreationEvent(AmityPostCreationEvent.Success)
            }
            .doOnError {
                setPostCreationEvent(AmityPostCreationEvent.Failed)
            }
            .subscribe()
    }

    private fun deleteImageOrFileInPost(): Completable {
        return _post.value?.getChildren()?.let { ekoPostItems ->
            Observable.fromIterable(ekoPostItems)
                .map { ekoPostItem ->
                    when (val postData = ekoPostItem.getData()) {
                        is AmityPost.Data.IMAGE -> {
                            if (postData.getImage()?.getFileId() in deletedImageIds) {
                                ekoPostItem.delete()
                            } else {
                                Completable.complete()
                            }
                        }

                        is AmityPost.Data.VIDEO -> {
                            if (postData.getThumbnailImage()?.getFileId() in deletedImageIds) {
                                ekoPostItem.delete()
                            } else {
                                Completable.complete()
                            }
                        }

                        else -> {
                            Completable.complete()
                        }
                    }
                }.ignoreElements()
        } ?: kotlin.run {
            Completable.complete()
        }
    }

    private fun updateParentPost(
        postId: String,
        postText: String,
        attachments: List<AmityPost.Data>,
        newAttachments: List<AmityFileInfo>,
        userMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        val attachmentType: Type? =
            attachments.firstOrNull()?.let {
                when (it) {
                    is AmityPost.Data.IMAGE -> {
                        if (it.getImage()?.getFileId() in deletedImageIds) {
                            null
                        } else {
                            Type.IMAGE
                        }
                    }

                    is AmityPost.Data.VIDEO -> {
                        if (it.getThumbnailImage()?.getFileId() in deletedImageIds) {
                            null
                        } else {
                            Type.VIDEO
                        }
                    }

                    else -> null
                }
            }.let {
                it ?: newAttachments.firstOrNull()?.let { newAttachment ->
                    when (newAttachment) {
                        is AmityImage -> Type.IMAGE
                        is AmityVideo -> Type.VIDEO
                        else -> null
                    }
                }
            }

        val postEditor = AmitySocialClient.newPostRepository()
            .editPost(postId = postId)
            .text(postText)

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
                    .attachments(*emptyList<AmityImage>().toTypedArray())
            }
        }

        postEditor
            .metadata(AmityMentionMetadataCreator(userMentions).create())
            .mentionUsers(userMentions.map { it.getUserId() })

        return postEditor
            .build()
            .apply()
    }

    fun createPost(
        postText: String,
        mentionedUsers: List<AmityMentionMetadata.USER>
    ) {
        setPostCreationEvent(AmityPostCreationEvent.Creating)
        val target = getTargetForPostCreation()
        val metadata = mentionedUsers.takeIf { it.isNotEmpty() }?.let {
            AmityMentionMetadataCreator(mentionedUsers).create()
        }
        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

        when {
            isUploadedImageMedia() -> {
                val orderById =
                    mediaMap.values.withIndex().associate { it.value.id to it.index }
                val sortedImages =
                    uploadedMediaMap.values.sortedBy {
                        orderById[it.getFileId()]
                    }.map {
                        it as AmityImage
                    }.toSet()

                createPostTextAndImages(
                    postText = postText,
                    images = sortedImages,
                    target = target,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                )
            }

            isUploadedVideoMedia() -> {
                val videos = uploadedMediaMap.values.toList().map {
                    it as AmityVideo
                }.toSet()

                createPostTextAndVideos(
                    postText = postText,
                    videos = videos,
                    target = target,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                )
            }

            // TODO: 16/6/24 create file post
            else -> {
                createPostText(
                    postText = postText,
                    target = target,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
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
                setPostCreationEvent(AmityPostCreationEvent.Failed)
            }
            .subscribe()
    }

    private fun createPostText(
        postText: String,
        target: AmityPost.Target,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createTextPost(
                target = target,
                text = postText,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
            )
    }

    private fun createPostTextAndImages(
        postText: String,
        images: Set<AmityImage>,
        target: AmityPost.Target,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createImagePost(
                target = target,
                text = postText,
                images = images,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
            )
    }

    private fun createPostTextAndVideos(
        postText: String,
        videos: Set<AmityVideo>,
        target: AmityPost.Target,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createVideoPost(
                target = target,
                text = postText,
                videos = videos,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
            )
    }

    fun addMedia(medias: List<Uri>, mediaType: Type) {
        if (medias.size + mediaMap.size > 10) {
            setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.MaxUploadLimitReached)
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
                    Type.IMAGE -> AmityPostAttachmentAllowedPickerType.Image
                    Type.VIDEO -> AmityPostAttachmentAllowedPickerType.Video
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
        }
    }

    fun isUploadedImageMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityImage }
    }

    fun isUploadedVideoMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityVideo }
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
                false
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
        result: AmityUploadResult<AmityFileInfo>
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
                uploadFailedMediaMap.remove(postMedia.url.toString())
                uploadedMediaMap[result.getFile().getFileId()] = result.getFile()

                val pm = AmityPostMedia(
                    id = result.getFile().getFileId(),
                    uploadId = postMedia.uploadId,
                    url = postMedia.url,
                    uploadState = AmityFileUploadState.COMPLETE,
                    currentProgress = 100,
                    type = postMedia.type
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
}

sealed class AmityPostAttachmentPickerEvent {
    object Initial : AmityPostAttachmentPickerEvent()
    object OpenImageOrVideoSelectionSheet : AmityPostAttachmentPickerEvent()
    object OpenImageCamera : AmityPostAttachmentPickerEvent()
    object OpenVideoCamera : AmityPostAttachmentPickerEvent()
    object OpenImagePicker : AmityPostAttachmentPickerEvent()
    object OpenVideoPicker : AmityPostAttachmentPickerEvent()
    object OpenFilePicker : AmityPostAttachmentPickerEvent()
    object MaxUploadLimitReached : AmityPostAttachmentPickerEvent()
}

sealed class AmityPostAttachmentAllowedPickerType {
    object All : AmityPostAttachmentAllowedPickerType()
    object Image : AmityPostAttachmentAllowedPickerType()
    object Video : AmityPostAttachmentAllowedPickerType()
    object File : AmityPostAttachmentAllowedPickerType()
}

sealed class AmityPostCreationEvent {
    object Initial : AmityPostCreationEvent()
    object Creating : AmityPostCreationEvent()
    object Updating : AmityPostCreationEvent()
    object Failed : AmityPostCreationEvent()
    object Success : AmityPostCreationEvent()
    object Pending : AmityPostCreationEvent()
}