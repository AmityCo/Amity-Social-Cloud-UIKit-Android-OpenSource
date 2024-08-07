package com.amity.snipet.verifier.social.search.global

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel

class AmityUserSearchResultComponent {
    /* begin_sample_code
      gist_id: 3349d4e86e966780c2ac0195ffe10dbf
      filename: AmityUserSearchResultComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: User search result component
      */
    @Composable
    fun composeUserSearchResultComponent(
        viewModel: AmityGlobalSearchViewModel,
    ) {
        AmityUserSearchResultComponent(
            viewModel = viewModel,
        )
    }
    /* end_sample_code */
}