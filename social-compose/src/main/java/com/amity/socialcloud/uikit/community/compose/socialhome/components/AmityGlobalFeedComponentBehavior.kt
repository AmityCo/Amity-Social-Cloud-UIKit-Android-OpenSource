package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityGlobalFeedComponentBehavior {

    open fun goToPostDetailPage(
        context: Context,
        id: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = id,
            category = category
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

    open fun goToUserProfilePage(
        context: Context,
        userId: String
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId
        )
        context.startActivity(intent)
    }
}