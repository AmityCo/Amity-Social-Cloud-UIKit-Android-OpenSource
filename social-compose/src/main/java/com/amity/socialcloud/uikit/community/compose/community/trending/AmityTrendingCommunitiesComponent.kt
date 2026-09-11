package com.amity.socialcloud.uikit.community.compose.community.trending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityJoinCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityTrendingCommunityShimmer
import kotlin.collections.map
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString


@Composable
fun AmityTrendingCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onStateChanged: (AmityTrendingCommunitiesViewModel.CommunityListState) -> Unit = {},
    refreshKey: Int = 0,
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
    var lastCommunities by remember { mutableStateOf(emptyList<AmityCommunity>()) }
    val communitiesFlow = remember(refreshKey) {
        viewModel.getTrendingCommunities()
    }

    val communities by communitiesFlow.collectAsState(initial = lastCommunities)
    LaunchedEffect(communities) {
        if (communities.isNotEmpty()) {
            lastCommunities = communities
        }
    }
    val communityListState by viewModel.communityListState.collectAsState()
    val resolvedListState = if (communities.isNotEmpty()) {
        AmityTrendingCommunitiesViewModel.CommunityListState.SUCCESS
    } else {
        communityListState
    }

    val joinRequests by viewModel.joinRequestList.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "trending_communities"
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            when (resolvedListState) {
                AmityTrendingCommunitiesViewModel.CommunityListState.SUCCESS -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(38.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = amitySocialString("amity_social_button_trending_now"),
                            style = AmityTheme.typography.titleLegacy
                        )
                    }

                    communities.forEachIndexed { index, community ->
                        val joinRequest = community.getLocalJoinRequest()?.find {
                            it.getRequestorPublicId() == AmityCoreClient.getUserId() &&
                                    it.getStatus() == AmityJoinRequestStatus.PENDING
                        }

                        AmityJoinCommunityView(
                            modifier = modifier,
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            community = community,
                            joinRequest = joinRequest,
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