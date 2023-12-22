package com.amity.socialcloud.uikit.community.compose.story.view

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity

@UnstableApi
class AmityViewStoryPageBehavior(
    private val context: Context
) {

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