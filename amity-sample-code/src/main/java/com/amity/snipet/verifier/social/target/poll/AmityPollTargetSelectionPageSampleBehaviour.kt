package com.amity.snipet.verifier.social.target.poll

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.target.poll.AmityPollTargetSelectionPageBehavior


class AmityPollTargetSelectionPageSampleBehaviour {
    /* begin_sample_code
     gist_id: a182e7789d08684af5355ad3696363b8
     filename: AmityPollTargetSelectionPageSampleBehaviour.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Poll target selection page behavior customization
     */
    class CustomPollTargetSelectionPageBehaviour : AmityPollTargetSelectionPageBehavior() {
        override fun goToPollPostComposerPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityPost.TargetType,
            community: AmityCommunity?
        ) {
            // custom implementation for navigating to Poll post Composer Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomPollTargetSelectionPageBehaviour()
        AmityUIKit4Manager.behavior.pollTargetSelectionPageBehavior = customBehaviour
    }
    /* end_sample_code */
}