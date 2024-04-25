@file:JvmName("AmityLiveChatMessageListKt")

package com.amity.socialcloud.uikit.chat.compose.live.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.chat.compose.live.LoadingIndicator
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.elements.ConfirmDeletePopup
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityLiveChatMessageReceiverView
import com.amity.socialcloud.uikit.chat.compose.live.elements.ReplyMessageReceiverView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityLiveChatMessageSenderView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAction
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.elements.ReplyMessageSenderView
import com.amity.socialcloud.uikit.chat.compose.live.util.getContent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.copyText
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun AmityLiveChatMessageList(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityLiveChatPageViewModel,
) {
    val isModerator = remember { viewModel.isChannelModerator() }.collectAsState(initial = false)
    var isError by remember {
        mutableStateOf(false)
    }

    val membership = remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isBanned() == new.isBanned()
                        && old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val messageListState: AmityLiveChatPageViewModel.MessageListState by remember {
        derivedStateOf {
            AmityLiveChatPageViewModel.MessageListState.from(
                member = membership.value,
                hasError = isError
            )
        }
    }

    val isGlobalBanned by remember {
        viewModel.observeGlobalBanEvent().distinctUntilChanged()
    }.collectAsState(initial = false)

    val messages = viewModel.getMessageList(
        onError = { isError = true }
    ).collectAsLazyPagingItems()

    val onReply = { message: AmityMessage ->
        viewModel.setReplyToMessage(message)
    }

    val onDelete = { message: AmityMessage ->
        viewModel.showDeleteConfirmation(message = message)
    }

    val onCopy = @Composable { message: AmityMessage ->
        copyText(getContent(message))
    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var highestSegment = 0

    LaunchedEffect(messages) {
        snapshotFlow { messages.itemSnapshotList.firstOrNull() }
            .collect { message ->
                if (message != null
                    && message.getSegment() > highestSegment
                ) {
                    highestSegment = message.getSegment()
                    scope.launch {
                        state.scrollToItem(0)
                    }
                }
            }
    }

    AmityBaseComponent(
        componentId = "message_list",
        pageScope = pageScope,
    ) {
        if (messageListState == AmityLiveChatPageViewModel.MessageListState.ERROR && !isGlobalBanned) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_message_list_reload),
                        contentDescription = "Reload button",
                        modifier = Modifier
                            .width(28.dp)
                            .height(24.dp)
                            .clickable {
                                isError = false
                            },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Couldn't load chat",
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            color = AmityTheme.colors.secondaryShade2,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        } else if (messageListState == AmityLiveChatPageViewModel.MessageListState.BANNED || isGlobalBanned) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_chat_banned),
                        contentDescription = "Banned from channel",
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "You are banned from chat",
                        style = TextStyle(
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(600),
                            color = AmityTheme.colors.secondaryShade2,
                            textAlign = TextAlign.Center,
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You won’t be able to participate in this chat until you’ve been unbanned.",
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            color = AmityTheme.colors.secondaryShade2,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true,
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(rememberNestedScrollInteropConnection()),

                state = state,
            ) {

                items(
                    count = messages.itemCount,
                    key = messages.itemKey { it.getMessageId() }
                ) { index ->
                    messages[index]?.let { message ->
                        val onDeleteAction =
                            if (message.getCreatorId() == AmityCoreClient.getUserId() || isModerator.value) {
                                { onDelete(message) }
                            } else {
                                null
                            }
                        val onReplyAction =
                            if (message.getState() == AmityMessage.State.SYNCED) {
                                { onReply(message) }
                            } else {
                                null
                            }
                        val onCopyAction = @Composable { onCopy(message) }
                        val onFlagAction =
                            if (message.getCreatorId() != AmityCoreClient.getUserId() && !message.isFlaggedByMe()) {
                                {
                                    viewModel.flagMessage(
                                        message = message,
                                        onSuccess = {
                                            getComponentScope().showSnackbar(
                                                message = "Message reported",
                                                drawableRes = CommonR.drawable.amity_ic_check_circle
                                            )
                                        },
                                        onError = {
                                            getComponentScope().showSnackbar(
                                                message = "This message failed to be reported. Please try again."
                                            )
                                        }
                                    )
                                }
                            } else {
                                null
                            }
                        val onUnFlagAction =
                            if (message.getCreatorId() != AmityCoreClient.getUserId() && message.getFlagCount() > 0 && message.isFlaggedByMe()) {
                                {
                                    viewModel.unFlagMessage(
                                        message = message,
                                        onSuccess = {
                                            getComponentScope().showSnackbar(
                                                message = "Message unreported",
                                                drawableRes = CommonR.drawable.amity_ic_check_circle
                                            )
                                        },
                                        onError = {
                                            getComponentScope().showSnackbar(
                                                message = "This message failed to be unreported. Please try again."
                                            )
                                        }
                                    )
                                }
                            } else {
                                null
                            }
                        val onMessageAction = AmityMessageAction(
                            onDelete = onDeleteAction,
                            onReply = onReplyAction,
                            onCopy = onCopyAction,
                            onFlag = onFlagAction,
                            onUnFlag = onUnFlagAction,
                        )
                        if (message.getCreator()?.getUserId() == AmityCoreClient.getUserId()) {
                            AmityLiveChatMessageSenderView(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                message = message,
                                viewModel = viewModel,
                                onMessageAction = onMessageAction,
                            )
                        } else {
                            AmityLiveChatMessageReceiverView(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                message = message,
                                viewModel = viewModel,
                                onMessageAction = onMessageAction,
                            )
                        }
                    }
                }


                // Handle loading state
                messages.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            viewModel.isFetching(true)
                            item {
                                LoadingIndicator(itemCount = itemCount)
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            viewModel.isFetching(true)
                            item {
                                LoadingIndicator(itemCount = itemCount)
                            }
                        }

                        loadState.append is LoadState.NotLoading
                                || loadState.append is LoadState.Error -> {
                            viewModel.isFetching(false)
                        }
                    }
                }
            }
            ConfirmDeletePopup(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                viewModel = viewModel
            )
        }

    }
}