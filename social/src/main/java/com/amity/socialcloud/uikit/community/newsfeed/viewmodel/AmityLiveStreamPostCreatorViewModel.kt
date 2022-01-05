package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import android.net.Uri
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.core.file.AmityUploadResult
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.video.AmityVideoClient
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.sdk.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AmityLiveStreamPostCreatorViewModel :
    AmityBaseViewModel() {

    var communityId: String? = null
    var thumbnailId: String? = null
    var createdPostId: String? = null

    fun uploadThumbnail(
        uri: Uri,
        onUploading: () -> Unit,
        onUploadCompleted: () -> Unit,
        onUploadFailed: () -> Unit
    ): Completable {
        onUploading.invoke()
        return AmityCoreClient.newFileRepository().uploadImage(uri)
            .build()
            .transfer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { handleUploadState(it, onUploadCompleted, onUploadFailed) }
            .doOnError { onUploadFailed.invoke() }
            .ignoreElements()
    }

    fun removeThumbnail() {
        thumbnailId = null
    }

    fun createLiveStreamingPost(
        title: String,
        description: String,
        onCreateCompleted: (streamId: String?) -> Unit,
        onCreateFailed: () -> Unit
    ): Completable {
        var streamId: String? = null
        return createStream(title, description)
            .doOnSuccess { streamId = it.getStreamId() }
            .flatMapCompletable { Completable.defer { createPost(it) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onCreateCompleted.invoke(streamId) }
            .doOnError { onCreateFailed.invoke() }
    }

    fun getTargetProfile(onProfileLoaded: (title: String?, imageUrl: String?) -> Unit): Completable {
        return if (communityId.isNullOrEmpty()) {
            getCurrentUser(onProfileLoaded)
        } else {
            getCommunity(onProfileLoaded)
        }
    }

    private fun getCommunity(onProfileLoaded: (title: String?, imageUrl: String?) -> Unit): Completable {
        return AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId!!)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onProfileLoaded(
                    it.getDisplayName(),
                    it.getAvatar()?.getUrl(AmityImage.Size.SMALL)
                )
            }
            .ignoreElement()
    }

    private fun getCurrentUser(onProfileLoaded: (title: String?, imageUrl: String?) -> Unit): Completable {
        return AmityCoreClient.newUserRepository()
            .getCurrentUser()
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onProfileLoaded(it.getDisplayName(), it.getAvatar()?.getUrl(AmityImage.Size.SMALL))
            }
            .ignoreElement()
    }

    private fun createStream(title: String, description: String): Single<AmityStream> {
        return AmityVideoClient.newStreamRepository()
            .createVideoStream(
                title = title,
                description = description,
                resolution = AmityBroadcastResolution.HD_720P,
                thumbnailFileId = thumbnailId
            )
    }

    private fun createPost(
        stream: AmityStream,
    ): Completable {
        return if (communityId.isNullOrEmpty()) {
            createUserSelfPost(
                stream = stream
            )
        } else {
            createCommunityPost(
                stream = stream
            )
        }
    }

    private fun createCommunityPost(stream: AmityStream): Completable {
        communityId?.let { communityId ->
            return AmitySocialClient.newPostRepository()
                .createPost()
                .targetCommunity(communityId = communityId)
                .liveStream(streamId = stream.getStreamId())
                .text(text = generatePostText(stream))
                .build()
                .post()
                .doOnSuccess { createdPostId = it.getPostId() }
                .ignoreElement()
        }
        return Completable.error(NoTargetException)
    }

    private fun createUserSelfPost(stream: AmityStream): Completable {
        return AmitySocialClient.newPostRepository()
            .createPost()
            .targetMe()
            .liveStream(streamId = stream.getStreamId())
            .text(text = generatePostText(stream))
            .build()
            .post()
            .doOnSuccess { createdPostId = it.getPostId() }
            .ignoreElement()
    }

    private fun generatePostText(stream: AmityStream): String {
        return if (stream.getTitle().isNullOrBlank()
            && stream.getDescription().isNullOrBlank()
        ) {
            ""
        } else {
            stream.getTitle().plus("\n\n").plus(stream.getDescription())
        }
    }

    private fun handleUploadState(
        uploadResult: AmityUploadResult<AmityImage>,
        onUploadCompleted: () -> Unit,
        onUploadFailed: () -> Unit
    ) {
        when (uploadResult) {
            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> onUploadFailed.invoke()
            is AmityUploadResult.COMPLETE -> {
                onUploadCompleted.invoke()
                thumbnailId = uploadResult.getFile().getFileId()
            }
        }
    }

    object NoTargetException : Exception("No target type or target id")
}
