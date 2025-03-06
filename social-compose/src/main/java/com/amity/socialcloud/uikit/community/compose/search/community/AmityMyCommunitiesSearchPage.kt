package com.amity.socialcloud.uikit.community.compose.search.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityTopSearchBarComponent
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchType
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel


@Composable
fun AmityMyCommunitiesSearchPage(
    modifier: Modifier = Modifier,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityGlobalSearchViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    LaunchedEffect(Unit) {
        viewModel.setSearchType(AmityGlobalSearchType.MY_COMMUNITY)
    }

    AmityBasePage(
        pageId = "my_communities_search_page"
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            AmityTopSearchBarComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                viewModel = viewModel,
                shouldShowKeyboard = true,
            )

            AmityCommunitySearchResultComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                viewModel = viewModel,
            )
        }
    }
}