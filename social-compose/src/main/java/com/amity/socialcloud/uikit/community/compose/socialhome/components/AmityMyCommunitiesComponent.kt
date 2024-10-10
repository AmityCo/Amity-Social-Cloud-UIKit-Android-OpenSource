package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityCommunityListShimmer

@Composable
fun AmityMyCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.myCommunitiesComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmitySocialHomePageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val communities = remember { viewModel.getMyCommunities() }.collectAsLazyPagingItems()
    val communityListState by viewModel.communityListState.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {
        AmityNewsFeedDivider()
        Spacer(modifier.height(8.dp))
        AmityBaseComponent(
            pageScope = pageScope,
            componentId = "my_communities"
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                AmitySocialHomePageViewModel.CommunityListState.from(
                    loadState = communities.loadState.refresh,
                    itemCount = communities.itemCount,
                ).let(viewModel::setCommunityListState)

                when (communityListState) {
                    AmitySocialHomePageViewModel.CommunityListState.SUCCESS -> {
                        items(
                            count = communities.itemCount,
                            key = { index -> index }
                        ) { index ->
                            val community = communities[index] ?: return@items

                            AmityCommunityView(
                                modifier = modifier,
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                community = community,
                            ) {
                                behavior.goToCommunityProfilePage(
                                    context = context,
                                    community = community,
                                )
                            }
                        }
                    }

                    AmitySocialHomePageViewModel.CommunityListState.LOADING -> {
                        item {
                            AmityCommunityListShimmer()
                        }
                    }

                    AmitySocialHomePageViewModel.CommunityListState.EMPTY -> {}
                    AmitySocialHomePageViewModel.CommunityListState.ERROR -> {}
                }
            }
        }
    }
}