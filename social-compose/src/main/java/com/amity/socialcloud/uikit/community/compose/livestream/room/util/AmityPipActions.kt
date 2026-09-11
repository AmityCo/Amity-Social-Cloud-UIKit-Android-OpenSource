package com.amity.socialcloud.uikit.community.compose.livestream.room.util

import android.app.Activity
import android.app.PendingIntent
import android.app.RemoteAction
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR

/**
 * Playback controls offered inside the Picture-in-Picture window.
 *
 * The window is drawn by the system, so these are the only controls we can put in it: the OS
 * decides their placement and styling, and allows at most three. Expand-to-full-screen is not
 * listed because the system provides it natively.
 *
 * The same set is mirrored by the in-app mini-player, so a control behaves identically whether
 * the viewer left the app or merely moved to another page inside it.
 */
enum class AmityPipAction(val id: String) {
    SKIP_BACK("amity_pip_skip_back"),
    TOGGLE_PLAY("amity_pip_toggle_play"),
    SKIP_FORWARD("amity_pip_skip_forward"),
    ;

    companion object {
        fun fromId(id: String?): AmityPipAction? = entries.firstOrNull { it.id == id }
    }
}

/** Broadcast that carries a [AmityPipAction] tap from the window back to the host Activity. */
const val AMITY_PIP_CONTROL_ACTION = "com.amity.socialcloud.uikit.PIP_CONTROL"
const val AMITY_PIP_CONTROL_EXTRA = "amity_pip_control_extra"

/**
 * Builds the window's controls for the current playback state.
 *
 * [canSeek] distinguishes recorded playback from live. A recording gets the ±10 s skips around
 * play/pause — exactly the three actions the OS allows — while live gets play/pause alone,
 * since there is nothing ahead of the live edge to skip to. There is deliberately no mute
 * control: the window has no way to show mute state, so a mute toggle there is a switch the
 * viewer cannot read, and the hardware volume keys already cover it.
 *
 * Titles double as the accessibility labels TalkBack announces, so they are localized rather
 * than hardcoded. The icons are tinted by the system; we cannot control their colour.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.buildPipActions(
    isPlaying: Boolean,
    canSeek: Boolean,
): List<RemoteAction> {
    val strings = DefaultAmitySocialStringProvider.getInstance()

    fun action(
        control: AmityPipAction,
        iconRes: Int,
        label: String,
        requestCode: Int,
    ): RemoteAction {
        val intent = Intent(AMITY_PIP_CONTROL_ACTION)
            .setPackage(packageName)
            .putExtra(AMITY_PIP_CONTROL_EXTRA, control.id)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return RemoteAction(
            Icon.createWithResource(this, iconRes),
            label,
            label,
            pendingIntent,
        )
    }

    val playPause = action(
        control = AmityPipAction.TOGGLE_PLAY,
        iconRes = if (isPlaying) CommonComposeR.drawable.amity_ic_pause else CommonComposeR.drawable.amity_ic_play_v4,
        label = if (isPlaying) {
            strings.getString("amity_social_livestream_pip_pause")
        } else {
            strings.getString("amity_social_livestream_pip_play")
        },
        requestCode = 1,
    )

    if (!canSeek) return listOf(playPause)

    return listOf(
        action(
            control = AmityPipAction.SKIP_BACK,
            iconRes = R.drawable.amity_ic_exo_rew_10,
            label = strings.getString("amity_social_livestream_pip_skip_back"),
            requestCode = 2,
        ),
        playPause,
        action(
            control = AmityPipAction.SKIP_FORWARD,
            iconRes = R.drawable.amity_ic_exo_ffwd_10,
            label = strings.getString("amity_social_livestream_pip_skip_forward"),
            requestCode = 3,
        ),
    )
}
