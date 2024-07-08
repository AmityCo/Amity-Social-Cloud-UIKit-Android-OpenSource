package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

open class AmityMyCommunitiesComponentBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        community: AmityCommunity,
    ) {
        //  do nothing, need to override in social module to access community profile page
    }
}