package com.amity.socialcloud.uikit.community.compose.story.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.common.readableMinuteSeconds
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asDrawableRes
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraPreviewElement
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraShutterButtonElement
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryPhotoVideoSelectionElement
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityStoryMediaType
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryCameraHelper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun AmityCreateStoryPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityStory.TargetType,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptics = LocalHapticFeedback.current

    val behavior = remember {
        AmitySocialBehaviorHelper.createStoryPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    var isPhotoSelected by remember { mutableStateOf(true) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var isMediaPermissionGranted by remember { mutableStateOf(false) }
    var shouldShowPermissionRequiredMessage by remember { mutableStateOf(false) }

    var isBackCameraSelected by remember { mutableStateOf(true) }
    var isFlashLightOn by remember { mutableStateOf(false) }
    var isCurrentlyRecording by remember { mutableStateOf(false) }
    var videoRecordDuration by remember { mutableIntStateOf(0) }


    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCreateStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val maxRecordDurationSeconds by viewModel.maxRecordDurationSeconds.collectAsState()

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCameraPermissionGranted = permissions.entries.all { it.value }
            shouldShowPermissionRequiredMessage = !isCameraPermissionGranted
        }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                context.grantUriPermission(
                    context.packageName,
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                behavior.goToDraftStoryPage(
                    context = context,
                    launcher = launcher,
                    targetId = targetId,
                    targetType = targetType,
                    mediaType = if (isPhotoSelected) AmityStoryMediaType.Image(it)
                    else AmityStoryMediaType.Video(it),
                )
            }
        }

    val mediaPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isMediaPermissionGranted = isGranted
            if (isMediaPermissionGranted) {
                mediaPickerLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType =
                        if (isPhotoSelected) ActivityResultContracts.PickVisualMedia.ImageOnly
                        else ActivityResultContracts.PickVisualMedia.VideoOnly,
                    )
                )
            }
        }

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
            isCameraPermissionGranted = true
        } else {
            cameraPermissionLauncher.launch(permissions)
        }
    }

    LaunchedEffect(
        isCameraPermissionGranted,
        isBackCameraSelected,
        isPhotoSelected,
        isFlashLightOn,
    ) {
        if (!isCameraPermissionGranted) return@LaunchedEffect

        AmityStoryCameraHelper.startCamera(
            context = context,
            lifecycleOwner = lifecycleOwner,
            isBackCameraSelected = isBackCameraSelected,
            isImageCaptureMode = isPhotoSelected,
            isFlashLightOn = isFlashLightOn,
        )
    }

    LaunchedEffect(isCurrentlyRecording) {
        if (isCurrentlyRecording) {
            while (true) {
                delay(1.seconds)
                videoRecordDuration += 1
            }
        } else {
            videoRecordDuration = 0
        }
    }

    AmityBasePage(pageId = "camera_page") {
        LaunchedEffect(shouldShowPermissionRequiredMessage) {
            if (shouldShowPermissionRequiredMessage) {
                getPageScope().showSnackbar("Required permission is not granted.")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ConstraintLayout(
                modifier = Modifier.aspectRatio(9f / 16f)
            ) {
                val (cameraPreview, shutterBtn, closeBtn, flashBtn, selectMediaBtn, switchCameraBtn, durationTimer) = createRefs()

                AmityStoryCameraPreviewElement(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .testTag("camera_view")
                        .constrainAs(cameraPreview) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                )

                if (!isCurrentlyRecording) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_button"
                    ) {
                        AmityMenuButton(
                            icon = getConfig().getValue("close_icon").asDrawableRes(),
                            size = 12.dp,
                            modifier = Modifier
                                .size(32.dp)
                                .constrainAs(closeBtn) {
                                    top.linkTo(parent.top, 16.dp)
                                    start.linkTo(parent.start, 16.dp)
                                }
                                .testTag(getAccessibilityId()),
                            background = getConfig().getBackgroundColor(),
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                context.closePage()
                            }
                        )
                    }

                    if (isBackCameraSelected) {
                        AmityMenuButton(
                            icon = if (isFlashLightOn) R.drawable.amity_ic_story_flash else R.drawable.amity_ic_story_flash_off,
                            size = if (isFlashLightOn) 19.dp else 24.dp,
                            modifier = Modifier
                                .size(32.dp)
                                .constrainAs(flashBtn) {
                                    top.linkTo(parent.top, 16.dp)
                                    end.linkTo(parent.end, 16.dp)
                                }
                                .testTag("flash_light_button"),
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                isFlashLightOn = !isFlashLightOn
                            }
                        )
                    }
                    AmityMenuButton(
                        icon = R.drawable.amity_ic_story_media,
                        size = 24.dp,
                        modifier = Modifier
                            .size(40.dp)
                            .constrainAs(selectMediaBtn) {
                                start.linkTo(parent.start, 16.dp)
                                centerVerticallyTo(shutterBtn)
                            }
                            .testTag("media_picker_button"),
                    ) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        mediaPickerLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType =
                                if (isPhotoSelected) ActivityResultContracts.PickVisualMedia.ImageOnly
                                else ActivityResultContracts.PickVisualMedia.VideoOnly,
                            )
                        )
                    }
                    AmityMenuButton(
                        icon = R.drawable.amity_ic_story_switch_camera,
                        size = 20.dp,
                        modifier = Modifier
                            .size(40.dp)
                            .constrainAs(switchCameraBtn) {
                                end.linkTo(parent.end, 16.dp)
                                centerVerticallyTo(shutterBtn)
                            }
                            .testTag("switch_camera_button"),
                    ) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        isBackCameraSelected = !isBackCameraSelected
                    }
                } else {
                    Box(modifier = modifier
                        .size(60.dp, 26.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmityTheme.colors.alert)
                        .constrainAs(durationTimer) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        Text(
                            text = videoRecordDuration.readableMinuteSeconds(),
                            color = Color.White,
                            modifier = modifier.align(Alignment.Center)
                        )
                    }
                }

                AmityStoryCameraShutterButtonElement(
                    modifier = modifier
                        .size(72.dp)
                        .constrainAs(shutterBtn) {
                            bottom.linkTo(parent.bottom, 32.dp)
                            centerHorizontallyTo(parent)
                        }
                        .testTag("camera_shutter_button"),
                    isImageCaptureMode = isPhotoSelected,
                    maxRecordDurationSeconds = maxRecordDurationSeconds,
                    onImageCaptureClicked = {
                        AmityStoryCameraHelper.takePhoto(
                            context = context,
                            onImageSaved = {
                                behavior.goToDraftStoryPage(
                                    context = context,
                                    launcher = launcher,
                                    targetId = targetId,
                                    targetType = targetType,
                                    mediaType = AmityStoryMediaType.Image(Uri.parse(it))
                                )
                            }
                        )
                    },
                    onVideoCaptureStarted = {
                        isCurrentlyRecording = true
                        AmityStoryCameraHelper.captureVideo(
                            context = context,
                            onVideoSaved = {
                                behavior.goToDraftStoryPage(
                                    context = context,
                                    launcher = launcher,
                                    targetId = targetId,
                                    targetType = targetType,
                                    mediaType = AmityStoryMediaType.Video(Uri.parse(it))
                                )
                            }
                        )
                    },
                    onVideoCaptureStopped = {
                        isCurrentlyRecording = false
                        AmityStoryCameraHelper.stopCaptureVideo()
                    }
                )
            }

            Box(
                modifier = modifier.fillMaxSize()
            ) {
                AmityStoryPhotoVideoSelectionElement(
                    modifier = modifier
                        .width(260.dp)
                        .align(Alignment.Center)
                        .testTag("switch_mode"),
                    onSelectionChange = {
                        isPhotoSelected = it
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AmityCreateStoryPagePreview() {
    AmityCreateStoryPage(
        targetId = "",
        targetType = AmityStory.TargetType.COMMUNITY
    )
}