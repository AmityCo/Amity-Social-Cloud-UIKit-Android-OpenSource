package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LocalPinnableContainer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.amityGlobalFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.amityGlobalPinnedFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AmityNewsFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onExploreRequested: () -> Unit = {},
) {
    val context = LocalContext.current

    val behavior = remember {
        AmitySocialBehaviorHelper.globalFeedComponentBehavior
    }

    val viewModel = viewModel<AmitySocialHomePageViewModel>()
    val posts = remember { viewModel.getGlobalFeed() }.collectAsLazyPagingItems()
    val pinnedPosts = remember {
        viewModel.getGlobalPinnedPosts()
    }.collectAsState(emptyList())

    val lazyListState = rememberLazyListState()
    val postListState by viewModel.postListState.collectAsState()

    val isRefreshing by viewModel.isGlobalFeedRefreshing.collectAsState()
    val isPullRefreshIndicatorVisible by viewModel.isPullRefreshIndicatorVisible.collectAsState()
    val isStoryTabVisible by viewModel.isStoryTabVisible.collectAsState()

    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isPullRefreshIndicatorVisible,
        onRefresh = {
            viewModel.setGlobalFeedRefreshing(showIndicator = true)
            posts.refresh()
            scope.launch {
                viewModel.refreshGlobalPinnedPosts()
            }
            AmityPostComposerHelper.clear()
        }
    )


    LaunchedEffect(Unit) {
        viewModel.setGlobalFeedRefreshing(showIndicator = false)
    }

    LaunchedEffect(isStoryTabVisible) {
        if (isStoryTabVisible) {
            scope.launch {
                delay(100)
                if (lazyListState.firstVisibleItemIndex != 0) {
                    lazyListState.scrollToItem(0)
                }
            }
        }
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "newsfeed"
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(
                state = lazyListState,
                modifier = modifier.fillMaxSize()
            ) {
                AmitySocialHomePageViewModel.PostListState.from(
                    loadState = posts.loadState.refresh,
                    itemCount = posts.itemCount,
                ).let(viewModel::setPostListState)

                item(key = "dummy_story_tab") {
                    LocalPinnableContainer.current?.pin()
                    if (isRefreshing) {
                        Column(
                            modifier = Modifier.height(126.dp)
                        ) {
                            AmityNewsFeedDivider()
                            LazyRow(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(start = 16.dp),
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                items(6) {
                                    AmityStoryShimmer(modifier)
                                }
                            }
                        }
                    }
                }

                item(key = "story_tab") {
                    LocalPinnableContainer.current?.pin()
                    if (!isRefreshing) {
                        val storyTabHeight = if (isStoryTabVisible) 126.dp else 0.dp
                        Box(
                            modifier = Modifier.height(storyTabHeight)
                        ) {
                            AmityStoryTabComponent(
                                type = AmityStoryTabComponentType.GlobalFeed(
                                    refreshEventFlow = viewModel.isGlobalFeedRefreshing,
                                    onStateChanged = {
                                        viewModel.setStoryTabState(it)
                                    }
                                )
                            )
                        }
                    }
                }

                item {
                    AmityNewsFeedDivider()
                }

                if (isRefreshing) {
                    items(4) {
                        AmityPostShimmer()
                        AmityNewsFeedDivider()
                    }
                } else {
                    if (posts.itemCount > 0) {
                        amityGlobalPinnedFeedLLS(
                            modifier = modifier,
                            pageScope = pageScope,
                            pinnedPosts = pinnedPosts,
                            onClick = {
                                behavior.goToPostDetailPage(
                                    context = context,
                                    id = it.getPostId(),
                                    category = AmityPostCategory.GLOBAL
                                )
                            }
                        )
                    }
                }

                amityGlobalFeedLLS(
                    modifier = modifier,
                    pageScope = pageScope,
                    globalPosts = posts,
                    pinnedPosts = pinnedPosts,
                    postListState = postListState,
                    onClick = {
                        behavior.goToPostDetailPage(
                            context = context,
                            id = it.getPostId()
                        )
                    },
                    onCreateCommunityClicked = {
                        behavior.goToCreateCommunityPage(context)
                    },
                    onExploreCommunityClicked = {
                        onExploreRequested()
                    }
                )
            }

            PullRefreshIndicator(
                refreshing = isPullRefreshIndicatorVisible,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}