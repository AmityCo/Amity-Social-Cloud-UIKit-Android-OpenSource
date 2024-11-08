package com.amity.snipet.verifier.social.target.poll

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.target.poll.AmityPollTargetSelectionPage
import com.amity.socialcloud.uikit.community.compose.target.poll.AmityPollTargetSelectionPageActivity


class AmityPollTargetSelectionPageSample {
    /* begin_sample_code
      gist_id: 51d5391108df17b1d7993f63d92cd090
      filename: AmityPollTargetSelectionPageSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Navigate to poll target selection page
      */
    @Composable
    fun pollTargetSelectionPage() {
        AmityPollTargetSelectionPage()
    }

    fun startAnActivity(context: Context) {
        val intent = AmityPollTargetSelectionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}