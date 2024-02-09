package com.amity.socialcloud.uikit.community.compose.story.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryBodyRow
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryBottomRow
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryHeaderRow
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun AmityViewStoryPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navigateToCreateStoryPage: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    AmityComposeTheme {
        val stories = viewModel.stories.collectAsLazyPagingItems()

        val storyPagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            stories.itemCount
        }

        val coroutineScope = rememberCoroutineScope()
        val shouldPauseTimer by viewModel.shouldPauseTimer.collectAsState()
        var shouldRestartTimer by remember { mutableStateOf(false) }

        val exoPlayer = remember { ExoPlayer.Builder(context).build() }

        var isShowingVideoStory by remember { mutableStateOf(false) }
        var isAudioMuted by remember { mutableStateOf(false) }
        var shouldShowLoading by remember { mutableStateOf(false) }
        val isVideoPlaybackReady by AmityStoryVideoPlayerHelper.isVideoPlaybackReady.collectAsState()

        LaunchedEffect(community.getCommunityId()) {
            viewModel.getStories(communityId = community.getCommunityId())
        }

        LaunchedEffect(exoPlayer) {
            AmityStoryVideoPlayerHelper.setup(exoPlayer)
        }

        LaunchedEffect(stories.itemCount) {
            shouldShowLoading = stories.itemCount == 0
            AmityStoryVideoPlayerHelper.add(stories.itemSnapshotList.items)
        }

        LaunchedEffect(storyPagerState.targetPage, stories.itemCount) {
            if (stories.itemCount == 0) return@LaunchedEffect

            val story = stories.peek(storyPagerState.targetPage) ?: return@LaunchedEffect
            isShowingVideoStory = story.getDataType() == AmityStory.DataType.VIDEO

            viewModel.markAsSeen(story)
        }

        LaunchedEffect(isShowingVideoStory, shouldPauseTimer) {
            if (stories.itemCount == 0) return@LaunchedEffect
            val storyId =
                stories.peek(storyPagerState.targetPage)?.getStoryId() ?: return@LaunchedEffect
            if (isShowingVideoStory) {
                AmityStoryVideoPlayerHelper.playMediaItem(storyId = storyId)
            } else {
                exoPlayer.pause()
                return@LaunchedEffect
            }
            if (shouldPauseTimer) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
        }

        LaunchedEffect(isVideoPlaybackReady, isShowingVideoStory) {
            if (isShowingVideoStory) {
                viewModel.handleSegmentTimer(shouldPause = !isVideoPlaybackReady)
                shouldShowLoading = !isVideoPlaybackReady
            }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        viewModel.handleSegmentTimer(shouldPause = false)
                        if (isShowingVideoStory) {
                            exoPlayer.play()
                        }
                    }

                    Lifecycle.Event.ON_PAUSE,
                    Lifecycle.Event.ON_STOP -> {
                        viewModel.handleSegmentTimer(shouldPause = true)
                        if (isShowingVideoStory) {
                            exoPlayer.pause()
                        }
                    }

                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        val openConfirmDeleteDialog = remember { mutableStateOf(false) }
        var storyIdToDelete by remember { mutableStateOf("") }

        if (openConfirmDeleteDialog.value) {
            AmityAlertDialog(
                dialogTitle = "Discard this story?",
                dialogText = "The story will be permanently deleted. It cannot be undone.",
                confirmText = "Discard",
                dismissText = "Cancel",
                onConfirmation = {
                    shouldShowLoading = true
                    viewModel.deleteStory(
                        storyId = storyIdToDelete,
                        onSuccess = {
                            shouldShowLoading = false
                            openConfirmDeleteDialog.value = false
                            viewModel.handleSegmentTimer(shouldPause = false)

                            if (stories.itemCount <= 1) {
                                onCloseClicked()
                            } else {
                                coroutineScope.launch {
                                    moveSegment(
                                        shouldMoveToNext = false,
                                        totalSegments = stories.itemCount,
                                        storyPagerState = storyPagerState,
                                        firstSegmentReached = {
                                            coroutineScope.launch {
                                                shouldRestartTimer = true
                                                delay(300)
                                                shouldRestartTimer = false
                                            }
                                        },
                                        lastSegmentReached = {}
                                    )
                                }
                            }
                        },
                        onError = {
                            shouldShowLoading = false
                            openConfirmDeleteDialog.value = false
                            viewModel.handleSegmentTimer(shouldPause = false)
                        }
                    )
                },
                onDismissRequest = {
                    openConfirmDeleteDialog.value = false
                    viewModel.handleSegmentTimer(shouldPause = false)
                }
            )
        }

        AmityBasePage(pageId = "story_page") {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                HorizontalPager(
                    state = storyPagerState,
                    beyondBoundsPageCount = 3,
                    userScrollEnabled = false,
                    key = {
                        try {
                            stories.peek(it)?.getStoryId() ?: -1
                        } catch (e: IndexOutOfBoundsException) {
                            -1
                        }
                    }
                ) { index ->
                    val story = try {
                        stories.peek(index) ?: return@HorizontalPager
                    } catch (e: IndexOutOfBoundsException) {
                        return@HorizontalPager
                    }

                    Column {
                        AmityStoryBodyRow(
                            modifier = modifier
                                .aspectRatio(9f / 16f),
                            exoPlayer = exoPlayer,
                            story = story,
                            isVisible = storyPagerState.targetPage == index,
                            onTap = { isTapRight ->
                                AmityStoryVideoPlayerHelper.resetPlaybackIndex()

                                coroutineScope.launch {
                                    moveSegment(
                                        shouldMoveToNext = isTapRight,
                                        totalSegments = stories.itemCount,
                                        storyPagerState = storyPagerState,
                                        firstSegmentReached = {
                                            coroutineScope.launch {
                                                shouldRestartTimer = true
                                                delay(300)
                                                shouldRestartTimer = false
                                            }
                                        },
                                        lastSegmentReached = { onCloseClicked() }
                                    )
                                }
                            },
                            onHold = {
                                viewModel.handleSegmentTimer(shouldPause = it)
                            },
                            onSwipeDown = {
                                onCloseClicked()
                            }
                        )

                        AmityStoryBottomRow(
                            modifier = modifier,
                            pageScope = getPageScope(),
                            story = story,
                            onDeleteClicked = {
                                viewModel.handleSegmentTimer(shouldPause = true)
                                storyIdToDelete = it
                                openConfirmDeleteDialog.value = true
                            }
                        )
                    }
                }

                AmityStoryHeaderRow(
                    pageScope = getPageScope(),
                    communityId = community.getCommunityId(),
                    communityDisplayName = community.getDisplayName(),
                    avatarUrl = community.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: "",
                    stories = stories.itemSnapshotList.items,
                    totalSegments = stories.itemCount,
                    currentSegment = storyPagerState.targetPage,
                    isVisible = true,
                    shouldPauseTimer = shouldPauseTimer,
                    shouldRestartTimer = shouldRestartTimer,
                    moveToNextSegment = {
                        AmityStoryVideoPlayerHelper.resetPlaybackIndex()
                        coroutineScope.launch {
                            moveSegment(
                                shouldMoveToNext = true,
                                totalSegments = stories.itemCount,
                                storyPagerState = storyPagerState,
                                firstSegmentReached = {
                                    coroutineScope.launch {
                                        shouldRestartTimer = true
                                        delay(300)
                                        shouldRestartTimer = false
                                    }
                                },
                                lastSegmentReached = { onCloseClicked() }
                            )
                        }
                    },
                    onCloseClicked = onCloseClicked,
                    navigateToCreatePage = {
                        navigateToCreateStoryPage()
                    },
                    onDeleteClicked = {
                        viewModel.handleSegmentTimer(shouldPause = true)
                        showBottomSheet = true
                        storyIdToDelete = it
                    }
                )

                if (shouldShowLoading) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                if (isShowingVideoStory) {
                    Box(
                        modifier = Modifier.size(48.dp, 120.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isAudioMuted) R.drawable.amity_ic_story_audio_mute
                                else R.drawable.amity_ic_story_audio_unmute
                            ),
                            contentDescription = "Mute/Unmute Audio",
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .clickable {
                                    isAudioMuted = !isAudioMuted
                                    exoPlayer.volume = if (isAudioMuted) 0f else 1f
                                }
                        )
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            viewModel.handleSegmentTimer(shouldPause = false)
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                        ) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_delete_story,
                                text = "Delete story",
                            ) {
                                openConfirmDeleteDialog.value = true

                                scope
                                    .launch {
                                        sheetState.hide()
                                    }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
                AmityStoryVideoPlayerHelper.clear()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private suspend fun moveSegment(
    shouldMoveToNext: Boolean,
    totalSegments: Int,
    storyPagerState: PagerState,
    firstSegmentReached: () -> Unit,
    lastSegmentReached: () -> Unit,
) {
    val currentSegment = storyPagerState.currentPage

    if (shouldMoveToNext) {
        if (currentSegment == totalSegments - 1) {  //  last segment
            lastSegmentReached()
        } else {    //  move to next segment
            storyPagerState.scrollToPage(currentSegment + 1)
        }
    } else {
        if (currentSegment == 0) {  //  first segment
            firstSegmentReached()
        } else {    //  move to previous segment
            storyPagerState.scrollToPage(currentSegment - 1)
        }
    }
}

@Preview
@Composable
fun AmityViewStoryPagePreview() {
//    AmityViewStoryPage {}
}