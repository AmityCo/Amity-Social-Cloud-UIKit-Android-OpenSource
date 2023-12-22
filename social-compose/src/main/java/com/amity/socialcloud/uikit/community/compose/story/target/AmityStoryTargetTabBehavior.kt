package com.amity.socialcloud.uikit.community.compose.story.target

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageActivity

@UnstableApi
class AmityStoryTargetTabBehavior(
    private val context: Context
) {

    fun goToViewStoryPage(
        community: AmityCommunity,
    ) {
        val intent = AmityViewStoryPageActivity.newIntent(
            context = context,
            community = community,
        )
        context.startActivity(intent)
    }

    fun goToCreateStoryPage(
        community: AmityCommunity
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            community = community
        )
        context.startActivity(intent)
    }

    fun goToCreateStoryPage(
        launcher: ActivityResultLauncher<Intent>,
        community: AmityCommunity,
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            community = community,
        )
        launcher.launch(intent)
    }
}