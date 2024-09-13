package com.amity.socialcloud.uikit

import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageBehavior
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

class AmityCustomCommunityMembershipPageBehavior : AmityCommunityMembershipPageBehavior() {

    override fun goToUserProfilePage(context: Context) {
        val intent = AmityUserProfileActivity.newIntent(
            context = context.pageContext,
            id = context.userId!!,
        )
        context.pageContext.startActivity(intent)
    }
}