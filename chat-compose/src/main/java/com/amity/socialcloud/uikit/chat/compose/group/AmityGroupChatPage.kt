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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.AsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import androidx.compose.foundation.Image
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.group.component.AmityGroupChatMessageList
import com.amity.socialcloud.uikit.chat.compose.group.composer.AmityGroupChatMessageComposer
import com.amity.socialcloud.uikit.chat.compose.group.composer.GroupMentionSuggestionView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenDialog
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatHeaderSkeleton
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatWaitingForNetworkRow
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
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

    AmityBasePage(pageId = "group_chat_page") {
        CompositionLocalProvider(LocalSentVideoUris provides sentVideoUris) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
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

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            if (membership?.isBanned() == true) {
                // Full page banned view
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_chat_banned_group_page),
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = amityChatString("chat.error.banned.chat.title"),
                            style = AmityTheme.typography.titleBold,
                            color = AmityTheme.colors.baseShade3,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = amityChatString("chat.error.banned.chat.sub.title"),
                            style = AmityTheme.typography.caption,
                            color = AmityTheme.colors.baseShade3,
                            modifier = Modifier.padding(horizontal = 64.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBack),
            tint = AmityTheme.colors.base,
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Tappable header area → navigates to group settings
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onHeaderTap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(avatarUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build()
            )
            val painterState by painter.state.collectAsState()

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onAvatarClick)
            ) {
                if (isBanned) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(AmityTheme.colors.secondaryShade2),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                } else {
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Group Avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    if (painterState !is AsyncImagePainter.State.Success) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(AmityTheme.colors.primaryShade3),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = displayName,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isBanned) AmityTheme.colors.baseShade2
                                else AmityTheme.colors.baseInverse,
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