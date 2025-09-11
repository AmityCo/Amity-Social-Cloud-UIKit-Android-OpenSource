package com.amity.socialcloud.uikit.community.compose.clip.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.readableMinuteSeconds
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.create.element.AmityClipCameraShutterButtonElement
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityMediaAndCameraNoPermissionView
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraPreviewElement
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryCameraHelper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private const val FILE_SIZE_LIMIT = 2L * 1024 * 1024 * 1024 // 2 GB

@Composable
fun AmityCreateClipPage(
    targetId: String? = null,
    targetType: AmityPostTargetType? = null,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptics = LocalHapticFeedback.current

    val behavior = remember {
        AmitySocialBehaviorHelper.createClipPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    var isBackCameraSelected by remember { mutableStateOf(true) }
    var isFlashLightOn by remember { mutableStateOf(false) }
    var isCurrentlyRecording by remember { mutableStateOf(false) }
    var videoRecordDuration by remember { mutableIntStateOf(0) }
    var isShowClipTooShortDialog by remember { mutableStateOf(false) }
    val isImageCaptureMode by remember { mutableStateOf(false) }
    var showUploadFileSizeError by remember { mutableStateOf<Pair<String, String>?>(null) }


    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCreateClipPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val maxRecordDurationSeconds by viewModel.maxRecordDurationSeconds.collectAsState()

    val cameraAndAudioPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    var isCameraPermissionGranted by remember {
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
            isCameraPermissionGranted = permissions.entries.all { it.value }
        }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                context.grantUriPermission(
                    context.packageName,
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Get file size using ContentResolver instead of toFile()
                val fileSize =
                    context.contentResolver.openFileDescriptor(it, "r")?.use { descriptor ->
                        descriptor.statSize
                    } ?: 0L

                val isFileSizeExceed = fileSize > FILE_SIZE_LIMIT

                if (isFileSizeExceed) {
                    showUploadFileSizeError = Pair(
                        "Maximum file size limit reached",
                        "Please choose a video with smaller file size."
                    )
                } else if (checkVideoIsExceedLength(context, it, 15)) {
                    showUploadFileSizeError = Pair(
                        "Clip must be under 15 minutes",
                        "Please choose a different video to upload."
                    )
                } else {
                    behavior.goToDraftClipPage(
                        context = context,
                        launcher = launcher,
                        targetId = targetId ?: "",
                        targetType = targetType ?: AmityPostTargetType.COMMUNITY,
                        mediaType = it
                    )
                }
            }
        }

    val mediaPickerPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mediaPickerLauncher.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
                )
            )
        } else {
            behavior.goToNoPermissionPage(
                context = context,
            )
        }
    }

    DisposableEffectWithLifeCycle(
        onResume = {
            if (!isCameraPermissionGranted) {
                cameraPermissionLauncher.launch(cameraAndAudioPermissions)
            }
        },
        onDestroy = {

        }
    )

    LaunchedEffect(
        isCameraPermissionGranted,
        isBackCameraSelected,
        isFlashLightOn,
        isImageCaptureMode
    ) {
        if (isCameraPermissionGranted) {
            AmityStoryCameraHelper.startCamera(
                context = context,
                lifecycleOwner = lifecycleOwner,
                isBackCameraSelected = isBackCameraSelected,
                isImageCaptureMode = isImageCaptureMode,
                isFlashLightOn = isFlashLightOn,
            )
        }
    }

    LaunchedEffect(isCurrentlyRecording) {
        if (isCurrentlyRecording) {
            while (true) {
                delay(1.seconds)
                videoRecordDuration += 1
            }
        }
    }

    LaunchedEffect(videoRecordDuration) {
        if (isCurrentlyRecording && videoRecordDuration >= 900) { // 15 minutes = 900 seconds
            isCurrentlyRecording = false
            AmityStoryCameraHelper.stopCaptureVideo()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(9f / 16f)
                .fillMaxSize()
        ) {
            // Camera preview as the background
            AmityStoryCameraPreviewElement(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .testTag("camera_view")
            )

            // Top bar (close, flash)
            if (!isCurrentlyRecording) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .zIndex(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Uncomment and use if needed
                    // AmityMenuButton for close button here

                    AmityMenuButton(
                        icon = R.drawable.amity_ic_close,
                        size = 12.dp,
                        modifier = Modifier
                            .size(32.dp),
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            context.closePage()
                        }
                    )

                    Spacer(modifier = Modifier.width(32.dp)) // Placeholder for close button

                    if (isBackCameraSelected && isCameraPermissionGranted) {
                        AmityMenuButton(
                            icon = if (isFlashLightOn) R.drawable.amity_ic_story_flash else R.drawable.amity_ic_story_flash_off,
                            size = if (isFlashLightOn) 19.dp else 24.dp,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("flash_light_button"),
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                isFlashLightOn = !isFlashLightOn
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                }
            } else {
                // Timer bar at top center
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .size(60.dp, 26.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFF305A))
                ) {
                    Text(
                        text = videoRecordDuration.readableMinuteSeconds(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        style = AmityTheme.typography.bodyBold,
                    )
                }
            }

            AmityClipCameraShutterButtonElement(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .size(72.dp)
                    .testTag("camera_shutter_button"),
                isImageCaptureMode = isImageCaptureMode,
                isEnable = isCameraPermissionGranted,
                maxRecordDurationSeconds = 900, // 15 minutes
                onVideoCaptureStarted = {
                    isCurrentlyRecording = true
                    AmityStoryCameraHelper.captureVideo(
                        context = context,
                        onVideoSaved = {
                            if (videoRecordDuration < 1) {
                                isShowClipTooShortDialog = true
                            } else {
                                // Navigate to draft clip page with the recorded video
                                behavior.goToDraftClipPage(
                                    context = context,
                                    launcher = launcher,
                                    targetId = targetId ?: "",
                                    targetType = targetType ?: AmityPostTargetType.COMMUNITY,
                                    mediaType = it.toUri()
                                )
                            }
                            videoRecordDuration = 0
                        }
                    )
                },
                onVideoCaptureStopped = {
                    isCurrentlyRecording = false
                    AmityStoryCameraHelper.stopCaptureVideo()
                }
            )

            if (!isCameraPermissionGranted) {
                // Show permission required message
                AmityMediaAndCameraNoPermissionView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    title = "Allow access to your\ncamera and microphone",
                    description = "This lets you record and live stream\n" +
                            "from this device.",
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
        }
        // Bottom bar (media, shutter, switch camera)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentlyRecording) {
                AmityMenuButton(
                    icon = R.drawable.amity_ic_story_media,
                    size = 24.dp,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("media_picker_button"),
                ) {
                    if (isCameraPermissionGranted) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        // On Android 13+ (Tiramisu), no permissions needed for photo picker
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
                                )
                            )
                        } else {
                            // For older versions, check storage permission first
                            if (isMediaPickerPermissionGranted) {
                                mediaPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
                                    )
                                )
                            } else {
                                mediaPickerPermissionLauncher.launch(mediaPickerPermissions)
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f)) // Placeholder for media button
            }

            AmityMenuButton(
                icon = R.drawable.amity_ic_story_switch_camera,
                size = 20.dp,
                modifier = Modifier
                    .size(40.dp)
                    .alpha(if (isCameraPermissionGranted) 1f else 0.3f)
                    .testTag("switch_camera_button"),
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                isBackCameraSelected = !isBackCameraSelected
            }
        }


        if (isShowClipTooShortDialog) {
            // Show dialog for clip too short
            AmityAlertDialog(
                onDismissRequest = {
                    isShowClipTooShortDialog = false
                },
                dialogTitle = "Clip too short",
                dialogText = "Clip must be at least 1 second long.",
                dismissText = "OK",
            )
        }

        if (showUploadFileSizeError != null) {
            // Show dialog for file size limit exceeded
            AmityAlertDialog(
                onDismissRequest = {
                    showUploadFileSizeError = null
                },
                dialogTitle = showUploadFileSizeError?.first ?: "",
                dialogText = showUploadFileSizeError?.second ?: "",
                dismissText = "OK",
            )
        }
    }
}

private fun checkVideoIsExceedLength(context: Context, uri: Uri, length: Int): Boolean {
    val mediaRetriever = MediaMetadataRetriever()
    try {
        mediaRetriever.setDataSource(context, uri)
        val durationMs =
            mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                ?: 0
        val durationMinutes = durationMs / (1000 * 60)

        return durationMinutes > length
    } catch (e: Exception) {
        return false
    } finally {
        mediaRetriever.release()
    }
}

@Composable
@Preview(heightDp = 800, widthDp = 400)
fun AmityCreateClipPagePreview() {
    AmityCreateClipPage()
}