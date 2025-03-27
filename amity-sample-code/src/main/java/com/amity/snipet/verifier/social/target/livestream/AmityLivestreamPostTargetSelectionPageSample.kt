package com.amity.snipet.verifier.social.target.livestream

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPage
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPageActivity

class AmityLivestreamPostTargetSelectionPageSample {
    /* begin_sample_code
      gist_id: 51204c15c87e567bb0438e2af7dc1db3
      filename: AmityLivestreamPostTargetSelectionPageSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Navigate to livestream post target selection page
      */
    @Composable
    fun composeLivestreamPostTargetSelectionPage() {
        AmityLivestreamPostTargetSelectionPage()
    }

    fun startAnActivity(context: Context) {
        val intent = AmityLivestreamPostTargetSelectionPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}