package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryTargetElement
import com.amity.socialcloud.uikit.community.compose.story.target.utils.toRingUiState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AmityStoryGlobalTabComponent(
    modifier: Modifier = Modifier,
    refreshEventFlow: MutableStateFlow<Boolean> = MutableStateFlow(false),
    onStateChanged: (AmityStoryGlobalTabViewModel.TargetListState) -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    val isRefreshing by refreshEventFlow.collectAsState()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityStoryGlobalTabViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val targets = remember {
        viewModel.getTargets()
    }.collectAsLazyPagingItems()

    val targetListState by viewModel.targetListState.collectAsState()

    LaunchedEffect(targets.itemSnapshotList.items) {
        AmityStorySharedGlobalTargetsObject.setTargets(targets.itemSnapshotList.items)
    }

    LaunchedEffect(targets.itemCount) {
        viewModel.prefetchStoriesFromTargets(targets.itemSnapshotList.items)
    }

    LaunchedEffect(isRefreshing) {
        if(isRefreshing) {
            targets.refresh()
        }
    }

    AmityBaseComponent(componentId = "story_tab_component") {
        AmityStoryGlobalTabViewModel.TargetListState.from(
            loadState = targets.loadState,
            itemCount = targets.itemCount,
        ).let {
            onStateChanged(it)
            viewModel.setTargetListState(it)
        }
        onStateChanged(targetListState)
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            if (
                targetListState == AmityStoryGlobalTabViewModel.TargetListState.LOADING
                || targetListState == AmityStoryGlobalTabViewModel.TargetListState.SUCCESS
            ) {
                AmityNewsFeedDivider(modifier)
            }
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                modifier = modifier
                    .fillMaxWidth()
            ) {
                when (targetListState) {

                    AmityStoryGlobalTabViewModel.TargetListState.EMPTY -> {

                    }

                    AmityStoryGlobalTabViewModel.TargetListState.LOADING -> {
                        items(6) {
                            AmityStoryShimmer(modifier)
                        }
                    }

                    AmityStoryGlobalTabViewModel.TargetListState.SUCCESS -> {
                        items(
                            count = targets.itemCount,
                            key = { targets[it]?.getTargetId() ?: it }
                        ) { index ->
                            val target = targets[index]
                            val community = if (target is AmityStoryTarget.COMMUNITY) {
                                target.getCommunity()
                            } else {
                                null
                            }
                            if (target == null || community == null) return@items

                            AmityStoryTargetElement(
                                modifier = modifier,
                                componentScope = getComponentScope(),
                                community = community,
                                ringUiState = target.toRingUiState(),
                                isCommunityTarget = false,
                                hasManageStoryPermission = false,
                            ) {
                                AmityStorySharedGlobalTargetsObject.setSelectedTarget(
                                    index = index,
                                    target = target
                                )

                                behavior.goToViewStoryPage(
                                    context = context,
                                    type = AmityViewStoryPageType.GlobalFeed(
                                        communityId = target.getTargetId()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmityStoryShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.size(width = 64.dp, height = 118.dp)
    ) {
        Box(
            modifier = modifier
                .size(64.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(64.dp)
                )
        )
        Box(
            Modifier
                .padding(top = 8.dp)
                .width(54.dp)
                .height(8.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
}

@Preview
@Composable
fun PreviewAmityStoryGlobalTargetTabComponent() {
    AmityStoryGlobalTabComponent()
}