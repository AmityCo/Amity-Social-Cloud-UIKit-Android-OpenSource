package com.amity.snipet.verifier.social.story.create

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityStoryMediaType


class AmityCreateStoryPageSampleBehavior {
    /* begin_sample_code
     gist_id: ed4841ad88c230abd860cdd7fab01952
     filename: AmityCreateStoryPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Create story page behavior customization
     */
    class CustomCreateStoryPageBehaviour : AmityCreateStoryPageBehavior() {

        override fun goToDraftStoryPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityStory.TargetType,
            mediaType: AmityStoryMediaType
        ) {
            // custom implementation for navigating to Draft Story Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCreateStoryPageBehaviour()
        AmityUIKit4Manager.behavior.createStoryPageBehavior = customBehaviour
    }
    /* end_sample_code */
}