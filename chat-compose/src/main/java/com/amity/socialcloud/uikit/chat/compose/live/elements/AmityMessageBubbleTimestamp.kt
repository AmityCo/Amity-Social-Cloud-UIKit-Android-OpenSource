package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityMessageBubbleTimestamp(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	onDelete: (() -> Unit)?,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "message_bubble_timestamp"
	) {
		if (!message.isDeleted()) {
			Row(modifier = modifier) {
				if (message.getState() == AmityMessage.State.SYNCED) {
					Spacer(modifier = Modifier.width(6.dp))
					Text(
						text = message.getCreatedAt().toString("h:mm a"),
						fontSize = 9.sp,
						lineHeight = 12.sp,
						fontWeight = FontWeight(400),
						color = AmityTheme.colors.secondaryShade2,
						modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp),
					)
				} else if (message.getState() == AmityMessage.State.SYNCING
					|| message.getState() == AmityMessage.State.CREATED
					|| message.getState() == AmityMessage.State.UPLOADING
				) {
					Spacer(modifier = Modifier.width(6.dp))
					Text(
						text = "sending...",
						fontSize = 9.sp,
						lineHeight = 12.sp,
						fontWeight = FontWeight(400),
						color = AmityTheme.colors.secondaryShade2,
						modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp),
					)
				} else if (message.getState() == AmityMessage.State.FAILED) {
					Image(
						painter = painterResource(id = R.drawable.amity_ic_fail_sending_message),
						contentDescription = "Failed to send message icon",
						modifier = Modifier
							.size(34.dp)
							.clickable {
							   onDelete?.invoke()
							},
					)
				}
			}
		}
	}
}