package com.amity.socialcloud.uikit.chat.compose.conversation

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.config.AmityChatConfigHelper
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.R as CommonR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityConversationChatUserActionSheet(
    isMuted: Boolean,
    isUserReported: Boolean,
    isUserBlocked: Boolean,
    onMuteToggle: () -> Unit,
    onReportToggle: () -> Unit,
    onBlockToggle: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = AmityTheme.colors.background,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .padding(bottom = 0.dp)
                        .then(
                            Modifier
                                .size(width = 36.dp, height = 4.dp)
                        ),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
        ) {
            // Mute / Unmute
            if (AmityChatConfigHelper.isConversationUserActionEnabled("mute")) {
                ActionSheetItem(
                    iconResId = if (isMuted) R.drawable.amity_ic_chat_notification_on
                    else R.drawable.amity_ic_chat_notification_off,
                    text = amityChatString(
                        if (isMuted) "chat.action.turn.on.notification"
                        else "chat.action.turn.off.notification"
                    ),
                    onClick = {
                        onDismiss()
                        onMuteToggle()
                    },
                )
            }

            // Report / Unreport User
            if (AmityChatConfigHelper.isConversationUserActionEnabled("report")) {
                ActionSheetItem(
                    iconResId = if (isUserReported) R.drawable.amity_ic_unreport
                    else R.drawable.amity_ic_flag_message,
                    text = amityChatString(
                        key = if (isUserReported) "chat.action.unreport.user"
                        else "chat.action.report.user"
                    ),
                    onClick = {
                        onDismiss()
                        onReportToggle()
                    },
                )
            }

            // Block / Unblock User
            if (AmityChatConfigHelper.isConversationUserActionEnabled("block")) {
                ActionSheetItem(
                    iconResId = R.drawable.amity_ic_chat_unblock_user,
                    text = amityChatString(
                        key = if (isUserBlocked) "chat.action.unblock.user"
                        else "chat.action.block.user"
                    ),
                    onClick = {
                        onDismiss()
                        onBlockToggle()
                    },
                )
            }
        }
    }
}

@Composable
private fun ActionSheetItem(
    iconResId: Int,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = AmityTheme.colors.base,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = AmityTheme.colors.base,
        )
    }
}
