package com.amity.socialcloud.uikit.community.compose.story.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.sdk.model.social.story.analytics
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryBodyRow
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryBottomRow
import com.amity.socialcloud.uikit.community.compose.story.view.components.AmityStoryHeaderRow
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryModalBottomSheet
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityViewStoryPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityStory.TargetType,
    exoPlayer: ExoPlayer? = null,
    isSingleTarget: Boolean = true,
    isTargetVisible: Boolean = true,
    shouldRestartTimer: Boolean = false,
    firstSegmentReached: () -> Unit = {},
    lastSegmentReached: () -> Unit = {},
    navigateToCreateStoryPage: () -> Unit = {},
    navigateToCommunityProfilePage: (AmityCommunity) -> Unit = {},
    onClose: () -> Unit = {},
) {
    if (exoPlayer == null) return

    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val stories = remember(targetId, targetType, isSingleTarget) {
        viewModel.getActiveStories(targetId, targetType)
    }.collectAsLazyPagingItems()

    val storyPagerState = rememberPagerState {
        stories.itemCount
    }

    val currentStory by remember(targetId, storyPagerState.targetPage, stories.itemCount) {
        derivedStateOf {
            try {
                stories[storyPagerState.targetPage]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }
    }

    val currentTarget = remember(currentStory?.getTargetId()) {
        currentStory?.getTarget()
    }

    val community = remember(currentTarget?.getTargetId()) {
        if (currentTarget is AmityStoryTarget.COMMUNITY) {
            currentTarget.getCommunity()
        } else {
            null
        }
    }

    val isCommunityJoined = remember(viewModel, community) {
        community?.isJoined() == true
    }

    val isCommentCreateAllowed = remember(viewModel, community) {
        community?.getStorySettings()?.allowComment == true
    }

    val isShowingVideoStory by remember(currentStory) {
        derivedStateOf {
            currentStory?.getDataType() == AmityStory.DataType.VIDEO
        }
    }

    val scope = rememberCoroutineScope()
    val shouldPauseTimer by viewModel.shouldPauseTimer.collectAsState()

    var isAudioMuted by remember { mutableStateOf(false) }
    var shouldShowLoading by remember { mutableStateOf(false) }
    var isMovedToUnseenStory by remember { mutableStateOf(false) }
    val isVideoPlaybackReady by AmityStoryVideoPlayerHelper.isVideoPlaybackReady.collectAsState()

    LaunchedEffect(exoPlayer) {
        AmityStoryVideoPlayerHelper.setup(exoPlayer)
    }

    LaunchedEffect(isTargetVisible, isMovedToUnseenStory, currentStory?.getStoryId()) {
        if (isTargetVisible && isMovedToUnseenStory) {
            currentStory?.analytics()?.markAsSeen()
        }
    }

    LaunchedEffect(targetId, stories.itemCount, stories.itemSnapshotList.items.size) {
        if (!isMovedToUnseenStory &&
            stories.itemCount > 0 &&
            stories.itemCount == stories.itemSnapshotList.items.size
        ) {
            if (storyPagerState.targetPage == 0) {
                stories.itemSnapshotList.items.indexOfFirst {
                    !it.isSeen()
                }.let { index ->
                    if (index != -1) {
                        storyPagerState.scrollToPage(index)
                    }
                }
            }
            isMovedToUnseenStory = true
        }
    }

    LaunchedEffect(targetId, stories.itemSnapshotList.items.size) {
        shouldShowLoading = stories.itemSnapshotList.items.isEmpty()
        AmityStoryVideoPlayerHelper.add(stories.itemSnapshotList.items)
    }

    LaunchedEffect(isTargetVisible, isShowingVideoStory, shouldPauseTimer) {
        if (!isTargetVisible) return@LaunchedEffect
        val storyId = currentStory?.getStoryId() ?: return@LaunchedEffect
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

    val dialogState by viewModel.dialogUIState.collectAsState()

    when (dialogState) {
        is AmityStoryModalDialogUIState.OpenConfirmDeleteDialog -> {
            viewModel.handleSegmentTimer(true)
            val data = dialogState as AmityStoryModalDialogUIState.OpenConfirmDeleteDialog
            AmityAlertDialog(
                dialogTitle = "Discard this story?",
                dialogText = "The story will be permanently deleted. It cannot be undone.",
                confirmText = "Discard",
                dismissText = "Cancel",
                onConfirmation = {
                    shouldShowLoading = true
                    viewModel.deleteStory(
                        storyId = data.storyId,
                        onSuccess = {
                            shouldShowLoading = false
                            viewModel.updateDialogUIState(AmityStoryModalDialogUIState.CloseDialog)
                            viewModel.handleSegmentTimer(false)

                            if (stories.itemCount <= 1) {
                                onClose()
                            } else {
                                scope.launch {
                                    moveSegment(
                                        shouldMoveToNext = false,
                                        totalSegments = stories.itemCount,
                                        storyPagerState = storyPagerState,
                                        firstSegmentReached = firstSegmentReached,
                                        lastSegmentReached = lastSegmentReached,
                                    )
                                }
                            }
                        },
                        onError = {
                            shouldShowLoading = false
                            viewModel.updateDialogUIState(AmityStoryModalDialogUIState.CloseDialog)
                            viewModel.handleSegmentTimer(false)
                        }
                    )
                },
                onDismissRequest = {
                    viewModel.updateDialogUIState(AmityStoryModalDialogUIState.CloseDialog)
                    viewModel.handleSegmentTimer(false)
                }
            )
        }

        AmityStoryModalDialogUIState.CloseDialog -> {
            viewModel.updateDialogUIState(AmityStoryModalDialogUIState.CloseDialog)
        }
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
                key = { stories[it]?.getStoryId() ?: it },
            ) { index ->
                val story = stories[index] ?: return@HorizontalPager

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.fillMaxSize()
                ) {
                    AmityStoryBodyRow(
                        modifier = modifier
                            .aspectRatio(9f / 16f),
                        exoPlayer = exoPlayer,
                        dataType = story.getDataType(),
                        data = story.getData(),
                        state = story.getState(),
                        items = story.getStoryItems(),
                        isVisible = storyPagerState.targetPage == index,
                        onTap = { isTapRight ->
                            AmityStoryVideoPlayerHelper.resetPlaybackIndex()

                            scope.launch {
                                moveSegment(
                                    shouldMoveToNext = isTapRight,
                                    totalSegments = stories.itemCount,
                                    storyPagerState = storyPagerState,
                                    firstSegmentReached = firstSegmentReached,
                                    lastSegmentReached = lastSegmentReached,
                                )
                            }
                        },
                        onHold = {
                            viewModel.handleSegmentTimer(shouldPause = it)
                        },
                        onSwipeUp = {
                            viewModel.updateSheetUIState(
                                AmityStoryModalSheetUIState.OpenCommentTraySheet(
                                    storyId = story.getStoryId(),
                                    shouldAllowInteraction = isCommunityJoined,
                                    shouldAllowComment = isCommentCreateAllowed,
                                )
                            )
                        },
                        onSwipeDown = {
                            onClose()
                        },
                        onHyperlinkClick = {
                            scope.launch(Dispatchers.IO) {
                                story.analytics().markLinkAsClicked()
                            }
                        }
                    )

                    AmityStoryBottomRow(
                        modifier = modifier,
                        pageScope = getPageScope(),
                        storyId = story.getStoryId(),
                        story = story,
                        target = story.getTarget(),
                        state = story.getState(),
                        reachCount = story.getReach(),
                        commentCount = story.getCommentCount(),
                        reactionCount = story.getReactionCount(),
                        isReactedByMe = story.getMyReactions().isNotEmpty(),
                        shouldShowCommentTray = false,
                    )
                }
            }

            AmityStoryHeaderRow(
                pageScope = getPageScope(),
                story = currentStory,
                totalSegments = stories.itemCount,
                currentSegment = storyPagerState.targetPage,
                shouldPauseTimer = shouldPauseTimer || !isTargetVisible,
                shouldRestartTimer = shouldRestartTimer,
                isSingleTarget = isSingleTarget,
                moveToNextSegment = {
                    AmityStoryVideoPlayerHelper.resetPlaybackIndex()
                    scope.launch {
                        moveSegment(
                            shouldMoveToNext = true,
                            totalSegments = stories.itemCount,
                            storyPagerState = storyPagerState,
                            firstSegmentReached = firstSegmentReached,
                            lastSegmentReached = lastSegmentReached,
                        )
                    }
                },
                onCloseClicked = onClose,
                navigateToCreatePage = navigateToCreateStoryPage,
                navigateToCommunityProfilePage = navigateToCommunityProfilePage,
            )

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

            if (shouldShowLoading || currentStory == null || !isMovedToUnseenStory) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = if (isMovedToUnseenStory) 0.5f else 1f))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AmityTheme.colors.primary
                    )
                }
            }

            if (isTargetVisible) {
                AmityStoryModalBottomSheet(modifier = modifier)
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