package com.amity.snipet.verifier.social.story.draft

import android.content.Context
import android.net.Uri
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryPageActivity
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityStoryMediaType

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
        mediaType: AmityStoryMediaType = AmityStoryMediaType.Image(Uri.EMPTY)
    ) {
        val intent = AmityDraftStoryPageActivity.newIntent(
            context = context,
            targetId = community.getCommunityId(),
            targetType = AmityStory.TargetType.COMMUNITY,
            mediaType = mediaType,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}