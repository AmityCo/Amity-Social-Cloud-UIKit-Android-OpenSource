package com.amity.socialcloud.uikit.chat.compose.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.config.AmityChatConfigHelper
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityUserAvatarView
import com.amity.socialcloud.uikit.chat.compose.message.component.AmityChatMessageList
import com.amity.socialcloud.uikit.chat.compose.message.composer.AmityMessageComposer
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatHeaderSkeleton
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatWaitingForNetworkRow
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.chat.compose.message.element.LocalSentVideoUris
import androidx.compose.runtime.CompositionLocalProvider
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenDialog
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl

@Composable
fun AmityChatPage(
    modifier: Modifier = Modifier,
    channelId: String,
    jumpToMessageId: String? = null,
) {
    val viewModel = remember { AmityChatPageViewModel(channelId, jumpToMessageId) }
    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val otherMembers by viewModel.getOtherMember().collectAsState(initial = emptyList())
    val isMuted by viewModel.isMuted.collectAsState()
    val isUserReported by viewModel.isUserReported.collectAsState()
    val isUserBlocked by viewModel.isUserBlocked.collectAsState()
    val isFetching by viewModel.isFetching.collectAsState()
    val connection by viewModel.getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)
    val context = LocalContext.current
    var showActionSheet by remember { mutableStateOf(false) }
    val hasAnyUserAction = remember { AmityChatConfigHelper.hasAnyEnabledChatUserAction() }
    var showBlockConfirm by remember { mutableStateOf(false) }
    var showUnblockConfirm by remember { mutableStateOf(false) }
    var showAvatarFullScreen by remember { mutableStateOf(false) }
    val headerAvatarUrl = otherMembers.firstOrNull()?.getUser()?.resolvedAvatarUrl(AmityImage.Size.LARGE)
        ?: channel?.getAvatar()?.getUrl(AmityImage.Size.LARGE)

    // Fetch follow/block info when other member is known
    val otherUserId = otherMembers.firstOrNull()?.getUserId()
    LaunchedEffect(otherUserId) {
        if (otherUserId != null) {
            viewModel.fetchFollowInfo(otherUserId)
        }
    }

    // Snackbar messages
    val muteMessage = amityChatString("chat.action.mute")
    val muteErrorMessage = amityChatString("chat.action.mute.failed")
    val unmuteMessage = amityChatString("chat.action.unmute")
    val unmuteErrorMessage = amityChatString("chat.action.unmute.failed")
    val blockSuccessMsg = amityChatString("chat.block.success")
    val unblockSuccessMsg = amityChatString("chat.unblock.success")
    val blockErrorMsg = amityChatString("chat.block.failed")
    val unblockErrorMsg = amityChatString("chat.unblock.failed")
    val successUserUnreported = amityChatString("chat.action.unreport.user.success")
    val errorUnreportUser = amityChatString("chat.action.unreport.user.failed")
    val successUserReported = amityChatString("chat.action.report.user.success")
    val errorReportUser = amityChatString("chat.action.report.user.failed")

    val behavior = remember {
        AmityChatBehaviorHelper.conversationChatPageBehavior
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onStop()
        }
    }

    val sentVideoUris by viewModel.sentVideoUris.collectAsState()

    AmityBasePage(pageId = "chat_page") {
        CompositionLocalProvider(LocalSentVideoUris provides sentVideoUris) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header
            val headerDisplayName = otherMembers.firstOrNull()?.getUser()?.getDisplayName() ?: channel?.getDisplayName() ?: ""
            val isHeaderLoading = headerDisplayName.isEmpty() && headerAvatarUrl == null

            if (isHeaderLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = { (context as? android.app.Activity)?.finish() }),
                        tint = AmityTheme.colors.base,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AmityChatHeaderSkeleton()
                }
            } else {
                ConversationChatHeader(
                    displayName = headerDisplayName,
                    avatarUrl = headerAvatarUrl,
                    isUserDeleted = otherMembers.firstOrNull()?.getUser()?.isDeleted() == true,
                    connection = connection,
                    showMoreButton = hasAnyUserAction,
                    onBack = {
                        (context as? android.app.Activity)?.finish()
                    },
                    onAvatarClick = {
                        showAvatarFullScreen = true
                    },
                    onMoreClick = {
                        showActionSheet = true
                    },
                )
            }

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            // Message list
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                AmityChatMessageList(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                    jumpToMessageId = jumpToMessageId,
                )
                // Show/dismiss only on state CHANGE — calling these in the composition
                // body re-fired them on every recomposition, and showProgressSnackbar
                // dismisses the current snackbar first, making the indicator blink.
                val pageScope = getPageScope()
                val loadingLabel = amityChatString("chat.label.loading.chat")
                LaunchedEffect(isFetching) {
                    if (isFetching) {
                        pageScope.showProgressSnackbar(loadingLabel)
                    } else {
                        pageScope.dismissSnackbar()
                    }
                }
            }

            // Composer or blocked banner
            if (isUserBlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.baseShade4)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .imePadding()
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = amityChatString("chat.blocked.message"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.baseShade2,
                        ),
                    )
                }
            } else {
                AmityMessageComposer(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }
        }

        // Bottom sheet for user actions
        if (showActionSheet) {
            AmityConversationChatUserActionSheet(
                isMuted = isMuted,
                isUserReported = isUserReported,
                isUserBlocked = isUserBlocked,
                onMuteToggle = {
                    viewModel.toggleMute(
                        onSuccess = { isMutedState ->
                            val message = if (isMutedState) muteMessage else unmuteMessage
                            AmityUIKitSnackbar.publishSnackbarMessage(message)
                        },
                        onError = {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                if (isMuted) unmuteErrorMessage
                                else muteErrorMessage
                            )
                        }
                    )
                },
                onReportToggle = {
                    val userId = otherMembers.firstOrNull()?.getUserId() ?: return@AmityConversationChatUserActionSheet
                    if (isUserReported) {
                        viewModel.unreportUser(
                            userId,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(successUserUnreported)
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUnreportUser)
                            }
                        )
                    } else {
                        viewModel.reportUser(
                            userId,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(successUserReported)
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(errorReportUser)
                            }
                        )
                    }
                },
                onBlockToggle = {
                    if (isUserBlocked) showUnblockConfirm = true else showBlockConfirm = true
                },
                onDismiss = {
                    showActionSheet = false
                },
            )
        }

        // Block confirmation dialog
        val otherDisplayName = otherMembers.firstOrNull()?.getUser()?.getDisplayName() ?: ""
        if (showBlockConfirm) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.block.confirm.title"),
                message = amityChatString("chat.block.confirm.message", otherDisplayName),
                confirmLabel = amityChatString("chat.block.confirm.label"),
                onConfirm = {
                    showBlockConfirm = false
                    val userId = otherMembers.firstOrNull()?.getUserId() ?: return@AmityChatConfirmDialog
                    viewModel.blockUser(
                        userId,
                        onSuccess = { AmityUIKitSnackbar.publishSnackbarMessage(blockSuccessMsg) },
                        onError = { AmityUIKitSnackbar.publishSnackbarMessage(blockErrorMsg) },
                    )
                },
                onDismiss = { showBlockConfirm = false },
            )
        }

        // Unblock confirmation dialog
        if (showUnblockConfirm) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.unblock.confirm.title"),
                message = amityChatString("chat.unblock.confirm.message", otherDisplayName),
                confirmLabel = amityChatString("chat.unblock.confirm.label"),
                onConfirm = {
                    showUnblockConfirm = false
                    val userId = otherMembers.firstOrNull()?.getUserId() ?: return@AmityChatConfirmDialog
                    viewModel.unblockUser(
                        userId,
                        onSuccess = { AmityUIKitSnackbar.publishSnackbarMessage(unblockSuccessMsg) },
                        onError = { AmityUIKitSnackbar.publishSnackbarMessage(unblockErrorMsg) },
                    )
                },
                onDismiss = { showUnblockConfirm = false },
            )
        }

        if (showAvatarFullScreen) {
            AmityAvatarFullScreenDialog(
                avatarUrl = headerAvatarUrl,
                onDismiss = { showAvatarFullScreen = false },
            )
        }
        }
    }
}

@Composable
private fun ConversationChatHeader(
    modifier: Modifier = Modifier,
    displayName: String,
    avatarUrl: String?,
    isUserDeleted: Boolean = false,
    connection: NetworkConnectionEvent = NetworkConnectionEvent.Connected,
    showMoreButton: Boolean = true,
    onBack: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Back button
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBack),
            tint = AmityTheme.colors.base,
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Avatar — always use circular user avatar for conversations
        AmityUserAvatarView(
            modifier = Modifier.clickable(onClick = onAvatarClick),
            avatarUrl = avatarUrl,
            displayName = displayName,
            isDeleted = isUserDeleted,
            size = 36,
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Channel name + waiting-for-network subtitle
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = displayName,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.baseInverse,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (connection is NetworkConnectionEvent.Disconnected) {
                AmityChatWaitingForNetworkRow()
            }
        }

        // Meatball menu (three-dot) — hidden when all user actions are disabled
        if (showMoreButton) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_more),
                contentDescription = "More options",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onMoreClick),
                tint = AmityTheme.colors.base,
            )
        }
    }
}
