package com.amity.socialcloud.uikit.community.compose.story.target

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.utils.getActivity
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType

open class AmityStoryTabComponentBehavior {

    open fun goToViewStoryPage(
        context: Context,
        type: AmityViewStoryPageType,
    ) {
        val intent = AmityViewStoryPageActivity.newIntent(
            context = context,
            type = type,
        )
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context.getActivity()).toBundle()
        )
    }

    open fun goToCreateStoryPage(
        context: Context,
        targetId: String,
        targetType: AmityStory.TargetType,
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
        )
        context.startActivity(intent)
    }

    open fun goToCreateStoryPage(
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