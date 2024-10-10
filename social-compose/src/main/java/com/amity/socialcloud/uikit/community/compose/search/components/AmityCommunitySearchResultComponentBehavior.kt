package com.amity.socialcloud.uikit.community.compose.search.components

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity

open class AmityCommunitySearchResultComponentBehavior {
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
}