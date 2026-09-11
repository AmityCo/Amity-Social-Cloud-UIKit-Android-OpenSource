package com.amity.socialcloud.uikit.community.compose.livestream.room.util

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.util.Rational
import androidx.annotation.RequiresApi

/**
 * Bridge that the Compose room player (`AmityRoomPlayerPage`) uses to drive its host
 * Activity's Picture-in-Picture behaviour. Implemented by `AmityRoomPlayerPageActivity`.
 *
 * PiP is a whole-Activity window transition, so the actual `enterPictureInPictureMode`
 * call has to live on the Activity — the Composable only expresses intent through this
 * interface. The current PiP state is passed back into the Composable as a parameter
 * (state-backed) so overlays can be hidden while in the floating window.
 *
 * NOTE (prototype): scoped to the live room viewer only. Broadcaster/co-host mode must
 * never enter PiP — the page gates that via [setPipAllowed].
 */
interface AmityRoomPipController {

    /**
     * Whether PiP is currently allowed. Drives auto-enter (API 31+) and the manual
     * [enterPipMode] guard. The page sets this to `true` only in viewer mode.
     */
    fun setPipAllowed(allowed: Boolean)

    /**
     * Request an immediate PiP transition (e.g. background fallback on API < 31).
     *
     * Returns whether a window actually opened. It will not when PiP is ineligible, when the
     * viewer has switched it off in device settings — which no API lets us check beforehand,
     * so the refusal is the only signal — or when the Activity is no longer resumed. Callers
     * that queue work behind the transition MUST branch on this rather than assume success.
     */
    fun enterPipMode(): Boolean

    /**
     * Enter PiP, then launch [intent] full-screen once the PiP transition has settled.
     *
     * The room becomes the pinned PiP task; the launch is deferred until that is confirmed
     * and then flagged [Intent.FLAG_ACTIVITY_NEW_TASK] so it routes by task-affinity into the
     * base full-screen task rather than being dropped into the pinned task (which would show
     * the destination inside the PiP window). Falls back to a plain launch when PiP is
     * unavailable, so navigation always works.
     */
    fun enterPipAndStart(intent: Intent)

    /**
     * Supplies the handlers invoked when the viewer taps a control inside the floating window.
     *
     * The player lives in the Composable, but the window's controls are owned by the Activity,
     * so taps have to be routed back. Pass null on teardown to drop the references.
     *
     * The skip handlers are only ever invoked for recorded playback — live offers no skips.
     */
    fun setPipControlHandlers(
        onTogglePlay: (() -> Unit)?,
        onSkipBack: (() -> Unit)?,
        onSkipForward: (() -> Unit)?,
    )

    /**
     * Reports the current playback state so the window's controls stay truthful — a paused
     * stream must not keep offering "pause". [canSeek] is true for recorded playback, which
     * adds the ±10 s skips. Rebuilds the actions on every change.
     */
    fun setPipPlaybackState(isPlaying: Boolean, canSeek: Boolean)
}

/** True when the OS + device support Picture-in-Picture (API 26+ with the system feature). */
fun Activity.deviceSupportsPip(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)

/**
 * Whether a pause is a navigation away that should leave the stream in a floating window.
 *
 * Pause is a blunt signal — it also fires for things that are not the viewer going somewhere —
 * so each of those is excluded:
 *  - **finishing**: a deliberate exit stops playback, it never opens a window;
 *  - **configuration change**: a rotation is not navigation;
 *  - **already floating**: auto-enter (API 31+) beats us to it when the viewer presses Home;
 *  - **screen off**: locking the phone must not leave a window waiting on unlock.
 *
 * Shared by both PiP hosts so in-app navigation behaves identically in each.
 */
fun Activity.shouldEnterPipOnPause(): Boolean {
    if (isFinishing || isChangingConfigurations) return false
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
    if (isInPictureInPictureMode) return false
    val power = getSystemService(Context.POWER_SERVICE) as? PowerManager
    return power?.isInteractive != false
}

/**
 * Build PiP params for the room player. Aspect ratio is portrait (9:16) to match the
 * portrait livestream surface. `autoEnter`/seamless resize only exist on API 31+.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun buildRoomPipParams(
    autoEnter: Boolean,
    actions: List<android.app.RemoteAction> = emptyList(),
): PictureInPictureParams {
    val builder = PictureInPictureParams.Builder()
        .setAspectRatio(Rational(9, 16))
    if (actions.isNotEmpty()) {
        builder.setActions(actions)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        builder.setAutoEnterEnabled(autoEnter)
        builder.setSeamlessResizeEnabled(false)
    }
    return builder.build()
}
