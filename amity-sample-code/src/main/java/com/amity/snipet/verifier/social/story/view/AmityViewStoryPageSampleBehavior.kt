package com.amity.snipet.verifier.social.story.view

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior

class AmityViewStoryPageSampleBehavior {
    /* begin_sample_code
    gist_id: d61526187cd0dc51a02d9205bfd3a8fc
    filename: AmityViewStoryPageSampleBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
    description: View story page behavior customization
    */
    class CustomViewStoryPageBehavior : AmityViewStoryPageBehavior() {

        override fun goToCreateStoryPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityStory.TargetType,
        ) {
            // custom implementation for navigating to Create Story Page
        }

        override fun goToCommunityProfilePage(
            context: Context,
            community: AmityCommunity,
        ) {
            // custom implementation for navigating to Community Profile Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehavior = CustomViewStoryPageBehavior()
        AmityUIKit4Manager.behavior.viewStoryPageBehavior = customBehavior
    }
    /* end_sample_code */
}