package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingTarget
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

interface PostViewModel {

    fun shouldShowPostOptions(post: AmityPost): Boolean {
        return (post.getFeedType() == AmityFeedType.PUBLISHED && !isPostReadOnly(post))
                || (post.getFeedType() == AmityFeedType.REVIEWING && post.getCreatorId() == AmityCoreClient.getUserId())
    }

    fun isPostReadOnly(post: AmityPost): Boolean {
        val target = post.getTarget()
        if (target is AmityPost.Target.COMMUNITY) {
            val community = target.getCommunity()
            return community?.isJoined() == false
        }
        return false
    }

    fun getPost(
        postId: String,
        onLoaded: (AmityPost) -> Unit,
        onError: () -> Unit
    ): Flowable<AmityPost> {
        return AmitySocialClient.newPostRepository().getPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onLoaded.invoke(it)
            }.doOnError {
                onError.invoke()
            }
    }

    fun addPostReaction(post: AmityPost): Completable {
        return AmityCoreClient.newReactionRepository()
            .addReaction(
                referenceType = AmityReactionReferenceType.POST,
                referenceId = post.getPostId(),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removePostReaction(post: AmityPost): Completable {
        return AmityCoreClient.newReactionRepository()
            .removeReaction(
                referenceType = AmityReactionReferenceType.POST,
                referenceId = post.getPostId(),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun reportPost(
        post: AmityPost,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .flagPost(
                postId = post.getPostId(),
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun unReportPost(
        post: AmityPost,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .unflagPost(
                postId = post.getPostId(),
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun deletePost(
        post: AmityPost,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .hardDeletePost(
                postId = post.getPostId(),
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun closePoll(
        pollId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newPollRepository()
            .closePoll(pollId = pollId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun approvePost(
        postId: String,
        onAlreadyApproved: () -> Unit,
        onError: () -> Unit,
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .approvePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                if (AmityError.from(it) == AmityError.FORBIDDEN_ERROR) {
                    onAlreadyApproved.invoke()
                } else {
                    onError.invoke()
                }
            }
    }

    fun declinePost(
        postId: String,
        onAlreadyDeclined: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .declinePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                if (AmityError.from(it) == AmityError.FORBIDDEN_ERROR) {
                    onAlreadyDeclined.invoke()
                } else {
                    onError.invoke()
                }
            }
    }

    fun getSharingOptionMenuItems(
        post: AmityPost,
        shareToMyFeed: () -> Unit,
        shareToGroupFeed: () -> Unit,
        shareToExternal: () -> Unit,
    ): List<BottomSheetMenuItem> {
        val items = arrayListOf<BottomSheetMenuItem>()
        val myFeed =
            BottomSheetMenuItem(null, null, R.string.amity_share_to_my_timeline, shareToMyFeed)
        val groupFeed =
            BottomSheetMenuItem(null, null, R.string.amity_share_to_group, shareToGroupFeed)
        val external = BottomSheetMenuItem(null, null, R.string.amity_more_options, shareToExternal)
        var possibleTargets = listOf<AmityPostSharingTarget>()
        val target = post.getTarget()
        when (target) {
            is AmityPost.Target.USER -> {
                if (target.getUser()?.getUserId() == AmityCoreClient.getUserId()) {
                    possibleTargets =
                        AmitySocialUISettings.postSharingSettings.myFeedPostSharingTarget
                } else {
                    possibleTargets =
                        AmitySocialUISettings.postSharingSettings.userFeedPostSharingTarget
                }
            }
            is AmityPost.Target.COMMUNITY -> {
                if (target.getCommunity()?.isPublic() != false) {
                    possibleTargets =
                        AmitySocialUISettings.postSharingSettings.publicCommunityPostSharingTarget
                } else {
                    possibleTargets =
                        AmitySocialUISettings.postSharingSettings.privateCommunityPostSharingTarget
                }
            }
            else -> {}
        }

        if (possibleTargets.contains(AmityPostSharingTarget.MyFeed)
            || (possibleTargets.contains(AmityPostSharingTarget.OriginFeed) && (((target as? AmityPost.Target.USER)?.getUser()
                ?.getUserId() ?: "") == AmityCoreClient.getUserId()))
        ) {
            items.add(myFeed)
        }

        if (possibleTargets.contains(AmityPostSharingTarget.PrivateCommunity)
            || possibleTargets.contains(AmityPostSharingTarget.PublicCommunity)
            || (possibleTargets.contains(AmityPostSharingTarget.OriginFeed) && (target is AmityPost.Target.COMMUNITY))
        ) {
            items.add(groupFeed)
        }

        if (possibleTargets.contains(AmityPostSharingTarget.External)) {
            items.add(external)
        }

        return items
    }

    fun getPostOptionMenuItems(
        post: AmityPost,
        editPost: () -> Unit,
        deletePost: () -> Unit,
        reportPost: () -> Unit,
        unReportPost: () -> Unit,
        closePoll: () -> Unit,
        deletePoll: () -> Unit
    ): List<BottomSheetMenuItem> {
        val items = arrayListOf<BottomSheetMenuItem>()
        val editPostMenuItem = BottomSheetMenuItem(null, null, R.string.amity_edit_post, editPost)
        val deletePostMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_delete_post, deletePost)
        val reportPostMenuItem = BottomSheetMenuItem(null, null, R.string.amity_report, reportPost)
        val unReportPostMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_undo_report, unReportPost)
        val closePollMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_close_poll, closePoll)
        val deletePollMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_delete_poll, deletePoll)

        if (post.getCreatorId() == AmityCoreClient.getUserId()) {
            if (post.getChildren().getOrNull(0)?.getType() == AmityPost.DataType.POLL) {
                val data = post.getChildren()[0].getData() as AmityPost.Data.POLL
                if (data.getPoll().blockingFirst().getStatus() == AmityPoll.Status.OPEN) {
                    items.add(closePollMenuItem)
                }
                items.add(deletePollMenuItem)
            } else {
                if (post.getFeedType() != AmityFeedType.REVIEWING && isPostDataTypeEditable(post)) {
                    items.add(editPostMenuItem)
                }
                items.add(deletePostMenuItem)
            }
        } else {
            val target = post.getTarget()
            when (target) {
                is AmityPost.Target.COMMUNITY -> {
                    target.getCommunity()?.getCommunityId()?.let {
                        val hasEditPermission =
                            AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY_POST)
                                .atCommunity(it).check().blockingFirst(false)
                        if (hasEditPermission) {
                            // TBC
                            //items.add(editPostMenuItem)
                        }
                        val hasDeletePermission =
                            AmityCoreClient.hasPermission(AmityPermission.DELETE_COMMUNITY_POST)
                                .atCommunity(it)
                                .check()
                                .blockingFirst(false)
                        if (hasDeletePermission) {
                            items.add(deletePostMenuItem)
                        }
                    }
                }
                else -> {
                    val hasDeletePermission =
                        AmityCoreClient.hasPermission(AmityPermission.DELETE_USER_FEED_POST)
                            .atGlobal()
                            .check()
                            .blockingFirst(false)
                    if (hasDeletePermission) {
                        items.add(deletePostMenuItem)
                    }
                }
            }

            if (post.getFeedType() == AmityFeedType.PUBLISHED) {
                if (post.isFlaggedByMe()) {
                    items.add(unReportPostMenuItem)
                } else {
                    items.add(reportPostMenuItem)
                }
            }
        }
        return items
    }

    fun vote(pollId: String, answerIds: List<String>): Completable {
        return AmitySocialClient.newPollRepository()
            .votePoll(pollId = pollId, answerIds = answerIds)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun searchUsersMention(
        keyword: String,
        onResult: (users: PagingData<AmityUser>) -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository().searchUsers(keyword)
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

    private fun isPostDataTypeEditable(post: AmityPost): Boolean {
        return if (post.getType() == AmityPost.DataType.LIVE_STREAM) {
            false
        } else {
            return if (post.getChildren().isNotEmpty()) {
                post.getChildren()[0].getType() != AmityPost.DataType.LIVE_STREAM
            } else {
                true
            }
        }
    }
}