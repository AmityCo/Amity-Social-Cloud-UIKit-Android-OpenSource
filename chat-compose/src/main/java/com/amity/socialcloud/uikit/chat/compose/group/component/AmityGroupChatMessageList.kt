package com.amity.socialcloud.uikit.chat.compose.group.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.first
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.utils.NoRippleInteractionSource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.elements.CenterConfirmDeletePopup
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatDateSeparator
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityMessageBubble
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityMessageActionMenuAction
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatNewMessageNotification
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatScrollToBottomFab
import com.amity.socialcloud.uikit.chat.compose.message.element.shouldShowDateSeparator
import com.amity.socialcloud.uikit.chat.compose.message.element.saveImageToGallery
import com.amity.socialcloud.uikit.chat.compose.message.element.saveVideoToGallery
import com.amity.socialcloud.uikit.chat.compose.message.report.AmityChatReportOtherReasonContent
import com.amity.socialcloud.uikit.chat.compose.message.report.AmityChatReportMessageDeletedErrorContent
import com.amity.socialcloud.uikit.chat.compose.live.elements.ConfirmDeletePopup
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.chat.compose.message.element.reaction.AmityChatMessageReactionSheet
import com.amity.socialcloud.uikit.common.reaction.AmityMessageReactionListViewModel
import com.amity.socialcloud.uikit.common.reaction.AmityMessageReactionListViewModel.AmityMessageReactionListSheetUIState
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelection
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelectionVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityGroupChatMessageList(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityGroupChatPageViewModel,
    isModerator: Boolean = false,
    memberRoles: Map<String, List<String>> = emptyMap(),
    isUserMuted: Boolean = false,
    jumpToMessageId: String? = null,
) {
    val messages = viewModel.messageList.collectAsLazyPagingItems()
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val reactionListViewModel =
        viewModel<AmityMessageReactionListViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var highestSegment by remember { mutableIntStateOf(0) }
    var newMessage by remember { mutableStateOf<AmityMessage?>(null) }
    var highlightedMessageId by remember { mutableStateOf<String?>(null) }
    var jumpHandled by remember { mutableStateOf(false) }

    val errorReportMessage = amityChatString("chat.toast.message.reported.error")
    val reportSuccessMsg = amityChatString("chat.toast.message.reported")
    val unreportErrorMessage = amityChatString("chat.toast.un.report.message.error")
    val unreportSuccessMessage = amityChatString("chat.toast.un.report.message")
    val copiedMsg = amityChatString("chat.toast.copied")


    var showReportSheet by remember { mutableStateOf(false) }
    var messageToReport by remember { mutableStateOf<AmityMessage?>(null) }
    val reportSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val showDeleteConfirmation by remember { viewModel.showDeleteDialog }

    LaunchedEffect(Unit) {
        snapshotFlow {
            val sourceRefresh = messages.loadState.source.refresh
            val mediatorRefresh = messages.loadState.mediator?.refresh
            Triple(sourceRefresh, mediatorRefresh, messages.itemCount)
        }.first { (sourceRefresh, mediatorRefresh, itemCount) ->
            sourceRefresh is LoadState.NotLoading && (
                itemCount > 0 ||
                mediatorRefresh is LoadState.NotLoading
            )
        }
        viewModel.finishLoading()
    }
    // Start in overflow (reverseLayout) mode so the very first paint anchors the newest message to
    // // the bottom with NO programmatic scrolling — starting in natural mode and flipping after
    // layout // caused a visible "opens at top, then jumps" flash. Once laid out, if the content
    // doesn't fill // the viewport, relax to natural top-down layout so short conversations sit at
    // the top, re-latching // to overflow mode if content later grows past the viewport.
    var contentOverflowsViewport by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        // Wait until the list has actually laid out items, so scrollability is meaningful.
        snapshotFlow { state.layoutInfo.totalItemsCount }.first { it > 0 }
        if (!state.canScrollForward && !state.canScrollBackward) {
            contentOverflowsViewport = false
            snapshotFlow { state.canScrollForward || state.canScrollBackward }.first { it }
            contentOverflowsViewport = true
            if (jumpToMessageId == null || jumpHandled) {
                state.scrollToItem(0)
            }
        }
    }

    // reverseLayout=true means index 0 = newest message, so the paging index from peek() is already
    // // the correct LazyColumn index — no flip needed.
    LaunchedEffect(jumpToMessageId) {
        if (jumpToMessageId == null || jumpHandled) return@LaunchedEffect
        snapshotFlow { messages.loadState.refresh to messages.itemCount }
            .first { (load, count) -> load is LoadState.NotLoading && count > 0 }
        val targetIndex = (0 until messages.itemCount).firstOrNull { i ->
            messages.peek(i)?.getMessageId() == jumpToMessageId
        }
        if (targetIndex != null) {
            state.scrollToItem(targetIndex)
            highlightedMessageId = jumpToMessageId
            jumpHandled = true
        }
    }

    // "Scrolled up" only has meaning once the list is taller than the viewport.
    // In reverseLayout=true, firstVisibleItemIndex == 0 means user is at the bottom.
    val isScrolledUp by remember {
        derivedStateOf {
            contentOverflowsViewport && (
                state.firstVisibleItemIndex > 1 ||
                (state.firstVisibleItemIndex == 1 && state.firstVisibleItemScrollOffset > 0)
            )
        }
    }

    // Watch for new messages by tracking segment changes (distinctUntilChanged avoids re-fires)
    LaunchedEffect(Unit) {
        snapshotFlow { messages.itemSnapshotList.firstOrNull() }
            .map { it?.getSegment() ?: 0 }
            .distinctUntilChanged()
            .collect { segment ->
                if (segment > highestSegment) {
                    highestSegment = segment
                    val firstMsg = messages.itemSnapshotList.firstOrNull()
                    val isOwnMessage = firstMsg?.getCreator()?.getUserId() == AmityCoreClient.getUserId()
                    if (!isScrolledUp || isOwnMessage) {
                        // Only scroll programmatically in overflow mode (reverseLayout=true).
                        // In non-overflow, the newest message is already visible at the bottom.
                        if (contentOverflowsViewport) {
                            scope.launch { state.scrollToItem(0) }
                        }
                        if (firstMsg != null) viewModel.markMessageAsRead(firstMsg)
                        newMessage = null
                    } else {
                        newMessage = firstMsg
                    }
                }
            }
    }

    LaunchedEffect(isScrolledUp) {
        if (!isScrolledUp) {
            newMessage = null
        }
    }

    AmityBaseComponent(
        componentId = "message_list",
        pageScope = pageScope,
    ) {
        val loadState = messages.loadState.refresh
        val isLoading = loadState is LoadState.Loading
        val isError = loadState is LoadState.Error

        if (isError) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = amityChatString("chat.load.error"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.token(AmityColorToken.TextEmptyStateTitleDefault),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        } else if (isLoading && messages.itemCount == 0) {
            // Blank fill during initial load: AmityGroupChatPage already shows a "Loading chat"
            // toast via // showProgressSnackbar, so rendering the skeleton here too caused both to
            // appear at once. Mirrors // AmityChatMessageList.
            Box(modifier = Modifier.fillMaxSize())
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // Non-overflow: natural top flow, oldest first, top-aligned when few messages.
                    // Overflow: reverseLayout anchors newest to bottom — iMessage/WhatsApp behaviour.
                    reverseLayout = contentOverflowsViewport,
                    contentPadding = PaddingValues(bottom = 8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .nestedScroll(rememberNestedScrollInteropConnection()),
                    state = state,
                ) {
                    items(
                        count = messages.itemCount,
                        key = { index ->
                            val actualIndex = if (contentOverflowsViewport) index
                                              else messages.itemCount - 1 - index
                            messages.peek(actualIndex)?.getMessageId() ?: index
                        }
                    ) { index ->
                        val actualIndex = if (contentOverflowsViewport) index
                                          else messages.itemCount - 1 - index
                        if (actualIndex < 0 || actualIndex >= messages.itemCount) return@items
                        messages[actualIndex]?.let { message ->
                        val isOwnMessage = message.getCreatorId() == AmityCoreClient.getUserId()
                        val isSenderModerator = memberRoles[message.getCreatorId()]
                            ?.any { it.contains(AmityConstants.CHANNEL_MODERATOR_ROLE) } == true

                        // Overflow (reverseLayout=true): separator AFTER bubble (visually above).
                        // //   Check NEXT paging item (older with LAST_CREATED: index+1). // Non-
                        // overflow (reverseLayout=false): separator BEFORE bubble. //   Check PREV
                        // paging item (older = actualIndex+1 in LAST_CREATED).
                        val currentDate = message.getCreatedAt()?.millis ?: 0L
                        val showSeparatorBefore: Boolean
                        val showSeparatorAfter: Boolean
                        if (contentOverflowsViewport) {
                            val isLastItem = index + 1 >= messages.itemCount
                            val nextMsg = if (!isLastItem) messages.peek(index + 1) else null
                            val nextDate = nextMsg?.getCreatedAt()?.millis
                            showSeparatorBefore = false
                            showSeparatorAfter = isLastItem ||
                                (nextDate != null && shouldShowDateSeparator(currentDate, nextDate))
                        } else {
                            val prevPagingIndex = actualIndex + 1
                            val isOldestLoaded = prevPagingIndex >= messages.itemCount
                            val prevMsg = if (!isOldestLoaded) messages.peek(prevPagingIndex) else null
                            val prevDate = prevMsg?.getCreatedAt()?.millis
                            showSeparatorBefore = isOldestLoaded ||
                                (prevDate != null && shouldShowDateSeparator(currentDate, prevDate))
                            showSeparatorAfter = false
                        }
                        if (showSeparatorBefore) {
                            AmityChatDateSeparator(
                                dateTime = message.getCreatedAt() ?: org.joda.time.DateTime.now(),
                            )
                        }

                        AmityMessageBubble(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            message = message,
                            isSenderModerator = isSenderModerator,
                            isUserMuted = isUserMuted,
                            onAddReaction = { msg, reactionName ->
                                viewModel.addReaction(msg, reactionName)
                            },
                            onRemoveReaction = { msg, reactionName ->
                                viewModel.removeReaction(msg, reactionName)
                            },
                            onOpenReactions = { msg ->
                                reactionListViewModel.updateSheetUIState(
                                    AmityMessageReactionListSheetUIState.OpenSheet(msg)
                                )
                            },
                            optionAction = AmityMessageActionMenuAction(
                                onReply = {
                                    viewModel.setReplyToMessage(message)
                                },
                                onEdit = {
                                    viewModel.startEditingMessage(message)
                                },
                                onCopy = {
                                    val data = message.getData()
                                    if (data is AmityMessage.Data.TEXT) {
                                        clipboardManager.setText(AnnotatedString(data.getText()))
                                        AmityUIKitSnackbar.publishSnackbarMessage(copiedMsg)
                                    }
                                },
                                onDelete = if (isOwnMessage || isModerator) {
                                    {
                                        viewModel.showDeleteConfirmation(message)
                                    }
                                } else null,
                                onReport = if (!isOwnMessage) {
                                    {
                                        messageToReport = message
                                        showReportSheet = true
                                    }
                                } else null,
                                onUnreport = if (!isOwnMessage) {
                                    { viewModel.unflagMessage(
                                        message = message,
                                        onSuccess = {
                                            AmityUIKitSnackbar.publishSnackbarMessage(
                                                unreportSuccessMessage
                                            )
                                        },
                                        onError = {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                unreportErrorMessage
                                            )
                                        }
                                    ) }
                                } else null,
                                onSave = when (message.getData()) {
                                    is AmityMessage.Data.IMAGE -> {
                                        { scope.launch { saveImageToGallery(context, message) } }
                                    }
                                    // Video save is intentionally omitted, not an oversight.
                                    // Re-enable it together with the media-preview dialog's
                                    // own video-save gate, or the two surfaces disagree.
                                    else -> null
                                },
                            ),
                            onCancelUpload = { msg ->
                                viewModel.cancelUpload(msg)
                            },
                            onResend = { msg ->
                                viewModel.resendMessage(msg)
                            },
                            onDelete = { msg ->
                                viewModel.deleteFailedMessage(msg)
                            },
                            parentMessageFlow = message.getParentId()?.let { parentId ->
                                remember(parentId) { viewModel.getMessage(parentId) }
                            },
                            isGroupChat = true,
                            isHighlighted = highlightedMessageId == message.getMessageId(),
                        )

                        if (showSeparatorAfter) {
                            AmityChatDateSeparator(
                                dateTime = message.getCreatedAt() ?: org.joda.time.DateTime.now(),
                            )
                        }
                    }
                    }
                }

                // New message notification (takes priority over FAB)
                AmityChatNewMessageNotification(
                    message = newMessage,
                    onClick = {
                        newMessage = null
                        scope.launch { state.animateScrollToItem(0) }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )

                AmityChatScrollToBottomFab(
                    visible = isScrolledUp && newMessage == null,
                    onClick = {
                        scope.launch { state.animateScrollToItem(0) }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 8.dp),
                )
            }
        }

        AmityChatMessageReactionSheet()

        if (showDeleteConfirmation) {
            CenterConfirmDeletePopup(
                pageScope = pageScope,
                onCancel = {
                    viewModel.dismissDeleteConfirmation()
                },
                onDelete = {
                    viewModel.deleteMessage(
                        onError = {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                DefaultAmityChatStringProvider.getInstance().getString("chat.toast.delete.error")
                            )
                        }
                    )
                }
            )
        }

        if (showReportSheet) {
            var isMessageDeleted by remember { mutableStateOf(false) }

            LaunchedEffect(messageToReport) {
                messageToReport?.let { msg ->
                    viewModel.getMessage(msg.getMessageId()).collect { updatedMsg ->
                        if (updatedMsg.isDeleted()) {
                            isMessageDeleted = true
                        }
                    }
                }
            }

            AmitySheet(
                onDismissRequest = {
                    showReportSheet = false
                    messageToReport = null
                },
                sheetState = reportSheetState,
            ) {
                AmityGroupChatMessageReportContent(
                    isMessageDeleted = isMessageDeleted,
                    onCloseClick = {
                        showReportSheet = false
                        messageToReport = null
                    },
                    onSubmitClick = { reason, onError ->
                        messageToReport?.let { message ->
                            viewModel.flagMessageWithReason(
                                message = message,
                                reason = reason,
                                onSuccess = {
                                    showReportSheet = false
                                    messageToReport = null
                                    AmityUIKitSnackbar.publishSnackbarMessage(
                                        reportSuccessMsg
                                    )
                                },
                                onError = {
                                    showReportSheet = false
                                    messageToReport = null
                                    onError()
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(errorReportMessage)
                                },
                            )
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmityGroupChatMessageReportContent(
    isMessageDeleted: Boolean = false,
    onCloseClick: () -> Unit = {},
    onSubmitClick: (AmityContentFlagReason, () -> Unit) -> Unit = { _, _ -> },
) {
    if (isMessageDeleted) {
        AmityChatReportMessageDeletedErrorContent(onCloseClick = onCloseClick)
        return
    }

    var showOtherReason by remember { mutableStateOf(false) }

    if (showOtherReason) {
        AmityChatReportOtherReasonContent(
            onBackClick = { showOtherReason = false },
            onSubmitClick = { detail, onError ->
                onSubmitClick(AmityContentFlagReason.Others(detail), onError)
            },
            onCloseClick = onCloseClick,
        )
    } else {
        AmityGroupChatMessageReportReasonList(
            onCloseClick = onCloseClick,
            onSubmitClick = onSubmitClick,
            onOthersClick = { showOtherReason = true },
        )
    }
}

@Composable
private fun AmityGroupChatMessageReportReasonList(
    onCloseClick: () -> Unit = {},
    onSubmitClick: (AmityContentFlagReason, () -> Unit) -> Unit = { _, _ -> },
    onOthersClick: () -> Unit = {},
) {
    val (selectedReason, onReasonSelected) = remember { mutableStateOf<AmityContentFlagReason?>(null) }
    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = amityChatString("chat.report.title"),
                style = AmityTheme.typography.titleBold,
                modifier = Modifier.align(Alignment.Center),
                color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickableWithoutRipple { onCloseClick() }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(CommonR.drawable.amity_ic_cross_r),
                    contentDescription = "cancel_report_button",
                    tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                )
            }
        }

        AmityDivider(variant = AmityDividerVariant.Post)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = amityChatString("chat.report.description"),
                    style = AmityTheme.typography.body,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 12.dp),
                    color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTextDescriptionDefault),
                )
            }

            item {
                val radioOptions = AmityContentFlagReason.list().dropLast(1)

                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { reportReason ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp)
                                .selectable(
                                    selected = (reportReason == selectedReason),
                                    interactionSource = NoRippleInteractionSource(),
                                    indication = null,
                                    onClick = { onReasonSelected(reportReason) },
                                    role = Role.RadioButton,
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = reportReason.reason,
                                style = AmityTheme.typography.bodyBold,
                                color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                            )

                            AmitySelection(
                                variant = AmitySelectionVariant.RADIO,
                                isSelected = (reportReason == selectedReason),
                                value = reportReason.reason,
                                onChange = { _, _ -> onReasonSelected(reportReason) },
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(start = 16.dp, end = 24.dp)
                        .clickableWithoutRipple { onOthersClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = amityChatString("chat.report.others"),
                        style = AmityTheme.typography.bodyBold,
                        color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                    )

                    Icon(
                        painterResource(CommonR.drawable.amity_ic_chevron_right),
                        tint = AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault),
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .height(18.dp),
                    )
                }
            }
        }

        AmityDivider(variant = AmityDividerVariant.Post)

        AmityButton(
            variant = AmityButtonVariant.MAIN,
            onClick = {
                selectedReason?.let {
                    setButtonEnabled(false)
                    onSubmitClick(it) {
                        setButtonEnabled(true)
                    }
                }
            },
            label = amityChatString("chat.report.submit"),
            enabled = selectedReason != null && isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}
