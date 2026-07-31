package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Atomic Tab item — one selectable segment of a tab bar.
 *
 * Three types (see [AmityTabVariant]): Pill (filled + 1px border + label, r24), Underlined
 * (an optional 20x20 leading/trailing glyph flanking the label, over a 2px bottom indicator
 * that paints only when selected), and Icon (glyph over the same 2px indicator). Hover and
 * Press are transient pointer states; Press is tracked internally, Hover is n/a on touch.
 * The Active look is driven by [selected] (owned by the parent bar), not by internal state.
 *
 * Token grammar resolved via the string engine:
 *   Pill:       Surface|Border|Text /Tab/Pill/{State}
 *   Underlined: Text/Tab/Underlined/{State}, Line/Tab/Underlined/Active, Icon/Tab/{State} (leading/trailing slot)
 *   Icon:       Icon/Tab/{State}, Line/Tab/Icon/Active
 *   Skeleton:   Surface/SkeletonEffect/Default (Pill only)
 * State segment is one of Default, Hover, Press, Active, Disabled, Skeleton.
 */
enum class AmityTabVariant { Pill, Underlined, Icon }

private const val TAB_LABEL_SIZE_SP = 17
private const val TAB_LABEL_LINE_SP = 24
private val TAB_INDICATOR_HEIGHT = 2.dp
private val TAB_PILL_RADIUS = 24.dp
private val TAB_PILL_HEIGHT = 40.dp
private val TAB_STACK_HEIGHT = 56.dp
private val TAB_UNDERLINED_ICON_SIZE = 20.dp
private val TAB_UNDERLINED_ICON_GAP = 8.dp

@Composable
fun AmityTab(
    variant: AmityTabVariant,
    modifier: Modifier = Modifier,
    label: String? = null,
    @DrawableRes icon: Int? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    selected: Boolean = false,
    disabled: Boolean = false,
    loading: Boolean = false,
    onPress: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    // Resolve the token State segment. Priority: disabled > skeleton(pill) > active > press > default.
    val isSkeleton = loading && variant == AmityTabVariant.Pill
    val state: String = when {
        disabled -> "Disabled"
        isSkeleton -> "Skeleton"
        selected -> "Active"
        pressed -> "Press"
        else -> "Default"
    }

    val clickable = onPress != null && !disabled && !loading
    val clickModifier =
        if (clickable) {
            Modifier.clickableNoRipple(interactionSource) { onPress?.invoke() }
        } else Modifier

    when (variant) {
        AmityTabVariant.Pill -> PillTab(
            modifier = modifier.then(clickModifier),
            label = label.orEmpty(),
            selected = selected,
            state = state,
            skeleton = isSkeleton,
        )

        AmityTabVariant.Underlined -> UnderlinedTab(
            modifier = modifier.then(clickModifier),
            label = label.orEmpty(),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            selected = selected,
            state = state,
        )

        AmityTabVariant.Icon -> IconTab(
            modifier = modifier.then(clickModifier),
            icon = icon,
            selected = selected,
            state = state,
        )
    }
}

@Composable
private fun PillTab(
    modifier: Modifier,
    label: String,
    selected: Boolean,
    state: String,
    skeleton: Boolean,
) {
    if (skeleton) {
        Box(
            modifier = modifier
                .height(TAB_PILL_HEIGHT)
                .defaultMinSize(minWidth = 68.dp)
                .background(
                    color = AmityTheme.token("Surface/SkeletonEffect/Default"),
                    shape = RoundedCornerShape(TAB_PILL_RADIUS),
                )
        )
        return
    }

    val surface = AmityTheme.token("Surface/Tab/Pill/$state")
    val border = AmityTheme.token("Border/Tab/Pill/$state")
    val text = AmityTheme.token("Text/Tab/Pill/$state")

    Box(
        modifier = modifier
            .height(TAB_PILL_HEIGHT)
            .background(color = surface, shape = RoundedCornerShape(TAB_PILL_RADIUS))
            .border(width = 1.dp, color = border, shape = RoundedCornerShape(TAB_PILL_RADIUS))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, style = tabLabelStyle(selected, text))
    }
}

@Composable
private fun UnderlinedTab(
    modifier: Modifier,
    label: String,
    @DrawableRes leadingIcon: Int?,
    @DrawableRes trailingIcon: Int?,
    selected: Boolean,
    state: String,
) {
    val text = AmityTheme.token("Text/Tab/Underlined/$state")
    // Leading/trailing glyph slot shares the Icon-tab tint token — no dedicated
    // Underlined icon-slot token exists in the Design Tokens contract.
    val iconTint = AmityTheme.token("Icon/Tab/$state")
    Box(
        modifier = modifier
            .height(TAB_STACK_HEIGHT)
            .width(IntrinsicSize.Max),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(TAB_UNDERLINED_ICON_GAP),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leadingIcon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(TAB_UNDERLINED_ICON_SIZE),
                )
            }
            Text(text = label, style = tabLabelStyle(selected, text))
            if (trailingIcon != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = trailingIcon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(TAB_UNDERLINED_ICON_SIZE),
                )
            }
        }
        Indicator(
            visible = selected,
            color = AmityTheme.token("Line/Tab/Underlined/Active"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun IconTab(
    modifier: Modifier,
    @DrawableRes icon: Int?,
    selected: Boolean,
    state: String,
) {
    val tint = AmityTheme.token("Icon/Tab/$state")
    Box(
        modifier = modifier.height(TAB_STACK_HEIGHT),
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .size(24.dp),
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .size(24.dp),
            )
        }
        Indicator(
            visible = selected,
            color = AmityTheme.token("Line/Tab/Icon/Active"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(24.dp), // Icon variant: indicator matches the 24dp glyph
        )
    }
}

@Composable
private fun Indicator(visible: Boolean, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(TAB_INDICATOR_HEIGHT)
            .background(if (visible) color else Color.Transparent)
    )
}

@Composable
private fun tabLabelStyle(selected: Boolean, color: Color): TextStyle =
    AmityTheme.typography.body.copy(
        fontSize = TAB_LABEL_SIZE_SP.sp,
        lineHeight = TAB_LABEL_LINE_SP.sp,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        color = color,
    )

/** Ripple-free click without pulling in the material ripple indication. */
private fun Modifier.clickableNoRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
): Modifier = this.then(
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick,
    )
)
