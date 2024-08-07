package com.amity.snipet.verifier.social.socialhome

import android.content.Context
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageBehavior

class AmitySocialHomePageSampleBehavior {
    /* begin_sample_code
    gist_id: 62a245b88d792703fa10c2ea3780ab73
    filename: AmitySocialHomeTopNavigationComponent.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Social Home Page Behavior customization
    */
    class CustomSocialHomePageBehavior : AmitySocialHomePageBehavior() {

        override fun goToGlobalSearchPage(context: Context) {
            // custom implementation for navigating to Global Search Page
        }

        override fun goToMyCommunitiesSearchPage(context: Context) {
            // custom implementation for navigating to My Communities Search Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehavior = CustomSocialHomePageBehavior()
        AmityUIKit4Manager.behavior.socialHomePageBehavior = customBehavior
    }
    /* end_sample_code */
}