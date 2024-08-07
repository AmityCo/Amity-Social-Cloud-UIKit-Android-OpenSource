package com.amity.snipet.verifier.social.target.post

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageBehavior

class AmityPostTargetSelectionPageSampleBehavior {
    /* begin_sample_code
     gist_id: 6d7cc96d1926f576831cbb93be87e8fb
     filename: AmityPostTargetSelectionPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Post target selection page behavior customization
     */
    class CustomPostTargetSelectionPageBehaviour : AmityPostTargetSelectionPageBehavior() {
        override fun goToPostComposerPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String?,
            targetType: AmityPostTargetType,
            community: AmityCommunity?
        ) {
            // custom implementation for navigating to Post Composer Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomPostTargetSelectionPageBehaviour()
        AmityUIKit4Manager.behavior.postTargetSelectionPageBehavior = customBehaviour
    }
    /* end_sample_code */
}