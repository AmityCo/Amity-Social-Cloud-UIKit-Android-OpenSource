package com.amity.snipet.verifier.livestream.create

import android.content.Context
import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPage
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity

class AmityCreateLivestreamPageSample {
    /* begin_sample_code
    gist_id: 2bd17fd7bd4636550189a719b3592974
    filename: AmityCreateLivestreamPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
    description: Livestream Creation Page
    */
    @Composable
    fun composeCreateLivestreamPage(
        targetId: String,
        targetType: AmityPost.TargetType,
        community: AmityCommunity? = null,
    ) {
        AmityCreateLivestreamPage(
            targetId = targetId,
            targetType = targetType,
            targetCommunity = community
        )
    }

    fun startAnActivity(
        context: Context, targetId: String,
        targetType: AmityPost.TargetType,
        community: AmityCommunity? = null,
    ) {
        val intent = AmityCreateLivestreamPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            community = community,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */

}