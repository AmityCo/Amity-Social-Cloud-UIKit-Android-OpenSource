package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction

/**
 * AmityKeyboard — atomic Keyboard (Native).
 *
 * IMPORTANT: The keyboard is a PLATFORM-NATIVE OS component (Android Gboard / Material-3 IME).
 * The design token system defines ZERO Keyboard tokens, so there is intentionally no token usage
 * here and nothing to skin: the OS paints every key, the action/suggestion row, and the home
 * indicator, following the system light/dark appearance rather than the app theme. UIKit never
 * renders, sizes, or restyles the keyboard.
 *
 * Because of that, this file does NOT expose a drawable @Composable that paints a keyboard.
 * Instead it captures the small keyboard-handling surface every UIKit consumer actually relies
 * on:
 *   - [AmityKeyboardReturnKeyType]  the return-key hint a text input passes to the OS keyboard
 *   - [AmityKeyboardFrame]          the payload describing the OS keyboard's frame
 *   - [AmityKeyboardObserver]       a composable that observes OS show/hide/frame changes so
 *                                   consumers can lift actionable content above the keyboard
 */

/**
 * Set by the TEXT INPUT, not by the keyboard itself. Maps 1:1 onto Compose's [ImeAction] so a
 * consumer can wire it into KeyboardOptions.
 */
enum class AmityKeyboardReturnKeyType {
    Default,
    Done,
    Search,
    Send;

    /** The Compose [ImeAction] the OS keyboard should present for this return-key hint. */
    fun toImeAction(): ImeAction = when (this) {
        Default -> ImeAction.Default
        Done -> ImeAction.Done
        Search -> ImeAction.Search
        Send -> ImeAction.Send
    }
}

/**
 * AmityKeyboardFrame — the payload delivered to the observation callbacks. Carries no
 * colour/style information, since the keyboard is never token-themed.
 *
 * @param height OS-reported keyboard height in dp, in the observing view's space; 0 when hidden.
 * @param isVisible whether the OS keyboard is currently presented.
 * @param animationDuration iOS-only; always null on Android (the IME does not report an
 *   animation duration), kept for cross-platform API parity.
 */
data class AmityKeyboardFrame(
    val height: Float,
    val isVisible: Boolean,
    val animationDuration: Long? = null,
)

/**
 * AmityKeyboardObserver — subscribes to the OS keyboard's show/hide/frame changes and invokes
 * the matching callbacks. Consumers (message composer, focused fields) use [AmityKeyboardFrame.height]
 * to keep actionable content reachable above the keyboard.
 *
 * This reads the platform IME [WindowInsets], so it reflects the real OS keyboard the system
 * draws — UIKit does not size or theme it. Place once high in a screen that hosts focusable input.
 *
 * @param onKeyboardWillShow fired when the OS keyboard begins presenting.
 * @param onKeyboardWillHide fired when the OS keyboard begins dismissing.
 * @param onKeyboardFrameChange fired on every intermediate frame change (height/type change).
 */
@Composable
fun AmityKeyboardObserver(
    onKeyboardWillShow: (AmityKeyboardFrame) -> Unit = {},
    onKeyboardWillHide: (AmityKeyboardFrame) -> Unit = {},
    onKeyboardFrameChange: (AmityKeyboardFrame) -> Unit = {},
) {
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime

    val currentShow by rememberUpdatedState(onKeyboardWillShow)
    val currentHide by rememberUpdatedState(onKeyboardWillHide)
    val currentChange by rememberUpdatedState(onKeyboardFrameChange)

    LaunchedEffect(imeInsets, density) {
        var previouslyVisible = false
        snapshotFlow { imeInsets.getBottom(density) }
            .collect { bottomPx ->
                val heightDp = with(density) { bottomPx.toDp().value }
                val isVisible = bottomPx > 0
                val frame = AmityKeyboardFrame(
                    height = heightDp,
                    isVisible = isVisible,
                    animationDuration = null,
                )
                currentChange(frame)
                if (isVisible && !previouslyVisible) {
                    currentShow(frame)
                } else if (!isVisible && previouslyVisible) {
                    currentHide(frame)
                }
                previouslyVisible = isVisible
            }
    }
}

/**
 * Convenience accessor returning the current [AmityKeyboardFrame] as observable state, for
 * consumers that prefer to read the frame directly (e.g. to pad content) rather than react
 * to discrete show/hide callbacks.
 */
@Composable
fun rememberAmityKeyboardFrame(): AmityKeyboardFrame {
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val bottomPx = imeInsets.getBottom(density)
    return remember(bottomPx, density) {
        AmityKeyboardFrame(
            height = with(density) { bottomPx.toDp().value },
            isVisible = bottomPx > 0,
            animationDuration = null,
        )
    }
}
