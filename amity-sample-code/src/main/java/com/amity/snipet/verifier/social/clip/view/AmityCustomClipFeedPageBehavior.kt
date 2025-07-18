package com.amity.snipet.verifier.social.clip.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.clip.draft.AmityDraftClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType

class AmityCustomClipFeedPageBehavior {
    /* begin_sample_code
 gist_id:efe611f4ffcf7fc387706554f936e528
 filename: AmityCustomClipFeedPageBehavior.kt
 asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
 description: Livestream post target selection page behavior customization
 */
    class CustomClipFeedPageBehavior : AmityClipPageBehavior() {
        override fun goToPostDetailPage(context: Context, postId: String) {

        }

        override fun goToClipPostComposerPage(
            context: Context,
            targetId: String?,
            targetType: AmityPostTargetType,
        ) {

        }

        override fun goToCommunityProfilePage(
            context: Context,
            community: AmityCommunity,
        ) {

        }

        override fun goToUserProfilePage(context: Context, userId: String) {

        }

        override fun goToSelectPostTargetPage(
            context: Context,
            type: AmityPostTargetSelectionPageType,
        ) {

        }

        override fun goToCreateCommunityPage(context: Context) {

        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomClipFeedPageBehavior()
        AmityUIKit4Manager.behavior.clipFeedPageBehavior = customBehaviour
    }
    /* end_sample_code */
}