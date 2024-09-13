package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
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

    open fun goToCreateCommunityPage(
        context: Context,
    ) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context,
            mode = AmityCommunitySetupPageMode.Create
        )
        context.startActivity(intent)
    }
}