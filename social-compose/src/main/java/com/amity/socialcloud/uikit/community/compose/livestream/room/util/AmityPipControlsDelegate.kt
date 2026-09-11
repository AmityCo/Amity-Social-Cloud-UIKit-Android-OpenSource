package com.amity.socialcloud.uikit.community.compose.livestream.room.util

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Owns the playback controls shown inside the Picture-in-Picture window for a host Activity.
 *
 * The window is system-drawn, so its controls are declared as [android.app.RemoteAction]s and
 * taps arrive back as broadcasts rather than as ordinary clicks. The player itself lives in the
 * Composable, so the tap has to be routed there — the host registers handlers via
 * [setHandlers] and reports state via [setPlaybackState] so the icons stay truthful.
 *
 * Shared by both PiP hosts, which need identical behaviour.
 */
class AmityPipControlsDelegate(private val activity: Activity) {

    private var onTogglePlay: (() -> Unit)? = null
    private var onSkipBack: (() -> Unit)? = null
    private var onSkipForward: (() -> Unit)? = null

    private var isPlaying: Boolean = true
    private var canSeek: Boolean = false

    private var receiverRegistered = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != AMITY_PIP_CONTROL_ACTION) return
            when (AmityPipAction.fromId(intent.getStringExtra(AMITY_PIP_CONTROL_EXTRA))) {
                AmityPipAction.TOGGLE_PLAY -> onTogglePlay?.invoke()
                AmityPipAction.SKIP_BACK -> onSkipBack?.invoke()
                AmityPipAction.SKIP_FORWARD -> onSkipForward?.invoke()
                null -> Unit
            }
        }
    }

    /** Registers the broadcast receiver. Call from the host's onCreate. */
    fun register() {
        if (receiverRegistered) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        ContextCompat.registerReceiver(
            activity,
            receiver,
            IntentFilter(AMITY_PIP_CONTROL_ACTION),
            // The PendingIntent is sent by the system on our behalf and stays within the app,
            // so the receiver must not be exported.
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        receiverRegistered = true
    }

    /** Unregisters the receiver and drops handler references. Call from the host's onDestroy. */
    fun unregister() {
        if (receiverRegistered) {
            runCatching { activity.unregisterReceiver(receiver) }
            receiverRegistered = false
        }
        onTogglePlay = null
        onSkipBack = null
        onSkipForward = null
    }

    fun setHandlers(
        onTogglePlay: (() -> Unit)?,
        onSkipBack: (() -> Unit)?,
        onSkipForward: (() -> Unit)?,
    ) {
        this.onTogglePlay = onTogglePlay
        this.onSkipBack = onSkipBack
        this.onSkipForward = onSkipForward
    }

    /**
     * Updates the window's controls for the current state. No-ops when nothing changed, so a
     * chatty player does not rebuild PendingIntents on every frame.
     */
    fun setPlaybackState(isPlaying: Boolean, canSeek: Boolean, pipAllowed: Boolean) {
        if (this.isPlaying == isPlaying && this.canSeek == canSeek) return
        this.isPlaying = isPlaying
        this.canSeek = canSeek
        refresh(pipAllowed)
    }

    /** Rebuilds and applies the params so the icons match the current state. */
    fun refresh(pipAllowed: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        if (!activity.deviceSupportsPip()) return
        runCatching {
            activity.setPictureInPictureParams(
                buildRoomPipParams(
                    autoEnter = pipAllowed,
                    actions = activity.buildPipActions(isPlaying = isPlaying, canSeek = canSeek),
                )
            )
        }
    }

    /** Current actions, for hosts that build params themselves. */
    fun currentActions(): List<android.app.RemoteAction> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.buildPipActions(isPlaying = isPlaying, canSeek = canSeek)
        } else {
            emptyList()
        }
}
