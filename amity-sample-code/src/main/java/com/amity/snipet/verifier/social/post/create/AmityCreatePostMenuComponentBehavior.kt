package com.amity.snipet.verifier.social.post.create

import android.content.Context
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCreatePostMenuComponentBehavior
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType

class AmityCreatePostMenuComponentBehavior {
    /* begin_sample_code
      gist_id: 9dd421495daa1981003bf950037e7c8c
      filename: AmityCreatePostMenuComponentBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Create post menu behavior customization
      */
    class CustomCreatePostMenuComponentBehavior : AmityCreatePostMenuComponentBehavior() {
        override fun goToSelectPostTargetPage(
            context: Context,
            type: AmityPostTargetSelectionPageType
        ) {
            // custom implementation for navigating to Post Target Selection Page
        }

        override fun goToSelectStoryTargetPage(context: Context) {
            // custom implementation for navigating to Story Target Selection Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomCreatePostMenuComponentBehavior()
        AmityUIKit4Manager.behavior.createPostMenuComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}