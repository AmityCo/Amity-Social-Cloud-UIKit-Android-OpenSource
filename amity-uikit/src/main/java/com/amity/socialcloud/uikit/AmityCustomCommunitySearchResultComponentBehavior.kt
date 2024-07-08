package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity

class AmityCustomCommunitySearchResultComponentBehavior :
    AmityCommunitySearchResultComponentBehavior() {

    override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
        val intent = AmityCommunityPageActivity.newIntent(
            context = context,
            community = community,
        )
        context.startActivity(intent)
    }
}