package com.amity.snipet.verifier.social.user.profile

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserProfileHeaderComponentBehavior

class AmityUserProfileHeaderComponentSampleBehavior {
    /* begin_sample_code
    gist_id: 3fa9a8cd3cf5ab3023b492bc28c75c97
    filename: AmityUserProfileHeaderComponentSampleBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Profile Header Component sample behavior
    */
    class CustomAmityUserProfileHeaderComponentBehavior :
        AmityUserProfileHeaderComponentBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = AmityUserProfileHeaderComponentBehavior()
        AmityUIKit4Manager.behavior.userProfileHeaderComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}