package com.amity.socialcloud.uikit.community.compose.clip.draft

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

open class AmityDraftClipPageBehavior {

    open fun goToPostComposerPage(
        context: Context,
        clip: AmityClip,
        uri: Uri,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityPostTargetType,
        community: AmityCommunity? = null,
        isMute: Boolean,
        aspectRatio: AmityClip.DisplayMode
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerCreateClipOptions(
                mode = AmityPostComposerOptions.Mode.CREATE_CLIP,
                targetId = targetId,
                targetType = targetType,
                community = community,
                clip = clip,
                uri = uri,
                isMute = isMute,
                aspectRatio = aspectRatio
            )
        )
        launcher.launch(intent)
    }
}