package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

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
            .background(AmityTheme.colors.background)
            .padding(vertical = 8.dp),
    ) {
        // Reply
        action.onReply?.let { onReply ->
            MessageOptionItem(
                icon = R.drawable.amity_ic_reply_message,
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
                    icon = R.drawable.amity_ic_copy_message,
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
                icon = R.drawable.amity_ic_save_image,
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
                    icon = R.drawable.amity_ic_flag_message,
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
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = AmityTheme.colors.baseShade4,
                )
                MessageOptionItem(
                    icon = R.drawable.amity_ic_delete_message,
                    text = amityChatString("chat.option.delete"),
                    textColor = AmityTheme.colors.alert,
                    iconTint = AmityTheme.colors.alert,
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
    textColor: androidx.compose.ui.graphics.Color = AmityTheme.colors.baseInverse,
    iconTint: androidx.compose.ui.graphics.Color = AmityTheme.colors.base,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
