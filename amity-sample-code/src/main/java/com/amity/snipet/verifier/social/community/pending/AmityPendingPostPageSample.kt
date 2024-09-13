package com.amity.snipet.verifier.social.community.pending

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.pending.AmityPendingPostsPage

class AmityPendingPostPageSample {
    /* begin_sample_code
    gist_id: eda118afcd18feff46bc8be25c01967d
    filename: AmityPendingPostPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Pending Post Page sample
    */
    @Composable
    fun composePendingPostPage(community: AmityCommunity) {
        AmityPendingPostsPage(
            community = community,
        )
    }
    /* end_sample_code */
}