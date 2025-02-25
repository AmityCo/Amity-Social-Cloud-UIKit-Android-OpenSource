package com.amity.snipet.verifier.social.search.community

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.search.community.AmityMyCommunitiesSearchPageBehavior

class AmityMyCommunitiesSearchPageBehavior {
    /* begin_sample_code
      gist_id: 5780054fb292861d5baefa206ef7e1cd
      filename: AmityMyCommunitiesSearchPageBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: My communities search page behavior customization
      */
    class CustomMyCommunitiesSearchPageBehavior : AmityMyCommunitiesSearchPageBehavior() {
        override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
            // custom implementation for navigating to Community Profile Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomMyCommunitiesSearchPageBehavior()
        AmityUIKit4Manager.behavior.myCommunitiesSearchPageBehavior = customBehaviour
    }
    /* end_sample_code */
}