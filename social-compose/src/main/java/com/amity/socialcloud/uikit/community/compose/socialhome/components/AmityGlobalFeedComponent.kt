package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.amityGlobalFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.pinnedPostIds
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.postIds
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.renderableFeedItemCount
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.renderablePinnedPosts
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel

@Composable
fun AmityGlobalFeedComponent(
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
    val pinnedPosts = viewModel.globalPinnedPosts.collectAsState()

    val lazyListState = rememberLazyListState()
    // NOTE: this component renders locally created posts and the paginated feed, but NOT a
    // pinned section — it passes pinnedPosts to the renderer only so they can be de-duplicated
    // and badged. Pinned posts must therefore NOT suppress its empty state: counting content
    // this component never draws would leave a blank screen with no empty state.
    val visibleCreatedPosts = AmityPostComposerHelper.getCreatedPosts()
    val renderableItemCount = posts.itemSnapshotList.items.renderableFeedItemCount(
        pinnedPostIds = pinnedPosts.value.pinnedPostIds(),
        createdPostIds = visibleCreatedPosts.postIds(),
    ) + visibleCreatedPosts.size

    val postListState = derivePostListState(
        refreshLoadState = posts.loadState.refresh,
        appendLoadState = posts.loadState.append,
        renderableItemCount = renderableItemCount,
    )
    RequestNextRenderableFeedPage(posts, renderableItemCount)

    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isGlobalFeedRefreshing.collectAsState()

    var refreshKey by remember { mutableIntStateOf(0) }

    val onRefresh = {
        refreshKey++
        viewModel.setGlobalFeedRefreshing()
        posts.refresh()
        viewModel.clearCreatedPostsForRefresh()
    }

    LaunchedEffect(postListState) {
        viewModel.setPostListState(postListState)
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "global_feed"
    ) {
        PullToRefreshBox(
            // PDT-4657: pullRefreshState was created and handed to the indicator, but never
            // to the box, so the box drove its own separate state and the indicator never
            // tracked the drag -- the pull read as doing nothing.
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    isRefreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = modifier.fillMaxSize()
            ) {
                amityGlobalFeedLLS(
                    modifier = modifier,
                    pageScope = pageScope,
                    globalPosts = posts,
                    pinnedPosts = pinnedPosts,
                    postListState = postListState,
                    onClick = { post ->
                        behavior.goToPostDetailPage(
                            context = AmityGlobalFeedComponentBehavior.Context(
                                pageContext = context,
                                target = post.getTarget()
                            ),
                            id = post.getPostId(),
                            autoFocusCommentInput = true,
                        )
                    },
                    onCreateCommunityClicked = {
                        behavior.goToCreateCommunityPage(context)
                    },
                    onExploreCommunityClicked = {
                        onExploreRequested()
                    },
                    refreshKey = refreshKey,
                )
            }
        }
    }
}
