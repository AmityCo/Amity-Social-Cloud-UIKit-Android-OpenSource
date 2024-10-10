package com.amity.socialcloud.uikit.community.compose.user.pending

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityUserPendingFollowRequestsPageBehavior : AmityBaseBehavior() {

    open fun goToUserProfilePage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId
        )
        context.startActivity(intent)
    }
}