package com.amity.snipet.verifier.social.target.post

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPage
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageActivity

class AmityPostTargetSelectionPage {
    /* begin_sample_code
      gist_id: b79578ef1ef7a1b45f10a82254fc17fa
      filename: AmityPostTargetSelectionPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Navigate to post target selection page
      */
    @Composable
    fun composePostTargetSelectionPage(
        type: AmityPostTargetSelectionPageType
    ) {
        AmityPostTargetSelectionPage(
            type = type,
        )
    }

    fun startAnActivity(context: Context) {
        val intent = AmityPostTargetSelectionPageActivity.newIntent(
            context = context,
            type = AmityPostTargetSelectionPageType.POST
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}