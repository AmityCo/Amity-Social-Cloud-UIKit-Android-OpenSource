package com.amity.socialcloud.uikit.community.compose.post.composer

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.showToast
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmitySelectedMediaComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaAttachmentElement
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaCameraSelectionSheet
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionSuggestionView
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionTextField
import com.google.gson.JsonObject

@Composable
fun AmityPostComposerPage(
    modifier: Modifier = Modifier,
    options: AmityPostComposerOptions
) {
    val context = LocalContext.current

    val isInEditMode by remember {
        mutableStateOf(options is AmityPostComposerOptions.AmityPostComposerEditOptions)
    }
    var queryToken by remember { mutableStateOf("") }

    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var selectedUserToMention by remember { mutableStateOf<AmityUser?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata.USER>>(emptyList()) }

    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var showMaxUploadLimitReachedDialog by remember { mutableStateOf(false) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showPendingPostDialog by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    LaunchedEffect(options) {
        viewModel.setComposerOptions(options)
    }

    val post by viewModel.post.collectAsState()

    val mentionGetter = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            AmityMentionMetadataGetter(options.post.getMetadata() ?: JsonObject())
        } else {
            AmityMentionMetadataGetter(JsonObject())
        }
    }

    val postText = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            (options.post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
        } else {
            ""
        }
    }

    var localPostText by remember { mutableStateOf(postText) }

    val postAttachmentPickerEvent by viewModel.postAttachmentPickerEvent.collectAsState()
    val postCreationEvent by viewModel.postCreationEvent.collectAsState()
    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()
    val isAllMediaSuccessfullyUploaded by viewModel.isAllMediaSuccessfullyUploaded.collectAsState()

    val shouldAllowToPost by remember(isInEditMode) {
        derivedStateOf {
            if (isInEditMode) {
                localPostText != postText || post?.getChildren()?.size != selectedMediaFiles.size
            } else {
                localPostText.isNotEmpty() || isAllMediaSuccessfullyUploaded
            }
        }
    }

    var capturedMediaUri by remember { mutableStateOf(Uri.EMPTY) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            viewModel.addMedia(uris, AmityPostMedia.Type.IMAGE)
        }

    val videoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            viewModel.addMedia(uris, AmityPostMedia.Type.VIDEO)
        }

    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.addMedia(listOf(capturedMediaUri), AmityPostMedia.Type.IMAGE)
            }
        }

    val videoCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { isSuccess ->
            if (isSuccess) {
                viewModel.addMedia(listOf(capturedMediaUri!!), AmityPostMedia.Type.VIDEO)
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCameraPermissionGranted = permissions.entries.all { it.value }
            if (!isCameraPermissionGranted) {
                context.showToast("Camera permission not granted")
            }
        }

    SideEffect {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
        )

        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            isCameraPermissionGranted = true
        }
    }

    LaunchedEffect(postAttachmentPickerEvent) {
        when (postAttachmentPickerEvent) {
            AmityPostAttachmentPickerEvent.OpenImageOrVideoSelectionSheet -> {
                showMediaCameraSelectionSheet = true

                val permissions = arrayOf(
                    android.Manifest.permission.CAMERA,
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

            AmityPostAttachmentPickerEvent.OpenImageCamera -> {
                if (isCameraPermissionGranted) {
                    val imageFile = AmityCameraUtil.createImageFile(context)
                    if (imageFile == null) {
                        context.showToast("Failed to create image file")
                    } else {
                        capturedMediaUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                        imageCaptureLauncher.launch(capturedMediaUri)
                    }
                } else {
                    context.showToast("Camera permission not granted")

                    val permissions = arrayOf(
                        android.Manifest.permission.CAMERA,
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
            }

            AmityPostAttachmentPickerEvent.OpenVideoCamera -> {
                if (isCameraPermissionGranted) {
                    val videoFile = AmityCameraUtil.createVideoFile(context)
                    if (videoFile == null) {
                        context.showToast("Failed to create video file")
                    } else {
                        capturedMediaUri = AmityCameraUtil.createVideoUri(context, videoFile)
                        videoCaptureLauncher.launch(capturedMediaUri)
                    }
                } else {
                    context.showToast("Camera permission not granted")

                    val permissions = arrayOf(
                        android.Manifest.permission.CAMERA,
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
            }

            AmityPostAttachmentPickerEvent.OpenImagePicker -> {
                imagePickerLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            AmityPostAttachmentPickerEvent.OpenVideoPicker -> {
                videoPickerLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly))
            }

            AmityPostAttachmentPickerEvent.OpenFilePicker -> {

            }

            AmityPostAttachmentPickerEvent.MaxUploadLimitReached -> {
                showMaxUploadLimitReachedDialog = true
            }

            AmityPostAttachmentPickerEvent.Initial -> {}
        }
    }

    if (showMaxUploadLimitReachedDialog) {
        val mediaType = if (viewModel.isUploadingImageMedia()) {
            "images"
        } else if (viewModel.isUploadingVideoMedia()) {
            "videos"
        } else {
            "files"
        }

        AmityAlertDialog(
            dialogTitle = "Maximum upload limit reached",
            dialogText = "You’ve reached the upload limit of 10 $mediaType. Any additional $mediaType will not be saved.",
            dismissText = "Close",
        ) {
            showMaxUploadLimitReachedDialog = false
        }
    }

    if (showDiscardPostDialog) {
        AmityAlertDialog(
            dialogTitle = "Discard this post?",
            dialogText = "The post will be permanently deleted. It cannot be undone.",
            confirmText = "Discard",
            dismissText = "Keep editing",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                context.closePageWithResult(Activity.RESULT_CANCELED)
            },
            onDismissRequest = {
                showDiscardPostDialog = false
            }
        )
    }

    if (showPendingPostDialog) {
        AmityAlertDialog(
            dialogTitle = "",
            dialogText = "Your post has been submitted to the pending list. It will be reviewed by community moderator.",
            dismissText = "OK",
        ) {
            showPendingPostDialog = false
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    BackHandler {
        showDiscardPostDialog = true
    }

    AmityBasePage(
        pageId = "post_composer_page"
    ) {
        LaunchedEffect(postCreationEvent) {
            when (postCreationEvent) {
                AmityPostCreationEvent.Creating -> {
                    getPageScope().showProgressSnackbar("Posting...")
                }

                AmityPostCreationEvent.Updating -> {
                    getPageScope().showProgressSnackbar("Updating...")
                }

                AmityPostCreationEvent.Success -> {
                    context.closePageWithResult(Activity.RESULT_OK)
                }

                AmityPostCreationEvent.Pending -> {
                    showPendingPostDialog = true
                }

                AmityPostCreationEvent.Failed -> {
                    val text =
                        "Failed to " + if (isInEditMode) "edit" else "create" + " post. Please try again."
                    getPageScope().showSnackbar(
                        message = text,
                        drawableRes = R.drawable.amity_ic_warning,
                        additionalHeight = if (isInEditMode) 0 else 52,
                    )
                }

                AmityPostCreationEvent.Initial -> {}
            }
        }

        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (header, content, media, suggestions, attachment) = createRefs()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 19.dp, bottom = 17.dp)
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "close_button"
                ) {
                    Icon(
                        painter = painterResource(getConfig().getIcon()),
                        contentDescription = null,
                        tint = AmityTheme.colors.base,
                        modifier = modifier
                            .clickableWithoutRipple {
                                showDiscardPostDialog = true
                            }
                            .testTag(getAccessibilityId()),
                    )
                }

                val title = when (options) {
                    is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                        if (options.targetType == AmityPostTargetType.USER) {
                            options.targetId ?: "My Timeline"
                        } else {
                            options.community?.getDisplayName() ?: "Community"
                        }
                    }

                    is AmityPostComposerOptions.AmityPostComposerEditOptions -> {
                        "Edit Post"
                    }
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = if (isInEditMode) "edit_post_title" else "community_display_name"
                ) {
                    Text(
                        text = if (isInEditMode) getConfig().getText() else title,
                        style = AmityTheme.typography.title,
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = if (isInEditMode) "edit_post_button" else "create_new_post_button"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.body.copy(
                            color = if (shouldAllowToPost) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                        ),
                        modifier = modifier
                            .clickableWithoutRipple(enabled = shouldAllowToPost) {
                                if (isInEditMode) {
                                    viewModel.updatePost(
                                        postText = localPostText,
                                        mentionedUsers = mentionedUsers,
                                    )
                                } else {
                                    viewModel.createPost(
                                        postText = localPostText,
                                        mentionedUsers = mentionedUsers,
                                    )
                                }
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
            }

            if (isInEditMode) {
                if (post != null) {
                    AmityMentionTextField(
                        maxLines = 30,
                        hintText = "What's going on...",
                        mentionedUser = selectedUserToMention,
                        value = localPostText,
                        mentionMetadata = mentionGetter.getMentionedUsers(),
                        mentionees = post?.getMentionees() ?: emptyList(),
                        onValueChange = {
                            localPostText = it
                        },
                        onMentionAdded = {
                            selectedUserToMention = null
                        },
                        onQueryToken = {
                            queryToken = it ?: ""
                            shouldShowSuggestion = (it != null)
                        },
                        onUserMentions = {
                            mentionedUsers = it
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                            .constrainAs(content) {
                                top.linkTo(header.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            } else {
                AmityMentionTextField(
                    maxLines = 30,
                    hintText = "What's going on...",
                    mentionedUser = selectedUserToMention,
                    value = localPostText,
                    onValueChange = {
                        localPostText = it
                    },
                    onMentionAdded = {
                        selectedUserToMention = null
                    },
                    onQueryToken = {
                        queryToken = it ?: ""
                        shouldShowSuggestion = (it != null)
                    },
                    onUserMentions = {
                        mentionedUsers = it
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .constrainAs(content) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }

            AmitySelectedMediaComponent(
                modifier = Modifier
                    .constrainAs(media) {
                        top.linkTo(content.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            if (shouldShowSuggestion) {
                AmityMentionSuggestionView(
                    heightIn = 150.dp,
                    community = viewModel.community,
                    keyword = queryToken,
                    modifier = Modifier
                        .constrainAs(suggestions) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(attachment.top)
                        },
                ) {
                    selectedUserToMention = it
                    shouldShowSuggestion = false
                }
            }

            if (!isInEditMode) {
                AmityMediaAttachmentElement(
                    modifier = Modifier
                        .constrainAs(attachment) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    pageScope = getPageScope(),
                )
            }
        }

        if (showMediaCameraSelectionSheet) {
            AmityMediaCameraSelectionSheet(
                modifier = modifier,
            ) { type ->
                showMediaCameraSelectionSheet = false

                type?.let {
                    viewModel.setPostAttachmentPickerEvent(
                        when (type) {
                            AmityPostMedia.Type.IMAGE -> AmityPostAttachmentPickerEvent.OpenImageCamera
                            AmityPostMedia.Type.VIDEO -> AmityPostAttachmentPickerEvent.OpenVideoCamera
                        }
                    )
                }
            }
        }
    }
}