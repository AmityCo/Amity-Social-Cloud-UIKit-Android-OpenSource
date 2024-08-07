package com.amity.snipet.verifier.social.target.story

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageBehavior

class AmityStoryTargetSelectionPageSampleBehavior {
    /* begin_sample_code
     gist_id: c578dc8dc6bac6c9c637c3e6dce14ad1
     filename: AmityStoryTargetSelectionPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Story target selection page behavior customization
     */
    class CustomStoryTargetSelectionPageBehaviour : AmityStoryTargetSelectionPageBehavior() {
        override fun goToStoryCreationPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityStory.TargetType
        ) {
            // custom implementation for navigating to Create Story Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomStoryTargetSelectionPageBehaviour()
        AmityUIKit4Manager.behavior.storyTargetSelectionPageBehavior = customBehaviour
    }
    /* end_sample_code */
}