package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import androidx.compose.material3.ripple

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
    // Height of the reaction picker row (emoji buttons + padding) + gap, in pixels.
    // The popup is anchored to the bubble Box, so anchorBounds.top == bubble top.
    // When there is not enough room below, we push the menu above the reaction picker
    // which sits directly above the bubble top.
    val reactionPickerHeightPx = with(density) { 52.dp.roundToPx() }
    val gapPx = with(density) { 8.dp.roundToPx() }
    val reactionPreviewOffsetPx = if (hasReactions) with(density) { 32.dp.roundToPx() } else 0

    val positionProvider = remember(reactionPickerHeightPx, gapPx, reactionPreviewOffsetPx, isCurrentUser) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val spaceBelow = windowSize.height - anchorBounds.bottom - reactionPreviewOffsetPx
                val x = if (isCurrentUser) {
                    anchorBounds.right - popupContentSize.width
                } else {
                    anchorBounds.left
                }
                // Show above if bubble is in the lower 30% of the screen or not enough space below
                val isInLower30Percent = anchorBounds.bottom > windowSize.height * 0.7f
                return if (!isInLower30Percent && popupContentSize.height <= spaceBelow) {
                    // Enough space below the bubble: show below reaction preview
                    IntOffset(x, anchorBounds.bottom + reactionPreviewOffsetPx)
                } else {
                    // Show above the reaction picker
                    val y = anchorBounds.top - reactionPickerHeightPx - gapPx - popupContentSize.height
                    IntOffset(x, y.coerceAtLeast(0))
                }
            }
        }
    }

    Popup(popupPositionProvider = positionProvider) {
        Column(
            modifier = modifier
                .widthIn(min = 160.dp)
                .shadow(
                    shape = RoundedCornerShape(16.dp),
                    elevation = 8.dp,
                    clip = true,
                )
                .clip(RoundedCornerShape(16.dp))
                .background(color = AmityTheme.colors.background)
                .padding(8.dp)
        ) {
            // Edit (only for own synced text messages, hidden when muted)
            if (isSynced && !isUserMuted && isCurrentUser && message.getData() is AmityMessage.Data.TEXT) {
                action.onEdit?.let { onEdit ->
                    OptionPopupItem(
                        icon = ImageVector.vectorResource(id = R.drawable.amity_ic_edit_profile),
                        text = amityChatString("chat.option.edit"),
                        tint = AmityTheme.colors.baseInverse,
                        onDismiss = onDismiss,
                        action = onEdit,
                    )
                }
            }

            // Reply (only for synced messages, hidden when muted)
            if (isSynced && !isUserMuted) {
                action.onReply?.let { onReply ->
                    OptionPopupItem(
                        icon = ImageVector.vectorResource(id = R.drawable.amity_ic_reply_message),
                        text = amityChatString("chat.option.reply"),
                        tint = AmityTheme.colors.baseInverse,
                        onDismiss = onDismiss,
                        action = onReply,
                    )
                }
            }

            // Copy (only for text messages — always available, even when muted)
            if (message.getData() is AmityMessage.Data.TEXT) {
                action.onCopy?.let { onCopy ->
                    OptionPopupItem(
                        icon = ImageVector.vectorResource(id = R.drawable.amity_ic_copy_message),
                        text = amityChatString("chat.option.copy"),
                        tint = AmityTheme.colors.baseInverse,
                        onDismiss = onDismiss,
                        action = onCopy,
                    )
                }
            }

            // Save (only for synced image/video messages, hidden when muted)
            if (isSynced && !isUserMuted && (message.getData() is AmityMessage.Data.IMAGE || message.getData() is AmityMessage.Data.VIDEO)) {
                action.onSave?.let { onSave ->
                    OptionPopupItem(
                        icon = ImageVector.vectorResource(id = R.drawable.amity_ic_save_image),
                        text = amityChatString("chat.action.save"),
                        tint = AmityTheme.colors.baseInverse,
                        onDismiss = onDismiss,
                        action = onSave,
                    )
                }
            }

            // Report / Unreport (only for other's synced messages, hidden when muted)
            if (isSynced && !isUserMuted && !isCurrentUser) {
                if (message.isFlaggedByMe()) {
                    action.onUnreport?.let { onUnreport ->
                        OptionPopupItem(
                            icon = ImageVector.vectorResource(id = R.drawable.amity_ic_unreport),
                            text = amityChatString("chat.option.unreport"),
                            tint = AmityTheme.colors.baseInverse,
                            onDismiss = onDismiss,
                            action = onUnreport,
                        )
                    }
                } else {
                    action.onReport?.let { onReport ->
                        OptionPopupItem(
                            icon = ImageVector.vectorResource(id = R.drawable.amity_ic_flag_message),
                            text = amityChatString("chat.option.report"),
                            tint = AmityTheme.colors.baseInverse,
                            onDismiss = onDismiss,
                            action = onReport,
                        )
                    }
                }
            }

            if (isCurrentUser) {
                // Delete (only for own messages, or moderator — always available, even when muted)
                action.onDelete?.let { onDelete ->
                    OptionPopupItem(
                        icon = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
                        text = amityChatString("chat.option.delete"),
                        tint = AmityTheme.colors.alert,
                        onDismiss = onDismiss,
                        action = onDelete,
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionPopupItem(
    icon: ImageVector,
    text: String,
    tint: Color = AmityTheme.typography.bodyLegacy.color,
    onDismiss: () -> Unit,
    action: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .widthIn(144.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                indication = ripple(color = AmityTheme.colors.baseShade2),
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onDismiss()
                action()
            }
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = AmityTheme.typography.body,
            color = tint,
        )
    }
}
