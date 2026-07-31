package com.amity.socialcloud.uikit.chat.compose.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.chat.compose.group.component.AmityGroupChatMessageList
import com.amity.socialcloud.uikit.chat.compose.group.composer.AmityGroupChatMessageComposer
import com.amity.socialcloud.uikit.chat.compose.group.composer.GroupMentionSuggestionView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenDialog
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatHeaderSkeleton
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatWaitingForNetworkRow
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.chat.compose.message.element.LocalSentVideoUris
import androidx.compose.runtime.CompositionLocalProvider
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.collections.any
import kotlin.text.contains

@Composable
fun AmityGroupChatPage(
    modifier: Modifier = Modifier,
    channelId: String,
    jumpToMessageId: String? = null,
) {
    val viewModel = remember {
        AmityGroupChatPageViewModel(channelId, jumpToMessageId)
    }
    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val isFetching by viewModel.isFetching.collectAsState()
    val context = LocalContext.current

    val behavior = remember {
        AmityChatBehaviorHelper.groupChatPageBehavior
    }

    val memberRoles by remember {
        viewModel.getMemberRoles()
    }.collectAsState(initial = emptyMap())

    val membership by remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isBanned() == new.isBanned()
                        && old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val isChannelMuted by remember {
        viewModel.observeChannelMuted()
    }.collectAsState(initial = false)

    val connection by viewModel.getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)


    val isUserMuted = membership?.isMuted() == true
    val headerAvatarUrl = channel?.getAvatar()?.getUrl(AmityImage.Size.LARGE)
    var showAvatarFullScreen by remember { mutableStateOf(false) }

    // Mention suggestion state (hoisted from composer)
    var showMentionSuggestion by remember { mutableStateOf(false) }
    var mentionQueryToken by remember { mutableStateOf("") }
    var mentionSuggestionSelected by remember { mutableStateOf<AmityMentionSuggestion?>(null) }

    val sentVideoUris by viewModel.sentVideoUris.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onStop()
        }
    }

    AmityBasePage(pageId = "group_chat_page", useAmityToast = true) {
        CompositionLocalProvider(LocalSentVideoUris provides sentVideoUris) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfaceListDefaultDefault)),
        ) {
            // Header — tapping navigates to group settings
            val headerDisplayName = if (membership?.isBanned() == true) {
                amityChatString("chat.error.banned.chat.navbar.title")
            } else {
                channel?.getDisplayName() ?: ""
            }
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
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_chevron_left),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = { (context as? android.app.Activity)?.finish() }),
                        tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AmityChatHeaderSkeleton()
                }
            } else {
                GroupChatHeader(
                    displayName = headerDisplayName,
                    avatarUrl = headerAvatarUrl,
                    connection = connection,
                    onBack = {
                        (context as? android.app.Activity)?.finish()
                    },
                    onAvatarClick = {
                        showAvatarFullScreen = true
                    },
                    onHeaderTap = {
                        behavior.goToGroupSetting(context, channelId)
                    },
                    isBanned = membership?.isBanned() == true,
                )
            }

            AmityDivider(variant = AmityDividerVariant.Post)

            if (membership?.isBanned() == true) {
                // Full page banned view
                AmityEmptyState(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    variant = AmityEmptyStateVariant.ICON,
                    icon = CommonR.drawable.amity_ic_comment_exclamation_l,
                    title = amityChatString("chat.error.banned.chat.title"),
                    description = amityChatString("chat.error.banned.chat.sub.title"),
                )
            } else {
                // Message list
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    AmityGroupChatMessageList(
                        pageScope = getPageScope(),
                        viewModel = viewModel,
                        isModerator = memberRoles[AmityCoreClient.getUserId()]?.any { it.contains(AmityConstants.CHANNEL_MODERATOR_ROLE) } == true,
                        memberRoles = memberRoles,
                        isUserMuted = isUserMuted,
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

                    // Floating mention suggestion (overlays bottom of message list with gap)
                    if (showMentionSuggestion) {
                        // Tap interceptor: dismiss mention suggestion when tapping message list area
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        showMentionSuggestion = false
                                    }
                                }
                        )

                        GroupMentionSuggestionView(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            keyword = mentionQueryToken,
                            viewModel = viewModel,
                            onClick = {
                                mentionSuggestionSelected = it
                                showMentionSuggestion = false
                            },
                            onClose = {
                                showMentionSuggestion = false
                            },
                        )
                    }
                }

                // Composer with mute / banned banner handling
                AmityGroupChatMessageComposer(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                    isModerator = memberRoles[AmityCoreClient.getUserId()]?.any { it.contains(AmityConstants.CHANNEL_MODERATOR_ROLE) } == true,
                    isUserMuted = isUserMuted,
                    isUserBanned = membership?.isBanned() == true,
                    isChannelMuted = isChannelMuted,
                    mentionSuggestionSelected = mentionSuggestionSelected,
                    onMentionSuggestionConsumed = { mentionSuggestionSelected = null },
                    onMentionQueryChanged = { show, query ->
                        showMentionSuggestion = show
                        mentionQueryToken = query
                    },
                )
            }
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
private fun GroupChatHeader(
    modifier: Modifier = Modifier,
    displayName: String,
    avatarUrl: String?,
    connection: NetworkConnectionEvent = NetworkConnectionEvent.Connected,
    isBanned: Boolean = false,
    onBack: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onHeaderTap: () -> Unit = {},
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
            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_chevron_left),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBack),
            tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Tappable header area → navigates to group settings
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onHeaderTap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AmityAvatar(
                variant = if (!isBanned && !avatarUrl.isNullOrEmpty()) AmityAvatarVariant.Image else AmityAvatarVariant.Icon,
                imageUrl = avatarUrl.takeUnless { isBanned },
                icon = CommonR.drawable.amity_ic_comments_alt_s,
                style = AmityAvatarStyle.Squared,
                size = AmityAvatarSize.Size40,
                onClick = onAvatarClick,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = displayName,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBanned) AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault)
                                else AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (connection is NetworkConnectionEvent.Disconnected) {
                    AmityChatWaitingForNetworkRow()
                }
            }
        }
    }
}