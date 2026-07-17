package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityAnnotatedText
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.BottomConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityLiveBadgeRed
import com.amity.socialcloud.uikit.common.ui.theme.amityLivestreamChatBubbleBackground
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamChatViewModel.AmityLiveStreamSheetUIState
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportOtherReasonScreen
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportReasonListScreen
import com.google.gson.JsonObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatOverlay(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    channelId: String,
    streamHostUserId: String? = null,
    fromNonMemberCommunity: Boolean = false,
    onReactionClick: () -> Unit,
    coHostUserId: String? = null,
    canInviteCohost: Boolean = false,
    onInviteCohost: (String, AmityUser?) -> Unit = {_,_ -> },
    onCohostBadgeClick: () -> Unit = {},
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

    val messages = remember(viewModel) { viewModel.getMessageList() }.collectAsLazyPagingItems()

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
                .topFadingEdge()
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
                        isChannelModerator = (isCurrentUserModerator && message.getCreatorId() != streamHostUserId),
                        hostUserId = hostUserId,
                        coHostUserId = coHostUserId,
                        isMessageCreatorModerator = viewModel.isUserModerator(userId),
                        isMessageCreatorMuted = viewModel.isUserMuted(userId),
                        onOpenAction = {
                            viewModel.updateSheetUIState(
                                AmityLiveStreamSheetUIState.OpenSheet(message, (isCurrentUserModerator && message.getCreatorId() != streamHostUserId))
                            )
                            viewModel.setTargetDeletedMessage(message)
                        },
                        onConfirmDelete = {
                            viewModel.showDeleteConfirmation(message)
                        },
                        onUserNameClick = {
                            val isCoHostUser = coHostUserId != null && userId == coHostUserId
                            if (isCoHostUser) {
                                // Co-host: open the full "Co-host actions" sheet (badge +
                                // manage-product-tags toggle + Remove from live). It is wired
                                // via onCohostBadgeClick only on the host page (which owns the
                                // room view model / product-permission logic); it's a no-op on
                                // non-host pages, so no empty sheet appears there.
                                onCohostBadgeClick()
                            } else if (userId != AmityCoreClient.getUserId()
                                && isCurrentUserModerator
                                && userId != hostUserId) {
                                val displayName = message.getCreator()?.getDisplayName() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unknown_user_lowercase")
                                viewModel.updateSheetUIState(
                                    AmityLiveStreamSheetUIState.OpenUserActionsSheet(
                                        userId = userId,
                                        displayName = displayName,
                                        user = message.getCreator()
                                    )
                                )
                            }
                        },
                        onCohostBadgeClick = onCohostBadgeClick,
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
                containerColor = AmityTheme.colors.background,
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
                                            pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_message_unreported"))
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
                            },
                            isHostMessage = message.getCreatorId() == hostUserId
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
                            user = userActionsState.user,
                            isModerator = isModerator,
                            isMuted = isMuted,
                            canInviteCohost = canInviteCohost,
                            onInviteCohost = { userId, user ->
                                onInviteCohost(userId, user)
                            },
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
                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_delete_message_failed")
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
    coHostUserId: String?,
    isMessageCreatorModerator: kotlinx.coroutines.flow.Flow<Boolean>,
    isMessageCreatorMuted: kotlinx.coroutines.flow.Flow<Boolean>,
    onOpenAction: () -> Unit,
    onConfirmDelete: () -> Unit,
    onUserNameClick: () -> Unit = {},
    onCohostBadgeClick: () -> Unit = {},
) {
    val isCreatorModerator by isMessageCreatorModerator.collectAsState(initial = false)
    val isCreatorMuted by isMessageCreatorMuted.collectAsState(initial = false)
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = amityLivestreamChatBubbleBackground.copy(alpha = 0.3f) // Reduced opacity
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
                    ) {
                        Text(
                            text = message.getCreator()?.getDisplayName() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unknown_user_lowercase"),
                            color = if (message.isDeleted()) AmityTheme.colors.baseShade2 else AmityTheme.colors.baseShade1,
                            style = AmityTheme.typography.captionSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.clickableWithoutRipple {
                                onUserNameClick()
                            }.weight(1f, false)
                        )

                        // Show brand badge if user is a brand
                        val isBrandCreator = message.getCreator()?.isBrand() == true
                        if (isBrandCreator && !message.isDeleted()) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                contentDescription = "Brand badge",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Show Host badge if user is stream host (takes precedence over moderator badge)
                        if (message.getCreatorId() == hostUserId && !message.isDeleted()) {
                            HostBadge(onCohostBadgeClick = onCohostBadgeClick)
                        } else if (message.getCreatorId() == coHostUserId && !message.isDeleted()) {
                            HostBadge(
                                isCoHost = true,
                                onCohostBadgeClick = onCohostBadgeClick,
                            )
                        }
                        // Show Moderator badge if user is moderator but NOT host
                        else if (isCreatorModerator && !message.isDeleted()) {
                            ModeratorBadge()
                        }

                        // Show Muted badge if current user is a moderator viewing a muted creator,
                        // or if the message belongs to the current user and they are muted.
                        val isOwnMessage = message.getCreatorId() == AmityCoreClient.getUserId()
                        if ((isChannelModerator || isOwnMessage) && isCreatorMuted && !message.isDeleted()) {
                            MutedBadge()
                        }
                    }
                    if (message.getState() == AmityMessage.State.SYNCED && !message.isDeleted()) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.amity_ic_livestream_chat_options
                            ),
                            contentDescription = "message options",
                            tint = AmityTheme.colors.baseInverse,
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
                            tint = AmityTheme.colors.baseShade2,
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
                            color = if (message.isDeleted()) AmityTheme.colors.baseShade2 else AmityTheme.colors.baseInverse,
                        ),
                        highlightColor = AmityTheme.colors.baseInverse,
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
                    tint = AmityTheme.colors.baseInverse,
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
fun HostBadge(
    modifier: Modifier = Modifier,
    isCoHost: Boolean = false,
    onCohostBadgeClick: ()-> Unit,
) {
    Row(
        modifier = modifier
            .background(
                color = amityLiveBadgeRed,
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = if (isCoHost) {
                R.drawable.amity_ic_cohost_chat_badge
            } else {
                R.drawable.amity_ic_livestream_host
            }),
            contentDescription = if (isCoHost) { "Co-host badge" } else {  "Host badge" },
            tint = AmityTheme.colors.baseInverse,
            modifier = Modifier
                .size(12.dp)
                .padding(start = 2.dp, end = 2.dp)
                .clickableWithoutRipple {
                    if (isCoHost) {
                        onCohostBadgeClick.invoke()
                    }
                }
        )
        Text(
            text = if (isCoHost) {
                DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cohost")
            } else {
                DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_host")
            },
            color = AmityTheme.colors.baseInverse,
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
                color = AmityTheme.colors.secondaryShade3,
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_moderator_social),
            contentDescription = "Moderator badge",
            tint = AmityTheme.colors.base,
            modifier = Modifier
                .size(width = 12.dp, height = 9.dp)
                .padding(start = 2.dp, top = 1.dp, bottom = 1.dp, end = 1.dp)
        )
        Text(
            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_common_button_moderator"),
            color = AmityTheme.colors.base,
            style = AmityTheme.typography.captionSmall,
            modifier = Modifier.padding(end = 3.dp)
        )
    }
}

@Composable
fun MutedBadge() {
    Icon(
        painter = painterResource(id = R.drawable.amity_ic_mute_user),
        contentDescription = "Muted badge",
        tint = AmityTheme.colors.baseShade2,
        modifier = Modifier
            .size(16.dp)
    )
}

fun getContent(message: AmityMessage): String {
    return if (message.isDeleted()) {
        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_message_deleted")
    } else {
        (message.getData() as? AmityMessage.Data.TEXT)?.getText() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_unsupported_message_type")
    }
}


@Composable
fun AmityLivestreamMessageActionsContainer(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    isChannelModerator: Boolean,
    onDelete: () -> Unit,
    onReport: (String) -> Unit = {},
    onUnreport: (String) -> Unit = {},
    isHostMessage: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        if (message.getCreatorId() != AmityCoreClient.getUserId()) {
            if(!message.isFlaggedByMe()) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_report_comment,
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_report_message"),
                    color = AmityTheme.colors.baseInverse,
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_report_comment_button"),
                ) {
                    onReport(message.getMessageId())
                }
            } else {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_unreport,
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unreport_message"),
                    color = AmityTheme.colors.baseInverse,
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_unreport_comment_button"),
                ) {
                    onUnreport(message.getMessageId())
                }
            }
        }
        if (message.getCreatorId() == AmityCoreClient.getUserId() || (isChannelModerator && !isHostMessage)) {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_delete_story,
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_delete_message"),
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
    user: AmityUser?,
    isModerator: Boolean,
    isMuted: Boolean = false,
    canInviteCohost: Boolean = false,
    onInviteCohost: (String, AmityUser?) -> Unit = { _, _ -> },
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
            .background(AmityTheme.colors.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display name with brand badge and muted icon on the right if muted
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    color = AmityTheme.colors.base,
                    style = AmityTheme.typography.titleBold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, false),
                    overflow = TextOverflow.Ellipsis,
                )

                // Show brand badge if user is a brand
                val isBrandUser = user?.isBrand() == true
                if (isBrandUser) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                        contentDescription = "Brand badge",
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Muted icon if user is muted
                if (isMuted) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_mute_user),
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
                            color = AmityTheme.colors.baseShade3,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_moderator_social),
                        contentDescription = "Moderator badge",
                        tint = AmityTheme.colors.baseInverse,
                        modifier = Modifier
                            .size(width = 12.dp, height = 9.dp)
                            .padding(start = 2.dp, top = 1.dp, bottom = 1.dp, end = 1.dp)
                    )
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_common_button_moderator"),
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
                .background(AmityTheme.colors.baseShade4)
        )

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {

        // Invite as co-host button - only show if canInviteCohost is true
        if (canInviteCohost) {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_invite_cohost_in_chat,
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_invite_as_co_host"),
                color = AmityTheme.colors.base,
            ) {
                onInviteCohost(userId, null)
                onClose()
            }
        }

        // Promote/Demote moderator button - hide promote if user is muted
        if (!isMuted) {
            AmityBottomSheetActionItem(
                icon = if (isModerator) R.drawable.amity_ic_demote_moderator else R.drawable.amity_ic_promote_moderator,
                text = if (isModerator) DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_demote_to_member") else DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_promote_to_moderator"),
                color = AmityTheme.colors.base,
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
                icon = if (isMuted) R.drawable.amity_ic_unmute_user else R.drawable.amity_ic_mute_user,
                text = if (isMuted) DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unmute_user") else DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_mute_user"),
                color = AmityTheme.colors.base,
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
            dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_moderator_promotion"),
            dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_promote_moderator_description"),
            confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_promote"),
            dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
            onConfirmation = {
                showPromoteDialog = false
                viewModel.promoteToModerator(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_user_promoted"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_user_promoted"))
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_promoted_failed"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_promoted_failed"))
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
            dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_moderator_demotion"),
            dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_demote_moderator_description"),
            confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_demote"),
            dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showDemoteDialog = false
                viewModel.demoteToMember(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_demote_success_toast"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_demote_success_toast"))
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_demoted_failed"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_demoted_failed"))
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
            dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_confirm_mute"),
            dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_mute_user_description"),
            confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_mute"),
            dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showMuteDialog = false
                viewModel.muteUser(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_user_muted"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_user_muted"))
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_muted_failed"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_muted_failed"))
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
            dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_confirm_unmute"),
            dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_unmute_user_description"),
            confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unmute"),
            dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
            onConfirmation = {
                showUnmuteDialog = false
                viewModel.unmuteUser(
                    userId = userId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_user_unmuted"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_user_unmuted"))
                        onClose()
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_unmuted_failed"))
                        pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_unmuted_failed"))
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
            pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_message_reported"))
            viewModel.updateSheetUIState(AmityLiveStreamSheetUIState.CloseSheet)
        },
        onError = { error ->
            onError()
            pageScope?.showSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_message_report_failed"), drawableRes = R.drawable.amity_ic_warning)
        }
    )
}

/**
 * Fades the top edge of a scrolling container to transparent so chat messages
 * dissolve as they scroll up off the top instead of clipping abruptly.
 *
 * Uses an offscreen compositing layer so the [BlendMode.DstIn] mask only affects
 * this content and not the video/other UI drawn behind it. (An earlier attempt
 * applied the blend without an offscreen layer, which bled into other UI parts.)
 */
private fun Modifier.topFadingEdge(fadeHeight: Dp = 48.dp): Modifier =
    this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 0f,
                    endY = fadeHeight.toPx(),
                ),
                blendMode = BlendMode.DstIn,
            )
        }
