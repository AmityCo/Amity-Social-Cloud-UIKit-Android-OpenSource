package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

class AmityCustomPostContentComponentBehavior : AmityPostContentComponentBehavior() {

    override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
        val intent = AmityCommunityPageActivity.newIntent(
            context = context,
            community = community,
        )
        context.startActivity(intent)
    }

    override fun goToUserProfilePage(context: Context, user: AmityUser) {
        val intent = AmityUserProfileActivity.newIntent(
            context = context,
            id = user.getUserId(),
        )
        context.startActivity(intent)
    }
}
