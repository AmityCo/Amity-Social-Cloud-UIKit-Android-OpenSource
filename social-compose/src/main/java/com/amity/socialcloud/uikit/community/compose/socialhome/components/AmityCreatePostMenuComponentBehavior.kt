package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.target.AmityTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageActivity

open class AmityCreatePostMenuComponentBehavior {

    fun goToSelectPostTargetPage(
        context: Context,
        type: AmityTargetSelectionPageType
    ) {
        val intent = AmityPostTargetSelectionPageActivity.newIntent(
            context = context,
            type = type,
        )
        context.startActivity(intent)
    }

    fun goToSelectStoryTargetPage(
        context: Context,
    ) {
        val intent = AmityStoryTargetSelectionPageActivity.newIntent(
            context = context,
            type = AmityTargetSelectionPageType.STORY,
        )
        context.startActivity(intent)
    }
}