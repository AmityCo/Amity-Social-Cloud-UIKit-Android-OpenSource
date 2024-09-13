package com.amity.snipet.verifier.social.community.membership

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageBehavior


class AmityCommunityMembershipPageBehaviorSample {
    /* begin_sample_code
      gist_id: 3a9de57a320cc1fdda8ce5b2611f9441
      filename: AmityCommunityMembershipPageBehaviorSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Community membership page behavior customization
      */
    class CustomCommunityMembershipPageBehavior : AmityCommunityMembershipPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCommunityMembershipPageBehavior()
        AmityUIKit4Manager.behavior.communityMembershipPageBehavior = customBehaviour
    }
    /* end_sample_code */
}