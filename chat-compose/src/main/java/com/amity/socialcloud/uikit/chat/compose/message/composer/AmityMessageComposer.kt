package com.amity.socialcloud.uikit.chat.compose.message.composer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import com.amity.socialcloud.uikit.chat.compose.conversation.AmityChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.composer.MessageComposeErrorPopup
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import java.io.File
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityMessageComposer(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityChatPageViewModel,
    isModerator: Boolean = false,
    isUserMuted: Boolean = false,
    isUserBanned: Boolean = false,
    isChannelMuted: Boolean = false,
) {
    val shouldShowComposer = when {
        isUserBanned -> false
        isUserMuted -> false
        isChannelMuted -> isModerator
        else -> true
    }

    var messageText by remember { mutableStateOf("") }
    val replyMessage by viewModel.replyToMessage.collectAsState()
    val editingMessage by viewModel.editingMessage.collectAsState()
    var showMediaSection by remember { mutableStateOf(false) }
    var isSendButtonEnabled by remember { mutableStateOf(false) }
    var showComposeErrorDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var preEditText by remember { mutableStateOf("") }

    LaunchedEffect(editingMessage) {
        if (editingMessage != null) {
            preEditText = messageText
            messageText = (editingMessage!!.getData() as? AmityMessage.Data.TEXT)?.getText() ?: ""
        }
    }

    LaunchedEffect(editingMessage, messageText) {
        val editedMessage = (editingMessage?.getData() as? AmityMessage.Data.TEXT)?.getText()
        isSendButtonEnabled = if (editedMessage == null) {
            messageText.isNotBlank()
        } else {
            messageText.isNotBlank() && messageText != editedMessage
        }
    }

    LaunchedEffect(replyMessage) {
        if (replyMessage != null) {
            focusRequester.requestFocus()
        }
    }

    // Observe reply parent message for live updates (e.g., deletion)
    val replyMessageId = replyMessage?.getMessageId()
    LaunchedEffect(replyMessageId) {
        if (replyMessageId != null) {
            viewModel.getMessage(replyMessageId).collect { observedMessage ->
                viewModel.updateReplyToMessage(observedMessage)
            }
        }
    }

    val context = LocalContext.current

    // Image/Video picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri != null) {
            val parentId = replyMessage?.getMessageId()
            val mimeType = context.contentResolver.getType(uri)
            if (mimeType?.startsWith("video/") == true) {
                viewModel.sendVideoMessage(uri, parentId = parentId)
            } else {
                viewModel.sendImageMessage(uri, parentId = parentId)
            }
            viewModel.dismissReplyMessage()
            showMediaSection = false
        }
    }

    // Camera photo launcher
    var cameraPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success) {
            cameraPhotoUri?.let { uri ->
                val parentId = replyMessage?.getMessageId()
                viewModel.sendImageMessage(uri, parentId = parentId)
                viewModel.dismissReplyMessage()
                showMediaSection = false
            }
        }
    }

    // Camera video launcher
    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
    ) { success: Boolean ->
        if (success) {
            cameraVideoUri?.let { uri ->
                val parentId = replyMessage?.getMessageId()
                viewModel.sendVideoMessage(uri, parentId = parentId)
                viewModel.dismissReplyMessage()
                showMediaSection = false
            }
        }
    }

    // Camera type chooser state
    var showCameraChooser by remember { mutableStateOf(false) }
    val cameraChooserSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted: Boolean ->
        if (granted) {
            showCameraChooser = true
        }
    }

    // Camera type chooser bottom sheet
    if (showCameraChooser) {
        ModalBottomSheet(
            onDismissRequest = { showCameraChooser = false },
            sheetState = cameraChooserSheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .navigationBarsPadding()
            ) {
                AmityBottomSheetActionItem(
                    icon = null,
                    text = amityChatString("chat.reply.photo.label"),
                ) {
                    showCameraChooser = false
                    val photoFile = File.createTempFile(
                        "IMG_${System.currentTimeMillis()}",
                        ".jpg",
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    )
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.applicationContext.packageName}.UikitCommonProvider",
                        photoFile,
                    )
                    cameraPhotoUri = uri
                    cameraLauncher.launch(uri)
                }

                AmityBottomSheetActionItem(
                    icon = null,
                    text = amityChatString("chat.reply.video.label"),
                ) {
                    showCameraChooser = false
                    val videoFile = File.createTempFile(
                        "VID_${System.currentTimeMillis()}",
                        ".mp4",
                        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                    )
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.applicationContext.packageName}.UikitCommonProvider",
                        videoFile,
                    )
                    cameraVideoUri = uri
                    cameraVideoLauncher.launch(uri)
                }
            }
        }
    }

    AmityBaseComponent(
        componentId = "message_composer",
        pageScope = pageScope,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding(),
        ) {
            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            if (!shouldShowComposer) {
                MutedBanner(
                    isUserMuted = isUserMuted,
                    isUserBanned = isUserBanned,
                )
            } else {
            // Edit / reply preview — mutually exclusive, edit takes priority
            when {
                editingMessage != null -> EditPreview(
                    onDismiss = {
                        messageText = preEditText
                        viewModel.cancelEditingMessage()
                    },
                )
                replyMessage != null -> ReplyPreview(
                    message = replyMessage!!,
                    onDismiss = { viewModel.dismissReplyMessage() },
                )
            }

            // Compose bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (editingMessage == null) {
                    // Media toggle button
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 8.dp)
                            .size(32.dp)
                            .clickable {
                                showMediaSection = !showMediaSection
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (showMediaSection)
                                    R.drawable.amity_ic_chat_media_close
                                else
                                    R.drawable.amity_ic_chat_media_open
                            ),
                            contentDescription = "Toggle media",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }

                // Text input
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(AmityTheme.colors.baseShade4)
                        .heightIn(min = 40.dp, max = 120.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (messageText.isEmpty()) {
                        Text(
                            text = amityChatString("chat.composer.placeholder"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 15.sp,
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                    BasicTextField(
                        value = messageText,
                        onValueChange = {
                            messageText = it
                            if (showMediaSection) showMediaSection = false
                        },
                        modifier = Modifier.fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 15.sp,
                            color = AmityTheme.colors.baseInverse,
                        ),
                        cursorBrush = SolidColor(AmityTheme.colors.primary),
                    )
                }

                // Send button — hidden when media section is open
                if (!showMediaSection) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSendButtonEnabled) AmityTheme.colors.primary
                                else AmityTheme.colors.baseShade4
                            )
                            .clickable(enabled = isSendButtonEnabled) {
                                val text = messageText.trim()
                                if (text.length > 10000) {
                                    showComposeErrorDialog = true
                                    return@clickable
                                }
                                if (text.isNotEmpty()) {
                                    val currentEditingMessage = editingMessage
                                    if (currentEditingMessage != null) {
                                        AmityChatClient.newMessageRepository()
                                            .editTextMessage(currentEditingMessage.getMessageId())
                                            .text(text)
                                            .build()
                                            .apply()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({}, {})
                                        messageText = ""
                                        viewModel.cancelEditingMessage()
                                    } else {
                                        val parentId = replyMessage?.getMessageId()
                                        viewModel.createTextMessage(text, parentId = parentId)
                                        messageText = ""
                                        viewModel.dismissReplyMessage()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_arrow_upward),
                            contentDescription = "Send",
                            modifier = Modifier.size(20.dp),
                            tint = if (isSendButtonEnabled) amityColorWhite
                            else AmityTheme.colors.baseShade3,
                        )
                    }
                }
            }

            // Expandable media section
            AnimatedVisibility(
                visible = showMediaSection,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    // Camera button
                    MediaButton(
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

                    // Photo/Gallery button
                    MediaButton(
                        iconResId = R.drawable.amity_ic_chat_image_button,
                        label = amityChatString("chat.media.photo"),
                        onClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageAndVideo,
                                )
                            )
                        },
                    )
                }
            }
            } // end else (shouldShowComposer)
        }
    }

    // Message length error dialog
    if (showComposeErrorDialog) {
        MessageComposeErrorPopup(
            confirmText = amityCommonString("amity_common_modal_dialog_done_button"),
            onDismiss = {
                showComposeErrorDialog = false
            }
        )
    }
}

@Composable
private fun MediaButton(
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

@Composable
private fun EditPreview(
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.baseShade4)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = amityChatString("chat.editing.message"),
            style = AmityTheme.typography.captionBold,
        )
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade2,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ReplyPreview(
    message: AmityMessage,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val defaultUser = amityChatString("chat.unknown.user")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.baseShade4)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val creator = message.getCreator()
                val text = if (creator?.getUserId() == AmityCoreClient.getUserId()){
                    amityChatString("chat.replying.to", amityChatString("chat.message.replying.yourself"))
                } else {
                    amityChatString("chat.replying.to", creator?.getDisplayName() ?: defaultUser)
                }
                Text(
                    text = text,
                    style = AmityTheme.typography.captionBold.copy(
                        color = AmityTheme.colors.base,
                    ),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = getReplyPreviewText(message, context),
                    style = AmityTheme.typography.caption.copy(
                        color = AmityTheme.colors.baseShade1,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Thumbnail for image/video replies
            val data = message.getData()
            if (data is AmityMessage.Data.IMAGE) {
                val imageUrl = data.getImage()?.getUrl(AmityImage.Size.SMALL)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmityTheme.colors.baseShade3),
                    contentAlignment = Alignment.Center,
                ) {
                    if (!imageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imageUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED).build(),
                            contentDescription = "Reply image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            } else if (data is AmityMessage.Data.VIDEO) {
                val thumbnailUrl = data.getThumbnailImage()?.getUrl(AmityImage.Size.SMALL)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmityTheme.colors.baseShade3),
                    contentAlignment = Alignment.Center,
                ) {
                    if (!thumbnailUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(thumbnailUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED).build(),
                            contentDescription = "Video thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_video_play),
                        contentDescription = "Video",
                        modifier = Modifier.size(24.dp),
                        tint = amityColorWhite,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
            contentDescription = "Close reply",
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onDismiss),
            tint = AmityTheme.colors.baseShade2,
        )
    }
}

private fun getReplyPreviewText(message: AmityMessage, context: android.content.Context): String {
    if (message.isDeleted()) {
        return DefaultAmityChatStringProvider.getInstance().getString("chat.jump.to.message.unavailable")
    }
    return when (val data = message.getData()) {
        is AmityMessage.Data.TEXT -> data.getText()
        is AmityMessage.Data.IMAGE -> context.getString(R.string.amity_chat_reply_photo_label)
        is AmityMessage.Data.VIDEO -> context.getString(R.string.amity_chat_reply_video_label)
        else -> context.getString(R.string.amity_chat_preview_message)
    }
}

@Composable
private fun MutedBanner(
    isUserMuted: Boolean,
    isUserBanned: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_muted),
            contentDescription = if (isUserBanned) "Banned" else "Muted",
            modifier = Modifier.size(20.dp),
            tint = AmityTheme.colors.baseShade1,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = when {
                isUserBanned -> amityChatString("chat.group.user.banned")
                isUserMuted -> amityChatString("chat.group.user.muted")
                else -> amityChatString("chat.group.permission.only.moderators.banner")
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AmityTheme.colors.baseShade1,
            ),
        )
    }
}
