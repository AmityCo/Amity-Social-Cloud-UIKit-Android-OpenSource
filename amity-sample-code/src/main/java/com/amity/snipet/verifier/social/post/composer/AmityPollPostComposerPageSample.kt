package com.amity.snipet.verifier.social.post.composer

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPage
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPage


class AmityPollPostComposerPageSample {
    /* begin_sample_code
      gist_id: b8db86ef2c7776380bfa5b3fffffbeec
      filename: AmityPollPostComposerPageSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Poll post composer page
      */
    @Composable
    fun pollPostComposerPage(
        targetId: String,
        targetType: AmityPost.TargetType,
    ) {
        AmityPollPostComposerPage(
            targetId = targetId,
            targetType = targetType,
        )
    }
    /* end_sample_code */
}