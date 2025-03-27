package com.amity.socialcloud.uikit.community.compose.livestream.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.video.AmityCameraView
import com.amity.socialcloud.sdk.video.AmityStreamBroadcaster
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterConfiguration
import com.amity.socialcloud.sdk.video.StreamBroadcaster
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.sdk.video.model.AmityStreamBroadcasterState
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.elements.drawVerticalScrollbar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity.Companion.EXTRA_PARAM_TARGET_ID
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity.Companion.EXTRA_PARAM_TARGET_TYPE
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCircularProgressWithCountDownTimer
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamNoInternetView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityCreateLivestreamNoPermissionView
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityEditThumbnailSheet
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityLivestreamTextField
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityThumbnailView
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.AmityCreateLivestreamPageUiState
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.LivestreamThumbnailUploadUiState
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.flowable.FlowableInterval
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private const val LIVESTREAM_DURATION_SNACK_BAR = 14220
private const val LIVESTREAM_COUNTDOWN_DURATION = 10
private const val LIVESTREAM_DURATION_COUNTDOWN = 14390
private const val LIVESTREAM_INTERNET_LOSS_MAXIMUM_DURATION = 180000L

@Composable
fun AmityCreateLivestreamPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityPost.TargetType,
    targetCommunity: AmityCommunity? = null,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCreateLivestreamPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

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

    var showEditThumbnailSheet by remember { mutableStateOf(false) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showEndLivestreamDialog by remember { mutableStateOf(false) }
    var showLivestreamLimitSnackBar by remember { mutableStateOf(false) }
    var showCountdownEndingLivestream by remember { mutableStateOf(false) }
    var showCannotStartLivestreamDialog by remember { mutableStateOf(false) }
    var showThumbnailErrorUploadDialog by remember {
        mutableStateOf(
            Pair(
                first = false,
                second = 0
            )
        )
    }
    var isTerminated by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val haptics = LocalHapticFeedback.current

    val behavior = remember {
        AmitySocialBehaviorHelper.createLivestreamPageBehavior
    }

    var streamBroadcaster: StreamBroadcaster? by remember { mutableStateOf(null) }
    val broadcasterConfig = AmityStreamBroadcasterConfiguration.Builder()
        .setOrientation(Configuration.ORIENTATION_PORTRAIT)
        .setResolution(AmityBroadcastResolution.HD_720P)
        .build()

    var durationDisposable: Disposable? by remember { mutableStateOf(null) }
    var duration by remember { mutableIntStateOf(0) }
    var liveHour by remember { mutableIntStateOf(0) }
    var liveMin by remember { mutableIntStateOf(0) }
    var liveSecond by remember { mutableIntStateOf(0) }

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
        isCameraAndRecAudioPermissionGranted && !uiState.liveTitle.isNullOrBlank() && uiState.thumbnailUploadUiState !is LivestreamThumbnailUploadUiState.Uploading

    LaunchedEffect(Unit) {
        if (targetType == AmityPost.TargetType.USER) {
            viewModel.setupScreen()
        } else {
            targetCommunity?.let {
                viewModel.setupScreen(it.getCommunityId())
            }
        }
    }

    LaunchedEffect(uiState.broadcasterState) {
        when (uiState.broadcasterState) {
            is AmityStreamBroadcasterState.CONNECTED -> {
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

            is AmityStreamBroadcasterState.DISCONNECTED -> {
                uiState.streamModeration?.getTerminateLabels()?.let {
                    if (it.isNotEmpty() && !isTerminated) {
                        isTerminated = true
                        if (durationDisposable?.isDisposed == false) {
                            streamBroadcaster?.stopPreview()
                            streamBroadcaster?.stopPublish()
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
            streamBroadcaster?.let {
                endLivestream(
                    context = context,
                    streamBroadcaster = it,
                    durationDisposable = durationDisposable,
                    behavior = behavior,
                    uiState = uiState,
                    errorType = LivestreamErrorScreenType.INTERNET
                )
            }
        }
    }

    LaunchedEffect(uiState.amityPost?.isDeleted()) {
        if (uiState.amityPost?.isDeleted() == true) {
            if (durationDisposable?.isDisposed == false) {
                streamBroadcaster?.stopPreview()
                streamBroadcaster?.stopPublish()
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
        },
        onDestroy = {
            streamBroadcaster?.stopPreview()
            if (durationDisposable?.isDisposed == false) {
                duration = 0
                streamBroadcaster?.stopPublish()
                durationDisposable?.dispose()
            }
            uiState.amityPost?.let {
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

    AmityBasePage(pageId = "create_livestream_page") {
        Column(
            modifier = modifier
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
                    AndroidView(
                        factory = { context ->
                            val amityCamera = AmityCameraView(context)
                            streamBroadcaster = AmityStreamBroadcaster.Builder(amityCamera)
                                .setConfiguration(broadcasterConfig)
                                .build()
                            streamBroadcaster?.let {
                                it.startPreview()
                                viewModel.subscribeBroadcaster(it)
                            }
                            amityCamera
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (uiState.broadcasterState is AmityStreamBroadcasterState.CONNECTED) {
                                Color.Transparent
                            } else {
                                Color.Black.copy(
                                    alpha = 0.5f
                                )
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState.broadcasterState) {
                        is AmityStreamBroadcasterState.IDLE -> {
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
                                        painter = painterResource(getConfig().getIcon()),
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
                                    modifier = modifier
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
                                    AmityLivestreamTextField(
                                        value = uiState.liveTitle ?: "",
                                        onValueChange = {
                                            viewModel.setTitleText(it)
                                        },
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        placeHolder = stringResource(R.string.amity_v4_create_livestream_title_placeholder),
                                        singleLine = true,
                                        maxCharLength = 30,
                                        textStyle = AmityTheme.typography.headLine.copy(
                                            color = Color.White,
                                            textAlign = TextAlign.Start,
                                        ),
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    AmityLivestreamTextField(
                                        value = uiState.liveDesc ?: "",
                                        onValueChange = {
                                            viewModel.setDescText(it)
                                        },
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .imePadding(),
                                        placeHolder = stringResource(R.string.amity_v4_create_livestream_desc_placeholder),
                                        singleLine = false,
                                        textStyle = AmityTheme.typography.body.copy(
                                            color = Color.White,
                                            textAlign = TextAlign.Start,
                                        ),
                                    )
                                }
                            } else {
                                AmityCreateLivestreamNoPermissionView(
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
                                                uiState.liveTitle?.let {
                                                    viewModel.createLiveStreamingPost(
                                                        title = it,
                                                        description = uiState.liveDesc ?: " ",
                                                        onCreateCompleted = { streamId ->
                                                            streamId?.let {
                                                                streamBroadcaster?.startPublish(it)
                                                            }
                                                        },
                                                        onCreateFailed = {
                                                            showCannotStartLivestreamDialog = true
                                                        },
                                                    )
                                                }
                                            }
                                        }
                                        .testTag(getAccessibilityId()),
                                    alpha = if (startStreamButtonEnable) 1f else 0.3f
                                )
                            }
                        }

                        is AmityStreamBroadcasterState.CONNECTING -> {
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

                        is AmityStreamBroadcasterState.CONNECTED,
                        is AmityStreamBroadcasterState.DISCONNECTED,
                            -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (showCountdownEndingLivestream) {
                                    AmityCircularProgressWithCountDownTimer(
                                        totalTime = LIVESTREAM_COUNTDOWN_DURATION,
                                        onTimeUp = {
                                            streamBroadcaster?.let {
                                                endLivestream(
                                                    context = context,
                                                    streamBroadcaster = it,
                                                    durationDisposable = durationDisposable,
                                                    behavior = behavior,
                                                    uiState = uiState,
                                                    showLivestreamPostExceeded = true
                                                )
                                            }
                                            showCountdownEndingLivestream = false
                                        }
                                    )
                                }

                                if (uiState.broadcasterState is AmityStreamBroadcasterState.DISCONNECTED) {
                                    AmityCreateLivestreamNoInternetView()
                                }

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "live_timer_status"
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .background(
                                                    color = getConfig().getBackgroundColor(),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = formatTime(liveHour, liveMin, liveSecond),
                                                color = Color.White,
                                                style = AmityTheme.typography.captionBold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

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
                    when (uiState.broadcasterState) {
                        is AmityStreamBroadcasterState.IDLE -> {
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

                        is AmityStreamBroadcasterState.CONNECTING -> {}

                        is AmityStreamBroadcasterState.CONNECTED,
                        is AmityStreamBroadcasterState.DISCONNECTED,
                            -> {
                            AmityBaseElement(
                                pageScope = getPageScope(),
                                componentScope = getComponentScope(),
                                elementId = "end_live_stream_button"
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .clip(RoundedCornerShape(size = 8.dp))
                                        .border(
                                            width = 1.dp,
                                            color = AmityTheme.colors.baseShade4,
                                            shape = RoundedCornerShape(size = 8.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .clickableWithoutRipple {
                                            showEndLivestreamDialog = true
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        ),
                                        text = getConfig().getText(),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        componentScope = getComponentScope(),
                        elementId = "switch_camera_button"
                    ) {
                        Image(
                            painter = painterResource(getConfig().getIcon()),
                            contentDescription = "switch camera button",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .testTag("switch_camera_button")
                                .clickableWithoutRipple {
                                    if (isCameraAndRecAudioPermissionGranted) {
                                        streamBroadcaster?.switchCamera()
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                },
                            alpha = if (isCameraAndRecAudioPermissionGranted) 1f else 0.5f
                        )
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
                    streamBroadcaster?.let {
                        endLivestream(
                            context = context,
                            streamBroadcaster = it,
                            durationDisposable = durationDisposable,
                            behavior = behavior,
                            uiState = uiState,
                        )
                    }
                },
                onDismissRequest = {
                    showEndLivestreamDialog = false
                }
            )
        }

        if (showEditThumbnailSheet) {
            AmityEditThumbnailSheet(
                modifier = modifier,
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

        if (showLivestreamLimitSnackBar) {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                message = stringResource(R.string.amity_v4_create_livestream_duration_exceed_snackbar),
                offsetFromBottom = 130
            )
            showLivestreamLimitSnackBar = false
        }
    }
}

private fun endLivestream(
    context: Context,
    streamBroadcaster: StreamBroadcaster,
    durationDisposable: Disposable?,
    behavior: AmityCreateLivestreamPageBehavior,
    uiState: AmityCreateLivestreamPageUiState,
    showLivestreamPostExceeded: Boolean = false,
    errorType: LivestreamErrorScreenType = LivestreamErrorScreenType.NONE,
) {
    if (durationDisposable?.isDisposed == false) {
        streamBroadcaster.stopPreview()
        if (errorType == LivestreamErrorScreenType.INTERNET) {
            streamBroadcaster.stopPublishWithNoInternet()
        } else {
            streamBroadcaster.stopPublish()
        }
        durationDisposable.dispose()
    }
    behavior.goToPostDetailPage(
        context = context,
        id = uiState.createPostId ?: "",
        category = AmityPostCategory.GENERAL,
        showLivestreamPostExceeded = showLivestreamPostExceeded
    )
    context.closePageWithResult(Activity.RESULT_OK)
}

fun formatTime(hour: Int, minute: Int, second: Int): String {
    return when {
        hour > 0 -> "LIVE $hour:%02d:%02d".format(minute, second)
        minute >= 10 -> "LIVE %d:%02d".format(minute, second)
        else -> "LIVE %d:%02d".format(minute, second)
    }
}