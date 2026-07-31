package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Content axis. label -> Default, icon -> Icon Only.
 */
enum class AmityBadgeVariant { LABEL, ICON }

/**
 * Corner treatment. ROUND -> radius 256 (pill/circle), SQUARE -> radius 4.
 */
enum class AmityBadgeShape { ROUND, SQUARE }

/**
 * Surface treatment.
 */
enum class AmityBadgeFill { FILLED, GHOST }

/**
 * The six intrinsic badge heights. [dp] is the frame height in px.
 */
enum class AmityBadgeSize(val dp: Int) {
    SIZE_14(14),
    SIZE_16(16),
    SIZE_20(20),
    SIZE_24(24),
    SIZE_28(28),
    SIZE_32(32),
}

/**
 * Semantic family. Segment strings match the token literals.
 */
enum class AmityBadgeFamily(val segment: String) {
    GENERAL("General"),
    POST_STATUS("PostStatus"),
    USER_STATUS("UserStatus"),
    CHAT("Chat"),
    LIVE("Live"),
    EVENT("Event"),
    COMMUNITY("Community"),
}

/**
 * Semantic preset: a family + a per-family case (e.g. General/Notification, UserStatus/Moderator).
 * When set, the badge resolves its Surface/Text/Icon tokens from the SemanticBadge tier.
 */
data class AmityBadgePreset(
    val family: AmityBadgeFamily,
    val case: String,
)

// Per-size horizontal (left/right) inset for label variants; icon-only uses 0. The ramp is sampled
// per shape AND fill — Round only has a measured Filled ramp (Ghost isn't confirmed for Round, so it
// falls back to the Round-Filled numbers); Square has distinct Filled and Ghost ramps. Square-Ghost
// has no sampled 14/16 instance, so those sizes reuse its smallest confirmed value (20's).
private fun horizontalPaddingDp(size: AmityBadgeSize, shape: AmityBadgeShape, fill: AmityBadgeFill): Int {
    if (shape == AmityBadgeShape.SQUARE && fill == AmityBadgeFill.GHOST) {
        return when (size) {
            AmityBadgeSize.SIZE_14, AmityBadgeSize.SIZE_16, AmityBadgeSize.SIZE_20 -> 2
            AmityBadgeSize.SIZE_24, AmityBadgeSize.SIZE_28 -> 2
            AmityBadgeSize.SIZE_32 -> 4
        }
    }
    if (shape == AmityBadgeShape.SQUARE) {
        return when (size) {
            AmityBadgeSize.SIZE_14, AmityBadgeSize.SIZE_16 -> 2
            AmityBadgeSize.SIZE_20 -> 6
            AmityBadgeSize.SIZE_24, AmityBadgeSize.SIZE_28 -> 8
            AmityBadgeSize.SIZE_32 -> 12
        }
    }
    // Round-Filled (and Round-Ghost fallback).
    return when (size) {
        AmityBadgeSize.SIZE_14, AmityBadgeSize.SIZE_16 -> 4
        AmityBadgeSize.SIZE_20 -> 6
        AmityBadgeSize.SIZE_24, AmityBadgeSize.SIZE_28 -> 8
        AmityBadgeSize.SIZE_32 -> 12
    }
}

private fun cornerRadiusDp(shape: AmityBadgeShape): Dp = when (shape) {
    AmityBadgeShape.ROUND -> 256.dp
    AmityBadgeShape.SQUARE -> 4.dp
}

// SF Pro Regular label ramp: two size/line-height tiers across the six badge heights.
private fun labelFontSizeSp(size: AmityBadgeSize): Int = when (size) {
    AmityBadgeSize.SIZE_14, AmityBadgeSize.SIZE_16, AmityBadgeSize.SIZE_20 -> 10
    AmityBadgeSize.SIZE_24, AmityBadgeSize.SIZE_28, AmityBadgeSize.SIZE_32 -> 13
}

private fun labelLineHeightSp(size: AmityBadgeSize): Int = when (size) {
    AmityBadgeSize.SIZE_14, AmityBadgeSize.SIZE_16, AmityBadgeSize.SIZE_20 -> 13
    AmityBadgeSize.SIZE_24, AmityBadgeSize.SIZE_28, AmityBadgeSize.SIZE_32 -> 18
}

// Icon-only glyph size: a sampled ramp per size AND shape (Round/Square diverge at 20px).
private fun iconGlyphDp(size: AmityBadgeSize, shape: AmityBadgeShape): Dp = when (shape) {
    AmityBadgeShape.ROUND -> when (size) {
        AmityBadgeSize.SIZE_14 -> 10
        AmityBadgeSize.SIZE_16 -> 12
        AmityBadgeSize.SIZE_20 -> 12
        AmityBadgeSize.SIZE_24 -> 16
        AmityBadgeSize.SIZE_28 -> 16
        AmityBadgeSize.SIZE_32 -> 24
    }
    AmityBadgeShape.SQUARE -> when (size) {
        AmityBadgeSize.SIZE_14 -> 12
        AmityBadgeSize.SIZE_16 -> 12
        AmityBadgeSize.SIZE_20 -> 16
        AmityBadgeSize.SIZE_24 -> 16
        AmityBadgeSize.SIZE_28 -> 16
        AmityBadgeSize.SIZE_32 -> 24
    }
}.dp

// Leading/trailing icon glyph size for label-mode badges (flush against the label, 0px gap).
// Matches the icon-only ramp except Round-14, which is measured larger (12) in label mode.
private fun labelIconGlyphDp(size: AmityBadgeSize, shape: AmityBadgeShape): Dp = when (shape) {
    AmityBadgeShape.ROUND -> when (size) {
        AmityBadgeSize.SIZE_14 -> 12
        AmityBadgeSize.SIZE_16 -> 12
        AmityBadgeSize.SIZE_20 -> 12
        AmityBadgeSize.SIZE_24 -> 16
        AmityBadgeSize.SIZE_28 -> 16
        AmityBadgeSize.SIZE_32 -> 24
    }
    AmityBadgeShape.SQUARE -> when (size) {
        AmityBadgeSize.SIZE_14 -> 12
        AmityBadgeSize.SIZE_16 -> 12
        AmityBadgeSize.SIZE_20 -> 16
        AmityBadgeSize.SIZE_24 -> 16
        AmityBadgeSize.SIZE_28 -> 16
        AmityBadgeSize.SIZE_32 -> 24
    }
}.dp

/**
 * Surface (fill) token for the current configuration.
 */
private fun surfacePath(preset: AmityBadgePreset?, fill: AmityBadgeFill): String =
    if (preset != null) {
        "Surface/Badge/SemanticBadge/${preset.family.segment}/${preset.case}"
    } else {
        val tier = if (fill == AmityBadgeFill.FILLED) "Filled" else "Ghost"
        "Surface/Badge/AtomicBadge/$tier/Default"
    }

/**
 * Label token for the current configuration. The General family collapses its text case to
 * "Default" (Text/Badge/SemanticBadge/General/Default/Default).
 */
private fun textPath(preset: AmityBadgePreset?): String =
    if (preset != null) {
        val case = if (preset.family == AmityBadgeFamily.GENERAL) "Default" else preset.case
        "Text/Badge/SemanticBadge/${preset.family.segment}/$case/Default"
    } else {
        "Text/Badge/AtomicBadge/Default"
    }

/**
 * Glyph token for the current configuration.
 */
private fun iconPath(preset: AmityBadgePreset?): String =
    if (preset != null) {
        "Icon/Badge/SemanticBadge/${preset.family.segment}/${preset.case}/Default"
    } else {
        "Icon/Badge/AtomicBadge/Default"
    }

/**
 * AmityBadge — the atomic count/status chip. Renders a text [label] or an [icon] glyph inside a
 * filled or ghost, round or square surface across six sizes. Presentational only (State=Default).
 *
 * When [preset] is supplied it resolves the Surface/Text/Icon tokens from the SemanticBadge tier
 * (e.g. General/Notification unread pill, UserStatus/Moderator, Chat/Mention). Otherwise it falls
 * back to the AtomicBadge base tokens.
 *
 * @param variant whether the badge renders a label or an icon glyph.
 * @param label label text when [variant] is LABEL; ignored otherwise.
 * @param icon caller-supplied drawable res for the glyph when [variant] is ICON; ignored otherwise.
 * @param leadingIcon optional drawable res for a glyph flush before the label when [variant] is
 * LABEL; ignored otherwise.
 * @param trailingIcon optional drawable res for a glyph flush after the label when [variant] is
 * LABEL; ignored otherwise.
 * @param shape ROUND (pill/circle) or SQUARE (radius 4).
 * @param fill FILLED or GHOST surface.
 * @param size one of the six intrinsic heights.
 * @param border adds the Border/Badge ring when true.
 * @param preset optional semantic family + case preset.
 */
@Composable
fun AmityBadge(
    variant: AmityBadgeVariant,
    modifier: Modifier = Modifier,
    label: String? = null,
    @DrawableRes icon: Int? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    shape: AmityBadgeShape = AmityBadgeShape.ROUND,
    fill: AmityBadgeFill = AmityBadgeFill.FILLED,
    size: AmityBadgeSize = AmityBadgeSize.SIZE_24,
    border: Boolean = false,
    preset: AmityBadgePreset? = null,
) {
    val cornerShape = RoundedCornerShape(cornerRadiusDp(shape))
    val surface = AmityTheme.token(surfacePath(preset, fill))

    val isIcon = variant == AmityBadgeVariant.ICON
    val horizontalPad = if (isIcon) 0 else horizontalPaddingDp(size, shape, fill)

    var boxModifier = modifier
        .height(size.dp.dp)
        .defaultMinSize(minWidth = size.dp.dp)

    // Ghost surfaces paint no fill, but the token is still bound here for consistency.
    boxModifier = boxModifier.background(color = surface, shape = cornerShape)

    if (border) {
        val borderColor = AmityTheme.token("Border/Badge/AtomicBadge/Default")
        boxModifier = boxModifier.border(width = 1.dp, color = borderColor, shape = cornerShape)
    }

    boxModifier = boxModifier.padding(horizontal = horizontalPad.dp)

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        if (isIcon) {
            if (icon != null) {
                val tint = AmityTheme.token(iconPath(preset))
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(iconGlyphDp(size, shape)),
                )
            }
        } else {
            val textColor = AmityTheme.token(textPath(preset))
            val glyphTint = if (leadingIcon != null || trailingIcon != null) {
                AmityTheme.token(iconPath(preset))
            } else {
                null
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = leadingIcon),
                        contentDescription = null,
                        tint = requireNotNull(glyphTint),
                        modifier = Modifier.size(labelIconGlyphDp(size, shape)),
                    )
                }
                Text(
                    text = label.orEmpty(),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = AmityTheme.typography.caption.copy(
                        fontSize = labelFontSizeSp(size).sp,
                        lineHeight = labelLineHeightSp(size).sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )
                if (trailingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = trailingIcon),
                        contentDescription = null,
                        tint = requireNotNull(glyphTint),
                        modifier = Modifier.size(labelIconGlyphDp(size, shape)),
                    )
                }
            }
        }
    }
}
