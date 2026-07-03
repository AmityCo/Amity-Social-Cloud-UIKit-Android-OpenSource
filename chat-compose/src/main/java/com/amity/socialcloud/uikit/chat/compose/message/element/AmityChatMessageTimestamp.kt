package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityChatMessageTimestamp(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    isCurrentUser: Boolean,
    onFailedClick: (() -> Unit)? = null,
    isCancelledUpload: Boolean = false,
) {
    val state = message.getState()

    when (state) {
        AmityMessage.State.SYNCED -> {
            Row(
                modifier = modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatMessageTime(message.getCreatedAt()),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 11.sp,
                        color = AmityTheme.colors.baseShade2,
                    ),
                )
            }
        }

        AmityMessage.State.SYNCING,
        AmityMessage.State.CREATED,
        AmityMessage.State.UPLOADING -> {
            Text(
                modifier = modifier,
                text = amityChatString("chat.sending.status"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 11.sp,
                    color = AmityTheme.colors.baseShade2,
                ),
            )
        }

        AmityMessage.State.FAILED -> {
            if (isCancelledUpload) {
                Box(contentAlignment = Alignment.Center, modifier = modifier) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(AmityTheme.colors.baseShade4, CircleShape)
                            .then(
                                if (onFailedClick != null) Modifier.clickable { onFailedClick() }
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_retry),
                            contentDescription = "Retry",
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(AmityTheme.colors.base)
                        )
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = modifier) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_fail_sending_message),
                        contentDescription = "Failed to send",
                        modifier = Modifier
                            .size(36.dp)
                            .then(
                                if (onFailedClick != null) Modifier.clickable { onFailedClick() }
                                else Modifier
                            ),
                        colorFilter = ColorFilter.tint(AmityTheme.colors.baseShade2)
                    )
                }
            }
        }

        else -> {}
    }
}

private fun formatMessageTime(dateTime: org.joda.time.DateTime?): String {
    if (dateTime == null) return ""
    return dateTime.toString("h:mm a")
}
