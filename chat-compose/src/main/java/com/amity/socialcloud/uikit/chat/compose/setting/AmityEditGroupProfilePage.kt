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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant

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
    AmityBasePage(pageId = "edit_group_profile_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
        ) {
            // Header with Save button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    iconSize = AmityIconButtonSize.SIZE24,
                    icon = CommonR.drawable.amity_ic_chevron_left,
                    onClick = { (context as? Activity)?.finish() },
                    modifier = Modifier.align(Alignment.CenterStart),
                )

                Text(
                    text = amityChatString("chat.group.profile"),
                    style = AmityTheme.typography.titleLegacy,
                    color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                if (isSaving) {
                    AmityLoader(
                        variant = AmityLoaderVariant.Spinner,
                        size = AmityLoaderSize.Sm,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd),
                    )
                } else {
                    AmityButton(
                        variant = AmityButtonVariant.MAIN,
                        style = AmityButtonStyle.GHOST,
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        mainSize = AmityMainButtonSize.SM,
                        label = amityChatString("chat.group.edit.profile.save"),
                        enabled = hasChanged && isValid,
                        onClick = {
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
                        modifier = Modifier.align(Alignment.CenterEnd),
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
                        // Show the channel's existing avatar via the AmityAvatar atom — mirrors
                        // AmityGroupSettingPage (identical channel source + URL) which renders it
                        // correctly. The prior hand-rolled Coil painter always fell through to the
                        // default even when the channel HAD an avatar (this bug); the atom loads the
                        // image and only falls back to the default (icon on Surface/Avatar/Profile/
                        // Default) when there is genuinely no avatar.
                        AmityAvatar(
                            variant = AmityAvatarVariant.Image,
                            imageUrl = channel?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM),
                            style = AmityAvatarStyle.Squared,
                            size = AmityAvatarSize.Size120,
                            icon = CommonR.drawable.amity_ic_comments_alt_s,
                        )
                    }
                    // Dark overlay with camera icon or progress
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            // Semantic media-overlay scrim (50% black).
                            .background(AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (avatarUploadState is AmityEditGroupProfilePageViewModel.AvatarUploadState.Uploading) {
                            AmityLoader(
                                variant = AmityLoaderVariant.UploadSpinner,
                                size = AmityLoaderSize.Lg,
                                modifier = Modifier.size(40.dp),
                            )
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = CommonR.drawable.amity_ic_camera_r,
                                ),
                                contentDescription = "Change photo",
                                tint = AmityTheme.token(AmityColorToken.IconAvatarDefault),
                                modifier = Modifier.size(64.dp),
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
                                fontSize = 17.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AmityTheme.token(AmityColorToken.TextInputTextInputTitleDefault),
                            ),
                        )
                        Text(
                            text = " " + amityChatString("chat.group.name.required"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 13.sp,
                                color = AmityTheme.token(AmityColorToken.TextInputTextInputIndicatorDefault),
                            ),
                        )
                    }
                    Text(
                        text = "${displayName.length}/$GROUP_NAME_MAX_LENGTH",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = AmityTheme.token(AmityColorToken.TextInputTextInputTextCountDefault),
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
                        color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabledFilled),
                    ),
                    cursorBrush = SolidColor(AmityTheme.token(AmityColorToken.TextInputTextInputTextCursorDefault)),
                    singleLine = false,
                    decorationBox = { innerTextField ->
                        Column {
                            Box(modifier = Modifier.padding(vertical = 8.dp)) {
                                if (displayName.isEmpty()) {
                                    Text(
                                        text = amityChatString("chat.edit.group.profile.name.placeholder"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontSize = 16.sp,
                                            color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabledFilled),
                                        ),
                                    )
                                }
                                innerTextField()
                            }
                            AmityDivider(variant = AmityDividerVariant.Content, inset = false)
                        }
                    },
                )
            }
        }

        // Image picker bottom sheet
        if (showImagePickerSheet) {
            AmitySheet(
                onDismissRequest = { showImagePickerSheet = false },
                sheetState = sheetState,
                contentWindowInsets = { WindowInsets.waterfall },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .navigationBarsPadding(),
                ) {
                    AmityChatSheetActionItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        color = AmityTheme.token(AmityColorToken.SurfaceIconButtonFilledSecondaryEnabled),
                                    )
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        id = CommonR.drawable.amity_ic_camera_r,
                                    ),
                                    contentDescription = null,
                                    tint = AmityTheme.token(AmityColorToken.IconIconButtonFilledSecondaryDefault),
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

                    AmityChatSheetActionItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        color = AmityTheme.token(AmityColorToken.SurfaceIconButtonFilledSecondaryEnabled),
                                    )
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        id = CommonR.drawable.amity_ic_image_r,
                                    ),
                                    contentDescription = null,
                                    tint = AmityTheme.token(AmityColorToken.IconIconButtonFilledSecondaryDefault),
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
            // Native single-button dialog via chat's shared confirm dialog.
            AmityChatConfirmDialog(
                title = amityCommonString("amity_common_button_inappropriate_image"),
                message = amityCommonString("amity_common_label_choose_different_image"),
                confirmLabel = amityChatString("chat.button.ok"),
                confirmColor = AmityTheme.token(AmityColorToken.TextBaseHighlight),
                cancelLabel = null,
                onConfirm = { showInappropriateImageDialog = false },
                onDismiss = { showInappropriateImageDialog = false },
            )
        }
    }
}
