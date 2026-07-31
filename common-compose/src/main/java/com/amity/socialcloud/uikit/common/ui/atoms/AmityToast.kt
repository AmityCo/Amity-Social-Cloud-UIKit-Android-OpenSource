package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.delay

/**
 * Status semantics for [AmityToast].
 *
 * These select which leading element resolves into the Icon slot. `variant` selects icon *content*,
 * not a token variant: the CustomToast family exposes a single Default state for Surface/Text/Icon,
 * mode-invariant across light and dark. The only token swap a variant triggers is [LOADING], which
 * substitutes a spinner bound to the Loaders-family `Surface/Loaders/Spinner/Primary/Icon` token in place
 * of the `Icon/CustomToast/Default` glyph.
 */
enum class AmityToastVariant {
    SUCCESS,
    ERROR,
    INFORMATIVE,
    LOADING,
}

// Intrinsic box-model.
private val TOAST_PADDING_TOP = 16.dp
private val TOAST_PADDING_END = 16.dp
private val TOAST_PADDING_BOTTOM = 16.dp
private val TOAST_PADDING_START = 12.dp
private val TOAST_RADIUS = 8.dp
private val TOAST_GAP = 12.dp
private val TOAST_MIN_HEIGHT = 56.dp
private val TOAST_MAX_WIDTH = 343.dp

// Icon slot: a 24x24 frame holding a 16x16 glyph.
private val TOAST_ICON_FRAME = 24.dp
private val TOAST_GLYPH = 16.dp

/**
 * AmityToast — the atomic transient status/feedback pill. Renders a rounded [Surface] carrying an
 * optional leading element (an icon glyph, or a spinner when [variant] is [AmityToastVariant.LOADING])
 * and a [message] label that wraps to a maximum of two lines.
 *
 * Presentational and auto-dismissing: it has no interactive hit-area of its own. When [duration] is
 * non-null the toast schedules [onDismiss] after that many milliseconds; pass `null` to disable
 * auto-dismiss (dismissal is then purely programmatic — the caller stops composing the toast).
 *
 * Token bindings (all a single Default state, mode-invariant):
 *  - Surface -> Surface/CustomToast/Default/Default
 *  - Text    -> Text/CustomToast/Default
 *  - Icon    -> Icon/CustomToast/Default (glyph types only)
 *  - Spinner -> Surface/Loaders/Spinner/Primary/Icon (loading variant only; a Loaders-family substitute)
 *
 * @param message the status/feedback text; wraps to a maximum of 2 lines.
 * @param variant status semantics selecting the leading element's content.
 * @param icon caller-supplied drawable res for the leading glyph (success/error/informative types);
 *   the atom does not hardcode which glyph. Ignored when [variant] is LOADING or when [showIcon] false.
 * @param showIcon whether the leading element renders at all (the Icon slot is optional).
 * @param duration auto-dismiss delay in ms before [onDismiss] fires; null disables auto-dismiss.
 * @param onDismiss invoked once when the auto-dismiss timeout elapses.
 */
@Composable
fun AmityToast(
    message: String,
    modifier: Modifier = Modifier,
    variant: AmityToastVariant = AmityToastVariant.INFORMATIVE,
    @DrawableRes icon: Int? = null,
    showIcon: Boolean = true,
    duration: Long? = 4000L,
    onDismiss: () -> Unit = {},
) {
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    // Auto-dismiss: re-arm whenever the duration changes; null disables it.
    LaunchedEffect(duration) {
        if (duration != null) {
            delay(duration)
            currentOnDismiss()
        }
    }

    val surface = AmityTheme.token("Surface/CustomToast/Default/Default")
    val textColor = AmityTheme.token("Text/CustomToast/Default")

    Row(
        modifier = modifier
            .width(TOAST_MAX_WIDTH)
            .defaultMinSize(minHeight = TOAST_MIN_HEIGHT)
            .background(color = surface, shape = RoundedCornerShape(TOAST_RADIUS))
            .padding(
                start = TOAST_PADDING_START,
                top = TOAST_PADDING_TOP,
                end = TOAST_PADDING_END,
                bottom = TOAST_PADDING_BOTTOM,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TOAST_GAP),
    ) {
        if (showIcon) {
            LeadingElement(variant = variant, icon = icon)
        }
        Text(
            text = message,
            color = textColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
        )
    }
}

@Composable
private fun LeadingElement(
    variant: AmityToastVariant,
    @DrawableRes icon: Int?,
) {
    Box(
        modifier = Modifier.size(TOAST_ICON_FRAME),
        contentAlignment = Alignment.Center,
    ) {
        if (variant == AmityToastVariant.LOADING) {
            // Spinner substitute — a Loaders-family token, not a CustomToast token (Icon slot rule).
            val spinnerColor = AmityTheme.token("Surface/Loaders/Spinner/Primary/Icon")
            CircularProgressIndicator(
                modifier = Modifier.size(TOAST_GLYPH),
                color = spinnerColor,
                strokeWidth = 2.dp,
            )
        } else if (icon != null) {
            val tint = AmityTheme.token("Icon/CustomToast/Default")
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = tint,
                // Status-icon drawables (success/warning/info) are 24-viewport SoT assets that already
                // carry ~4px internal padding (mark ≈ 17.5 within the 24 box). Render at the FULL 24dp
                // frame so the mark shows at its designed ~16dp; sizing to TOAST_GLYPH (16) double-padded
                // it down to ~11dp — visibly too small vs Figma Custom Toast (10855:12178). The drawn
                // spinner above has no viewport padding, so it correctly stays at TOAST_GLYPH.
                modifier = Modifier.size(TOAST_ICON_FRAME),
            )
        }
    }
}
