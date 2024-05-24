package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityMessageQuickReaction(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	onMessageAction: AmityMessageAction,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "message_quick_reaction"
	) {
		val quickReactionName = getElementScope().getConfig().get("reaction")?.asString
		val isQuickReactionExists = AmityMessageReactions.getList().any { it.name == quickReactionName }
		if (
			quickReactionName != null
			&& isQuickReactionExists
			&& !message.isDeleted()
			&& !message.isFlaggedByMe()
			&& message.getState() == AmityMessage.State.SYNCED
			&& message.getMyReactions().isEmpty()
		) {
			Row(
				modifier = modifier
					.padding(top = 10.dp, bottom = 4.dp)
					.clickable {
						onMessageAction.onAddReaction.invoke(message, quickReactionName)
					}
			
			) {
				Image(
					painter = painterResource(id = R.drawable.amity_ic_message_quick_reaction),
					contentDescription = "Message quick reaction icon",
					modifier = Modifier
						.width(20.dp)
						.height(20.dp),
				)
			}
		}
	}
}