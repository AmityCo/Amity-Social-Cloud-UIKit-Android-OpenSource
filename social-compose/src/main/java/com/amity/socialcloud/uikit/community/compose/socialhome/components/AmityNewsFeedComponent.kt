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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.pinnedPostIds
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.postIds
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.renderableFeedItemCount
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.renderablePinnedPosts
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AmityNewsFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onExploreRequested: () -> Unit = {},
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()

    val behavior = remember {
        AmitySocialBehaviorHelper.globalFeedComponentBehavior
    }

    val viewModel = viewModel<AmitySocialHomePageViewModel>()
    val posts = remember { viewModel.getGlobalFeed() }.collectAsLazyPagingItems()
    val pinnedPosts = viewModel.globalPinnedPosts.collectAsState()
    val pinnedPostsState by viewModel.globalPinnedPostsState.collectAsState()

    val lazyListState = rememberLazyListState()
    // Renderable content across every source this feed shows. The empty state must reflect what
    // the user can actually SEE, so each source is filtered by the one shared predicate before
    // being counted — a source holding only unsupported or deleted posts contributes nothing.
    val visiblePinnedPosts = if (AmityFeedAuxiliarySources.FOLLOWING_SHOWS_PINNED_POSTS) {
        pinnedPosts.value.renderablePinnedPosts()
    } else {
        emptyList()
    }
    val visibleCreatedPosts = if (AmityFeedAuxiliarySources.FOLLOWING_SHOWS_CREATED_POSTS) {
        AmityPostComposerHelper.getCreatedPosts()
    } else {
        emptyList()
    }
    val renderableItemCount = posts.itemSnapshotList.items.renderableFeedItemCount(
        // The set amityGlobalFeedLLS de-dups against, exactly: EVERY pinned id, not only the
        // renderable ones. A pinned entry whose post payload has not loaded still carries a
        // postId and still suppresses that paginated row, so counting it here keeps a row the
        // renderer drops.
        pinnedPostIds = pinnedPosts.value.pinnedPostIds(),
        createdPostIds = visibleCreatedPosts.postIds(),
    ) + visiblePinnedPosts.size + visibleCreatedPosts.size

    // Only ask the paginated load state when NOTHING renderable exists; otherwise the feed has
    // content and is a success regardless of what the paginated source alone would say.
    val postListState = derivePostListState(
        refreshLoadState = posts.loadState.refresh,
        appendLoadState = posts.loadState.append,
        renderableItemCount = renderableItemCount,
        auxiliaryContentState = if (AmityFeedAuxiliarySources.FOLLOWING_SHOWS_PINNED_POSTS) {
            pinnedPostsState.contentState
        } else {
            AmitySocialHomePageViewModel.AuxiliaryContentState.READY
        },
    )
    RequestNextRenderableFeedPage(posts, renderableItemCount)

    val isRefreshing by viewModel.isGlobalFeedRefreshing.collectAsState()
    val isPullRefreshIndicatorVisible by viewModel.isPullRefreshIndicatorVisible.collectAsState()
    val isStoryTabVisible by viewModel.isStoryTabVisible.collectAsState()

    val scope = rememberCoroutineScope()

    var refreshKey by remember { mutableIntStateOf(0) }

    val onRefresh = {
        refreshKey++
        viewModel.setGlobalFeedRefreshing(showIndicator = true)
        posts.refresh()
        scope.launch {
            viewModel.refreshGlobalPinnedPosts()
        }
        viewModel.clearCreatedPostsForRefresh()
    }


    LaunchedEffect(Unit) {
        viewModel.setGlobalFeedRefreshing(showIndicator = false)
    }

    LaunchedEffect(postListState) {
        viewModel.setPostListState(postListState)
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
        PullToRefreshBox(
            // PDT-4657: pullRefreshState was created and handed to the indicator, but never
            // to the box, so the box drove its own separate state and the indicator never
            // tracked the drag -- the pull read as doing nothing.
            state = pullRefreshState,
            isRefreshing = isPullRefreshIndicatorVisible,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    isRefreshing = isPullRefreshIndicatorVisible,
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
                item(key = "story_tab") {
                    LocalPinnableContainer.current?.pin()
                    val storyTabHeight = if (isStoryTabVisible) 130.dp else 0.dp
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

                item {
                    AmityNewsFeedDivider()
                }

                if (isRefreshing && renderableItemCount == 0) {
                    items(4) {
                        AmityPostShimmer()
                        AmityNewsFeedDivider()
                    }
                } else {
                    if (visiblePinnedPosts.isNotEmpty()) {
                        amityGlobalPinnedFeedLLS(
                            modifier = modifier,
                            pageScope = pageScope,
                            pinnedPosts = pinnedPosts,
                            onClick = { post ->
                                behavior.goToPostDetailPage(
                                    context = AmityGlobalFeedComponentBehavior.Context(
                                        pageContext = context,
                                        target = post.getTarget()
                                    ),
                                    id = post.getPostId(),
                                    category = AmityPostCategory.GLOBAL,
                                    autoFocusCommentInput = true,
                                )
                            },
                            onClipClicked = {
                                behavior.goToClipFeedPage(
                                    context = context,
                                    postId = it.getPostId()
                                )
                            },
                            refreshKey = refreshKey,
                        )
                    }
                }

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
                    onClipClick = { childPost ->
                        behavior.goToClipFeedPage(
                            context = context,
                            postId = childPost.getPostId()
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
