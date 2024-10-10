package com.amity.socialcloud.uikit.community.compose.user.profile.components

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.user.pending.AmityUserPendingFollowRequestsPageActivity
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageActivity
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageTab

open class AmityUserProfileHeaderComponentBehavior : AmityBaseBehavior() {

    open fun goToUserRelationshipPage(
        context: Context,
        userId: String,
        selectedTab: AmityUserRelationshipPageTab,
    ) {
        val intent = AmityUserRelationshipPageActivity.newIntent(
            context = context,
            userId = userId,
            selectedTab = selectedTab,
        )
        context.startActivity(intent)
    }

    open fun goToPendingFollowRequestPage(
        context: Context,
    ) {
        val intent = AmityUserPendingFollowRequestsPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
}