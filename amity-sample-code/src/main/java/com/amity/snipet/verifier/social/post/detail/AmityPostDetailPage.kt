package com.amity.snipet.verifier.social.post.detail

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPage
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle

class AmityPostDetailPageSample {
    /* begin_sample_code
      gist_id: 3b8d784369e0bd41fdbd20f370bec422
      filename: AmityPostDetailPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Amity Post Detail Page
      */
    @Composable
    fun composePostDetailPage(postId: String) {
        AmityPostDetailPage(
            id = postId,
            style = AmityPostContentComponentStyle.DETAIL,
            hideTarget = false,
        )
    }

    fun startAnActivity(context: Context, postId: String) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}