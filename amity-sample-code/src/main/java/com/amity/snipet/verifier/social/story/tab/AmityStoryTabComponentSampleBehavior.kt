package com.amity.snipet.verifier.social.story.tab

import android.content.Context
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType

class AmityStoryTabComponentSampleBehavior {
    /* begin_sample_code
     gist_id: 799eb459979731e772dcce17e4a31559
     filename: AmityStoryTabComponentSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Story tab component behavior customization
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
        val customBehaviour = CustomStoryTabComponentBehaviour()
        AmityUIKit4Manager.behavior.storyTabComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}