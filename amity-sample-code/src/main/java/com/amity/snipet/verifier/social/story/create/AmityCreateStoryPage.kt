package com.amity.snipet.verifier.social.story.create

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity

class AmityCreateStoryPage {
    /* begin_sample_code
       gist_id: 2d6efdfe8d061bed6d2ae6bd2491671f
       filename: AmityCreateStoryPage.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/story-creation
       description: Navigate to create story page
       */
    fun startAnActivity(context: Context, community: AmityCommunity) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            targetId = community.getCommunityId(),
            targetType = AmityStory.TargetType.COMMUNITY
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}