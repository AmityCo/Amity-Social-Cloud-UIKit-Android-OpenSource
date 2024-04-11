package com.amity.socialcloud.uikit.community.compose.story.create

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityStoryMediaType

open class AmityCreateStoryPageBehavior {

    open fun goToDraftStoryPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityStory.TargetType,
        mediaType: AmityStoryMediaType,
    ) {
        val intent = AmityDraftStoryPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            mediaType = mediaType,
        )
        launcher.launch(intent)
    }
}