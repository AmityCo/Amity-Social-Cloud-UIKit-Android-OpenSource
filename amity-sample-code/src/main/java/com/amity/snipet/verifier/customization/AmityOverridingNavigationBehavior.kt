package com.amity.snipet.verifier.customization

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponentBehavior

@UnstableApi
class AmityOverridingNavigationBehavior {
    /* begin_sample_code
     gist_id: 85d5a80eb7feaf332bf1b0351c264f89
     filename: AmityOverridingNavigationBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Overriding navigation behavior
     */
    class CustomStoryTabComponentBehaviour : AmityStoryTargetTabComponentBehavior() {

        override fun goToViewStoryPage(
            context: Context,
            community: AmityCommunity,
        ) {
            // custom implementation for navigating to View Story Page
        }

        override fun goToCreateStoryPage(
            context: Context,
            community: AmityCommunity
        ) {
            // custom implementation for navigating to Story Creation Page
        }
    }

    // Call this function in Application class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customStoryTabComponentBehaviour = CustomStoryTabComponentBehaviour()
        AmityUIKit4Manager.behavior.storyTabComponentBehavior = customStoryTabComponentBehaviour
    }
    /* end_sample_code */
}