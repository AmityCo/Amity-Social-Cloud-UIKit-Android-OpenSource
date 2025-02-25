package com.amity.snipet.verifier.social.post.detail

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior

class AmityPostContentComponentSampleBehavior {
    /* begin_sample_code
     gist_id: e7c75e6dde0532689ec176d00767cfef
     filename: AmityPostContentComponentSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
     description: Post content component behavior customization
     */
    class CustomPostContentComponentBehaviour : AmityPostContentComponentBehavior() {
        override fun goToCommunityProfilePage(context: Context, community: AmityCommunity) {
            // custom implementation for navigating to Community Profile Page
        }

        override fun goToUserProfilePage(context: Context, userId: String) {
            // custom implementation for navigating to User Profile Page
        }

        override fun goToPostComposerPage(context: Context, post: AmityPost) {
            // custom implementation for navigating to Post Composer Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomPostContentComponentBehaviour()
        AmityUIKit4Manager.behavior.postContentComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}