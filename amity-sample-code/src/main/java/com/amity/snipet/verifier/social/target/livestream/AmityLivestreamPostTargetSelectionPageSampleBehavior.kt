package com.amity.snipet.verifier.social.target.livestream

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPageBehavior

class AmityLivestreamPostTargetSelectionPageSampleBehavior {
    /* begin_sample_code
     gist_id: 97c8580e8e72c4aefeb31e7f0fa35984
     filename: LivestreamPostTargetSelectionPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Livestream post target selection page behavior customization
     */
    class CustomLivestreamPostTargetSelectionPageBehavior : AmityLivestreamPostTargetSelectionPageBehavior() {
        override fun goToLivestreamPostComposerPage(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityPost.TargetType,
            community: AmityCommunity?,
        ) {
            super.goToLivestreamPostComposerPage(context, launcher, targetId, targetType, community)
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomLivestreamPostTargetSelectionPageBehavior()
        AmityUIKit4Manager.behavior.livestreamTargetSelectionPageBehavior = customBehaviour
    }
    /* end_sample_code */
}