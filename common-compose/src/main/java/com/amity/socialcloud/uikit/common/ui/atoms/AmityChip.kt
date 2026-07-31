package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Atomic Chip.
 *
 * A composite atom: a leading [AmityAvatar] slot (+ optional [AmityBadge] status indicator on
 * its bottom-right corner), a text label, and a trailing [AmityButton] (`ICON` variant) slot,
 * all inside a filled-or-outlined pill surface. `AmityChip` itself only owns the pill's
 * Surface/Border/Text tokens — the leading/trailing content resolves its own token families via
 * the nested atoms. There is no `Icon/Chips` wildcard token.
 *
 * Every owned part binds a semantic color token via [AmityTheme.token] — no hardcoded colors.
 *
 * @param variant FILLED or OUTLINED — the pill's surface treatment.
 * @param label the chip's text content.
 * @param size MEDIUM (36 px) or SMALL (20 px); drives the leading avatar size and label weight.
 * @param disabled resolves the Disabled token row across Surface/Border/Text.
 * @param showLeading shows/hides the leading Avatar slot.
 * @param showTrailing shows/hides the trailing IconButton slot.
 * @param avatar content for the leading slot when [showLeading] is true; ignored otherwise.
 * @param indicator optional status badge preset rendered on the leading avatar's bottom-right
 *                  corner via [AmityBadge]; ignored when [showLeading] is false or [avatar] is null.
 * @param trailingIcon drawable res for the trailing IconButton glyph; defaults to `plus-r`
 *                      (`R.drawable.amity_ic_plus_r`).
 * @param onPress called when the chip body (excluding the trailing IconButton) is tapped. Not
 *                called while [disabled].
 * @param onTrailingPress called when the trailing IconButton is tapped; only fires when
 *                        [showTrailing] is true and not [disabled].
 */

/** The surface treatment. */
enum class AmityChipVariant { FILLED, OUTLINED }

/** The two intrinsic chip sizes. */
enum class AmityChipSize { MEDIUM, SMALL }

/**
 * Leading-avatar content for [AmityChip] — mirrors [AmityAvatar]'s own content params so the
 * chip can forward them verbatim.
 *
 * @param borderWidth forwarded to the nested [AmityAvatar]'s own `borderWidth` param; default `2`
 *                     renders the `Avatar/Border` ring (Avatar's own `borderWidth` defaults to
 *                     `0` — off — so without this field the ring would be unreachable from the
 *                     Chip).
 */
data class AmityChipAvatarSource(
    val variant: AmityAvatarVariant,
    val imageUrl: String? = null,
    val initials: String? = null,
    @DrawableRes val icon: Int? = null,
    val style: AmityAvatarStyle = AmityAvatarStyle.Rounded,
    val borderWidth: Int = 2,
)

@Composable
fun AmityChip(
    variant: AmityChipVariant,
    label: String,
    modifier: Modifier = Modifier,
    size: AmityChipSize = AmityChipSize.MEDIUM,
    disabled: Boolean = false,
    showLeading: Boolean = true,
    showTrailing: Boolean = true,
    avatar: AmityChipAvatarSource? = null,
    indicator: AmityBadgePreset? = null,
    @DrawableRes trailingIcon: Int = R.drawable.amity_ic_plus_r,
    onPress: (() -> Unit)? = null,
    onTrailingPress: (() -> Unit)? = null,
) {
    // Surface/Text share a Default/Disabled state naming; Border uses Enabled/Disabled for
    // the same resting row — bound as exported, not normalized.
    val surfaceToken = when (variant) {
        AmityChipVariant.FILLED ->
            if (disabled) AmityColorToken.SurfaceChipsFilledDisabled else AmityColorToken.SurfaceChipsFilledDefault
        AmityChipVariant.OUTLINED ->
            if (disabled) AmityColorToken.SurfaceChipsOutlinedDisabled else AmityColorToken.SurfaceChipsOutlinedDefault
    }
    val borderToken = when (variant) {
        AmityChipVariant.FILLED ->
            if (disabled) AmityColorToken.BorderChipsFilledDisabled else AmityColorToken.BorderChipsFilledEnabled
        AmityChipVariant.OUTLINED ->
            if (disabled) AmityColorToken.BorderChipsOutlinedDisabled else AmityColorToken.BorderChipsOutlinedEnabled
    }
    val textToken = when (variant) {
        AmityChipVariant.FILLED ->
            if (disabled) AmityColorToken.TextChipsFilledDisabled else AmityColorToken.TextChipsFilledDefault
        AmityChipVariant.OUTLINED ->
            if (disabled) AmityColorToken.TextChipsOutlinedDisabled else AmityColorToken.TextChipsOutlinedDefault
    }

    val surface = AmityTheme.token(surfaceToken)
    // Filled ships this token but paints no visible stroke; it is still bound here for
    // consistency, not forced to a visible ring.
    val border = AmityTheme.token(borderToken)
    val textColor = AmityTheme.token(textToken)

    val isMedium = size == AmityChipSize.MEDIUM
    val shape = RoundedCornerShape(24.dp)

    val avatarSize = if (isMedium) AmityAvatarSize.Size28 else AmityAvatarSize.Size16
    val labelStyle = if (isMedium) {
        AmityTheme.typography.bodyLegacy.copy(fontWeight = FontWeight.SemiBold)
    } else {
        AmityTheme.typography.captionLegacy.copy(fontWeight = FontWeight.Normal)
    }
    val containerPadding = if (isMedium) {
        PaddingValues(4.dp)
    } else {
        PaddingValues(vertical = 0.dp, horizontal = 2.dp)
    }
    val labelInset = if (isMedium) 8.dp else 4.dp

    var chipModifier = modifier
        .clip(shape)
        .background(color = surface, shape = shape)
    // Filled paints no stroke, so only Outlined paints the border; `border` above is still
    // computed for documentation/consistency.
    if (variant == AmityChipVariant.OUTLINED) {
        chipModifier = chipModifier.border(width = 1.dp, color = border, shape = shape)
    }
    if (onPress != null) {
        chipModifier = chipModifier.clickable(enabled = !disabled) { onPress() }
    }
    chipModifier = chipModifier.padding(containerPadding)

    Row(
        modifier = chipModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        if (showLeading && avatar != null) {
            AmityAvatar(
                variant = avatar.variant,
                imageUrl = avatar.imageUrl,
                initials = avatar.initials,
                icon = avatar.icon,
                style = avatar.style,
                size = avatarSize,
                borderWidth = avatar.borderWidth,
                indicator = indicator?.let { preset ->
                    {
                        AmityBadge(
                            variant = AmityBadgeVariant.ICON,
                            shape = AmityBadgeShape.ROUND,
                            size = AmityBadgeSize.SIZE_14,
                            preset = preset,
                        )
                    }
                },
            )
        }

        Text(
            text = label,
            color = textColor,
            style = labelStyle,
            modifier = Modifier.padding(horizontal = labelInset),
        )

        if (showTrailing) {
            // Ghost/Secondary uses the trailing-icon token (Icon/IconButton/Ghost/Secondary/*);
            // its Surface/IconButton/Ghost/Secondary/Enabled row doesn't exist in the token set
            // (Ghost surfaces only ship a /Hover row), so AmityButton's own transparent fallback
            // renders "no painted surface at rest."
            AmityButton(
                variant = AmityButtonVariant.ICON,
                style = AmityButtonStyle.GHOST,
                hierarchy = AmityButtonHierarchy.SECONDARY,
                iconSize = AmityIconButtonSize.SIZE20,
                icon = trailingIcon,
                enabled = !disabled,
                onClick = { onTrailingPress?.invoke() },
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
private fun AmityChipFilledMediumPreview() {
    AmityChip(
        variant = AmityChipVariant.FILLED,
        label = "Jirawadee",
        size = AmityChipSize.MEDIUM,
        avatar = AmityChipAvatarSource(variant = AmityAvatarVariant.Text, initials = "JW"),
    )
}

@Preview(showBackground = true)
@Composable
private fun AmityChipOutlinedMediumPreview() {
    AmityChip(
        variant = AmityChipVariant.OUTLINED,
        label = "Jirawadee",
        size = AmityChipSize.MEDIUM,
        avatar = AmityChipAvatarSource(variant = AmityAvatarVariant.Text, initials = "JW"),
        indicator = AmityBadgePreset(family = AmityBadgeFamily.USER_STATUS, case = "Moderator"),
    )
}

@Preview(showBackground = true)
@Composable
private fun AmityChipFilledSmallPreview() {
    AmityChip(
        variant = AmityChipVariant.FILLED,
        label = "Jirawadee",
        size = AmityChipSize.SMALL,
        avatar = AmityChipAvatarSource(variant = AmityAvatarVariant.Text, initials = "JW"),
    )
}

@Preview(showBackground = true)
@Composable
private fun AmityChipOutlinedSmallPreview() {
    AmityChip(
        variant = AmityChipVariant.OUTLINED,
        label = "Jirawadee",
        size = AmityChipSize.SMALL,
        avatar = AmityChipAvatarSource(variant = AmityAvatarVariant.Text, initials = "JW"),
    )
}

@Preview(showBackground = true)
@Composable
private fun AmityChipDisabledPreview() {
    AmityChip(
        variant = AmityChipVariant.FILLED,
        label = "Jirawadee",
        size = AmityChipSize.MEDIUM,
        disabled = true,
        avatar = AmityChipAvatarSource(variant = AmityAvatarVariant.Text, initials = "JW"),
    )
}
