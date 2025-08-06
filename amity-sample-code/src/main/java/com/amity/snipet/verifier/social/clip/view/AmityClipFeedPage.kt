package com.amity.snipet.verifier.social.clip.view

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType

class AmityClipFeedPage {
    /* begin_sample_code
      gist_id:230fd7d7bc4a3da519dfdcd698790908
      filename: AmityClipFeedPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/viewing-clip-page
      description: Navigate to clip feed page
      */
    fun startAnActivity(context: Context,  type: AmityClipFeedPageType) {
        val intent = AmityClipFeedPageActivity.newIntent(
            context = context,
            type = type
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}