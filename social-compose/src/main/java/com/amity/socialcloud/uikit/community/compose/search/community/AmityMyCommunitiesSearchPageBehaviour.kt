package com.amity.socialcloud.uikit.community.compose.search.community

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

class AmityMyCommunitiesSearchPageBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        community: AmityCommunity,
    ) {
        //  do nothing, need to override in social module to access community profile page
    }
}