package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * AmityReaction — the atomic family behind message reactions: a single reaction disc ([GLYPH]),
 * up to 5 overlapping discs ([GROUP]), a count-bearing pill chip nesting a [GROUP] + a label
 * ([COUNT]), and the long-press quick-react picker row ([POPOVER]). Selected by [variant].
 *
 * Presentational only: formatting the display count, ranking which reactions appear in a
 * [AmityReactionVariant.GROUP]/[AmityReactionVariant.COUNT] list, and resolving a reaction name to
 * its current icon are host/business-logic concerns. Reaction glyphs are supplied by the host as
 * an [AmityReactionItem] (name + drawable) rather than a fixed enum, because the reaction set on
 * this SDK is admin-configurable — a closed reaction-type enum would not be able to render a
 * custom-configured reaction icon.
 *
 * Every owned surface/text/border part binds a colors-v2 semantic token via [AmityTheme.token] —
 * no hardcoded colors. The emoji glyph drawables themselves are fixed illustration assets (or, on
 * this platform, host-configured custom drawables) and are not token-bound.
 *
 * @param variant which of the four pieces to render.
 * @param reaction [GLYPH] only — the glyph to draw. A `null` icon renders the separator-ring +
 * fallback glyph.
 * @param size [GLYPH] only — the disc diameter. [GROUP] fixes discs at 20dp and [POPOVER] items
 * at 32dp (40dp visually on Pressed), so this is ignored for those variants.
 * @param reactions [GROUP] / [COUNT] only — up to 5 reactions to stack, in display order.
 * @param displayCount [COUNT] only — the pre-formatted count label (e.g. "12.5K"); this atom does
 * not format numbers itself.
 * @param countContext [COUNT] only — `CHAT` (supports `active`, self-reacted styling) or `POST`
 * (single General style).
 * @param active [COUNT] only, `countContext = CHAT` — renders the Active (self-reacted) row.
 * @param popoverContext [POPOVER] only — `CHAT` (opaque Filled row) or `LIVE_STREAM_CHAT`
 * (translucent Transparent row).
 * @param items [POPOVER] only — up to 5 reaction items; each optionally `active` (persisted
 * highlight). The transient Pressed/enlarge state is computed internally from the user's drag
 * position across the row, not passed in.
 * @param showMore [POPOVER] only — appends a trailing "More" item that opens the full reaction
 * picker; its tooltip only renders when [moreLabel] is supplied.
 * @param moreLabel [POPOVER] only — the localized label shown in the "More" item's tooltip while
 * pressed; the atom never hardcodes this copy itself.
 * @param onPress [GLYPH] / [GROUP] / [COUNT] — tapped.
 * @param onSelect [POPOVER] only — a reaction item was released on during the drag gesture.
 * @param onMorePress [POPOVER] only — the trailing "More" item was released on.
 * @param onDismiss [POPOVER] only — fired immediately after [onSelect] / [onMorePress] resolves a
 * drag gesture, so the host can close the picker.
 */
@Composable
fun AmityReaction(
    variant: AmityReactionVariant,
    modifier: Modifier = Modifier,
    reaction: AmityReactionItem? = null,
    size: AmityReactionSize = AmityReactionSize.SIZE20,
    reactions: List<AmityReactionItem> = emptyList(),
    displayCount: String? = null,
    countContext: AmityReactionCountContext = AmityReactionCountContext.CHAT,
    active: Boolean = false,
    popoverContext: AmityReactionPopoverContext = AmityReactionPopoverContext.CHAT,
    items: List<AmityReactionPopoverItem> = emptyList(),
    showMore: Boolean = false,
    moreLabel: String? = null,
    onPress: (() -> Unit)? = null,
    onSelect: ((AmityReactionItem) -> Unit)? = null,
    onMorePress: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    when (variant) {
        AmityReactionVariant.GLYPH -> {
            var glyphModifier = modifier
            if (onPress != null) {
                glyphModifier = glyphModifier.clickable { onPress() }
            }
            ReactionGlyph(item = reaction, size = size.dp, modifier = glyphModifier)
        }

        AmityReactionVariant.GROUP -> {
            var groupModifier = modifier
            if (onPress != null) {
                groupModifier = groupModifier.clickable { onPress() }
            }
            ReactionOverlapRow(
                reactions = reactions.take(MaxStackedGlyphs),
                discSize = GroupDiscSize,
                overlapStep = GroupOverlapStep,
                modifier = groupModifier,
            )
        }

        AmityReactionVariant.COUNT -> ReactionCountChip(
            reactions = reactions.take(MaxStackedGlyphs),
            displayCount = displayCount,
            context = countContext,
            active = active,
            modifier = modifier,
            onPress = onPress,
        )

        AmityReactionVariant.POPOVER -> ReactionPopoverRow(
            context = popoverContext,
            items = items.take(MaxStackedGlyphs),
            showMore = showMore,
            moreLabel = moreLabel,
            modifier = modifier,
            onSelect = onSelect,
            onMorePress = onMorePress,
            onDismiss = onDismiss,
        )
    }
}

/** Which of the 4 structurally distinct pieces [AmityReaction] renders. */
enum class AmityReactionVariant { GLYPH, GROUP, COUNT, POPOVER }

/** [AmityReactionVariant.GLYPH]-only disc diameters. */
enum class AmityReactionSize(val dp: Dp) {
    SIZE16(16.dp),
    SIZE20(20.dp),
    SIZE24(24.dp),
    SIZE32(32.dp),
    SIZE40(40.dp),
}

/** [AmityReactionVariant.COUNT]-only context — which token row + label style it binds. */
enum class AmityReactionCountContext { CHAT, POST }

/** [AmityReactionVariant.POPOVER]-only context — which row-surface token it binds. */
enum class AmityReactionPopoverContext { CHAT, LIVE_STREAM_CHAT }

/**
 * A single reaction's display content — a name (used for accessibility + fallback lookups) and
 * its drawable. [icon] is nullable: `null` means "the host could not resolve this reaction name to
 * a known drawable" (e.g. an admin removed a reaction from config after members already reacted
 * with it) and renders the separator-ring + fallback glyph instead.
 */
data class AmityReactionItem(
    val name: String,
    @DrawableRes val icon: Int? = null,
)

/**
 * One item in a [AmityReactionVariant.POPOVER] row.
 *
 * @param reaction the glyph + name to render.
 * @param active persisted highlight — this is the current user's already-applied reaction.
 * @param displayName the localized name shown in the tooltip while this item is being pressed;
 * the atom does not resolve this itself.
 */
data class AmityReactionPopoverItem(
    val reaction: AmityReactionItem,
    val active: Boolean = false,
    val displayName: String = reaction.name,
)

// ---------------------------------------------------------------------------
// Geometry constants
// ---------------------------------------------------------------------------

private const val MaxStackedGlyphs = 5

private val GroupDiscSize = 20.dp
private val GroupOverlapStep = (-8).dp

private val CountChipRadius = 24.dp
private val CountChipBorderWidth = 1.dp
private val CountChipPaddingVertical = 4.dp
private val CountChipPaddingHorizontal = 6.dp
private val CountLabelGap = 2.dp

private val PopoverRowRadius = 32.dp
private val PopoverRowPadding = 8.dp
private val PopoverItemSlotSize = 40.dp
private val PopoverItemRestingIconSize = 32.dp
private val PopoverMoreRestingIconSize = 24.dp
private val PopoverMorePressedIconSize = 32.dp
// The Reaction Name tooltip renders at 16dp here; the atom family's own geometry table states a
// full pill (9999) for this part — an unreconciled mismatch between the two, not assumed away.
private val PopoverTooltipRadius = 16.dp
private val PopoverTooltipMaxWidth = 64.dp
private val PopoverHighlightScale = 1.25f
private val PopoverHighlightRise = (-12).dp

// ---------------------------------------------------------------------------
// glyph — shared by GROUP/COUNT's stacked discs and POPOVER's items
// ---------------------------------------------------------------------------

@Composable
private fun ReactionGlyph(item: AmityReactionItem?, size: Dp, modifier: Modifier = Modifier) {
    if (item?.icon == null) {
        Box(
            modifier = modifier
                .size(size)
                .background(
                    color = AmityTheme.token(AmityColorToken.BorderReactionReactionAtomDefault),
                    shape = CircleShape,
                ),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_message_reaction_missing),
                contentDescription = item?.name,
                modifier = Modifier.size(size),
            )
        }
    } else {
        Image(
            imageVector = ImageVector.vectorResource(id = item.icon),
            contentDescription = item.name,
            modifier = modifier.size(size),
        )
    }
}

/**
 * Up to 5 [discSize] glyphs overlapping at [overlapStep] (a negative offset), optionally followed
 * by [trailing] content (the [COUNT] variant's label) shifted left by the same total overlap so it
 * sits flush against the last disc instead of leaving a gap sized for the discs' un-overlapped
 * width.
 */
@Composable
private fun ReactionOverlapRow(
    reactions: List<AmityReactionItem>,
    discSize: Dp,
    overlapStep: Dp,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    val overlapCount = (reactions.size - 1).coerceAtLeast(0)
    val totalOverlap = overlapStep * overlapCount

    Layout(
        modifier = modifier,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                reactions.forEachIndexed { index, item ->
                    ReactionGlyph(
                        item = item,
                        size = discSize,
                        modifier = Modifier
                            .offset(x = overlapStep * index)
                            .zIndex(1f - (0.1f * index)),
                    )
                }
                if (trailing != null) {
                    Spacer(modifier = Modifier.width(CountLabelGap))
                    Box(modifier = Modifier.offset(x = totalOverlap)) {
                        trailing()
                    }
                }
            }
        },
    ) { measurables, constraints ->
        val placeable = measurables.single().measure(constraints)
        val width = (placeable.width + totalOverlap.roundToPx()).coerceAtLeast(0)
        layout(width, placeable.height) {
            placeable.placeRelative(IntOffset.Zero)
        }
    }
}

// ---------------------------------------------------------------------------
// count — a group + a numeric label inside a pill chip
// ---------------------------------------------------------------------------

@Composable
private fun ReactionCountChip(
    reactions: List<AmityReactionItem>,
    displayCount: String?,
    context: AmityReactionCountContext,
    active: Boolean,
    modifier: Modifier = Modifier,
    onPress: (() -> Unit)?,
) {
    val shape = RoundedCornerShape(CountChipRadius)
    val surfaceColor = AmityTheme.token(countSurfaceToken(context, active))
    val borderColor = AmityTheme.token(countBorderToken(context, active))
    val labelColor = AmityTheme.token(countLabelToken(context, active))

    var chipModifier = modifier
        .wrapContentWidth()
        .border(width = CountChipBorderWidth, color = borderColor, shape = shape)
        .background(color = surfaceColor, shape = shape)
    if (onPress != null) {
        chipModifier = chipModifier.clickable { onPress() }
    }
    chipModifier = chipModifier.padding(
        vertical = CountChipPaddingVertical,
        horizontal = CountChipPaddingHorizontal,
    )

    Box(modifier = chipModifier) {
        ReactionOverlapRow(
            reactions = reactions,
            discSize = GroupDiscSize,
            overlapStep = GroupOverlapStep,
            trailing = displayCount?.let { count ->
                {
                    Text(
                        text = count,
                        style = countLabelStyle(context).copy(color = labelColor, textAlign = TextAlign.Center),
                        modifier = Modifier.wrapContentWidth(),
                    )
                }
            },
        )
    }
}

private fun countSurfaceToken(context: AmityReactionCountContext, active: Boolean): AmityColorToken = when (context) {
    // No dedicated Post-context surface token ships in the registry; Post reuses the Chat resting
    // surface (Post has no confirmed Active row either).
    AmityReactionCountContext.POST -> AmityColorToken.SurfaceReactionsReactionCountDefault
    AmityReactionCountContext.CHAT -> if (active) {
        AmityColorToken.SurfaceReactionsReactionCountActive
    } else {
        AmityColorToken.SurfaceReactionsReactionCountDefault
    }
}

private fun countBorderToken(context: AmityReactionCountContext, active: Boolean): AmityColorToken = when (context) {
    AmityReactionCountContext.POST -> AmityColorToken.BorderReactionReactionCountDefault
    AmityReactionCountContext.CHAT -> if (active) {
        AmityColorToken.BorderReactionReactionCountActive
    } else {
        AmityColorToken.BorderReactionReactionCountDefault
    }
}

private fun countLabelToken(context: AmityReactionCountContext, active: Boolean): AmityColorToken = when (context) {
    AmityReactionCountContext.POST -> AmityColorToken.TextReactionsPostReactionCountGeneral
    AmityReactionCountContext.CHAT -> if (active) {
        AmityColorToken.TextReactionsChatReactionCountActive
    } else {
        AmityColorToken.TextReactionsChatReactionCountDefault
    }
}

private fun countLabelStyle(context: AmityReactionCountContext): TextStyle = when (context) {
    AmityReactionCountContext.CHAT -> TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(590),
        letterSpacing = (-0.4).sp,
    )
    AmityReactionCountContext.POST -> TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal)
}

// ---------------------------------------------------------------------------
// popover — the long-press quick-react row, its own drag-to-highlight gesture
// ---------------------------------------------------------------------------

private sealed interface PopoverSlot {
    data class Reaction(val item: AmityReactionPopoverItem) : PopoverSlot
    data class More(val label: String?) : PopoverSlot
}

@Composable
private fun ReactionPopoverRow(
    context: AmityReactionPopoverContext,
    items: List<AmityReactionPopoverItem>,
    showMore: Boolean,
    moreLabel: String?,
    modifier: Modifier = Modifier,
    onSelect: ((AmityReactionItem) -> Unit)?,
    onMorePress: (() -> Unit)?,
    onDismiss: (() -> Unit)?,
) {
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current

    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var lastHapticIndex by remember { mutableStateOf<Int?>(null) }
    var rowWidthPx by remember { mutableStateOf(0) }

    val slots = remember(items, showMore, moreLabel) {
        items.map { PopoverSlot.Reaction(it) } + if (showMore) listOf(PopoverSlot.More(moreLabel)) else emptyList()
    }

    val rowSurfaceToken = when (context) {
        AmityReactionPopoverContext.CHAT -> AmityColorToken.SurfaceReactionsReactionPopoverFilledDefault
        AmityReactionPopoverContext.LIVE_STREAM_CHAT -> AmityColorToken.SurfaceReactionsReactionPopoverTransparentDefault
    }
    val rowSurfaceColor = AmityTheme.token(rowSurfaceToken)
    val shape = RoundedCornerShape(PopoverRowRadius)

    Row(
        modifier = modifier
            .shadow(shape = shape, elevation = 4.dp, clip = false)
            .background(color = rowSurfaceColor, shape = shape)
            .graphicsLayer { clip = false }
            .padding(PopoverRowPadding)
            .onGloballyPositioned { coordinates -> rowWidthPx = coordinates.size.width }
            .pointerInput(slots.size) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    highlightedIndex = popoverSlotIndexForX(down.position.x, rowWidthPx, slots.size)

                    drag(down.id) { change ->
                        change.consume()
                        val idx = popoverSlotIndexForX(change.position.x, rowWidthPx, slots.size)
                        if (idx != highlightedIndex) {
                            highlightedIndex = idx
                            if (idx != null && lastHapticIndex != idx) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                lastHapticIndex = idx
                            }
                        }
                    }

                    val chosen = highlightedIndex
                    highlightedIndex = null
                    lastHapticIndex = null
                    if (chosen != null) {
                        when (val slot = slots[chosen]) {
                            is PopoverSlot.Reaction -> onSelect?.invoke(slot.item.reaction)
                            is PopoverSlot.More -> onMorePress?.invoke()
                        }
                        onDismiss?.invoke()
                    }
                }
            },
    ) {
        slots.forEachIndexed { index, slot ->
            PopoverItemSlot(
                slot = slot,
                isHighlighted = highlightedIndex == index,
                density = density,
            )
        }
    }
}

@Composable
private fun PopoverItemSlot(slot: PopoverSlot, isHighlighted: Boolean, density: Density) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) PopoverHighlightScale else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "reactionScale",
    )
    val yOffset by animateDpAsState(
        targetValue = if (isHighlighted) PopoverHighlightRise else 0.dp,
        label = "reactionYOffset",
    )
    val tooltipLabel = when (slot) {
        is PopoverSlot.Reaction -> slot.item.displayName
        is PopoverSlot.More -> slot.label
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(PopoverItemSlotSize)) {
        if (isHighlighted && !tooltipLabel.isNullOrEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = with(density) { yOffset.toPx() - PopoverItemSlotSize.toPx() }
                    }
                    .background(
                        color = AmityTheme.token(AmityColorToken.SurfaceReactionsReactionPopoverReactionNameActive),
                        shape = RoundedCornerShape(PopoverTooltipRadius),
                    )
                    .padding(vertical = 2.dp, horizontal = 6.dp)
                    .widthIn(min = 0.dp, max = PopoverTooltipMaxWidth),
            ) {
                Text(
                    text = tooltipLabel,
                    color = AmityTheme.token(AmityColorToken.TextReactionsReactionPopoverReactionNameGeneral),
                    style = AmityTheme.typography.captionSmall.copy(lineHeight = 13.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        when (slot) {
            is PopoverSlot.Reaction -> Box(
                modifier = Modifier
                    .background(
                        // The ReactionState/Active disc shows ONLY for the self-reacted item —
                        // product decision: the pressed/lifted item scales up without the disc.
                        color = if (slot.item.active) {
                            AmityTheme.token(AmityColorToken.SurfaceReactionsReactionPopoverReactionStateActive)
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape,
                    )
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationY = with(density) { yOffset.toPx() }
                    },
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(
                        id = slot.item.reaction.icon ?: R.drawable.amity_ic_message_reaction_missing,
                    ),
                    contentDescription = slot.item.reaction.name,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(PopoverItemRestingIconSize),
                )
            }

            is PopoverSlot.More -> Box(
                // The "More" item's resting fill (an atomic-tier neutral shade) has no colors-v2
                // semantic token registered on this platform yet — left unfilled rather than
                // approximated with an unrelated token; only the icon tint below is bound.
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationY = with(density) { yOffset.toPx() }
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_plus_r),
                    contentDescription = null,
                    tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isHighlighted) PopoverMorePressedIconSize else PopoverMoreRestingIconSize),
                )
            }
        }
    }
}

private fun popoverSlotIndexForX(x: Float, rowWidthPx: Int, slotCount: Int): Int? {
    if (slotCount <= 0 || x < 0 || x > rowWidthPx) {
        return null
    }
    val percent = if (rowWidthPx > 0) (x / rowWidthPx).coerceIn(0f, 1f) else 0f
    return (percent * slotCount).toInt().coerceIn(0, slotCount - 1)
}
