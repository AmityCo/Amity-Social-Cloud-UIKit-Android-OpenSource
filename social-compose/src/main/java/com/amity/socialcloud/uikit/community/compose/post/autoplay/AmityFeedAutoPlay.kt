package com.amity.socialcloud.uikit.community.compose.post.autoplay


import android.view.TextureView
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.semantics.text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.databinding.AmityFeedAutoplayPlayerBinding
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * The feed's autoplay controller. Null outside a feed, and while autoplay is not allowed, so search
 * results, pending posts and a feed with autoplay off keep the plain thumbnail.
 */
val LocalAmityFeedAutoPlayController = staticCompositionLocalOf<AmityFeedAutoPlayController?> { null }

/** True inside an [AmityFeedAutoPlayHost], whether or not autoplay is allowed right now. */
private val LocalAmityFeedAutoPlayHosted = staticCompositionLocalOf { false }

/**
 * Wraps one feed list. Every video frame inside [content] can autoplay, and they share one player.
 * Autoplay runs only while [AmityFeedAutoPlayGate] allows it.
 */
@Composable
fun AmityFeedAutoPlayHost(content: @Composable () -> Unit) {
    val context = LocalContext.current.applicationContext
    LaunchedEffect(Unit) {
        AmityFeedAutoPlayGate.start(context)
    }
    val isAllowed by AmityFeedAutoPlayGate.isAllowed.collectAsState()

    // The content stays in the same place in the composition either way, so turning autoplay on or
    // off does not reset the list's scroll position.
    val controller = if (isAllowed) remember { AmityFeedAutoPlayController(context) } else null
    if (controller != null) {
        AmityFeedAutoPlayControllerEffects(controller)
    }

    CompositionLocalProvider(
        LocalAmityFeedAutoPlayController provides controller,
        LocalAmityFeedAutoPlayHosted provides true,
    ) {
        content()
    }
}

@Composable
private fun AmityFeedAutoPlayControllerEffects(controller: AmityFeedAutoPlayController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isMuted by AmityFeedAutoPlaySession.isMuted.collectAsState()
    val isWindowFocused = LocalWindowInfo.current.isWindowFocused

    // A sheet or dialog over the feed (comments, the ⋯ menu, the product tag list, the full
    // player) opens its own window and takes focus from the feed's window. The video pauses under
    // it and resumes when the feed shows again.
    LaunchedEffect(controller, isWindowFocused) {
        if (isWindowFocused) {
            controller.resume(COVERED_TOKEN)
        } else {
            controller.suspend(COVERED_TOKEN)
        }
    }

    // Pause on ON_PAUSE, not ON_STOP: opening a post starts the next page before this one stops,
    // and the post detail page must find the position this feed saved.
    DisposableEffect(lifecycleOwner, controller) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> controller.onHostStarted()
                Lifecycle.Event.ON_PAUSE -> controller.onHostStopped()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    DisposableEffect(controller) {
        onDispose { controller.release() }
    }

    LaunchedEffect(controller, isMuted) {
        controller.setMuted(isMuted)
    }

    // Stopping is immediate. Starting waits for the settle delay, which is zero by default: the
    // pick starts loading at once, and a frame a fast scroll passes stops again below half.
    LaunchedEffect(controller) {
        snapshotFlow { controller.pickCandidate() }
            .distinctUntilChanged()
            .collectLatest { key ->
                if (key != null && key != controller.activeKey) {
                    delay(AmityFeedAutoPlayConfig.settleDelayMs)
                }
                controller.activate(key)
            }
    }
}

private const val COVERED_TOKEN = "feed_covered"

private data class FrameBounds(
    val visibleTop: Float,
    val visibleBottom: Float,
    val frameHeight: Float,
    val screenHeight: Float,
)

/**
 * Marks a header that a feed pins over the top of its list, for example a sticky header. A video
 * frame under the header counts only the part below it as visible.
 */
fun Modifier.amityFeedAutoPlayCover(): Modifier = composed {
    val controller = LocalAmityFeedAutoPlayController.current ?: return@composed Modifier
    val id = remember { Any() }
    DisposableEffect(controller) {
        onDispose { controller.removeCover(id) }
    }
    Modifier.onGloballyPositioned { coordinates ->
        controller.reportCover(id, coordinates.boundsInRoot().bottom)
    }
}

/**
 * Autoplay layer for one media frame. Place it inside the frame's Box, above the thumbnail. It
 * fills the frame, reports how much of the frame is on screen, and shows the player and the sound
 * button while this frame is active.
 *
 * [onClick] keeps the frame's own tap action while the player covers the thumbnail.
 *
 * Returns true when the caller must hide its play icon. While autoplay is on, every frame hides it,
 * except the playing frame when its playback failed (SOL-PDT-3429 S-THUMBNAIL, S-FALLBACK). A play
 * icon on a waiting frame would flash on and off as the pick moves. When the first frame of the
 * playing video has not rendered after [AmityFeedAutoPlayConfig.startSpinnerDelayMs], a loading
 * indicator joins the thumbnail. [isWaiting] shows the indicator at once, for a live room that is
 * reconnecting. Returns false when autoplay is off, so the play icon shows.
 */
@OptIn(UnstableApi::class)
@Composable
fun BoxScope.AmityFeedAutoPlayLayer(
    key: String,
    source: AmityFeedAutoPlaySource,
    isEligible: Boolean = true,
    fitVideo: Boolean = false,
    isWaiting: Boolean = false,
    onClick: () -> Unit = {},
): Boolean {
    val controller = LocalAmityFeedAutoPlayController.current
    if (controller == null) {
        // Autoplay is off (the member's setting, the network switch, Data Saver or no connection)
        // in a feed that hosts it: keep the frame's id and its "thumbnail" state, as the iOS UIKit
        // does, so tests can tell the frame is there and not playing. No click handler: the
        // frame's own tap still opens the player.
        if (LocalAmityFeedAutoPlayHosted.current) {
            val videoLabel = amitySocialString("amity_social_feed_video_label")
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .testTag("feed_post_video")
                    .semantics {
                        // TalkBack reads the content description in place of the test text.
                        contentDescription = videoLabel
                        text = AnnotatedString("thumbnail")
                    }
            )
        }
        return false
    }

    DisposableEffect(controller, key) {
        onDispose { controller.unregister(key) }
    }
    LaunchedEffect(controller, key, isEligible) {
        if (!isEligible) controller.unregister(key)
    }

    // A header pinned over the list is drawn above the frame, so it does not clip the frame's
    // bounds. The part under it is taken off here, and again whenever the header changes.
    var bounds by remember(key) { mutableStateOf<FrameBounds?>(null) }
    val currentSource by rememberUpdatedState(source)
    val currentIsEligible by rememberUpdatedState(isEligible)
    LaunchedEffect(controller, key) {
        snapshotFlow { bounds to controller.coveredTop }.collect { (frame, coveredTop) ->
            if (frame == null || !currentIsEligible) return@collect
            controller.report(
                key = key,
                fraction = visibleFraction(
                    visibleHeight = uncoveredHeight(frame.visibleTop, frame.visibleBottom, coveredTop),
                    frameHeight = frame.frameHeight,
                    screenHeight = frame.screenHeight,
                ),
                top = maxOf(frame.visibleTop, coveredTop),
                source = currentSource,
            )
        }
    }

    val isActive = controller.activeKey == key
    val frameKeeper = remember(key) { FrameKeeper(key) }
    val isMuted by AmityFeedAutoPlaySession.isMuted.collectAsState()
    val isForceMuted = (source as? AmityFeedAutoPlaySource.Clip)?.isForceMuted == true
    val isSilent = isMuted || isForceMuted
    // [isWaiting]: a live room that reconnects shows the spinner whatever the player reports.
    val isLoading = isActive && !controller.hasError &&
        (!controller.isFirstFrameRendered || controller.isBuffering || isWaiting)
    // A start shows the spinner only after it has loaded for a while; a stall shows it at once. The
    // player still reports buffering just after its first frame, so a start ends only once it plays.
    var hasPlayed by remember(key) { mutableStateOf(false) }
    LaunchedEffect(isActive, controller.isFirstFrameRendered, controller.isBuffering) {
        if (!isActive) {
            hasPlayed = false
        } else if (controller.isFirstFrameRendered && !controller.isBuffering) {
            hasPlayed = true
        }
    }
    val isStarting = isActive && !controller.hasError && !hasPlayed
    var hasStartTakenLong by remember(key) { mutableStateOf(false) }
    LaunchedEffect(isStarting) {
        hasStartTakenLong = false
        if (isStarting) {
            delay(AmityFeedAutoPlayConfig.startSpinnerDelayMs)
            hasStartTakenLong = true
        }
    }
    val showsSpinner = isLoading && (!isStarting || hasStartTakenLong || isWaiting)
    val frameState = when {
        !isActive -> "thumbnail"
        controller.hasError -> "fallback"
        controller.hasEnded -> "ended"
        isLoading -> "loading"
        isSilent -> "playing_muted"
        else -> "playing_sound"
    }
    // The state words above are for tests (Maestro reads them as text). TalkBack reads the content
    // description in place of the text, and this state description in plain words.
    val videoLabel = amitySocialString("amity_social_feed_video_label")
    val spokenState = when (frameState) {
        "fallback" -> amitySocialString("amity_social_label_playback_unavailable")
        "ended" -> amitySocialString("amity_social_feed_video_ended")
        "loading" -> amitySocialString("amity_social_feed_video_loading")
        "playing_muted" -> amitySocialString("amity_social_feed_video_playing_muted")
        "playing_sound" -> amitySocialString("amity_social_feed_video_playing_sound")
        else -> null
    }

    Box(
        modifier = Modifier
            .matchParentSize()
            // Zoom mode scales the video view past the frame. Without the clip it covers the
            // post header and the carousel dots.
            .clipToBounds()
            .testTag("feed_post_video")
            // The state word as text, for Maestro (the iOS UIKit has it as the accessibility value,
            // which Maestro reads as text).
            .semantics {
                contentDescription = videoLabel
                if (spokenState != null) stateDescription = spokenState
                text = AnnotatedString(frameState)
            }
            .onGloballyPositioned { coordinates ->
                if (!isEligible) return@onGloballyPositioned
                val height = coordinates.size.height
                if (height <= 0) return@onGloballyPositioned
                // boundsInRoot is clipped by every ancestor, so it holds the visible part.
                val visible = coordinates.boundsInRoot()
                bounds = FrameBounds(
                    visibleTop = visible.top,
                    visibleBottom = visible.bottom,
                    frameHeight = height.toFloat(),
                    screenHeight = coordinates.findRootCoordinates().size.height.toFloat(),
                )
            }
    ) {
        // The frame this video stopped on, under the player. It shows while the video waits and
        // while it loads again; the player draws over it once its first frame renders.
        AmityFeedAutoPlayFrames.frameOf(key)?.let { frame ->
            if (!(isActive && controller.hasError)) {
                Image(
                    bitmap = frame,
                    contentDescription = null,
                    contentScale = if (fitVideo) ContentScale.Fit else ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
        if (isActive && !controller.hasError) {
            AndroidViewBinding(
                modifier = Modifier.matchParentSize(),
                factory = AmityFeedAutoplayPlayerBinding::inflate,
                onRelease = {
                    feedAutoplayPlayer.player = null
                    frameKeeper.view = null
                    if (controller.frameCapturer === frameKeeper.capture) controller.frameCapturer = null
                },
            ) {
                frameKeeper.view = feedAutoplayPlayer.videoSurfaceView as? TextureView
                if (controller.frameCapturer !== frameKeeper.capture) controller.frameCapturer = frameKeeper.capture
                feedAutoplayPlayer.resizeMode = if (fitVideo) {
                    AspectRatioFrameLayout.RESIZE_MODE_FIT
                } else {
                    AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
                if (feedAutoplayPlayer.player !== controller.player) {
                    feedAutoplayPlayer.player = controller.player
                }
            }

            // The player view takes the touch, so the frame's tap action is repeated here.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickableWithoutRipple { onClick() }
            )

            if (controller.hasEnded) {
                // The last frame stays, dimmed, under Watch again.
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(amityColorBlack.copy(alpha = 0.5f))
                )
                AmityFeedAutoPlayWatchAgainButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { controller.replay() },
                )
            } else if (showsSpinner) {
                AmityFeedAutoPlayLoadingOverlay(
                    label = amitySocialString("amity_social_feed_video_loading"),
                )
            }

            AmityFeedAutoPlaySoundButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp),
                isMuted = isSilent,
                isEnabled = !isForceMuted,
            )
        }
    }

    return !(isActive && controller.hasError)
}

/**
 * The feed's loading state: the frame dimmed with black at 50%, the same dim as the ended frame, and
 * the loading spinner in the centre (REQ-041b). It also shows over a live preview that is
 * reconnecting.
 */
@Composable
fun BoxScope.AmityFeedAutoPlayLoadingOverlay(label: String? = null) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(amityColorBlack.copy(alpha = 0.5f))
    )
    AmityFeedAutoPlayLoadingIndicator(
        modifier = Modifier
            .align(Alignment.Center)
            .semantics { if (label != null) contentDescription = label }
    )
}

/**
 * The video player's loading spinner: a 40×40 white quarter arc, 2 dp wide, with no track, the same
 * on every platform.
 */
@Composable
fun AmityFeedAutoPlayLoadingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "feedVideoLoading")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "feedVideoLoadingAngle",
    )
    Canvas(modifier = modifier.size(40.dp)) {
        val stroke = 2.dp.toPx()
        drawArc(
            color = amityColorWhite,
            startAngle = angle,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(size.width - stroke, size.height - stroke),
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

/**
 * Shown over the dimmed last frame of a video that played to its end. A tap plays it again. It is the
 * full video player's Watch again control at the feed's size: a 40×40 circle with a 24 dp rotate-left
 * icon and the label below. The full player draws it at 48×48.
 */
@Composable
private fun AmityFeedAutoPlayWatchAgainButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val label = amitySocialString("amity_social_feed_video_replay")
    Column(
        modifier = modifier
            .testTag("feed_post_video/replay_button")
            .clickableWithoutRipple { onClick() }
            .semantics { contentDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(amityColorBlack.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_feed_video_watch_again),
                contentDescription = null,
                tint = amityColorWhite,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = label,
            style = AmityTheme.typography.captionLegacy.copy(color = amityColorWhite),
        )
    }
}

/**
 * Round sound button in the bottom-right corner of a playing frame. It shares one sound state with
 * every feed, post detail and the full player.
 */
@Composable
fun AmityFeedAutoPlaySoundButton(
    modifier: Modifier = Modifier,
    isMuted: Boolean,
    isEnabled: Boolean = true,
) {
    val label = amitySocialString(
        if (isMuted) "amity_social_feed_video_unmute" else "amity_social_feed_video_mute"
    )
    val spokenState = amitySocialString(
        if (isMuted) "amity_social_feed_video_sound_off" else "amity_social_feed_video_sound_on"
    )
    Box(
        modifier = modifier
            .size(24.dp)
            // A disabled button is drawn at 40%, and it keeps the tap, so a tap on it does not open
            // the player (SOL-PDT-3429 R-SESSION-MUTE, a clip saved as muted).
            .alpha(if (isEnabled) 1f else 0.4f)
            .clip(CircleShape)
            .background(amityColorBlack.copy(alpha = 0.5f))
            .testTag("feed_post_video/sound_button")
            .clickableWithoutRipple {
                if (isEnabled) AmityFeedAutoPlaySession.toggleMuted()
            }
            .semantics {
                contentDescription = label
                stateDescription = spokenState
                text = AnnotatedString(if (isMuted) "muted" else "sound_on")
                if (!isEnabled) disabled()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(
                id = if (isMuted) {
                    R.drawable.amity_ic_feed_video_sound_off
                } else {
                    R.drawable.amity_ic_feed_video_sound_on
                }
            ),
            contentDescription = null,
            tint = amityColorWhite,
            modifier = Modifier.size(16.dp),
        )
    }
}

/** Keeps the frame a video stopped on (R-RESUME), read from the player's texture view. */
private class FrameKeeper(private val key: String) {
    var view: TextureView? = null

    val capture: (String) -> Unit = { activeKey ->
        val textureView = view
        if (activeKey == key && textureView != null && textureView.width > 0 && textureView.height > 0) {
            // Half size is enough for a frame in the feed, and it saves memory.
            textureView.getBitmap(textureView.width / 2, textureView.height / 2)
                ?.let {
                    AmityFeedAutoPlayFrames.save(key, it.asImageBitmap())
                    Log.d("AmityFeedAutoPlay", "kept the last frame of $key")
                }
        }
    }
}
