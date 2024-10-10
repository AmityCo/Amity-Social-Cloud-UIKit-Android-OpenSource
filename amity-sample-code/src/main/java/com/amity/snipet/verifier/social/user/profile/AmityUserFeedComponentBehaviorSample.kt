package com.amity.snipet.verifier.social.user.profile

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserFeedComponentBehavior

class AmityUserFeedComponentBehaviorSample {
    /* begin_sample_code
    gist_id: 2d9f10820a29608fe7af65beb06568b9
    filename: AmityUserFeedComponentBehaviorSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Feed Component sample behavior
    */
    class CustomAmityUserFeedComponentBehavior : AmityUserFeedComponentBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomAmityUserFeedComponentBehavior()
        AmityUIKit4Manager.behavior.userFeedComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}