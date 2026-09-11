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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LocalPinnableContainer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.error.AmityForYouFeedDisabledError
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.paging.feed.global.amityForYouFeedLLS
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * For You Feed content component — sibling to [AmityNewsFeedComponent].
 *
 * Spec: front-end-tech-specs/UIKIT/components/AmityForYouFeedComponent/v1.md
 *
 * - Globally pinned / featured posts rendered above the ranked feed (same source
 *   as the Following tab via `getGlobalPinnedPosts()`).
 * - Newly created posts from [AmityPostComposerHelper] rendered immediately below
 *   the pinned section, above the paginated ranked feed. Deduplication prevents
 *   a post appearing twice when the paginated feed catches up.
 * - Sponsored posts injected client-side by [AmityAdInjector] in the ViewModel.
 * - **Pull-to-refresh does NOT drop the cursor** (REQ-005). The gesture renders
 *   the standard refresh animation as user feedback; the SDK keeps reading from
 *   the active snapshot.
 * - [AmityPost.analytics.markAsViewed] fires on every ranked post entering the
 *   viewport (REQ-008). Featured / created / sponsored items are excluded via
 *   key-prefix filtering (`"foryou_"`).
 * - [AmityPost.analytics.markAsMeaningfullyViewed] fires at ≥1s ≥50% visibility
 *   for ranked posts (REQ-009), passing the paginated index as feedRenderPosition.
 * - On [AmityForYouFeedDisabledError] surfaced via LoadState.Error, invokes
 *   [onFeatureDisabled] so the parent page can drop the tab (REQ-023).
 */
@Composable
fun AmityForYouFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onFeatureDisabled: () -> Unit = {},
    onSwitchToFollowingRequested: () -> Unit = {},
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()
    val behavior = remember { AmitySocialBehaviorHelper.globalFeedComponentBehavior }

    val viewModel = viewModel<AmitySocialHomePageViewModel>()

    // REQ-005: flow collected once; SDK cursor never dropped on pull-to-refresh.
    val posts = remember { viewModel.getForYouFeed() }.collectAsLazyPagingItems()
    val pinnedPosts = viewModel.globalPinnedPosts.collectAsState()
    val pinnedPostsState by viewModel.globalPinnedPostsState.collectAsState()
    val scope = rememberCoroutineScope()

    val isRefreshing by viewModel.isGlobalFeedRefreshing.collectAsState()
    val isStoryTabVisible by viewModel.isStoryTabVisible.collectAsState()

    val refreshLoadState = posts.loadState.refresh
    val appendLoadState = posts.loadState.append
    val isFeedExhausted =
        appendLoadState is LoadState.NotLoading &&
            appendLoadState.endOfPaginationReached  // REQ-015

    // Renderable content only, and locally created posts count too — a just-created post on an
    // otherwise empty feed must end the first-page shimmer instead of hiding behind it.
    val visiblePinnedPosts = pinnedPosts.value.renderablePinnedPosts()
    val visibleCreatedPosts = AmityPostComposerHelper.getCreatedPosts()
    val renderableItemCount = posts.itemSnapshotList.items.renderableFeedItemCount(
        // Same as AmityNewsFeedComponent: amityForYouFeedLLS de-dups on EVERY pinned id.
        pinnedPostIds = pinnedPosts.value.pinnedPostIds(),
        createdPostIds = visibleCreatedPosts.postIds(),
    ) + visiblePinnedPosts.size + visibleCreatedPosts.size
    val hasVisibleContent = renderableItemCount > 0
    val isLoadingFirstPage = !hasVisibleContent &&
        (refreshLoadState is LoadState.Loading ||
            !isFeedExhausted ||
            pinnedPostsState.contentState ==
            AmitySocialHomePageViewModel.AuxiliaryContentState.LOADING)
    val canShowCaughtUp = isFeedExhausted &&
        pinnedPostsState !is AmitySocialHomePageViewModel.GlobalPinnedPostsState.Loading
    val isPinnedRefresh =
        (pinnedPostsState as? AmitySocialHomePageViewModel.GlobalPinnedPostsState.Loading)
            ?.isRefresh == true
    val isPullRefreshing = isLoadingFirstPage || isPinnedRefresh
    RequestNextRenderableFeedPage(posts, renderableItemCount)

    // REQ-023: surface AmityForYouFeedDisabledError to the parent page.
    LaunchedEffect(refreshLoadState) {
        val err = (refreshLoadState as? LoadState.Error)?.error
        if (err is AmityForYouFeedDisabledError) {
            onFeatureDisabled()
        }
    }

    // REQ-008: markAsViewed on every ranked post entering the viewport.
    // Filters by "foryou_" key prefix to exclude pinned / created / ad items (REQ-012).
    val viewedReported = remember { mutableSetOf<String>() }
    LaunchedEffect(lazyListState, posts.itemCount) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .distinctUntilChanged()
            .filterNotNull()
            .collect { visibleItems ->
                visibleItems
                    .filter {
                        val key = it.key as? String ?: return@filter false
                        key.startsWith("foryou_") && !key.startsWith("foryou_ad_")
                    }
                    .forEach { itemInfo ->
                        val postId = (itemInfo.key as String).removePrefix("foryou_")
                        if (viewedReported.add(postId)) {
                            for (i in 0 until posts.itemCount) {
                                val item = posts.peek(i) as? AmityListItem.PostItem ?: continue
                                if (item.post.getPostId() == postId) {
                                    item.post.analytics().markAsViewed()
                                    break
                                }
                            }
                        }
                    }
            }
    }

    // REQ-009: markAsMeaningfullyViewed at ≥1s ≥50% visibility for ranked posts.
    // Polling every 250ms because visibility-without-scroll doesn't emit snapshot events.
    // Filters by "foryou_" key prefix to exclude pinned / created / ad items (REQ-012).
    val meaningfulFired = remember { mutableSetOf<String>() }
    val visibleSince = remember { mutableMapOf<String, Long>() }
    LaunchedEffect(lazyListState, posts.itemCount) {
        while (true) {
            val info = lazyListState.layoutInfo
            val now = System.currentTimeMillis()
            val currentlyEligible = mutableSetOf<String>()

            info.visibleItemsInfo
                .filter {
                    val key = it.key as? String ?: return@filter false
                    key.startsWith("foryou_") && !key.startsWith("foryou_ad_")
                }
                .forEach { itemInfo ->
                    val postId = (itemInfo.key as String).removePrefix("foryou_")

                    var paginatedIdx: Int? = null
                    var post: AmityPost? = null
                    for (i in 0 until posts.itemCount) {
                        val item = posts.peek(i) as? AmityListItem.PostItem ?: continue
                        if (item.post.getPostId() == postId) {
                            paginatedIdx = i
                            post = item.post
                            break
                        }
                    }
                    if (paginatedIdx == null || post == null) return@forEach

                    val visibleStart = maxOf(itemInfo.offset, info.viewportStartOffset)
                    val visibleEnd = minOf(itemInfo.offset + itemInfo.size, info.viewportEndOffset)
                    val visibleHeight = (visibleEnd - visibleStart).coerceAtLeast(0)
                    val fraction = if (itemInfo.size > 0) visibleHeight.toFloat() / itemInfo.size else 0f

                    if (fraction >= 0.5f) {
                        currentlyEligible.add(postId)
                        val since = visibleSince.getOrPut(postId) { now }
                        if (!meaningfulFired.contains(postId) && (now - since) >= 1000L) {
                            meaningfulFired.add(postId)
                            post.analytics().markAsMeaningfullyViewed(paginatedIdx)
                        }
                    }
                }

            visibleSince.keys.retainAll(currentlyEligible)

            delay(250L)
        }
    }

    var refreshKey by remember { mutableIntStateOf(0) }

    // REQ-005: keep the ranked cursor, but refresh the independent pinned source.
    val onRefresh: () -> Unit = {
        refreshKey++
        scope.launch { viewModel.refreshGlobalPinnedPosts() }
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "amity_for_you_feed_component"
    ) {
        PullToRefreshBox(
            // PDT-4657: pullRefreshState was created and handed to the indicator, but never
            // to the box, so the box drove its own separate state and the indicator never
            // tracked the drag -- the pull read as doing nothing.
            state = pullRefreshState,
            isRefreshing = isPullRefreshing,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    isRefreshing = isPullRefreshing,
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
                item(key = "foryou_story_tab") {
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

                item(key = "foryou_story_divider") {
                    AmityNewsFeedDivider()
                }

                if (isLoadingFirstPage) {
                    items(4) {
                        AmityPostShimmer()
                    }
                } else {
                    // Featured / globally pinned posts — always at the top (REQ-013 precursor).
                    amityGlobalPinnedFeedLLS(
                        modifier = modifier,
                        pageScope = pageScope,
                        pinnedPosts = pinnedPosts,
                        onClick = { post ->
                            behavior.goToPostDetailPage(
                                context = AmityGlobalFeedComponentBehavior.Context(
                                    pageContext = context,
                                    target = post.getTarget(),
                                ),
                                id = post.getPostId(),
                                category = AmityPostCategory.GLOBAL,
                                autoFocusCommentInput = false,
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

                    // Created posts + ranked paginated feed + injected ads.
                    amityForYouFeedLLS(
                        modifier = modifier,
                        pageScope = pageScope,
                        forYouPosts = posts,
                        pinnedPosts = pinnedPosts,
                        onClick = { post ->
                            behavior.goToPostDetailPage(
                                context = AmityGlobalFeedComponentBehavior.Context(
                                    pageContext = context,
                                    target = post.getTarget(),
                                ),
                                id = post.getPostId(),
                                category = AmityPostCategory.GENERAL,
                                autoFocusCommentInput = false,
                            )
                        },
                        onClipClick = { childPost ->
                            behavior.goToClipFeedPage(
                                context = context,
                                postId = childPost.getPostId()
                            )
                        },
                        refreshKey = refreshKey,
                    )

                    if (appendLoadState is LoadState.Loading) {
                        item(key = "foryou_append_loading") {
                            AmityPostShimmer()
                        }
                    }

                    // REQ-015 / REQ-017 / REQ-018: caught-up cell at end of pagination.
                    if (canShowCaughtUp) {
                        item(key = "feed_caught_up") {
                            AmityFeedCaughtUpComponent(
                                modifier = modifier,
                                pageScope = pageScope,
                                onSwitchRequested = onSwitchToFollowingRequested,
                            )
                        }
                    }
                }
            }
        }

        // Renders nothing extra; LazyColumn covers all states.
        Box(modifier = Modifier)
    }
}
