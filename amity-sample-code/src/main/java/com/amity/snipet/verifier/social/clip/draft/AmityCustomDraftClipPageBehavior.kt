package com.amity.snipet.verifier.social.clip.draft

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.clip.draft.AmityDraftClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

class AmityCustomDraftClipPageBehavior {
    /* begin_sample_code
    gist_id:48310c7f5d9e386e8a21b5669dbca7f6
    filename: AmityCustomDraftClipPageBehavior.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
    description: Livestream post target selection page behavior customization
    */
    class CustomDraftClipPostTargetSelectionPageBehavior : AmityDraftClipPageBehavior() {
        override fun goToPostComposerPage(
            context: Context,
            clip: AmityClip,
            uri: Uri,
            launcher: ActivityResultLauncher<Intent>,
            targetId: String,
            targetType: AmityPostTargetType,
            community: AmityCommunity?,
            isMute: Boolean,
            aspectRatio: AmityClip.DisplayMode
        ) {
            // Custom implementation for navigating to Post Composer Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomDraftClipPostTargetSelectionPageBehavior()
        AmityUIKit4Manager.behavior.draftClipPageBehavior = customBehaviour
    }
    /* end_sample_code */
}