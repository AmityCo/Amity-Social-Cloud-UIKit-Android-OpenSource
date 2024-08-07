package com.amity.snipet.verifier.social.search.community

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior

class AmityCommunitySearchResultComponentBehavior {
    /* begin_sample_code
      gist_id: a0f2074468cb26d8189e59b08a0cc903
      filename: AmityCommunitySearchResultComponentBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Community search result component behavior customization
      */
    class CustomCommunitySearchResultComponentBehavior :
        AmityCommunitySearchResultComponentBehavior() {

        override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
            // custom implementation for navigating to Community Profile Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCommunitySearchResultComponentBehavior()
        AmityUIKit4Manager.behavior.communitySearchResultComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}