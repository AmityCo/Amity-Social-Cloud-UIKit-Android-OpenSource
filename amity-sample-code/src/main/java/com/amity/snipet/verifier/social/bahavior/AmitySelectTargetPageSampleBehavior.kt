package com.amity.snipet.verifier.social.bahavior

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.AmityTargetSelectionPageBehavior

class AmityTargetSelectionPageSampleBehavior {
    /* begin_sample_code
     gist_id: c578dc8dc6bac6c9c637c3e6dce14ad1
     filename: AmityTargetSelectionPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Target selection page behavior customization
     */
    class CustomTargetSelectionPageBehaviour : AmityTargetSelectionPageBehavior() {
        override fun goToCreateStoryPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityStory.TargetType
        ) {
            // custom implementation for navigating to Create Story Page
        }
    }

    // Call this function in Application class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomTargetSelectionPageBehaviour()
        AmityUIKit4Manager.behavior.targetSelectionPageBehavior = customBehaviour
    }
    /* end_sample_code */
}