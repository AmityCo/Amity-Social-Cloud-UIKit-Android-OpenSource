package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBase
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBaseShade4
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityProductTagBadge
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.VideoSeekBar
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import kotlinx.coroutines.delay

/**
 * Full-screen video/clip player for the composer's pre-upload attachments.
 *
 * Mirrors AmityVideoPlayerPage's pager/autoplay/dismiss idioms, but plays straight from each
 * item's local Uri via AmityPostVideoPlayerHelper.addUrls -- composer media has no post id or
 * SDK-resolved stream URL yet, so the published-post player (which pages over AmityPost
 * children and their room/HLS special cases) cannot be reused as-is. Scope is intentionally
 * minimal: no menu -- playback, seeking, and audio state are the only interactions.
 */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmitySelectedMediaVideoPlayerPage(
    modifier: Modifier = Modifier,
    mediaList: List<AmityPostMedia>,
    initialMediaKey: String,
    mediaProductTags: Map<String, List<AmityProduct>> = emptyMap(),
    isProductCatalogueEnabled: Boolean = false,
    onDismiss: () -> Unit,
    onTagProductClick: (AmityPostMedia) -> Unit = {},
) {
    if (mediaList.isEmpty()) {
        onDismiss()
        return
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(10_000)
            .setSeekForwardIncrementMs(10_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }

    var verticalDragAmount by remember { mutableFloatStateOf(0f) }
    // Volume is a property of the one shared player, so the member's audio choice carries across
    // swipes rather than resetting on each frame.
    var isAudioMuted by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    val initialPage = remember {
        mediaList.indexOfFirst { it.url.toString() == initialMediaKey }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(initialPage = initialPage) { mediaList.size }

    // One-time setup: composer media has no remove/reorder control inside this viewer, so the
    // playlist is fixed for the dialog's lifetime -- no need to react to later recompositions.
    LaunchedEffect(Unit) {
        AmityPostVideoPlayerHelper.setup(exoPlayer)
        val playbackUrls = mediaList.map { item ->
            (item.media as? AmityPostMedia.Media.Video)?.video?.getVideoUrlWithFallbackQuality()
                ?: item.url.toString()
        }
        AmityPostVideoPlayerHelper.addUrls(playbackUrls)
    }

    // Autoplay the opened page and every adjacent page swiped to.
    LaunchedEffect(pagerState.currentPage) {
        delay(100)
        AmityPostVideoPlayerHelper.playMediaItem(pagerState.currentPage)
        exoPlayer.play()
    }

    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(1_000)
            showControls = false
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                isPlaying = exoPlayer.isPlaying
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            AmityPostVideoPlayerHelper.clear()
        }
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        AmityBaseComponent(
            componentId = "selected_media_video_player",
            needScaffold = true,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(amityMediaSurface)
            ) {
                HorizontalPager(
                    state = pagerState,
                    key = { it },
                    modifier = modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (verticalDragAmount > 100) {
                                        onDismiss()
                                    }
                                    verticalDragAmount = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                verticalDragAmount += dragAmount
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showControls = !showControls
                            }
                        }
                ) { index ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AmityStoryVideoPlayer(
                            exoPlayer = exoPlayer,
                            isVisible = pagerState.currentPage == index,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                if (showControls) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isPlaying) CommonComposeR.drawable.amity_ic_pause
                                else CommonComposeR.drawable.amity_ic_play_v4
                            ),
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickableWithoutRipple {
                                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                                }
                        )
                    }
                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(amityColorBlack.copy(alpha = 0.5f)),
                ) {
                    val (closeBtn, muteBtn, counter) = createRefs()

                    AmityMenuButton(
                        size = 32.dp,
                        iconPadding = 8.dp,
                        background = amityColorBaseShade4,
                        tint = amityColorBase,
                        modifier = Modifier
                            .zIndex(Float.MAX_VALUE)
                            .constrainAs(closeBtn) {
                                top.linkTo(parent.top, margin = 16.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            },
                    ) {
                        onDismiss()
                    }

                    Image(
                        painter = painterResource(
                            id = if (isAudioMuted) R.drawable.amity_ic_audio_mute_filled
                            else R.drawable.amity_ic_audio_unmute_filled
                        ),
                        contentDescription = if (isAudioMuted) "Unmute video" else "Mute video",
                        modifier = Modifier
                            .size(32.dp)
                            .zIndex(Float.MAX_VALUE)
                            .constrainAs(muteBtn) {
                                top.linkTo(closeBtn.top)
                                bottom.linkTo(closeBtn.bottom)
                                end.linkTo(parent.end, margin = 16.dp)
                            }
                            .clickableWithoutRipple {
                                isAudioMuted = !isAudioMuted
                                exoPlayer.volume = if (isAudioMuted) 0f else 1f
                            },
                        colorFilter = ColorFilter.tint(amityColorWhite)
                    )

                    Text(
                        text = "${pagerState.currentPage + 1} / ${mediaList.size}",
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = amityColorWhite
                        ),
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Video ${pagerState.currentPage + 1} of ${mediaList.size}"
                            }
                            .constrainAs(counter) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(closeBtn.bottom)
                            }
                    )
                }

                // same data + callback the composer's inline carousel badge already uses.
                val currentMediaId = mediaList.getOrNull(pagerState.currentPage)?.id
                val currentTagCount = mediaProductTags[currentMediaId]?.size ?: 0
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    if (isProductCatalogueEnabled && currentTagCount > 0) {
                        AmityProductTagBadge(
                            count = currentTagCount,
                            modifier = Modifier.padding(end = 12.dp, bottom = 16.dp),
                            onClick = {
                                mediaList.getOrNull(pagerState.currentPage)?.let(onTagProductClick)
                            }
                        )
                    }
                    if (showControls) {
                        VideoSeekBar(
                            exoPlayer = exoPlayer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            BackHandler {
                onDismiss()
            }
        }
    }
}
