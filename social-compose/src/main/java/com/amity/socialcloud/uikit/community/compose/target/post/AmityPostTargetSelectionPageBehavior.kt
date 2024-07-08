package com.amity.socialcloud.uikit.community.compose.target.post

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType


open class AmityPostTargetSelectionPageBehavior {

    open fun goToPostComposerPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String? = null,
        targetType: AmityPostTargetType,
        community: AmityCommunity? = null,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerCreateOptions(
                targetId = targetId,
                targetType = targetType,
                community = community
            )
        )
        launcher.launch(intent)
    }

    /*
    goToLiveStreamPage(context: AmityPostTargetSelectionPageBehavior.Context): void {
        // navigation logic
    }
    goToPostComposerPage(context: AmityPostTargetSelectionPageBehavior.Context): void {
        // navigation logic
    }
     */
}