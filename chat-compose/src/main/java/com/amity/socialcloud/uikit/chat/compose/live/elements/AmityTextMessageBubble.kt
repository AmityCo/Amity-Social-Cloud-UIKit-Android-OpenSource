package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.util.getContent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAnnotatedText
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AmityLiveChatMessageSenderView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    message: AmityMessage,
    viewModel: AmityLiveChatPageViewModel,
    onMessageAction: AmityMessageAction = AmityMessageAction(),
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_bubble_sender_text"
    ) {
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
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_bubble_receiver_text"
    ) {
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
) {
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
    var reactionExpanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_bubble",
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            AmityMessageAvatarView(
                pageScope = pageScope,
                avatarUrl = message.getCreator()?.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
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
                    color = AmityTheme.colors.secondaryShade3,
                    modifier = Modifier.align(Alignment.Start),
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (!message.isDeleted()) {
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        AmityMessageReactionPicker(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            message = message,
                            show = reactionExpanded,
                            onAddReaction = onMessageAction.onAddReaction,
                            onRemoveReaction = onMessageAction.onRemoveReaction,
                        ) { reactionExpanded = false }
                    }
                }
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        val hasMentionedMe =
                            message.getCreatorId() != AmityCoreClient.getUserId() &&
                                    message.getMentionees().any {
							            it is AmityMentionee.CHANNEL
                                                || ((it is AmityMentionee.USER) && it.getUserId() == AmityCoreClient.getUserId())
                                    }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .shadow(
                                    shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp),
                                    elevation = if (menuExpanded) 4.dp else 0.dp,
                                    clip = true,
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (hasMentionedMe) {
                                        AmityTheme.colors.highlight
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = if (message.isDeleted()) {
                                        RoundedCornerShape(12.dp)
                                    } else {
                                        RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
                                    },
                                )
                                .background(
                                    color = AmityTheme.colors.baseShade4,
                                )
                                .weight(1.0f, fill = false)
                                .wrapContentWidth(align = Alignment.Start)
                        ) {
                            if (message.isDeleted()) {
                                DeletedMessageContent(message = message)
                            } else {
                                TextMessageContent(message = message,
                                    onLongPress = {
                                        menuExpanded = true
                                        reactionExpanded = true
                                    })
                            }
                        }
                        AmityMessageQuickReaction(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            message = message,
                            onMessageAction = onMessageAction,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                        AmityMessageBubbleFlag(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            message = message
                        )
                        AmityMessageBubbleTimestamp(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            message = message,
                            onDelete = onMessageAction.onDelete,
                        )
                    }
                    Layout(
                        content = {
                            AmityMessageReactionPreview(
                                pageScope = pageScope,
                                componentScope = componentScope,
                                message = message,
                                modifier = Modifier
                                    .offset(y = (-8).dp)
                                    .clickable {
                                        onMessageAction.onOpenReactions(message)
                                    }
                            )
                        },
                        measurePolicy = { measurables, constraints ->
                            // Measure each child
                            val placeables = measurables.map { measurable ->
                                measurable.measure(constraints)
                            }

                            // Determine the height of the item
                            val height = Math.max(placeables.sumOf { it.height } - (8.dp.roundToPx()), 0)

                            // Set the size of the item
                            layout(
                                height = height,
                                width = placeables.maxOfOrNull { it.width } ?: 0) {
                                var xPosition = 0
                                placeables.forEach { placeable ->
                                    placeable.placeRelative(IntOffset(xPosition, 0))
                                    xPosition += placeable.width
                                }
                            }
                        }
                    )
                    if (!message.isDeleted()) {
                        Box(modifier = Modifier.padding(top = 8.dp)) {
                            AmityMessageOption(
                                pageScope = pageScope,
                                componentScope = componentScope,
                                show = menuExpanded,
                                onMessageAction = onMessageAction,
                                modifier = Modifier.width(243.dp),
                            ) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    delay(100)
                                    reactionExpanded = false
                                }
                                menuExpanded = false
                            }
                        }
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
    onLongPress: () -> Unit = {},
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
        onLongPress = onLongPress,
        modifier = modifier
            .padding(10.dp, 8.dp)
            .testTag(getTextMessageContentTestTag(message = message))
    )
}

@Composable
fun DeletedMessageContent(
    message: AmityMessage,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(Alignment.CenterVertically),
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_delete_message),
            contentDescription = "deleted message icon",
            tint = AmityTheme.colors.baseInverse,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .size(16.dp)
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