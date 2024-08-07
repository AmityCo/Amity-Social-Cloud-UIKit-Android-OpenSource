package com.amity.snipet.verifier.social.search

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.search.components.AmityTopSearchBarComponent
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel

class AmityTopSearchBarComponent {
    /* begin_sample_code
      gist_id: 9790958b617ea4812cc9493738092509
      filename: AmityTopSearchBarComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Top search bar component
      */
    @Composable
    fun composeTopSearchBarComponent(
        viewModel: AmityGlobalSearchViewModel,
    ) {
        AmityTopSearchBarComponent(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}