@file:JvmName("AmityLiveChatMessageListKt")

package com.amity.socialcloud.uikit.chat.compose.live.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.LoadingIndicator
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.elements.ConfirmDeletePopup
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityLiveChatMessageReceiverView
import com.amity.socialcloud.uikit.chat.compose.live.elements.ReplyMessageReceiverView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityLiveChatMessageSenderView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAction
import com.amity.socialcloud.uikit.chat.compose.live.elements.ReplyMessageSenderView
import com.amity.socialcloud.uikit.chat.compose.live.util.getContent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.copyText
import kotlinx.coroutines.launch

@Composable
fun AmityLiveChatMessageList(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityLiveChatPageViewModel,
) {
    val isModerator = remember { viewModel.isChannelModerator() }.collectAsState(initial = false)
    var isError by remember { mutableStateOf(false) }
    val messages = viewModel.getMessageList(
        onError = { isError = true }
    ).collectAsLazyPagingItems()
    
    val onReply = { message: AmityMessage ->
        viewModel.setReplyToMessage(message)
    }
    
    val onDelete = { message: AmityMessage ->
        viewModel.showDeleteConfirmation(messageId = message.getMessageId())
    }
    
    val onCopy = @Composable { message: AmityMessage ->
        copyText(getContent(message))
    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    AmityBaseComponent(componentId = "message_list") {
        if (!isError) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true,
                modifier = modifier
                    .fillMaxSize(),
                state = state,
            ) {
                scope.launch {
                    state.scrollToItem(0)
                }
                items(
                    count = messages.itemCount,
                    key = messages.itemKey { it.getMessageId() }
                ) { index ->
                    messages[index]?.let { message ->
                        val onDeleteAction = if (message.getCreatorId() == AmityCoreClient.getUserId() || isModerator.value) {
                            { onDelete(message) }
                        } else {
                            null
                        }
                        val onReplyAction = if (message.getState() == AmityMessage.State.SYNCED) {
                            { onReply(message) }
                        } else {
                            null
                        }
                        val onCopyAction = @Composable { onCopy(message) }
                        val onMessageAction = AmityMessageAction(
                            onDelete = onDeleteAction,
                            onReply = onReplyAction,
                            onCopy = onCopyAction
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
                            item { LoadingIndicator(itemCount) }
                        }
                        
                        loadState.append is LoadState.Loading -> {
                            viewModel.isFetching(true)
                            item { LoadingIndicator(itemCount) }
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
                viewModel = viewModel
            )
        } else {
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
        }
    }
}