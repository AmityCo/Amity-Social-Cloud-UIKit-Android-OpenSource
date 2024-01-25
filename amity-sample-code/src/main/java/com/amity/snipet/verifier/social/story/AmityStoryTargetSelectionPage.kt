package com.amity.snipet.verifier.social.story

import android.content.Context
import android.content.Intent
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityStoryTargetPickerActivity
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityStoryTargetPickerFragment

class AmityStoryTargetSelectionPage {
    /* begin_sample_code
      gist_id: 6c935d3ba9364ed4495867ef9a8f7385
      filename: AmityStoryTargetSelectionPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/story-target-selection-page
      description: Navigate to story target selection page
      */
    fun startAnActivity(context: Context) {
        val intent = Intent(
            context,
            AmityStoryTargetPickerActivity::class.java
        )
        context.startActivity(intent)
    }

    @androidx.media3.common.util.UnstableApi
    fun createFragment(storyCreationType: String) {
        AmityStoryTargetPickerFragment.newInstance()
            .build(storyCreationType = storyCreationType)
    }
    /* end_sample_code */
}