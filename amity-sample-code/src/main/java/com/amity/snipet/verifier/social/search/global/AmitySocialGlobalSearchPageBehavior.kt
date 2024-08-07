package com.amity.snipet.verifier.social.search.global

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageBehavior

class AmitySocialGlobalSearchPageBehavior {
    /* begin_sample_code
      gist_id: 89a6b3c545eeaab632231fa8f2131410
      filename: AmitySocialGlobalSearchPageBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Social global search page behavior customization
      */
    class CustomSocialGlobalSearchPageBehavior : AmitySocialGlobalSearchPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomSocialGlobalSearchPageBehavior()
        AmityUIKit4Manager.behavior.socialGlobalSearchPageBehavior = customBehaviour
    }
    /* end_sample_code */
}