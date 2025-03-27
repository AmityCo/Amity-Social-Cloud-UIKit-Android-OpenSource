package com.amity.socialcloud.uikit.community.compose.community.trending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityJoinCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityTrendingCommunityShimmer


@Composable
fun AmityTrendingCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onStateChanged: (AmityTrendingCommunitiesViewModel.CommunityListState) -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.exploreComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityTrendingCommunitiesViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // Remember the Flow of communities to prevent recreating on recomposition
    val communitiesFlow = remember {
        viewModel.getTrendingCommunities()
    }

    val communities = communitiesFlow.collectAsState(initial = emptyList())
    val communityListState by viewModel.communityListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "trending_communities"
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            when (communityListState) {
                AmityTrendingCommunitiesViewModel.CommunityListState.SUCCESS -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(38.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Trending now",
                            style = AmityTheme.typography.titleLegacy
                        )
                    }

                    communities.value.forEachIndexed { index, community ->
                        AmityJoinCommunityView(
                            modifier = modifier,
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            community = community,
                            label = "0" + (index + 1),
                            onClick = {
                                behavior.goToCommunityProfilePage(
                                    context = context,
                                    communityId = community.getCommunityId(),
                                )
                            },
                        )
                    }
                }

                AmityTrendingCommunitiesViewModel.CommunityListState.LOADING -> {
                    AmityTrendingCommunityShimmer()
                }

                AmityTrendingCommunitiesViewModel.CommunityListState.EMPTY -> {
                    // Empty state handler if needed
                }

                AmityTrendingCommunitiesViewModel.CommunityListState.ERROR -> {
                    // Error state handler if needed
                }

            }
            Spacer(modifier = modifier.height(24.dp))
            onStateChanged(communityListState)
        }
    }
}