package com.amity.socialcloud.uikit.community.compose.livestream.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.presentation.AmityVideoPlayer
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamMessageComposeBar
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ChatOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReaction
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReactionsOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ReactionPicker
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.EXTRA_PARAM_LIVESTREAM_ERROR_TYPE
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamDisconnectedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamEndedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamLoadingView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamPostLiveLabel
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow.Companion as TextOverflow1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityLivestreamPlayerPage(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityLivestreamPlayerViewModel>(
        factory = AmityLivestreamPlayerViewModel.create(post),
        viewModelStoreOwner = viewModelStoreOwner
    )
    val state by remember { viewModel.liveStreamFullScreenState }.collectAsState()

    var showReactionPicker by remember { mutableStateOf(false) }
    val floatingReactions = remember { mutableStateListOf<FloatingReaction>() }
    var messageText by remember { mutableStateOf("") }
    val isTargetCommunity by remember(post) {
        mutableStateOf(post.getTarget() is AmityPost.Target.COMMUNITY)
    }

    val bottomSheetState = rememberModalBottomSheetState()

    val clipboardManager = LocalClipboardManager.current

    var showBottomSheet by remember { mutableStateOf(false) }


    val reactions by viewModel.observeLiveReactions(post.getPostId()).collectAsState(emptyList())
    LaunchedEffect(reactions) {
        reactions.map {
            AmityMessageReactions.toReaction(it.getReactionName())?.let { reaction ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Add random delay between 0-4 s to create visual jitter effect
                    delay((Math.random() * 4000).toLong())
                    withContext(Dispatchers.Main) {
                        floatingReactions.add(
                            FloatingReaction(
                                reaction = reaction,
                                id = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    DisposableEffectWithLifeCycle(
        onStart = {
            viewModel.subscribePostRT(state.post)
        },
        onDestroy = {
            viewModel.unSubscribePostRT(state.post)
        }
    )

    LaunchedEffect(state.post.isDeleted()) {
        if (state.post.isDeleted()) {
            closePageWithLivestreamError(
                context,
                LivestreamErrorScreenType.DELETED
            )
        }
    }

    val connection by viewModel
        .getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)
    if (state.isBanned == true && state.stream?.getStatus() == AmityStream.Status.LIVE) {
        AmityLivestreamBannedPage(
            onOkClick = {
                context.closePageWithResult(Activity.RESULT_OK)
            }
        )
        return
    }
    if (state.reviewStatus == AmityReviewStatus.DECLINED) {
        AmityLivestreamDeclinedPage(
            onOkClick = {
                context.closePageWithResult(Activity.RESULT_OK)
            }
        )
        return
    }
    AmityBasePage(pageId = "live_stream_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "stream_player",
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                val streamStatus = state.stream?.getStatus()

                val terminateLabels =
                    state.stream?.getStreamModeration()?.getTerminateLabels() ?: emptyList()
                val isUnavailable = state.error != null || state.stream?.isDeleted() == true
                val streamId = state.stream?.getStreamId()
                val isDisconnected =
                    connection == NetworkConnectionEvent.Disconnected && streamStatus == AmityStream.Status.LIVE
                if (terminateLabels.isNotEmpty() || streamStatus == AmityStream.Status.ENDED || isUnavailable || streamId == null) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (streamId == null) {
                            "stream_loading"
                        } else if (terminateLabels.isNotEmpty()) {
                            "stream_terminated"
                        } else if (streamStatus == AmityStream.Status.ENDED) {
                            "stream_ended"
                        } else if (isDisconnected) {
                            "stream_disconnected"
                        } else {
                            "stream_unavailable"
                        }
                    ) {
                        Box(
                            contentAlignment = Alignment.TopStart,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        ) {
                            if (streamId == null) {
                                AmityLivestreamLoadingView()
                            } else if (terminateLabels.isNotEmpty()) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.TERMINATED
                                )
                            } else if (streamStatus == AmityStream.Status.ENDED) {
                                AmityLivestreamEndedView()
                            } else if (isUnavailable) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.DELETED
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 52.dp, start = 36.dp)
                                    .wrapContentSize()
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.amity_ic_close
                                    ),
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(4.dp)
                                        .clickableWithoutRipple {
                                            context.closePageWithResult(Activity.RESULT_OK)
                                        }
                                        .testTag(getAccessibilityId()),
                                )
                            }
                        }
                    }
                } else {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = ""
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                        ) {
                            AndroidView(
                                modifier = modifier.fillMaxSize(),
                                factory = { context: Context ->
                                    AmityVideoPlayer(context = context).apply {
                                        setOnClose {
                                            context.closePageWithResult(Activity.RESULT_OK)
                                        }
                                    }
                                },
                                update = { amityVideoPlayer: AmityVideoPlayer ->
                                    amityVideoPlayer.enableStopWhenPause()
                                    amityVideoPlayer.play(streamId)
                                },
                                onRelease = {
                                    it.stop()
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = 60.dp)
                            ) {
                                CommunityLivestreamPlayerHeader(
                                    stream = state.stream,
                                    onOptionsClick = {
                                        showBottomSheet = true
                                    })
                            }
                            if (isDisconnected) {
                                AmityLivestreamDisconnectedView()
                            }
                        }

                    }
                }
                if (streamStatus == AmityStream.Status.LIVE && isTargetCommunity) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .imePadding()
                            .align(Alignment.BottomStart)
                            .fillMaxSize()
                    ) {
                        // Floating reactions animation
                        FloatingReactionsOverlay(
                            reactions = floatingReactions,
                            modifier = Modifier
                                .height(182.dp)
                                .width(120.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        // Chat overlay
                        ChatOverlay(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                                .drawWithContent {
                                    drawContent()
                                    // Draw fade to transparent at top
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White, // Use white for fade mask
                                                Color.Transparent,
                                                Color.Transparent,
                                            ),
                                            startY = 0f,
                                            endY = 120.dp.toPx()
                                        ),
                                        blendMode = BlendMode.DstOut
                                    )
                                },
                            pageScope = getPageScope(),
                            channelId = state.stream?.getChannelId() ?: "",
                            onReactionClick = { showReactionPicker = true }
                        )
                        AmityLivestreamMessageComposeBar(
                            pageScope = getPageScope(),
                            channelId = state.stream?.getChannelId() ?: "",
                            value = messageText,
                            isPendingApproval = state.reviewStatus == AmityReviewStatus.UNDER_REVIEW,
                            onValueChange = {
                                messageText = it
                            },
                            onSend = {},
                            onReactionClick = {
                                (AmityMessageReactions.getList().getOrNull(1)
                                    ?: AmityMessageReactions.getList().firstOrNull())
                                    ?.let { defaultReaction ->
                                        floatingReactions.add(
                                            FloatingReaction(
                                                reaction = defaultReaction,
                                                id = System.currentTimeMillis()
                                            )
                                        )
                                        state.stream?.let { stream ->
                                            AmityCoreClient.newLiveReactionRepository()
                                                .createReaction(
                                                    liveStreamId = stream.getStreamId(),
                                                    referenceId = state.post.getPostId(),
                                                    referenceType = AmityLiveReactionReferenceType.POST,
                                                    reactionName = defaultReaction.name,
                                                )
                                        }
                                    }
                            },
                            onReactionLongClick = { showReactionPicker = true },
                        )
                    }

                    // Reaction picker popup
                    if (showReactionPicker) {
                        ReactionPicker(
                            onReactionSelected = { reaction ->
                                // Add floating reaction
                                floatingReactions.add(
                                    FloatingReaction(
                                        reaction = reaction,
                                        id = System.currentTimeMillis()
                                    )
                                )
                                state.stream?.let {
                                    AmityCoreClient.newLiveReactionRepository()
                                        .createReaction(
                                            liveStreamId = it.getStreamId(),
                                            referenceId = state.post.getPostId(),
                                            referenceType = AmityLiveReactionReferenceType.POST,
                                            reactionName = reaction.name,
                                        )
                                }
                            },
                            onDismiss = { showReactionPicker = false },
                            modifier = Modifier.padding(bottom = 56.dp, end = 16.dp)
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = bottomSheetState,
            containerColor = Color(0xFF191919),
            contentColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 20.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            Color.Gray,
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier.navigationBarsPadding()
            ) {
                val postLink = AmityUIKitConfigController.getPostLink(post)

                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_v4_link_icon,
                    text = "Copy live stream link",
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    color = Color(0xFFEBECEF)
                ) {
                    clipboardManager.setText(AnnotatedString(postLink))
                    AmityUIKitSnackbar.publishSnackbarMessage("Link copied")
                    // Delay the bottom sheet dismissal slightly
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        showBottomSheet = false
                    }
                }

                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_v4_share_icon,
                    text = "Share to",
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    color = Color(0xFFEBECEF)
                ) {
                    showBottomSheet = false
                    // Open native Android share sheet
                    sharePost(context, postLink)
                }
            }

        }
    }
}

private fun closePageWithLivestreamError(context: Context, errorType: LivestreamErrorScreenType) {
    val resultIntent = Intent().apply {
        putExtra(EXTRA_PARAM_LIVESTREAM_ERROR_TYPE, errorType.name)
    }
    context.closePageWithResult(Activity.RESULT_OK, resultIntent)
}

@Composable
fun CommunityLivestreamPlayerHeader(
    modifier: Modifier = Modifier,
    stream: AmityStream? = null,
    onOptionsClick: () -> Unit = {},
) {
    val post = stream?.getPost()
    val info: Pair<String, String> = when (post?.getTarget()) {
        is AmityPost.Target.COMMUNITY -> {
            val community = (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
            Pair(
                community?.getDisplayName() ?: "Unknown Community",
                community?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
            )
        }

        is AmityPost.Target.USER -> {
            val user = (post.getTarget() as? AmityPost.Target.USER)?.getUser()
            Pair(
                user?.getDisplayName() ?: "Unknown User",
                user?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
            )
        }

        else -> {
            Pair("Unknown", "")
        }
    }
    val community = (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
    Row(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.6f),
                        Color.Transparent
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Close button + Community info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.width(40.dp))

            // Community avatar
            AsyncImage(
                model = info.second,
                contentDescription = "Community Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Gray), // Fallback background
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.amity_ic_community_placeholder) // Add placeholder
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Community name and display name
            Column {
                Text(
                    text = info.first,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow1.Ellipsis
                )
                Text(
                    text = "By ${post?.getCreator()?.getDisplayName() ?: "Unknown User"}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow1.Ellipsis
                )
            }
        }

        if (stream?.getStatus() == AmityStream.Status.LIVE) {
            // Right side: LIVE badge
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFFF3B5C), // Pink/Red color for LIVE badge
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "LIVE",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            val postLink = if(post!= null) AmityUIKitConfigController.getPostLink(post) else ""
            if(postLink.isNotEmptyOrBlank()) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(
                        id = R.drawable.amity_v4_option_vertical
                    ),
                    contentDescription = "Options",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickableWithoutRipple {
                            onOptionsClick()
                        },
                )
            }
        }
    }
}
