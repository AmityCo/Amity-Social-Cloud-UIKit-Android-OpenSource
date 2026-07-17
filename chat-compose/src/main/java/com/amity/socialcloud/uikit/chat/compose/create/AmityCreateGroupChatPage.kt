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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.painterResource
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
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageActivity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

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

    AmityBasePage(pageId = "create_group_page") {
        Column(modifier = modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                // Close (×) icon on left
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_close,
                    ),
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple { showLeaveConfirmation = true },
                    tint = AmityTheme.colors.base,
                )

                // Title in center
                Text(
                    text = amityChatString("chat.create.group.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                // "Create" text button on right
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                        color = AmityTheme.colors.primary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = amityChatString("chat.create.group.button"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.primary,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickableWithoutRipple {
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

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Avatar picker
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(AmityTheme.colors.primaryShade3),
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
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(amityColorBlack.copy(alpha = 0.3f))
                            .clickableWithoutRipple {
                                showImagePickerSheet = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (avatarUploadState is AmityCreateGroupChatPageViewModel.AvatarUploadState.Uploading) {
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

                Spacer(modifier = Modifier.height(24.dp))

                // Group name input — underline style
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
                                        color = AmityTheme.colors.base,
                                    )
                                ) {
                                    append(amityChatString("chat.group.name.label"))
                                }
                                append("  ")
                                withStyle(
                                    SpanStyle(
                                        color = AmityTheme.colors.baseShade2,
                                    )
                                ) {
                                    append(amityChatString("chat.group.name.optional"))
                                }
                            },
                            style = AmityTheme.typography.bodyLegacy.copy(fontSize = 15.sp),
                        )
                        Text(
                            text = "${groupName.length}/100",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 13.sp,
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 44.dp),
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
                                color = AmityTheme.colors.base,
                            ),
                            cursorBrush = SolidColor(AmityTheme.colors.primary),
                            singleLine = false,
                            decorationBox = { innerTextField ->
                                if (groupName.isEmpty()) {
                                    Text(
                                        text = amityChatString("chat.group.name.placeholder"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontSize = 15.sp,
                                            color = AmityTheme.colors.baseShade3,
                                        ),
                                    )
                                }
                                innerTextField()
                            },
                        )
                    }
                    HorizontalDivider(color = AmityTheme.colors.baseShade4)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Privacy section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = amityChatString("chat.privacy.label"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.base,
                        ),
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Public option
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
                                .background(AmityTheme.colors.baseShade4),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_globe,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = AmityTheme.colors.base,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = amityChatString("chat.create.group.public.title"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmityTheme.colors.base,
                                ),
                            )
                            Text(
                                text = amityChatString("chat.create.group.public.subtitle"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 13.sp,
                                    color = AmityTheme.colors.baseShade2,
                                ),
                            )
                        }
                        RadioButton(
                            selected = isPublic,
                            onClick = { isPublic = true },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                                unselectedColor = AmityTheme.colors.baseShade3,
                            ),
                        )
                    }

                    // Private option
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
                                .background(AmityTheme.colors.baseShade4),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.amity_ic_chat_lock,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = AmityTheme.colors.base,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = amityChatString("chat.create.group.private.title"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmityTheme.colors.base,
                                ),
                            )
                            Text(
                                text = amityChatString("chat.create.group.private.subtitle"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 13.sp,
                                    color = AmityTheme.colors.baseShade2,
                                ),
                            )
                        }
                        RadioButton(
                            selected = !isPublic,
                            onClick = { isPublic = false },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                                unselectedColor = AmityTheme.colors.baseShade3,
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.backgroundShade1)
                        .padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        amityChatString("chat.privacy.warning"),
                        style = AmityTheme.typography.caption
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Members section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = amityChatString("chat.group.members"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.base,
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
                                horizontalArrangement = Arrangement.spacedBy(19.dp),
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
                                        .size(22.dp)
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
                                        .size(22.dp)
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
        // Leave confirmation dialog
        if (showLeaveConfirmation) {
            Dialog(
                onDismissRequest = {},
                DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(AmityTheme.colors.baseShade4)
                            .width(270.dp)
                    ) {
                        Column {
                            Column(
                                modifier = Modifier.padding(19.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = amityChatString("chat.leave.without.finishing.title"),
                                    fontSize = 17.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight(600),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = AmityTheme.colors.baseInverse,
                                )
                                Text(
                                    text = amityChatString("chat.leave.without.finishing.message"),
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(400),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = AmityTheme.colors.baseInverse,
                                )
                            }
                            HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
                            Row(
                                modifier = Modifier
                                    .height(41.dp)
                                    .fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = { showLeaveConfirmation = false },
                                    modifier = Modifier.weight(0.5f)
                                ) {
                                    Text(
                                        text = amityChatString("chat.cancel"),
                                        fontSize = 17.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight(600),
                                        color = AmityTheme.colors.primary,
                                    )
                                }
                                VerticalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
                                TextButton(
                                    onClick = {
                                        showLeaveConfirmation = false
                                        onBack(true)
                                    },
                                    modifier = Modifier.weight(0.5f)
                                ) {
                                    Text(
                                        text = amityChatString("chat.leave.without.finishing.label"),
                                        fontSize = 17.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight(600),
                                        color = AmityTheme.colors.alert,
                                    )
                                }
                            }
                        }
                    }
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
            AmityUserAvatarView(
                user = user,
                size = 40.dp,
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
                    Image(
                        imageVector = ImageVector.vectorResource(
                            R.drawable.amity_ic_chat_group_moderator,
                        ),
                        contentDescription = "Moderator",
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            if (onRemove != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(4.dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(amityColorBlack.copy(alpha = 0.3f))
                        .clickableWithoutRipple { onRemove() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove member",
                        tint = amityColorWhite,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isCurrentUser) amityChatString("chat.member.you")
            else user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 11.sp,
                color = AmityTheme.colors.baseShade1,
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AmityTheme.colors.baseShade4),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_add),
                contentDescription = "Add member",
                modifier = Modifier.size(20.dp),
                tint = AmityTheme.colors.base,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amityChatString("chat.add.member.chip"),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 11.sp,
                color = AmityTheme.colors.baseShade1,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
