package com.amity.snipet.verifier.social.story

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryPageActivity

class AmityDraftStoryPage {
    /* begin_sample_code
       gist_id: 3f8cdf76d0ebf2bcb54f445924cba509
       filename: AmityDraftStoryPage.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/story-creation/draft-page
       description: Navigate to draft story page
       */
    fun startAnActivity(
        context: Context,
        community: AmityCommunity,
        isImage: Boolean,
        fileUri: String
    ) {
        val intent = AmityDraftStoryPageActivity.newIntent(
            context = context,
            targetId = community.getCommunityId(),
            isImage = isImage,
            fileUri = fileUri
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}