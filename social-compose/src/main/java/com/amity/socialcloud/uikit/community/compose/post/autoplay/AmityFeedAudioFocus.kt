package com.amity.socialcloud.uikit.community.compose.post.autoplay

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Audio focus for the feed player and the full player while they play with sound.
 *
 * The players ask for transient focus. Another app's music then pauses while a video plays with
 * sound, and plays again when the viewer mutes the video or leaves it (SOL-PDT-3429
 * R-NO-AUDIO-INTERRUPT, PDT-7114). ExoPlayer can only ask for full focus for media, and a music app
 * that loses full focus does not play again.
 *
 * Each player holds focus under its own token. Focus is given back a short time after the last
 * token lets go, so the music does not play for a moment when the feed hands the sound to the
 * full player.
 */
object AmityFeedAudioFocus {

    private const val TAG = "AmityFeedAutoPlay"
    private const val ABANDON_DELAY_MS = 500L

    private val holders = linkedMapOf<String, () -> Unit>()
    private val handler = Handler(Looper.getMainLooper())
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null
    private var hasFocus = false

    private val abandonRunnable = Runnable { abandon() }

    private val focusListener = AudioManager.OnAudioFocusChangeListener { change ->
        // The system may call from another thread.
        handler.post { onFocusChange(change) }
    }

    /**
     * Asks for focus under [token]. [onLost] runs when another app takes the focus. The player
     * must then go silent, because two sounds must not play at once.
     */
    fun request(context: Context, token: String, onLost: () -> Unit) {
        holders[token] = onLost
        handler.removeCallbacks(abandonRunnable)
        if (hasFocus) return
        val manager = audioManager
            ?: (context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager)
                ?.also { audioManager = it }
            ?: return
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val request = focusRequest ?: AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build()
                )
                .setOnAudioFocusChangeListener(focusListener)
                .build()
                .also { focusRequest = it }
            manager.requestAudioFocus(request)
        } else {
            @Suppress("DEPRECATION")
            manager.requestAudioFocus(
                focusListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            )
        }
        hasFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        Log.d(TAG, "audio focus for $token: ${if (hasFocus) "granted" else "refused"}")
        if (!hasFocus) {
            holders.remove(token)
            onLost()
        }
    }

    /** Lets go of the focus held under [token]. */
    fun release(token: String) {
        if (holders.remove(token) == null) return
        if (holders.isEmpty()) {
            handler.removeCallbacks(abandonRunnable)
            handler.postDelayed(abandonRunnable, ABANDON_DELAY_MS)
        }
    }

    private fun onFocusChange(change: Int) {
        when (change) {
            // A short sound, for example a notification, only lowers the volume. The system does
            // that, so the player keeps its sound.
            AudioManager.AUDIOFOCUS_LOSS,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(TAG, "audio focus lost ($change)")
                val lost = holders.values.toList()
                holders.clear()
                handler.removeCallbacks(abandonRunnable)
                abandon()
                lost.forEach { it() }
            }
        }
    }

    private fun abandon() {
        if (holders.isNotEmpty() || !hasFocus) return
        val manager = audioManager ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let { manager.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            manager.abandonAudioFocus(focusListener)
        }
        hasFocus = false
        Log.d(TAG, "audio focus given back")
    }
}
