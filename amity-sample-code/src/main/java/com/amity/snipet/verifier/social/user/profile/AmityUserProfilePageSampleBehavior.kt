package com.amity.snipet.verifier.social.user.profile

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageBehavior

class AmityUserProfilePageSampleBehavior {
    /* begin_sample_code
    gist_id: c5bc8c14eb6930d53afdbeacfbdd37d2
    filename: AmityUserProfilePageSampleBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Profile Page sample behavior
    */
    class CustomAmityUserProfilePageBehavior : AmityUserProfilePageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomAmityUserProfilePageBehavior()
        AmityUIKit4Manager.behavior.userProfilePageBehavior = customBehaviour
    }
    /* end_sample_code */
}