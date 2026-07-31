package com.amity.socialcloud.uikit.chat.compose.conversation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.amity.socialcloud.uikit.chat.compose.config.AmityChatConfigHelper
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.compose.R as CommonR
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

    AmitySheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
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
                    iconResId = if (isMuted) CommonR.drawable.amity_ic_bell_r
                    else CommonR.drawable.amity_ic_bell_slash_r,
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
                    iconResId = if (isUserReported) CommonR.drawable.amity_ic_flag_slash_r
                    else CommonR.drawable.amity_ic_flag_r,
                    // DM-scoped labels, keyed to match iOS. The chat.action.* pair is shared with
                    // the group member list and reads "member" there, meaningless in a 1:1.
                    text = amityChatString(
                        key = if (isUserReported) "chat.dm.action.unreport.user"
                        else "chat.dm.action.report.user"
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
                    iconResId = CommonR.drawable.amity_ic_user_slash_r,
                    text = amityChatString(
                        key = if (isUserBlocked) "chat.action.unblock.user"
                        else "chat.action.block.user"
                    ),
                    // Block is intentionally NON-destructive (neutral, not red)
                    isDestructive = false,
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
    isDestructive: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (isDestructive) AmityTheme.token(AmityColorToken.IconListLeadingDestructiveDefault)
            else AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = if (isDestructive) AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault)
            else AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
        )
    }
}
