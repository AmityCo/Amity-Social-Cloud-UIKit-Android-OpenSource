package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityMessageBubbleFlag(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "message_bubble_flag"
	) {
		if (!message.isDeleted() && message.getState() == AmityMessage.State.SYNCED && message.getFlagCount() > 0 && message.isFlaggedByMe()) {
			Row(
				modifier = modifier
					.width(20.dp)
					.height(34.dp)
					.padding(start = 6.dp, top = 10.dp, bottom = 4.dp)
			
			) {
				Image(
					painter = painterResource(id = R.drawable.amity_ic_flag_message),
					contentDescription = "Message reported icon",
					modifier = Modifier
						.width(20.dp)
						.height(20.dp)
						.padding(top = 2.dp, bottom = 2.dp),
				)
			}
		}
	}
}