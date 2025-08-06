package com.amity.socialcloud.uikit.community.compose.clip.view

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.element.AmityClipLoadStateError
import com.amity.socialcloud.uikit.community.compose.clip.view.element.AmityClipModalBottomSheet
import com.amity.socialcloud.uikit.community.compose.clip.view.element.AmityClipPostDeleted
import com.amity.socialcloud.uikit.community.compose.clip.view.element.AmityClipProfileShimmer
import com.amity.socialcloud.uikit.community.compose.clip.view.element.AmityEmptyClipFeedScreen
import com.amity.socialcloud.uikit.community.compose.clip.view.element.ClipItem
import com.amity.socialcloud.uikit.community.compose.clip.view.element.ClipToolbar
import com.amity.socialcloud.uikit.community.compose.clip.view.util.SharedClipFeedStore
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.utils.AmityClipPlayerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.abs

// Add this constant at the top of the file
private const val ENDLESS_LOOP_MULTIPLIER = 1000

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityClipFeedPage(
    type: AmityClipFeedPageType,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val behavior = remember {
        AmitySocialBehaviorHelper.viewClipPageBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel =
        viewModel<AmityClipFeedPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // Check if we have shared paging data for both User and Community
    val (sharedPagingData, initialIndex) = remember(type) {
        when (type) {
            is AmityClipFeedPageType.UserClipFeed -> {
                SharedClipFeedStore.getClipPagingData(type.userId, "user") ?: (null to 0)
            }

            is AmityClipFeedPageType.CommunityClipFeed -> {
                SharedClipFeedStore.getClipPagingData(type.communityId, "community") ?: (null to 0)
            }

            else -> null to 0
        }
    }

    // Initialize query based on feed type
    LaunchedEffect(type) {
        initializeClipQuery(type, viewModel)
    }

    val amityClip by viewModel.amityClip.collectAsState()

    // Use shared paging data if available
    val amityClips = if (sharedPagingData != null &&
        (type is AmityClipFeedPageType.UserClipFeed || type is AmityClipFeedPageType.CommunityClipFeed)
    ) {
        sharedPagingData.collectAsLazyPagingItems()
    } else {
        // Fallback to normal initialization
        LaunchedEffect(type) {
            initializeClipQuery(type, viewModel)
        }
        viewModel.amityClips.collectAsLazyPagingItems()
    }

    val isClipLoading by viewModel.isLoading.collectAsState()
    val isClipError by viewModel.isError.collectAsState()

    // Create pager state
    val pagerState = rememberPagerState(
        initialPage = if ((type is AmityClipFeedPageType.UserClipFeed ||
                    type is AmityClipFeedPageType.CommunityClipFeed) &&
            sharedPagingData != null
        ) {
            initialIndex
        } else if (type is AmityClipFeedPageType.GlobalFeed &&
            amityClips.itemCount >= 10
        ) {
            // Start at middle of virtual pages for endless loop only if >= 10 items
            (ENDLESS_LOOP_MULTIPLIER / 2) * amityClips.itemCount
        } else 0,
        pageCount = {
            when (type) {
                is AmityClipFeedPageType.NewsFeed -> {
                    if (amityClip.isNotEmpty()) 1 else 0
                }

                is AmityClipFeedPageType.GlobalFeed -> {
                    // Create virtual endless pages only if >= 10 items
                    if (amityClips.itemCount >= 10) {
                        amityClips.itemCount * ENDLESS_LOOP_MULTIPLIER
                    } else {
                        amityClips.itemCount
                    }
                }

                else -> {
                    if (amityClip.isNotEmpty()) {
                        amityClips.itemCount
                    } else {
                        amityClips.itemCount
                    }
                }
            }
        }
    )

    // Create ExoPlayer once with progressive loading
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    // Initialize clip player helper and setup player listeners
    val (isPlaying, playerState, playerError) = setupPlayerWithListeners(exoPlayer, coroutineScope)

    // Track swipe gesture state
    var isDragging by remember { mutableStateOf(false) }
    var dragDelta by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 100f
    var wasPlaying by remember { mutableStateOf(true) }

    var isPostDeleted by remember { mutableStateOf(false) }

    var toolbarTitle by remember { mutableStateOf("") }
    var community by remember { mutableStateOf<AmityCommunity?>(null) }

    // Control toolbar visibility based on scroll position or user action
    var isToolbarVisible by remember { mutableStateOf(true) }
    var lastTapTime by remember { mutableLongStateOf(0L) }

    // Monitor page changes to update playback
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                // Clear any previous media to prevent wrong video showing
                exoPlayer.clearMediaItems()
            }
    }

    // Clean up player when component is destroyed
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AmityBasePage(
        pageId = "clip_feed_page",
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastTapTime > 300) {
                            isToolbarVisible = !isToolbarVisible
                            lastTapTime = currentTime
                        }
                    }
                }
        ) {
            // Show initial loading state
            if (getClipCount(type, amityClip, amityClips) == 0 && isLoading(
                    type,
                    amityClips,
                    isClipLoading
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AmityTheme.colors.baseShade1),
                ) {
                    AmityClipProfileShimmer(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    )
                }
            } else if (hasError(type, amityClips, isClipError)) {
                AmityClipLoadStateError(
                    onReloadClick = {
                        initializeClipQuery(type, viewModel)
                    }
                )
            } else if (shouldShowEmptyState(type, amityClip, amityClips)) {
                AmityEmptyClipFeedScreen(
                    onExploreCommunityClick = {
                        context.closePageWithResult(Activity.RESULT_OK)
                    },
                    onCreateCommunityClick = {
                        context.closePageWithResult(Activity.RESULT_OK)
                        behavior.goToCreateCommunityPage(context)
                    }
                )
            } else {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            if (type !is AmityClipFeedPageType.NewsFeed) {
                                detectVerticalDragGestures(
                                    onDragStart = {
                                        isDragging = true
                                        dragDelta = 0f
                                        wasPlaying = exoPlayer.isPlaying
                                        exoPlayer.pause()
                                    },
                                    onDragEnd = {
                                        isDragging = false
                                        if (abs(dragDelta) > swipeThreshold) {
                                            coroutineScope.launch {
                                                // Fix: Ensure correct navigation direction
                                                if (dragDelta < 0) {
                                                    // Swipe UP - go to next clip (newer content)
                                                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                                    }
                                                } else {
                                                    // Swipe DOWN - go to previous clip (older content)
                                                    if (pagerState.currentPage > 0) {
                                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                                    }
                                                }
                                            }
                                        } else {
                                            if (wasPlaying) {
                                                exoPlayer.play()
                                            }
                                        }
                                        dragDelta = 0f
                                    },
                                    onDragCancel = {
                                        isDragging = false
                                        dragDelta = 0f
                                        if (wasPlaying) {
                                            exoPlayer.play()
                                        }
                                    },
                                    onVerticalDrag = { _, dragAmount ->
                                        if (!pagerState.isScrollInProgress) {
                                            dragDelta += dragAmount
                                        }
                                    }
                                )
                            }
                        },
                    key = { page ->
                        when (type) {
                            is AmityClipFeedPageType.NewsFeed -> amityClip.getOrNull(page)
                                ?.getPostId()
                                ?: "newsfeed_$page"

                            is AmityClipFeedPageType.GlobalFeed -> {
                                val actualIndex = if (amityClips.itemCount >= 10) {
                                    page % amityClips.itemCount
                                } else {
                                    page
                                }
                                amityClips[actualIndex]?.getPostId() ?: "global_$actualIndex"
                            }

                            else -> amityClips[page]?.getPostId() ?: "page_$page"
                        }
                    },
                    beyondViewportPageCount = 0,
                    userScrollEnabled = true,
                    pageSpacing = 0.dp // Ensure no spacing between pages
                ) { page ->
                    val clip = when (type) {
                        is AmityClipFeedPageType.UserNewsFeed,
                        is AmityClipFeedPageType.CommunityFeed,
                            -> {
                            if (page == 0) {
                                if (amityClip.isNotEmpty()) amityClip[0] else null
                            } else {
                                val firstClipId =
                                    if (amityClip.isNotEmpty()) amityClip[0].getPostId() else null

                                // Calculate the actual index in amityClips, accounting for skipped duplicates
                                var actualIndex = page - 1
                                var skippedCount = 0

                                // Check how many items before this position are duplicates of the first clip
                                for (i in 0 until minOf(page, amityClips.itemCount)) {
                                    if (i < amityClips.itemCount && amityClips[i]?.getPostId() == firstClipId) {
                                        skippedCount++
                                    }
                                }

                                // Adjust the index by adding the number of skipped duplicates
                                actualIndex = page - 1 + skippedCount

                                if (actualIndex < amityClips.itemCount) {
                                    amityClips[actualIndex]
                                } else {
                                    null
                                }
                            }
                        }

                        is AmityClipFeedPageType.NewsFeed -> {
                            amityClip[page]
                        }

                        is AmityClipFeedPageType.GlobalFeed -> {
                            // Handle endless loop only if >= 10 items
                            val actualIndex = if (amityClips.itemCount >= 10) {
                                page % amityClips.itemCount
                            } else {
                                page
                            }
                            amityClips[actualIndex]
                        }

                        is AmityClipFeedPageType.UserClipFeed,
                        is AmityClipFeedPageType.CommunityClipFeed,
                            -> {
                            amityClips[page]
                        }
                    }

                    clip?.let { clipData ->
                        isPostDeleted = clipData.isDeleted() || isClipError == AmityError.ITEM_NOT_FOUND.code.toString()
                        if (isPostDeleted) {
                            exoPlayer.pause()
                            AmityClipPostDeleted(
                                onNextClipClick = {
                                    if (type is AmityClipFeedPageType.NewsFeed) {
                                        context.closePageWithResult(Activity.RESULT_OK)
                                    } else {
                                        coroutineScope.launch {
                                            if (pagerState.currentPage < pagerState.pageCount - 1) {
                                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                            } else {
                                                // If at the last page, reset to first page
                                                pagerState.scrollToPage(0)
                                            }
                                        }
                                    }

                                }
                            )
                        } else {
                            if (page == pagerState.currentPage && !isDragging) {
                                val target = clipData.getTarget() as? AmityPost.Target.COMMUNITY
                                if (target != null) {
                                    val communityData = target.getCommunity()
                                    toolbarTitle = communityData?.getDisplayName() ?: ""
                                    community = communityData
                                } else {
                                    toolbarTitle = clipData.getCreator()?.getDisplayName() ?: ""
                                }
                            }

                            ClipItem(
                                modifier = Modifier.clickableWithoutRipple {
                                    if (isPlaying) {
                                        exoPlayer.pause()
                                    } else {
                                        exoPlayer.play()
                                    }
                                },
                                post = clipData,
                                community = community,
                                behavior = behavior,
                                isCurrentlyPlaying = page == pagerState.currentPage && !isDragging,
                                playerState = playerState,
                                exoPlayer = exoPlayer,
                                pageIndex = page,
                                viewModel = viewModel,
                                type = type,
                                reloadClick = {
                                    initializeClipQuery(type, viewModel)
                                }
                            )
                        }
                    }
                }
            }

            // Show Play button when there are clips and player is not playing
            if (getClipCount(
                    type,
                    amityClip,
                    amityClips
                ) > 0 && !isPlaying && !isDragging && playerState == ExoPlayer.STATE_READY && !isPostDeleted
            ) {
                Image(
                    painter = painterResource(R.drawable.amity_ic_play_v4),
                    contentDescription = "Play Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                        .clickableWithoutRipple {
                            exoPlayer.play()
                        }
                )
            }

            // Toolbar implementation
            ClipToolbar(
                title = toolbarTitle,
                community = community,
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_OK)
                },
                isLoading = getClipCount(type, amityClip, amityClips) == 0,
                isError = hasError(
                    type = type,
                    amityClips = amityClips,
                    isClipError = isClipError
                ) || isPostDeleted,
                onTitleClick = {
                    community?.let {
                        behavior.goToCommunityProfilePage(
                            context, it
                        )
                    } ?: run {
                        behavior.goToUserProfilePage(
                            context, toolbarTitle
                        )
                    }
                },
                onCreateClick = {
                    handleCreateClipNavigation(type, context, behavior, community)
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )

            if (getClipCount(type, amityClip, amityClips) > 0) {
                AmityClipModalBottomSheet(behavior = behavior, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun setupPlayerWithListeners(
    exoPlayer: ExoPlayer,
    coroutineScope: CoroutineScope,
): Triple<Boolean, Int, PlaybackException?> {
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var playerState by remember { mutableIntStateOf(ExoPlayer.STATE_IDLE) }
    var playerError by remember { mutableStateOf<PlaybackException?>(null) }

    // Initialize clip player helper
    LaunchedEffect(exoPlayer) {
        AmityClipPlayerHelper.setup(exoPlayer, coroutineScope)
    }

    // Listen to ExoPlayer state changes
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                isPlaying = exoPlayer.isPlaying
                playerState = playbackState
            }

            override fun onPlayerError(error: PlaybackException) {
                playerError = error
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    return Triple(isPlaying, playerState, playerError)
}

private fun isLoading(
    type: AmityClipFeedPageType,
    amityClips: LazyPagingItems<AmityPost>,
    isClipLoading: Boolean = false,
): Boolean {
    return when (type) {
        is AmityClipFeedPageType.NewsFeed -> isClipLoading // NewsFeed doesn't use paging loading states
        else -> amityClips.loadState.refresh is LoadState.Loading
    }
}

private fun hasError(
    type: AmityClipFeedPageType,
    amityClips: LazyPagingItems<AmityPost>,
    isClipError: String? = null,
): Boolean {
    return when (type) {
        is AmityClipFeedPageType.NewsFeed -> isClipError != null // NewsFeed doesn't use paging error states
        else -> amityClips.loadState.refresh is LoadState.Error && amityClips.itemCount == 0
    }
}

private fun shouldShowEmptyState(
    type: AmityClipFeedPageType,
    amityClip: List<AmityPost>,
    amityClips: LazyPagingItems<AmityPost>,
): Boolean {
    return getClipCount(type, amityClip, amityClips) == 0 &&
            !isLoading(type, amityClips) &&
            !hasError(type, amityClips)
}

private fun getClipCount(
    type: AmityClipFeedPageType,
    amityClip: List<AmityPost>,
    amityClips: LazyPagingItems<AmityPost>,
): Int {
    return when (type) {
        is AmityClipFeedPageType.NewsFeed -> amityClip.size
        else -> amityClips.itemCount
    }
}

private fun initializeClipQuery(
    type: AmityClipFeedPageType,
    viewModel: AmityClipFeedPageViewModel,
) {
    when (type) {
        is AmityClipFeedPageType.UserNewsFeed -> {
            val postId = type.postId
            viewModel.queryClipOnUserFeed(postId = postId)
        }

        is AmityClipFeedPageType.CommunityFeed -> {
            val postId = type.postId
            val communityId = type.communityId
            viewModel.queryClipOnCommunityFeed(postId = postId, communityId = communityId)
        }

        is AmityClipFeedPageType.NewsFeed -> {
            viewModel.queryClipOnNewFeed(type.postId)
        }

        is AmityClipFeedPageType.GlobalFeed -> {
            viewModel.queryClipOnGlobalFeed()
        }

        else -> {

        }
    }
}

private fun handleCreateClipNavigation(
    type: AmityClipFeedPageType,
    context: Context,
    behavior: AmityClipPageBehavior,
    community: AmityCommunity?,
) {
    when (type) {
        is AmityClipFeedPageType.UserClipFeed,
        is AmityClipFeedPageType.UserNewsFeed,
            -> {
            behavior.goToClipPostComposerPage(
                context = context,
                targetId = AmityCoreClient.getUserId(),
                targetType = AmityPostTargetType.USER
            )
        }

        is AmityClipFeedPageType.CommunityClipFeed,
        is AmityClipFeedPageType.CommunityFeed,
            -> {
            behavior.goToClipPostComposerPage(
                context = context,
                targetId = community?.getCommunityId(),
                targetType = AmityPostTargetType.COMMUNITY,
            )
        }

        is AmityClipFeedPageType.GlobalFeed,
        is AmityClipFeedPageType.NewsFeed,
            -> {
            behavior.goToSelectPostTargetPage(
                context = context,
                type = AmityPostTargetSelectionPageType.CLIP
            )
        }
    }
}