package com.amity.snipet.verifier.social.story

import android.content.Context
import android.content.Intent
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityTargetSelectionPageActivity

class AmityTargetSelectionPage {
    /* begin_sample_code
      gist_id: 6c935d3ba9364ed4495867ef9a8f7385
      filename: AmityTargetSelectionPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/story-target-selection-page
      description: Navigate to target selection page
      */
    fun startAnActivity(context: Context) {
        val intent = Intent(
            context,
            AmityTargetSelectionPageActivity::class.java
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}