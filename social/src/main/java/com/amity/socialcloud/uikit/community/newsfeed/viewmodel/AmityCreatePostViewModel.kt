package com.amity.socialcloud.uikit.community.newsfeed.viewmodel


import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.AmityUserRepository
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityFileInfo
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.common.AmityFileUtils
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class AmityCreatePostViewModel : AmityBaseViewModel() {

    private val TAG = AmityCreatePostViewModel::class.java.canonicalName
    private val postRepository = AmitySocialClient.newPostRepository()
    private val communityRepository = AmitySocialClient.newCommunityRepository()
    private val userRepository: AmityUserRepository = AmityCoreClient.newUserRepository()

    var postId: String? = null
    var community: AmityCommunity? = null
    var postText: CharSequence? = null

    private var post: AmityPost? = null

    private val postMediaLiveData = MutableLiveData<MutableList<PostMedia>>()
    private val imageMap = LinkedHashMap<String, PostMedia>()
    private val uploadedMediaMap = LinkedHashMap<String, AmityFileInfo>()
    private val deletedImageIds = mutableListOf<String>()
    private val uploadFailedMediaMap = LinkedHashMap<String, Boolean>()

    private val filesLiveData = MutableLiveData<MutableList<AmityFileAttachment>>()
    private val filesMap = LinkedHashMap<String, AmityFileAttachment>()
    private val uploadedFilesMap = LinkedHashMap<String, AmityFile>()
    private val deletedFileIds = mutableListOf<String>()
    private val uploadFailedFile = LinkedHashMap<String, Boolean>()


    fun preparePostData(post: AmityPost) {
        this.community = (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
        this.post = post
        when (post.getData()) {
            is AmityPost.Data.TEXT -> prepareTextPost(post)
            is AmityPost.Data.IMAGE -> prepareImagePost(post)
            is AmityPost.Data.VIDEO -> prepareVideoPost(post)
            is AmityPost.Data.FILE -> prepareFilePost(post)
            else -> {}
        }
    }

    private fun prepareTextPost(post: AmityPost) {
        postText = getPostText(post)
        setUpPostTextWithImagesOrFiles(post)
    }

    private fun prepareImagePost(post: AmityPost) {
        getPostImage(post)?.let {
            val feedImage = mapImageToFeedImage(it)
            imageMap[feedImage.url.toString()] = feedImage
        }
        postMediaLiveData.value = imageMap.values.toMutableList()
    }

    private fun prepareVideoPost(post: AmityPost) {
        getPostVideoThumbnail(post)?.let {
            val videoPost = mapImageToFeedImage(it)
            imageMap[videoPost.url.toString()] = videoPost
        }
        postMediaLiveData.value = imageMap.values.toMutableList()
    }

    private fun prepareFilePost(post: AmityPost) {
        getPostFile(post)?.let {
            val attachment = mapFileToFileAttachment(it)
            filesMap[attachment.uri.toString()] = attachment
        }
        filesLiveData.value = filesMap.values.toMutableList()
    }

    private fun setUpPostTextWithImagesOrFiles(post: AmityPost) {
        val postChildren = post.getChildren()
        if (postChildren.isNotEmpty()) {
            when (postChildren.first().getData()) {
                is AmityPost.Data.IMAGE -> setupImagePost(postChildren)
                is AmityPost.Data.VIDEO -> setupVideoPost(postChildren)
                is AmityPost.Data.FILE -> setupFilePost(postChildren)
                else -> {}
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun searchUsersMention(
        keyword: String,
        onResult: (users: PagingData<AmityUser>) -> Unit
    ): Completable {
        return userRepository.searchUsers(keyword)
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    @ExperimentalPagingApi
    fun searchCommunityUsersMention(
        communityId: String,
        keyword: String,
        onResult: (users: PagingData<AmityCommunityMember>) -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityCommunityMembership.MEMBER))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    private fun setupImagePost(postChildren: List<AmityPost>) {
        postChildren
            .map { getPostImage(it) }
            .forEach { image ->
                image?.let {
                    val feedImage = mapImageToFeedImage(it)
                    imageMap[feedImage.url.toString()] = feedImage
                }
            }
        postMediaLiveData.value = imageMap.values.toMutableList()
    }

    private fun setupVideoPost(postChildren: List<AmityPost>) {
        postChildren
            .map { getPostVideoThumbnail(it) }
            .forEach { image ->
                image?.let {
                    val videoThumbnail = mapImageToFeedImage(it)
                    imageMap[videoThumbnail.url.toString()] = videoThumbnail
                }
            }
        postMediaLiveData.value = imageMap.values.toMutableList()
    }

    private fun setupFilePost(postChildren: List<AmityPost>) {
        postChildren
            .map { getPostFile(it) }
            .forEach { file ->
                file?.let {
                    val attachment = mapFileToFileAttachment(it)
                    filesMap[attachment.uri.toString()] = attachment
                }
            }
        filesLiveData.value = filesMap.values.toMutableList()
    }

    private fun mapFileToFileAttachment(ekoFile: AmityFile): AmityFileAttachment {
        val fileSize = ekoFile.getFileSize().toLong()
        return AmityFileAttachment(
            ekoFile.getFileId(),
            null,
            ekoFile.getFileName(),
            fileSize,
            Uri.parse(ekoFile.getUrl()),
            AmityFileUtils.humanReadableByteCount(fileSize, true)!!,
            ekoFile.getMimeType(),
            FileUploadState.COMPLETE,
            100
        )
    }

    private fun mapFileToFileAttachment(
        fileAttachment: AmityFileAttachment,
        ekoFile: AmityFile
    ): AmityFileAttachment {
        val fileSize = ekoFile.getFileSize().toLong()
        return AmityFileAttachment(
            ekoFile.getFileId(),
            fileAttachment.uploadId,
            ekoFile.getFileName(),
            fileSize,
            fileAttachment.uri,
            AmityFileUtils.humanReadableByteCount(fileSize, true)!!,
            ekoFile.getMimeType(),
            FileUploadState.COMPLETE,
            100
        )
    }

    fun observeCommunity(communityId: String): Completable {
        return communityRepository.getCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                this.community = it
            }
            .ignoreElements()
    }

    fun getPost(): AmityPost? {
        return post
    }

    fun getPostDetails(
        postId: String,
        onPostLoading: () -> Unit,
        onPostLoaded: (AmityPost) -> Unit,
        onPostLoadFailed: (Throwable) -> Unit
    ): Completable {
        return postRepository.getPost(postId)
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

    private fun getPostText(newsFeed: AmityPost): CharSequence? {
        val textData = newsFeed.getData() as? AmityPost.Data.TEXT
        return textData?.getText()
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

    fun getImages(): MutableLiveData<MutableList<PostMedia>> {
        return postMediaLiveData
    }

    fun updatePostText(
        postText: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onUpdateSuccess: (AmityPost?) -> Unit,
        onUpdateFailed: (Throwable) -> Unit
    ): Completable {
        val textData = post!!.getData() as AmityPost.Data.TEXT
        return deleteImageOrFileInPost()
            .andThen(Completable.defer {
                val attachments = post?.getChildren()
                    ?.map { it.getData() }
                    ?: emptyList()
                updateParentPost(postText, textData, attachments, userMentions)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onUpdateSuccess.invoke(getPost()) }
            .doOnError { onUpdateFailed.invoke(it) }
    }

    private fun updateParentPost(
        postText: String,
        textData: AmityPost.Data.TEXT,
        attachments: List<AmityPost.Data>,
        userMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        val firstAttachment = attachments.firstOrNull()
        val videos = attachments.mapNotNull {
            it as? AmityPost.Data.VIDEO
        }.filter {
            !(it.getThumbnailImage()?.getFileId()?.let(deletedImageIds::contains) ?: false)
        }.map {
            it.getVideo().blockingGet()
        }
        val images = attachments
            .mapNotNull { (it as? AmityPost.Data.IMAGE)?.getImage() }
            .filter { !deletedImageIds.contains(it.getFileId()) }
        val files = attachments
            .mapNotNull { (it as? AmityPost.Data.FILE)?.getFile() }
            .filter { !deletedFileIds.contains(it.getFileId()) }
        val postEditor = when (firstAttachment) {
            is AmityPost.Data.VIDEO -> {
                textData.edit().videoAttachments(videos).text(postText)
            }
            is AmityPost.Data.IMAGE -> {
                textData.edit().imageAttachments(images).text(postText)
            }
            is AmityPost.Data.FILE -> {
                textData.edit().fileAttachments(files).text(postText)
            }
            else -> {
                textData.edit().text(postText)
            }
        }
        postEditor
            .metadata(AmityMentionMetadataCreator(userMentions).create())
            .mentionUsers(userMentions.map { it.getUserId() })
        return postEditor
            .build()
            .apply()
    }

    private fun createPostText(
        postText: String,
        userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        return if (community != null) {
            val postTextCreator = postRepository.createPost()
                .targetCommunity(community!!.getCommunityId()).text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        } else {
            val postTextCreator = postRepository.createPost().targetMe().text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        }
    }

    private fun createPostTextAndImages(
        postText: String,
        images: List<AmityImage>,
        userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        val imageArray = images.toTypedArray()
        return if (community != null) {
            val postTextCreator = postRepository.createPost()
                .targetCommunity(community!!.getCommunityId()).image(*imageArray).text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        } else {
            val postTextCreator = postRepository.createPost()
                .targetMe().image(*imageArray).text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        }
    }

    private fun createPostTextAndVideos(
        postText: String,
        videos: List<AmityVideo>,
        userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        val videoArray = videos.toTypedArray()
        return if (community != null) {
            val postTextCreator = postRepository.createPost()
                .targetCommunity(community!!.getCommunityId()).video(*videoArray).text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        } else {
            val postTextCreator = postRepository.createPost()
                .targetMe().video(*videoArray).text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        }
    }

    private fun createPostTextAndFiles(
        postText: String,
        files: List<AmityFile>,
        userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        val fileArray = files.toTypedArray()
        return if (community != null) {
            val postTextCreator = postRepository.createPost()
                .targetCommunity(community!!.getCommunityId())
                .file(*fileArray)
                .text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        } else {
            val postTextCreator = postRepository.createPost()
                .targetMe()
                .file(*fileArray)
                .text(postText)
            if (userMentions.isNotEmpty()) {
                postTextCreator
                    .metadata(AmityMentionMetadataCreator(userMentions).create())
                    .mentionUsers(userMentions.map { it.getUserId() })
            }
            postTextCreator.build().post()
        }
    }
    
    fun uploadMediaList(postMedia: List<PostMedia>): Completable {
        return Flowable.fromIterable(postMedia)
                .flatMapCompletable(::uploadMedia)
    }

    private fun uploadMedia(postMedia: PostMedia): Completable {
        when (postMedia.type) {
            PostMedia.Type.IMAGE -> {
                return AmityFileService()
                    .uploadImage(postMedia.url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { updateMediaUploadStatus(postMedia, it) }
                    .ignoreElements()
            }
            PostMedia.Type.VIDEO -> {
                return AmityFileService()
                    .uploadVideo(postMedia.url, AmityContentFeedType.POST)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { updateMediaUploadStatus(postMedia, it) }
                    .ignoreElements()
            }
            else -> {
                return Completable.error(Throwable("The media is not video or image"))
            }
        }
    }


    fun uploadFile(attachment: AmityFileAttachment): Flowable<AmityUploadResult<AmityFile>> {
        return AmityFileService().uploadFile(attachment.uri)
    }

    private fun deleteImageOrFileInPost(
    ): Completable {
        return post?.getChildren()?.let { ekoPostItems ->
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
                        is AmityPost.Data.FILE -> {
                            if (postData.getFile()?.getFileId() in deletedFileIds) {
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
        } ?: kotlin.run {
            Completable.complete()
        }
    }

    fun addMedia(images: List<Uri>, mediaType: PostMedia.Type): List<PostMedia> {
        val uploadingPostMediaList = arrayListOf<PostMedia>()
        images.forEach { uriItem ->
            if (!imageMap.containsKey(uriItem.toString())) {
                val postMedia = PostMedia(UUID.randomUUID().toString(), uriItem, mediaType)
                imageMap[uriItem.toString()] = postMedia
                uploadingPostMediaList.add(postMedia)
            }
        }
        val currentImages = imageMap.values.toMutableList()
        postMediaLiveData.value = currentImages
        return uploadingPostMediaList.filter { it.id == null }
    }

    fun removeMedia(postMedia: PostMedia) {
        imageMap.remove(postMedia.url.toString())
        uploadFailedMediaMap.remove(postMedia.url.toString())
        cancelUpload(postMedia.uploadId)


        if (postMedia.id != null) {
            //In case update post we want to keep track of the images to delete
            if (post != null) {
                deletedImageIds.add(postMedia.id!!)
            } else {
                uploadedMediaMap.remove(postMedia.id!!)
            }
        }
        postMediaLiveData.value = imageMap.values.toMutableList()
        triggerImageRemovedEvent()

    }

    private fun cancelUpload(uploadId: String?) {
        if (uploadId != null) {
            Log.d(TAG, "cancel file upload $uploadId")
            AmityFileService().cancelUpload(uploadId)
        }
    }

    private fun triggerImageRemovedEvent() {
        triggerEvent(
            AmityEventIdentifier.CREATE_POST_IMAGE_REMOVED,
            postMediaLiveData.value?.size ?: 0
        )
    }

    private fun updateMediaUploadStatus(
        postMedia: PostMedia,
        imageUpload: AmityUploadResult<AmityFileInfo>
    ) {
        when (imageUpload) {
            is AmityUploadResult.PROGRESS -> {
                val updatedFeedImage = PostMedia(
                    postMedia.id,
                    postMedia.uploadId,
                    postMedia.url,
                    FileUploadState.UPLOADING,
                    imageUpload.getUploadInfo().getProgressPercentage(),
                    postMedia.type
                )
                updateList(updatedFeedImage)
            }
            is AmityUploadResult.COMPLETE -> {
                uploadFailedMediaMap.remove(postMedia.url.toString())
                uploadedMediaMap[imageUpload.getFile().getFileId()] = imageUpload.getFile()
                val updatedFeedImage = PostMedia(
                    imageUpload.getFile().getFileId(),
                    postMedia.uploadId,
                    postMedia.url,
                    FileUploadState.COMPLETE,
                    100,
                    postMedia.type
                )
                updateList(updatedFeedImage)
                if (!hasPendingImageToUpload() && hasFirstTimeFailedToUploadImages()) {
                    triggerImageUploadFailedEvent()
                }
            }
            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                Log.d(TAG, "Image upload error " + postMedia.url)
                if (imageMap.containsKey(postMedia.url.toString())) {

                    val updatedFeedImage = PostMedia(
                        postMedia.id,
                        postMedia.uploadId,
                        postMedia.url,
                        FileUploadState.FAILED,
                        0,
                        postMedia.type
                    )
                    val firstTimeFailedToUpload =
                        !uploadFailedMediaMap.containsKey(updatedFeedImage.url.toString())
                    uploadFailedMediaMap[updatedFeedImage.url.toString()] = firstTimeFailedToUpload
                    updateList(updatedFeedImage)
                    if (!hasPendingImageToUpload() && hasFirstTimeFailedToUploadImages()) {
                        triggerImageUploadFailedEvent()
                    }
                }

            }
        }
    }

    private fun triggerImageUploadFailedEvent() {
        uploadFailedMediaMap.keys.forEach {
            uploadFailedMediaMap[it] = false
        }
        triggerEvent(AmityEventIdentifier.FAILED_TO_UPLOAD_IMAGE)
    }

    private fun updateList(postMedia: PostMedia) {
        if (imageMap.containsKey(postMedia.url.toString())) {
            imageMap[postMedia.url.toString()] = postMedia
            postMediaLiveData.value = imageMap.values.toMutableList()
        }
    }

    private fun updateList(fileAttachment: AmityFileAttachment) {
        if (filesMap.containsKey(fileAttachment.uri.toString())) {
            filesMap[fileAttachment.uri.toString()] = fileAttachment
            filesLiveData.value = filesMap.values.toMutableList()
        }
    }

    fun createPost(
        postText: String,
        userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        val orderById = imageMap.values.withIndex().associate { it.value.id to it.index }
        val sortedImages = uploadedMediaMap.values.toList().sortedBy { orderById[it.getFileId()] }
        return when {
            isUploadedImageMedia() -> {
                createPostTextAndImages(
                    postText,
                    sortedImages as List<AmityImage>,
                    userMentions
                )
            }
            isUploadedVideoMedia() -> {
                createPostTextAndVideos(
                    postText,
                    uploadedMediaMap.values.toList() as List<AmityVideo>,
                    userMentions
                )
            }
            //TODO isFileMedia
            uploadedFilesMap.isNotEmpty() -> {
                createPostTextAndFiles(postText, uploadedFilesMap.values.toList(), userMentions)
            }
            else -> {
                createPostText(postText, userMentions)
            }
        }
    }

    fun isUploadedImageMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityImage }
    }

    fun isUploadedVideoMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityVideo }
    }

    fun isUploadingImageMedia(): Boolean {
        return imageMap.values.any { it.type == PostMedia.Type.IMAGE }
    }

    fun isUploadingVideoMedia(): Boolean {
        return imageMap.values.any { it.type == PostMedia.Type.VIDEO }
    }

    private fun mapImageToFeedImage(ekoImage: AmityImage): PostMedia {
        return PostMedia(
            ekoImage.getFileId(),
            null,
            Uri.parse(ekoImage.getUrl(AmityImage.Size.MEDIUM)),
            FileUploadState.COMPLETE,
            100
        )
    }

    fun hasPendingImageToUpload(): Boolean {
        val totalImageProcessed = uploadedMediaMap.size + uploadFailedMediaMap.size
        return if (post == null)
            postMediaLiveData.value != null && totalImageProcessed < postMediaLiveData.value!!.size
        else
            false
    }

    fun hasPendingFileToUpload(): Boolean {
        val totalFileProcessed = uploadedFilesMap.size + uploadFailedFile.size
        return if (post == null)
            return filesLiveData.value != null && filesLiveData.value!!.size != totalFileProcessed
        else
            false
    }


    fun hasUpdateOnPost(postText: String): Boolean {
        if (post == null)
            return false
        if (postText.isEmpty() && postMediaLiveData.value.isNullOrEmpty() && filesLiveData.value.isNullOrEmpty()) {
            return false
        }
        if (postText != getPostText(post!!))
            return true
        if (deletedImageIds.size > 0)
            return true
        if (deletedFileIds.size > 0)
            return true

        return false
    }

    fun hasFailedToUploadImages(): Boolean {
        return uploadFailedMediaMap.size > 0
    }

    fun hasFailedToUploadFiles(): Boolean {
        return uploadFailedFile.size > 0
    }

    fun getFiles(): MutableLiveData<MutableList<AmityFileAttachment>> {
        return filesLiveData
    }


    fun addFiles(fileAttachments: MutableList<AmityFileAttachment>): MutableList<AmityFileAttachment> {
        val addedFiles = mutableListOf<AmityFileAttachment>()
        fileAttachments.forEach {
            val key = it.uri.toString()
            if (!isDuplicateFile(it)) {
                filesMap[key] = it
                uploadedFilesMap.remove(key)
                addedFiles.add(it)
            }
        }
        filesLiveData.value = filesMap.values.toMutableList()
        return addedFiles
    }

    private fun isDuplicateFile(fileAttachment: AmityFileAttachment): Boolean {
        val result = filesMap.values.filter {
            it.uri == fileAttachment.uri && it.uploadState != FileUploadState.FAILED
        }
        return result.isNotEmpty()
    }


    fun removeFile(file: AmityFileAttachment) {
        filesMap.remove(file.uri.toString())
        uploadFailedFile.remove(file.uri.toString())
        cancelUpload(file.uploadId)

        if (file.id != null) {
            //In case update post we want to keep track of the images to delete
            if (post != null) {
                deletedFileIds.add(file.id)
            } else {
                uploadedFilesMap.remove(file.id)
            }
        }
        filesLiveData.value = filesMap.values.toMutableList()
    }

    fun updateFileUploadStatus(
        fileAttachment: AmityFileAttachment,
        fileUpload: AmityUploadResult<AmityFile>
    ) {
        when (fileUpload) {
            is AmityUploadResult.PROGRESS -> {
                Log.d(
                    TAG,
                    "File upload progress " + fileAttachment.name + fileUpload.getUploadInfo()
                        .getProgressPercentage()
                )
                val updatedFileAttachment = AmityFileAttachment(
                    fileAttachment.id,
                    fileAttachment.uploadId,
                    fileAttachment.name,
                    fileAttachment.size,
                    fileAttachment.uri,
                    fileAttachment.readableSize,
                    fileAttachment.mimeType,
                    FileUploadState.UPLOADING,
                    fileUpload.getUploadInfo().getProgressPercentage()
                )
                updateList(updatedFileAttachment)
            }
            is AmityUploadResult.COMPLETE -> {
                Log.d(TAG, "File upload Complete " + fileAttachment.name)
                uploadedFilesMap[fileUpload.getFile().getFileId()] = fileUpload.getFile()
                val updatedFileAttachment =
                    mapFileToFileAttachment(fileAttachment, fileUpload.getFile())
                updateList(updatedFileAttachment)
                if (!hasPendingFileToUpload() && hasFirstTimeFailedToUploadFiles()) {
                    triggerFileUploadFailedEvent()
                }
            }
            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                Log.d(TAG, "File upload error " + fileAttachment.name)
                if (filesMap.containsKey(fileAttachment.uri.toString())) {
                    val updatedFileAttachment = AmityFileAttachment(
                        fileAttachment.id,
                        fileAttachment.uploadId,
                        fileAttachment.name,
                        fileAttachment.size,
                        fileAttachment.uri,
                        fileAttachment.readableSize,
                        fileAttachment.mimeType,
                        FileUploadState.FAILED,
                        0
                    )
                    val firstTimeFailedToUpload =
                        !uploadFailedFile.containsKey(fileAttachment.uri.toString())

                    uploadFailedFile[updatedFileAttachment.uri.toString()] = firstTimeFailedToUpload
                    updateList(updatedFileAttachment)
                    if (!hasPendingFileToUpload() && hasFirstTimeFailedToUploadFiles()) {
                        triggerFileUploadFailedEvent()
                    }
                }

            }
        }
    }

    private fun hasFirstTimeFailedToUploadFiles(): Boolean {
        return uploadFailedFile.values.contains(true)
    }

    private fun hasFirstTimeFailedToUploadImages(): Boolean {
        return uploadFailedMediaMap.values.contains(true)
    }


    private fun triggerFileUploadFailedEvent() {
        uploadFailedFile.keys.forEach {
            uploadFailedFile[it] = false
        }
        triggerEvent(AmityEventIdentifier.FAILED_TO_UPLOAD_FILES)
    }

    fun hasAttachments(): Boolean {
        if (filesLiveData.value != null)
            return filesLiveData.value!!.size > 0
        return false
    }

    fun hasImages(): Boolean {
        if (postMediaLiveData.value != null)
            return postMediaLiveData.value!!.size > 0
        return false
    }

    fun discardPost() {
        postMediaLiveData.value?.forEach {
            if (it.id == null && it.uploadId != null)
                cancelUpload(it.uploadId)
        }
        filesLiveData.value?.forEach {
            if (it.id == null && it.uploadId != null) {
                cancelUpload(it.uploadId)
            }
        }
    }

    fun checkReviewingPost(feedTye: AmityFeedType, showDialog: () -> Unit, closePage: () -> Unit) {
        if (feedTye == AmityFeedType.REVIEWING) {
            showDialog()
        } else {
            closePage()
        }
    }
}