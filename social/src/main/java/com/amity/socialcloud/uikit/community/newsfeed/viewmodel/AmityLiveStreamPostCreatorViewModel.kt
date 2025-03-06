package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.AmityUserRepository
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class AmityLiveStreamPostCreatorViewModel :
    AmityBaseViewModel() {

    var communityId: String? = null
    var thumbnailId: String? = null
    var createdPostId: String? = null
    var community: AmityCommunity? = null
    private val userRepository: AmityUserRepository = AmityCoreClient.newUserRepository()
    private val communityRepository = AmitySocialClient.newCommunityRepository()

    fun uploadThumbnail(
        uri: Uri,
        onUploading: () -> Unit,
        onUploadCompleted: () -> Unit,
        onUploadFailed: () -> Unit
    ): Completable {
        onUploading.invoke()
        return AmityFileService().uploadImage(uri)
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
        onCreateFailed: () -> Unit,
        descriptionUserMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        var streamId: String? = null
        return createStream(title, description)
            .doOnSuccess { streamId = it.getStreamId() }
            .flatMapCompletable { Completable.defer { createPost(it, descriptionUserMentions) } }
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
        return AmityCoreClient
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
            .createStream(
                title = title,
                description = description,
                resolution = AmityBroadcastResolution.HD_720P,
                thumbnailFileId = thumbnailId
            )
    }

    private fun createPost(
        stream: AmityStream,
        descriptionUserMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        val userMentions = getUserMentions(stream, descriptionUserMentions)
        return if (communityId.isNullOrEmpty()) {
            createUserSelfPost(
                stream = stream,
                userMentions = userMentions
            )
        } else {
            createCommunityPost(
                stream = stream,
                userMentions = userMentions
            )
        }
    }

    private fun getUserMentions(
        stream: AmityStream,
        descriptionUserMentions: List<AmityMentionMetadata.USER>
    ): List<AmityMentionMetadata.USER> {
        val titleLength = stream.getTitle()?.length?.plus(2) ?: 0
        return descriptionUserMentions.map {
            AmityMentionMetadata.USER(
                userId = it.getUserId(),
                index = it.getIndex() + titleLength,
                length = it.getLength()
            )
        }
    }

    private fun createCommunityPost(
        stream: AmityStream,
        userMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        communityId?.let { communityId ->
            return AmitySocialClient.newPostRepository()
                .createPost()
                .targetCommunity(communityId = communityId)
                .liveStream(streamId = stream.getStreamId())
                .text(text = generatePostText(stream))
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
                .build()
                .post()
                .doOnSuccess { createdPostId = it.getPostId() }
                .ignoreElement()
        }
        return Completable.error(NoTargetException)
    }

    private fun createUserSelfPost(
        stream: AmityStream,
        userMentions: List<AmityMentionMetadata.USER>
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .createPost()
            .targetMe()
            .liveStream(streamId = stream.getStreamId())
            .text(text = generatePostText(stream))
            .metadata(AmityMentionMetadataCreator(userMentions).create())
            .mentionUsers(userMentions.map { it.getUserId() })
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
            else -> {}
        }
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

    fun observeCommunity(communityId: String?): Completable {
        this.communityId = communityId
        return communityId?.let {
            communityRepository.getCommunity(communityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    this.community = it
                }
                .ignoreElements()
        } ?: Completable.complete()
    }

    object NoTargetException : Exception("No target type or target id")
}
