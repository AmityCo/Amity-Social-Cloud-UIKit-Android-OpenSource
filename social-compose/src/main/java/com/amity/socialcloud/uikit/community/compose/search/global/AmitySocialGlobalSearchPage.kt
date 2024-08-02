package com.amity.socialcloud.uikit.community.compose.search.global

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponent
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

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> viewModel.setSearchType(AmityGlobalSearchType.COMMUNITY)
            1 -> viewModel.setSearchType(AmityGlobalSearchType.USER)
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
            )

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                divider = {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4
                    )
                },
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = AmityTheme.colors.primary
                    )
                },
                contentColor = AmityTheme.colors.primary,
                modifier = modifier.padding(bottom = 8.dp)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        selectedTabIndex = 0
                    }
                ) {
                    Text(
                        text = "Communities",
                        style = AmityTheme.typography.title.copy(
                            color = if (selectedTabIndex == 0) AmityTheme.colors.primary else AmityTheme.colors.baseShade3
                        ),
                        modifier = modifier.padding(vertical = 12.dp)
                    )
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        selectedTabIndex = 1
                    }
                ) {
                    Text(
                        text = "Users",
                        style = AmityTheme.typography.title.copy(
                            color = if (selectedTabIndex == 1) AmityTheme.colors.primary else AmityTheme.colors.baseShade3
                        ),
                        modifier = modifier.padding(vertical = 12.dp)
                    )
                }
            }

            when (selectedTabIndex) {
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