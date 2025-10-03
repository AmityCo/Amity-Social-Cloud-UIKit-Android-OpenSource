package com.amity.socialcloud.uikit.community.compose.post.detail.components

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityPostContentComponentBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        community: AmityCommunity,
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = community.getCommunityId(),
        )
        context.startActivity(intent)
    }

    open fun goToUserProfilePage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
        )
        context.startActivity(intent)
    }

    open fun goToGlobalSearchPage(
        context: Context,
        prefilledText: String?,
    ) {
        val intent = AmitySocialGlobalSearchPageActivity.newIntent(
            context = context,
            prefilledText = prefilledText,
        )
        context.startActivity(intent)
    }

    open fun goToPostComposerPage(
        context: Context,
        post: AmityPost,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = if (post.getChildren().firstOrNull()?.getData() is AmityPost.Data.CLIP) {
                AmityPostComposerOptions.AmityPostComposerEditClipOptions(post = post)
            } else {
                AmityPostComposerOptions.AmityPostComposerEditOptions(post = post)
            }
        )
        context.startActivity(intent)
    }

    open fun goToClipFeedPage(
        context: Context,
        postId: String,
    ) {
        val intent = AmityClipFeedPageActivity.newIntent(
            context = context,
            type = AmityClipFeedPageType.NewsFeed(postId = postId)
        )
        context.startActivity(intent)
    }

    open fun handleVisitorUserAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleVisitorUserAction()
    }

    open fun handleNonMemberAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonMemberAction()
    }

    open fun handleNonFollowerAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonFollowerAction()
    }
}