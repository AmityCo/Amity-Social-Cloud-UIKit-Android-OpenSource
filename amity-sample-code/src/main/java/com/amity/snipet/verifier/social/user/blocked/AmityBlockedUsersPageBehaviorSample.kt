package com.amity.snipet.verifier.social.user.blocked

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.blocked.AmityBlockedUsersPageBehavior

class AmityBlockedUsersPageBehaviorSample {
    /* begin_sample_code
    gist_id: a72b9721e3c616b1b18f1f9f5aab5073
    filename: AmityBlockedUsersPageBehaviorSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Blocked Users Page sample behavior
    */
    class CustomAmityBlockedUsersPageBehavior : AmityBlockedUsersPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomAmityBlockedUsersPageBehavior()
        AmityUIKit4Manager.behavior.blockedUsersPageBehavior = customBehaviour
    }
    /* end_sample_code */
}