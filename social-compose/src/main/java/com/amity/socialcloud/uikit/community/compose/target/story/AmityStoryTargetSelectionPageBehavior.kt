package com.amity.socialcloud.uikit.community.compose.target.story

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity


open class AmityStoryTargetSelectionPageBehavior {

    open fun goToStoryCreationPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityStory.TargetType,
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
        )
        launcher.launch(intent)
    }
}