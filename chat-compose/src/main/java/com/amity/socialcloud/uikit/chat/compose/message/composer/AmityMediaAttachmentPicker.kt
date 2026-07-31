package com.amity.socialcloud.uikit.chat.compose.message.composer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
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

    // Picker slot per v2: sheet surface, 106 high, 40 px Filled/Secondary actions
    // 56 apart, inset 8 top
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(106.dp)
            .background(AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral))
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(56.dp, Alignment.CenterHorizontally),
    ) {
        // Camera button
        MediaAttachmentButton(
            iconResId = CommonR.drawable.amity_ic_camera_r,
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

        // Media/Gallery button
        MediaAttachmentButton(
            iconResId = CommonR.drawable.amity_ic_image_r,
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
        AmitySheet(
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
    // Avatar-with-label action per v2: 40 px Filled/Secondary icon button + 13/18 label (gap 4)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE40,
            icon = iconResId,
            onClick = onClick,
        )
        Text(
            text = label,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = AmityTheme.token(AmityColorToken.TextIconButtonLabelGeneral),
            ),
        )
    }
}
