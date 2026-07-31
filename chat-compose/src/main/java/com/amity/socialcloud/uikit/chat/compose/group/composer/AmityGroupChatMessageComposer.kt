package com.amity.socialcloud.uikit.chat.compose.group.composer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import androidx.compose.ui.draw.shadow
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityMessageMentionTextField
import com.amity.socialcloud.uikit.chat.compose.live.composer.MessageComposeErrorPopup
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarType
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopover
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import java.io.File
import kotlinx.coroutines.delay

@Composable
fun AmityGroupChatMessageComposer(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityGroupChatPageViewModel,
    isModerator: Boolean = false,
    isUserMuted: Boolean = false,
    isUserBanned: Boolean = false,
    isChannelMuted: Boolean = false,
    mentionSuggestionSelected: AmityMentionSuggestion? = null,
    onMentionSuggestionConsumed: () -> Unit = {},
    onMentionQueryChanged: (Boolean, String) -> Unit = { _, _ -> },
) {
    val shouldShowComposer = when {
        isUserBanned -> false
        isUserMuted -> false
        isChannelMuted -> isModerator
        else -> true
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
            AmityDivider(variant = AmityDividerVariant.Post)

            if (shouldShowComposer) {
                ComposerContent(
                    viewModel = viewModel,
                    onMentionQueryChanged = onMentionQueryChanged,
                    mentionSuggestionSelected = mentionSuggestionSelected,
                    onMentionSuggestionConsumed = onMentionSuggestionConsumed,
                )
            } else {
                MutedBanner(
                    isUserMuted = isUserMuted,
                    isUserBanned = isUserBanned,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposerContent(
    viewModel: AmityGroupChatPageViewModel,
    onMentionQueryChanged: (Boolean, String) -> Unit = { _, _ -> },
    mentionSuggestionSelected: AmityMentionSuggestion? = null,
    onMentionSuggestionConsumed: () -> Unit = {},
) {
    var messageText by remember { mutableStateOf("") }
    var shouldClearText by remember { mutableStateOf(false) }
    val replyMessage by viewModel.replyToMessage.collectAsState()
    val editingMessage by viewModel.editingMessage.collectAsState()
    var showMediaSection by remember { mutableStateOf(false) }
    var isSendButtonEnabled by remember { mutableStateOf(false) }
    var showComposeErrorDialog by remember { mutableStateOf(false) }
    var showMentionLimitErrorDialog by remember { mutableStateOf(false) }
    var preEditText by remember { mutableStateOf("") }

    var shouldRequestFocus by remember { mutableStateOf(false) }

    val bannedWordErrorMessage = amityChatString("chat.toast.banned.word")
    val linkNotAllowedErrorMessage = amityChatString("chat.toast.link.not.allow")
    val generalErrorMessage = amityChatString("chat.message.failed.to.send")

    // Observe reply parent message for live updates (e.g., deletion)
    val replyMessageId = replyMessage?.getMessageId()
    LaunchedEffect(replyMessageId) {
        if (replyMessageId != null) {
            viewModel.getMessage(replyMessageId).collect { observedMessage ->
                viewModel.updateReplyToMessage(observedMessage)
            }
        }
    }

    LaunchedEffect(replyMessage, editingMessage) {
        if (replyMessage != null || editingMessage != null) {
            shouldRequestFocus = true
            kotlinx.coroutines.delay(300)
            shouldRequestFocus = false
        }
    }

    LaunchedEffect(editingMessage, messageText) {
        val editedText = (editingMessage?.getData() as? AmityMessage.Data.TEXT)?.getText()
        isSendButtonEnabled = if (editedText == null) {
            messageText.isNotBlank()
        } else {
            messageText.isNotBlank() && messageText != editedText
        }
    }

    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var queryToken by remember { mutableStateOf("") }
    var selectedUserToMention by remember { mutableStateOf<AmityMentionSuggestion?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata>>(emptyList()) }
    var suppressMentionSuggestion by remember { mutableStateOf(false) }
    var editMentionMetadata by remember { mutableStateOf<List<AmityMentionMetadata>>(emptyList()) }
    var editMentionees by remember { mutableStateOf<List<AmityMentionee>>(emptyList()) }

    LaunchedEffect(shouldShowSuggestion, queryToken) {
        if (!suppressMentionSuggestion) {
            onMentionQueryChanged(shouldShowSuggestion, queryToken)
        }
    }

    // Suppress mention popup when entering edit mode and prepare mention metadata for highlighting
    LaunchedEffect(editingMessage) {
        if (editingMessage != null) {
            preEditText = messageText
            suppressMentionSuggestion = true
            val metadata = editingMessage!!.getMetadata()
            if (metadata != null) {
                val getter = AmityMentionMetadataGetter(metadata)
                editMentionMetadata = getter.getMentionedUsers() + getter.getMentionedChannels()
                editMentionees = editingMessage!!.getMentionees()
            } else {
                editMentionMetadata = emptyList()
                editMentionees = emptyList()
            }
            messageText = (editingMessage!!.getData() as? AmityMessage.Data.TEXT)?.getText() ?: ""
            shouldShowSuggestion = false
            kotlinx.coroutines.delay(500)
            suppressMentionSuggestion = false
        } else {
            editMentionMetadata = emptyList()
            editMentionees = emptyList()
        }
    }

    LaunchedEffect(mentionSuggestionSelected) {
        if (mentionSuggestionSelected != null) {
            selectedUserToMention = mentionSuggestionSelected
            shouldShowSuggestion = false
            onMentionSuggestionConsumed()
        }
    }
    val context = LocalContext.current

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

    var showCameraChooser by remember { mutableStateOf(false) }
    val cameraChooserSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted: Boolean ->
        if (granted) {
            showCameraChooser = true
        }
    }

    if (showCameraChooser) {
        AmitySheet(
            onDismissRequest = { showCameraChooser = false },
            sheetState = cameraChooserSheetState,
            contentWindowInsets = { WindowInsets.waterfall },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .navigationBarsPadding()
            ) {
                AmityChatSheetActionItem(
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

                AmityChatSheetActionItem(
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

    // Edit / reply preview — mutually exclusive, edit takes priority
    when {
        editingMessage != null -> EditPreview(
            message = editingMessage!!,
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // Bottom-align with an 8dp inset: the +/send buttons ride the input as it grows
        // multi-line, and sit centered against the idle single-line input.
        verticalAlignment = Alignment.Bottom,
    ) {
        if (editingMessage == null) {
            AmityButton(
                modifier = Modifier.padding(end = 12.dp, bottom = 8.dp),
                variant = AmityButtonVariant.ICON,
                style = AmityButtonStyle.FILLED,
                hierarchy = AmityButtonHierarchy.SECONDARY,
                iconSize = AmityIconButtonSize.SIZE32,
                icon = if (showMediaSection)
                    CommonR.drawable.amity_ic_cross_r
                else
                    CommonR.drawable.amity_ic_plus_r,
                onClick = {
                    showMediaSection = !showMediaSection
                },
            )
        }

        // Reuses live chat's mention text field rather than a separate implementation.
        AmityMessageMentionTextField(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp, max = 120.dp),
            value = messageText,
            hint = amityChatString("chat.composer.placeholder"),
            maxLines = 8,
            isEnabled = true,
            addedMention = selectedUserToMention,
            shouldClearText = shouldClearText,
            shouldRequestFocus = shouldRequestFocus,
            editMentionMetadata = editMentionMetadata,
            editMentionees = editMentionees,
            onValueChange = {
                messageText = it
                if (showMediaSection) showMediaSection = false
            },
            onMentionAdded = {
                selectedUserToMention = null
            },
            onQueryToken = {
                queryToken = it ?: ""
                shouldShowSuggestion = (it != null)
            },
            onMention = {
                mentionedUsers = it
            },
            onMentionLimitReached = {
                showMentionLimitErrorDialog = true
            },
        )

        if (!showMediaSection) {
            Spacer(modifier = Modifier.width(12.dp))

            AmityButton(
                modifier = Modifier.padding(bottom = 8.dp),
                variant = AmityButtonVariant.ICON,
                style = AmityButtonStyle.FILLED,
                hierarchy = if (isSendButtonEnabled) AmityButtonHierarchy.PRIMARY else AmityButtonHierarchy.SECONDARY,
                iconSize = AmityIconButtonSize.SIZE32,
                icon = CommonR.drawable.amity_ic_arrow_up_r,
                enabled = isSendButtonEnabled,
                onClick = {
                    val text = messageText.trim()
                    when {
                        text.length > 10000 -> {
                            showComposeErrorDialog = true
                        }
                        text.isNotEmpty() -> {
                            val currentEditingMessage = editingMessage
                            if (currentEditingMessage != null) {
                                shouldClearText = true
                                AmityChatClient.newMessageRepository()
                                    .editTextMessage(currentEditingMessage.getMessageId())
                                    .text(text)
                                    .build()
                                    .apply()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        { shouldClearText = false },
                                        { shouldClearText = false },
                                    )
                                messageText = ""
                                viewModel.cancelEditingMessage()
                            } else {
                                shouldClearText = true
                                val parentId = replyMessage?.getMessageId()
                                viewModel.createTextMessage(
                                    text = text,
                                    parentId = parentId,
                                    mentionMetadata = mentionedUsers,
                                    onSuccess = {
                                        shouldClearText = false
                                    },
                                    onError = { exception ->
                                        shouldClearText = false
                                        val errorMessage = if (exception is AmityException) {
                                            when (AmityError.from(exception.code)) {
                                                AmityError.BAN_WORD_FOUND -> bannedWordErrorMessage
                                                AmityError.LINK_NOT_ALLOWED -> linkNotAllowedErrorMessage
                                                else -> exception.message ?: generalErrorMessage
                                            }
                                        } else {
                                            exception.message ?: generalErrorMessage
                                        }
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(errorMessage)
                                    },
                                )
                                messageText = ""
                                selectedUserToMention = null
                                mentionedUsers = emptyList()
                                viewModel.dismissReplyMessage()
                            }
                        }
                    }
                },
            )
        }
    }

    AnimatedVisibility(
        visible = showMediaSection,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral))
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(56.dp, Alignment.CenterHorizontally),
        ) {
            MediaButton(
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

            MediaButton(
                iconResId = CommonR.drawable.amity_ic_image_r,
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

    if (showComposeErrorDialog) {
        MessageComposeErrorPopup(
            confirmText = amityCommonString("amity_common_modal_dialog_done_button"),
            onDismiss = {
                showComposeErrorDialog = false
            }
        )
    }

    if (showMentionLimitErrorDialog) {
        MessageComposeErrorPopup(
            title = amityChatString("chat.reach.mention.limit.title"),
            message = amityChatString("chat.reach.mention.limit.message"),
            confirmText = amityChatString("chat.button.ok"),
            onDismiss = {
                showMentionLimitErrorDialog = false
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

@Composable
private fun MutedBanner(
    isUserMuted: Boolean,
    isUserBanned: Boolean = false,
) {
    AmityBanner(
        hierarchy = AmityBannerHierarchy.SUBDUE,
        centered = true,
        description = when {
            isUserBanned -> amityChatString("chat.group.user.banned")
            isUserMuted -> amityChatString("chat.group.user.muted")
            else -> amityChatString("chat.group.permission.only.moderators.banner")
        },
    )
}

@Composable
private fun EditPreview(
    message: AmityMessage,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.token(AmityColorToken.SurfaceBannerSubdueGeneral))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = amityChatString("chat.editing.message"),
            style = AmityTheme.typography.captionBold.copy(
                color = AmityTheme.token(AmityColorToken.TextBannerSubdueOverlineGeneral),
            ),
        )

        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.GHOST,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = CommonR.drawable.amity_ic_cross_r,
            onClick = onDismiss,
        )
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
            .background(AmityTheme.token(AmityColorToken.SurfaceBannerSubdueGeneral))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                val isReplyingToSelf = message.getCreator()?.getUserId() == AmityCoreClient.getUserId()
                Text(
                    text = if (isReplyingToSelf){
                        amityChatString("chat.replying.to", amityChatString("chat.message.replying.yourself"))
                    } else {
                        amityChatString("chat.replying.to", message.getCreator()?.getDisplayName() ?: defaultUser)
                    },
                    style = AmityTheme.typography.captionBold.copy(
                        color = AmityTheme.token(AmityColorToken.TextBannerSubdueOverlineGeneral),
                    ),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = getReplyPreviewText(message, context),
                    style = AmityTheme.typography.caption.copy(
                        color = AmityTheme.token(AmityColorToken.TextBannerSubdueTextDescriptionGeneral),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            val data = message.getData()
            if (data is AmityMessage.Data.IMAGE) {
                val imageUrl = data.getImage()?.getUrl(AmityImage.Size.SMALL)
                AsyncImage(
                    model = ImageRequest.Builder(context).data(imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED).build(),
                    contentDescription = "Reply image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else if (data is AmityMessage.Data.VIDEO) {
                val thumbnailUrl = data.getThumbnailImage()?.getUrl(AmityImage.Size.SMALL)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmityTheme.token(AmityColorToken.SurfaceMediaVideoLoading)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (!thumbnailUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(thumbnailUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED).build(),
                            contentDescription = "Video thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack),
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_video_play_s),
                            contentDescription = "Video",
                            modifier = Modifier.size(16.dp),
                            tint = AmityTheme.token(AmityColorToken.IconIconButtonTransparentPrimaryDefault),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.GHOST,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = CommonR.drawable.amity_ic_cross_r,
            onClick = onDismiss,
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
        else -> {
            context.getString(R.string.amity_chat_preview_message)
        }
    }
}

@Composable
internal fun GroupMentionSuggestionView(
    modifier: Modifier = Modifier,
    keyword: String,
    viewModel: AmityGroupChatPageViewModel,
    onClick: (AmityMentionSuggestion) -> Unit,
    onClose: (() -> Unit)? = null,
) {
    val suggestions = remember(keyword) {
        if (keyword.isEmpty()) {
            viewModel.getChannelMembers()
        } else {
            viewModel.searchChannelMembers(keyword)
        }
    }.collectAsLazyPagingItems()

    if (suggestions.itemCount == 0) return

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        // `Modifier.fillMaxWidth()` overrides AmityPopover's default fixed width so the suggestion
        // card spans this composer's full width.
        AmityPopover(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(0.dp, 120.dp)
            ) {
        items(
            count = suggestions.itemCount,
            key = { index -> index }
        ) {
            val suggestion = suggestions[it] ?: return@items
            val text = if (suggestion is AmityMentionSuggestion.USER) {
                suggestion.user.getDisplayName() ?: ""
            } else {
                amityChatString("chat.tab.all")
            }
            val avatarUrl = if (suggestion is AmityMentionSuggestion.USER) {
                suggestion.user.getAvatar()?.getUrl(AmityImage.Size.SMALL)
            } else {
                null
            }
            val isBrandUser = suggestion is AmityMentionSuggestion.USER && suggestion.user.isBrand()
            if (suggestion is AmityMentionSuggestion.USER && suggestion.user.isGlobalBan()) return@items
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = AmityTheme.token(AmityColorToken.SurfacePopoverListsDefault))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            bounded = true,
                            color = AmityTheme.token(AmityColorToken.SurfacePopoverListsHover),
                        ),
                        onClick = { onClick(suggestion) },
                    )
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                if (suggestion is AmityMentionSuggestion.CHANNEL) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AmityTheme.token(AmityColorToken.SurfaceFeaturedIconSolid)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_at_s),
                            contentDescription = null,
                            tint = AmityTheme.token(AmityColorToken.IconFeaturedIconSolid),
                            modifier = Modifier.size(24.dp),
                        )
                    }
                } else {
                    AmityMessageAvatarView(
                        avatarUrl = avatarUrl,
                        displayName = text,
                        avatarType = AmityAvatarType.USER,
                        size = 32.dp,
                    )
                }
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.captionBold.copy(
                        color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                    ),
                    modifier = Modifier.padding(start = 12.dp)
                )

                if (isBrandUser) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (suggestion is AmityMentionSuggestion.CHANNEL) {
                    Text(
                        text = amityChatString("chat.mention.everyone"),
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.caption,
                        color = AmityTheme.token(AmityColorToken.TextListTrailingTextGeneral),
                    )
                }
            }
        }
            }
        }

        if (onClose != null) {
            AmityButton(
                variant = AmityButtonVariant.ICON,
                style = AmityButtonStyle.FILLED,
                hierarchy = AmityButtonHierarchy.SECONDARY,
                iconSize = AmityIconButtonSize.SIZE24,
                icon = CommonR.drawable.amity_ic_cross_r,
                contentDescription = "Close",
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(8.dp, (-8).dp)
                    .shadow(elevation = 4.dp, shape = CircleShape),
            )
        }
    }
}
