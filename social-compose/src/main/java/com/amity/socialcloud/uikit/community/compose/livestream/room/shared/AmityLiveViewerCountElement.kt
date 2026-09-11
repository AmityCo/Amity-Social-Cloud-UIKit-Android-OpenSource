package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountConfig
import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountMode
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

/**
 * The viewer-count pill in the livestream top bar.
 *
 * The admin's network-level configuration decides whether a viewer sees the number. When it is
 * suppressed the pill still renders in its LIVE-only form, so a viewer always knows the stream
 * is live — only the count is withheld.
 *
 * The config is read once when the viewer enters the page and is not observed afterwards: an
 * admin change reaches a watching viewer on their next join. Within a single mount the pill
 * still reacts to the count ticking across the threshold and to the viewer being promoted to
 * co-host.
 *
 * @param viewerCount latest viewer count, or null when it is not known yet.
 * @param isHostOrCoHost hosts and co-hosts are exempt from every mode and always see the count.
 * @param config the network configuration, or null when it has not resolved or the read failed.
 * @param isConfigResolved false while the cold read is still in flight. The pill renders nothing
 *   until it flips, so the count is never shown and then retracted.
 * @param onCountVisibilityChanged fired when count visibility flips, never on the first pass.
 */
@Composable
fun AmityLiveViewerCountElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewerCount: Int?,
    isHostOrCoHost: Boolean = false,
    config: AmityLiveViewerCountConfig?,
    isConfigResolved: Boolean = config != null,
    onCountVisibilityChanged: ((Boolean) -> Unit)? = null,
) {
    if (!isConfigResolved) {
        return
    }

    val isCountVisible = isCountVisible(
        viewerCount = viewerCount,
        isHostOrCoHost = isHostOrCoHost,
        config = config,
    )

    var lastReportedVisibility by remember { mutableStateOf(isCountVisible) }
    LaunchedEffect(isCountVisible) {
        if (isCountVisible != lastReportedVisibility) {
            lastReportedVisibility = isCountVisible
            onCountVisibilityChanged?.invoke(isCountVisible)
        }
    }

    AmityBaseElement(
        pageScope = pageScope,
        elementId = "live_viewer_count_element"
    ) {
        AmityRoomViewerCountBadge(
            modifier = modifier,
            viewerCount = viewerCount.takeIf { isCountVisible },
        )
    }
}

/**
 * Resolves the pill's count visibility. Ordered — an earlier rule is never overridden by a
 * later one.
 *
 * A null [config] means the read failed, which resolves to today's behaviour rather than to a
 * hidden count: a failure must never hide a count the admin did not ask to hide.
 */
internal fun isCountVisible(
    viewerCount: Int?,
    isHostOrCoHost: Boolean,
    config: AmityLiveViewerCountConfig?,
): Boolean {
    if (isHostOrCoHost) {
        return true
    }
    return when (config?.mode) {
        null, AmityLiveViewerCountMode.ALWAYS_SHOW -> true
        AmityLiveViewerCountMode.HIDE_ENTIRELY -> false
        AmityLiveViewerCountMode.SHOW_ABOVE_MINIMUM -> (viewerCount ?: 0) >= config.threshold
    }
}
