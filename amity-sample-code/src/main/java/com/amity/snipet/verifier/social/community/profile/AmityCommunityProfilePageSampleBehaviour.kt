package com.amity.snipet.verifier.social.community.profile

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior


class AmityCommunityProfilePageSampleBehaviour {
    /* begin_sample_code
    gist_id: a9e59a1d9cd599631ea035eb6513465b
    filename: AmityCommunityProfilePageSampleBehaviour.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Community page behavior customization
    */
    class AmityCustomCommunityProfilePageBehavior : AmityCommunityProfilePageBehavior() {
        override fun goToCommunitySettingPage(
            context: Context,
        ) {
            // custom implementation for navigating to Community Setting Page
        }

        override fun goToPendingPostPage(context: Context) {
            // custom implementation for navigating to Post Review Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehavior = AmityCustomCommunityProfilePageBehavior()
        AmityUIKit4Manager.behavior.communityProfilePageBehavior = customBehavior
    }
    /* end_sample_code */
}