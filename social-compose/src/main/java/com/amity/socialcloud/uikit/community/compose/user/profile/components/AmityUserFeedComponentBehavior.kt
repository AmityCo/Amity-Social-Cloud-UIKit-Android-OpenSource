package com.amity.socialcloud.uikit.community.compose.user.profile.components

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity

open class AmityUserFeedComponentBehavior : AmityBaseBehavior() {

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
}