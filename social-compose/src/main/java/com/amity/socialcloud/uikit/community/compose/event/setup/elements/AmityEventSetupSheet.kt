package com.amity.socialcloud.uikit.community.compose.event.setup.elements

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity

/**
 * Content insets for the tall event setup sheets, without the top inset. A sheet pads its content
 * by however much of the status bar it covers, so when a bounce carries one of these sheets up into
 * the status bar it resizes mid-animation, settles towards a new position, and repeats: the flicker.
 */
@Composable
internal fun eventSetupSheetContentInsets(): WindowInsets =
    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)

/**
 * Stops upward scroll and fling that a sheet's scrolling content cannot use (at its end, or when it
 * all fits) from reaching the sheet, which would otherwise settle with that velocity and bounce past
 * its expanded position. Downward leftovers still reach the sheet, so swipe-down-to-dismiss works.
 */
internal fun Modifier.consumeUpwardOverscroll(): Modifier = nestedScroll(UpwardOverscrollConsumer)

private object UpwardOverscrollConsumer : NestedScrollConnection {
    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset =
        if (available.y < 0f) Offset(x = 0f, y = available.y) else Offset.Zero

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity =
        if (available.y < 0f) Velocity(x = 0f, y = available.y) else Velocity.Zero
}
