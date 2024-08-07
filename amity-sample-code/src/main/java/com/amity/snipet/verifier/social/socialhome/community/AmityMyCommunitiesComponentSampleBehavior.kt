package com.amity.snipet.verifier.social.socialhome.community

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior

class AmityMyCommunitiesComponentSampleBehavior {
    /* begin_sample_code
    gist_id: 4210c5f3a13782076410b057a394fa9e
    filename: AmityMyCommunitiesComponentSampleBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity My Communities Component Behavior customization
    */
    class CustomMyCommunitiesComponentBehavior : AmityMyCommunitiesComponentBehavior() {
        override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
            // custom implementation for navigating to Community Profile Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehavior = CustomMyCommunitiesComponentBehavior()
        AmityUIKit4Manager.behavior.myCommunitiesComponentBehavior = customBehavior
    }
    /* end_sample_code */
}