package com.amity.socialcloud.uikit.community.compose.clip.create

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.uikit.community.compose.clip.draft.AmityDraftClipPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.create.nopermission.AmityNoPermissionPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

open class AmityCreateClipPageBehavior {
    open fun goToDraftClipPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityPostTargetType,
        mediaType: Uri,
    ) {
        val intent = AmityDraftClipPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            media = mediaType,
        )
        launcher.launch(intent)
    }

    open fun goToNoPermissionPage(
        context: Context,
    ) {
        val intent = AmityNoPermissionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
}