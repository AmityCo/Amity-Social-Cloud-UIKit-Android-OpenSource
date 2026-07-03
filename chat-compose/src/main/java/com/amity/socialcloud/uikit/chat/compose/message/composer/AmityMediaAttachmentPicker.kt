package com.amity.socialcloud.uikit.chat.compose.message.composer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import java.io.File

/**
 * Public thin wrapper for the media attachment picker.
 * Component ID: media_attachment_picker
 *
 * Exposes camera capture and gallery selection entry points for chat message attachments.
 *
 * @param modifier        Optional modifier.
 * @param onSelectMedia   Called with the URI of the selected or captured media.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityMediaAttachmentPicker(
    modifier: Modifier = Modifier,
    onSelectMedia: (Uri) -> Unit,
) {
    val context = LocalContext.current
    var showCameraChooser by remember { mutableStateOf(false) }
    val cameraChooserSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var cameraPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) cameraPhotoUri?.let(onSelectMedia)
    }

    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
    ) { success ->
        if (success) cameraVideoUri?.let(onSelectMedia)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) showCameraChooser = true
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        uri?.let(onSelectMedia)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Camera button
        MediaAttachmentButton(
            iconResId = R.drawable.amity_ic_chat_camera_button,
            label = amityChatString("chat.media.camera"),
            onClick = {
                val hasCameraPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                if (hasCameraPermission) {
                    showCameraChooser = true
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
        )

        Spacer(modifier = Modifier.width(72.dp))

        // Gallery button
        MediaAttachmentButton(
            iconResId = R.drawable.amity_ic_chat_image_button,
            label = amityChatString("chat.media.photo"),
            onClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageAndVideo,
                    )
                )
            },
        )
    }

    // Camera type chooser bottom sheet
    if (showCameraChooser) {
        ModalBottomSheet(
            onDismissRequest = { showCameraChooser = false },
            sheetState = cameraChooserSheetState,
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    text = amityChatString("chat.reply.photo.label"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showCameraChooser = false
                            val photoFile = File.createTempFile("img_", ".jpg", context.cacheDir)
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                photoFile,
                            )
                            cameraPhotoUri = uri
                            cameraPhotoLauncher.launch(uri)
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    style = AmityTheme.typography.bodyLegacy,
                )
                Text(
                    text = amityChatString("chat.reply.video.label"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showCameraChooser = false
                            val videoFile = File.createTempFile("vid_", ".mp4", context.cacheDir)
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                videoFile,
                            )
                            cameraVideoUri = uri
                            cameraVideoLauncher.launch(uri)
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    style = AmityTheme.typography.bodyLegacy,
                )
            }
        }
    }
}

@Composable
private fun MediaAttachmentButton(
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = label,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade1,
            ),
        )
    }
}
