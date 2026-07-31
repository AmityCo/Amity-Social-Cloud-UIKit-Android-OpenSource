package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

/** The row's Color axis — Default or Destructive (Figma `Color` variant). */
enum class AmityListItemVariant { DEFAULT, DESTRUCTIVE }

/** Which content the leading slot renders. */
enum class AmityListLeadingType { NONE, ICON, FEATURED_ICON, AVATAR, MEDIA }

/** Which content a trailing component renders. */
enum class AmityListTrailingType { ICON, CHECKBOX, RADIO, TOGGLE, BUTTON, BADGE, REACTION, TEXT }

/** Content for [AmityListItem]'s leading slot. */
data class AmityListLeadingContent(
    val type: AmityListLeadingType,
    @DrawableRes val icon: Int? = null,
    val avatarUrl: String? = null,
    val avatarInitials: String? = null,
    // 32×32 sampled (the List atom spec § Geometry); real consumers size it independently, e.g. 40×40
    // in chat.
    val avatarSize: AmityAvatarSize = AmityAvatarSize.Size32,
    val avatarStyle: AmityAvatarStyle = AmityAvatarStyle.Rounded,
    val avatarBorderWidth: Int = 0,
    val indicator: (@Composable () -> Unit)? = null,
    // Featured Icon has no dedicated atom yet (the List atom spec § Composition) — Solid/Tinted is
    // this atom's own minimal composed-shape choice, not a documented prop.
    val featuredIconSolid: Boolean = false,
    // Media has no dedicated atom either — opaque caller-supplied content, like AmityBanner's own
    // Media leading slot.
    val media: (@Composable () -> Unit)? = null,
)

/** Content for one of [AmityListItem]'s up-to-2 trailing components. */
data class AmityListTrailingContent(
    val type: AmityListTrailingType,
    @DrawableRes val icon: Int? = null,
    val checked: Boolean = false,
    val toggledOn: Boolean = false,
    val label: String? = null,
    val badgePreset: AmityBadgePreset? = null,
    @DrawableRes val badgeIcon: Int? = null,
    val badgeLabel: String? = null,
    val reactionCount: Int? = null,
)

/**
 * AmityListItem — the atomic generic list row: an optional Leading Controller (Checkbox/Radio),
 * an optional leading slot (Icon / Featured Icon / Avatar / Media), a content column (Overline,
 * Title row [Header + inline Subhead + trailing glyph], Description), an optional trailing column
 * (a label above up to 2 trailing components), and an optional full-width Bottom slot.
 *
 * A composite atom: it owns only the row's own Surface/Text/Icon `List` token family (bound per
 * [variant] + the row's internal Default/Hover/Active/Disabled state). Every slot renders a nested
 * atom that owns its own tokens — [AmitySelection] (leading controller + trailing Checkbox/Radio),
 * [AmityAvatar] (+ an optional [AmityBadge] corner indicator), [AmityToggle] (trailing Toggle),
 * [AmityButton] (trailing Button), [AmityBadge] (trailing Badge). The Chip-group row (composes
 * [AmityChip]) is not wired here — no consumer of this atom needs it yet and the atom spec's own
 * `Text/List/Label/{state}` override isn't expressible through [AmityChip]'s current API (it always
 * resolves its own internal label token) — flagged, not invented here.
 *
 * @param variant Default or Destructive — selects the bound `{Surface,Text,Icon}/List/{variant}/…` row.
 * @param title the Header text (display name).
 * @param overline optional single-line text above the title row.
 * @param subhead optional inline text after [title] in the title row (e.g. a `(999)` count).
 * @param description optional up-to-2-line body/preview text below the title row.
 * @param descriptionIcon optional glyph shown before [description]; ignored when [description] is null.
 * @param headerIcon optional glyph shown at the end of the title row (e.g. a mute glyph).
 * @param highlight renders the search-match `Highlight` text row (Header/Subhead/Description) — an
 * axis independent of [active] (the List atom spec § Designer Flags #4); the row surface is unaffected.
 * @param titleAccessory opaque content rendered in the title row, after [subhead] and before
 * [headerIcon] — an Android-only escape hatch for un-tokenized content the documented API doesn't
 * model (e.g. a raw brand/verification badge image), not a prop the atom spec defines.
 * @param leadingType which leading slot content to render.
 * @param leading content for the leading slot; shape depends on [leadingType].
 * @param showLeadingControl shows a Checkbox/Radio before the leading slot.
 * @param leadingControlType which [AmitySelection] control [showLeadingControl] renders.
 * @param leadingControlChecked checked/active state of the leading control.
 * @param trailingLabel small text above the trailing components (e.g. a timestamp).
 * @param trailing up to 2 trailing slot items (extras beyond 2 are dropped).
 * @param bottom opaque full-width content for the optional Bottom slot; renders only when set.
 * @param disabled renders the Disabled token row and suppresses [onPress].
 * @param active renders the Active row surface — marks this row as selected/currently-open.
 * @param loading renders the Skeleton row (avatar-circle + bar shimmer) in place of all real content.
 * @param onPress called on tap of an enabled, non-loading row.
 * @param onLeadingControlChange called when the Leading Controller toggles.
 * @param onTrailingPress called with the tapped trailing component's index (0–1).
 */
@Composable
fun AmityListItem(
    variant: AmityListItemVariant,
    title: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    subhead: String? = null,
    description: String? = null,
    @DrawableRes descriptionIcon: Int? = null,
    @DrawableRes headerIcon: Int? = null,
    highlight: Boolean = false,
    titleAccessory: (@Composable () -> Unit)? = null,
    leadingType: AmityListLeadingType = AmityListLeadingType.NONE,
    leading: AmityListLeadingContent? = null,
    showLeadingControl: Boolean = false,
    leadingControlType: AmitySelectionVariant = AmitySelectionVariant.CHECKBOX,
    leadingControlChecked: Boolean = false,
    trailingLabel: String? = null,
    trailing: List<AmityListTrailingContent> = emptyList(),
    bottom: (@Composable () -> Unit)? = null,
    disabled: Boolean = false,
    active: Boolean = false,
    loading: Boolean = false,
    onPress: (() -> Unit)? = null,
    onLeadingControlChange: ((Boolean) -> Unit)? = null,
    onTrailingPress: ((Int) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()

    val rowState = when {
        disabled -> ListRowState.DISABLED
        hovered -> ListRowState.HOVER
        active -> ListRowState.ACTIVE
        else -> ListRowState.DEFAULT
    }

    val surface = AmityTheme.token(
        if (loading) AmityColorToken.SurfaceListSkeletonSkeleton else surfaceToken(variant, rowState)
    )

    var rowModifier = modifier
        .fillMaxWidth()
        .background(surface)
    if (onPress != null && !disabled && !loading) {
        // Shares interactionSource with the Hover-state resolution above (matches AmityTab/AmityButton's
        // own no-ripple, tracked-interaction-source convention).
        rowModifier = rowModifier.clickable(
            interactionSource = interactionSource,
            indication = null,
        ) { onPress() }
    }
    rowModifier = rowModifier.padding(vertical = RowVerticalPadding, horizontal = RowHorizontalPadding)

    Column(modifier = rowModifier) {
        if (loading) {
            ListItemSkeletonRow()
        } else {
            MainContentRow(
                variant = variant,
                rowState = rowState,
                title = title,
                overline = overline,
                subhead = subhead,
                description = description,
                descriptionIcon = descriptionIcon,
                headerIcon = headerIcon,
                highlight = highlight,
                titleAccessory = titleAccessory,
                leadingType = leadingType,
                leading = leading,
                showLeadingControl = showLeadingControl,
                leadingControlType = leadingControlType,
                leadingControlChecked = leadingControlChecked,
                trailingLabel = trailingLabel,
                trailing = trailing,
                disabled = disabled,
                onLeadingControlChange = onLeadingControlChange,
                onTrailingPress = onTrailingPress,
            )
            if (bottom != null) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = BottomGap)) {
                    bottom()
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Internal row-state axis — resolved once, threaded through every token lookup below so a single
// `when` per part stays exhaustive (no string-path concatenation), matching AmityBanner's convention.
// ---------------------------------------------------------------------------

private enum class ListRowState { DEFAULT, HOVER, ACTIVE, DISABLED }

/** Text/Icon parts have no `Active` row (the List atom spec § States) — only the surface recolors. */
private enum class ListTextState { DEFAULT, HOVER, DISABLED, HIGHLIGHT }

private fun textState(rowState: ListRowState, highlight: Boolean): ListTextState = when {
    highlight -> ListTextState.HIGHLIGHT
    rowState == ListRowState.HOVER -> ListTextState.HOVER
    rowState == ListRowState.DISABLED -> ListTextState.DISABLED
    else -> ListTextState.DEFAULT
}

private fun surfaceToken(variant: AmityListItemVariant, state: ListRowState): AmityColorToken = when (variant) {
    AmityListItemVariant.DEFAULT -> when (state) {
        ListRowState.DEFAULT -> AmityColorToken.SurfaceListDefaultDefault
        ListRowState.HOVER -> AmityColorToken.SurfaceListDefaultHover
        ListRowState.ACTIVE -> AmityColorToken.SurfaceListDefaultActive
        ListRowState.DISABLED -> AmityColorToken.SurfaceListDefaultDisabled
    }
    // Destructive ships no Active/Swipe row (the List atom spec § Overview) — Active/Default both
    // resolve to Destructive/Default.
    AmityListItemVariant.DESTRUCTIVE -> when (state) {
        ListRowState.HOVER -> AmityColorToken.SurfaceListDestructiveHover
        ListRowState.DISABLED -> AmityColorToken.SurfaceListDestructiveDisabled
        else -> AmityColorToken.SurfaceListDestructiveDefault
    }
}

private fun headerToken(variant: AmityListItemVariant, state: ListTextState): AmityColorToken = when (variant) {
    AmityListItemVariant.DEFAULT -> when (state) {
        ListTextState.DEFAULT -> AmityColorToken.TextListHeaderDefaultDefault
        ListTextState.HOVER -> AmityColorToken.TextListHeaderDefaultHover
        ListTextState.DISABLED -> AmityColorToken.TextListHeaderDefaultDisabled
        ListTextState.HIGHLIGHT -> AmityColorToken.TextListHeaderDefaultHighlight
    }
    // Destructive ships no Highlight row (the List atom spec § Design Tokens) — falls back to Default.
    AmityListItemVariant.DESTRUCTIVE -> when (state) {
        ListTextState.HOVER -> AmityColorToken.TextListHeaderDestructiveHover
        ListTextState.DISABLED -> AmityColorToken.TextListHeaderDestructiveDisabled
        else -> AmityColorToken.TextListHeaderDestructiveDefault
    }
}

private fun subheadToken(variant: AmityListItemVariant, state: ListTextState): AmityColorToken = when (variant) {
    AmityListItemVariant.DEFAULT -> when (state) {
        ListTextState.DEFAULT -> AmityColorToken.TextListSubheadDefaultDefault
        ListTextState.HOVER -> AmityColorToken.TextListSubheadDefaultHover
        ListTextState.DISABLED -> AmityColorToken.TextListSubheadDefaultDisabled
        ListTextState.HIGHLIGHT -> AmityColorToken.TextListSubheadDefaultHighlight
    }
    AmityListItemVariant.DESTRUCTIVE -> when (state) {
        ListTextState.HOVER -> AmityColorToken.TextListSubheadDestructiveHover
        ListTextState.DISABLED -> AmityColorToken.TextListSubheadDestructiveDisabled
        else -> AmityColorToken.TextListSubheadDestructiveDefault
    }
}

private fun textDescriptionToken(variant: AmityListItemVariant, state: ListTextState): AmityColorToken =
    when (variant) {
        AmityListItemVariant.DEFAULT -> when (state) {
            ListTextState.DEFAULT -> AmityColorToken.TextListTextDescriptionDefaultDefault
            ListTextState.HOVER -> AmityColorToken.TextListTextDescriptionDefaultHover
            ListTextState.DISABLED -> AmityColorToken.TextListTextDescriptionDefaultDisabled
            ListTextState.HIGHLIGHT -> AmityColorToken.TextListTextDescriptionDefaultHighlight
        }
        AmityListItemVariant.DESTRUCTIVE -> when (state) {
            ListTextState.HOVER -> AmityColorToken.TextListTextDescriptionDestructiveHover
            ListTextState.DISABLED -> AmityColorToken.TextListTextDescriptionDestructiveDisabled
            else -> AmityColorToken.TextListTextDescriptionDestructiveDefault
        }
    }

// Icon/List/Leading/{variant}/{state} — also the token a trailing Icon-type component binds: no
// dedicated trailing-icon token exists in the registry, corroborated by AmityGroupMemberListPage v2
// independently citing this same family for its trailing "more" glyph (the List atom spec § Designer
// Flags #5).
private fun leadingIconToken(variant: AmityListItemVariant, state: ListRowState): AmityColorToken = when (variant) {
    AmityListItemVariant.DEFAULT -> when (state) {
        ListRowState.DEFAULT -> AmityColorToken.IconListLeadingDefaultDefault
        ListRowState.HOVER -> AmityColorToken.IconListLeadingDefaultHover
        ListRowState.ACTIVE -> AmityColorToken.IconListLeadingDefaultActive
        ListRowState.DISABLED -> AmityColorToken.IconListLeadingDefaultDisabled
    }
    AmityListItemVariant.DESTRUCTIVE -> when (state) {
        ListRowState.HOVER -> AmityColorToken.IconListLeadingDestructiveHover
        ListRowState.DISABLED -> AmityColorToken.IconListLeadingDestructiveDisabled
        else -> AmityColorToken.IconListLeadingDestructiveDefault
    }
}

// ---------------------------------------------------------------------------
// Geometry constants (the List atom spec § Geometry) — explicit, not derived.
// ---------------------------------------------------------------------------

private val RowVerticalPadding = 8.dp
private val RowHorizontalPadding = 16.dp
private val MainContentGap = 8.dp
private val ContentColumnGap = 2.dp
private val TitleRowGap = 2.dp
private val TrailingColumnGap = 4.dp
private val TrailingComponentsGap = 4.dp
private val BottomGap = 12.dp

private val LeadingIconSize = 24.dp
private val FeaturedIconSize = 32.dp
private val FeaturedIconGlyphSize = 24.dp
private val FeaturedIconRadius = 8.dp
private val MediaWidth = 160.dp
private val MediaHeight = 90.dp
private val MediaCornerRadius = 8.dp

private val HeaderIconSize = 16.dp
private val DescriptionIconSize = 18.dp
// The Trailing—Icon geometry is a deliberate 32×32 outlier vs. its 24×24-class trailing siblings
// (the List atom spec § Geometry — measured from the design masters).
private val TrailingIconSize = 32.dp

private val ReactionPillHeight = 24.dp
private val ReactionPillMinWidth = 24.dp
private val ReactionPillRadius = 99.dp
private val ReactionPillHorizontalPadding = 6.dp

private val SkeletonRowHeight = 56.dp
private val SkeletonAvatarSize = 40.dp
private val SkeletonBarWidth = 160.dp
private val SkeletonBarHeight = 14.dp
private val SkeletonBarRadius = 4.dp

// ---------------------------------------------------------------------------
// Main content row
// ---------------------------------------------------------------------------

@Composable
private fun MainContentRow(
    variant: AmityListItemVariant,
    rowState: ListRowState,
    title: String,
    overline: String?,
    subhead: String?,
    description: String?,
    @DrawableRes descriptionIcon: Int?,
    @DrawableRes headerIcon: Int?,
    highlight: Boolean,
    titleAccessory: (@Composable () -> Unit)?,
    leadingType: AmityListLeadingType,
    leading: AmityListLeadingContent?,
    showLeadingControl: Boolean,
    leadingControlType: AmitySelectionVariant,
    leadingControlChecked: Boolean,
    trailingLabel: String?,
    trailing: List<AmityListTrailingContent>,
    disabled: Boolean,
    onLeadingControlChange: ((Boolean) -> Unit)?,
    onTrailingPress: ((Int) -> Unit)?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MainContentGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showLeadingControl) {
            AmitySelection(
                variant = leadingControlType,
                isSelected = leadingControlChecked,
                isDisabled = disabled,
                onChange = onLeadingControlChange?.let { callback ->
                    { selected: Boolean, _: String? -> callback(selected) }
                },
            )
        }

        if (leadingType != AmityListLeadingType.NONE) {
            LeadingSlot(type = leadingType, content = leading, variant = variant, rowState = rowState)
        }

        ContentColumn(
            modifier = Modifier.weight(1f),
            variant = variant,
            rowState = rowState,
            title = title,
            overline = overline,
            subhead = subhead,
            description = description,
            descriptionIcon = descriptionIcon,
            headerIcon = headerIcon,
            highlight = highlight,
            titleAccessory = titleAccessory,
        )

        if (trailingLabel != null || trailing.isNotEmpty()) {
            TrailingColumn(
                label = trailingLabel,
                items = trailing.take(2),
                variant = variant,
                rowState = rowState,
                disabled = disabled,
                onTrailingPress = onTrailingPress,
            )
        }
    }
}

@Composable
private fun LeadingSlot(
    type: AmityListLeadingType,
    content: AmityListLeadingContent?,
    variant: AmityListItemVariant,
    rowState: ListRowState,
) {
    when (type) {
        AmityListLeadingType.NONE -> Unit

        AmityListLeadingType.ICON -> if (content?.icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = content.icon),
                contentDescription = null,
                tint = AmityTheme.token(leadingIconToken(variant, rowState)),
                modifier = Modifier.size(LeadingIconSize),
            )
        }

        AmityListLeadingType.FEATURED_ICON -> {
            val surface = AmityTheme.token(
                if (content?.featuredIconSolid == true) {
                    AmityColorToken.SurfaceFeaturedIconSolid
                } else {
                    AmityColorToken.SurfaceFeaturedIconTinted
                }
            )
            val glyphTint = AmityTheme.token(
                if (content?.featuredIconSolid == true) {
                    AmityColorToken.IconFeaturedIconSolid
                } else {
                    AmityColorToken.IconFeaturedIconTinted
                }
            )
            Box(
                modifier = Modifier
                    .size(FeaturedIconSize)
                    .clip(RoundedCornerShape(FeaturedIconRadius))
                    .background(surface),
                contentAlignment = Alignment.Center,
            ) {
                if (content?.icon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = content.icon),
                        contentDescription = null,
                        tint = glyphTint,
                        modifier = Modifier.size(FeaturedIconGlyphSize),
                    )
                }
            }
        }

        AmityListLeadingType.AVATAR -> if (content != null) {
            AmityAvatar(
                variant = when {
                    !content.avatarUrl.isNullOrEmpty() -> AmityAvatarVariant.Image
                    !content.avatarInitials.isNullOrEmpty() -> AmityAvatarVariant.Text
                    else -> AmityAvatarVariant.Icon
                },
                imageUrl = content.avatarUrl,
                initials = content.avatarInitials,
                icon = content.icon,
                style = content.avatarStyle,
                size = content.avatarSize,
                borderWidth = content.avatarBorderWidth,
                indicator = content.indicator,
            )
        }

        AmityListLeadingType.MEDIA -> Box(
            modifier = Modifier
                .size(width = MediaWidth, height = MediaHeight)
                .clip(RoundedCornerShape(MediaCornerRadius)),
        ) {
            content?.media?.invoke()
        }
    }
}

@Composable
private fun ContentColumn(
    modifier: Modifier,
    variant: AmityListItemVariant,
    rowState: ListRowState,
    title: String,
    overline: String?,
    subhead: String?,
    description: String?,
    @DrawableRes descriptionIcon: Int?,
    @DrawableRes headerIcon: Int?,
    highlight: Boolean,
    titleAccessory: (@Composable () -> Unit)?,
) {
    val textState = textState(rowState, highlight)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ContentColumnGap),
    ) {
        if (overline != null) {
            Text(
                text = overline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // SemiBold 13/18 — captionLegacy's own default weight, no override needed.
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.token(AmityColorToken.TextListOverlineDefaultDefault),
                ),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(TitleRowGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(weight = 1f, fill = false),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // Bold 15/20 — bodyLegacy is 15/20 Normal by default, so only the weight needs
                // overriding.
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.Bold,
                    color = AmityTheme.token(headerToken(variant, textState)),
                ),
            )
            if (subhead != null) {
                Text(
                    text = subhead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    // Regular 13/18.
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.token(subheadToken(variant, textState)),
                    ),
                )
            }
            titleAccessory?.invoke()
            if (headerIcon != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = headerIcon),
                    contentDescription = null,
                    tint = AmityTheme.token(AmityColorToken.IconListHeaderGeneral),
                    modifier = Modifier.size(HeaderIconSize),
                )
            }
        }

        if (description != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(TitleRowGap),
                verticalAlignment = Alignment.Top,
            ) {
                if (descriptionIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = descriptionIcon),
                        contentDescription = null,
                        tint = AmityTheme.token(AmityColorToken.IconListDescriptionGeneral),
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
                        color = AmityTheme.token(textDescriptionToken(variant, textState)),
                    ),
                )
            }
        }
    }
}

@Composable
private fun TrailingColumn(
    label: String?,
    items: List<AmityListTrailingContent>,
    variant: AmityListItemVariant,
    rowState: ListRowState,
    disabled: Boolean,
    onTrailingPress: ((Int) -> Unit)?,
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(TrailingColumnGap),
    ) {
        if (label != null) {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // Regular 13/18 (state-invariant).
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.token(AmityColorToken.TextListTrailingSubtextDefault),
                ),
            )
        }
        if (items.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(TrailingComponentsGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    TrailingItem(
                        item = item,
                        variant = variant,
                        rowState = rowState,
                        disabled = disabled,
                        onPress = { onTrailingPress?.invoke(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TrailingItem(
    item: AmityListTrailingContent,
    variant: AmityListItemVariant,
    rowState: ListRowState,
    disabled: Boolean,
    onPress: () -> Unit,
) {
    when (item.type) {
        AmityListTrailingType.ICON -> if (item.icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = item.icon),
                contentDescription = null,
                tint = AmityTheme.token(leadingIconToken(variant, rowState)),
                modifier = Modifier
                    .size(TrailingIconSize)
                    .clickableWithoutRipple(enabled = !disabled) { onPress() },
            )
        }

        AmityListTrailingType.CHECKBOX -> AmitySelection(
            variant = AmitySelectionVariant.CHECKBOX,
            isSelected = item.checked,
            isDisabled = disabled,
            icon = item.icon,
            onChange = { _, _ -> onPress() },
        )

        AmityListTrailingType.RADIO -> AmitySelection(
            variant = AmitySelectionVariant.RADIO,
            isSelected = item.checked,
            isDisabled = disabled,
            icon = item.icon,
            onChange = { _, _ -> onPress() },
        )

        AmityListTrailingType.TOGGLE -> AmityToggle(
            isOn = item.toggledOn,
            isDisabled = disabled,
            onChange = { onPress() },
        )

        // Main-Button style (the List atom spec § Geometry — 98×28).
        AmityListTrailingType.BUTTON -> AmityButton(
            variant = AmityButtonVariant.MAIN,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.PRIMARY,
            mainSize = AmityMainButtonSize.SM,
            label = item.label,
            icon = item.icon,
            enabled = !disabled,
            onClick = { onPress() },
        )

        AmityListTrailingType.BADGE -> AmityBadge(
            variant = if (item.badgeLabel != null) AmityBadgeVariant.LABEL else AmityBadgeVariant.ICON,
            label = item.badgeLabel,
            icon = item.badgeIcon,
            shape = AmityBadgeShape.ROUND,
            size = AmityBadgeSize.SIZE_24,
            preset = item.badgePreset,
        )

        // Reaction has no dedicated atom yet (the List atom spec § Composition) — a minimal pill using
        // the existing Reaction/Reactions token families, not a documented component.
        AmityListTrailingType.REACTION -> ReactionPill(count = item.reactionCount ?: 0)

        AmityListTrailingType.TEXT -> if (item.label != null) {
            Text(
                text = item.label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // Regular 15/20 (state-invariant) — bodyLegacy is 15/20 Normal by default.
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.token(AmityColorToken.TextListTrailingTextGeneral),
                ),
            )
        }
    }
}

@Composable
private fun ReactionPill(count: Int) {
    val surface = AmityTheme.token(AmityColorToken.SurfaceReactionsReactionCountDefault)
    val border = AmityTheme.token(AmityColorToken.BorderReactionReactionCountDefault)
    val text = AmityTheme.token(AmityColorToken.TextReactionsChatReactionCountDefault)
    val shape = RoundedCornerShape(ReactionPillRadius)

    Box(
        modifier = Modifier
            .height(ReactionPillHeight)
            .defaultMinSize(minWidth = ReactionPillMinWidth)
            .clip(shape)
            .background(color = surface, shape = shape)
            .border(width = 1.dp, color = border, shape = shape)
            .padding(horizontal = ReactionPillHorizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = count.toString(),
            color = text,
            style = AmityTheme.typography.captionLegacy.copy(fontWeight = FontWeight.Normal),
        )
    }
}

// ---------------------------------------------------------------------------
// State=Skeleton content row — shared shimmer token, not a List-owned skeleton token.
// ---------------------------------------------------------------------------

@Composable
private fun ListItemSkeletonRow() {
    val shimmer = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SkeletonRowHeight),
        horizontalArrangement = Arrangement.spacedBy(MainContentGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(SkeletonAvatarSize)
                .clip(CircleShape)
                .background(shimmer),
        )
        Box(
            modifier = Modifier
                .width(SkeletonBarWidth)
                .height(SkeletonBarHeight)
                .clip(RoundedCornerShape(SkeletonBarRadius))
                .background(shimmer),
        )
    }
}
