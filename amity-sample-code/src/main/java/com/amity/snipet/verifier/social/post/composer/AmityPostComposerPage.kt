package com.amity.snipet.verifier.social.post.composer

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPage

class AmityPostComposerPage {
    /* begin_sample_code
      gist_id: 080eabe5d6f6e1ad23b2f8eb185795da
      filename: AmityPostComposerPage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Post composer page
      */
    @Composable
    fun composePostComposerPage(
        options: AmityPostComposerOptions
    ) {
        AmityPostComposerPage(
            options = options,
        )
    }
    /* end_sample_code */
}