package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity

class AmityCustomViewStoryPageBehavior : AmityViewStoryPageBehavior() {

    override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = community.getCommunityId(),
        )
        context.startActivity(intent)
    }
}