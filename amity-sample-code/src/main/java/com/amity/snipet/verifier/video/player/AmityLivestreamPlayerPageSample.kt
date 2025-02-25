package com.amity.snipet.verifier.video.player

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.livestream.AmityLivestreamPlayerPage
import com.amity.socialcloud.uikit.community.compose.livestream.AmityLivestreamPlayerPageActivity

class AmityLivestreamPlayerPageSample {
    /* begin_sample_code
      gist_id: e00510d1eb447ec047a8caf9834177ae
      filename: AmityLivestreamPlayerPageSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Amity Livestream player page
      */
    @Composable
    fun composeLivestreamPlayerPage(post: AmityPost, onClose: () -> Unit) {
        AmityLivestreamPlayerPage(
            post = post,
            onClose = {
                // For example: activity.finish()
            },
        )
    }

    fun startAnActivity(context: Context, post: AmityPost) {
        val intent = AmityLivestreamPlayerPageActivity.newIntent(
            context = context,
            post = post,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}