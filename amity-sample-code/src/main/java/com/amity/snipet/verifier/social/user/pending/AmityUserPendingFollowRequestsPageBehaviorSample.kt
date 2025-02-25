package com.amity.snipet.verifier.social.user.pending

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.pending.AmityUserPendingFollowRequestsPageBehavior

class AmityUserPendingFollowRequestsPageBehaviorSample {
    /* begin_sample_code
    gist_id: 9cc7f72ba18325d0b4147c0a0276a923
    filename: AmityUserPendingFollowRequestsPageBehaviorSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Pending Follow Requests Page sample behavior
    */
    class CustomAmityUserPendingFollowRequestsPageBehavior :
        AmityUserPendingFollowRequestsPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomAmityUserPendingFollowRequestsPageBehavior()
        AmityUIKit4Manager.behavior.userPendingFollowRequestsPageBehavior = customBehaviour
    }
    /* end_sample_code */
}