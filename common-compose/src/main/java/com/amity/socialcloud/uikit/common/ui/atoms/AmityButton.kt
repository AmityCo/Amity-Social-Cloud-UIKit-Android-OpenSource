package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

// Variants that legitimately have no surface fill (ghost/transparent/outlined/link/description) are
// absent from the token set and resolve to loud magenta; treat a missing surface as transparent so
// those buttons render with no background instead of a magenta box.
@Composable
private fun surfaceOrTransparent(path: String): Color {
    val c = AmityTheme.token(path)
    return if (c == Color(0xFFFF00FF)) Color.Transparent else c
}

// ---------------------------------------------------------------------------
// AmityButton — atomic Button.
//
// Single presentational entry point for the button family, implementing the
// three documented, consumer-used sub-components:
//   - MAIN   : text (+/- icon) CTA          grammar {Role}/MainButton/{Color}/{Type}/{Hierarchy}/{State}
//   - ICON   : icon-only round button       grammar {Role}/IconButton/{Type}/{Hierarchy}/{State}
//   - SQUARE : vertical icon-over-label     grammar {Role}/SquareButton/{Color}/{Hierarchy}/{State}
//
// FAB / Split / Reaction are intentionally deferred (no token grammar defined
// yet). Hover/pressed are resolved internally from the interaction source;
// only `enabled` is caller-controlled.
// ---------------------------------------------------------------------------

enum class AmityButtonVariant { MAIN, ICON, SQUARE }

enum class AmityButtonColor(internal val segment: String) {
    DEFAULT("Default"),
    DESTRUCTIVE("Destructive"),
}

enum class AmityButtonHierarchy(internal val segment: String) {
    PRIMARY("Primary"),
    SECONDARY("Secondary"),
    GENERAL("General"), // Icon Button only
}

enum class AmityButtonStyle(internal val segment: String) {
    FILLED("Filled"),
    OUTLINED("Outlined"),
    GHOST("Ghost"),
    INVERSE("Inverse"),
    LINK("Link"),
    DESCRIPTION("Description"),
    TRANSPARENT("Transparent"),
    LABEL("Label"), // Icon Button only
}

enum class AmityMainButtonSize { LG, SM }

// Per-size ramp read off the Figma IconButton masters (the Button atom spec): the inset (padding
// to the glyph) and glyph size are EXPLICIT per size, NOT a formula. Invariant: box = glyph + 2·inset.
enum class AmityIconButtonSize(internal val dp: Int, internal val padding: Int, internal val glyph: Int) {
    SIZE16(16, 2, 12),
    SIZE20(20, 2, 16),
    SIZE24(24, 4, 16),
    SIZE32(32, 4, 24),
    SIZE40(40, 8, 24),
    SIZE48(48, 12, 24),
    SIZE64(64, 16, 32),
}

@Composable
fun AmityButton(
    variant: AmityButtonVariant,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: AmityButtonColor = AmityButtonColor.DEFAULT,
    hierarchy: AmityButtonHierarchy = AmityButtonHierarchy.PRIMARY,
    style: AmityButtonStyle = AmityButtonStyle.FILLED,
    mainSize: AmityMainButtonSize = AmityMainButtonSize.LG,
    iconSize: AmityIconButtonSize = AmityIconButtonSize.SIZE40,
    label: String? = null,
    @DrawableRes icon: Int? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    when (variant) {
        AmityButtonVariant.MAIN -> MainButton(
            modifier = modifier,
            interactionSource = interactionSource,
            pressed = pressed,
            onClick = onClick,
            color = color,
            hierarchy = hierarchy,
            style = style,
            size = mainSize,
            label = label,
            icon = icon,
            enabled = enabled,
        )

        AmityButtonVariant.ICON -> IconButton(
            modifier = modifier,
            interactionSource = interactionSource,
            pressed = pressed,
            onClick = onClick,
            hierarchy = hierarchy,
            style = style,
            size = iconSize,
            icon = icon,
            contentDescription = contentDescription,
            enabled = enabled,
        )

        AmityButtonVariant.SQUARE -> SquareButton(
            modifier = modifier,
            interactionSource = interactionSource,
            pressed = pressed,
            onClick = onClick,
            color = color,
            hierarchy = hierarchy,
            label = label,
            icon = icon,
            enabled = enabled,
        )
    }
}

// ---------------------------------------------------------------------------
// State resolution — state naming is inconsistent across sub-components;
// each helper honors its own.
// ---------------------------------------------------------------------------

private fun mainState(enabled: Boolean, pressed: Boolean): String =
    if (!enabled) "Disabled" else if (pressed) "Hover" else "Enabled"

private fun squareState(enabled: Boolean, pressed: Boolean): String =
    if (!enabled) "Disabled" else if (pressed) "Hover" else "Default"

// Icon Button mixes state names per role: Surface uses Enabled/Hovered/Disabled,
// while Icon/Text use Default/Hovered/Disabled.
private fun iconState(role: String, enabled: Boolean, pressed: Boolean): String = when {
    !enabled -> "Disabled"
    pressed -> "Hovered"
    role == "Surface" -> "Enabled"
    else -> "Default"
}

// ---------------------------------------------------------------------------
// MAIN
// ---------------------------------------------------------------------------

@Composable
private fun MainButton(
    modifier: Modifier,
    interactionSource: MutableInteractionSource,
    pressed: Boolean,
    onClick: () -> Unit,
    color: AmityButtonColor,
    hierarchy: AmityButtonHierarchy,
    style: AmityButtonStyle,
    size: AmityMainButtonSize,
    label: String?,
    @DrawableRes icon: Int?,
    enabled: Boolean,
) {
    val state = mainState(enabled, pressed)

    fun path(role: String) =
        "$role/MainButton/${color.segment}/${style.segment}/${hierarchy.segment}/$state"

    val surface = surfaceOrTransparent(path("Surface"))
    val textColor = AmityTheme.token(path("Text"))
    val iconColor = AmityTheme.token(path("Icon"))
    // Border is resolved through the sentinel guard so an unmapped Border token → transparent (no-op),
    // letting us paint it for every style without risking the magenta unmapped-sentinel showing.
    val borderColor = surfaceOrTransparent(path("Border"))

    val iconOnly = label == null && icon != null
    val isLg = size == AmityMainButtonSize.LG

    val height = if (isLg) 40 else 28
    val radius = when {
        // Sm + Transparent is an exception to the Sm=r6 pattern (renders 8px).
        !isLg && style == AmityButtonStyle.TRANSPARENT -> 8
        isLg -> 8
        else -> 6
    }
    val shape: Shape = RoundedCornerShape(radius.dp)
    val gap = if (isLg) 8 else 4

    val padding: PaddingValues = when {
        iconOnly -> PaddingValues(if (isLg) 10.dp else 4.dp)
        isLg -> PaddingValues(horizontal = 12.dp, vertical = 10.dp)
        else -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    }

    var base = modifier
        .height(height.dp)
        .clip(shape)
        .background(surface, shape)
    // Every MainButton Type carries a Border token (the Semantic re-export reworked Filled
    // dark-mode borders so Filled buttons show a visible edge) — paint it for all styles, not only
    // Outlined. Where no border is defined the token resolves transparent, so this is a visual no-op.
    base = base.border(1.dp, borderColor, shape)
    base = base
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick,
        )
        .padding(padding)

    Row(
        modifier = if (iconOnly) base.size(height.dp) else base,
        horizontalArrangement = Arrangement.spacedBy(gap.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = label,
                tint = iconColor,
                // MainButton icon is a FIXED 20×20 at both Lg and Sm (the Button atom spec line 80) — it does
                // NOT scale down with the button size the way IconButton's glyph does.
                modifier = Modifier.size(20.dp),
            )
        }
        if (label != null) {
            Text(
                text = label,
                color = textColor,
                style = AmityTheme.typography.bodyBold.copy(
                    fontSize = if (isLg) 15.sp else 13.sp,
                    lineHeight = if (isLg) 20.sp else 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.4).sp,
                ),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// ICON — round icon-only button
// ---------------------------------------------------------------------------

@Composable
private fun IconButton(
    modifier: Modifier,
    interactionSource: MutableInteractionSource,
    pressed: Boolean,
    onClick: () -> Unit,
    hierarchy: AmityButtonHierarchy,
    style: AmityButtonStyle,
    size: AmityIconButtonSize,
    @DrawableRes icon: Int?,
    contentDescription: String?,
    enabled: Boolean,
) {
    fun path(role: String) =
        "$role/IconButton/${style.segment}/${hierarchy.segment}/${iconState(role, enabled, pressed)}"

    val surface = surfaceOrTransparent(path("Surface"))
    val iconColor = AmityTheme.token(path("Icon"))

    val shape: Shape = CircleShape // radius 99 (pill)

    Row(
        modifier = modifier
            .size(size.dp.dp)
            .clip(shape)
            .background(surface, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(size.padding.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = contentDescription,
                tint = iconColor,
                // Explicit per-size glyph (not size − 2·inset), and the 40 px row is no longer
                // style-split — the spec ramp pins 40 → glyph 24 / inset 8 for every style.
                modifier = Modifier.size(size.glyph.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// SQUARE — vertical icon-over-label action (swipe/toolbar), 80x82
// ---------------------------------------------------------------------------

@Composable
private fun SquareButton(
    modifier: Modifier,
    interactionSource: MutableInteractionSource,
    pressed: Boolean,
    onClick: () -> Unit,
    color: AmityButtonColor,
    hierarchy: AmityButtonHierarchy,
    label: String?,
    @DrawableRes icon: Int?,
    enabled: Boolean,
) {
    val state = squareState(enabled, pressed)

    fun path(role: String) =
        "$role/SquareButton/${color.segment}/${hierarchy.segment}/$state"

    val surface = surfaceOrTransparent(path("Surface"))
    val iconColor = AmityTheme.token(path("Icon"))
    val textColor = AmityTheme.token(path("Text"))

    Column(
        modifier = modifier
            .width(80.dp)
            .height(82.dp)
            .background(surface)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
        }
        if (label != null) {
            Text(
                text = label,
                color = textColor,
                style = AmityTheme.typography.caption.copy(fontSize = 12.sp),
            )
        }
    }
}
