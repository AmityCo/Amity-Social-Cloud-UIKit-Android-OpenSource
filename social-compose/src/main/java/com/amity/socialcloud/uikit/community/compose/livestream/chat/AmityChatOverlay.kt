package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAnnotatedText
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.BottomConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamChatViewModel.AmityLiveStreamSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportOtherReasonScreen
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportReasonListScreen
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatOverlay(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    channelId: String,
    onReactionClick: () -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityLivestreamChatViewModel>(
        factory = AmityLivestreamChatViewModel.create(channelId),
        viewModelStoreOwner = viewModelStoreOwner
    )
    val messages = viewModel.getMessageList().collectAsLazyPagingItems()

    val isChannelModerator by remember {
        derivedStateOf { }
        viewModel.isChannelModerator().distinctUntilChanged()
    }.collectAsState(initial = false)

    var messageText by remember { mutableStateOf("") }
    var showMessageAction by remember { mutableStateOf(false) }
    val targetDeletedMessage by remember { viewModel.targetDeletedMessage }
    val isShowDeleteDialog by remember { viewModel.showDeleteDialog }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by remember { viewModel.sheetUIState }.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityLiveStreamSheetUIState.CloseSheet
        }
    }

    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        0.3f to Color.Black.copy(alpha = 0.1f),
                        0.6f to Color.Black.copy(alpha = 0.4f),
                        0.8f to Color.Black.copy(alpha = 0.7f),
                        1.0f to Color.Black
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp)) // Add some space at the top
            }
            items(
                count = messages.itemCount,
                key = messages.itemKey { it.getMessageId() }
            ) { index ->
                messages[index]?.let { message ->
                    ChatMessageItem(
                        message = message,
                        isChannelModerator = isChannelModerator,
                        onOpenAction = {
                            viewModel.updateSheetUIState(
                                AmityLiveStreamSheetUIState.OpenSheet(message, isChannelModerator)
                            )
                            viewModel.setTargetDeletedMessage(message)
                        },
                        onConfirmDelete = {
                            viewModel.showDeleteConfirmation(message)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Add space between messages

                }
            }
            item {
                Spacer(modifier = Modifier.height(40.dp)) // Add some space at the top
            }
        }


        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                },
                sheetState = sheetState,
                containerColor = Color(0xFF191919),
                contentWindowInsets = { WindowInsets.waterfall },
            ) {
                when(sheetUIState) {
                    is AmityLiveStreamSheetUIState.OpenSheet -> {
                        val message = (sheetUIState as AmityLiveStreamSheetUIState.OpenSheet).message
                        AmityLivestreamMessageActionsContainer(
                            message = message,
                            isChannelModerator = isChannelModerator,
                            onReport = { messageId ->
                                viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.OpenReportSheet(messageId))
                            },
                            onUnreport = { messageId ->
                                viewModel.unflagMessage(
                                    messageId = messageId,
                                    onSuccess = {
                                        pageScope?.showSnackbar("Message unreported")
                                    }
                                )
                                viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                            },
                            onDelete = {
                                viewModel.deleteMessage()
                                viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                            }
                        )
                    }
                    is AmityLiveStreamSheetUIState.OpenReportSheet -> {
                        AmityBaseComponent(
                            componentId = "",
                            needScaffold = true
                        ) {
                            AmityReportReasonListScreen(
                                onCloseSheetClick = {
                                    viewModel.updateSheetUIState(
                                        AmityLiveStreamSheetUIState.CloseSheet
                                    )
                                },
                                onOtherClick = {
                                    viewModel.updateSheetUIState(
                                        AmityLiveStreamSheetUIState.OpenReportOtherReasonSheet(sheetUIState.messageId)
                                    )
                                },
                                onSubmitClick = { reason, enableButtonCallback ->
                                    submitReport(
                                        viewModel = viewModel,
                                        messageId = sheetUIState.messageId,
                                        pageScope = pageScope,
                                        reason = reason,
                                        onError = enableButtonCallback
                                    )
                                }
                            )
                        }
                    }

                    is AmityLiveStreamSheetUIState.OpenReportOtherReasonSheet -> {
                        AmityBaseComponent(
                            componentId = "",
                            needScaffold = true
                        ) {
                            AmityReportOtherReasonScreen(
                                onBackClick = {
                                    viewModel.updateSheetUIState(
                                        AmityLiveStreamSheetUIState.OpenReportSheet(sheetUIState.messageId)
                                    )
                                },
                                onSubmitClick = { detail, enableButtonCallback ->
                                    submitReport(
                                        viewModel = viewModel,
                                        messageId = sheetUIState.messageId,
                                        reason = AmityContentFlagReason.Others(detail),
                                        pageScope = pageScope,
                                        onError = enableButtonCallback
                                    )
                                },
                                onCloseSheetClick = {
                                    viewModel.updateSheetUIState(
                                        AmityLiveStreamSheetUIState.CloseSheet
                                    )
                                }
                            )
                        }
                    }

                    else -> {}
                }

            }
        }
    }

    if (isShowDeleteDialog) {
        BottomConfirmDeletePopup(
            pageScope = pageScope,
            componentScope = componentScope,
            onDelete = {
                viewModel.deleteMessage(
                    onError = {
                        pageScope?.showSnackbar(
                            message = "Unable to delete message. Please try again."
                        )
                    }
                )
            },
            onDismiss = {
                viewModel.dismissDeleteConfirmation()
            },
        )
    }
}

@Composable
fun ChatMessageItem(
    message: AmityMessage,
    isChannelModerator: Boolean,
    onOpenAction: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x4D636878) // Reduced opacity
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = message.getCreator()?.getDisplayName() ?: "Unknown user",
                        color = if (message.isDeleted()) Color(0xFF6E7487) else Color(0xFFA5A9B5),
                        style = AmityTheme.typography.captionSmall,
                    )
                    if (message.getState() == AmityMessage.State.SYNCED && !message.isDeleted()) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.amity_ic_livestream_chat_options
                            ),
                            contentDescription = "message options",
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .clickableWithoutRipple {
                                    onOpenAction()
                                },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    if (message.isDeleted()) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.amity_ic_delete_story
                            ),
                            contentDescription = "message options",
                            tint = Color(0xFF6E7487),
                            modifier = Modifier
                                .size(16.dp)
                                .clickableWithoutRipple {
                                    onOpenAction()
                                },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    AmityAnnotatedText(
                        text = getContent(message),
                        mentionGetter = AmityMentionMetadataGetter(message.getMetadata() ?: JsonObject()),
                        mentionees = message.getMentionees(),
                        style = AmityTheme.typography.caption.copy(
                            color = if (message.isDeleted()) Color(0xFF6E7487) else Color.White,
                        ),
                        highlightColor = Color.White,
                        onLongPress = {},
                    )
                }
            }
            if (message.getState() == AmityMessage.State.FAILED) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.amity_ic_livestream_chat_sending_fail
                    ),
                    contentDescription = "message sending failed icon",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickableWithoutRipple {
                            onConfirmDelete.invoke()
                        },
                )
            }
        }
    }
}

fun getContent(message: AmityMessage): String {
    return if (message.isDeleted()) {
        "This message was deleted"
    } else {
        (message.getData() as? AmityMessage.Data.TEXT)?.getText() ?: "Unsupport message type"
    }
}


@Composable
fun AmityLivestreamMessageActionsContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    message: AmityMessage,
    isChannelModerator: Boolean,
    onDelete: () -> Unit,
    onReport: (String) -> Unit = {},
    onUnreport: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Color(0xFF191919))
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        if (message.getCreatorId() != AmityCoreClient.getUserId()) {
            if(!message.isFlaggedByMe()) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_report_comment,
                    text = "Report message",
                    color = Color.White,
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_report_comment_button"),
                ) {
                    onReport(message.getMessageId())
                }
            } else {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_unreport,
                    text = "Unreport message",
                    color = Color.White,
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_unreport_comment_button"),
                ) {
                    onUnreport(message.getMessageId())
                }
            }
        }
        if (message.getCreatorId() == AmityCoreClient.getUserId() || isChannelModerator) {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_delete_story,
                text = "Delete message",
                color = AmityTheme.colors.alert,
                modifier = modifier.testTag("comment_tray_component/bottom_sheet_delete_comment_button"),
            ) {
                onDelete()
            }
        }
    }
}

private fun submitReport(
    viewModel: AmityLivestreamChatViewModel,
    messageId: String,
    reason: AmityContentFlagReason,
    pageScope: AmityComposePageScope? = null,
    onError: () -> Unit = {},  // Add new parameter with default value
) {
    viewModel.flagMessage(
        messageId = messageId,
        reason = reason,
        onSuccess = {
            pageScope?.showSnackbar("Message reported")
            viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
        },
        onError = { error ->
            onError()
            pageScope?.showSnackbar("Failed to report message. Please try again.", drawableRes = R.drawable.amity_ic_warning)
        }
    )
}