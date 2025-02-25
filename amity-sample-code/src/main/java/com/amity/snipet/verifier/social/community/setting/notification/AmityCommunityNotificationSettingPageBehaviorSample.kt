package com.amity.snipet.verifier.social.community.setting.notification

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPageBehavior


class AmityCommunityNotificationSettingPageBehaviorSample {
    /* begin_sample_code
      gist_id: 160dc511427e388763b07ae6d4082274
      filename: AmityCommunityNotificationSettingPageBehaviorSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Community setup page behavior customization
      */
    class CustomCommunityNotificationSettingPageBehavior :
        AmityCommunityNotificationSettingPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCommunityNotificationSettingPageBehavior()
        AmityUIKit4Manager.behavior.communityNotificationSettingPageBehavior = customBehaviour
    }
    /* end_sample_code */
}