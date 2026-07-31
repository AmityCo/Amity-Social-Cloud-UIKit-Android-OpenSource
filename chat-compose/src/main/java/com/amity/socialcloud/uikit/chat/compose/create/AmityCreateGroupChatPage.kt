package com.amity.socialcloud.uikit.chat.compose.create

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadge
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeFamily
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgePreset
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeShape
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelection
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelectionVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageActivity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatSheetActionItem
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AmityCreateGroupChatPage(
    modifier: Modifier = Modifier,
    selectedUsers: List<AmityUser>,
    onBack: (backToHome: Boolean) -> Unit = {},
    onAddMember: () -> Unit = {},
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel = viewModel<AmityCreateGroupChatPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val creationState by viewModel.creationState.collectAsState()

    val currentUser by produceState<AmityUser?>(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            try { AmityCoreClient.getCurrentUser().blockingFirst() } catch (e: Exception) { null }
        }
    }

    var groupName by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var currentMembers by remember { mutableStateOf(selectedUsers) }
    val scrollState = rememberScrollState()
    val avatarUploadState by viewModel.avatarUploadState.collectAsState()
    var showImagePickerSheet by remember { mutableStateOf(false) }
    var showInappropriateImageDialog by remember { mutableStateOf(false) }

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

    val isLoading = creationState is AmityCreateGroupChatPageViewModel.CreationState.Loading
    val createGroupErrorMessage = amityChatString("chat.create.group.error")
    val createGroupSuccessMessage = amityChatString("chat.create.group.success")
    val sheetState = rememberModalBottomSheetState()
    var showLeaveConfirmation by remember { mutableStateOf(false) }

    BackHandler {
        showLeaveConfirmation = true
    }

    AmityBasePage(pageId = "create_group_page", useAmityToast = true) {
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    iconSize = AmityIconButtonSize.SIZE32,
                    icon = CommonR.drawable.amity_ic_cross_r,
                    onClick = { showLeaveConfirmation = true },
                    modifier = Modifier.align(Alignment.CenterStart),
                )

                Text(
                    text = amityChatString("chat.create.group.title"),
                    style = AmityTheme.typography.titleLegacy.copy(
                        color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    ),
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                if (isLoading) {
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
                        label = amityChatString("chat.create.group.button"),
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            viewModel.createGroup(
                                displayName = groupName,
                                members = currentMembers,
                                isPublic = isPublic,
                                onSuccess = { channelId ->
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(createGroupSuccessMessage)
                                    context.startActivity(
                                        AmityGroupChatPageActivity.newIntent(context, channelId)
                                    )
                                    (context as? Activity)?.finish()
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(createGroupErrorMessage)
                                },
                            )
                        },
                    )
                }
            }

            AmityDivider(variant = AmityDividerVariant.Post)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(AmityTheme.token(AmityColorToken.SurfaceAvatarProfileDefault)),
                        contentAlignment = Alignment.Center,
                    ) {
                        val showUploadedUri = when (avatarUploadState) {
                            is AmityCreateGroupChatPageViewModel.AvatarUploadState.Uploading ->
                                (avatarUploadState as AmityCreateGroupChatPageViewModel.AvatarUploadState.Uploading).uri
                            is AmityCreateGroupChatPageViewModel.AvatarUploadState.Success ->
                                (avatarUploadState as AmityCreateGroupChatPageViewModel.AvatarUploadState.Success).uri
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
                            Icon(
                                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_comments_alt_s),
                                contentDescription = null,
                                tint = AmityTheme.token(AmityColorToken.IconAvatarDefault),
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack))
                            .clickableWithoutRipple {
                                showImagePickerSheet = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (avatarUploadState is AmityCreateGroupChatPageViewModel.AvatarUploadState.Uploading) {
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

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        color = AmityTheme.token(AmityColorToken.TextInputTextInputTitleDefault),
                                    )
                                ) {
                                    append(amityChatString("chat.group.name.label"))
                                }
                                append("  ")
                                withStyle(
                                    SpanStyle(
                                        fontSize = 13.sp,
                                        color = AmityTheme.token(AmityColorToken.TextInputTextInputIndicatorDefault),
                                    )
                                ) {
                                    append(amityChatString("chat.group.name.optional"))
                                }
                            },
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 17.sp,
                                lineHeight = 24.sp,
                            ),
                        )
                        Text(
                            text = "${groupName.length}/100",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 13.sp,
                                color = AmityTheme.token(AmityColorToken.TextInputTextInputTextCountDefault),
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Field keeps 16dp vertical padding so the placeholder/typed text isn't
                            // cramped against the label and underline above it.
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.TopStart,
                    ) {
                        BasicTextField(
                            value = groupName,
                            onValueChange = { newValue ->
                                groupName = newValue.take(100)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 15.sp,
                                color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabledFilled),
                            ),
                            cursorBrush = SolidColor(AmityTheme.token(AmityColorToken.TextInputTextInputTextCursorDefault)),
                            singleLine = false,
                            decorationBox = { innerTextField ->
                                if (groupName.isEmpty()) {
                                    Text(
                                        text = amityChatString("chat.group.name.placeholder"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontSize = 15.sp,
                                            color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabled),
                                        ),
                                    )
                                }
                                innerTextField()
                            },
                        )
                    }
                    HorizontalDivider(color = AmityTheme.token(AmityColorToken.LineInputTextInputUnderlinedDefault))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = amityChatString("chat.privacy.label"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                        ),
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isPublic = true }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AmityTheme.token(AmityColorToken.SurfaceFeaturedIconTinted)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = CommonR.drawable.amity_ic_earth_africa_s,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = AmityTheme.token(AmityColorToken.IconFeaturedIconTinted),
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = amityChatString("chat.create.group.public.title"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                                ),
                            )
                            Text(
                                text = amityChatString("chat.create.group.public.subtitle"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 13.sp,
                                    color = AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault),
                                ),
                            )
                        }
                        AmitySelection(
                            variant = AmitySelectionVariant.RADIO,
                            isSelected = isPublic,
                            onChange = { _, _ -> isPublic = true },
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isPublic = false }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AmityTheme.token(AmityColorToken.SurfaceFeaturedIconTinted)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    CommonR.drawable.amity_ic_lock_keyhole_r,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = AmityTheme.token(AmityColorToken.IconFeaturedIconTinted),
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = amityChatString("chat.create.group.private.title"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                                ),
                            )
                            Text(
                                text = amityChatString("chat.create.group.private.subtitle"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 13.sp,
                                    color = AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault),
                                ),
                            )
                        }
                        AmitySelection(
                            variant = AmitySelectionVariant.RADIO,
                            isSelected = !isPublic,
                            onChange = { _, _ -> isPublic = false },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AmityBanner(
                    hierarchy = AmityBannerHierarchy.SUBDUE,
                    description = amityChatString("chat.privacy.warning"),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = amityChatString("chat.group.members"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.token(AmityColorToken.TextInputUserInputTitleDefault),
                        ),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val memberItems = buildList {
                        add(null) // placeholder for AddMemberChip
                        currentUser?.let { add(it) }
                        addAll(currentMembers)
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        memberItems.chunked(4).forEach { rowItems ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(28.dp),
                            ) {
                                rowItems.forEachIndexed { index, item ->
                                    val globalIndex = memberItems.indexOf(item)
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (item == null) {
                                            AddMemberChip(onClick = onAddMember)
                                        } else {
                                            val isCurrentUser = currentUser != null && globalIndex == 1
                                            MemberChip(
                                                user = item,
                                                isCurrentUser = isCurrentUser,
                                                onRemove = if (isCurrentUser) null else {
                                                    { currentMembers = currentMembers.filter { u -> u.getUserId() != item.getUserId() } }
                                                },
                                            )
                                        }
                                    }
                                }
                                // Fill remaining slots in last row
                                repeat(4 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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
                            // Nested atom shares the row's onClick — its own click listener would
                            // otherwise shadow the row's tap.
                            AmityButton(
                                variant = AmityButtonVariant.ICON,
                                style = AmityButtonStyle.FILLED,
                                hierarchy = AmityButtonHierarchy.SECONDARY,
                                iconSize = AmityIconButtonSize.SIZE32,
                                icon = CommonR.drawable.amity_ic_camera_r,
                                onClick = {
                                    showImagePickerSheet = false
                                    cameraPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                                },
                            )
                        },
                        text = amityChatString("chat.media.camera"),
                    ) {
                        showImagePickerSheet = false
                        cameraPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                    }

                    AmityChatSheetActionItem(
                        icon = {
                            // Nested atom shares the row's onClick — its own click listener would
                            // otherwise shadow the row's tap.
                            AmityButton(
                                variant = AmityButtonVariant.ICON,
                                style = AmityButtonStyle.FILLED,
                                hierarchy = AmityButtonHierarchy.SECONDARY,
                                iconSize = AmityIconButtonSize.SIZE32,
                                icon = CommonR.drawable.amity_ic_image_r,
                                onClick = {
                                    showImagePickerSheet = false
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            )
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
        if (showLeaveConfirmation) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.leave.without.finishing.title"),
                message = amityChatString("chat.leave.without.finishing.message"),
                confirmLabel = amityChatString("chat.leave.without.finishing.label"),
                cancelLabel = amityChatString("chat.cancel"),
                onConfirm = {
                    showLeaveConfirmation = false
                    onBack(true)
                },
                onDismiss = { showLeaveConfirmation = false },
            )
        }

        if (showInappropriateImageDialog) {
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

/** First uppercase character of the trimmed display name, matching legacy avatar-initial derivation. */
private fun memberChipInitials(user: AmityUser): String {
    return user.getDisplayName().toChatAvatarInitial().orEmpty()
}

@Composable
private fun MemberChip(
    user: AmityUser,
    isCurrentUser: Boolean = false,
    onRemove: (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box {
            val avatarUrl = user.resolvedAvatarUrl()?.ifEmpty { null }
            AmityAvatar(
                variant = if (avatarUrl != null) AmityAvatarVariant.Image else AmityAvatarVariant.Text,
                imageUrl = avatarUrl,
                initials = memberChipInitials(user),
                size = AmityAvatarSize.Size40,
                borderWidth = 2,
            )
            if (isCurrentUser) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    AmityBadge(
                        variant = AmityBadgeVariant.ICON,
                        icon = CommonR.drawable.amity_ic_shield_check_s,
                        shape = AmityBadgeShape.ROUND,
                        size = AmityBadgeSize.SIZE_16,
                        preset = AmityBadgePreset(
                            family = AmityBadgeFamily.USER_STATUS,
                            case = "Moderator",
                        ),
                    )
                }
            }
            if (onRemove != null) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.TRANSPARENT,
                    hierarchy = AmityButtonHierarchy.PRIMARY,
                    iconSize = AmityIconButtonSize.SIZE16,
                    icon = CommonR.drawable.amity_ic_cross_r,
                    contentDescription = "Remove member",
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isCurrentUser) amityChatString("chat.member.you")
            else user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = AmityTheme.token(AmityColorToken.TextAvatarLabelDefault),
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AddMemberChip(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickableWithoutRipple(onClick = onClick),
    ) {
        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE40,
            icon = CommonR.drawable.amity_ic_plus_r,
            onClick = onClick,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amityChatString("chat.add.member.chip"),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = AmityTheme.token(AmityColorToken.TextIconButtonLabelGeneral),
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
