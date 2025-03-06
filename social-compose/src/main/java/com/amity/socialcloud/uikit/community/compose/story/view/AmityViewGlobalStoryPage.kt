package com.amity.socialcloud.uikit.community.compose.story.view

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStorySharedGlobalTargetsObject
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityViewGlobalStoryPage(
    modifier: Modifier = Modifier,
    targetId: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val behavior = remember {
        AmitySocialBehaviorHelper.viewStoryPageBehavior
    }

    var shouldShowLoading by remember { mutableStateOf(false) }
    var shouldRestartTimer by remember { mutableStateOf(false) }

    val targets = remember {
        AmityStorySharedGlobalTargetsObject.getTargets()
    }

    val selectedTarget = remember {
        AmityStorySharedGlobalTargetsObject.getSelectedTarget()
    }

    val targetPagerState = rememberPagerState {
        targets.size
    }

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(targetId, selectedTarget.first) {
        if (selectedTarget.first < 0) return@LaunchedEffect

        scope.launch {
            targetPagerState.scrollToPage(selectedTarget.first)
        }
    }

    LaunchedEffect(targets.size, selectedTarget.first) {
        shouldShowLoading = targets.isEmpty()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (selectedTarget.first >= 0) {
            HorizontalPager(
                state = targetPagerState,
                beyondViewportPageCount = 2,
                key = {
                    targets[it].getTargetId()
                },
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) { index ->
                val target = targets[index]

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = targetPagerState.offsetForPage(index)
                            val offScreenRight = pageOffset < 0f
                            val deg = 30f
                            val interpolated =
                                FastOutLinearInEasing.transform(pageOffset.absoluteValue)
                            rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

                            transformOrigin = TransformOrigin(
                                pivotFractionX = if (offScreenRight) 0f else 1f,
                                pivotFractionY = .5f
                            )
                        }
                ) {
                    AmityViewCommunityStoryPage(
                        modifier = modifier,
                        targetId = target.getTargetId(),
                        targetType = target.getTargetType(),
                        exoPlayer = exoPlayer,
                        isSingleTarget = false,
                        isTargetVisible = targetPagerState.targetPage == index && !targetPagerState.isScrollInProgress,
                        shouldRestartTimer = shouldRestartTimer,
                        navigateToCommunityProfilePage = { community ->
                            behavior.goToCommunityProfilePage(
                                context = context,
                                community = community
                            )
                        },
                        firstSegmentReached = {
                            scope.launch {
                                moveTarget(
                                    shouldMoveToNext = false,
                                    totalTargets = targets.size,
                                    targetPagerState = targetPagerState,
                                    firstTargetReached = {
                                        scope.launch {
                                            shouldRestartTimer = true
                                            delay(300)
                                            shouldRestartTimer = false
                                        }
                                    }
                                )
                            }
                        },
                        lastSegmentReached = {
                            scope.launch {
                                moveTarget(
                                    shouldMoveToNext = true,
                                    totalTargets = targets.size,
                                    targetPagerState = targetPagerState,
                                    lastTargetReached = {
                                        context.closePage()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }

        if (shouldShowLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AmityTheme.colors.primary
            )
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
private suspend fun moveTarget(
    shouldMoveToNext: Boolean,
    totalTargets: Int,
    targetPagerState: PagerState,
    firstTargetReached: () -> Unit = {},
    lastTargetReached: () -> Unit = {},
) {
    val currentTarget = targetPagerState.currentPage

    if (shouldMoveToNext) {
        if (currentTarget == totalTargets - 1) {  //  last target
            lastTargetReached()
        } else {    //  move to next target
            targetPagerState.animateScrollToPage(
                page = currentTarget + 1,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        }
    } else {
        if (currentTarget == 0) {  //  first target
            firstTargetReached()
        } else {    //  move to previous target
            targetPagerState.animateScrollToPage(
                page = currentTarget - 1,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@Preview
@Composable
fun PreviewAmityViewGlobalStoryPage() {
    AmityViewGlobalStoryPage(
        targetId = "",
    )
}