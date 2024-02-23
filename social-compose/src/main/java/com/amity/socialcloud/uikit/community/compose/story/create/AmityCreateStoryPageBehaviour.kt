package com.amity.socialcloud.uikit.community.compose.story.create

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryPageActivity

open class AmityCreateStoryPageBehavior {

    open fun goToDraftStoryPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityStory.TargetType,
        isImage: Boolean,
        fileUri: String,
    ) {
        val intent = AmityDraftStoryPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            isImage = isImage,
            fileUri = fileUri
        )
        launcher.launch(intent)
    }
}