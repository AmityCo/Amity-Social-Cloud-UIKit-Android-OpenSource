package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.util.Locale
import kotlinx.coroutines.delay
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.chat.compose.live.elements.CenterConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityChatMediaPreviewDialog(
    media: List<Any>, // List of AmityImage or AmityVideo
    selectedIndex: Int = 0,
    isCurrentUser: Boolean = false,
    onDeleteMessage: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isVideoMedia = media.any { it is AmityVideo }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(10_000)
            .setSeekForwardIncrementMs(10_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }
    var isAudioMuted by remember { mutableStateOf(false) }
    var verticalDragAmount by remember { mutableFloatStateOf(0f) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(initialPage = selectedIndex) { media.size }

    LaunchedEffect(media) {
        exoPlayer.clearMediaItems()
        media.filterIsInstance<AmityVideo>().forEach { video ->
            video.getVideoUrlWithFallbackQuality()?.let { url ->
                exoPlayer.addMediaItem(MediaItem.fromUri(url))
            }
        }
        exoPlayer.prepare()
    }

    LaunchedEffect(pagerState.currentPage) {
        // Find video index within the video items for ExoPlayer
        val currentItem = media.getOrNull(pagerState.currentPage)
        if (currentItem is AmityVideo) {
            val videoIndex = media.take(pagerState.currentPage + 1).count { it is AmityVideo } - 1
            exoPlayer.seekTo(videoIndex, 0)
            exoPlayer.pause()
        }
    }

    if (showDeleteConfirm) {
        CenterConfirmDeletePopup(
            onCancel = {
                showDeleteConfirm = false
            },
            onDelete = {
                showDeleteConfirm = false
                onDeleteMessage?.invoke()
                onDismiss()
            }
        )
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                key = { it },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111111))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (verticalDragAmount > 0) {
                                    onDismiss()
                                }
                                verticalDragAmount = 0f
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            verticalDragAmount += dragAmount
                        }
                    }
            ) { index ->
                when (val item = media.getOrNull(index)) {
                    is AmityImage -> {
                        ImagePreviewPage(image = item)
                    }

                    is AmityVideo -> {
                        VideoPreviewPage(
                            exoPlayer = exoPlayer,
                            isVisible = pagerState.currentPage == index,
                        )
                    }

                    else -> Unit
                }
            }

            // Top bar: close button, mute toggle, page counter
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack)),
            ) {
                val (closeBtn, muteBtn, counter) = createRefs()

                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.TRANSPARENT,
                    hierarchy = AmityButtonHierarchy.PRIMARY,
                    iconSize = AmityIconButtonSize.SIZE32,
                    icon = CommonR.drawable.amity_ic_cross_r,
                    contentDescription = "Close",
                    onClick = onDismiss,
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE)
                        .constrainAs(closeBtn) {
                            top.linkTo(parent.top, margin = 13.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                )

                if (isVideoMedia) {
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.TRANSPARENT,
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        iconSize = AmityIconButtonSize.SIZE32,
                        icon = if (isAudioMuted) CommonR.drawable.amity_ic_volume_slash_s
                        else CommonR.drawable.amity_ic_volume_high_s,
                        contentDescription = "Video Audio",
                        onClick = {
                            isAudioMuted = !isAudioMuted
                            exoPlayer.volume = if (isAudioMuted) 0f else 1f
                        },
                        modifier = Modifier.constrainAs(muteBtn) {
                            top.linkTo(parent.top, margin = 13.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                    )
                }

                if (media.size > 1) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${media.size}",
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.token(AmityColorToken.TextBaseInverse),
                        ),
                        modifier = Modifier
                            .semantics {
                                contentDescription =
                                    "Photo ${pagerState.currentPage + 1} of ${media.size}"
                            }
                            .constrainAs(counter) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(closeBtn.bottom)
                            }
                    )
                }
            }

            // The bottom actions float as free-standing 40dp Transparent/Primary discs — the
            // design has no band surface behind them.
            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(72.dp),
            ) {
                val (saveBtn, deleteBtn) = createRefs()

                // Save button — bottom right. Hidden for videos for now — remove this gate
                // to restore.
                val currentPageItem = media.getOrNull(pagerState.currentPage)
                if (currentPageItem !is AmityVideo) AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.TRANSPARENT,
                    hierarchy = AmityButtonHierarchy.PRIMARY,
                    iconSize = AmityIconButtonSize.SIZE40,
                    icon = CommonR.drawable.amity_ic_arrow_down_to_bracket_r,
                    contentDescription = "Save",
                    onClick = {
                        val currentItem = media.getOrNull(pagerState.currentPage)
                        scope.launch {
                            when (currentItem) {
                                is AmityImage -> saveImageToGallery(context, currentItem)
                                is AmityVideo -> saveVideoToGallery(context, currentItem)
                            }
                        }
                    },
                    modifier = Modifier.constrainAs(saveBtn) {
                        end.linkTo(parent.end, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                )

                if (isCurrentUser && onDeleteMessage != null) {
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.TRANSPARENT,
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        iconSize = AmityIconButtonSize.SIZE40,
                        icon = CommonR.drawable.amity_ic_trash_r,
                        contentDescription = "Delete",
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.constrainAs(deleteBtn) {
                            start.linkTo(parent.start, margin = 16.dp)
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        },
                    )
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }

            BackHandler {
                onDismiss()
            }
        }
    }
}

@Composable
private fun ImagePreviewPage(
    image: AmityImage,
    modifier: Modifier = Modifier,
) {
    val imageUrl = image.getUrl(AmityImage.Size.LARGE) ?: image.getUrl(AmityImage.Size.MEDIUM)
    var aspectRatio by remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            val imageBoxModifier = if (aspectRatio != null) {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio!!)
            } else {
                Modifier.fillMaxSize()
            }

            Box(modifier = imageBoxModifier) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Image Preview",
                    contentScale = ContentScale.Fit,
                    onSuccess = { result ->
                        val size = result.painter.intrinsicSize
                        if (size.width > 0 && size.height > 0) {
                            aspectRatio = size.width / size.height
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(rememberZoomState()),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageBroken)),
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPreviewPage(
    exoPlayer: ExoPlayer,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    var isPlaying by remember { mutableStateOf(false) }
    var playerState by remember { mutableIntStateOf(Player.STATE_IDLE) }
    // Media controls are revealed by tapping the video and auto-hide while playing.
    var showControls by remember { mutableStateOf(false) }
    // Bumped on every control interaction to restart the auto-hide countdown.
    var controlsInteraction by remember { mutableIntStateOf(0) }

    // Track play/pause + buffering state, but only while this page is the visible one.
    DisposableEffect(exoPlayer, isVisible) {
        if (!isVisible) {
            return@DisposableEffect onDispose { }
        }
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                playerState = state
                isPlaying = exoPlayer.isPlaying
            }
        }
        exoPlayer.addListener(listener)
        isPlaying = exoPlayer.isPlaying
        playerState = exoPlayer.playbackState
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    // Auto-hide the controls after 1s of no interaction while playing. When paused
    // the play button stays visible (it can still be dismissed by tapping the video).
    LaunchedEffect(showControls, isPlaying, controlsInteraction) {
        if (showControls && isPlaying) {
            delay(1_000)
            showControls = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    showControls = !showControls
                }
            },
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    // Custom Compose controls are used instead of the default ExoPlayer
                    // controller UI, so the built-in controls never appear.
                    useController = false
                    player = if (isVisible) exoPlayer else null
                }
            },
            update = { view ->
                view.useController = false
                view.player = if (isVisible) exoPlayer else null
            },
        )

        if (showControls) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack)),
            )
        }

        // Center transport controls: play/pause.
        // Not gated on playback state, so seeking (which briefly buffers) doesn't
        // make the controls flicker away and back.
        if (showControls) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {

                // Fixed-size 64dp disc so the box doesn't resize when the icon swaps for the
                // buffering spinner.
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AmityTheme.token(AmityColorToken.SurfaceIconButtonTransparentPrimaryEnabled)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (playerState == Player.STATE_BUFFERING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = AmityTheme.token(AmityColorToken.IconIconButtonTransparentPrimaryDefault),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            painter = painterResource(
                                if (isPlaying) CommonR.drawable.amity_ic_video_pause_s
                                else CommonR.drawable.amity_ic_video_play_s
                            ),
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = AmityTheme.token(AmityColorToken.IconIconButtonTransparentPrimaryDefault),
                            modifier = Modifier
                                .size(32.dp)
                                .clickableWithoutRipple {
                                    if (isPlaying) exoPlayer.pause() else exoPlayer.play()
                                    controlsInteraction++
                                },
                        )
                    }
                }
            }

            // Seek bar — pinned to the bottom, lifted above the save/delete bar.
            VideoSeekBar(
                exoPlayer = exoPlayer,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
            )
        }
    }
}

// Mirrors VideoSeekBar in social-compose's AmityVideoPlayerPage. Copied here because
// chat-compose cannot depend on social-compose; keep the two in sync.
@Composable
private fun VideoSeekBar(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
) {
    var duration by remember { mutableLongStateOf(0L) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(exoPlayer) {
        while (true) {
            if (!isDragging) {
                duration = exoPlayer.duration.takeIf { it > 0 } ?: 0L
                currentPosition = exoPlayer.currentPosition
            }
            delay(100)
        }
    }

    val progress = if (duration > 0) {
        (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatVideoDuration(currentPosition),
                color = AmityTheme.token(AmityColorToken.TextBaseInverse),
                style = AmityTheme.typography.body,
            )
            Text(
                text = formatVideoDuration(duration),
                color = AmityTheme.token(AmityColorToken.TextBaseInverse),
                style = AmityTheme.typography.body,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            exoPlayer.play()
                        },
                        onDrag = { change, _ ->
                            if (duration > 0) {
                                val seekRatio = (change.position.x / size.width).coerceIn(0f, 1f)
                                val seekPosition = (seekRatio * duration).toLong()
                                exoPlayer.seekTo(seekPosition)
                                currentPosition = seekPosition
                            }
                        },
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (duration > 0) {
                            val seekPosition = (offset.x / size.width * duration).toLong()
                            exoPlayer.seekTo(seekPosition)
                        }
                    }
                },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        color = AmityTheme.token(AmityColorToken.SurfaceProgressBarEmpty),
                        shape = RoundedCornerShape(2.dp),
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(4.dp)
                        .background(
                            color = AmityTheme.token(AmityColorToken.SurfaceProgressBarFilled),
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }

            // Thumb indicator - centered on progress position
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceAtLeast(0.01f))
                    .align(Alignment.CenterStart),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(12.dp)
                        .background(AmityTheme.token(AmityColorToken.SurfaceProgressKnobDefault), RoundedCornerShape(6.dp)),
                )
            }
        }
    }
}

private fun formatVideoDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60)) % 24
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}
