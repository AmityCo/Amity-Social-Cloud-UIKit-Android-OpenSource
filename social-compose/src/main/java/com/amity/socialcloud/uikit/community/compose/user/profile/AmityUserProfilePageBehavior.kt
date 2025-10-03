package com.amity.socialcloud.uikit.community.compose.user.profile

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.clip.create.AmityCreateClipPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.clip.view.util.SharedClipFeedStore
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.user.blocked.AmityBlockedUsersPageActivity
import com.amity.socialcloud.uikit.community.compose.user.edit.AmityEditUserProfilePageActivity
import kotlinx.coroutines.flow.Flow

open class AmityUserProfilePageBehavior : AmityBaseBehavior() {

    open fun goToEditUserPage(
        context: Context,
    ) {
        val intent = AmityEditUserProfilePageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToBlockedUsersPage(
        context: Context,
    ) {
        val intent = AmityBlockedUsersPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToPostComposerPage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerCreateOptions(
                targetId = userId,
                targetType = AmityPostTargetType.USER,
            )
        )
        context.startActivity(intent)
    }

    open fun goToPollComposerPage(
        context: Context,
        userId: String,
        pollType: String,
    ) {
        val intent = AmityPollPostComposerPageActivity.newIntent(
            context = context,
            targetId = userId,
            targetType = AmityPost.TargetType.USER,
            pollType = pollType
        )
        context.startActivity(intent)
    }

    open fun goToLivestreamPostComposerPage(
        context: Context,
        targetId: String,
        targetType: AmityPost.TargetType,
        community: AmityCommunity? = null,
    ) {
        val intent = AmityCreateLivestreamPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            community = community,
        )
        context.startActivity(intent)
    }

    open fun goToClipPostComposerPage(
        context: Context,
        targetId: String,
        launcher: ActivityResultLauncher<Intent>,
        targetType: AmityPostTargetType,
    ) {
        val intent = AmityCreateClipPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
        )
        launcher.launch(intent)
    }

    open fun goToClipFeedPage(
        context: Context,
        postId: String,
        userId: String? = null,
        clipPagingData: Flow<PagingData<AmityPost>>? = null,
        selectedIndex: Int? = null,
        type: AmityClipFeedPageType,
    ) {
        // Store the shared data
        clipPagingData?.let { clipPage ->
            selectedIndex?.let { index ->
                userId?.let {
                    SharedClipFeedStore.setClipPagingData(
                        feedId = it,
                        feedType = "user",
                        pagingData = clipPage,
                        selectedIndex = index
                    )
                }
            }
        }

        val intent = AmityClipFeedPageActivity.newIntent(
            context = context,
            type = type
        )
        context.startActivity(intent)
    }


    open fun goToPostDetailPage(
        context: Context,
        postId: String,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            hideTarget = true,
        )
        context.startActivity(intent)
    }

    open fun handleVisitorUserAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleVisitorUserAction()
    }

    open fun handleNonFollowerAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonFollowerAction()
    }
}