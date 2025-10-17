package com.amity.socialcloud.uikit.community.compose.livestream.chat

import android.util.Log
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
import androidx.compose.runtime.key
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
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityAnnotatedText
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.BottomConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
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
    streamHostUserId: String? = null,
    fromNonMemberCommunity: Boolean = false,
    onReactionClick: () -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityLivestreamChatViewModel>(
        factory = AmityLivestreamChatViewModel.create(channelId),
        viewModelStoreOwner = viewModelStoreOwner
    )

    val behavior = remember {
        AmitySocialBehaviorHelper.createLivestreamPageBehavior
    }

    val messages = viewModel.getMessageList().collectAsLazyPagingItems()

    // Use metadata as the single source of truth for moderator status
    // This ensures immediate response to promote/demote actions via metadata updates
    val isCurrentUserModerator by viewModel.isUserModerator(AmityCoreClient.getUserId())
        .collectAsState(initial = false)

    val hostUserId = streamHostUserId

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
                key = { index ->
                    messages[index]?.getMessageId() ?: "message_$index"
                }
            ) { index ->
                messages[index]?.let { message ->
                    val userId = message.getCreatorId()
                    
                    ChatMessageItem(
                        message = message,
                        isChannelModerator = isCurrentUserModerator,
                        hostUserId = hostUserId,
                        isMessageCreatorModerator = viewModel.isUserModerator(userId),
                        isMessageCreatorMuted = viewModel.isUserMuted(userId),
                        onOpenAction = {
                            viewModel.updateSheetUIState(
                                AmityLiveStreamSheetUIState.OpenSheet(message, isCurrentUserModerator)
                            )
                            viewModel.setTargetDeletedMessage(message)
                        },
                        onConfirmDelete = {
                            viewModel.showDeleteConfirmation(message)
                        },
                        onUserNameClick = {
                            if (userId != AmityCoreClient.getUserId() 
                                && isCurrentUserModerator
                                && userId != hostUserId) {
                                val displayName = message.getCreator()?.getDisplayName() ?: "Unknown user"
                                viewModel.updateSheetUIState(
                                    AmityLiveStreamSheetUIState.OpenUserActionsSheet(
                                        userId = userId,
                                        displayName = displayName
                                    )
                                )
                            }
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
                            isChannelModerator = isCurrentUserModerator,
                            onReport = { messageId ->
                                if (AmityCoreClient.isVisitor()) {
                                    behavior.handleVisitorUserAction()
                                    viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                                } else if (fromNonMemberCommunity) {
                                    behavior.handleNonMemberAction()
                                    viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                                } else {
                                    viewModel.updateSheetUIState(
                                        AmityLiveStreamSheetUIState.OpenReportSheet(
                                            messageId
                                        )
                                    )
                                }
                            },
                            onUnreport = { messageId ->
                                if (AmityCoreClient.isVisitor()) {
                                    behavior.handleVisitorUserAction()
                                } else if (fromNonMemberCommunity) {
                                    behavior.handleNonMemberAction()
                                } else {
                                    viewModel.unflagMessage(
                                        messageId = messageId,
                                        onSuccess = {
                                            pageScope?.showSnackbar("Message unreported")
                                        }
                                    )
                                }
                                viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                            },
                            onDelete = {
                                if (AmityCoreClient.isVisitor()) {
                                    behavior.handleVisitorUserAction()
                                } else if (fromNonMemberCommunity) {
                                    behavior.handleNonMemberAction()
                                } else {
                                    viewModel.deleteMessage()
                                }
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

                    is AmityLiveStreamSheetUIState.OpenUserActionsSheet -> {
                        val userActionsState = sheetUIState as AmityLiveStreamSheetUIState.OpenUserActionsSheet
                        
                        val isModerator by viewModel.isUserModerator(userActionsState.userId)
                            .collectAsState(initial = false)
                        
                        val isMuted by viewModel.isUserMuted(userActionsState.userId)
                            .collectAsState(initial = false)
                        
                        AmityUserActionsSheet(
                            displayName = userActionsState.displayName,
                            userId = userActionsState.userId,
                            isModerator = isModerator,
                            isMuted = isMuted,
                            viewModel = viewModel,
                            pageScope = pageScope,
                            onClose = {
                                viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
                            }
                        )
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
    hostUserId: String?,
    isMessageCreatorModerator: kotlinx.coroutines.flow.Flow<Boolean>,
    isMessageCreatorMuted: kotlinx.coroutines.flow.Flow<Boolean>,
    onOpenAction: () -> Unit,
    onConfirmDelete: () -> Unit,
    onUserNameClick: () -> Unit = {}
) {
    val isCreatorModerator by isMessageCreatorModerator.collectAsState(initial = false)
    val isCreatorMuted by isMessageCreatorMuted.collectAsState(initial = false)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickableWithoutRipple {
                            onUserNameClick()
                        }
                    ) {
                        Text(
                            text = message.getCreator()?.getDisplayName() ?: "Unknown user",
                            color = if (message.isDeleted()) Color(0xFF6E7487) else Color(0xFFA5A9B5),
                            style = AmityTheme.typography.captionSmall,
                        )
                        
                        // Show Host badge if user is stream host (takes precedence over moderator badge)
                        if (message.getCreatorId() == hostUserId && !message.isDeleted()) {
                            OwnerBadge()
                        }
                        // Show Moderator badge if user is moderator but NOT host
                        else if (isCreatorModerator && !message.isDeleted()) {
                            ModeratorBadge()
                        }
                        
                        // Show Muted badge only if current user is a moderator and message creator is muted
                        if (isChannelModerator && isCreatorMuted && !message.isDeleted()) {
                            MutedBadge()
                        }
                    }
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

@Composable
fun OwnerBadge() {
    Row(
        modifier = Modifier
            .background(
                color = AmityTheme.colors.alert,
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_livestream_host),
            contentDescription = "Host badge",
            tint = Color.White,
            modifier = Modifier
                .size(12.dp)
                .padding(start = 1.dp, top = 1.dp, bottom = 1.dp, end = 2.dp)
        )
        Text(
            text = "Host",
            color = Color.White,
            style = AmityTheme.typography.captionSmall,
            modifier = Modifier.padding(end = 3.dp)
        )
    }
}

@Composable
fun ModeratorBadge() {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFF40434E),
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_moderator_social),
            contentDescription = "Moderator badge",
            tint = Color.White,
            modifier = Modifier
                .size(width = 12.dp, height = 9.dp)
                .padding(start = 2.dp, top = 1.dp, bottom = 1.dp, end = 1.dp)
        )
        Text(
            text = "Moderator",
            color = AmityTheme.colors.baseShade3,
            style = AmityTheme.typography.captionSmall,
            modifier = Modifier.padding(end = 3.dp)
        )
    }
}

@Composable
fun MutedBadge() {
    Icon(
        painter = painterResource(id = R.drawable.amity_v4_ic_mute),
        contentDescription = "Muted badge",
        tint = AmityTheme.colors.baseShade2,
        modifier = Modifier
            .size(16.dp)
    )
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

@Composable
fun AmityUserActionsSheet(
    displayName: String,
    userId: String,
    isModerator: Boolean,
    isMuted: Boolean = false,
    viewModel: AmityLivestreamChatViewModel,
    pageScope: AmityComposePageScope?,
    onClose: () -> Unit
) {
    var showPromoteDialog by remember { mutableStateOf(false) }
    var showDemoteDialog by remember { mutableStateOf(false) }
    var showMuteDialog by remember { mutableStateOf(false) }
    var showUnmuteDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .background(Color(0xFF191919))
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display name with muted icon on the right if muted
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    color = Color(0xFFEBECEF),
                    style = AmityTheme.typography.titleBold,
                )
                
                // Muted icon if user is muted
                if (isMuted) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_v4_ic_mute),
                        contentDescription = "Muted badge",
                        tint = AmityTheme.colors.baseShade2,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
            
            // Moderator badge (icon + text) under display name if user is a moderator
            if (isModerator) {
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            color = Color(0xFF40434E),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_moderator_social),
                        contentDescription = "Moderator badge",
                        tint = Color.White,
                        modifier = Modifier
                            .size(width = 12.dp, height = 9.dp)
                            .padding(start = 2.dp, top = 1.dp, bottom = 1.dp, end = 1.dp)
                    )
                    Text(
                        text = "Moderator",
                        color = AmityTheme.colors.baseShade3,
                        style = AmityTheme.typography.captionSmall,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                }
            }
        }
        
        // Divider line
        Spacer(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(AmityTheme.colors.base)
        )
        
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
        
        // Promote/Demote moderator button - hide promote if user is muted
        if (!isMuted) {
            AmityBottomSheetActionItem(
                icon = if (isModerator) R.drawable.amity_ic_demote_moderator else R.drawable.amity_ic_promote_moderator,
                text = if (isModerator) "Demote to member" else "Promote to moderator",
                color = Color(0xFFEBECEF),
            ) {
                if (isModerator) {
                    showDemoteDialog = true
                } else {
                    showPromoteDialog = true
                }
            }
        }
        
        // Mute/Unmute user - only show if user is not a moderator
        if (!isModerator) {
            AmityBottomSheetActionItem(
                icon = if (isMuted) R.drawable.amity_v4_ic_unmute else R.drawable.amity_v4_ic_mute,
                text = if (isMuted) "Unmute user" else "Mute user",
                color = Color(0xFFEBECEF),
            ) {
                if (isMuted) {
                    showUnmuteDialog = true
                } else {
                    showMuteDialog = true
                }
            }
        }

        }
    }
    
    // Promote confirmation dialog
    if (showPromoteDialog) {
        AmityAlertDialog(
            dialogTitle = "Moderator promotion",
            dialogText = "Are you sure you want to promote this member to moderator? They will gain access to all moderator permissions in this live stream.",
            confirmText = "Promote",
            dismissText = "Cancel",
            onConfirmation = {
                showPromoteDialog = false
                viewModel.promoteToModerator(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("User promoted.")
                        pageScope?.showSnackbar("User promoted to moderator")
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to promote user. Please try again.")
                        pageScope?.showSnackbar("Failed to promote user. Please try again.")
                    }
                )
            },
            onDismissRequest = {
                showPromoteDialog = false
            }
        )
    }
    
    // Demote confirmation dialog
    if (showDemoteDialog) {
        AmityAlertDialog(
            dialogTitle = "Moderator demotion",
            dialogText = "Are you sure you want to demote this moderator? They will lose access to all moderator permissions in this live stream.",
            confirmText = "Demote",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showDemoteDialog = false
                viewModel.demoteToMember(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("User demoted.")
                        pageScope?.showSnackbar("User demoted to member")
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to demote user. Please try again.")
                        pageScope?.showSnackbar("Failed to demote user. Please try again.")
                    }
                )
            },
            onDismissRequest = {
                showDemoteDialog = false
            }
        )
    }
    
    // Mute confirmation dialog
    if (showMuteDialog) {
        AmityAlertDialog(
            dialogTitle = "Confirm mute",
            dialogText = "Are you sure you want to mute this user? They will no longer be able to send messages.",
            confirmText = "Mute",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showMuteDialog = false
                viewModel.muteUser(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("User muted.")
                        pageScope?.showSnackbar("User muted")
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to mute user. Please try again.")
                        pageScope?.showSnackbar("Failed to mute user. Please try again.")
                    }
                )
            },
            onDismissRequest = {
                showMuteDialog = false
            }
        )
    }
    
    // Unmute confirmation dialog
    if (showUnmuteDialog) {
        AmityAlertDialog(
            dialogTitle = "Confirm unmute",
            dialogText = "Are you sure you want to unmute this user? They can now send messages.",
            confirmText = "Unmute",
            dismissText = "Cancel",
            onConfirmation = {
                showUnmuteDialog = false
                viewModel.unmuteUser(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("User unmuted.")
                        pageScope?.showSnackbar("User unmuted")
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to unmute user. Please try again.")
                        pageScope?.showSnackbar("Failed to unmute user. Please try again.")
                    }
                )
            },
            onDismissRequest = {
                showUnmuteDialog = false
            }
        )
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