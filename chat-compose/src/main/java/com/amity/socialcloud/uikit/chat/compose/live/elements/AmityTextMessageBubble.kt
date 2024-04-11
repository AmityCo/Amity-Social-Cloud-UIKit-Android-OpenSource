package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.util.getContent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAnnotatedText
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.copyText
import com.google.gson.JsonObject

@Composable
fun AmityLiveChatMessageSenderView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	viewModel: AmityLiveChatPageViewModel,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	AmityBaseElement(elementId = "message_bubble_sender_text") {
		val parentId = message.getParentId()
		if (parentId == null || message.isDeleted()) {
			MessageSenderBubble(
				message = message,
				pageScope = pageScope,
				componentScope = componentScope,
				onMessageAction = onMessageAction
			)
		} else {
			ReplyMessageSenderView(
				message = message,
				pageScope = pageScope,
				componentScope = componentScope,
				viewModel = viewModel,
				parentId = parentId,
				onMessageAction = onMessageAction,
			)
		}
	}
}

@Composable
fun AmityLiveChatMessageReceiverView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	viewModel: AmityLiveChatPageViewModel,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	AmityBaseElement(elementId = "message_bubble_receiver_text") {
		val parentId = message.getParentId()
		if (parentId == null || message.isDeleted()) {
			MessageReceiverView(
				message = message,
				pageScope = pageScope,
				componentScope = componentScope,
				onMessageAction = onMessageAction
			)
		} else {
			ReplyMessageReceiverView(
				message = message,
				pageScope = pageScope,
				componentScope = componentScope,
				viewModel = viewModel,
				parentId = parentId,
				onMessageAction = onMessageAction,
			)
		}
	}
}

@Composable
fun MessageReceiverView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	BaseMessageBubble(
		modifier = modifier,
		pageScope = pageScope,
		componentScope = componentScope,
		message = message,
		onMessageAction = onMessageAction,
	)
}

@Composable
fun MessageSenderBubble(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
){
	BaseMessageBubble(
		modifier = modifier.testTag("message_list/message_bubble_sender_text"),
		pageScope = pageScope,
		componentScope = componentScope,
		message = message,
		onMessageAction = onMessageAction,
	)
}

@Composable
fun BaseMessageBubble(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	onMessageAction: AmityMessageAction,
) {
	var menuExpanded by remember { mutableStateOf(false) }
	AmityBaseElement(elementId = "message_bubble") {
		Row(
			modifier = modifier
				.fillMaxWidth()
				.padding(16.dp, 8.dp, 16.dp, 0.dp),
			horizontalArrangement = Arrangement.Start
		) {
			AmityMessageAvatarView(
				pageScope = pageScope,
				avatarUrl = message.getCreator()?.getAvatar()?.getUrl() ?: "",
				size = 32.dp
			)
			Spacer(modifier = Modifier.width(8.dp))
			Column(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalAlignment = Alignment.Start
			) {
				Text(
					text = message.getCreator()?.getDisplayName() ?: "Unknown",
					fontSize = 13.sp,
					lineHeight = 18.sp,
					fontWeight = FontWeight(600),
					color = AmityTheme.colors.baseShade3,
					modifier = Modifier.align(Alignment.Start),
				)
				Spacer(modifier = Modifier.height(4.dp))
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.Bottom,
				) {
					Surface(
						shape = if (message.isDeleted()) {
							RoundedCornerShape(12.dp)
						} else {
							RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
						},
						color = AmityTheme.colors.baseShade5,
						modifier = Modifier
							.weight(1.0f, fill = false)
							.wrapContentWidth(align = Alignment.Start)
							.pointerInput(Unit) {
								detectTapGestures(
									onLongPress = { offset ->
										menuExpanded = true
									}
								)
							}
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
						) {
							if (message.isDeleted()) {
								DeletedMessageContent(message = message)
							} else {
								TextMessageContent(message = message)
							}
						}
					}
					AmityMessageBubbleTimestamp(pageScope = pageScope, componentScope = componentScope, message = message)
				}
				if (!message.isDeleted()) {
					Box(modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)) {
						AmityMessageOption(
							pageScope = pageScope,
							componentScope = componentScope,
							show = menuExpanded,
							onMessageAction = onMessageAction,
							modifier = Modifier.width(243.dp),
						) { menuExpanded = false }
					}
				}
			}
		}
	}
}

@Composable
fun TextMessageContent(
	message: AmityMessage,
	modifier: Modifier = Modifier,
) {
	AmityAnnotatedText(
		text = getContent(message),
		mentionGetter = AmityMentionMetadataGetter(message.getMetadata() ?: JsonObject()),
		mentionees = message.getMentionees(),
		style = TextStyle(
			fontSize = 15.sp,
			lineHeight = 20.sp,
			fontWeight = FontWeight(400),
			color = AmityTheme.colors.baseInverse,
		),
		modifier = modifier
			.padding(10.dp, 8.dp)
			.testTag(getTextMessageContentTestTag(message = message))
	)
}

@Composable
fun DeletedMessageContent(message: AmityMessage) {
	Spacer(modifier = Modifier.width(8.dp))
	Icon(
		imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
		contentDescription = "deleted message icon",
		tint = AmityTheme.colors.baseInverse,
		modifier = Modifier.size(16.dp)
	)
	Text(
		text = getContent(message),
		style = TextStyle(
			fontSize = 15.sp,
			lineHeight = 20.sp,
			fontWeight = FontWeight(400),
			color = AmityTheme.colors.baseInverse,
		),
		modifier = Modifier
			.padding(4.dp, 5.dp, 8.dp, 5.dp)
			.testTag(getTextMessageContentTestTag(message = message))
	)
}

fun getTextMessageContentTestTag(message: AmityMessage): String {
	return if (message.getCreatorId() == AmityCoreClient.getUserId()) {
		"message_list/message_bubble_sender_text_text_view"
	} else {
		"message_list/message_bubble_receiver_text_text_view"
	}
}

fun getTextMessageBubbleTestTag(message: AmityMessage): String {
	return if (message.getCreatorId() == AmityCoreClient.getUserId()) {
		"message_list/message_bubble_sender_text"
	} else {
		"message_list/message_bubble_receiver_text"
	}
}