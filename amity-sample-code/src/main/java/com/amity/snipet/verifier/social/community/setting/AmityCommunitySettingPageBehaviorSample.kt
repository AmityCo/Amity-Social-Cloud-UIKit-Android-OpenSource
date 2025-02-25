package com.amity.snipet.verifier.social.community.setting

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageBehavior


class AmityCommunitySettingPageBehaviorSample {
    /* begin_sample_code
      gist_id: 569b38bb05415f0e6ae810817534f8fd
      filename: AmityCommunitySettingPageBehaviorSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Community setup page behavior customization
      */
    class CustomCommunitySettingPageBehavior : AmityCommunitySettingPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCommunitySettingPageBehavior()
        AmityUIKit4Manager.behavior.communitySettingPageBehavior = customBehaviour
    }
    /* end_sample_code */
}