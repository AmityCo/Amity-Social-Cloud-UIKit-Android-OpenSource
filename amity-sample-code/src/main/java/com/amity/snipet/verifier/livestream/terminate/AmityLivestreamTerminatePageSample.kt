package com.amity.snipet.verifier.livestream.terminate

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamTerminatedPage
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamTerminatedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType

class AmityLivestreamTerminatePageSample {
    /* begin_sample_code
   gist_id: d9b762f230494e874814775a202b65bf
   filename: AmityLivestreamTerminatePageSample.kt
   asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
   description: Amity Livestream terminate page
   */
    @Composable
    fun composeLivestreamPlayerPage(livestreamScreenType: LivestreamScreenType) {
        AmityLivestreamTerminatedPage(
            livestreamScreenType = livestreamScreenType,
        )
    }

    fun startAnActivity(context: Context, livestreamScreenType: LivestreamScreenType) {
        val intent = AmityLivestreamTerminatedPageActivity.newIntent(
            context = context,
            screenType = livestreamScreenType,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}