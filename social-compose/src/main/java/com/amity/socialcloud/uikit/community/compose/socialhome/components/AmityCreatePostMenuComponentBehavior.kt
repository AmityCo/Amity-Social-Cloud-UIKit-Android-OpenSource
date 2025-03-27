package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.compose.target.poll.AmityPollTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageActivity

open class AmityCreatePostMenuComponentBehavior {

    open fun goToSelectPostTargetPage(
        context: Context,
        type: AmityPostTargetSelectionPageType
    ) {
        val intent = AmityPostTargetSelectionPageActivity.newIntent(
            context = context,
            type = type,
        )
        context.startActivity(intent)
    }

    open fun goToSelectStoryTargetPage(
        context: Context,
    ) {
        val intent = AmityStoryTargetSelectionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToSelectPollTargetPage(
        context: Context,
    ) {
        val intent = AmityPollTargetSelectionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToSelectLivestreamTargetPage(
        context: Context,
    ) {
        val intent = AmityLivestreamPostTargetSelectionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
}