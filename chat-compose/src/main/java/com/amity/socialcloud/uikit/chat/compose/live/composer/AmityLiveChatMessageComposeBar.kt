package com.amity.socialcloud.uikit.chat.compose.live.composer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getValue
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@Composable
fun AmityLiveChatMessageComposeBar(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityLiveChatPageViewModel,
) {
    var messageText by remember { mutableStateOf("") }
    val isTextValid by remember {
        derivedStateOf { messageText.isNotEmpty() }
    }
    var shouldClearText by remember { mutableStateOf(false) }

    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var queryToken by remember { mutableStateOf("") }

    var selectedUserToMention by remember { mutableStateOf<AmityMentionSuggestion?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata>>(emptyList()) }

    var isReplyingToMessage by remember { mutableStateOf(false) }

    val parentMessage by remember { viewModel.replyTo }

    fun onDismissParent() {
        viewModel.dismissReplyMessage()
    }

    val membership by remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val isChannelMuted by remember {
        derivedStateOf { }
        viewModel.getChannelFlow().map {
            it.isMuted()
        }.distinctUntilChanged()
    }.collectAsState(initial = false)

    val isChannelModerator by remember {
        derivedStateOf { }
        viewModel.isChannelModerator().distinctUntilChanged()
    }.collectAsState(initial = false)

    val messageListState by remember { viewModel.messageListState }

    var showComposeErrorDialog = remember { mutableStateOf(false) }


    LaunchedEffect(parentMessage) {
        isReplyingToMessage = parentMessage != null
    }

    if (messageListState == AmityLiveChatPageViewModel.MessageListState.SUCCESS) {

        AmityBaseComponent(
            pageScope = pageScope,
            componentId = "message_composer"
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {

                if (showComposeErrorDialog.value) {
                    MessageComposeErrorPopup(
                        onDismiss = { showComposeErrorDialog.value = false }
                    )
                }

                if (parentMessage != null && isReplyingToMessage) {
                    AmityMessageComposeReplyLabel(
                        parentMessage = parentMessage,
                    ) {
                        isReplyingToMessage = false
                        viewModel.dismissReplyMessage()
                    }
                }

                if (shouldShowSuggestion) {
                    AmityMentionSuggestionView(
                        modifier = modifier,
                        keyword = queryToken,
                        viewModel = viewModel
                    ) {
                        selectedUserToMention = it
                        shouldShowSuggestion = false
                    }
                }

                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    AmityBaseElement(
                        elementId = "message_composer_text_field",
                        componentScope = getComponentScope()
                    ) {

                        val hint: String = try {
                            getComponentScope().getConfig().getValue("placeholder_text")
                        } catch (e: Exception) {
                            "Write a message"
                        }
                        AmityMessageMentionTextField(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("message_composer_text_field"),
                            hint = hint,
                            maxLines = 5,
                            isEnabled = isChannelModerator || ((membership?.isMuted() != true) && !isChannelMuted),
                            addedMention = selectedUserToMention,
                            shouldClearText = shouldClearText,
                            onValueChange = {
                                messageText = it
                            },
                            onMentionAdded = {
                                selectedUserToMention = null
                            },
                            onQueryToken = {
                                queryToken = it ?: ""
                                shouldShowSuggestion = (it != null)
                            },
                            onMention = {
                                mentionedUsers = it
                            }
                        )
                    }
                    val maxChar: Int = try {
                        getComponentScope().getConfig().getValue("message_limit").toInt()
                    } catch (e: Exception) {
                        10000
                    }
                    Button(
                        onClick = {
                            if (messageText.isBlank()) {
                                return@Button
                            }
                            if (messageText.length > maxChar) {
                                showComposeErrorDialog.value = true
                                return@Button
                            }
                            shouldClearText = true
                            viewModel.createMessage(
                                parentId = parentMessage?.getMessageId(),
                                text = messageText.trim(),
                                mentionMetadata = mentionedUsers,
                                onSuccess = {
                                    messageText = ""
                                    selectedUserToMention = null
                                    mentionedUsers = emptyList()
                                    isReplyingToMessage = false
                                    shouldClearText = false
                                    onDismissParent()
                                },
                                onError = { exception ->
                                    messageText = ""
                                    mentionedUsers = emptyList()
                                    shouldClearText = false
                                    selectedUserToMention = null
                                    isReplyingToMessage = false
                                    onDismissParent()

                                    val errorMessage: String = if (exception is AmityException) {
                                        when (AmityError.from(exception.code)) {
                                            AmityError.BAN_WORD_FOUND -> "Your message wasn't sent as it contained a blocked word."
                                            AmityError.LINK_NOT_ALLOWED -> "Your message wasn't sent as it contained a link that's not allowed."
                                            else -> exception.message ?: "This message failed to be sent. Please try again."
                                        }
                                    } else {
                                        exception.message ?: "This message failed to be sent. Please try again."
                                    }
                                    getComponentScope().showSnackbar(
                                        message = errorMessage
                                    )
                                }
                            )
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmityTheme.colors.primary,
                            contentColor = AmityTheme.colors.primaryShade4,
                            disabledContainerColor = AmityTheme.colors.primaryShade2,
                            disabledContentColor = AmityTheme.colors.primaryShade4,
                        ),
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(enabled = isTextValid) {},
                        contentPadding = PaddingValues(1.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_arrow_upward),
                            contentDescription = "Send",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageComposeErrorPopup(
    onDismiss: () -> Unit,
) {

    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AmityTheme.colors.baseShade4)
                    .width(270.dp)
            ) {
                Column {
                    Column(
                        modifier = Modifier.padding(19.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Unable to send message",
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                        Text(
                            text = "Your message is too long. Please shorten your message and try again.",
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AmityTheme.colors.secondaryShade1
                    )
                    Row(
                        modifier = Modifier
                            .height(41.dp)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { onDismiss.invoke() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "OK",
                                fontSize = 17.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(600),
                                color = AmityTheme.colors.primary,
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AmityMessageComposerPreview() {
    AmityLiveChatMessageComposeBar(
        viewModel = AmityLiveChatPageViewModel("")
    )
}