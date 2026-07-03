package com.amity.socialcloud.uikit.chat.compose.setting

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.AsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

private const val GROUP_NAME_MAX_LENGTH = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityEditGroupProfilePage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityEditGroupProfilePageViewModel(channelId) }
    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val context = LocalContext.current

    var displayName by remember { mutableStateOf("") }
    var isInitialized by remember { mutableStateOf(false) }
    var showImagePickerSheet by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showInappropriateImageDialog by remember { mutableStateOf(false) }
    val avatarUploadState by viewModel.avatarUploadState.collectAsState()

    LaunchedEffect(channel) {
        if (!isInitialized && channel != null) {
            displayName = channel?.getDisplayName() ?: ""
            isInitialized = true
        }
    }

    val hasNameChanged = isInitialized && displayName != (channel?.getDisplayName() ?: "")
    val hasAvatarUploaded = avatarUploadState is AmityEditGroupProfilePageViewModel.AvatarUploadState.Success
    val hasChanged = hasNameChanged || hasAvatarUploaded
    val isValid = displayName.isNotBlank() && displayName.length <= GROUP_NAME_MAX_LENGTH

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                viewModel.uploadAvatar(it) {
                    showInappropriateImageDialog = true
                }
            }
        }

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && cameraUri != null) {
                viewModel.uploadAvatar(cameraUri!!) {
                    showInappropriateImageDialog = true
                }
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all { it.value }) {
                val imageFile = AmityCameraUtil.createImageFile(context)
                if (imageFile != null) {
                    cameraUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                    imageCaptureLauncher.launch(cameraUri)
                }
            }
        }
    val editProfileSuccessMessage = amityChatString("chat.group.edit.profile")
    val editProfileFailedMessage = amityChatString("chat.group.edit.profile.failed")
    AmityBasePage(pageId = "edit_group_profile_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header with Save button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            (context as? Activity)?.finish()
                        },
                    tint = AmityTheme.colors.base,
                )

                Text(
                    text = amityChatString("chat.group.profile"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                        color = AmityTheme.colors.primary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = amityChatString("chat.group.edit.profile.save"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (hasChanged && isValid) AmityTheme.colors.primary
                            else AmityTheme.colors.primary.copy(alpha = 0.4f),
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickableWithoutRipple(enabled = hasChanged && isValid) {
                                isSaving = true
                                viewModel.updateProfile(
                                    displayName = displayName.trim(),
                                    onSuccess = {
                                        isSaving = false
                                        AmityUIKitSnackbar.publishSnackbarMessage(editProfileSuccessMessage)
                                        (context as? Activity)?.finish()
                                    },
                                    onError = {
                                        isSaving = false
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                            editProfileFailedMessage
                                        )
                                    },
                                )
                            },
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Avatar with camera overlay
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickableWithoutRipple {
                            showImagePickerSheet = true
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    val showUploadedUri = when (avatarUploadState) {
                        is AmityEditGroupProfilePageViewModel.AvatarUploadState.Uploading ->
                            (avatarUploadState as AmityEditGroupProfilePageViewModel.AvatarUploadState.Uploading).uri
                        is AmityEditGroupProfilePageViewModel.AvatarUploadState.Success ->
                            (avatarUploadState as AmityEditGroupProfilePageViewModel.AvatarUploadState.Success).uri
                        else -> null
                    }

                    if (showUploadedUri != null) {
                        AsyncImage(
                            model = showUploadedUri,
                            contentDescription = "Group avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(channel?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()
                        )
                        val painterState by painter.state.collectAsState()
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painter,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Group Avatar",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(24.dp))
                            )
                            if (painterState !is AsyncImagePainter.State.Success) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(AmityTheme.colors.primaryShade3),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                    )
                                }
                            }
                        }
                    }
                    // Dark overlay with camera icon or progress
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(amityColorBlack.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (avatarUploadState is AmityEditGroupProfilePageViewModel.AvatarUploadState.Uploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = amityColorWhite,
                                strokeWidth = 3.dp,
                            )
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_camera,
                                ),
                                contentDescription = "Change photo",
                                tint = amityColorWhite,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Group Name label + char counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Text(
                            text = amityChatString("chat.group.name.label"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Text(
                            text = " " + amityChatString("chat.group.name.required"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 13.sp,
                                color = AmityTheme.colors.baseShade3,
                            ),
                        )
                    }
                    Text(
                        text = "${displayName.length}/$GROUP_NAME_MAX_LENGTH",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = AmityTheme.colors.baseShade2,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Group name text field with underline
                BasicTextField(
                    value = displayName,
                    onValueChange = {
                        displayName = it.take(GROUP_NAME_MAX_LENGTH)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 16.sp,
                        color = AmityTheme.colors.base,
                    ),
                    cursorBrush = SolidColor(AmityTheme.colors.primary),
                    singleLine = false,
                    decorationBox = { innerTextField ->
                        Column {
                            Box(modifier = Modifier.padding(vertical = 8.dp)) {
                                if (displayName.isEmpty()) {
                                    Text(
                                        text = amityChatString("chat.edit.group.profile.name.placeholder"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontSize = 16.sp,
                                            color = AmityTheme.colors.baseShade3,
                                        ),
                                    )
                                }
                                innerTextField()
                            }
                            HorizontalDivider(color = AmityTheme.colors.baseShade4)
                        }
                    },
                )
            }
        }

        // Image picker bottom sheet
        if (showImagePickerSheet) {
            ModalBottomSheet(
                onDismissRequest = { showImagePickerSheet = false },
                sheetState = sheetState,
                containerColor = AmityTheme.colors.background,
                contentWindowInsets = { WindowInsets.waterfall },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .navigationBarsPadding(),
                ) {
                    AmityBottomSheetActionItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        color = AmityTheme.colors.baseShade4,
                                    )
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_post_attachment_camera,
                                    ),
                                    contentDescription = null,
                                    tint = AmityTheme.colors.base,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center),
                                )
                            }
                        },
                        text = amityChatString("chat.media.camera"),
                    ) {
                        showImagePickerSheet = false
                        cameraPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                    }

                    AmityBottomSheetActionItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        color = AmityTheme.colors.baseShade4,
                                    )
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_post_attachment_photo,
                                    ),
                                    contentDescription = null,
                                    tint = AmityTheme.colors.base,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center),
                                )
                            }
                        },
                        text = amityChatString("chat.media.photo"),
                    ) {
                        showImagePickerSheet = false
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }

                    Box(modifier = Modifier.height(16.dp))
                }
            }
        }


        if (showInappropriateImageDialog) {
            AmityAlertDialog(
                dialogTitle = amityCommonString("amity_common_button_inappropriate_image"),
                dialogText = amityCommonString("amity_common_label_choose_different_image"),
                dismissText = amityChatString("chat.button.ok"),
                onDismissRequest = {
                    showInappropriateImageDialog = false
                }
            )
        }
    }
}
