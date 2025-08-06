package com.amity.snipet.verifier.social.clip.create

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.clip.create.AmityCreateClipPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

class AmityCreateClipPage {
    /* begin_sample_code
       gist_id:0824b1cba68cc5ebee7bfb0986b47cc1
       filename: AmityCreateClipPage.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/clip-creation
       description: Navigate to create story page
       */
    fun startAnActivity(context: Context, community: AmityCommunity? = null, targetType: AmityPostTargetType) {
        // To create a post on a user feed, create a user target

        val intent = AmityCreateClipPageActivity.newIntent(
            context = context,
            targetId = community?.getCommunityId() ?: "UserId", // Replace with actual user ID depending on targetType
            targetType = targetType
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}