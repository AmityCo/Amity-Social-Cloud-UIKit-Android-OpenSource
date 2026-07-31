package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Which divider variant to render. Governs the Line colour-token binding and the default geometry
 * (inset vs edge-to-edge).
 *
 * - Content: 1px hairline, 16dp inset from both edges. Binds Line-Divider-Content-Default.
 * - Post: 1px edge-to-edge hairline (also the default). Binds Line-Divider-Post-Default. An 8dp
 *   "thicker block" render of this same token exists, but no separate selector prop exists yet,
 *   so only the 1px hairline is implemented here.
 *
 * An Alphabet variant has no token binding in the current token model and is not implemented.
 */
enum class AmityDividerVariant {
    Content,
    Post,
}

/**
 * Line axis. Only Horizontal is implemented here; Vertical is not supported.
 */
enum class AmityDividerOrientation {
    Horizontal,
}

/**
 * AmityDivider — the atomic dividing line (Content vs Post) with an optional inline caption.
 *
 * Presentational only: no hover/press/focus state, no callbacks. Colour comes entirely from the
 * token engine via [AmityTheme.token].
 *
 * @param variant which divider variant to render (governs colour token + default inset).
 * @param modifier layout modifier applied to the divider container.
 * @param orientation line axis. Only [AmityDividerOrientation.Horizontal] is supported.
 * @param inset whether to apply the 16dp horizontal inset. Defaults to true for Content,
 *   false for Post (edge-to-edge). Pass an explicit value to override the per-variant default.
 * @param label optional caption breaking the line (e.g. "AND"); composes with the line rather than
 *   replacing it. Binds Text-Divider-Default.
 */
@Composable
fun AmityDivider(
    variant: AmityDividerVariant,
    modifier: Modifier = Modifier,
    orientation: AmityDividerOrientation = AmityDividerOrientation.Horizontal,
    inset: Boolean = variant == AmityDividerVariant.Content,
    label: String? = null,
) {
    val lineColor = when (variant) {
        AmityDividerVariant.Content -> AmityTheme.token("Line/Divider/Content/Default")
        AmityDividerVariant.Post -> AmityTheme.token("Line/Divider/Post/Default")
    }

    val insetDp = if (inset) 16.dp else 0.dp
    val thickness = 1.dp

    val insetModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = insetDp)

    if (label == null) {
        // Plain hairline: a 1px full-width (optionally inset) line.
        Box(
            modifier = insetModifier
                .height(thickness)
                .drawBehind {
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, size.height / 2f),
                        end = Offset(size.width, size.height / 2f),
                        strokeWidth = size.height,
                    )
                }
        )
    } else {
        // Labelled divider: line — caption — line, caption bound to Text/Divider/Default.
        val labelColor = AmityTheme.token("Text/Divider/Default")
        Row(
            modifier = insetModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DividerHairline(color = lineColor, modifier = Modifier.weight(1f), thickness = thickness)
            Text(
                text = label,
                style = AmityTheme.typography.caption,
                color = labelColor,
            )
            DividerHairline(color = lineColor, modifier = Modifier.weight(1f), thickness = thickness)
        }
    }
}

@Composable
private fun DividerHairline(
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    thickness: androidx.compose.ui.unit.Dp = 1.dp,
) {
    Box(
        modifier = modifier
            .height(thickness)
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = size.height,
                )
            }
    )
}
