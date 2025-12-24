package com.amity.socialcloud.uikit.community.compose.livestream.room.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitationStatus
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
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
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.elements.drawVerticalScrollbar
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamMessageComposeBar
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ChatOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReaction
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReactionsOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ReactionPicker
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCircularProgressWithCountDownTimer
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamNoInternetView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamPendingApprovalView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityEditThumbnailSheet
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityMediaAndCameraNoPermissionView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityNoOutlineTextField
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityThumbnailView
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.LivestreamThumbnailUploadUiState
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.AmityCreateRoomPageActivity.Companion.EXTRA_PARAM_TARGET_ID
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.AmityCreateRoomPageActivity.Companion.EXTRA_PARAM_TARGET_TYPE
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.model.AmityCreateRoomPageUiState
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityRoomViewerCountBadge
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityStreamerView
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamDeclinedPage
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import io.livekit.android.compose.ui.flipped
import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.Track
import io.livekit.android.util.flow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.flowable.FlowableInterval
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

private const val LIVESTREAM_DURATION_SNACK_BAR = 14220
private const val LIVESTREAM_COUNTDOWN_DURATION = 10
private const val LIVESTREAM_DURATION_COUNTDOWN = 14390
private const val LIVESTREAM_INTERNET_LOSS_MAXIMUM_DURATION = 180000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityCreateRoomPage(
    modifier: Modifier = Modifier,
    postId: String? = null,
    targetId: String,
    targetType: AmityPost.TargetType,
    targetCommunity: AmityCommunity? = null,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityCreateRoomPageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityCreateRoomPageViewModel(
                    postId = if (postId?.isNotEmpty() == true) postId else null,
                ) as T
            }
        }
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val targetId = result.data?.getStringExtra(EXTRA_PARAM_TARGET_ID) ?: ""
            val targetTypeString = result.data?.getStringExtra(EXTRA_PARAM_TARGET_TYPE)
            val targetType = AmityPost.TargetType.enumOf(targetTypeString)
            if (targetType == AmityPost.TargetType.USER) {
                viewModel.setupScreen()
            } else {
                viewModel.setupScreen(targetId)
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState()
    val removeCoHostSheetState = rememberModalBottomSheetState()

    val clipboardManager = LocalClipboardManager.current


    var showEditThumbnailSheet by remember { mutableStateOf(false) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showEndLivestreamDialog by remember { mutableStateOf(false) }
    var showLivestreamLimitSnackBar by remember { mutableStateOf(false) }
    var showCountdownEndingLivestream by remember { mutableStateOf(false) }
    var showCannotStartLivestreamDialog by remember { mutableStateOf(false) }
    var showInviteConfirmDialog by remember { mutableStateOf<Pair<String, AmityUser?>?>(null) }
    var showRemoveCoHostConfirmDialog by remember { mutableStateOf(false) }
    var showInviteCoHostSheet by remember { mutableStateOf(false) }
    var showCancelInvitationDialog by remember { mutableStateOf(false) }
    var showThumbnailErrorUploadDialog by remember {
        mutableStateOf(
            Pair(
                first = false,
                second = 0
            )
        )
    }
    val isTargetCommunity = targetType == AmityPost.TargetType.COMMUNITY
    var isTerminated by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val haptics = LocalHapticFeedback.current

    val behavior = remember {
        AmitySocialBehaviorHelper.createRoomPageBehavior
    }

    var durationDisposable: Disposable? by remember { mutableStateOf(null) }
    var duration by remember { mutableIntStateOf(0) }
    var liveHour by remember { mutableIntStateOf(0) }
    var liveMin by remember { mutableIntStateOf(0) }
    var liveSecond by remember { mutableIntStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showRemoveCoHost by remember { mutableStateOf(false) }
    var isReadOnly by remember { mutableStateOf(false) }
    var isStarting by remember { mutableStateOf(false) }

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
    val mediaPickerPermissions = Manifest.permission.READ_EXTERNAL_STORAGE

    val isMediaPickerPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                mediaPickerPermissions
            )
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCameraAndRecAudioPermissionGranted = permissions.entries.all { it.value }
        }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                context.grantUriPermission(
                    context.packageName,
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.uploadThumbnail(
                    uri = it,
                    onUploadFailed = { code ->
                        showThumbnailErrorUploadDialog =
                            if (code == AmityError.BUSINESS_ERROR.code) {
                                Pair(true, code)
                            } else {
                                Pair(true, 0)
                            }
                    }
                )
            }
        }

    val mediaPickerPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mediaPickerLauncher.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        } else {
            behavior.goToNoPermissionPage(
                context = context,
            )
        }
    }

    val startStreamButtonEnable =
        isCameraAndRecAudioPermissionGranted && (!uiState.liveTitle.isNullOrBlank() || uiState.room != null) && uiState.thumbnailUploadUiState !is LivestreamThumbnailUploadUiState.Uploading

    var showReactionPicker by remember { mutableStateOf(false) }
    val floatingReactions = remember { mutableStateListOf<FloatingReaction>() }
    var messageText by remember { mutableStateOf("") }


    val reactions by remember(uiState.createPostId) {  viewModel.observeLiveReactions(uiState.createPostId ?: "") }.collectAsState(emptyList())

    var userEnabledCamera by rememberSaveable { mutableStateOf(true) }
    var userEnabledMic by rememberSaveable { mutableStateOf(true) }

    val liveKitRoomState by remember(uiState.liveKitRoom) {
        uiState.liveKitRoom?.let {
            it::state.flow
        } ?: flowOf(Room.State.DISCONNECTED)
    }.collectAsState(Room.State.DISCONNECTED)

    val fromEventPage = !postId.isNullOrEmpty()

    SideEffect {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            isCameraAndRecAudioPermissionGranted = true
        }
    }

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

    LaunchedEffect(Unit) {
        if (targetType == AmityPost.TargetType.USER) {
            viewModel.setupScreen()
        } else {
            targetCommunity?.let {
                viewModel.setupScreen(it.getCommunityId())
            }
        }
    }

    LaunchedEffect(liveKitRoomState) {
        when (liveKitRoomState) {
            Room.State.CONNECTED -> {
                // Start duration counter only when it never start yet to prevent redundant counting
                if (duration == 0) {
                    durationDisposable = FlowableInterval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                        .map { duration++ }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            val durationMs = it * 1000
                            val second = durationMs / 1000 % 60
                            val min = (durationMs / 1000 / 60) % 60
                            val hour = durationMs / 1000 / 60 / 60

                            liveHour = hour
                            liveMin = min
                            liveSecond = second

                            if (it == LIVESTREAM_DURATION_SNACK_BAR) {
                                showLivestreamLimitSnackBar = true
                            }
                            if (it == LIVESTREAM_DURATION_COUNTDOWN) {
                                showCountdownEndingLivestream = true
                            }
                        }
                        .subscribe()
                }
            }

           Room.State.DISCONNECTED -> {
                uiState.roomModeration?.terminateLabels?.let {
                    if (it.isNotEmpty() && !isTerminated) {
                        isTerminated = true
                        if (durationDisposable?.isDisposed == false) {
                            uiState.liveKitRoom?.let(::stopLocalCameraTrack)
                            uiState.liveKitRoom?.release()
                            durationDisposable?.dispose()
                        }
                        behavior.goToTerminatedPage(
                            context = context,
                            screenType = LivestreamScreenType.CREATE
                        )
                        context.closePageWithResult(Activity.RESULT_OK)
                    }
                }
            }

            else -> {}
        }
    }

    LaunchedEffect(uiState.networkConnection) {
        if (uiState.networkConnection is NetworkConnectionEvent.Disconnected) {
            delay(LIVESTREAM_INTERNET_LOSS_MAXIMUM_DURATION)
            endLivestream(
                context = context,
                durationDisposable = durationDisposable,
                behavior = behavior,
                uiState = uiState,
                viewModel = viewModel,
                errorType = LivestreamErrorScreenType.INTERNET,
                shouldNavigateToPostDetails = !fromEventPage,
            )
        }
    }

    LaunchedEffect(uiState.post?.isDeleted()) {
        if (uiState.post?.isDeleted() == true) {
            if (durationDisposable?.isDisposed == false) {
                uiState.liveKitRoom?.let(::stopLocalCameraTrack)
                uiState.liveKitRoom?.release()
                durationDisposable?.dispose()
            }
            behavior.goToTerminatedPage(
                context = context,
                screenType = LivestreamScreenType.CREATE
            )
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    DisposableEffectWithLifeCycle(
        onResume = {
            if (!isCameraAndRecAudioPermissionGranted) {
                cameraPermissionLauncher.launch(cameraAndAudioPermissions)
            }
            isCameraAndRecAudioPermissionGranted = cameraAndAudioPermissions.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
        },
        onDestroy = {
            if (durationDisposable?.isDisposed == false) {
                duration = 0
                uiState.liveKitRoom?.let(::stopLocalCameraTrack)
                uiState.liveKitRoom?.release()
                durationDisposable?.dispose()
            }
            uiState.post?.let {
                viewModel.unSubscribePostRT(it)
            }
        }
    )

    BackHandler {
        if (uiState.liveTitle.isNullOrBlank() && uiState.thumbnailId.isNullOrBlank()) {
            context.closePageWithResult(Activity.RESULT_CANCELED)
        } else {
            showDiscardPostDialog = true

        }
    }

    if (uiState.post?.getReviewStatus() == AmityReviewStatus.DECLINED) {
        AmityLivestreamDeclinedPage(
            onOkClick = {
                context.closePageWithResult(Activity.RESULT_OK)
            }
        )
        return
    }
    AmityBasePage(pageId = "create_livestream_page") {
        Box(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    },
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
                            hostUserId = uiState.hostUserId ?: AmityCoreClient.getUserId(),
                            hostUser = uiState.hostUser,
                            cohostUserId = uiState.cohostUserId,
                            cohostUser = uiState.cohostUser,
                            onRoomChanged = { room ->
                                viewModel.onLiveKitRoomChange(room)
                                if (isStarting && room.state == Room.State.CONNECTED) {
                                    isStarting = false
                                }
                            },
                            onRemoveCoHost = {
                                showRemoveCoHost = true
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
                            liveKitRoomState == Room.State.DISCONNECTED && !isStarting -> {
                                val room = uiState.room
                                if (room != null) {
                                    EventRoomPlayerHeader(
                                        pageScope = getPageScope(),
                                        room = room,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(58.dp)
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
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
                                                        if (uiState.liveTitle.isNullOrBlank() && uiState.thumbnailId.isNullOrBlank()) {
                                                            context.closePageWithResult(Activity.RESULT_CANCELED)
                                                        } else {
                                                            showDiscardPostDialog = true
                                                        }

                                                    }
                                                    .testTag(getAccessibilityId()),
                                                painter = painterResource(R.drawable.amity_ic_room_close),
                                                contentDescription = "cancel_create_livestream_button",
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = stringResource(R.string.amity_v4_create_livestream_target_title),
                                                color = Color.White,
                                                style = AmityTheme.typography.bodyLegacy
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = uiState.targetName ?: "",
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = AmityTheme.typography.bodyLegacy.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                ),
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Image(
                                            painter = painterResource(R.drawable.amity_arrow_down),
                                            contentDescription = "Close Button",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickableWithoutRipple(
                                                    onClick = {
                                                        behavior.goToSelectLivestreamTargetPage(
                                                            context = context,
                                                            isEditMode = true,
                                                            launcher = launcher
                                                        )
                                                    }
                                                ),
                                        )
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                if (isCameraAndRecAudioPermissionGranted) {
                                    if (uiState.isPreparingInitialData) {
                                        Column(
                                            horizontalAlignment  = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .imePadding()
                                                .weight(1f)
                                        ) {
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
                                            Spacer(Modifier.weight(1f))
                                        }
                                    } else if (uiState.room == null) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .imePadding()
                                                .weight(1f)
                                                .padding(vertical = 16.dp)
                                                .drawVerticalScrollbar(scrollState)
                                                .verticalScroll(scrollState)
                                        ) {
                                            AmityNoOutlineTextField(
                                                value = uiState.liveTitle ?: "",
                                                onValueChange = {
                                                    viewModel.setTitleText(it)
                                                },
                                                modifier = Modifier.padding(horizontal = 16.dp),
                                                placeHolder = {
                                                    Text(
                                                        text = stringResource(R.string.amity_v4_create_livestream_title_placeholder),
                                                        color = Color.White.copy(alpha = 0.8f),
                                                        style = AmityTheme.typography.headLine.copy(
                                                            color = Color.White,
                                                            textAlign = TextAlign.Start,
                                                        )
                                                    )
                                                },
                                                singleLine = true,
                                                maxCharLength = 30,
                                                textStyle = AmityTheme.typography.headLine.copy(
                                                    color = Color.White,
                                                    textAlign = TextAlign.Start,
                                                ),
                                            )

                                            Spacer(Modifier.height(8.dp))

                                            AmityNoOutlineTextField(
                                                value = uiState.liveDesc ?: "",
                                                onValueChange = {
                                                    viewModel.setDescText(it)
                                                },
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .imePadding(),
                                                placeHolder = {
                                                    Text(
                                                        text = stringResource(R.string.amity_v4_create_livestream_desc_placeholder),
                                                        color = Color.White.copy(alpha = 0.8f),
                                                        style = AmityTheme.typography.body.copy(
                                                            color = Color.White,
                                                            textAlign = TextAlign.Start,
                                                        )
                                                    )
                                                },
                                                singleLine = false,
                                                textStyle = AmityTheme.typography.body.copy(
                                                    color = Color.White,
                                                    textAlign = TextAlign.Start,
                                                ),
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .imePadding()
                                                .weight(1f)
                                        ) {}
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
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    elementId = "start_livestream_button"
                                ) {
                                    Image(
                                        painter = painterResource(getConfig().getIcon()),
                                        contentDescription = "Start Livestream",
                                        modifier = Modifier
                                            .padding(bottom = 32.dp)
                                            .size(72.dp)
                                            .clickableWithoutRipple {
                                                if (startStreamButtonEnable) {
                                                    isStarting = true
                                                    val room = uiState.room
                                                    val post = uiState.post
                                                    if (room != null && post != null) {
                                                        viewModel.startRoom(
                                                            room = room,
                                                            post = post,
                                                            isReadOnly = isReadOnly,
                                                            onCreateCompleted = { broadcastData, scope ->
                                                                (broadcastData as? AmityRoomBroadcastData.CoHosts)?.let { data ->
                                                                    scope.launch {
                                                                        uiState.liveKitRoom?.connect(
                                                                            url = data.getCoHostUrl(),
                                                                            token = data.getCoHostToken()
                                                                        )
                                                                    }
                                                                }
                                                            },
                                                            onCreateFailed = {
                                                                showCannotStartLivestreamDialog =
                                                                    true
                                                            },
                                                        )
                                                    } else {
                                                        uiState.liveTitle?.let {
                                                            viewModel.createRoomPost(
                                                                title = it,
                                                                description = uiState.liveDesc
                                                                    ?: " ",
                                                                isReadOnly = isReadOnly,
                                                                onCreateCompleted = { broadcastData, scope ->
                                                                    (broadcastData as? AmityRoomBroadcastData.CoHosts)?.let { data ->
                                                                        scope.launch {
                                                                            uiState.liveKitRoom?.connect(
                                                                                url = data.getCoHostUrl(),
                                                                                token = data.getCoHostToken()
                                                                            )
                                                                        }
                                                                    }
                                                                },
                                                                onCreateFailed = {
                                                                    showCannotStartLivestreamDialog =
                                                                        true
                                                                },
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            .testTag(getAccessibilityId()),
                                        alpha = if (startStreamButtonEnable) 1f else 0.3f
                                    )
                                }
                            }

                            liveKitRoomState == Room.State.CONNECTED -> {
                                isStarting = false
                                Box(modifier = Modifier.fillMaxSize()) {
                                    if (showCountdownEndingLivestream) {
                                        AmityCircularProgressWithCountDownTimer(
                                            totalTime = LIVESTREAM_COUNTDOWN_DURATION,
                                            onTimeUp = {
                                                endLivestream(
                                                    context = context,
                                                    durationDisposable = durationDisposable,
                                                    behavior = behavior,
                                                    uiState = uiState,
                                                    viewModel = viewModel,
                                                    showLivestreamPostExceeded = true,
                                                    shouldNavigateToPostDetails = !fromEventPage,
                                                )
                                                showCountdownEndingLivestream = false
                                            }
                                        )
                                    }

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
                                            ),
                                        contentAlignment = Alignment.TopStart,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.TopCenter)
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            AmityBaseElement(
                                                pageScope = getPageScope(),
                                                elementId = "cancel_create_livestream_button"
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.TopStart,
                                                    modifier = Modifier
                                                ) {
                                                    Image(
                                                        modifier = Modifier
                                                            .padding(end = 12.dp)
                                                            .size(32.dp)
                                                            .clickableWithoutRipple {
                                                                showEndLivestreamDialog = true
                                                            }
                                                            .testTag(getAccessibilityId()),
                                                        painter = painterResource(id = R.drawable.amity_ic_room_close),
                                                        contentDescription = "end_livestream_button",
                                                        contentScale = ContentScale.Fit
                                                    )
                                                }
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                if ((uiState.viewerCount ?: 0) > 0) {
                                                    AmityRoomViewerCountBadge(
                                                        viewerCount = uiState.viewerCount,
                                                        isHost = true,
                                                        modifier = Modifier.padding(end = 4.dp)
                                                    )
                                                }
                                                AmityBaseElement(
                                                    pageScope = getPageScope(),
                                                    elementId = "live_timer_status"
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .background(
                                                                color = getConfig().getBackgroundColor(),
                                                                shape = RoundedCornerShape(4.dp)
                                                            )
                                                            .padding(
                                                                horizontal = 8.dp,
                                                                vertical = 4.dp
                                                            )
                                                    ) {
                                                        // Use this spacer to make it same height with viewer count badge
                                                        Spacer(
                                                            modifier = Modifier
                                                                .height(16.dp)
                                                                .width(0.dp)
                                                        )
                                                        Text(
                                                            text = formatTime(
                                                                liveHour,
                                                                liveMin,
                                                                liveSecond
                                                            ),
                                                            color = Color.White,
                                                            style = AmityTheme.typography.captionBold.copy(
                                                                fontSize = 13.sp,
                                                                lineHeight = 18.sp
                                                            ),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }

                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                                if (uiState.post?.getTarget() is AmityPost.Target.COMMUNITY) {
                                                    AmityBaseElement(
                                                        pageScope = getPageScope(),
                                                        elementId = "create_livestream_option_button"
                                                    ) {
                                                        Box(
                                                            contentAlignment = Alignment.TopStart,
                                                            modifier = Modifier
                                                                .size(32.dp)
                                                        ) {
                                                            Icon(
                                                                modifier = Modifier
                                                                    .size(32.dp)
                                                                    .clickableWithoutRipple {
                                                                        showBottomSheet = true
                                                                    }
                                                                    .testTag(getAccessibilityId()),
                                                                painter = painterResource(id = R.drawable.amity_ic_more_vertical),
                                                                contentDescription = "create_livestream_option_button",
                                                                tint = Color.White
                                                            )
                                                        }
                                                    }
                                                }
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
                                                        if (AmityCoreClient.isVisitor()) {
                                                            behavior.handleVisitorUserAction()
                                                        } else {
//                                                        AmityCoreClient.newLiveReactionRepository()
//                                                            .createReaction(
//                                                                liveStreamId = uiState.streamObj?.getStreamId()
//                                                                    ?: "",
//                                                                referenceId = uiState.createPostId
//                                                                    ?: "",
//                                                                referenceType = AmityLiveReactionReferenceType.POST,
//                                                                reactionName = reaction.name,
//                                                            )
                                                        }
                                                    },
                                                    onDismiss = { showReactionPicker = false },
                                                    modifier = Modifier.padding(
                                                        bottom = 56.dp,
                                                        end = 16.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            liveKitRoomState == Room.State.CONNECTING || isStarting -> {
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
                            if (uiState.room == null) {
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    componentScope = getComponentScope(),
                                    elementId = "add_thumbnail_button"
                                ) {
                                    when (uiState.thumbnailUploadUiState) {
                                        is LivestreamThumbnailUploadUiState.Success -> {
                                            AmityThumbnailView(
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                thumbnailsUri = uiState.thumbnailUri,
                                                isShowProgressIndicator = false
                                            ) {
                                                showEditThumbnailSheet = true
                                            }
                                        }

                                        is LivestreamThumbnailUploadUiState.Uploading -> {
                                            AmityThumbnailView(
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                thumbnailsUri = uiState.thumbnailUri,
                                                isShowProgressIndicator = true
                                            ) {
                                                showEditThumbnailSheet = true
                                            }
                                        }

                                        is LivestreamThumbnailUploadUiState.Idle -> {
                                            Row(
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart)
                                                    .clickableWithoutRipple {
                                                        if (isCameraAndRecAudioPermissionGranted) {
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                                mediaPickerLauncher.launch(
                                                                    PickVisualMediaRequest(
                                                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                                                    )
                                                                )
                                                            } else {
                                                                if (isMediaPickerPermissionGranted) {
                                                                    mediaPickerLauncher.launch(
                                                                        PickVisualMediaRequest(
                                                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                                                        )
                                                                    )
                                                                } else {
                                                                    mediaPickerPermissionLauncher.launch(
                                                                        mediaPickerPermissions
                                                                    )
                                                                }
                                                            }
                                                            haptics.performHapticFeedback(
                                                                HapticFeedbackType.LongPress
                                                            )
                                                        }
                                                    },
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Image(
                                                    painter = painterResource(getConfig().getIcon()),
                                                    contentDescription = "addThumbNail",
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .testTag("addThumbNail"),
                                                    alpha = if (isCameraAndRecAudioPermissionGranted) 1f else 0.5f
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = getConfig().getText(),
                                                    color = if (isCameraAndRecAudioPermissionGranted) Color.White else Color.White.copy(
                                                        alpha = 0.5f
                                                    ),
                                                    style = AmityTheme.typography.bodyLegacy.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                )
                                            }
                                        }

                                        else -> {}
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (targetType == AmityPost.TargetType.COMMUNITY) {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        componentScope = getComponentScope(),
                                        elementId = "create_livestream_settings_button"
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.amity_ic_room_create_setting),
                                            contentDescription = "create livestream settings button",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .testTag("create_livestream_settings_button")
                                                .clickableWithoutRipple {
                                                    showBottomSheet = true
                                                },
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(9.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.CenterVertically)
                                        .clickableWithoutRipple {
                                            userEnabledMic = !userEnabledMic
                                            focusManager.clearFocus()
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
            if (liveKitRoomState == Room.State.CONNECTED || liveKitRoomState == Room.State.RECONNECTING) {
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
            }
            if (uiState.isPendingApproval != true && (liveKitRoomState == Room.State.CONNECTED)) {
                if ((uiState.post?.getTarget() is AmityPost.Target.COMMUNITY)) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .imePadding()
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
                                .fillMaxHeight(0.2f)
                            ,
                            pageScope = getPageScope(),
                            channelId = uiState.channelId ?: "",
                            streamHostUserId = uiState.hostUserId,
                            coHostUserId = if (uiState.invitation == null) uiState.cohostUserId else null,
                            onReactionClick = { showReactionPicker = true },
                            canInviteCohost = uiState.cohostUserId.isNullOrBlank(),
                            onInviteCohost = { userId, user ->
                                showInviteConfirmDialog = Pair(userId, user)
                            },
                            onCohostBadgeClick = {
                                showRemoveCoHost = true
                            }
                        )
                        uiState.channelId.let {
                            AmityLivestreamMessageComposeBar(
                                pageScope = getPageScope(),
                                channelId = it ?: "",
                                value = messageText,
                                isPendingApproval = uiState.isPendingApproval ?: false,
                                streamHostUserId = uiState.hostUserId,
                                onValueChange = {
                                    messageText = it
                                },
                                onSend = {},
                                onReactionClick = {
//                                    AmityMessageReactions
//                                        .getList()
//                                        .getOrNull(1)
//                                        ?: AmityMessageReactions
//                                            .getList()
//                                            .firstOrNull()
//                                            ?.let { defaultReaction ->
//                                                floatingReactions.add(
//                                                    FloatingReaction(
//                                                        reaction = defaultReaction,
//                                                        id = System.currentTimeMillis()
//                                                    )
//                                                )
//                                                if (AmityCoreClient.isVisitor()) {
//                                                    behavior.handleVisitorUserAction()
//                                                } else {
//                                                    uiState.streamObj?.let { stream ->
//                                                        AmityCoreClient.newLiveReactionRepository()
//                                                            .createReaction(
//                                                                liveStreamId = stream.getStreamId(),
//                                                                referenceId = uiState.createPostId
//                                                                    ?: "",
//                                                                referenceType = AmityLiveReactionReferenceType.POST,
//                                                                reactionName = defaultReaction.name,
//                                                            )
//                                                    }
//                                                }
//                                            }
                                },
                                onReactionLongClick = { showReactionPicker = true },
                                isMicrophoneMute = !userEnabledMic,
                                onToggleMicrophone = {
                                    userEnabledMic = !userEnabledMic
                                },
                                onSwitchCamera = {
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
                                },
                                onOpenInviteSheet = if (isTargetCommunity) {
                                    {
                                        uiState.room
                                            ?.getRoomId()
                                            ?.let(viewModel::getRoomViewers)
                                        showInviteCoHostSheet = true
                                    }
                                } else { null }
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .imePadding()
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickableWithoutRipple {
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
                                        focusManager.clearFocus()
                                    }
                                    .testTag("switch_camera_button")
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.amity_v4_switch_camera_button),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(36.dp)
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
                    modifier = Modifier.height(255.dp).navigationBarsPadding()
                ) {
                    AmityCreateRoomSettings(
                        isReadOnly = isReadOnly,
                        onReadOnlyToggle = {
                            isReadOnly = it
                            uiState.channelId?.let { channelId ->
                                viewModel.setIsMutedChannel(channelId, it)
                            }
                        }
                    )
                    val post = uiState.post
                    val postLink = if(post != null) AmityUIKitConfigController.getPostLink(post) else ""
                    if(postLink.isNotEmptyOrBlank()) {
                        AmityBottomSheetActionItem(
                            icon = R.drawable.amity_v4_link_icon,
                            text = "Copy live stream link",
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                            color = Color(0xFFEBECEF)
                        ) {
                            showBottomSheet = false
                            // Generate the post link URL (adjust the URL format according to your app's deep linking structure)
                            clipboardManager.setText(AnnotatedString(postLink))
                            AmityUIKitSnackbar.publishSnackbarMessage("Link copied")
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

        if (showCannotStartLivestreamDialog) {
            AmityAlertDialog(
                dialogTitle = "Cannot start live stream",
                dialogText = "Something went wrong while trying to complete your request.\nPlease try again.",
                dismissText = "OK",
                onDismissRequest = {
                    showCannotStartLivestreamDialog = false
                },
            )
        }

        if (showThumbnailErrorUploadDialog.first) {
            val title =
                if (showThumbnailErrorUploadDialog.second == AmityError.BUSINESS_ERROR.code) {
                    "Inappropriate image"
                } else {
                    "Upload failed"
                }
            val desc =
                if (showThumbnailErrorUploadDialog.second == AmityError.BUSINESS_ERROR.code) {
                    "Please choose a different image to upload."
                } else {
                    "Please try again."
                }
            AmityAlertDialog(
                dialogTitle = title,
                dialogText = desc,
                dismissText = "OK",
                onDismissRequest = {
                    showThumbnailErrorUploadDialog = Pair(false, 0)
                },
            )
        }

        if (showDiscardPostDialog) {
            AmityAlertDialog(
                dialogTitle = stringResource(R.string.amity_v4_create_livestream_discard_livestream_dialog_title),
                dialogText = stringResource(R.string.amity_v4_create_livestream_discard_livestream_dialog_desc),
                confirmText = stringResource(R.string.amity_v4_create_livestream_discard_livestream_dialog_confirm_text),
                dismissText = stringResource(R.string.amity_v4_create_livestream_dialog_dismiss_text),
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.highlight,
                onConfirmation = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                },
                onDismissRequest = {
                    showDiscardPostDialog = false
                }
            )
        }

        if (showEndLivestreamDialog) {
            AmityAlertDialog(
                dialogTitle = stringResource(R.string.amity_v4_create_livestream_end_livestream_dialog_title),
                dialogText = stringResource(R.string.amity_v4_create_livestream_end_livestream_dialog_desc),
                confirmText = stringResource(R.string.amity_v4_create_livestream_end_livestream_dialog_confirm_text),
                dismissText = stringResource(R.string.amity_v4_create_livestream_dialog_dismiss_text),
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.highlight,
                onConfirmation = {
                    endLivestream(
                        context = context,
                        durationDisposable = durationDisposable,
                        behavior = behavior,
                        uiState = uiState,
                        viewModel = viewModel,
                        shouldNavigateToPostDetails = !fromEventPage,
                    )
                },
                onDismissRequest = {
                    showEndLivestreamDialog = false
                }
            )
        }

        if (showEditThumbnailSheet) {
            AmityEditThumbnailSheet(
                modifier = Modifier,
            ) {
                showEditThumbnailSheet = false

                it?.let { isChangeThumbnailSelect ->
                    if (isChangeThumbnailSelect) {
                        mediaPickerLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    } else {
                        viewModel.removeThumbnail()
                    }
                }
            }
        }

        showInviteConfirmDialog?.let {
            AmityAlertDialog(
                dialogTitle = "Invite co-host",
                dialogText = "If ${it.second?.getDisplayName() ?: "the user"} accepts your invitation, they'll become your co-host with moderator access. Co-host can turn on camera and mic, appear alongside you on stage and help manage the event chat. ",
                confirmText = "Invite co-host",
                dismissText = stringResource(R.string.amity_v4_create_livestream_dialog_dismiss_text),
                confirmTextColor = AmityTheme.colors.highlight,
                dismissTextColor = AmityTheme.colors.highlight,
                onConfirmation = {
                    viewModel.inviteCohost(userId = it.first, user = it.second)
                    showInviteConfirmDialog = null
                },
                onDismissRequest = {
                    showInviteConfirmDialog = null
                }
            )
        }

        if (showCancelInvitationDialog) {
            AmityAlertDialog(
                dialogTitle = "Cancel co-host invitation?",
                dialogText = "If you cancel, this user will remain in your live stream as a viewer. You will need to send a new invitation for them to become co-host.",
                confirmText = "Confirm",
                dismissText = stringResource(R.string.amity_v4_create_livestream_dialog_dismiss_text),
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.highlight,
                onConfirmation = {
                    uiState
                        .invitation
                        ?.let(viewModel::cancelInvitation)
                    showCancelInvitationDialog = false
                },
                onDismissRequest = {
                    showCancelInvitationDialog = false
                }
            )
        }

        if (showRemoveCoHostConfirmDialog) {
            AmityAlertDialog(
                dialogTitle = "Remove co-host?",
                dialogText = "This user will immediately stop broadcasting as co-host in your live stream and will return to the event as a viewer.",
                confirmText = "Remove",
                dismissText = stringResource(R.string.amity_v4_create_livestream_dialog_dismiss_text),
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.highlight,
                onConfirmation = {
                    val roomId = uiState.room?.getRoomId()
                    val coHostUserId = uiState.cohostUserId
                    if (roomId != null && coHostUserId != null) {
                        viewModel.removeParticipant(
                            roomId = roomId,
                            userId = coHostUserId
                        )
                    }
                    showRemoveCoHostConfirmDialog = false
                },
                onDismissRequest = {
                    showRemoveCoHostConfirmDialog = false
                }
            )
        }

        if (showLivestreamLimitSnackBar) {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                message = stringResource(R.string.amity_v4_create_livestream_duration_exceed_snackbar),
                offsetFromBottom = 130
            )
            showLivestreamLimitSnackBar = false
        }

        if (showRemoveCoHost) {
            val notJoinedYet = uiState.invitation != null
            ModalBottomSheet(
                onDismissRequest = {
                    showRemoveCoHost = false
                },
                sheetState = removeCoHostSheetState,
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
                    Text(
                        text = uiState.cohostUser?.getDisplayName() ?: "Unknown User",
                        style = AmityTheme.typography.titleBold.copy(
                            color = Color(0xFFEBECEF)
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF292B32))
                    )
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_cohost_remove,
                        text = if (notJoinedYet) "Cancel invitation" else "Remove as co-host",
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        color = AmityTheme.colors.alert,
                    ) {
                        showRemoveCoHost = false
                        if (notJoinedYet) {
                            showCancelInvitationDialog = true
                        } else {
                            showRemoveCoHostConfirmDialog = true
                        }
                    }
                }

            }
        }

        if (showInviteCoHostSheet) {
            AmityInviteCoHostBottomSheet(
                users = uiState.viewers,
                coHostUser = if (uiState.invitation == null) uiState.cohostUser else null,
                invitedUser = if (uiState.invitation?.getStatus() == AmityInvitationStatus.PENDING) uiState.cohostUser else null,
                onDismiss = {
                    showInviteCoHostSheet = false
                },
                onInvite = { user ->
                    showInviteCoHostSheet = false
                    showInviteConfirmDialog = Pair(user.getUserId(), user)
                },
                onCancel = {
                    showInviteCoHostSheet = false
                    showCancelInvitationDialog = true
                },
                onRemove = {
                    showInviteCoHostSheet = false
                    showRemoveCoHostConfirmDialog = true
                }
            )
        }
    }
}

private fun endLivestream(
    context: Context,
    durationDisposable: Disposable?,
    behavior: AmityCreateRoomPageBehavior,
    uiState: AmityCreateRoomPageUiState,
    viewModel: AmityCreateRoomPageViewModel,
    showLivestreamPostExceeded: Boolean = false,
    errorType: LivestreamErrorScreenType = LivestreamErrorScreenType.NONE,
    shouldNavigateToPostDetails: Boolean = true,
) {
    uiState.liveKitRoom?.let(::stopLocalCameraTrack)
    uiState.room?.getRoomId()?.let(viewModel::disposeRoom)
    if (durationDisposable?.isDisposed == false) {
        durationDisposable.dispose()
    }
    if (shouldNavigateToPostDetails) {
        behavior.goToPostDetailPage(
            context = context,
            id = uiState.createPostId ?: "",
            category = AmityPostCategory.GENERAL,
            showLivestreamPostExceeded = showLivestreamPostExceeded
        )
    }
    context.closePageWithResult(Activity.RESULT_OK)
}

fun formatTime(hour: Int, minute: Int, second: Int): String {
    return when {
        hour > 0 -> "LIVE $hour:%02d:%02d".format(minute, second)
        minute >= 10 -> "LIVE %d:%02d".format(minute, second)
        else -> "LIVE %d:%02d".format(minute, second)
    }
}


@Composable
fun EventRoomPlayerHeader(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    room: AmityRoom,
) {
    val context = LocalContext.current
    val creator = room.getCreator()
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

            // Community avatar
            AmityAvatarView(
                image = room.getThumbnail(),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                placeholder = R.drawable.amity_ic_default_community_avatar_circular
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Community name and display name
            Column {
                Text(
                    text = room.getTitle() ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "By ${creator?.getDisplayName() ?: "Unknown User"}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Show brand badge if user is a brand
                    val isBrandCreator = creator?.isBrand() == true
                    if (isBrandCreator) {
                        Image(
                            painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                            contentDescription = "Brand badge",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmityInviteCoHostHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Invite co-host",
            style = AmityTheme.typography.titleBold.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterEnd)
                .clickableWithoutRipple {
                    onDismiss()
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_close),
                contentDescription = "Close",
                tint = Color.White,
            )
        }
    }
}

@Composable
fun AmityViewerListItem(
    modifier: Modifier = Modifier,
    user: AmityUser,
    onInviteClick: (() -> Unit)? = null,
    onCancelClick: (() -> Unit)? = null,
    onRemoveClick: (() -> Unit)? = null,
) {
    val buttonText = if (onRemoveClick != null) {
        "Remove"
    } else if (onCancelClick != null) {
        "Cancel"
    } else {
        "Invite"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmityUserAvatarView(
            user = user,
            size = 40.dp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = user.getDisplayName() ?: "Unknown user",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.bodyBold.copy(
                    color = Color.White
                ),
            )
            val isBrandCreator = user.isBrand()
            if (isBrandCreator) {
                Image(
                    painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                    contentDescription = "Brand badge",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 2.dp)
                )
            }
        }

        val isCancel = onCancelClick != null
        Box(
            modifier = Modifier
                .alpha( if (onInviteClick == null && onRemoveClick == null && onCancelClick == null) 0.3f else 1f)
                .background( if (isCancel) { Color(0xFF40434E) } else { AmityTheme.colors.highlight}, RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .clickableWithoutRipple{
                    if (isCancel) {
                        onCancelClick.invoke()
                    } else if (onInviteClick != null) {
                        onInviteClick.invoke()
                    } else if (onRemoveClick != null) {
                        onRemoveClick.invoke()
                    }
                },
        ) {
            Text(
                text = buttonText,
                style = AmityTheme.typography.captionBold.copy(
                    color = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityInviteCoHostBottomSheet(
    users: List<AmityUser>,
    invitedUser: AmityUser?,
    coHostUser: AmityUser?,
    onDismiss: () -> Unit,
    onInvite: (AmityUser) -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val remainingUsers = users.filter { it.getUserId() != invitedUser?.getUserId() && it.getUserId() != coHostUser?.getUserId() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1C1E),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(Color(0xFF48484A))
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            AmityInviteCoHostHeader(onDismiss = onDismiss)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF292B32))
            )
            if (coHostUser != null) {
                Text(
                    text = "Co-hosting",
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                AmityViewerListItem(
                    user = coHostUser,
                    onRemoveClick = {
                        onRemove()
                    }
                )
            } else if (invitedUser != null) {
                Text(
                    text = "Pending invitation",
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                AmityViewerListItem(
                    user = invitedUser,
                    onCancelClick = {
                        onCancel()
                    }
                )
            }
            if (remainingUsers.isNotEmpty()) {
                Text(
                    text = "Who's watching",
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                // List of watchers
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(remainingUsers.size) { index ->
                        val user = remainingUsers[index]
                        AmityViewerListItem(
                            user = user,
                            onInviteClick = if (invitedUser == null) {
                                { onInvite(user) }
                            } else {
                                null
                            }
                        )
                    }
                }
            }
            if (invitedUser == null && coHostUser == null && remainingUsers.isEmpty()){
                EmptyViewerList()
            }

        }
    }
}

@Composable
fun EmptyViewerList() {
    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.amity_ic_invite_cohost_in_chat),
            tint = Color(0xFF40434E),
            contentDescription = "empty invitation icon",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No one's watching right now",
            style = AmityTheme.typography.titleBold.copy(
                color = Color(0xFF898E9E),
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = "Come back to invite a co-host once viewers have joined your live stream.",
            style = AmityTheme.typography.caption.copy(
                color = Color(0xFF898E9E),
                textAlign = TextAlign.Center,
            )
        )
    }
}

fun stopLocalCameraTrack(room: Room) {
    try {
        room.localParticipant
            .getTrackPublication(Track.Source.CAMERA)
            ?.track?.let { track ->
                (track as? LocalVideoTrack)?.stop()
            }
    } catch (e: Exception) {
        // Avoid unimplemented errors on custom stock camera implementations
    }
}