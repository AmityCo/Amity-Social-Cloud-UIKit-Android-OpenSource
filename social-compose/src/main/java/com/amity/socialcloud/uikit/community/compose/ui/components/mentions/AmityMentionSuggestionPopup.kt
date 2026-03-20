package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.roundToInt

/**
 * A caret-anchored Popup for mention suggestions.
 *
 * Contract:
 * - [anchorInWindow] is a window-coordinate Rect representing the caret or '@' character
 *   bounds.
 * - The popup will be positioned below the anchor by default, clamped within the window.
 */
@Composable
fun AmityMentionSuggestionPopup(
    anchorInWindow: Rect,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val verticalGapPx = with(density) { 6.dp.toPx() }

    val positionProvider = remember(anchorInWindow, verticalGapPx) {
        CaretBelowPopupPositionProvider(
            anchorBoundsInWindow = IntRect(
                left = anchorInWindow.left.roundToInt(),
                top = anchorInWindow.top.roundToInt(),
                right = anchorInWindow.right.roundToInt(),
                bottom = anchorInWindow.bottom.roundToInt(),
            ),
            verticalGapPx = verticalGapPx.roundToInt(),
        )
    }

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
        content = content,
    )
}

/**
 * A caret-anchored Popup for mention suggestions that ignores X anchoring.
 *
 * This variant pins the popup to the start of the window (x=0) and only follows the caret on Y.
 * Useful for full-width suggestion cards (like the new design).
 *
 * Uses [focusable = false] so keyboard input is never stolen from the underlying text field,
 * [onDismissRequest] is invoked when the user taps anywhere outside the popup.
 */
@Composable
fun AmityMentionSuggestionPopupFullWidth(
    anchorInWindow: Rect,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val verticalGapPx = with(density) { 6.dp.toPx() }

    val positionProvider = remember(anchorInWindow, verticalGapPx) {
        CaretBelowPopupPositionProviderFullWidth(
            anchorBoundsInWindow = IntRect(
                left = anchorInWindow.left.roundToInt(),
                top = anchorInWindow.top.roundToInt(),
                right = anchorInWindow.right.roundToInt(),
                bottom = anchorInWindow.bottom.roundToInt(),
            ),
            verticalGapPx = verticalGapPx.roundToInt(),
        )
    }

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
        content = content,
    )
}

private class CaretBelowPopupPositionProvider(
    private val anchorBoundsInWindow: IntRect,
    private val verticalGapPx: Int,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // Prefer below the caret
        val desiredX = anchorBoundsInWindow.left
        val desiredY = anchorBoundsInWindow.bottom + verticalGapPx

        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)

        val x = desiredX.coerceIn(0, maxX)
        var y = desiredY.coerceIn(0, maxY)

        // If there isn't enough space below, flip above the caret.
        val spaceBelow = windowSize.height - desiredY
        if (spaceBelow < popupContentSize.height) {
            val aboveY = anchorBoundsInWindow.top - verticalGapPx - popupContentSize.height
            y = aboveY.coerceIn(0, maxY)
        }

        return IntOffset(x, y)
    }
}

private class CaretBelowPopupPositionProviderFullWidth(
    private val anchorBoundsInWindow: IntRect,
    private val verticalGapPx: Int,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val desiredY = anchorBoundsInWindow.bottom + verticalGapPx
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)

        var y = desiredY.coerceIn(0, maxY)

        // Flip above caret if not enough space below
        val spaceBelow = windowSize.height - desiredY
        if (spaceBelow < popupContentSize.height) {
            val aboveY = anchorBoundsInWindow.top - verticalGapPx - popupContentSize.height
            y = aboveY.coerceIn(0, maxY)
        }

        return IntOffset(0, y)
    }
}