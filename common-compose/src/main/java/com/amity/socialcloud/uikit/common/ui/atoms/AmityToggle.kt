package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Internal interaction-state axis. Not consumer-facing:
 * Enabled/Hovered/Pressed/Focused/Disabled are all resolved inside the atom from the track's
 * [MutableInteractionSource] (pointer hover, touch press, keyboard/a11y focus) + isDisabled — the
 * clickable modifier below reports Hover and Focus through the same interaction source as Press.
 *
 * The segment string is the exact semantic token State segment.
 */
private enum class ToggleInteractionState(val segment: String) {
    ENABLED("Enabled"),
    HOVERED("Hovered"),
    PRESSED("Pressed"),
    FOCUSED("Focused"),
    DISABLED("Disabled"),
}

// Intrinsic geometry (Android 52x32 pill track).
private val TrackWidth = 52.dp
private val TrackHeight = 32.dp
private val TrackRadius = 16.dp // r100 pill = H/2

// No-icon thumb: the diameter itself is a function of the on/off value, not a single fixed size.
// Pressed overrides both values to one shared, larger diameter.
private val ThumbSizeOff = 16.dp
private val ThumbSizeOn = 24.dp
private val ThumbSizePressed = 28.dp

// Icon-variant thumb: diameter is constant across on/off (unlike the no-icon thumb above).
private val IconThumbSize = 24.dp

// Icon-in-thumb glyph pixel size is not yet confirmed against a measured source; kept at the
// previously chosen value pending that confirmation.
private val IconGlyphSize = 16.dp

// Resting (Enabled/Hovered/Focused/Disabled) horizontal thumb offset measured from the track's
// left edge.
private val ThumbOffsetOff = 8.dp
private val ThumbOffsetOn = 24.dp
private val IconThumbOffsetOff = 4.dp
private val IconThumbOffsetOn = 24.dp

// Pressed collapses the on/off resting-offset asymmetry to a single shared position.
private val ThumbOffsetPressed = 22.dp

/**
 * AmityToggle — the atomic on/off switch.
 *
 * Presentational + controlled: the persisted on/off value is fully owned by the consumer via
 * [isOn]; this atom never mutates it. Tapping an enabled toggle invokes [onChange] with the
 * flipped value and the consumer re-renders. Hovered/Pressed/Focused are all resolved internally
 * from the track's [MutableInteractionSource].
 *
 * Token grammar bound (Toggle token family):
 *  - Track surface : Surface/Toggle/Background/{Active|Inactive}/{State}
 *  - Track border  : Border/Toggle/Background/{Active|Inactive}/{State}
 *  - Thumb surface : Surface/Toggle/Thumb/{Active|Inactive}/{State}
 *  - Thumb border  : Border/Toggle/Thumb/{Active|Inactive}/{State}  (interaction-only: Hovered/Pressed/Focused)
 *  - Icon glyph    : Icon/Toggle/{Active|Inactive}/General          (fixed per on/off value, no state variance)
 *
 * State axis maps to token State segment: Enabled (rest), Hovered (pointer), Pressed (touch),
 * Focused (keyboard/a11y), Disabled (isDisabled). There is no Enabled/Disabled Thumb-border token,
 * so the Thumb renders borderless outside the Hovered/Pressed/Focused interaction states.
 *
 * The no-icon thumb's diameter AND resting horizontal position both depend on the current on/off
 * value ([ThumbSizeOff]/[ThumbSizeOn], [ThumbOffsetOff]/[ThumbOffsetOn]); Pressed overrides both to
 * one shared diameter/position regardless of value. The icon variant keeps a constant thumb
 * diameter ([IconThumbSize]) but still slides between [IconThumbOffsetOff] and [IconThumbOffsetOn].
 *
 * @param isOn persisted value. true binds the Active (on) slice, false the Inactive (off) slice.
 * @param modifier layout modifier applied to the 52x32 track.
 * @param isDisabled Enabled-family (false) / Disabled (true) — swaps track + thumb surfaces/borders
 *        to their Disabled token variant for the current [isOn] value.
 * @param icon optional caller-supplied glyph rendered inside the thumb (Android icon variant:
 *        smaller constant-diameter thumb in the same 52x32 track), tinted by Icon/Toggle/{on-off}/General.
 * @param onChange invoked with the flipped value when an enabled toggle is tapped.
 */
@Composable
fun AmityToggle(
    isOn: Boolean,
    modifier: Modifier = Modifier,
    isDisabled: Boolean = false,
    @DrawableRes icon: Int? = null,
    onChange: ((isOn: Boolean) -> Unit)? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val isFocused by interaction.collectIsFocusedAsState()
    val isHovered by interaction.collectIsHoveredAsState()

    val interactionState = when {
        isDisabled -> ToggleInteractionState.DISABLED
        isPressed -> ToggleInteractionState.PRESSED
        isFocused -> ToggleInteractionState.FOCUSED
        isHovered -> ToggleInteractionState.HOVERED
        else -> ToggleInteractionState.ENABLED
    }
    val state = interactionState.segment

    val onOff = if (isOn) "Active" else "Inactive"

    val trackSurface = AmityTheme.token("Surface/Toggle/Background/$onOff/$state")
    val trackBorder = AmityTheme.token("Border/Toggle/Background/$onOff/$state")
    val thumbSurface = AmityTheme.token("Surface/Toggle/Thumb/$onOff/$state")
    val iconTint = AmityTheme.token("Icon/Toggle/$onOff/General")

    // Thumb border is interaction-only (Hovered/Pressed/Focused); no Enabled/Disabled token exists.
    val hasThumbBorder = interactionState == ToggleInteractionState.HOVERED ||
        interactionState == ToggleInteractionState.PRESSED ||
        interactionState == ToggleInteractionState.FOCUSED
    val thumbBorder = if (hasThumbBorder) {
        AmityTheme.token("Border/Toggle/Thumb/$onOff/$state")
    } else null

    val isPressedState = interactionState == ToggleInteractionState.PRESSED
    val targetThumbSize = when {
        icon != null -> IconThumbSize
        isPressedState -> ThumbSizePressed
        isOn -> ThumbSizeOn
        else -> ThumbSizeOff
    }
    val targetThumbOffsetX = when {
        icon != null -> if (isOn) IconThumbOffsetOn else IconThumbOffsetOff
        isPressedState -> ThumbOffsetPressed
        isOn -> ThumbOffsetOn
        else -> ThumbOffsetOff
    }
    val thumbSize by animateDpAsState(targetValue = targetThumbSize, label = "amity_toggle_thumb_size")
    val thumbOffsetX by animateDpAsState(targetValue = targetThumbOffsetX, label = "amity_toggle_thumb_offset")

    var track = modifier
        .size(width = TrackWidth, height = TrackHeight)
        .clip(RoundedCornerShape(TrackRadius))
        .background(color = trackSurface, shape = RoundedCornerShape(TrackRadius))
        .border(width = 1.dp, color = trackBorder, shape = RoundedCornerShape(TrackRadius))
    if (!isDisabled && onChange != null) {
        track = track.clickable(
            interactionSource = interaction,
            indication = null,
        ) { onChange(!isOn) }
    }

    Box(modifier = track, contentAlignment = Alignment.CenterStart) {
        var thumb = Modifier
            .offset(x = thumbOffsetX)
            .size(thumbSize)
            .clip(CircleShape)
            .background(color = thumbSurface, shape = CircleShape)
        if (thumbBorder != null) {
            thumb = thumb.border(width = 1.dp, color = thumbBorder, shape = CircleShape)
        }

        Box(modifier = thumb, contentAlignment = Alignment.Center) {
            if (icon != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(IconGlyphSize),
                )
            }
        }
    }
}
