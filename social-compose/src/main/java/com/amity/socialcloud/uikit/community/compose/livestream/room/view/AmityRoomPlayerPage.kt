package com.amity.socialcloud.uikit.community.compose.livestream.room.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitationStatus
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
import com.amity.socialcloud.sdk.model.video.room.AmityRoomStatus
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.elements.drawVerticalScrollbar
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamMessageComposeBar
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ChatOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReaction
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReactionsOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ReactionPicker
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamNoInternetView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamPendingApprovalView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityMediaAndCameraNoPermissionView
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.AmityCreateRoomPageBehavior
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamBannedPage
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamDeclinedPage
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityStreamerView
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.EXTRA_PARAM_LIVESTREAM_ERROR_TYPE
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamDisconnectedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamEndedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamLoadingView
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityRoomViewerCountBadge
import io.livekit.android.compose.ui.flipped
import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.Track
import io.livekit.android.util.flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityRoomPlayerPage(
    modifier: Modifier = Modifier,
    post: AmityPost,
    liveOnly: Boolean = false,
    fromInvitation: Boolean = false,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityRoomPlayerViewModel>(
        factory = AmityRoomPlayerViewModel.create(post),
        viewModelStoreOwner = viewModelStoreOwner
    )
    val uiState by remember { viewModel.roomPlayerState }.collectAsState()

    var wasLive by remember { mutableStateOf(false) }

    var showReactionPicker by remember { mutableStateOf(false) }
    val floatingReactions = remember { mutableStateListOf<FloatingReaction>() }
    var messageText by remember { mutableStateOf("") }
    val isTargetCommunity by remember(post) {
        mutableStateOf(post.getTarget() is AmityPost.Target.COMMUNITY)
    }

    val bottomSheetState = rememberModalBottomSheetState()
    val showLeaveAsCoHostSheetState = rememberModalBottomSheetState()

    val clipboardManager = LocalClipboardManager.current

    val behavior = remember {
        AmitySocialBehaviorHelper.createRoomPageBehavior
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var showInvitationSheet by remember { mutableStateOf(false) }
    var showLeaveAsCoHostSheet by remember { mutableStateOf(false) }

    var userEnabledCamera by rememberSaveable { mutableStateOf(true) }
    var userEnabledMic by rememberSaveable { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    var isStarting by remember { mutableStateOf(false) }
    var showLeaveBackstageDialog by remember { mutableStateOf(false) }
    var showLeaveAsCoHostDialog by remember { mutableStateOf(false) }
    var showLeaveLivestreamDialog by remember { mutableStateOf(false) }
    var showCannotStartLivestreamDialog by remember { mutableStateOf(false) }
    val liveKitRoomState by remember(uiState.liveKitRoom) {
        uiState.liveKitRoom?.let {
            it::state.flow
        } ?: flowOf(Room.State.DISCONNECTED)
    }.collectAsState(Room.State.DISCONNECTED)

    val haptics = LocalHapticFeedback.current

    val cameraAndAudioPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    var isCameraAndRecAudioPermissionGranted by remember {
        mutableStateOf(cameraAndAudioPermissions.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == PackageManager.PERMISSION_GRANTED
        })
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCameraAndRecAudioPermissionGranted = permissions.entries.all { it.value }
        }

    DisposableEffectWithLifeCycle(
        onResume = {
            isCameraAndRecAudioPermissionGranted = cameraAndAudioPermissions.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
        },
    )

    LaunchedEffect(uiState.room?.getRoomId()) {
        uiState.room
            ?.let { room ->
                if (room.getStatus() == AmityRoomStatus.LIVE) {
                    viewModel.startRoomHeartbeat(roomId = room.getRoomId())
                }
            }
    }

    LaunchedEffect(uiState.room?.getRoomId()) {
        uiState.room?.let { room ->
            viewModel.fetchRoomInvitation(
                room = room,
                wasInvited = fromInvitation,
            )
        }
    }

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
            viewModel.subscribePostRT(uiState.post)
        },
        onDestroy = {
            viewModel.unSubscribePostRT(uiState.post)
        }
    )

    LaunchedEffect(uiState.post.isDeleted()) {
        if (uiState.post.isDeleted()) {
            closePageWithLivestreamError(
                context,
                LivestreamErrorScreenType.DELETED
            )
        }
    }

    LaunchedEffect(uiState.room) {
        if(!wasLive) {
            if(uiState.room?.getStatus() == AmityRoomStatus.LIVE) {
                wasLive = true
            }
        }

        if (uiState.room?.getStatus() == AmityRoomStatus.RECORDED && uiState.recordedUrls.isEmpty()) {
            uiState.room?.getRoomId()?.let(viewModel::fetchRecordedUrls)
        }

        if(uiState.room?.getStatus() == AmityRoomStatus.ENDED) {
            // Stop cohost mode when room ended
            viewModel.setIsStreamerMode(false)
        }
    }

    DisposableEffectWithLifeCycle(
        onDestroy = {
            uiState
                .room
                ?.getRoomId()
                ?.let(viewModel::stopRoomHeartbeat)
        }
    )

    val connection by viewModel
        .getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)

    if (uiState.isBanned == true && uiState.room?.getStatus() == AmityRoomStatus.LIVE) {

        return
    }
    if (fromInvitation && (uiState.room?.getStatus() == AmityRoomStatus.ENDED || uiState.room?.getStatus() == AmityRoomStatus.RECORDED)
        || uiState.reviewStatus == AmityReviewStatus.DECLINED
    ) {
        AmityLivestreamDeclinedPage(
            onOkClick = {
                context.closePageWithResult(Activity.RESULT_OK)
            }
        )
        return
    }

    if (liveOnly && uiState.room?.getStatus() != AmityRoomStatus.LIVE && !wasLive) {
        AmityLivestreamBannedPage(
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
                    .navigationBarsPadding(),
            ) {
                val roomStatus = uiState.room?.getStatus()
                val terminateLabels =
                    uiState.room?.getModeration()?.terminateLabels ?: emptyList()
                val roomId = uiState.room?.getRoomId()
                val isUnavailable = uiState.error != null || uiState.room?.isDeleted() == true || roomId == null
                val isDisconnected =
                    connection == NetworkConnectionEvent.Disconnected && roomStatus == AmityRoomStatus.LIVE
                if (
                    terminateLabels.isNotEmpty()
                        || roomStatus == AmityRoomStatus.ENDED
                        || (roomStatus != AmityRoomStatus.LIVE && wasLive)
                        || isUnavailable
                ){
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (roomId == null) {
                            "stream_loading"
                        } else if (terminateLabels.isNotEmpty()) {
                            "stream_terminated"
                        } else if (roomStatus == AmityRoomStatus.ENDED) {
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
                            if (roomId == null) {
                                AmityLivestreamLoadingView()
                            } else if (terminateLabels.isNotEmpty()) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.TERMINATED
                                )
                            } else if (
                                roomStatus == AmityRoomStatus.ENDED
                                || (roomStatus != AmityRoomStatus.LIVE && wasLive)
                            ) {
                                AmityLivestreamEndedView()
                            } else if (isUnavailable) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.DELETED
                                )
                            } else {
                                // Fallback to livestream ended view for unmatched cases
                                AmityLivestreamEndedView()
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
                } else if (!uiState.isStreamerMode) {
                    LaunchedEffect(uiState.invitation) {
                        val invitationUserId = uiState.invitation?.getInvitedUserId()
                        if (
                            invitationUserId != null
                            && invitationUserId == AmityCoreClient.getUserId()
                            && uiState.invitation?.getStatus() == AmityInvitationStatus.PENDING
                            && uiState.room?.getStatus() == AmityRoomStatus.LIVE
                            && !showInvitationSheet
                        ) {
                            showInvitationSheet = true
                        }

                    }
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = ""
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                        ) {
                            val liveUrl = uiState.room?.getLivePlaybackUrl()
                            val urls =
                                if (uiState.room?.getStatus() != AmityRoomStatus.RECORDED && !liveUrl.isNullOrBlank()) {
                                    listOf(liveUrl)
                                } else if (uiState.room?.getStatus() == AmityRoomStatus.RECORDED) {
                                    uiState.room
                                        ?.getRecordedPlaybackInfos()
                                        ?.mapNotNull { it.url }
                                        ?: emptyList()
                                } else {
                                    emptyList()
                                }
                            val mediaSource = getMediaSource(urls)
                            AndroidView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black),
                                factory = { context: Context ->
                                    val exoPlayer = ExoPlayer
                                        .Builder(context)
                                        .build()
                                        .apply {
                                            setMediaSource(mediaSource)
                                            prepare()
                                            playWhenReady = true
                                        }
                                    PlayerView(context).apply {
                                        player = exoPlayer
                                        useController = true
                                    }
                                },
                                onRelease = { view ->
                                    (view.player as? ExoPlayer)?.release()
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.6f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                    .padding(top = 60.dp)
                            ) {
                                CommunityRoomPlayerHeader(
                                    room = uiState.room,
                                    viewerCount = uiState.viewerCount,
                                    onOptionsClick = {
                                        showBottomSheet = true
                                    })
                            }
                            if (isDisconnected) {
                                AmityLivestreamDisconnectedView()
                            }
                        }

                    }
                } else {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                    ) {
                        Box(
                            modifier = modifier
                                .aspectRatio(9f / 16f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black)
                        ) {
                            if (isCameraAndRecAudioPermissionGranted) {
                                AmityStreamerView(
                                    modifier = Modifier.fillMaxSize(),
                                    userEnabledCamera = userEnabledCamera,
                                    userEnabledMic = userEnabledMic,
                                    cameraPosition = uiState.cameraPosition,
                                    broadcasterData = uiState.broadcasterData,
                                    hostUserId = uiState.hostUserId ?: post.getCreatorId(),
                                    hostUser = uiState.hostUser,
                                    cohostUserId = AmityCoreClient.getUserId(),
                                    cohostUser = uiState.cohostUser,
                                    onRoomChanged = { room ->
                                        viewModel.onLiveKitRoomChange(room)
                                        if (isStarting && room.state == Room.State.CONNECTED) {
                                            isStarting = false
                                        }
                                    },
                                    onCoHostLeave = {
                                        showLeaveAsCoHostSheet = true
                                    }
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (liveKitRoomState == Room.State.CONNECTED) {
                                            Color.Transparent
                                        } else {
                                            Color.Black.copy(
                                                alpha = 0.5f
                                            )
                                        }
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when {
                                    //Disable leaving transition
//                                    isLeaving
//                                        -> {
//                                        Spacer(Modifier.weight(1f))
//                                        CircularProgressIndicator(
//                                            modifier = Modifier
//                                                .width(40.dp)
//                                                .height(40.dp),
//                                            color = Color.White,
//                                            trackColor = Color.Gray,
//                                            strokeWidth = 2.dp,
//                                            strokeCap = StrokeCap.Round
//                                        )
//                                        Spacer(Modifier.height(13.dp))
//                                        Text(
//                                            text = "Leaving stage…",
//                                            color = Color.White,
//                                            style = AmityTheme.typography.titleLegacy.copy(
//                                                fontWeight = FontWeight.SemiBold
//                                            )
//                                        )
//                                        Text(
//                                            text = "You’ll return to the livestream as a viewer shortly.",
//                                            style = AmityTheme.typography.caption.copy(
//                                                color = Color.White
//                                            ),
//                                        )
//                                        Spacer(Modifier.weight(1f))
//                                    }
                                    liveKitRoomState == Room.State.DISCONNECTED && !isStarting -> {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(58.dp)
                                                .padding(horizontal = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            AmityBaseElement(
                                                pageScope = getPageScope(),
                                                elementId = "cancel_create_livestream_button"
                                            ) {
                                                Image(
                                                    modifier = Modifier
                                                        .padding(end = 12.dp)
                                                        .size(32.dp)
                                                        .clickableWithoutRipple {
                                                            if (uiState.isStreamerMode) {
                                                                showLeaveBackstageDialog = true
                                                            } else {
                                                                context.closePageWithResult(Activity.RESULT_CANCELED)
                                                            }
                                                        }
                                                        .testTag(getAccessibilityId()),
                                                    painter = painterResource(R.drawable.amity_ic_room_close),
                                                    contentDescription = "cancel_create_livestream_button",
                                                )
                                            }
                                            Text(
                                                text = "You’re in the backstage",
                                                style = TextStyle(
                                                    fontSize = 15.sp,
                                                    lineHeight = 20.sp,
                                                    fontWeight = FontWeight(400),
                                                    color = Color.White,
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }

                                        Spacer(Modifier.height(20.dp))

                                        if (isCameraAndRecAudioPermissionGranted) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .imePadding()
                                                    .weight(1f)
                                                    .padding(vertical = 16.dp)
                                                    .drawVerticalScrollbar(scrollState)
                                                    .verticalScroll(scrollState)
                                            ) {

                                            }
                                        } else {
                                            AmityMediaAndCameraNoPermissionView(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .background(Color.Black.copy(alpha = 0.5f)),
                                                title = stringResource(R.string.amity_v4_create_livestream_no_camera_permission_title),
                                                description = stringResource(R.string.amity_v4_create_livestream_no_camera_permission_desc),
                                                onOpenSettingClick = {
                                                    val intent =
                                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                            data =
                                                                Uri.fromParts(
                                                                    "package",
                                                                    context.packageName,
                                                                    null
                                                                )
                                                        }
                                                    context.startActivity(intent)
                                                }
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .shadow(
                                                        elevation = 8.dp,
                                                        spotColor = Color(0x33606170),
                                                        ambientColor = Color(0x33606170)
                                                    )
                                                    .shadow(
                                                        elevation = 2.dp,
                                                        spotColor = Color(0x1A28293D),
                                                        ambientColor = Color(0x1A28293D)
                                                    )
                                                    .background(
                                                        color = Color(0x80000000),
                                                        shape = RoundedCornerShape(size = 8.dp)
                                                    )
                                                    .padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(
                                                    12.dp,
                                                    Alignment.Top
                                                ),
                                                horizontalAlignment = Alignment.Start,
                                            ) {
                                                Text(
                                                    text = "Adjust your camera, mic and lighting before joining the live stream.",
// Typography/Body
                                                    style = TextStyle(
                                                        fontSize = 15.sp,
                                                        lineHeight = 20.sp,
                                                        fontWeight = FontWeight(400),
                                                        color = Color.White,
                                                        textAlign = TextAlign.Center,
                                                    )
                                                )
                                                if (isCameraAndRecAudioPermissionGranted) {
                                                    Button(
                                                        onClick = {
                                                            isStarting = true
                                                            uiState.room?.let { room ->
                                                                viewModel.observeOnlineUsersCount(
                                                                    roomId = room.getRoomId(),
                                                                    interval = 5,
                                                                )
                                                                viewModel.joinRoom(
                                                                    post = uiState.post,
                                                                    room = room,
                                                                    onJoinedCompleted = { broadcastData, scope ->
                                                                        (broadcastData as? AmityRoomBroadcastData.CoHosts)?.let { data ->
                                                                            scope.launch {
                                                                                uiState.liveKitRoom?.connect(
                                                                                    url = data.getCoHostUrl(),
                                                                                    token = data.getCoHostToken()
                                                                                )
                                                                            }
                                                                        }
                                                                    },
                                                                    onJoinedFailed = {
                                                                        showCannotStartLivestreamDialog =
                                                                            true
                                                                    },
                                                                )
                                                            }
                                                        },
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(40.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = Color(0xFFF26B1C)
                                                        ),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ) {
                                                        Image(
                                                            modifier = Modifier
                                                                .padding(end = 8.dp)
                                                                .size(20.dp)
                                                                .testTag(getAccessibilityId()),
                                                            painter = painterResource(id = R.drawable.amity_ic_cohost_badge),
                                                            contentDescription = "Join livestream",
                                                            contentScale = ContentScale.Fit
                                                        )
                                                        Text(
                                                            text = "Join LIVE",
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = Color.White
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    liveKitRoomState == Room.State.CONNECTED -> {
                                        isStarting = false
                                        Box(modifier = Modifier.fillMaxSize()) {

                                            if (liveKitRoomState == Room.State.DISCONNECTED) {
                                                AmityCreateLivestreamNoInternetView()
                                            } else if (uiState.isPendingApproval == true) {
                                                AmityCreateLivestreamPendingApprovalView()
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.TopStart)
                                                    .background(
                                                        brush = Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Black.copy(alpha = 0.6f),
                                                                Color.Transparent
                                                            )
                                                        )
                                                    )
                                                    .padding(top = 16.dp)
                                            ) {
                                                CommunityRoomPlayerHeader(
                                                    room = uiState.room,
                                                    viewerCount = uiState.viewerCount,
                                                    onOptionsClick = {
                                                        showBottomSheet = true
                                                    })
                                            }
                                        }
                                    }

//                                    liveKitRoomState == Room.State.CONNECTING
//                                            ||
                                    isStarting
                                        -> {
                                        Spacer(Modifier.weight(1f))
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(40.dp),
                                            color = Color.White,
                                            trackColor = Color.Gray,
                                            strokeWidth = 2.dp,
                                            strokeCap = StrokeCap.Round
                                        )
                                        Spacer(Modifier.height(13.dp))
                                        Text(
                                            text = stringResource(R.string.amity_v4_create_livestream_connecting_text),
                                            color = Color.White,
                                            style = AmityTheme.typography.titleLegacy.copy(
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Spacer(Modifier.weight(1f))
                                    }

                                    else -> {}
                                }
                            }
                        }
                        if (uiState.broadcasterData == null) {
                            AmityBaseComponent(
                                pageScope = getPageScope(),
                                componentId = "create_livestream_bottom_bar"
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black)
                                        .padding(horizontal = 16.dp),
                                ) {
//                            when (uiState.broadcasterState) {
//                                is AmityStreamBroadcasterState.IDLE -> {
//
//                                is AmityStreamBroadcasterState.CONNECTING -> {}
//
//                                is AmityStreamBroadcasterState.CONNECTED,
//                                is AmityStreamBroadcasterState.DISCONNECTED,
//                                    -> {
//                                    AmityBaseElement(
//                                        pageScope = getPageScope(),
//                                        componentScope = getComponentScope(),
//                                        elementId = "end_live_stream_button"
//                                    ) {
//                                        Box(
//                                            modifier = Modifier
//                                                .align(Alignment.CenterStart)
//                                                .clip(RoundedCornerShape(size = 8.dp))
//                                                .border(
//                                                    width = 1.dp,
//                                                    color = AmityTheme.colors.baseShade4,
//                                                    shape = RoundedCornerShape(size = 8.dp)
//                                                )
//                                                .padding(horizontal = 16.dp, vertical = 10.dp)
//                                                .clickableWithoutRipple {
//                                                    showEndLivestreamDialog = true
//                                                }
//                                        ) {
//                                            Text(
//                                                modifier = Modifier.align(
//                                                    Alignment.Center
//                                                ),
//                                                text = getConfig().getText(),
//                                                style = AmityTheme.typography.bodyLegacy.copy(
//                                                    fontWeight = FontWeight.SemiBold
//                                                ),
//                                                color = Color.White
//                                            )
//                                        }
//                                    }
//                                }
//                            }
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        AmityBaseElement(
                                            pageScope = getPageScope(),
                                            componentScope = getComponentScope(),
                                            elementId = "switch_camera_button"
                                        ) {
                                            Image(
                                                painter = painterResource(getConfig().getIcon()),
                                                contentDescription = "switch camera button",
                                                modifier = Modifier
                                                    .testTag("switch_camera_button")
                                                    .clickableWithoutRipple {
                                                        if (isCameraAndRecAudioPermissionGranted) {
                                                            val cameraPosition =
                                                                uiState.cameraPosition.flipped()
                                                            viewModel.onCameraPositionChange(
                                                                cameraPosition = cameraPosition
                                                            )
                                                            haptics.performHapticFeedback(
                                                                HapticFeedbackType.LongPress
                                                            )
                                                        }
                                                    },
                                                alpha = if (isCameraAndRecAudioPermissionGranted) 1f else 0.5f
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
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
                )
                if (roomStatus == AmityRoomStatus.LIVE && isTargetCommunity && (!uiState.isStreamerMode || (uiState.isStreamerMode && uiState.broadcasterData != null))) {
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
                                .fillMaxHeight(0.2f),
                            pageScope = getPageScope(),
                            channelId = uiState.room?.getChannelId() ?: "",
                            streamHostUserId = uiState.hostUserId,
                            coHostUserId = uiState.cohostUserId,
                            onReactionClick = { showReactionPicker = true }
                        )
                        AmityLivestreamMessageComposeBar(
                            pageScope = getPageScope(),
                            channelId = uiState.room?.getChannelId() ?: "",
                            value = messageText,
                            isPendingApproval = uiState.reviewStatus == AmityReviewStatus.UNDER_REVIEW,
                            streamHostUserId = uiState.hostUserId,
                            onValueChange = {
                                messageText = it
                            },
                            isNonMember = (uiState.post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isJoined() != true,
                            onSend = {
                                messageText = ""
                            },
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
                                        uiState.room?.let { room ->
                                            AmityCoreClient.newLiveReactionRepository()
                                                .createRoomReaction(
                                                    roomId = room.getRoomId(),
                                                    referenceId = uiState.post.getPostId(),
                                                    referenceType = AmityLiveReactionReferenceType.POST,
                                                    reactionName = defaultReaction.name,
                                                )
                                        }
                                    }
                            },
                            onReactionLongClick = { showReactionPicker = true },
                            isMicrophoneMute = !userEnabledMic,
                            onToggleMicrophone = if (uiState.isStreamerMode) {
                                { userEnabledMic = !userEnabledMic }
                            } else {
                                null
                            },
                            onSwitchCamera = if (uiState.isStreamerMode) {
                                {
                                    if (isCameraAndRecAudioPermissionGranted) {
                                        (uiState.liveKitRoom?.localParticipant
                                            ?.getTrackPublication(Track.Source.CAMERA)
                                            ?.track as? LocalVideoTrack)?.let { cameraTrack ->
                                            val cameraPosition = uiState.cameraPosition.flipped()
                                            viewModel.onCameraPositionChange(cameraPosition = cameraPosition)
                                            cameraTrack.switchCamera(position = cameraPosition)
                                        }
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                }
                            } else {
                                null
                            },
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
                                uiState.room?.let { room ->
                                    AmityCoreClient.newLiveReactionRepository()
                                        .createRoomReaction(
                                            roomId = room.getRoomId(),
                                            referenceId = uiState.post.getPostId(),
                                            referenceType = AmityLiveReactionReferenceType.POST,
                                            reactionName = reaction.name,
                                        )
                                }
                            },
                            onDismiss = { showReactionPicker = false },
                            modifier = Modifier.padding(bottom = 56.dp, end = 16.dp)
                        )
                    }
                } else if (roomStatus == AmityRoomStatus.LIVE && isTargetCommunity && uiState.isStreamerMode && uiState.broadcasterData == null) {

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterVertically)
                                .clickableWithoutRipple {
                                    userEnabledMic = !userEnabledMic
                                }
                                .testTag("toggle_microphone_button")
                        ) {
                            Image(
                                painter = if (!userEnabledMic) { painterResource(R.drawable.amity_ic_room_unmute_button) } else {
                                    painterResource(R.drawable.amity_ic_room_mute_button)
                                },
                                contentDescription = "",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(9.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            componentScope = getComponentScope(),
                            elementId = "switch_camera_button"
                        ) {
                            Image(
                                painter = painterResource(R.drawable.amity_ic_room_switch_camera),
                                contentDescription = "switch camera button",
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("switch_camera_button")
                                    .clickableWithoutRipple {
                                        if (isCameraAndRecAudioPermissionGranted) {
                                            val cameraPosition = uiState.cameraPosition.flipped()
                                            viewModel.onCameraPositionChange(cameraPosition = cameraPosition)
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    },
                                alpha = if (isCameraAndRecAudioPermissionGranted) 1f else 0.5f
                            )
                        }
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

    val invitation = uiState.invitation
    if (showInvitationSheet && invitation != null) {
        CoHostBottomSheet(
            invitation = invitation,
            onAccept = {
                showInvitationSheet = false
                viewModel.setIsStreamerMode(true)
                viewModel.acceptInvitation(invitation)
                if (!isCameraAndRecAudioPermissionGranted) {
                    cameraPermissionLauncher.launch(cameraAndAudioPermissions)
                }
            },
            onDecline = {
                showInvitationSheet = false
                viewModel.rejectInvitation(invitation, {
                    AmityUIKitSnackbar.publishSnackbarMessage("You've declined the invitation.")
                })
            },
            onDismiss = {
                showInvitationSheet = false
            }
        )
    }

    if (showLeaveAsCoHostSheet) {
        val notJoinedYet = uiState.invitation != null
        ModalBottomSheet(
            onDismissRequest = {
                showLeaveAsCoHostSheet = false
            },
            sheetState = showLeaveAsCoHostSheetState,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_cohost_leave,
                    text = "Leave as co-host",
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    color = AmityTheme.colors.alert,
                ) {
                    showLeaveAsCoHostSheet = false
                    showLeaveAsCoHostDialog = true
                }
            }

        }
    }

    if (showLeaveBackstageDialog) {
        AmityAlertDialog(
            dialogTitle = "Leave the backstage",
            dialogText = "Are you sure you want to leave the backstage? You'll return to the event as a viewer and will need a new co-host invite.",
            confirmText = "Leave",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                leaveRoom(
                    context = context,
                    behavior = behavior,
                    uiState = uiState,
                    viewModel = viewModel,
                    onSuccess = {
                        viewModel.setIsStreamerMode(false)
                        showLeaveBackstageDialog = false
                    },
                    onError = {
                        showLeaveBackstageDialog = false
                    }
                )
            },
            onDismissRequest = {
                showLeaveBackstageDialog = false
            }
        )
    }

    if (showLeaveAsCoHostDialog) {
        AmityAlertDialog(
            dialogTitle = "Leave as co-host?",
            dialogText = "If you leave as co-host, you'll immediately stop broadcasting live and return to the event as a viewer.",
            confirmText = "Leave",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                leaveRoom(
                    context = context,
                    behavior = behavior,
                    uiState = uiState,
                    viewModel = viewModel,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("You left as co-host and are now watching as a viewer.")
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Something went wrong. Please try again.")
                    }
                )
                viewModel.setIsStreamerMode(false)
                showLeaveAsCoHostDialog = false
            },
            onDismissRequest = {
                showLeaveAsCoHostDialog = false
            }
        )
    }

    if (showLeaveLivestreamDialog) {
        AmityAlertDialog(
            dialogTitle = "Leave event?",
            dialogText = "If you leave this event, you'll stop broadcasting as co-host and will exit the entire live stream.",
            confirmText = "Leave",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                leaveLivestream(
                    context = context,
                    behavior = behavior,
                    uiState = uiState,
                    viewModel = viewModel,
                )
                viewModel.setIsStreamerMode(false)
                showLeaveLivestreamDialog = false
            },
            onDismissRequest = {
                showLeaveLivestreamDialog = false
            }
        )
    }

    if (showCannotStartLivestreamDialog) {
        AmityAlertDialog(
            dialogTitle = "Cannot join as co-host",
            dialogText = "Please try again.",
            dismissText = "OK",
            onDismissRequest = {
                showCannotStartLivestreamDialog = false
            },
        )
    }
}


private fun leaveRoom(
    context: Context,
    behavior: AmityCreateRoomPageBehavior,
    uiState: RoomPlayerState,
    viewModel: AmityRoomPlayerViewModel,
    showLivestreamPostExceeded: Boolean = false,
    errorType: LivestreamErrorScreenType = LivestreamErrorScreenType.NONE,
    switchToViewer: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {},
) {
    ((uiState.post.getData() as? AmityPost.Data.ROOM)
        ?.getRoomId() ?: uiState.room?.getRoomId())
        ?.let { roomId ->
            viewModel.leaveRoom(
                roomId = roomId,
                onSuccess = onSuccess,
                onError = onError,
            )
        }
}

private fun leaveLivestream(
    context: Context,
    behavior: AmityCreateRoomPageBehavior,
    uiState: RoomPlayerState,
    viewModel: AmityRoomPlayerViewModel,
    showLivestreamPostExceeded: Boolean = false,
    errorType: LivestreamErrorScreenType = LivestreamErrorScreenType.NONE,
    switchToViewer: () -> Unit = {},
) {
    leaveRoom(
        context = context,
        behavior = behavior,
        uiState = uiState,
        viewModel = viewModel,
        showLivestreamPostExceeded = showLivestreamPostExceeded,
        errorType = errorType,
        switchToViewer = switchToViewer,
    )
    behavior.goToPostDetailPage(
        context = context,
        id = uiState.post.getPostId(),
        category = AmityPostCategory.GENERAL,
        showLivestreamPostExceeded = showLivestreamPostExceeded
    )
    context.closePageWithResult(Activity.RESULT_OK)
}

private fun closePageWithLivestreamError(context: Context, errorType: LivestreamErrorScreenType) {
    val resultIntent = Intent().apply {
        putExtra(EXTRA_PARAM_LIVESTREAM_ERROR_TYPE, errorType.name)
    }
    context.closePageWithResult(Activity.RESULT_OK, resultIntent)
}

@Composable
fun CommunityRoomPlayerHeader(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    room: AmityRoom? = null,
    viewerCount: Int? = null,
    onOptionsClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val post = room?.getPost()
    val target = post?.getTarget()

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Close button + Community info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                elementId = "close_room_button"
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .clickableWithoutRipple {
                            context.closePageWithResult(Activity.RESULT_OK)
                        }
                        .testTag(getAccessibilityId()),
                    painter = painterResource(R.drawable.amity_ic_room_close),
                    contentDescription = "close_room_button",
                )
            }

            // Use appropriate avatar view based on target type
            when (target) {
                is AmityPost.Target.COMMUNITY -> {
                    AmityCommunityAvatarView(
                        community = target.getCommunity(),
                        size = 32.dp,
                    )
                }
                is AmityPost.Target.USER -> {
                    AmityUserAvatarView(
                        user = target.getUser(),
                        size = 32.dp,
                    )
                }
                else -> {
                    AmityAvatarView(
                        image = null,
                        size = 32.dp,
                        iconPadding = 24.dp,
                        placeholder = R.drawable.amity_ic_community_placeholder,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            // Community name and display name
            Column {
                if (target is AmityPost.Target.COMMUNITY) {
                    Row(
                        //modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (target.getCommunity()?.isPublic() != true) {
                            AmityBaseElement(
                                elementId = "community_private_badge"
                            ) {
                                Icon(
                                    painter = painterResource(id = getConfig().getIcon()),
                                    tint = AmityTheme.colors.baseShade2,
                                    contentDescription = "Private Community",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .testTag(getAccessibilityId()),
                                )
                            }
                        }
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = target.getCommunity()?.getDisplayName() ?: "Unknown Community",
                            style = AmityTheme.typography.body.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (target.getCommunity()?.isOfficial() == true) {
                            AmityBaseElement(elementId = "community_official_badge") {
                                Image(
                                    painter = painterResource(id = getConfig().getIcon()),
                                    contentDescription = "Verified Community",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .testTag(getAccessibilityId()),
                                )
                            }
                        }
                    }
                    Row(
                        //modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = "By ${post?.getCreator()?.getDisplayName() ?: "Unknown User"}",
                            style = AmityTheme.typography.caption.copy(
                                color = Color.White.copy(alpha = 0.8f),
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Show brand badge if user is a brand
                        val isBrandCreator = post.getCreator()?.isBrand() == true
                        if (isBrandCreator) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                contentDescription = "Brand badge",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                } else if (target is AmityPost.Target.USER) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = target.getUser()?.getDisplayName() ?: "Unknown User",
                            style = AmityTheme.typography.body.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        // Show brand badge if user is a brand
                        val isBrandCreator = target.getUser()?.isBrand() == true
                        if (isBrandCreator) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                contentDescription = "Brand badge",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        if (room?.getStatus() == AmityRoomStatus.LIVE) {
            // Right side: LIVE badge
            AmityRoomViewerCountBadge(viewerCount = viewerCount)

            val postLink = if (post != null) AmityUIKitConfigController.getPostLink(post) else ""
            if (postLink.isNotEmptyOrBlank()) {
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

@androidx.annotation.OptIn(UnstableApi::class)
private fun getMediaSource(urls: List<String>): ConcatenatingMediaSource {
    val concatenatedSource = ConcatenatingMediaSource()
    urls.map { url ->
        val mediaItem = MediaItem.fromUri(url.toUri())
        val videoSource: MediaSource =
            HlsMediaSource.Factory(getDataSource())
                .createMediaSource(mediaItem)
        concatenatedSource.addMediaSource(videoSource)
    }
    return concatenatedSource
}

@androidx.annotation.OptIn(UnstableApi::class)
private fun getDataSource(): DefaultHttpDataSource.Factory {
    return DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)
        .let { factory ->
            AmityCoreClient.getAccessToken()
                ?.let { token -> mapOf("Authorization" to "Bearer $token") }
                ?.let(factory::setDefaultRequestProperties)
                ?: factory
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoHostBottomSheet(
    invitation: AmityInvitation,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val hostUser = invitation.getInviterUser()
    val cohostUser = invitation.getInvitedUser()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color(0xFF191919),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Avatars Row
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Host Avatar with Badge
                Box {
                    Spacer(modifier = Modifier.width(12.dp))
                    AmityUserAvatarView(
                        user = hostUser,
                        size = 120.dp,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFF1C1C1E), CircleShape)

                    )

                    // Host Badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 8.dp)
                            .background(
                                color = Color(0xFFFF2D55),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
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
                            text = "HOST",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Invited User Avatar
                AmityUserAvatarView(
                    user = cohostUser,
                    size = 120.dp,
                    modifier = Modifier
                        .offset(x = -25.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF1C1C1E), CircleShape)
                        .zIndex(0f),
                )
            }

            // Title
            Text(
                text = "Join as co-host",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFEBECEF),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Description
            Text(
                text = "${hostUser?.getDisplayName() ?: "Unknown user"} invited you to join their live stream as a co-host. You'll enter the backstage room to prepare before going live.",
                fontSize = 15.sp,
                color = AmityTheme.colors.baseShade1,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFF292B32))
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Accept Button
            Button(
                onClick = {
                    onAccept()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.highlight
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Accept invite",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Decline Button
            OutlinedButton(
                onClick = {
                    onDecline()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                border = BorderStroke(1.dp, Color(0xFF38383A)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Decline",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
