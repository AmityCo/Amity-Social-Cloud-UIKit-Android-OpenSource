package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityCommunityListShimmer

@Composable
fun AmityCommunitySearchResultComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    viewModel: AmityGlobalSearchViewModel,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communitySearchResultComponentBehavior
    }

    val keyword by viewModel.keyword.collectAsState("")
    val searchType by viewModel.searchType.collectAsState()

    val communities = remember(keyword, searchType) {
        viewModel.searchCommunities()
    }.collectAsLazyPagingItems()

    val loadState by viewModel.communityListState.collectAsState()

    if (keyword.isNotEmptyOrBlank()) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            AmityGlobalSearchViewModel.CommunityListState.from(
                loadState = communities.loadState.refresh,
                itemCount = communities.itemCount,
            ).let(viewModel::setCommunityListState)

            when (loadState) {
                AmityGlobalSearchViewModel.CommunityListState.EMPTY -> {
                    item {
                        AmityEmptySearchResultComponent(modifier)
                    }
                }

                AmityGlobalSearchViewModel.CommunityListState.LOADING -> {
                    item {
                        AmityCommunityListShimmer()
                    }
                }

                AmityGlobalSearchViewModel.CommunityListState.SUCCESS -> {
                    items(
                        count = communities.itemCount,
                        key = { index -> index }
                    ) { index ->
                        val community = communities[index] ?: return@items

                        AmityCommunityView(
                            modifier = modifier,
                            pageScope = pageScope,
                            componentScope = componentScope,
                            community = community,
                        ) {
                            behavior.goToCommunityProfilePage(
                                context = context,
                                community = it,
                            )
                        }
                    }
                }
            }
        }
    }
}