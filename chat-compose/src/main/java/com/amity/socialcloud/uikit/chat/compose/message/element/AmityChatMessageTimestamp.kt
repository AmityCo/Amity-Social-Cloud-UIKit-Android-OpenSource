package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.text.format.DateFormat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityTimestamp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

@Composable
fun AmityChatMessageTimestamp(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    isCurrentUser: Boolean,
    onFailedClick: (() -> Unit)? = null,
    isCancelledUpload: Boolean = false,
) {
    val state = message.getState()
    val context = LocalContext.current

    when (state) {
        AmityMessage.State.SYNCED -> {
            AmityTimestamp(
                text = formatMessageTime(message.getCreatedAt(), context),
                modifier = modifier.wrapContentWidth(),
            )
        }

        AmityMessage.State.SYNCING,
        AmityMessage.State.CREATED,
        AmityMessage.State.UPLOADING -> {
            AmityTimestamp(
                text = amityChatString("chat.sending.status"),
                modifier = modifier,
                color = AmityTheme.token(AmityColorToken.TextChatBubbleTimestampSendingDefault),
            )
        }

        AmityMessage.State.FAILED -> {
            Box(contentAlignment = Alignment.Center, modifier = modifier) {
                if (isCancelledUpload) {
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.FILLED,
                        hierarchy = AmityButtonHierarchy.SECONDARY,
                        iconSize = AmityIconButtonSize.SIZE24,
                        icon = CommonR.drawable.amity_ic_redo_r,
                        contentDescription = "Retry",
                        enabled = onFailedClick != null,
                        onClick = { onFailedClick?.invoke() },
                    )
                } else {
                    // A genuine send failure (not a cancelled upload) shows an exclamation mark
                    // rather than a retry affordance — the SDK's AmityMessage.State carries no
                    // failure-reason, so retry is only offered where the user explicitly cancelled.
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.TRANSPARENT,
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        iconSize = AmityIconButtonSize.SIZE24,
                        icon = CommonR.drawable.amity_ic_exclamation_s,
                        contentDescription = amityChatString("chat.status.failed"),
                        enabled = onFailedClick != null,
                        onClick = { onFailedClick?.invoke() },
                    )
                }
            }
        }

        else -> {}
    }
}

private fun formatMessageTime(dateTime: org.joda.time.DateTime?, context: android.content.Context): String {
    if (dateTime == null) return ""
    val pattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "h:mm a"
    return dateTime.toString(pattern)
}
