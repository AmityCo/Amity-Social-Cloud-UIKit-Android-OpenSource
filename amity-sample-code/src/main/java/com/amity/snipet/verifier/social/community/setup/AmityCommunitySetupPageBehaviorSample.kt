package com.amity.snipet.verifier.social.community.setup

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageBehavior


class AmityCommunitySetupPageBehaviorSample {
    /* begin_sample_code
      gist_id: eb5873284d6eef4cd7081b194ec0c9bf
      filename: AmityCommunitySetupPageBehaviorSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Community setup page behavior customization
      */
    class CustomCommunitySetupPageBehavior : AmityCommunitySetupPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCommunitySetupPageBehavior()
        AmityUIKit4Manager.behavior.communitySetupPageBehavior = customBehaviour
    }
    /* end_sample_code */
}