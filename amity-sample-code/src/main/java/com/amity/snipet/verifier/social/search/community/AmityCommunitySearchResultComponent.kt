package com.amity.snipet.verifier.social.search.community

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel

class AmityCommunitySearchResultComponent {
    /* begin_sample_code
      gist_id: 3772b820749808df479b52f0da340018
      filename: AmityCommunitySearchResultComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
      description: Community search result component
      */
    @Composable
    fun composeCommunitySearchResultComponent(
        viewModel: AmityGlobalSearchViewModel,
    ) {
        AmityCommunitySearchResultComponent(
            viewModel = viewModel,
        )
    }
    /* end_sample_code */
}