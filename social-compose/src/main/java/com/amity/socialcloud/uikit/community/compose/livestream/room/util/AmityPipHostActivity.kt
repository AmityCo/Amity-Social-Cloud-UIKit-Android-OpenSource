package com.amity.socialcloud.uikit.community.compose.livestream.room.util

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle

/**
 * Base for the Activities that host a livestream player in Picture-in-Picture.
 *
 * There are two — the live room player and the recorded-livestream player — and every part of
 * the PiP contract is identical between them: the same eligibility flag, the same entry points,
 * the same disarm-before-exit rule, the same session bookkeeping. Keeping that here means a
 * behaviour can only be fixed in one place, so the two hosts cannot quietly drift apart the way
 * they did while each carried its own copy.
 *
 * Subclasses supply only what differs: which post is playing ([currentPostId]) and what to show.
 */
abstract class AmityPipHostActivity : AppCompatActivity(), AmityRoomPipController {

    /** State-backed so the Composable recomposes and hides its overlays when PiP toggles. */
    protected var isInPipMode by mutableStateOf(false)
        private set

    /** Only a viewer with playable media may float; broadcaster/co-host mode sets this false. */
    private var pipAllowed = false

    /** A navigation deferred until the PiP transition settles (see [enterPipAndStart]). */
    private var pendingLaunch: Intent? = null

    /** Owns the window's playback controls; taps arrive as broadcasts, not clicks. */
    private val pipControls by lazy { AmityPipControlsDelegate(this) }

    /**
     * The post being played, used to match a re-tap against the open window so tapping the same
     * livestream expands it instead of starting a second session. Subclasses set this before
     * they show any content.
     */
    protected var currentPostId: String? = null

    /**
     * Reconciles this new player against whatever is already floating, and reports whether the
     * caller should stand down. Subclasses call it from onCreate and finish immediately when it
     * returns true.
     *
     * Same post — expand the window that is already open rather than starting a second session.
     * Different post — end that session first, so two streams never play at once.
     */
    protected fun expandExistingSessionFor(postId: String?): Boolean {
        if (AmityPipSessionRegistry.expandExisting(this, postId)) return true
        AmityPipSessionRegistry.endSessionForOtherPost(postId)
        return false
    }

    /** Registers the window's control receiver. Subclasses call this from onCreate. */
    protected fun registerPipControls() = pipControls.register()

    override fun setPipAllowed(allowed: Boolean) {
        pipAllowed = allowed
        // Keep the params in sync so the system can auto-enter PiP on background (API 31+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && deviceSupportsPip()) {
            setPictureInPictureParams(
                buildRoomPipParams(autoEnter = allowed, actions = pipControls.currentActions())
            )
        }
    }

    override fun enterPipMode(): Boolean {
        if (!pipAllowed) return false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || !deviceSupportsPip()) return false
        return try {
            // Reports failure by returning false, not by throwing — the viewer having switched
            // PiP off in device settings comes back this way, and there is no API to check for
            // it up front. The result has to be honoured, not discarded.
            enterPictureInPictureMode(
                buildRoomPipParams(autoEnter = true, actions = pipControls.currentActions())
            )
        } catch (e: IllegalStateException) {
            // Not permitted right now — typically the Activity is no longer resumed.
            false
        }
    }

    override fun enterPipAndStart(intent: Intent) {
        // Defer the launch until onPictureInPictureModeChanged confirms we're pinned, so the
        // destination can be routed into a task of its own rather than into the PiP window.
        pendingLaunch = intent
        if (!enterPipMode()) {
            // No window opened — most often because the viewer disabled PiP in device
            // settings. That callback will never arrive, so the queued destination would be
            // stranded and the tap would do nothing at all. Navigate now instead: the viewer
            // still gets where they were going, just without the stream floating over it.
            pendingLaunch = null
            startActivity(intent)
        }
    }

    override fun setPipControlHandlers(
        onTogglePlay: (() -> Unit)?,
        onSkipBack: (() -> Unit)?,
        onSkipForward: (() -> Unit)?,
    ) {
        pipControls.setHandlers(onTogglePlay, onSkipBack, onSkipForward)
    }

    override fun setPipPlaybackState(isPlaying: Boolean, canSeek: Boolean) {
        pipControls.setPlaybackState(isPlaying, canSeek, pipAllowed)
    }

    /**
     * A deliberate exit must never leave a floating window behind: closing the stream or
     * pressing back stops playback, it does not enter PiP.
     *
     * Auto-enter is armed for as long as the viewer is watching, so it has to be cleared before
     * the Activity tears down. Both finish paths are overridden because pages close via
     * `closePageWithResult`, which calls [finishAfterTransition] rather than [finish] —
     * guarding only one of them would miss every in-page exit.
     */
    private fun disarmPipBeforeFinish() {
        pipAllowed = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && deviceSupportsPip()) {
            try {
                setPictureInPictureParams(buildRoomPipParams(autoEnter = false))
            } catch (e: IllegalStateException) {
                // Activity is already tearing down — nothing left to disarm.
            }
        }
    }

    override fun finish() {
        disarmPipBeforeFinish()
        super.finish()
    }

    override fun finishAfterTransition() {
        disarmPipBeforeFinish()
        super.finishAfterTransition()
    }

    // Pre-31 fallback: leaving via Home doesn't auto-enter, so trigger PiP explicitly.
    // On API 31+ auto-enter handles backgrounding, so we skip to avoid a double transition.
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            enterPipMode()
        }
    }

    /**
     * Catches departures the other two entry points miss — navigation the app starts itself,
     * which merely pauses this Activity rather than backgrounding the app. Best effort: once
     * the Activity is no longer resumed the OS refuses the request, so anything that must
     * float should ask before it navigates (see [enterPipAndStart]).
     */
    override fun onPause() {
        super.onPause()
        if (shouldEnterPipOnPause()) {
            enterPipMode()
        }
    }

    override fun onDestroy() {
        pipControls.unregister()
        AmityPipSessionRegistry.onLeftPip(this)
        super.onDestroy()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration,
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInPipMode = isInPictureInPictureMode
        if (isInPictureInPictureMode) {
            AmityPipSessionRegistry.onEnteredPip(currentPostId, this)
            // Now pinned: launch the deferred destination into a SEPARATE full-screen task.
            // Modern Android pins the whole task, so NEW_TASK alone (which reuses this task by
            // affinity) would drop the destination into the PiP window. MULTIPLE_TASK forces a
            // distinct task, keeping the player as the window's content.
            pendingLaunch?.let { launch ->
                pendingLaunch = null
                launch.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                startActivity(launch)
            }
        } else {
            AmityPipSessionRegistry.onLeftPip(this)
            if (lifecycle.currentState == Lifecycle.State.CREATED) {
                // Closing the window (the ✕) stops the Activity while PiP is already false.
                // Finish so the session tears down instead of being left orphaned.
                finish()
            }
        }
    }
}
