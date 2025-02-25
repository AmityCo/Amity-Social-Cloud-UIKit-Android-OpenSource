package com.amity.snipet.verifier.social.target.story

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPage
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageActivity

class AmityStoryTargetSelectionPage {
    /* begin_sample_code
      gist_id: 6c935d3ba9364ed4495867ef9a8f7385
      filename: AmityStoryTargetSelectionPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/story-target-selection-page
      description: Navigate to story target selection page
      */
    @Composable
    fun composeStoryTargetSelectionPage() {
        AmityStoryTargetSelectionPage()
    }

    fun startAnActivity(context: Context) {
        val intent = AmityStoryTargetSelectionPageActivity.newIntent(context)
        context.startActivity(intent)
    }
    /* end_sample_code */
}