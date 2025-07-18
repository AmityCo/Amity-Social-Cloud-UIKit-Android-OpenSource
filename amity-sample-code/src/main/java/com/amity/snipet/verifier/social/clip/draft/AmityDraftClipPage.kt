package com.amity.snipet.verifier.social.clip.draft

import android.content.Context
import android.net.Uri
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.clip.draft.AmityDraftClipPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

class AmityDraftClipPage {
    /* begin_sample_code
     gist_id:40eaadbfe0fe6e25bc82705d4f86f4c8
     filename: AmityDraftClipPage.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/clip-creation/draft-page
     description: Navigate to draft clip page
     */
    fun startAnActivity(
        context: Context,
        community: AmityCommunity? = null,
        targetType: AmityPostTargetType= AmityPostTargetType.COMMUNITY,
        mediaUri: Uri,
    ) {
        val intent = AmityDraftClipPageActivity.newIntent(
            context = context,
            targetId = community?.getCommunityId() ?: "UserId", // Replace with actual user ID depending on targetType
            targetType = targetType,
            media = mediaUri,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}