package com.amity.snipet.verifier.social.community.pending

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingPostContentComponent


class AmityPendingPostComponentSample {
    /* begin_sample_code
    gist_id: f0849f7b3f0b51cc477e75151c0f2303
    filename: AmityPendingPostComponentSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Pending post component sample
    */
    @Composable
    fun composePendingPostComponent(post: AmityPost) {
        AmityPendingPostContentComponent(
            post = post,
            onAcceptAction = {

            },
            onDeclineAction = {

            }
        )
    }
    /* end_sample_code */
}