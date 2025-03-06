package com.amity.socialcloud.uikit.community.compose.search.global

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRowItem
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityPostSearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityTopSearchBarComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponent

@Composable
fun AmitySocialGlobalSearchPage(
    modifier: Modifier = Modifier,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityGlobalSearchViewModel>(viewModelStoreOwner = viewModelStoreOwner)


    val tabs = remember {
        listOf(
            //AmityTabRowItem(title = "Posts"),
            AmityTabRowItem(title = "Communities"),
            AmityTabRowItem(title = "Users"),
        )
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> viewModel.setSearchType(AmityGlobalSearchType.COMMUNITY)
            1 -> viewModel.setSearchType(AmityGlobalSearchType.USER)
//            0 -> viewModel.setSearchType(AmityGlobalSearchType.POST)
//            1 -> viewModel.setSearchType(AmityGlobalSearchType.COMMUNITY)
//            2 -> viewModel.setSearchType(AmityGlobalSearchType.USER)
        }
    }

    AmityBasePage(
        pageId = "social_global_search_page"
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            AmityTopSearchBarComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                viewModel = viewModel,
                shouldShowKeyboard = true
            )

            AmityTabRow(
                tabs = tabs,
                selectedIndex = selectedTabIndex
            ) {
                selectedTabIndex = it
            }

            Spacer(modifier.height(8.dp))

            when (selectedTabIndex) {
//                0 -> AmityPostSearchResultComponent(
//                    modifier = modifier,
//                    pageScope = getPageScope(),
//                    viewModel = viewModel
//                )

                0 -> {
                    AmityBaseComponent(
                        pageScope = getPageScope(),
                        componentId = "community_search_result"
                    ) {
                        AmityCommunitySearchResultComponent(
                            modifier = modifier,
                            pageScope = getPageScope(),
                            componentScope = getComponentScope(),
                            viewModel = viewModel,
                        )
                    }
                }

                1 -> AmityUserSearchResultComponent(
                    modifier = modifier,
                    pageScope = getPageScope(),
                    viewModel = viewModel
                )
            }
        }
    }
}