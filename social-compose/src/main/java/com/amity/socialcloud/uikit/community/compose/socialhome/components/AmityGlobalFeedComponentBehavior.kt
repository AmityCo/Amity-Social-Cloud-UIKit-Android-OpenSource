package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity

open class AmityGlobalFeedComponentBehavior {

    open fun goToPostDetailPage(
        context: Context,
        id: String,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = id,
        )
        context.startActivity(intent)
    }
}