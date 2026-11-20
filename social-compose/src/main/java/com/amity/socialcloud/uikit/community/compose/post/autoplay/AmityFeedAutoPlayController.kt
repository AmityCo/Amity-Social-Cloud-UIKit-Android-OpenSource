package com.amity.socialcloud.uikit.community.compose.post.autoplay

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.AmityVideoResolution
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityPipSessionRegistry
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/** What a feed frame plays when it becomes the active frame. */
sealed interface AmityFeedAutoPlaySource {

    /** An uploaded video. The feed plays a low resolution rendition to save data. */
    class Video(val data: AmityPost.Data.VIDEO) : AmityFeedAutoPlaySource

    /** A clip. A clip the creator posted muted stays muted whatever the session state is. */
    class Clip(val data: AmityPost.Data.CLIP) : AmityFeedAutoPlaySource {
        val isForceMuted: Boolean get() = data.getIsMuted() == true
    }

    /**
     * A room stream. The HLS playlists need the user's access token. A live room has one playlist;
     * a recorded room plays every recorded segment in order.
     */
    class Room(
        val playbackUrls: List<String>,
        val isLive: Boolean,
    ) : AmityFeedAutoPlaySource {
        constructor(playbackUrl: String, isLive: Boolean) : this(listOf(playbackUrl), isLive)
    }
}

/**
 * The visible share of a frame, by the frame's Playback behavior: the share of the frame on screen,
 * or, for a frame taller than the screen, the share of the screen it fills.
 */
fun visibleFraction(visibleHeight: Float, frameHeight: Float, screenHeight: Float): Float {
    if (frameHeight <= 0f || visibleHeight <= 0f) return 0f
    val ofFrame = visibleHeight / frameHeight
    val ofScreen = if (screenHeight > 0f) visibleHeight / screenHeight else 0f
    return maxOf(ofFrame, ofScreen).coerceIn(0f, 1f)
}

/** The height of the part of a frame between [visibleTop] and [visibleBottom] that lies below [coveredTop]. */
fun uncoveredHeight(visibleTop: Float, visibleBottom: Float, coveredTop: Float): Float =
    (visibleBottom - maxOf(visibleTop, coveredTop)).coerceAtLeast(0f)

/**
 * Owns the single player shared by every video frame in one feed.
 *
 * Each frame reports how much of it is on screen. The playing frame keeps the player until it drops
 * below [AmityFeedAutoPlayConfig.visibleThreshold]; then the most visible frame takes over, the
 * higher one on a tie, once the pick has stayed the same for [AmityFeedAutoPlayConfig.settleDelayMs].
 * Only one frame plays at a time. Each video resumes where it stopped (see AmityFeedAutoPlayPositions).
 */
@Stable
class AmityFeedAutoPlayController(private val context: Context) {

    private data class Candidate(
        val fraction: Float,
        val top: Float,
        val source: AmityFeedAutoPlaySource,
    )

    private val candidates = mutableStateMapOf<String, Candidate>()
    private val suspendTokens = mutableStateMapOf<String, Boolean>()
    private val covers = mutableStateMapOf<Any, Float>()
    private val urlCache = mutableMapOf<String, String>()

    /** Key of the frame that owns the player, or null when nothing plays. */
    var activeKey by mutableStateOf<String?>(null)
        private set

    /** True once the active frame has drawn its first video frame. */
    var isFirstFrameRendered by mutableStateOf(false)
        private set

    /** True while the active frame waits for data, before the first frame or after a stall. */
    var isBuffering by mutableStateOf(false)
        private set

    /** True when the active frame could not play. The frame falls back to its play icon. */
    var hasError by mutableStateOf(false)
        private set

    /** True when the active video played to its end. It stays on the last frame with a replay button. */
    var hasEnded by mutableStateOf(false)
        private set

    private var isHostStarted = true

    /**
     * Set by the frame that shows the player. Called with the active key just before the player
     * stops for that frame, so the frame can keep the image it stopped on (R-RESUME).
     */
    var frameCapturer: ((String) -> Unit)? = null
    private var isMuted = true
    private val focusToken = "feed-${System.identityHashCode(this)}"
    private var activeSource: AmityFeedAutoPlaySource? = null
    private var urlDisposable: Disposable? = null

    /**
     * The shared player. It is state, so a frame whose view appeared before the first video started
     * takes the player as soon as it is created.
     */
    var player by mutableStateOf<ExoPlayer?>(null)
        private set

    private val listener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            isFirstFrameRendered = true
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            isBuffering = playbackState == Player.STATE_BUFFERING
            if (playbackState == Player.STATE_ENDED && !isLive()) {
                // No loop: it plays once and stops on the last frame.
                hasEnded = true
                savePosition()
                Log.d(TAG, "ended $activeKey")
                updatePlayWhenReady()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.w(TAG, "playback failed for $activeKey", error)
            isFirstFrameRendered = false
            isBuffering = false
            hasError = true
            updateAudioFocus()
        }
    }

    /**
     * The bottom edge, in root coordinates, of the lowest header pinned over the top of the list. A
     * frame under it is not on screen.
     */
    val coveredTop: Float
        get() = covers.values.maxOrNull() ?: 0f

    fun reportCover(id: Any, bottom: Float) {
        if (covers[id] != bottom) covers[id] = bottom
    }

    fun removeCover(id: Any) {
        covers.remove(id)
    }

    /** Called by a frame on every layout pass. [fraction] is the visible share of the frame. */
    fun report(key: String, fraction: Float, top: Float, source: AmityFeedAutoPlaySource) {
        val current = candidates[key]
        if (current != null && current.fraction == fraction && current.top == top) return
        candidates[key] = Candidate(fraction, top, source)
    }

    fun unregister(key: String) {
        candidates.remove(key)
        if (activeKey == key) activate(null)
    }

    /**
     * The frame that should play now, or null. The playing frame keeps the player until it drops
     * below the threshold, even when another frame is more visible.
     */
    fun pickCandidate(): String? {
        val threshold = AmityFeedAutoPlayConfig.visibleThreshold
        val eligible = candidates.filterValues { it.fraction >= threshold }
        if (eligible.isEmpty()) return null
        activeKey?.let { key -> if (key in eligible) return key }
        val best = eligible.maxOf { it.value.fraction }
        return eligible.entries
            .filter { it.value.fraction >= best - FRACTION_TOLERANCE }
            .minByOrNull { it.value.top }
            ?.key
    }

    /** Hands the player to [key], or stops playback when [key] is null. */
    fun activate(key: String?) {
        if (key == activeKey) return
        captureFrame()
        savePosition()
        urlDisposable?.dispose()
        player?.run {
            stop()
            clearMediaItems()
        }
        isFirstFrameRendered = false
        isBuffering = false
        hasError = false
        hasEnded = false
        activeKey = key
        activeSource = null
        if (key == null) {
            updateAudioFocus()
            return
        }

        val source = candidates[key]?.source ?: run {
            activeKey = null
            return
        }
        activeSource = source
        urlDisposable = resolveUrl(key, source)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { urls -> if (activeKey == key) startPlayback(source, urls) },
                { error ->
                    Log.w(TAG, "could not resolve a playable url for $key", error)
                    if (activeKey == key) {
                        hasError = true
                        updateAudioFocus()
                    }
                },
            )
    }

    /** Stops playback while [token] holds it, for example while the full-screen player is open. */
    fun suspend(token: String) {
        savePosition()
        suspendTokens[token] = true
        updatePlayWhenReady()
    }

    fun resume(token: String) {
        suspendTokens.remove(token)
        syncPosition()
        rejoinLiveEdge()
        updatePlayWhenReady()
    }

    fun onHostStarted() {
        isHostStarted = true
        syncPosition()
        rejoinLiveEdge()
        updatePlayWhenReady()
    }

    /** A live preview that was paused joins the live stream again, not the point it paused at. */
    private fun rejoinLiveEdge() {
        if (isLive() && suspendTokens.isEmpty()) player?.seekToDefaultPosition()
    }

    fun onHostStopped() {
        savePosition()
        isHostStarted = false
        updatePlayWhenReady()
    }

    /** Plays the ended video again from the start. */
    fun replay() {
        val key = activeKey ?: return
        hasEnded = false
        AmityFeedAutoPlayPositions.save(key, 0L, hasEnded = false)
        player?.seekTo(0, 0L)
        updatePlayWhenReady()
    }

    fun setMuted(muted: Boolean) {
        val wasMuted = isMuted
        isMuted = muted
        applyAudio()
        if (wasMuted && !muted && player?.volume != 0f) {
            // Only one thing may be heard at a time. A floating livestream would otherwise play
            // over the feed.
            AmityPipSessionRegistry.endSession()
        }
    }

    fun release() {
        captureFrame()
        savePosition()
        urlDisposable?.dispose()
        player?.run {
            removeListener(listener)
            release()
        }
        player = null
        activeKey = null
        AmityFeedAudioFocus.release(focusToken)
        candidates.clear()
        covers.clear()
    }

    @OptIn(UnstableApi::class)
    private fun startPlayback(source: AmityFeedAutoPlaySource, urls: List<String>) {
        val url = urls.first()
        // The query holds a signed playback token, so it stays out of the log.
        Log.d(TAG, "playing $activeKey from ${url.substringBefore('?')} (${urls.size} item(s))")
        val exoPlayer = player ?: createPlayer().also { player = it }
        when (source) {
            is AmityFeedAutoPlaySource.Room -> {
                val dataSourceFactory = DefaultHttpDataSource.Factory()
                    .setAllowCrossProtocolRedirects(true)
                AmityCoreClient.getAccessToken()?.let { token ->
                    dataSourceFactory.setDefaultRequestProperties(mapOf("Authorization" to "Bearer $token"))
                }
                val hlsFactory = HlsMediaSource.Factory(dataSourceFactory)
                val mediaSources = urls.map { segmentUrl ->
                    val mediaItem = MediaItem.Builder()
                        .setUri(segmentUrl.toUri())
                        .apply {
                            if (source.isLive) {
                                setLiveConfiguration(
                                    MediaItem.LiveConfiguration.Builder()
                                        .setTargetOffsetMs(LIVE_TARGET_OFFSET_MS)
                                        .build()
                                )
                            }
                        }
                        .build()
                    hlsFactory.createMediaSource(mediaItem)
                }
                exoPlayer.setMediaSources(mediaSources)
            }

            else -> exoPlayer.setMediaItem(MediaItem.fromUri(url))
        }
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
        // Resume where the video stopped. A live room always joins at the live edge instead.
        val key = activeKey
        if (key != null && !isLive()) {
            val positionMs = AmityFeedAutoPlayPositions.positionOf(key)
            val itemIndex = AmityFeedAutoPlayPositions.itemIndexOf(key).coerceAtMost(urls.lastIndex)
            hasEnded = AmityFeedAutoPlayPositions.hasEnded(key)
            if (positionMs > 0L || itemIndex > 0) exoPlayer.seekTo(itemIndex, positionMs)
            Log.d(TAG, "resume $key at ${positionMs}ms${if (hasEnded) ", ended" else ""}")
        }
        applyAudio()
        exoPlayer.prepare()
        updatePlayWhenReady()
    }

    @OptIn(UnstableApi::class)
    private fun createPlayer(): ExoPlayer {
        // A small buffer: a muted preview that the viewer may scroll past in a second must not
        // download most of a progressive file first.
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                MIN_BUFFER_MS,
                MAX_BUFFER_MS,
                BUFFER_FOR_PLAYBACK_MS,
                BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
            )
            .build()
        val dataSourceFactory = DefaultDataSource.Factory(
            context,
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true),
        )
        return ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
            .build()
            .apply {
                // AmityFeedAudioFocus handles focus. ExoPlayer would take full focus, and the
                // viewer's music would not play again after a mute.
                setAudioAttributes(AUDIO_ATTRIBUTES, false)
                addListener(listener)
            }
    }

    private fun applyAudio() {
        val exoPlayer = player ?: return
        exoPlayer.volume = if (isSilent()) 0f else 1f
        updateAudioFocus()
    }

    private fun updatePlayWhenReady() {
        player?.playWhenReady = shouldPlay()
        updateAudioFocus()
    }

    private fun isSilent(): Boolean =
        isMuted || (activeSource as? AmityFeedAutoPlaySource.Clip)?.isForceMuted == true

    private fun shouldPlay(): Boolean =
        isHostStarted && suspendTokens.isEmpty() && activeKey != null && !hasEnded

    /**
     * Holds transient audio focus only while a video plays with sound. A silent or stopped player
     * gives it back, so the viewer's music plays again (R-NO-AUDIO-INTERRUPT, PDT-7114).
     */
    private fun updateAudioFocus() {
        if (player != null && !isSilent() && !hasError && shouldPlay()) {
            // Another app took the sound: the feed goes silent for the rest of the session.
            AmityFeedAudioFocus.request(context, focusToken) { AmityFeedAutoPlaySession.setMuted(true) }
        } else {
            AmityFeedAudioFocus.release(focusToken)
        }
    }

    /** A live room has no fixed frame to come back to, so it shows its thumbnail when scrolled back. */
    private fun captureFrame() {
        val key = activeKey ?: return
        if (!isFirstFrameRendered || hasError || isLive()) return
        frameCapturer?.invoke(key)
    }

    private fun isLive(): Boolean = (activeSource as? AmityFeedAutoPlaySource.Room)?.isLive == true

    /**
     * Takes the shared position when another surface moved the video on while this feed was paused,
     * for example post detail played it to the end, or the full player was opened on it.
     */
    private fun syncPosition() {
        val key = activeKey ?: return
        val exoPlayer = player ?: return
        if (isLive() || exoPlayer.playbackState == Player.STATE_IDLE) return
        val sharedMs = AmityFeedAutoPlayPositions.positionOf(key)
        val sharedEnded = AmityFeedAutoPlayPositions.hasEnded(key)
        if (sharedEnded == hasEnded && kotlin.math.abs(sharedMs - exoPlayer.currentPosition) < SYNC_TOLERANCE_MS) return
        Log.d(TAG, "sync $key to ${sharedMs}ms${if (sharedEnded) ", ended" else ""}")
        hasEnded = sharedEnded
        // The full player plays the original file, which can run a little longer than this stream.
        // A seek past the end draws no frame, so an ended video seeks to its own last frame.
        val duration = exoPlayer.duration
        val targetMs = if (sharedEnded && duration != C.TIME_UNSET) {
            (duration - LAST_FRAME_OFFSET_MS).coerceAtLeast(0L)
        } else {
            sharedMs
        }
        exoPlayer.seekTo(AmityFeedAutoPlayPositions.itemIndexOf(key), targetMs)
    }

    private fun savePosition() {
        val key = activeKey ?: return
        val exoPlayer = player ?: return
        if (isLive() || exoPlayer.playbackState == Player.STATE_IDLE) return
        AmityFeedAutoPlayPositions.save(key, exoPlayer.currentPosition, hasEnded, exoPlayer.currentMediaItemIndex)
    }

    private fun resolveUrl(key: String, source: AmityFeedAutoPlaySource): Single<List<String>> {
        if (source is AmityFeedAutoPlaySource.Room) {
            // A room playlist is not cached: a live room can end between two plays.
            return if (source.playbackUrls.isEmpty()) {
                Single.error(IllegalStateException("room has no playback url"))
            } else {
                Single.just(source.playbackUrls)
            }
        }
        urlCache[key]?.let { return Single.just(listOf(it)) }
        val lookup: Single<String> = when (source) {
            is AmityFeedAutoPlaySource.Video -> source.data.getVideo().map { video ->
                // A failed transcode never gets a stream, so it shows the fallback at once, not a
                // spinner that never ends (SOL-PDT-3429 S-FALLBACK).
                if (AmityVideo.Status.enumOf(video.getStatus()) == AmityVideo.Status.TRANSCODE_FAILED) {
                    throw IllegalStateException("video transcode failed")
                }
                previewUrl(video) ?: throw VideoNotReadyException()
            }

            is AmityFeedAutoPlaySource.Clip -> source.data.getClip().map { clip ->
                clip.getClipUrl() ?: throw IllegalStateException("clip has no url")
            }

            is AmityFeedAutoPlaySource.Room -> error("handled above")
        }
        // The SDK lookup never emits when the file is missing from the local store.
        return lookup
            .timeout(URL_LOOKUP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // A video still being transcoded has no url yet. It keeps the spinner and is looked up
            // again until its stream is ready, for as long as it stays the active frame.
            .retryWhen { errors ->
                errors.flatMap { error ->
                    if (error is VideoNotReadyException) {
                        Flowable.timer(TRANSCODE_RETRY_SECONDS, TimeUnit.SECONDS)
                    } else {
                        Flowable.error(error)
                    }
                }
            }
            .doOnSuccess { urlCache[key] = it }
            .map { listOf(it) }
    }

    private fun previewUrl(video: AmityVideo): String? {
        val available = video.getResolutions()
        return PREVIEW_RESOLUTIONS
            .firstOrNull { it in available }
            ?.let { video.getVideoUrl(it) }
            ?: video.getVideoUrl(AmityVideoResolution.ORIGINAL)
    }

    private companion object {
        const val TAG = "AmityFeedAutoPlay"
        const val FRACTION_TOLERANCE = 0.01f
        const val SYNC_TOLERANCE_MS = 1_000L
        const val LAST_FRAME_OFFSET_MS = 100L
        const val MIN_BUFFER_MS = 2_000
        const val MAX_BUFFER_MS = 8_000
        const val BUFFER_FOR_PLAYBACK_MS = 1_000
        const val BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 1_500
        const val LIVE_TARGET_OFFSET_MS = 6_000L
        const val URL_LOOKUP_TIMEOUT_SECONDS = 10L
        const val TRANSCODE_RETRY_SECONDS = 3L

        /** Lowest useful size first. A feed frame is never larger than the screen width. */
        val PREVIEW_RESOLUTIONS = listOf(
            AmityVideoResolution.RES_480,
            AmityVideoResolution.RES_360,
            AmityVideoResolution.RES_720,
            AmityVideoResolution.RES_1080,
        )

        val AUDIO_ATTRIBUTES: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    }
}

/** A video that is still being transcoded after upload and has no playable url yet. */
private class VideoNotReadyException : IllegalStateException("video has no url yet")
