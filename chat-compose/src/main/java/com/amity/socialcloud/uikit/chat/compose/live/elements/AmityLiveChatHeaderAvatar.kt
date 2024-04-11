package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityLiveChatHeaderAvatar(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	channel: AmityChannel?
) {
	AmityBaseElement(elementId = "avatar") {
		AmityMessageAvatarView(
			pageScope = pageScope,
			componentScope = componentScope,
			avatarUrl = channel?.getAvatar()?.getUrl() ?: "",
			size = 40.dp,
			placeholder = painterResource(id = R.drawable.amity_ic_chat_placeholder),
			placeHolderTint = null,
		)
	}
}