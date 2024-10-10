package com.amity.snipet.verifier.social.socialhome.feed

import android.content.Context
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityGlobalFeedComponentBehavior

class AmityGlobalFeedComponentSampleBehavior {
    /* begin_sample_code
    gist_id: b90b3cee1c80fcb51d7b2e6ce51e3aaa
    filename: AmityGlobalFeedComponentSampleBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Global Feed Component Behavior customization
    */
    class CustomGlobalFeedComponentBehavior : AmityGlobalFeedComponentBehavior() {
        override fun goToPostDetailPage(context: Context, id: String, category: AmityPostCategory) {
            // custom implementation for navigating to Post Detail Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehavior = CustomGlobalFeedComponentBehavior()
        AmityUIKit4Manager.behavior.globalFeedComponentBehavior = customBehavior
    }
    /* end_sample_code */
}