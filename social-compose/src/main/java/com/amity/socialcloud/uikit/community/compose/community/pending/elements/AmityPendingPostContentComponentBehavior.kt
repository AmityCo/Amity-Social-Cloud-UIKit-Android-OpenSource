package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityPendingPostContentComponentBehavior : AmityBaseBehavior() {

    open fun goToUserProfilePage(context: Context, userId: String) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
        )
        context.startActivity(intent)
    }

    open fun goToClipFeedPage(
        context: Context,
        postId: String,
    ) {
        val intent = AmityClipFeedPageActivity.newIntent(
            context = context,
            type = AmityClipFeedPageType.NewsFeed(postId)
        )
        context.startActivity(intent)
    }
}