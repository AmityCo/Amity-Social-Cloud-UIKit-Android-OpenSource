package com.amity.socialcloud.uikit.community.compose.community.setup

import android.net.Uri
import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.settings.AmityMembershipAcceptanceType
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class AmityCommunitySetupPageViewModel : AmityBaseViewModel() {

    var hasGlobalFeaturedPost: Boolean = false

    fun createCommunity(
        avatarUri: Uri,
        displayName: String,
        description: String,
        isPublic: Boolean,
        isDiscoverable: Boolean,
        requiresJoinApproval: Boolean,
        categoryIds: List<String>,
        userIds: List<String>,
        membershipAcceptanceType: AmityMembershipAcceptanceType = AmityMembershipAcceptanceType.AUTOMATIC,
        onSuccess: (AmityCommunity) -> Unit,
        onError: (String) -> Unit,
    ) {
        if (avatarUri == Uri.EMPTY) {
            createCommunity(
                avatar = null,
                displayName = displayName,
                description = description,
                isPublic = isPublic,
                isDiscoverable = isDiscoverable,
                requiresJoinApproval = requiresJoinApproval,
                categoryIds = categoryIds,
                userIds = userIds,
                membershipAcceptanceType = membershipAcceptanceType,
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            uploadAvatar(
                uri = avatarUri,
                onSuccess = { avatarImage ->
                    createCommunity(
                        avatar = avatarImage,
                        displayName = displayName,
                        description = description,
                        isPublic = isPublic,
                        isDiscoverable = isDiscoverable,
                        requiresJoinApproval = requiresJoinApproval,
                        categoryIds = categoryIds,
                        userIds = userIds,
                        membershipAcceptanceType = membershipAcceptanceType,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onError = onError
            )
        }
    }

    fun editCommunity(
        communityId: String,
        avatarUri: Uri,
        displayName: String,
        description: String,
        isPublic: Boolean,
        isDiscoverable: Boolean,
        requiresJoinApproval: Boolean,
        categoryIds: List<String>,
        onSuccess: (AmityCommunity) -> Unit,
        onError: (String) -> Unit,
    ) {
        if (avatarUri == Uri.EMPTY) {
            editCommunity(
                communityId = communityId,
                avatar = null,
                displayName = displayName,
                description = description,
                isPublic = isPublic,
                isDiscoverable = isDiscoverable,
                requiresJoinApproval = requiresJoinApproval,
                categoryIds = categoryIds,
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            uploadAvatar(
                uri = avatarUri,
                onSuccess = { avatarImage ->
                    editCommunity(
                        communityId = communityId,
                        avatar = avatarImage,
                        displayName = displayName,
                        description = description,
                        isPublic = isPublic,
                        isDiscoverable = isDiscoverable,
                        requiresJoinApproval = requiresJoinApproval,
                        categoryIds = categoryIds,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onError = onError
            )
        }
    }

    private fun uploadAvatar(
        uri: Uri,
        onSuccess: (AmityImage) -> Unit,
        onError: (String) -> Unit,
    ) {
        AmityCoreClient.newFileRepository()
            .uploadImage(uri)
            .doOnNext { uploadStatus ->
                when (uploadStatus) {
                    is AmityUploadResult.COMPLETE -> onSuccess(uploadStatus.getFile())
                    is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> onError("Failed to upload image")
                    else -> {}
                }
            }
            .doOnError {
                onError(it.message ?: "Failed to upload image")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun createCommunity(
        avatar: AmityImage?,
        displayName: String,
        description: String,
        isPublic: Boolean,
        isDiscoverable: Boolean,
        requiresJoinApproval: Boolean,
        categoryIds: List<String>,
        userIds: List<String>,
        membershipAcceptanceType: AmityMembershipAcceptanceType,
        onSuccess: (AmityCommunity) -> Unit,
        onError: (String) -> Unit,
    ) {
        AmitySocialClient.newCommunityRepository()
            .createCommunity(displayName, isDiscoverable, requiresJoinApproval)
            .description(description)
            .isPublic(isPublic)
            .categoryIds(categoryIds)
            .apply {
                if (avatar != null) avatar(avatar)
                if (membershipAcceptanceType == AmityMembershipAcceptanceType.AUTOMATIC) {
                    userIds(userIds)
                }
            }
            .build()
            .create()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError { onError(it.message ?: "Failed to create community") }
            .flatMapCompletable { community ->
                if (membershipAcceptanceType == AmityMembershipAcceptanceType.INVITATION && userIds.isNotEmpty()) {
                    community.createInvitations(userIds)
                } else {
                    Completable.complete()
                }
            }
            .subscribe()
    }

    private fun editCommunity(
        communityId: String,
        avatar: AmityImage?,
        displayName: String,
        description: String,
        isPublic: Boolean,
        isDiscoverable: Boolean,
        requiresJoinApproval: Boolean,
        categoryIds: List<String>,
        onSuccess: (AmityCommunity) -> Unit,
        onError: (String) -> Unit,
    ) {
        AmitySocialClient.newCommunityRepository()
            .editCommunity(
                communityId = communityId,
                isDiscoverable = isDiscoverable,
                requiresJoinApproval = requiresJoinApproval
            )
            .displayName(displayName)
            .description(description)
            .isPublic(isPublic)
            .categoryIds(categoryIds)
            .apply {
                if (avatar != null) avatar(avatar)
            }
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError { onError(it.message ?: "Failed to update community") }
            .subscribe()
    }

    fun observeGlobalFeaturedPost(communityId: String) {
        AmitySocialClient.newPostRepository()
            .getGlobalPinnedPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                hasGlobalFeaturedPost = it.any { pinnedPost ->
                    pinnedPost.post?.getTarget() is AmityPost.Target.COMMUNITY
                            && (pinnedPost.post?.getTarget() as AmityPost.Target.COMMUNITY).getCommunityId() == communityId
                }
            }
            .doOnError {

            }
            .subscribe()
    }

    fun getMembershipAcceptanceType(): Flowable<AmityMembershipAcceptanceType> {
        return AmitySocialClient.getSettings()
            .map { setting ->
                setting.getMembershipAcceptanceType()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}