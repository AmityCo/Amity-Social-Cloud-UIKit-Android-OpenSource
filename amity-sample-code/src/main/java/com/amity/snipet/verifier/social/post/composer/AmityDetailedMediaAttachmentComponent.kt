package com.amity.snipet.verifier.social.post.composer

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityDetailedMediaAttachmentComponent

class AmityDetailedMediaAttachmentComponent {
    /* begin_sample_code
      gist_id: 46781a6184584dd289482418c3148e93
      filename: AmityDetailedMediaAttachmentComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Detailed media attachment component
      */
    @Composable
    fun composeDetailedMediaAttachmentComponent(
        viewModel: AmityMediaAttachmentViewModel,
    ) {
        AmityDetailedMediaAttachmentComponent(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}