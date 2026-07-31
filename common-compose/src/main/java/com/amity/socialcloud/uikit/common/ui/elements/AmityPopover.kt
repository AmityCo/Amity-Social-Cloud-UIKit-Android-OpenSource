package com.amity.socialcloud.uikit.common.ui.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

// ---------------------------------------------------------------------------
// AmityPopover — shared, slot-based container.
//
// The rounded, elevated surface behind chat's contextual menus and floating
// pickers: the create-chat menu (Direct chat / Group chat), the message/media
// long-press action menus, and the mention picker. Presentational only —
// anchoring to a trigger element/point and open/close orchestration are the
// caller's own responsibility (see each consumer's own `Popup` +
// `PopupPositionProvider`); this file only renders the container, the flush
// (0-gap) row Slot, and the optional close (X) control.
// ---------------------------------------------------------------------------

/**
 * The rounded (`r12`), elevated container. Fixed instance width — 240.dp (Create-chat / generic)
 * or 160.dp (compact Message-/Media-pressed menus) — height is intrinsic to [content].
 *
 * @param width Fixed container width; defaults to the 240.dp (Create-chat) variant.
 * @param showClose Whether the close (X) affordance renders. Defaults to `false` — mobile/touch
 * consumers (this container's only Android consumers so far) dismiss by tap-outside only; set
 * `true` for a desktop/pointer surface that needs a visible affordance.
 * @param onDismiss Invoked when the close control is tapped. Unused when [showClose] is `false`.
 * @param content The Slot — expected to be List-family rows (see [AmityPopoverRow]).
 */
@Composable
fun AmityPopover(
    modifier: Modifier = Modifier,
    width: Dp = 240.dp,
    showClose: Boolean = false,
    onDismiss: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val surface = AmityTheme.token(AmityColorToken.SurfacePopoverBackgroundDefault)
    Box(modifier = modifier.width(width)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .amityPopoverElevation(surface)
                .clip(RoundedCornerShape(12.dp))
                .background(surface)
                .padding(vertical = 8.dp),
            content = content,
        )

        if (showClose) {
            // Absolute at left: containerWidth − 20, top: −8 (straddles the top-right corner).
            // TopEnd's default right/top edges sit at (width, 0), so the close control's own
            // right edge (left+24 = width+4) needs +4 horizontal, and its top (−8) needs −8
            // vertical, relative to that default.
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-8).dp),
            ) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.FILLED,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    iconSize = AmityIconButtonSize.SIZE24,
                    icon = R.drawable.amity_ic_cross_r,
                    onClick = onDismiss,
                )
            }
        }
    }
}

/**
 * Elevation gap: no semantic Elevation/shadow token exists in the token set yet, so the two-layer
 * drop shadow is hardcoded here — `0px 4px 8px rgba(96, 97, 112, 0.20)` layered under
 * `0px 0px 2px rgba(40, 41, 61, 0.10)`.
 *
 * It is PAINTED directly rather than expressed through `Modifier.shadow(elevation = …)`: the
 * elevation API renders far too faint to read against a light surface (and can't set the shadow's
 * x/y offset), so it looked like "no elevation." `setShadowLayer` reproduces the exact blur, offset
 * and colour of each design layer. The shadow is cast by the opaque [surfaceColor] fill — a
 * transparent shape casts nothing on a hardware canvas — so the surface is drawn here and again by
 * the caller's own `.background()`. The shadow bleeds beyond the popover bounds, so consumers that
 * host this inside a content-sized `Popup` must add a transparent gutter (see each consumer's
 * shadow-gutter) or it will be clipped by the popup window.
 */
private fun Modifier.amityPopoverElevation(surfaceColor: Color): Modifier = this.drawBehind {
    val radiusPx = 12.dp.toPx()
    val fill = surfaceColor.toArgb()
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply { isAntiAlias = true }
        // Layer 1 — 0·4·8 rgba(96,97,112,0.20)
        paint.color = fill
        paint.setShadowLayer(8.dp.toPx(), 0f, 4.dp.toPx(), Color(0x33606170).toArgb())
        canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
        // Layer 2 — 0·0·2 rgba(40,41,61,0.10)
        paint.setShadowLayer(2.dp.toPx(), 0f, 0f, Color(0x1A28293D).toArgb())
        canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
    }
}

/** The two row-height sub-variants. */
enum class AmityPopoverRowSize(internal val verticalPadding: Dp) {
    /** 56.dp rows, padding 16/16/16/16 — the Create-chat variant. */
    LARGE(16.dp),

    /** 48.dp rows, padding 12/16/12/16 — every Message-/Media-pressed variant. */
    COMPACT(12.dp),
}

/**
 * A single List-family row inside an [AmityPopover]'s Slot — not owned by this component (no
 * formal `List` atom exists yet), documented here directly. Rows stack flush (0 gap, no divider);
 * this composable renders one row only.
 *
 * @param destructive Selects the `.../Destructive/...` icon+label token pair (e.g. Delete); a
 * destructive row always shows the row's Default surface, never Hover.
 */
@Composable
fun AmityPopoverRow(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String,
    size: AmityPopoverRowSize = AmityPopoverRowSize.LARGE,
    destructive: Boolean = false,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    // Android has no pointer-hover concept on touch, so the Hover row surface is shown while
    // pressed instead. A destructive row always keeps its Default surface, never Hover.
    val pressed by interactionSource.collectIsPressedAsState()
    val showHoverSurface = pressed && !destructive

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (showHoverSurface) {
                    AmityTheme.token(AmityColorToken.SurfacePopoverListsHover)
                } else {
                    AmityTheme.token(AmityColorToken.SurfacePopoverListsDefault)
                },
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = AmityTheme.token(AmityColorToken.SurfacePopoverListsHover)),
                onClick = onSelect,
            )
            .padding(horizontal = 16.dp, vertical = size.verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (destructive) {
                AmityTheme.token(AmityColorToken.IconListLeadingDestructiveDefault)
            } else {
                AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault)
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            // SF Pro Regular 15/20 throughout every row —
            // `bodyLegacy` is exactly that (15sp/20sp, FontWeight.Normal), no weight override.
            style = AmityTheme.typography.bodyLegacy.copy(
                color = when {
                    destructive -> AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault)
                    showHoverSurface -> AmityTheme.token(AmityColorToken.TextListHeaderDefaultHover)
                    else -> AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault)
                },
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AmityPopoverCreateChatPreview() {
    // Illustrates the "Create chat" content variant (240x128, 2 rows x 56, Direct
    // Chat / Group chat) — preview-only strings, not shipped user-facing copy.
    AmityPopover {
        AmityPopoverRow(
            icon = R.drawable.amity_ic_user_plus_r,
            label = "Direct chat",
            onSelect = {},
        )
        AmityPopoverRow(
            icon = R.drawable.amity_ic_user_group_r,
            label = "Group chat",
            onSelect = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AmityPopoverDestructivePreview() {
    // Illustrates a compact Message-pressed variant (160x160-ish) with a destructive row.
    AmityPopover(width = 160.dp) {
        AmityPopoverRow(
            icon = R.drawable.amity_ic_share_left_l,
            label = "Reply",
            size = AmityPopoverRowSize.COMPACT,
            onSelect = {},
        )
        AmityPopoverRow(
            icon = R.drawable.amity_ic_trash_r,
            label = "Delete",
            size = AmityPopoverRowSize.COMPACT,
            destructive = true,
            onSelect = {},
        )
    }
}
