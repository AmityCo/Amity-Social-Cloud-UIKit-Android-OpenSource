package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity

class AmityCustomCommunitySearchResultComponentBehavior :
    AmityCommunitySearchResultComponentBehavior() {

    override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = community.getCommunityId(),
        )
        context.startActivity(intent)
    }
}