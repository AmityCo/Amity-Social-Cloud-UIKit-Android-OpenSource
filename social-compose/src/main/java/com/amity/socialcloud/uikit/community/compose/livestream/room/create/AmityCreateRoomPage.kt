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
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.semantics.semantics
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
import com.amity.socialcloud.sdk.core.engine.analytics.AnalyticsEventSourceType
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitationStatus
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
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
import com.amity.socialcloud.uikit.common.ui.theme.amityLivestreamSurfaceDivider
import com.amity.socialcloud.uikit.common.ui.theme.amityLivestreamSurfaceElevated
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamMessageComposeBar
import com.amity.socialcloud.uikit.community.compose.livestream.chat.ChatOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReaction
import com.amity.socialcloud.uikit.community.compose.livestream.chat.FloatingReactionsOverlay
import com.amity.socialcloud.uikit.community.compose.livestream.chat.HostBadge
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
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityAddProductBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductTaggingBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductTaggingButton
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewComponent
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityRoomViewerCountBadge
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityStreamerView
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.LivestreamPinnedProductElement
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamDeclinedPage
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.product.AmityProductTagSelectionComponent
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
import kotlin.collections.orEmpty
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityColorGray

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
    val myTimelineLabel = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_my_timeline")

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
                viewModel.setupScreen(myTimelineLabel = myTimelineLabel)
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
    var showProductTaggingUnavailableDialog: (() -> Unit)? by remember { mutableStateOf(null) } // pre live
    var showProductTaggingDisabledDialog by remember { mutableStateOf(false) } // during live
    var showDisableCohostManageProductPermissionDialog: (() -> Unit)? by remember { mutableStateOf(null) }
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
    var showCreateRoomBottomSheet by remember { mutableStateOf(false) }
    var showManageProductTagBottomSheet by remember { mutableStateOf(false) }
    var showAddProductBottomSheet by remember { mutableStateOf(false) }
    var showRemoveCoHost by remember { mutableStateOf(false) }
    var showProductWebViewBottomSheet by remember { mutableStateOf<AmityProduct?>(null) }
    var isReadOnly by remember { mutableStateOf(false) }
    var isStarting by remember { mutableStateOf(false) }
    // Product management uses the server post as source of truth only once the
    // room is live; before that (fresh create OR an existing post opened from an
    // event page) products are staged in local state. Keying reads on isLive keeps
    // them consistent with the write path in the view model, which branches the
    // same way. Reading getRoomPost() unconditionally hid staged products whenever
    // a post already existed (the from-event case).
    val taggedProducts = if (uiState.isLive) {
        uiState.getRoomPost()?.getProducts().orEmpty()
    } else {
        uiState.taggedProducts.orEmpty()
    }
    val pinnedProductId = if (uiState.isLive) {
        uiState.getRoomPost()?.getPinnedProductId() ?: uiState.pinnedProductId
    } else {
        uiState.pinnedProductId
    }
    val pinnedProduct = if (uiState.isLive) {
        uiState.getRoomPost()?.getPinnedProduct()
    } else {
        pinnedProductId?.let { id -> taggedProducts.firstOrNull { it.getProductId() == id } }
    }
    val taggedProductsCount = if (uiState.isLive) {
        uiState.getRoomPost()?.getProductTags()?.size ?: 0
    } else {
        uiState.taggedProducts?.size ?: 0
    }

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

    val activity = LocalContext.current as? AmityCreateRoomPageActivity

    // Shared go-live action invoked by both the on-screen button and the Bluetooth remote control.
    val triggerGoLive: () -> Unit = {
        if (startStreamButtonEnable && !isStarting) {
            val startAction: () -> Unit = {
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
                            showCannotStartLivestreamDialog = true
                        },
                    )
                } else {
                    uiState.liveTitle?.let {
                        viewModel.createRoomPost(
                            title = it,
                            description = uiState.liveDesc ?: " ",
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
                                showCannotStartLivestreamDialog = true
                            },
                        )
                    }
                }
            }
            if (uiState.taggedProducts.isNullOrEmpty()) {
                startAction()
            } else {
                viewModel.fetchProductCatalogueSettings(
                    onEnabledAction = {
                        startAction()
                    },
                    onDisabledAction = {
                        showProductTaggingUnavailableDialog = startAction
                    }
                )
            }
        }
    }
    val currentTriggerGoLive by rememberUpdatedState(triggerGoLive)

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

    SideEffect {
        // revert the volume key state
        activity?.isLiveActive = uiState.isLive
    }

    LaunchedEffect(activity) {
        activity?.remoteShutterFlow?.collect {
            currentTriggerGoLive()
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
            viewModel.setupScreen(myTimelineLabel = myTimelineLabel)
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
                    .background(amityMediaSurface)
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
                        .background(amityMediaSurface)
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
                            invitation = uiState.invitation,
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
                                    amityColorBlack.copy(
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
                                            Box(
                                                modifier = Modifier
                                                    .padding(end = 12.dp)
                                                    .size(32.dp)
                                                    .then(
                                                        if (isTargetCommunity) {
                                                            Modifier
                                                                .background(
                                                                    color = amityColorBlack.copy(alpha = 0.5f),
                                                                    shape = CircleShape
                                                                )
                                                                .padding(4.dp)
                                                        } else {
                                                            Modifier
                                                        }
                                                    )
                                            ) {
                                                Image(
                                                    modifier = Modifier
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
                                        }
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_create_livestream_target_title"),
                                                color = AmityTheme.colors.baseInverse,
                                                style = AmityTheme.typography.bodyLegacy
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = uiState.targetName ?: "",
                                                color = AmityTheme.colors.baseInverse,
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
                                                color = AmityTheme.colors.baseInverse,
                                                trackColor = amityColorGray,
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
                                                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_placeholder_create_livestream_title_placeholder"),
                                                        color = amityColorWhite.copy(alpha = 0.8f),
                                                        style = AmityTheme.typography.headLine.copy(
                                                            color = AmityTheme.colors.baseInverse,
                                                            textAlign = TextAlign.Start,
                                                        )
                                                    )
                                                },
                                                singleLine = true,
                                                maxCharLength = 30,
                                                textStyle = AmityTheme.typography.headLine.copy(
                                                    color = AmityTheme.colors.baseInverse,
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
                                                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_placeholder_create_livestream_desc_placeholder"),
                                                        color = amityColorWhite.copy(alpha = 0.8f),
                                                        style = AmityTheme.typography.body.copy(
                                                            color = AmityTheme.colors.baseInverse,
                                                            textAlign = TextAlign.Start,
                                                        )
                                                    )
                                                },
                                                singleLine = false,
                                                textStyle = AmityTheme.typography.body.copy(
                                                    color = AmityTheme.colors.baseInverse,
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
                                            .background(amityColorBlack.copy(alpha = 0.5f)),
                                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_permission_title_allow_camera_mic_access"),
                                        description = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_allow_camera_desc"),
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
                                                triggerGoLive()
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
                                                        amityColorBlack.copy(alpha = 0.6f),
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
                                                            color = AmityTheme.colors.baseInverse,
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
                                                                        showCreateRoomBottomSheet = true
                                                                    }
                                                                    .testTag(getAccessibilityId()),
                                                                painter = painterResource(id = R.drawable.amity_ic_more_vertical),
                                                                contentDescription = "create_livestream_option_button",
                                                                tint = AmityTheme.colors.baseInverse
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
                                    color = AmityTheme.colors.baseInverse,
                                    trackColor = amityColorGray,
                                    strokeWidth = 2.dp,
                                    strokeCap = StrokeCap.Round
                                )
                                Spacer(Modifier.height(13.dp))
                                Text(
                                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_create_livestream_connecting_text"),
                                    color = AmityTheme.colors.baseInverse,
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
                                .background(amityMediaSurface)
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
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart)
                                                    .width(70.dp)
                                                    .height(40.dp)
                                                    .clip(RoundedCornerShape(size = 4.dp))
                                                    .background(AmityTheme.colors.baseShade4)
                                                    .border(
                                                        width = 1.dp,
                                                        color = AmityTheme.colors.baseShade3,
                                                        shape = RoundedCornerShape(size = 4.dp)
                                                    )
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
                                                    }
                                            ) {
                                                Image(
                                                    painter = painterResource(R.drawable.amity_ic_create_livestream_add_thumbnail),
                                                    contentDescription = "add_thumbnail_button",
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .align(Alignment.Center)
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
                                    if (uiState.isProductCatalogueEnabled) {
                                        AmityProductTaggingButton(
                                            taggedProductsCount = taggedProductsCount,
                                            onClick = { showManageProductTagBottomSheet = true },
                                            componentScope = getComponentScope()
                                        )
                                        Spacer(modifier = Modifier.width(9.dp))
                                    }
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
                                                    showCreateRoomBottomSheet = true
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
                } else if (uiState.isPendingApproval == true) {
                    AmityBaseComponent(
                        pageScope = getPageScope(),
                        componentId = "create_livestream_bottom_bar"
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(amityMediaSurface)
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (targetType == AmityPost.TargetType.COMMUNITY && uiState.isProductCatalogueEnabled) {
                                    AmityProductTaggingButton(
                                        taggedProductsCount = taggedProductsCount,
                                        onClick = { showManageProductTagBottomSheet = true },
                                        componentScope = getComponentScope()
                                    )
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
            if ((liveKitRoomState == Room.State.CONNECTED || liveKitRoomState == Room.State.RECONNECTING) && uiState.isPendingApproval != true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Transparent,
                                    0.3f to amityMediaSurface.copy(alpha = 0.1f),
                                    0.6f to amityMediaSurface.copy(alpha = 0.4f),
                                    0.8f to amityMediaSurface.copy(alpha = 0.7f),
                                    1.0f to amityMediaSurface
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
                        if (pinnedProduct != null && uiState.isProductCatalogueEnabled) {
                            LivestreamPinnedProductElement(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                product = pinnedProduct,
                                pageScope = getPageScope(),
                                onUnpinClick = {
                                    viewModel.togglePinProduct(null)
                                },
                                onRemoveClick = {
                                    viewModel.removeTaggedProduct(pinnedProduct.getProductId())
                                },
                                onProductClick = {
                                    showProductWebViewBottomSheet = pinnedProduct
                                },
                                canManageProducts = true
                            )
                        }
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
                                } else { null },
                                canManageProducts = isTargetCommunity,
                                taggedProductsCount = taggedProductsCount,
                                onTaggedProductClick = {
                                    viewModel.fetchProductCatalogueSettings(
                                        onEnabledAction = {
                                            uiState.createPostId?.let { postId ->
                                                viewModel.getPost(postId)
                                            }
                                            showManageProductTagBottomSheet = true
                                        },
                                        onDisabledAction = {
                                            showProductTaggingDisabledDialog = true
                                        }
                                    )
                                },
                                isProductSettingsEnabled = uiState.isProductCatalogueEnabled
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

        if (showCreateRoomBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showCreateRoomBottomSheet = false
                },
                sheetState = bottomSheetState,
                containerColor = AmityTheme.colors.background,
                contentColor = AmityTheme.colors.baseInverse,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 20.dp)
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                amityColorGray,
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
                            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_copy_live_stream_link"),
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                            color = AmityTheme.colors.base
                        ) {
                            showCreateRoomBottomSheet = false
                            // Generate the post link URL (adjust the URL format according to your app's deep linking structure)
                            clipboardManager.setText(AnnotatedString(postLink))
                            AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_link_copied"))
                        }

                        AmityBottomSheetActionItem(
                            icon = R.drawable.amity_v4_share_icon,
                            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_share_to"),
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                            color = AmityTheme.colors.base
                        ) {
                            showCreateRoomBottomSheet = false
                            // Open native Android share sheet
                            sharePost(context, postLink)
                        }
                    }
                }

            }
        }

        if (showCannotStartLivestreamDialog) {
            AmityAlertDialog(
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_cannot_start_livestream"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_request_failed"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_ok"),
                onDismissRequest = {
                    showCannotStartLivestreamDialog = false
                },
            )
        }

        if (showThumbnailErrorUploadDialog.first) {
            val title =
                if (showThumbnailErrorUploadDialog.second == AmityError.BUSINESS_ERROR.code) {
                    DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_inappropriate_image")
                } else {
                    DefaultAmitySocialStringProvider.getInstance().getString("amity_social_error_upload_failed")
                }
            val desc =
                if (showThumbnailErrorUploadDialog.second == AmityError.BUSINESS_ERROR.code) {
                    DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_choose_different_image")
                } else {
                    DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_please_try_again")
                }
            AmityAlertDialog(
                dialogTitle = title,
                dialogText = desc,
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_ok"),
                onDismissRequest = {
                    showThumbnailErrorUploadDialog = Pair(false, 0)
                },
            )
        }

        if (showDiscardPostDialog) {
            AmityAlertDialog(
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_discard_livestream_dialog_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_discard_livestream_dialog_desc"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_discard_livestream_dialog_confirm_text"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
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
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_end_livestream_dialog_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_end_livestream_dialog_desc"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_create_livestream_end_livestream_dialog_confirm_text"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
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
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_invite_co_host"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_confirm_invite_cohost_message").format(it.second?.getDisplayName() ?: "the user"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_invite_co_host"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
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
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_alert_cancel_cohost_invitation_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_alert_cancel_cohost_invitation_message"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_confirm"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
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
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_alert_remove_cohost_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_alert_remove_cohost_message"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_remove"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
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
                message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_create_livestream_duration_exceed_snackbar"),
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
                containerColor = AmityTheme.colors.background,
                contentColor = AmityTheme.colors.baseInverse,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 20.dp)
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                amityColorGray,
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
                        text = uiState.cohostUser?.getDisplayName() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unknown_user"),
                        style = AmityTheme.typography.titleBold.copy(
                            color = AmityTheme.colors.base
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        HostBadge(
                            modifier = Modifier.align(Alignment.Center),
                            isCoHost = true
                        ) { }
                    }
                    Box(modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AmityTheme.colors.baseShade4)
                    )
                    if (uiState.isProductCatalogueEnabled) {
                        AmityCoHostManageProductPermission(
                            isEnabled = uiState.isCoHostCanManageProducts(),
                            onToggle = {
                                if (it) {
                                    viewModel.updateCoHostManageProductPermission(it)
                                } else {
                                    showDisableCohostManageProductPermissionDialog = {
                                        viewModel.updateCoHostManageProductPermission(it)
                                    }
                                }
                            }
                        )
                    }
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_cohost_remove,
                        text = if (notJoinedYet) DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel_invitation") else DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_remove_from_live"),
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

        if (showManageProductTagBottomSheet) {
            AmityProductTaggingBottomSheet(
                pageScope = getPageScope(),
                onDismiss = {
                    showManageProductTagBottomSheet = false
                },
                onPinProduct = {
                    viewModel.togglePinProduct(it)
                },
                onRemoveProduct = {
                    viewModel.removeTaggedProduct(it)
                },
                onAddProducts = {
                    showAddProductBottomSheet = true
                },
                onProductClick = { product, location ->
                    showProductWebViewBottomSheet = product
                },
                pinnedProductId = pinnedProductId,
                taggedProducts = taggedProducts,
                canManageProducts = true,
                isNetworkConnected = uiState.networkConnection is NetworkConnectionEvent.Connected,
                isHost = true
            )
        }

        if (showAddProductBottomSheet) {
            AmityAddProductBottomSheet(
                pageScope = getPageScope(),
                onDismiss = {
                    showAddProductBottomSheet = false
                },
                onDone = {
                    showAddProductBottomSheet = false
                    viewModel.addTaggedProducts(it)
                },
                taggedProduct = taggedProducts,
                isNetworkConnected = uiState.networkConnection is NetworkConnectionEvent.Connected,
                requestFocus = true,
            )
        }

        showProductWebViewBottomSheet?.let { product ->
            AmityProductWebViewBottomSheet(
                product = product,
                onDismiss = {
                    showProductWebViewBottomSheet = null
                },
            )
        }

        if (showProductTaggingUnavailableDialog != null) {
            AmityAlertDialog(
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tagging_unavailable_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_remove_products_description"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_go_live"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_edit_live"),
                onDismissRequest = {
                    showProductTaggingUnavailableDialog = null
                },
                onConfirmation = {
                    showProductTaggingUnavailableDialog?.invoke()
                    showProductTaggingUnavailableDialog = null
                },
            )
        }

        if (showDisableCohostManageProductPermissionDialog != null) {
            AmityAlertDialog(
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_title_disable_cohost_product_tags"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_disable_product_tagging_description"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_disable"),
                dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
                onDismissRequest = {
                    showDisableCohostManageProductPermissionDialog = null
                },
                onConfirmation = {
                    showDisableCohostManageProductPermissionDialog?.invoke()
                    showDisableCohostManageProductPermissionDialog = null
                },
            )
        }

        if (showProductTaggingDisabledDialog) {
            AmityAlertDialog(
                dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tagging_unavailable_title"),
                dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_remove_products_description"),
                confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_ok"),
                dismissText = "",
                onDismissRequest = {
                    showProductTaggingDisabledDialog = false
                },
                onConfirmation = {
                    showProductTaggingDisabledDialog = false
                },
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
    val liveString = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_live")
    return when {
        hour > 0 -> "$liveString $hour:%02d:%02d".format(minute, second)
        minute >= 10 -> "$liveString %d:%02d".format(minute, second)
        else -> "$liveString %d:%02d".format(minute, second)
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
                    color = AmityTheme.colors.baseInverse,
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
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_by_creator").format(creator?.getDisplayName() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unknown_user")),
                        color = amityColorWhite.copy(alpha = 0.8f),
                        fontSize = 16.sp,
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
            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_invite_co_host"),
            style = AmityTheme.typography.titleBold.copy(
                fontWeight = FontWeight.SemiBold,
                color = AmityTheme.colors.baseInverse
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
                painter = painterResource(id = R.drawable.amity_ic_dismiss_preview),
                contentDescription = "Close",
                tint = AmityTheme.colors.baseInverse,
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
        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_remove")
    } else if (onCancelClick != null) {
        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel")
    } else {
        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_invite")
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(amityLivestreamSurfaceElevated)
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
                text = user.getDisplayName() ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unknown_user_lowercase"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.bodyBold.copy(
                    color = AmityTheme.colors.baseInverse
                ),
                modifier = Modifier.weight(1f, false)
            )
            val isBrandCreator = user.isBrand()
            if (isBrandCreator) {
                Image(
                    painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                    contentDescription = "Brand badge",
                    modifier = Modifier
                        .size(12.dp)
                        .padding(start = 2.dp)
                )
            }
        }

        val isCancel = onCancelClick != null
        Box(
            modifier = Modifier
                .alpha( if (onInviteClick == null && onRemoveClick == null && onCancelClick == null) 0.3f else 1f)
                .background( if (isCancel) { AmityTheme.colors.baseShade3 } else { AmityTheme.colors.highlight}, RoundedCornerShape(8.dp))
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
                    color = AmityTheme.colors.baseInverse
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
        containerColor = amityLivestreamSurfaceElevated,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(amityLivestreamSurfaceDivider)
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
                    .background(AmityTheme.colors.baseShade4)
            )
            if (coHostUser != null) {
                Text(
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_co_hosting"),
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.baseInverse
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
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_pending_invitation"),
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.baseInverse
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
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_whos_watching"),
                    style = AmityTheme.typography.titleBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.baseInverse
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
            tint = AmityTheme.colors.baseShade3,
            contentDescription = "empty invitation icon",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_no_viewers_title"),
            style = AmityTheme.typography.titleBold.copy(
                color = AmityTheme.colors.baseShade1,
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_no_viewers_message"),
            style = AmityTheme.typography.caption.copy(
                color = AmityTheme.colors.baseShade1,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun AmityCoHostManageProductPermission(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_allow_co_host_to_manage_product_tags"),
                color = AmityTheme.colors.background,
                style = AmityTheme.typography.bodyBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_when_enabled_co_host_can_add_or_remove_tagged_products_"),
                color = AmityTheme.colors.baseShade1,
                style = AmityTheme.typography.caption
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AmityTheme.colors.baseInverse,
                checkedTrackColor = AmityTheme.colors.primary,
                uncheckedThumbColor = AmityTheme.colors.baseInverse,
                uncheckedTrackColor = amityColorGray
            ),
            modifier = Modifier.align(Alignment.Top)
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