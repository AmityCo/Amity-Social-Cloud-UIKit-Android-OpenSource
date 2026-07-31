package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

data class AmityMessageActionMenuAction(
    val onReply: (() -> Unit)? = null,
    val onCopy: (() -> Unit)? = null,
    val onEdit: (() -> Unit)? = null,
    val onDelete: (() -> Unit)? = null,
    val onReport: (() -> Unit)? = null,
    val onUnreport: (() -> Unit)? = null,
    val onSave: (() -> Unit)? = null,
)

@Composable
fun AmityMessageActionMenu(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    action: AmityMessageActionMenuAction = AmityMessageActionMenuAction(),
    onDismiss: () -> Unit = {},
) {
    val isCurrentUser = message.getCreatorId() == AmityCoreClient.getUserId()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(AmityTheme.token(AmityColorToken.SurfacePopoverBackgroundDefault))
            .padding(vertical = 8.dp),
    ) {
        // Reply
        action.onReply?.let { onReply ->
            MessageOptionItem(
                icon = CommonR.drawable.amity_ic_share_left_l,
                text = amityChatString("chat.option.reply"),
                onClick = {
                    onReply()
                    onDismiss()
                },
            )
        }

        // Copy (only for text messages)
        if (message.getData() is AmityMessage.Data.TEXT) {
            action.onCopy?.let { onCopy ->
                MessageOptionItem(
                    icon = CommonR.drawable.amity_ic_copy_r,
                    text = amityChatString("chat.option.copy"),
                    onClick = {
                        onCopy()
                        onDismiss()
                    },
                )
            }
        }

        // Save (only for image/video messages)
        action.onSave?.let { onSave ->
            MessageOptionItem(
                icon = CommonR.drawable.amity_ic_arrow_down_to_bracket_r,
                text = amityChatString("chat.action.save"),
                onClick = {
                    onDismiss()
                    onSave()
                },
            )
        }

        // Report (only for other's messages)
        if (!isCurrentUser) {
            action.onReport?.let { onReport ->
                MessageOptionItem(
                    icon = CommonR.drawable.amity_ic_flag_r,
                    text = amityChatString("chat.option.report"),
                    onClick = {
                        onReport()
                        onDismiss()
                    },
                )
            }
        }

        // Delete (only for own messages)
        if (isCurrentUser) {
            action.onDelete?.let { onDelete ->
                AmityDivider(variant = AmityDividerVariant.Content)
                MessageOptionItem(
                    icon = CommonR.drawable.amity_ic_trash_r,
                    text = amityChatString("chat.option.delete"),
                    textColor = AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault),
                    iconTint = AmityTheme.token(AmityColorToken.IconListLeadingDestructiveDefault),
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
private fun MessageOptionItem(
    icon: Int,
    text: String,
    textColor: androidx.compose.ui.graphics.Color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
    iconTint: androidx.compose.ui.graphics.Color = AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault),
    onClick: () -> Unit,
) {
    // Pressed rows fill with the popover-list Hover surface token, replacing the generic
    // Material ripple.
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isPressed) AmityTheme.token(AmityColorToken.SurfacePopoverListsHover)
                else Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconTint,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            ),
        )
    }
}
