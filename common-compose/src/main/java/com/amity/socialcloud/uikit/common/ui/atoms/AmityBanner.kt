package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * AmityBanner — the atomic standalone inline-notice row: an optional leading controller, an
 * optional single leading slot (icon/iconButton/avatar/media), a text column (Overline / Header
 * with optional flanking status badges + Subhead / Description with an optional glyph), and up to
 * 3 horizontal trailing accessories.
 *
 * A composite atom: it owns only the row's own Surface/Text/Icon `Banner` token family (bound per
 * [hierarchy]); every slot renders a nested atom that owns its own tokens — [AmitySelection]
 * (leading controller), [AmityButton] (leading/trailing icon buttons, leading-controller add
 * button), [AmityAvatar] (leading avatar), [AmityBadge] (title-row + trailing badges). Presentational
 * only for the container itself (no Hover/Press/Active/Disabled row state) — any interaction lives
 * in a composed slot, tracked by that slot's own atom.
 *
 * Not the composer's inline reply/edit banner (a different, feature-local affordance owned by
 * `AmityMessageComposer`) — that shape is not modeled here.
 *
 * `Text/Banner/{hierarchy}/Trailing/Text/General` and `.../Trailing/Subtext/General` ship in the
 * token registry but are intentionally left unbound: no sampled row places timestamp/label text in
 * the trailing row (only Icon/IconButton/Badge/Image/Video content) — see the Banner atom spec.
 *
 * @param hierarchy Default or Subdue — selects the bound `{Surface,Text,Icon}/Banner/{hierarchy}/…` row.
 * @param overline optional single-line text above the title row.
 * @param header the title row's Header text; the whole title row (badges + Header + Subhead) is
 * omitted when null.
 * @param headerLeadingBadge optional status badge before the Header text.
 * @param headerTrailingBadge optional status badge after the Header text, before [subhead].
 * @param subhead optional inline text after the (optional) trailing badge in the title row.
 * @param description optional up-to-2-line body text.
 * @param descriptionIcon optional glyph shown before [description]; ignored when [description] is null.
 * @param showLeadingController shows a Selection/add-button control before the leading slot.
 * @param leadingControllerType which control [showLeadingController] renders.
 * @param leadingControllerChecked checked/active state for a checkbox/radio controller.
 * @param leading optional content for the single leading slot.
 * @param trailing up to 3 trailing accessories, rendered side-by-side (extras beyond 3 are dropped).
 * @param loading renders the Skeleton placeholder (leading ghost icon button + circle + bar shimmer)
 * in place of all real content; the row surface itself is unchanged.
 * @param onPress tap on the banner body (excluding the leading controller and trailing accessories);
 * not invoked while [loading].
 * @param onLeadingControllerChange invoked when the leading controller's checkbox/radio toggles.
 * @param onLeadingPress invoked when the leading slot is an actionable icon button.
 * @param onTrailingPress invoked with the tapped trailing accessory's index (0–2).
 */
@Composable
fun AmityBanner(
    hierarchy: AmityBannerHierarchy,
    modifier: Modifier = Modifier,
    // Opt-in: center the content group (icon + text) horizontally instead of the default
    // list-row left-alignment. Used by full-width system notices (e.g. the chat "push notifications
    // disabled" banner, which the Figma centers) — leave false for list-item-style banners.
    centered: Boolean = false,
    overline: String? = null,
    header: String? = null,
    headerLeadingBadge: AmityBannerBadgeContent? = null,
    headerTrailingBadge: AmityBannerBadgeContent? = null,
    subhead: String? = null,
    description: String? = null,
    @DrawableRes descriptionIcon: Int? = null,
    showLeadingController: Boolean = false,
    leadingControllerType: AmityBannerControllerType = AmityBannerControllerType.CHECKBOX,
    leadingControllerChecked: Boolean = false,
    leading: AmityBannerLeadingContent? = null,
    trailing: List<AmityBannerTrailingContent> = emptyList(),
    loading: Boolean = false,
    onPress: (() -> Unit)? = null,
    onLeadingControllerChange: ((Boolean) -> Unit)? = null,
    onLeadingPress: (() -> Unit)? = null,
    onTrailingPress: ((Int) -> Unit)? = null,
) {
    val surface = AmityTheme.token(surfaceToken(hierarchy))

    var rowModifier = modifier
        .fillMaxWidth()
        .background(surface)
    if (onPress != null && !loading) {
        rowModifier = rowModifier.clickable { onPress() }
    }
    // Default-state list-row padding (8/16) differs from Skeleton-state and centered
    // system-notice padding (12/16) — not a single uniform inset (the Banner atom spec § Geometry).
    rowModifier = rowModifier.padding(
        vertical = if (loading || centered) 12.dp else 8.dp,
        horizontal = 16.dp,
    )

    Box(modifier = rowModifier) {
        if (loading) {
            BannerSkeletonRow()
        } else {
            BannerMainRow(
                hierarchy = hierarchy,
                centered = centered,
                overline = overline,
                header = header,
                headerLeadingBadge = headerLeadingBadge,
                headerTrailingBadge = headerTrailingBadge,
                subhead = subhead,
                description = description,
                descriptionIcon = descriptionIcon,
                showLeadingController = showLeadingController,
                leadingControllerType = leadingControllerType,
                leadingControllerChecked = leadingControllerChecked,
                leading = leading,
                trailing = trailing.take(3),
                onLeadingControllerChange = onLeadingControllerChange,
                onLeadingPress = onLeadingPress,
                onTrailingPress = onTrailingPress,
            )
        }
    }
    // ponytail: the Figma layer tree carries a named `Lists / Slot element/Bottom` (343×40) frame
    // on every sampled variant, but it is `visible: false` everywhere and no boolean component
    // property exposes/toggles it — its geometry/content/tokens are unconfirmed by the extraction
    // (the Banner atom spec § Designer Flags #8). Not implemented here; flagged for the designer to
    // confirm before a Bottom slot is added.
}

/** Hierarchy axis — Figma `Hierachy` (Default/Subdue). */
enum class AmityBannerHierarchy {
    DEFAULT,
    SUBDUE,
}

/** Which control [AmityBanner]'s leading controller renders when shown. */
enum class AmityBannerControllerType {
    CHECKBOX,
    RADIO,
    ADD_BUTTON,
}

/** Which content the single leading slot renders. */
enum class AmityBannerLeadingType {
    ICON,
    ICON_BUTTON,
    AVATAR,
    MEDIA,
}

/** Which content a trailing accessory renders. */
enum class AmityBannerTrailingType {
    ICON,
    ICON_BUTTON,
    BADGE,
    IMAGE,
    VIDEO,
}

/**
 * A status badge presented inline (title-row leading/trailing badges, an avatar's corner
 * indicator, or a trailing count badge) — a thin wrapper around [AmityBadgePreset] carrying the
 * extra icon/label content [AmityBadge] itself needs to render (the atom spec's own data model
 * lists only the preset; a preset alone resolves tokens but can't supply the glyph/count text).
 */
data class AmityBannerBadgeContent(
    val preset: AmityBadgePreset,
    @DrawableRes val icon: Int? = null,
    val label: String? = null,
)

/** Content for [AmityBanner]'s single leading slot. */
data class AmityBannerLeadingContent(
    val type: AmityBannerLeadingType,
    @DrawableRes val icon: Int? = null,
    val avatarUrl: String? = null,
    val avatarInitials: String? = null,
    val indicator: AmityBannerBadgeContent? = null,
    // Media has no dedicated atom in this system (the Banner atom spec § Designer Flags) — opaque
    // caller-supplied content rendered inside the atom's own 160×90 r8 frame.
    val media: (@Composable () -> Unit)? = null,
)

/** Content for one of [AmityBanner]'s up-to-3 trailing accessories. */
data class AmityBannerTrailingContent(
    val type: AmityBannerTrailingType,
    @DrawableRes val icon: Int? = null,
    val badge: AmityBannerBadgeContent? = null,
    // Image/Video are the same atom-less raw content as the leading Media slot.
    val media: (@Composable () -> Unit)? = null,
)

// ---------------------------------------------------------------------------
// Token resolution — the family is symmetric (10 parts × 2 Hierarchy values) as of the
// designer drop; resolved as a plain `when` (no string-path concatenation) so a renamed/removed
// AmityColorToken constant fails this file to compile instead of silently drifting.
// ---------------------------------------------------------------------------

private fun surfaceToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.SurfaceBannerDefaultGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.SurfaceBannerSubdueGeneral
}

private fun overlineToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.TextBannerDefaultOverlineGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.TextBannerSubdueOverlineGeneral
}

private fun headerToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.TextBannerDefaultHeaderGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.TextBannerSubdueHeaderGeneral
}

private fun subheadToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.TextBannerDefaultSubheadGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.TextBannerSubdueSubheadGeneral
}

private fun textDescriptionToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.TextBannerDefaultTextDescriptionGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.TextBannerSubdueTextDescriptionGeneral
}

private fun descriptionIconToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.IconBannerDefaultDescriptionGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.IconBannerSubdueDescriptionGeneral
}

private fun leadingIconToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.IconBannerDefaultLeadingIconGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.IconBannerSubdueLeadingIconGeneral
}

private fun trailingIconToken(h: AmityBannerHierarchy) = when (h) {
    AmityBannerHierarchy.DEFAULT -> AmityColorToken.IconBannerDefaultTrailingIconGeneral
    AmityBannerHierarchy.SUBDUE -> AmityColorToken.IconBannerSubdueTrailingIconGeneral
}

// ---------------------------------------------------------------------------
// Geometry constants (the Banner atom spec § Geometry) — explicit, not derived.
// ---------------------------------------------------------------------------

private val LeadingIconSize = 16.dp
private val LeadingAvatarSize = AmityAvatarSize.Size32
private val LeadingMediaWidth = 160.dp
private val LeadingMediaHeight = 90.dp
private val LeadingMediaCornerRadius = 8.dp

private val TrailingIconSize = 16.dp
private val TrailingMediaSize = 48.dp
private val TrailingMediaCornerRadius = 4.dp

private val DescriptionIconSize = 18.dp

private val SkeletonRowHeight = 40.dp
private val SkeletonCircleSize = 40.dp
private val SkeletonBarWidth = 140.dp
private val SkeletonBarHeight = 10.dp
private val SkeletonBarCornerRadius = 12.dp

// ---------------------------------------------------------------------------
// Main (State=Default) content row
// ---------------------------------------------------------------------------

@Composable
private fun BannerMainRow(
    hierarchy: AmityBannerHierarchy,
    centered: Boolean,
    overline: String?,
    header: String?,
    headerLeadingBadge: AmityBannerBadgeContent?,
    headerTrailingBadge: AmityBannerBadgeContent?,
    subhead: String?,
    description: String?,
    @DrawableRes descriptionIcon: Int?,
    showLeadingController: Boolean,
    leadingControllerType: AmityBannerControllerType,
    leadingControllerChecked: Boolean,
    leading: AmityBannerLeadingContent?,
    trailing: List<AmityBannerTrailingContent>,
    onLeadingControllerChange: ((Boolean) -> Unit)?,
    onLeadingPress: (() -> Unit)?,
    onTrailingPress: ((Int) -> Unit)?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        // centered → pack the content group and center it in the full-width row (system-notice
        // layout); default → left-aligned list-row.
        horizontalArrangement = if (centered) Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            else Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showLeadingController) {
            BannerLeadingController(
                type = leadingControllerType,
                checked = leadingControllerChecked,
                onChange = onLeadingControllerChange,
            )
        }

        // leading+content wrapper — FILL (absorbs the width the leading/trailing slots don't use)
        // in the default list-row layout; HUG when centered so the group can center.
        Row(
            modifier = if (centered) Modifier else Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leading != null) {
                BannerLeadingSlot(
                    content = leading,
                    hierarchy = hierarchy,
                    onLeadingPress = onLeadingPress,
                )
            }

            BannerContentColumn(
                modifier = if (centered) Modifier else Modifier.weight(1f),
                hierarchy = hierarchy,
                overline = overline,
                header = header,
                headerLeadingBadge = headerLeadingBadge,
                headerTrailingBadge = headerTrailingBadge,
                subhead = subhead,
                description = description,
                descriptionIcon = descriptionIcon,
            )
        }

        if (trailing.isNotEmpty()) {
            BannerTrailingRow(
                items = trailing,
                hierarchy = hierarchy,
                onTrailingPress = onTrailingPress,
            )
        }
    }
}

@Composable
private fun BannerLeadingController(
    type: AmityBannerControllerType,
    checked: Boolean,
    onChange: ((Boolean) -> Unit)?,
) {
    when (type) {
        AmityBannerControllerType.CHECKBOX -> AmitySelection(
            variant = AmitySelectionVariant.CHECKBOX,
            isSelected = checked,
            onChange = onChange?.let { callback -> { selected: Boolean, _: String? -> callback(selected) } },
        )

        AmityBannerControllerType.RADIO -> AmitySelection(
            variant = AmitySelectionVariant.RADIO,
            isSelected = checked,
            onChange = onChange?.let { callback -> { selected: Boolean, _: String? -> callback(selected) } },
        )

        // Filled/Primary — the same Button-atom preset as the Leading Icon Button slot.
        AmityBannerControllerType.ADD_BUTTON -> AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.PRIMARY,
            iconSize = AmityIconButtonSize.SIZE24,
            icon = R.drawable.amity_ic_plus_r,
            onClick = { onChange?.invoke(!checked) },
        )
    }
}

@Composable
private fun BannerLeadingSlot(
    content: AmityBannerLeadingContent,
    hierarchy: AmityBannerHierarchy,
    onLeadingPress: (() -> Unit)?,
) {
    when (content.type) {
        AmityBannerLeadingType.ICON -> if (content.icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = content.icon),
                contentDescription = null,
                tint = AmityTheme.token(leadingIconToken(hierarchy)),
                modifier = Modifier.size(LeadingIconSize),
            )
        }

        // Filled/Primary — a Button-atom instance; Banner owns none of its tokens/geometry.
        AmityBannerLeadingType.ICON_BUTTON -> AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.PRIMARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = content.icon,
            onClick = { onLeadingPress?.invoke() },
        )

        AmityBannerLeadingType.AVATAR -> AmityAvatar(
            variant = if (content.avatarUrl != null) AmityAvatarVariant.Image else AmityAvatarVariant.Text,
            imageUrl = content.avatarUrl,
            initials = content.avatarInitials,
            icon = content.icon,
            size = LeadingAvatarSize,
            indicator = content.indicator?.let { badge -> { BannerBadgeSlot(badge) } },
        )

        AmityBannerLeadingType.MEDIA -> Box(
            modifier = Modifier
                .size(width = LeadingMediaWidth, height = LeadingMediaHeight)
                .clip(RoundedCornerShape(LeadingMediaCornerRadius)),
        ) {
            content.media?.invoke()
        }
    }
}

@Composable
private fun BannerContentColumn(
    modifier: Modifier,
    hierarchy: AmityBannerHierarchy,
    overline: String?,
    header: String?,
    headerLeadingBadge: AmityBannerBadgeContent?,
    headerTrailingBadge: AmityBannerBadgeContent?,
    subhead: String?,
    description: String?,
    @DrawableRes descriptionIcon: Int?,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (overline != null) {
            Text(
                text = overline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // SemiBold 13/18 — matches captionLegacy's own default weight, no override needed.
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.token(overlineToken(hierarchy)),
                ),
            )
        }

        if (header != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                headerLeadingBadge?.let { BannerBadgeSlot(it) }
                Text(
                    text = header,
                    // Single-line title row (the Banner atom spec § Anatomy) — shrinks to fit, not
                    // fixed width, so the optional flanking badges/Subhead keep their own size.
                    modifier = Modifier.weight(weight = 1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    // Bold 15/20 (the Banner atom spec Designer Flags #11) — bodyLegacy is 15/20
                    // Normal by default, so only the weight needs overriding.
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmityTheme.token(headerToken(hierarchy)),
                    ),
                )
                headerTrailingBadge?.let { BannerBadgeSlot(it) }
                if (subhead != null) {
                    Text(
                        text = subhead,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        // Regular 13/18.
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.token(subheadToken(hierarchy)),
                        ),
                    )
                }
            }
        }

        if (description != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Top,
            ) {
                if (descriptionIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = descriptionIcon),
                        contentDescription = null,
                        tint = AmityTheme.token(descriptionIconToken(hierarchy)),
                        modifier = Modifier.size(DescriptionIconSize),
                    )
                }
                Text(
                    text = description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    // Regular 13/18.
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.token(textDescriptionToken(hierarchy)),
                    ),
                )
            }
        }
    }
}

@Composable
private fun BannerTrailingRow(
    items: List<AmityBannerTrailingContent>,
    hierarchy: AmityBannerHierarchy,
    onTrailingPress: ((Int) -> Unit)?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            when (item.type) {
                AmityBannerTrailingType.ICON -> if (item.icon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = item.icon),
                        contentDescription = null,
                        tint = AmityTheme.token(trailingIconToken(hierarchy)),
                        modifier = Modifier.size(TrailingIconSize),
                    )
                }

                // Ghost/Secondary — a Button-atom instance; Banner owns none of its tokens.
                AmityBannerTrailingType.ICON_BUTTON -> AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    iconSize = AmityIconButtonSize.SIZE24,
                    icon = item.icon,
                    onClick = { onTrailingPress?.invoke(index) },
                )

                AmityBannerTrailingType.BADGE -> item.badge?.let { badge ->
                    BannerBadgeSlot(badge, size = AmityBadgeSize.SIZE_24)
                }

                AmityBannerTrailingType.IMAGE, AmityBannerTrailingType.VIDEO -> Box(
                    modifier = Modifier
                        .size(TrailingMediaSize)
                        .clip(RoundedCornerShape(TrailingMediaCornerRadius)),
                ) {
                    item.media?.invoke()
                }
            }
        }
    }
}

@Composable
private fun BannerBadgeSlot(
    badge: AmityBannerBadgeContent,
    size: AmityBadgeSize = AmityBadgeSize.SIZE_16,
    shape: AmityBadgeShape = AmityBadgeShape.ROUND,
) {
    AmityBadge(
        variant = if (badge.label != null) AmityBadgeVariant.LABEL else AmityBadgeVariant.ICON,
        label = badge.label,
        icon = badge.icon,
        shape = shape,
        size = size,
        preset = badge.preset,
    )
}

// ---------------------------------------------------------------------------
// State=Skeleton content row — shared shimmer token, not a Banner-owned skeleton token.
// ---------------------------------------------------------------------------

@Composable
private fun BannerSkeletonRow() {
    val skeletonSurface = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SkeletonRowHeight),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ponytail: the sampled Figma skeleton renders a `chevron-left` glyph in this slot — an
        // arbitrary placeholder, not a prescriptive icon choice for a loading row (the Banner atom spec
        // § Composition). Binds Icon/IconButton/Ghost/Secondary/Default via the Button atom itself.
        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.GHOST,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = R.drawable.amity_ic_chevron_left,
        )
        Box(
            modifier = Modifier
                .size(SkeletonCircleSize)
                .clip(CircleShape)
                .background(skeletonSurface),
        )
        Box(
            modifier = Modifier
                .size(width = SkeletonBarWidth, height = SkeletonBarHeight)
                .clip(RoundedCornerShape(SkeletonBarCornerRadius))
                .background(skeletonSurface),
        )
    }
}
