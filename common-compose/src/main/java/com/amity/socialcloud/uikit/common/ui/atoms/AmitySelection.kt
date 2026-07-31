package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.Canvas
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Which Selection control to render.
 * RADIO -> RadioAtomic (center-dot glyph, single-select group semantics).
 * CHECKBOX -> CheckboxAtomic (check-mark glyph, independent multi-select).
 * Both share the 24x24 frame / 20x20 circle geometry and differ only by glyph.
 */
enum class AmitySelectionVariant(val segment: String) {
    RADIO("RadioAtomic"),
    CHECKBOX("CheckboxAtomic"),
}

// Intrinsic geometry constants.
private val FrameSize = 24.dp   // hit-area frame
private val CircleSize = 20.dp  // 24 - 2*2 padding inset
private val RadioDotSize = 8.dp // 8x8 center dot
private val CheckGlyphSize = 14.dp // ~14x14 check mark

private fun state(isDisabled: Boolean): String = if (isDisabled) "Disabled" else "Default"

/**
 * AmitySelection — the atomic Radio / Checkbox control.
 *
 * Presentational: the Active/Inactive state is fully controlled by the consumer via [isSelected];
 * this atom never owns radio-group exclusivity or checkbox multi-select — the parent list/group
 * does (see [value] / [onChange]).
 *
 * Token grammar bound (all from the semantic Selection token family):
 *  - Surface: Surface/Selection/{RadioAtomic|CheckboxAtomic}/{Active|Inactive}/{Default|Disabled}
 *  - Border : Border/Selection/{RadioAtomic|CheckboxAtomic}/Inactive/{Default|Disabled}  (Inactive only)
 *  - Icon   : Icon/Selection/{RadioAtomic|CheckboxAtomic}/{Default|Disabled}  (shared Active + Inactive)
 *
 * The glyph layer exists in both Active and Inactive renders and is painted at alpha 0 when Inactive.
 *
 * @param variant RADIO or CHECKBOX.
 * @param isSelected Active (true) / Inactive (false).
 * @param modifier layout modifier applied to the 24x24 frame.
 * @param isDisabled Default (false) / Disabled (true) — swaps every part to its Disabled token.
 * @param value optional identifier for a radio group (passed back through [onChange]); not owned here.
 * @param icon optional caller-supplied glyph for the CHECKBOX check mark. When null a built-in check
 *             is drawn. Ignored for RADIO (its center dot is a filled disc, drawn intrinsically).
 * @param onChange invoked with the toggled selection + [value] when an enabled control is tapped.
 */
@Composable
fun AmitySelection(
    variant: AmitySelectionVariant,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isDisabled: Boolean = false,
    value: String? = null,
    @DrawableRes icon: Int? = null,
    onChange: ((isSelected: Boolean, value: String?) -> Unit)? = null,
) {
    val control = variant.segment
    val active = if (isSelected) "Active" else "Inactive"
    val stateSeg = state(isDisabled)

    val surface = AmityTheme.token("Surface/Selection/$control/$active/$stateSeg")
    val iconTint = AmityTheme.token("Icon/Selection/$control/$stateSeg")

    val interaction = remember { MutableInteractionSource() }

    var frame = modifier.size(FrameSize)
    if (!isDisabled && onChange != null) {
        frame = frame.clickable(
            interactionSource = interaction,
            indication = null,
        ) { onChange(!isSelected, value) }
    }

    Box(modifier = frame, contentAlignment = Alignment.Center) {
        // 20x20 circle: Active fills the disc via Surface; Inactive is Surface + Border ring.
        var circle = Modifier
            .size(CircleSize)
            .clip(CircleShape)
            .background(color = surface, shape = CircleShape)
        if (!isSelected) {
            val ring = AmityTheme.token("Border/Selection/$control/Inactive/$stateSeg")
            circle = circle.border(width = 2.dp, color = ring, shape = CircleShape)
        }

        Box(modifier = circle, contentAlignment = Alignment.Center) {
            // Glyph present in both states; alpha 0 when Inactive.
            val glyphAlpha = if (isSelected) 1f else 0f
            when (variant) {
                AmitySelectionVariant.RADIO -> {
                    Box(
                        modifier = Modifier
                            .size(RadioDotSize)
                            .alpha(glyphAlpha)
                            .clip(CircleShape)
                            .background(color = iconTint, shape = CircleShape),
                    )
                }

                AmitySelectionVariant.CHECKBOX -> {
                    if (icon != null) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icon),
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier
                                .size(CheckGlyphSize)
                                .alpha(glyphAlpha),
                        )
                    } else {
                        Canvas(
                            modifier = Modifier
                                .size(CheckGlyphSize)
                                .alpha(glyphAlpha)
                                .padding(1.dp),
                        ) {
                            val w = size.width
                            val h = size.height
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(w * 0.18f, h * 0.52f)
                                lineTo(w * 0.42f, h * 0.74f)
                                lineTo(w * 0.84f, h * 0.28f)
                            }
                            drawPath(
                                path = path,
                                color = iconTint,
                                style = Stroke(
                                    width = w * 0.14f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
