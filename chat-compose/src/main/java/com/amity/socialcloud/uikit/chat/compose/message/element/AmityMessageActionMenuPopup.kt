package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage

import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopover
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopoverRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopoverRowSize

/**
 * Transparent gutter around the popover content so [AmityPopover]'s two-layer drop shadow has
 * room to render: a `Popup` window is sized tightly to its content, so a shadow drawn at the
 * surface's own edge would be clipped by the window bounds. The position provider below
 * compensates so the visible surface stays anchored where it would sit without the gutter.
 */
private val AmityMessageActionMenuShadowGutter = 16.dp

@Composable
fun AmityMessageActionMenuPopup(
    show: Boolean,
    message: AmityMessage,
    modifier: Modifier = Modifier,
    action: AmityMessageActionMenuAction = AmityMessageActionMenuAction(),
    isUserMuted: Boolean = false,
    hasReactions: Boolean = false,
    onDismiss: () -> Unit = {},
) {
    if (!show) return

    val isCurrentUser = message.getCreatorId() == AmityCoreClient.getUserId()
    val isSynced = message.getState() == AmityMessage.State.SYNCED

    val density = LocalDensity.current
    // The popup is anchored to the bubble, so anchorBounds.top == bubble top; when there isn't
    // enough room below, the menu goes above the reaction picker, which sits atop the bubble.
    val reactionPickerHeightPx = with(density) { 52.dp.roundToPx() }
    val gapPx = with(density) { 8.dp.roundToPx() }
    val reactionPreviewOffsetPx = if (hasReactions) with(density) { 32.dp.roundToPx() } else 0
    val gutterPx = with(density) { AmityMessageActionMenuShadowGutter.roundToPx() }

    val positionProvider = remember(reactionPickerHeightPx, gapPx, reactionPreviewOffsetPx, gutterPx, isCurrentUser) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val spaceBelow = windowSize.height - anchorBounds.bottom - reactionPreviewOffsetPx
                // popupContentSize includes the transparent shadow gutter on every side; offset by
                // +gutter (x) / −gutter (y) so the visible surface stays end-aligned to the anchor,
                // exactly as it sat before the gutter was added.
                val x = if (isCurrentUser) {
                    anchorBounds.right - popupContentSize.width + gutterPx
                } else {
                    anchorBounds.left - gutterPx
                }
                val isInLower30Percent = anchorBounds.bottom > windowSize.height * 0.7f
                return if (!isInLower30Percent && popupContentSize.height <= spaceBelow) {
                    IntOffset(x, anchorBounds.bottom + reactionPreviewOffsetPx - gutterPx)
                } else {
                    val y = anchorBounds.top - reactionPickerHeightPx - gapPx - popupContentSize.height + gutterPx
                    IntOffset(x, y.coerceAtLeast(0))
                }
            }
        }
    }

    Popup(popupPositionProvider = positionProvider) {
        Box(modifier = Modifier.padding(AmityMessageActionMenuShadowGutter)) {
            AmityPopover(modifier = modifier, width = 160.dp) {
                if (isSynced && !isUserMuted && isCurrentUser && message.getData() is AmityMessage.Data.TEXT) {
                    action.onEdit?.let { onEdit ->
                        AmityPopoverRow(
                            icon = CommonR.drawable.amity_ic_pen_r,
                            label = amityChatString("chat.option.edit"),
                            size = AmityPopoverRowSize.COMPACT,
                            onSelect = {
                                onDismiss()
                                onEdit()
                            },
                        )
                    }
                }

                if (isSynced && !isUserMuted) {
                    action.onReply?.let { onReply ->
                        AmityPopoverRow(
                            icon = CommonR.drawable.amity_ic_share_left_l,
                            label = amityChatString("chat.option.reply"),
                            size = AmityPopoverRowSize.COMPACT,
                            onSelect = {
                                onDismiss()
                                onReply()
                            },
                        )
                    }
                }

                // Copy is available even when muted, unlike the other actions.
                if (message.getData() is AmityMessage.Data.TEXT) {
                    action.onCopy?.let { onCopy ->
                        AmityPopoverRow(
                            icon = CommonR.drawable.amity_ic_copy_r,
                            label = amityChatString("chat.option.copy"),
                            size = AmityPopoverRowSize.COMPACT,
                            onSelect = {
                                onDismiss()
                                onCopy()
                            },
                        )
                    }
                }

                val isMediaMessage = message.getData() is AmityMessage.Data.IMAGE ||
                    message.getData() is AmityMessage.Data.VIDEO
                if (isSynced && !isUserMuted && isMediaMessage) {
                    action.onSave?.let { onSave ->
                        AmityPopoverRow(
                            icon = CommonR.drawable.amity_ic_arrow_down_to_bracket_r,
                            label = amityChatString("chat.action.save"),
                            size = AmityPopoverRowSize.COMPACT,
                            onSelect = {
                                onDismiss()
                                onSave()
                            },
                        )
                    }
                }

                if (isSynced && !isUserMuted && !isCurrentUser) {
                    if (message.isFlaggedByMe()) {
                        action.onUnreport?.let { onUnreport ->
                            AmityPopoverRow(
                                icon = CommonR.drawable.amity_ic_flag_slash_r,
                                label = amityChatString("chat.option.unreport"),
                                size = AmityPopoverRowSize.COMPACT,
                                onSelect = {
                                    onDismiss()
                                    onUnreport()
                                },
                            )
                        }
                    } else {
                        action.onReport?.let { onReport ->
                            AmityPopoverRow(
                                icon = CommonR.drawable.amity_ic_flag_r,
                                label = amityChatString("chat.option.report"),
                                size = AmityPopoverRowSize.COMPACT,
                                onSelect = {
                                    onDismiss()
                                    onReport()
                                },
                            )
                        }
                    }
                }

                // Delete. Who may delete is the caller's policy, not this menu's: own message in a
                // conversation, own message or a moderator's in a group. Callers express that by
                // passing a null callback, so this row must not re-derive it — an ownership check
                // here silently overrode the group's moderator branch.
                action.onDelete?.let { onDelete ->
                    AmityPopoverRow(
                        icon = CommonR.drawable.amity_ic_trash_r,
                        label = amityChatString("chat.option.delete"),
                        size = AmityPopoverRowSize.COMPACT,
                        destructive = true,
                        onSelect = {
                            onDismiss()
                            onDelete()
                        },
                    )
                }
            }
        }
    }
}
