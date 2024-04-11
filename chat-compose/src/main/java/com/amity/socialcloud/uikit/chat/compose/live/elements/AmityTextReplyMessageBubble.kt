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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.google.gson.JsonObject

@Composable
fun ReplyMessageSenderView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	parentId: String,
	viewModel: AmityLiveChatPageViewModel,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	BaseReplyMessage(
		modifier = modifier,
		pageScope = pageScope,
		componentScope = componentScope,
		message = message,
		parentId = parentId,
		viewModel = viewModel,
		onMessageAction = onMessageAction,
	)
}

@Composable
fun ReplyMessageReceiverView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	parentId: String,
	viewModel: AmityLiveChatPageViewModel,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	BaseReplyMessage(
		modifier = modifier,
		pageScope = pageScope,
		componentScope = componentScope,
		message = message,
		parentId = parentId,
		viewModel = viewModel,
		onMessageAction = onMessageAction,
	)
}

@Composable
fun BaseReplyMessage(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	parentId: String,
	viewModel: AmityLiveChatPageViewModel,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
	val parentMessage by viewModel.getMessage(parentId).collectAsState(initial = null)
	var menuExpanded by remember { mutableStateOf(false) }
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
				.fillMaxWidth()
				.pointerInput(Unit) {
					detectTapGestures(
						onLongPress = { offset ->
							menuExpanded = true
						}
					)
				},
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
				verticalAlignment = Alignment.Bottom
			) {
				Column(
					modifier = Modifier.weight(1.0f),
				) {
					Surface(
						shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
						color = AmityTheme.colors.baseShade6,
						modifier = Modifier.fillMaxWidth()
							.testTag(getReplyTextMessageBubbleTestTag(message))
					) {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(12.dp)
						) {
							parentMessage
								?.let { ParentTextMessage(it) }
								?: Box {
									ParentMessageLoading()
								}
						}
					}
					Surface(
						shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp),
						color = AmityTheme.colors.baseShade5,
						modifier = Modifier.fillMaxWidth(),
					) {
						Box(
							modifier = Modifier
								.padding(12.dp, 8.dp)
								.fillMaxWidth(),
						){
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
								modifier = Modifier.testTag(
									getTextMessageContentTestTag(message = message)
								)
							)
						}
						
					}
				}
				AmityMessageBubbleTimestamp(pageScope = pageScope, componentScope = componentScope, message = message)
			}
			Box (modifier = Modifier.padding(0.dp,8.dp,0.dp,0.dp)) {
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

@Composable
fun ParentTextMessage(message: AmityMessage) {
	Column {
		Text(
			text = message.getCreator()?.getDisplayName() ?: "Unknown",
			fontSize = 13.sp,
			lineHeight = 18.sp,
			fontWeight = FontWeight(600),
			color = AmityTheme.colors.baseInverse,
		)
		Spacer(modifier = Modifier.height(4.dp))
		if (message.isDeleted()) {
			DeletedParentMessageContent(message = message)
		} else {
			ParentTextMessageContent(message = message)
		}
	}
}

@Composable
fun ParentTextMessageContent(message: AmityMessage) {
	Text(
		text = getContent(message),
		style = TextStyle(
			fontSize = 13.sp,
			lineHeight = 18.sp,
			fontWeight = FontWeight(400),
			color = AmityTheme.colors.baseInverse,
		),
	)
}

@Composable
fun DeletedParentMessageContent(message: AmityMessage) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.padding(0.dp, 2.dp)
	) {
		Icon(
			imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
			contentDescription = "deleted message icon",
			tint = AmityTheme.colors.baseInverse,
			modifier = Modifier.size(16.dp)
		)
		Spacer(modifier = Modifier.width(4.dp))
		Text(
			text = getContent(message),
			style = TextStyle(
				fontSize = 13.sp,
				lineHeight = 18.sp,
				fontWeight = FontWeight(400),
				color = AmityTheme.colors.baseInverse,
			),
		)
	}
}

@Composable
fun ParentMessageLoading() {
	TextMessageShimmer()
}

@Composable
fun TextMessageShimmer() {
	Column {
		Box(
			modifier = Modifier
				.height(12.dp)
				.width(120.dp)
				.shimmerBackground(shape = RoundedCornerShape(6.dp))
		) {}
		Spacer(modifier = Modifier.height(6.dp))
		Box(
			modifier = Modifier
				.height(12.dp)
				.width(180.dp)
				.shimmerBackground(shape = RoundedCornerShape(6.dp))
		) {}
		Spacer(modifier = Modifier.height(6.dp))
	}
}

fun getReplyTextMessageContentTestTag(message: AmityMessage): String {
	return if (message.getCreatorId() == AmityCoreClient.getUserId()) {
		"message_list/message_bubble_sender_text_text_view"
	} else {
		"message_list/message_bubble_receiver_text_text_view"
	}
}

fun getReplyTextMessageBubbleTestTag(message: AmityMessage): String {
	return if (message.getCreatorId() == AmityCoreClient.getUserId()) {
		"message_list/message_bubble_sender_text"
	} else {
		"message_list/message_bubble_receiver_text"
	}
}