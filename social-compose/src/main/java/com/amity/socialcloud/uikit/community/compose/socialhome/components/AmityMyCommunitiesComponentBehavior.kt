package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity

open class AmityMyCommunitiesComponentBehavior {

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

    open fun goToCreateCommunityPage(context: Context) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context,
            mode = AmityCommunitySetupPageMode.Create,
        )
        context.startActivity(intent)
    }
}