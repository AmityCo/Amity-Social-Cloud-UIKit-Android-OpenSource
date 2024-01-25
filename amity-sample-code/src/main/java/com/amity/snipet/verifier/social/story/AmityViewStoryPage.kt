package com.amity.snipet.verifier.social.story

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageActivity

class AmityViewStoryPage {
    /* begin_sample_code
       gist_id: b56c789ce415afebce4347007ac62177
       filename: AmityViewStoryPage.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/viewing-story-page
       description: Navigate to view story page
       */
    @androidx.media3.common.util.UnstableApi
    fun startAnActivity(context: Context, community: AmityCommunity) {
        val intent = AmityViewStoryPageActivity.newIntent(
            context = context,
            community = community,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}