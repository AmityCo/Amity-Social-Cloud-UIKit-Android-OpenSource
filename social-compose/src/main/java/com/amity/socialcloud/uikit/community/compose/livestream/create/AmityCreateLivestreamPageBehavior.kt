package com.amity.socialcloud.uikit.community.compose.livestream.create

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.uikit.community.compose.livestream.create.nopermission.AmityNoPermissionPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.create.thumbnailpreview.AmityThumbnailPreviewPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamTerminatedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPageActivity

open class AmityCreateLivestreamPageBehavior {

    open fun goToNoPermissionPage(
        context: Context,
    ) {
        val intent = AmityNoPermissionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToSelectLivestreamTargetPage(
        context: Context,
        isEditMode: Boolean,
        launcher: ActivityResultLauncher<Intent>,
    ) {
        val intent = AmityLivestreamPostTargetSelectionPageActivity.newIntent(
            context = context,
            isEditMode = isEditMode,
        )
        launcher.launch(intent)
    }

    open fun goToThumbnailPreviewPage(
        context: Context,
        mediaUri: String,
    ) {
        val intent = AmityThumbnailPreviewPageActivity.newIntent(
            context = context,
            mediaUri = mediaUri,
        )
        context.startActivity(intent)
    }

    open fun goToPostDetailPage(
        context: Context,
        id: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
        showLivestreamPostExceeded: Boolean = false,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = id,
            category = category,
            showLivestreamPostExceeded = showLivestreamPostExceeded
        )
        context.startActivity(intent)
    }

    open fun goToTerminatedPage(
        context: Context,
        screenType: LivestreamScreenType,
    ) {
        val intent = AmityLivestreamTerminatedPageActivity.newIntent(
            context = context,
            screenType = screenType
        )
        context.startActivity(intent)
    }
}