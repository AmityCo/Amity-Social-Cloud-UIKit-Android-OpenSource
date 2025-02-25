package com.amity.snipet.verifier.social.post.composer

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityMediaAttachmentComponent

class AmityMediaAttachmentComponent {
    /* begin_sample_code
      gist_id: 8b6de2187d33eb49dcefba27fadd2bfe
      filename: AmityMediaAttachmentComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Media attachment component
      */
    @Composable
    fun composeMediaAttachmentComponent(
        viewModel: AmityMediaAttachmentViewModel,
    ) {
        AmityMediaAttachmentComponent(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}