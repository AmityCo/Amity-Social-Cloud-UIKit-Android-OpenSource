package com.amity.socialcloud.uikit.community.compose.community.trending

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityJoinCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityTrendingCommunityShimmer

@Composable
fun AmityTrendingCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onStateChanged: (AmityTrendingCommunitiesViewModel.CommunityListState) -> Unit = {}
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityTrendingCommunitiesViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val communities = viewModel.getTrendingCommunities().collectAsState(initial = emptyList())
    val communityListState by viewModel.communityListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "trending_communities"
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 554.dp)
                .padding(horizontal = 16.dp)
        ) {
            when (communityListState) {
                AmityTrendingCommunitiesViewModel.CommunityListState.SUCCESS -> {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text(
                                text = "Trending now",
                                style = AmityTheme.typography.title
                            )
                        }
                    }
                    item {
                        Spacer(modifier = modifier.height(8.dp))
                    }
                    items(
                        count = communities.value.size,
                        key = { index -> index }
                    ) { index ->
                        val community = communities.value[index]

                        AmityJoinCommunityView(
                            modifier = modifier,
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            community = community,
                            label = "0" + (index + 1),
                            onClick = {
                                val intent =
                                    AmityCommunityProfilePageActivity.newIntent(
                                        context = context,
                                        communityId = community.getCommunityId()
                                    )
                                context.startActivity(intent)
                            })

                        Spacer(modifier = modifier.height(8.dp))
                        if (index < communities.value.size - 1) {
                            HorizontalDivider(
                                color = AmityTheme.colors.divider
                            )
                            Spacer(modifier = modifier.height(8.dp))
                        }
                    }
                }

                AmityTrendingCommunitiesViewModel.CommunityListState.LOADING -> {
                    item {
                        AmityTrendingCommunityShimmer()
                    }
                }

                AmityTrendingCommunitiesViewModel.CommunityListState.EMPTY -> {

                }

                AmityTrendingCommunitiesViewModel.CommunityListState.ERROR -> {

                }
            }
            onStateChanged(communityListState)
        }
    }
}