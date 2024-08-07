package com.amity.snipet.verifier.customization

import android.content.Context
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType

class AmityOverridingNavigationBehavior {
    /* begin_sample_code
     gist_id: 85d5a80eb7feaf332bf1b0351c264f89
     filename: AmityOverridingNavigationBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Overriding navigation behavior
     */
    class CustomStoryTabComponentBehaviour : AmityStoryTabComponentBehavior() {

        override fun goToViewStoryPage(
            context: Context,
            type: AmityViewStoryPageType,
        ) {
            // custom implementation for navigating to View Story Page
        }

        override fun goToCreateStoryPage(
            context: Context,
            targetId: String,
            targetType: AmityStory.TargetType
        ) {
            // custom implementation for navigating to Story Creation Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customStoryTabComponentBehaviour = CustomStoryTabComponentBehaviour()
        AmityUIKit4Manager.behavior.storyTabComponentBehavior = customStoryTabComponentBehaviour
    }
    /* end_sample_code */
}