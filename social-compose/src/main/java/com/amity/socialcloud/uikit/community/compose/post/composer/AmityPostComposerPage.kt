package com.amity.socialcloud.uikit.community.compose.post.composer

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextConfigMode
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityAltTextConfigComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmitySelectedMediaComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityClipAttachmentElement
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaAttachmentElement
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaCameraSelectionSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaPlayButton
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionSuggestionView
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionTextField
import com.google.gson.JsonObject

@Composable
fun AmityPostComposerPage(
    modifier: Modifier = Modifier,
    options: AmityPostComposerOptions,
) {
    val maxTitleChar = 150

    val context = LocalContext.current

    val isInEditMode by remember {
        mutableStateOf(options is AmityPostComposerOptions.AmityPostComposerEditOptions)
    }
    var queryToken by remember { mutableStateOf("") }
    var hashtagToken by remember { mutableStateOf("") }

    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var selectedUserToMention by remember { mutableStateOf<AmityUser?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata.USER>>(emptyList()) }
    var hashtags by remember { mutableStateOf<List<AmityHashtag>>(emptyList()) }

    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var showMaxUploadLimitReachedDialog by remember { mutableStateOf(false) }
    var maxUploadLimitMediaType by remember { mutableStateOf<AmityPostMedia.Type?>(null) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showPendingPostDialog by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    var existingAttachmentType by remember { mutableStateOf<AmityPostMedia.Type?>(null) }
    val originalAttachmentIds = remember { mutableStateOf(emptyList<String>()) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    LaunchedEffect(options) {
        viewModel.setComposerOptions(options)
    }

    val isCreateClipMode = options is AmityPostComposerOptions.AmityPostComposerCreateClipOptions
    val isEditClipMode = options is AmityPostComposerOptions.AmityPostComposerEditClipOptions

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(isEditClipMode, isCreateClipMode) {
        val uri = if (isCreateClipMode) {
            (options as AmityPostComposerOptions.AmityPostComposerCreateClipOptions).uri
        } else {
            Uri.EMPTY
        }
        if (uri != Uri.EMPTY && uri != null) {

            exoPlayer.apply {
                addMediaItem(MediaItem.fromUri(uri))
                prepare()
                repeatMode = ExoPlayer.REPEAT_MODE_OFF
                playWhenReady = false
                volume = 0f
            }
        }
    }

    val post by viewModel.post.collectAsState()

    val mentionGetter = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            AmityMentionMetadataGetter(options.post.getMetadata() ?: JsonObject())
        } else {
            AmityMentionMetadataGetter(JsonObject())
        }
    }

    val hashtagGetter = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            AmityHashtagMetadataGetter(options.post.getMetadata() ?: JsonObject())
        } else {
            AmityHashtagMetadataGetter(JsonObject())
        }
    }

    // Initialize title text for edit mode
    val postTitle = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            (options.post.getData() as? AmityPost.Data.TEXT)?.getTitle() ?: ""
        } else {
            ""
        }
    }

    // Initialize body text for edit mode (excluding title)
    val postBodyText = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            (options.post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
        } else if (options is AmityPostComposerOptions.AmityPostComposerEditClipOptions) {
            (options.post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
        } else {
            ""
        }
    }

    val isKeyboardOpen by isKeyboardVisible()
    val keyboardHeight by getKeyboardHeight()

    var localPostText by remember { mutableStateOf(postBodyText) }

    // Title field state
    var titleText by remember { mutableStateOf(postTitle) }

    val postAttachmentPickerEvent by viewModel.postAttachmentPickerEvent.collectAsState()
    val postCreationEvent by viewModel.postCreationEvent.collectAsState()
    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()
    val isAllMediaSuccessfullyUploaded by viewModel.isAllMediaSuccessfullyUploaded.collectAsState()

    val shouldAllowToPost by remember(
        isInEditMode,
        isEditClipMode,
        titleText,
        localPostText,
        selectedMediaFiles,
        isAllMediaSuccessfullyUploaded,
        postCreationEvent
    ) {
        fun isContentReady(
            titleText: String,
            text: String,
            mediaFiles: List<AmityPostMedia>,
        ): Boolean {
            return if (mediaFiles.isEmpty()) {
                // If no attachments, body text must not be empty (body text is mandatory)
                if (!isCreateClipMode) {
                    text.trim().isNotEmpty()
                } else {
                    // For clip creation, text is optional
                    true
                }
            } else {
                // If attachments exist, only care if they're uploaded successfully
                // (title and text are optional when attachments exist)
                isAllMediaSuccessfullyUploaded
            }
        }

        // Don't allow posting when creation/update operation is in progress
        val isOperationInProgress = postCreationEvent == AmityPostCreationEvent.Creating ||
                postCreationEvent == AmityPostCreationEvent.Updating

        derivedStateOf {
            if (isOperationInProgress) {
                // Disable button during operations
                false
            } else if (isInEditMode) {
                // Edit mode validation
                val hasTitleChanged = titleText.trim() != postTitle.trim()
                val hasTextChanged = localPostText.trim() != postBodyText.trim()
                val hasAttachmentsChanged =
                    hasAttachmentsChanged(originalAttachmentIds.value, selectedMediaFiles)
                val isContentValid = isContentReady(titleText, localPostText, selectedMediaFiles)

                (hasTitleChanged || hasTextChanged || hasAttachmentsChanged) && isContentValid
            } else if (isEditClipMode) {
                // Edit clip mode validation
                val hasTextChanged = localPostText.trim() != postBodyText.trim()

                hasTextChanged
            } else {
                // Create mode validation
                isContentReady(titleText, localPostText, selectedMediaFiles)
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
                AmityUIKitSnackbar.publishSnackbarErrorMessage("Camera permission not granted")
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
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to create image file")
                    } else {
                        capturedMediaUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                        imageCaptureLauncher.launch(capturedMediaUri)
                    }
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Camera permission not granted")


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
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to create video file")

                    } else {
                        val videoUri = AmityCameraUtil.createVideoUri(context, videoFile)
                        if (videoUri == null) {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to create video URI")
                        } else {
                            capturedMediaUri = videoUri
                            videoCaptureLauncher.launch(videoUri)
                        }
                    }
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Camera permission not granted")


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

            is AmityPostAttachmentPickerEvent.MaxUploadLimitReached -> {
                showMaxUploadLimitReachedDialog = true
                maxUploadLimitMediaType =
                    (postAttachmentPickerEvent as AmityPostAttachmentPickerEvent.MaxUploadLimitReached).mediaType
            }

            AmityPostAttachmentPickerEvent.Initial -> {}
        }
    }


    LaunchedEffect(post) {
        // Set existing type if the post already has any attachment
        val firstAttachment = post?.getChildren()?.firstOrNull()
        existingAttachmentType = when {
            firstAttachment?.getData() is AmityPost.Data.IMAGE -> AmityPostMedia.Type.IMAGE
            firstAttachment?.getData() is AmityPost.Data.VIDEO -> AmityPostMedia.Type.VIDEO
            firstAttachment?.getData() is AmityPost.Data.CLIP -> AmityPostMedia.Type.ClIP
            else -> null
        }

        val children = post?.getChildren().orEmpty()
        originalAttachmentIds.value = children.mapNotNull { childPost ->
            when (val data = childPost.getData()) {
                is AmityPost.Data.IMAGE -> data.getImage()?.getFileId()
                is AmityPost.Data.VIDEO -> data.getThumbnailImage()?.getFileId()
                is AmityPost.Data.CLIP -> data.getThumbnailImage()?.getFileId()
                else -> null
            }
        }
    }

    LaunchedEffect(selectedMediaFiles) {
        if (selectedMediaFiles.isEmpty()) {
            existingAttachmentType = null
        } else {
            // Update to the type of the first selected media
            existingAttachmentType = selectedMediaFiles.first().type
        }
    }

    BackHandler {
        if (isInEditMode) {
            val hasTitleChanged = titleText.trim() != postTitle.trim()
            val hasTextChanged = localPostText.trim() != postBodyText.trim()
            val hasAttachmentsChanged =
                hasAttachmentsChanged(originalAttachmentIds.value, selectedMediaFiles)
            val hasEdited = hasTitleChanged || hasTextChanged || hasAttachmentsChanged
            if (hasEdited) {
                showDiscardPostDialog = true
            } else {
                context.closePageWithResult(Activity.RESULT_CANCELED)
            }
        } else if (isEditClipMode) {
            val hasEdited = localPostText.trim() != postBodyText.trim()
            if (hasEdited) {
                showDiscardPostDialog = true
            } else {
                context.closePageWithResult(Activity.RESULT_CANCELED)
            }
        } else {
            val hasInput = titleText.trim().isNotEmpty() || localPostText.trim()
                .isNotEmpty() || selectedMediaFiles.isNotEmpty()
            if (hasInput) {
                showDiscardPostDialog = true
            } else {
                context.closePageWithResult(Activity.RESULT_CANCELED)
            }
        }
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
                    getPageScope().dismissSnackbar()
                    showPendingPostDialog = true
                }

                is AmityPostCreationEvent.Failed -> {
                    getPageScope().dismissSnackbar()
                    val exception = (postCreationEvent as AmityPostCreationEvent.Failed).throwable
                    val error = AmityError.from(exception)
                    val text = when (error) {
                        AmityError.LINK_NOT_ALLOWED -> context.getString(R.string.amity_add_blocked_links_post_error_message)
                        AmityError.BAN_WORD_FOUND -> context.getString(R.string.amity_add_blocked_words_post_error_message)
                        else -> if (exception is TextPostExceedException) context.getString(
                            R.string.amity_post_text_exceed_error_message,
                            exception.charLimit
                        )
                        else if (isInEditMode || isEditClipMode) context.getString(R.string.amity_post_edit_generic_error_message) else context.getString(
                            R.string.amity_post_create_generic_error_message
                        )
                    }
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = text,
                        offsetFromBottom = 52
                    )
                }

                AmityPostCreationEvent.Initial -> {}
            }
        }

        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (header, titleField, content, media, suggestions, attachment, clipThumbnail) = createRefs()

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(AmityTheme.colors.background)
                    .padding(start = 12.dp, end = 16.dp)
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
                        painter = if (isCreateClipMode) painterResource(R.drawable.amity_ic_back) else painterResource(
                            getConfig().getIcon()
                        ),
                        contentDescription = null,
                        tint = AmityTheme.colors.base,
                        modifier = modifier
                            .align(Alignment.CenterStart)
                            .size(18.dp)
                            .padding(2.dp)
                            .clickableWithoutRipple {
                                if (isInEditMode) {
                                    val hasTitleChanged = titleText.trim() != postTitle.trim()
                                    val hasTextChanged = localPostText.trim() != postBodyText.trim()
                                    val hasAttachmentsChanged =
                                        post?.getChildren()?.size != selectedMediaFiles.size
                                    val hasEdited =
                                        hasTitleChanged || hasTextChanged || hasAttachmentsChanged
                                    if (hasEdited) {
                                        showDiscardPostDialog = true
                                    } else {
                                        context.closePageWithResult(Activity.RESULT_CANCELED)
                                    }
                                } else if (isEditClipMode) {
                                    val hasEdited = localPostText != postBodyText
                                    if (hasEdited) {
                                        showDiscardPostDialog = true
                                    } else {
                                        context.closePageWithResult(Activity.RESULT_CANCELED)
                                    }
                                } else {
                                    val hasInput =
                                        titleText.trim().isNotEmpty() || localPostText.trim()
                                            .isNotEmpty() || selectedMediaFiles.isNotEmpty()
                                    if (hasInput) {
                                        showDiscardPostDialog = true
                                    } else {
                                        context.closePageWithResult(Activity.RESULT_CANCELED)
                                    }
                                }
                            }
                            .testTag(getAccessibilityId()),
                    )
                }

                val title = when (options) {
                    is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                        if (options.targetType == AmityPostTargetType.USER) {
                            if (options.targetId == AmityCoreClient.getUserId()) {
                                "My timeline"
                            } else {
                                options.targetId ?: ""
                            }
                        } else {
                            options.community?.getDisplayName() ?: "Group"
                        }
                    }

                    is AmityPostComposerOptions.AmityPostComposerCreateClipOptions -> {
                        if (options.targetType == AmityPostTargetType.USER) {
                            if (options.targetId == AmityCoreClient.getUserId()) {
                                "My timeline"
                            } else {
                                options.targetId ?: ""
                            }
                        } else {
                            options.community?.getDisplayName() ?: "Group"
                        }
                    }

                    is AmityPostComposerOptions.AmityPostComposerEditOptions,
                    is AmityPostComposerOptions.AmityPostComposerEditClipOptions,
                        -> {
                        "Edit Post"
                    }
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = if (isInEditMode || isEditClipMode) "edit_post_title" else "community_display_name"
                ) {
                    Text(
                        text = if (isInEditMode || isEditClipMode) getConfig().getText() else title,
                        style = AmityTheme.typography.title,
                        modifier = modifier
                            .align(Alignment.Center)
                            .padding(vertical = 16.dp, horizontal = 40.dp)
                            .testTag(getAccessibilityId()),
                    )
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = if (isInEditMode || isEditClipMode) "edit_post_button" else "create_new_post_button"
                ) {
                    Text(
                        text = if (isInEditMode || isEditClipMode) "Save" else getConfig().getText(),
                        style = AmityTheme.typography.body.copy(
                            color = if (shouldAllowToPost) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                        ),
                        modifier = modifier
                            .align(Alignment.CenterEnd)
                            .clickableWithoutRipple(enabled = shouldAllowToPost) {
                                if (isInEditMode || isEditClipMode) {
                                    // For edit mode, pass title and text separately
                                    viewModel.updatePost(
                                        postText = localPostText.trim(),
                                        postTitle = titleText.trim(),
                                        mentionedUsers = mentionedUsers,
                                        hashtags = hashtags,
                                    )
                                } else {
                                    viewModel.createPost(
                                        postText = localPostText.trim(),
                                        postTitle = titleText.trim(),
                                        mentionedUsers = mentionedUsers,
                                        hashtags = hashtags,
                                    )
                                }
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
            }

            if (!(isEditClipMode || isCreateClipMode)) {
                // Title field
                BasicTextField(
                    value = titleText,
                    onValueChange = {
                        // Remove line breaks and convert them to spaces
                        val cleanedText = it.replace(Regex("[\r\n]+"), " ")
                        if (cleanedText.length <= maxTitleChar) {
                            titleText = cleanedText
                        } else {
                            // [Optional] provide feedback to the user that the limit is reached
                        }
                    },
                    textStyle = AmityTheme.typography.titleBold.copy(
                        color = AmityTheme.colors.base,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    cursorBrush = SolidColor(AmityTheme.colors.primary),
                    decorationBox = { innerTextField ->
                        if (titleText.isEmpty()) {
                            Text(
                                text = "Title (Optional)",
                                style = AmityTheme.typography.titleBold.copy(
                                    color = AmityTheme.colors.baseShade3,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                        innerTextField()
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp)
                        .constrainAs(titleField) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }

            //Show clip thumbnail only in create clip mode
            if (isCreateClipMode) {
                val aspect =
                    (options as AmityPostComposerOptions.AmityPostComposerCreateClipOptions).aspectRatio
                Box(
                    modifier = Modifier
                        .constrainAs(clipThumbnail) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            centerHorizontallyTo(header)
                        }
                        .padding(top = 16.dp)
                        .height(142.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    AmityStoryVideoPlayer(
                        exoPlayer = exoPlayer,
                        isVisible = true,
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp)),
                        scaleMode = if (aspect == AmityClip.DisplayMode.FIT) AspectRatioFrameLayout.RESIZE_MODE_FIT else AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                            .background(
                                color = Color(0x88000000),
                                shape = CircleShape
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_play_v4),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            } else if (isEditClipMode) {
                AmityClipAttachmentElement(
                    post = post,
                    modifier = Modifier
                        .constrainAs(clipThumbnail) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            centerHorizontallyTo(header)
                        }
                        .padding(top = 16.dp)
                )
            }

            if (isInEditMode || isEditClipMode) {
                if (post != null) {
                    AmityMentionTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                            .constrainAs(content) {
                                top.linkTo(if (isEditClipMode) clipThumbnail.bottom else titleField.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        value = localPostText,
                        maxLines = 30,
                        hintText = "Share something...",
                        mentionedUser = selectedUserToMention,
                        mentionMetadata = mentionGetter.getMentionedUsers(),
                        mentionees = post?.getMentionees() ?: emptyList(),
                        hashtagMetadata = hashtagGetter.getHashtags(),
                        textStyle = AmityTheme.typography.body.copy(
                            color = AmityTheme.colors.base,
                            fontSize = 16.sp  // Match original post composer font size
                        ),
                        contentPadding = PaddingValues(0.dp), // Post composer has minimal padding
                        verticalPadding = 0.dp,
                        horizontalPadding = 0.dp,
                        backgroundColor = Color.Transparent,
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
                        onHashtags = {
                            hashtags = it
                        },
                    )
                }
            } else {
                AmityMentionTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .constrainAs(content) {
                            top.linkTo(if (isCreateClipMode) clipThumbnail.bottom else titleField.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    value = localPostText,
                    maxLines = 30,
                    hintText = "Share something...",
                    mentionedUser = selectedUserToMention,
                    textStyle = AmityTheme.typography.body.copy(
                        color = AmityTheme.colors.base,
                        fontSize = 16.sp  // Match original post composer font size
                    ),
                    contentPadding = PaddingValues(0.dp), // Post composer has minimal padding
                    verticalPadding = 0.dp,
                    horizontalPadding = 0.dp,
                    backgroundColor = Color.Transparent,
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
                    onHashtags = {
                        hashtags = it
                    },
                )
            }

            if (shouldShowSuggestion) {
                AmityMentionSuggestionView(
                    heightIn = 220.dp, // Match the height we used for comments
                    shape = RoundedCornerShape(8.dp), // Apply rounded corners
                    community = viewModel.community,
                    keyword = queryToken,
                    modifier = Modifier
                        .constrainAs(suggestions) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(
                                if (isCreateClipMode || isEditClipMode) {
                                    parent.bottom
                                } else {
                                    attachment.top
                                }
                            )
                        }
                        .let { baseModifier ->
                            if ((isCreateClipMode || isEditClipMode) && isKeyboardOpen) {
                                baseModifier.padding(bottom = keyboardHeight)
                            } else {
                                baseModifier
                            }
                        },
                ) {
                    selectedUserToMention = it
                    shouldShowSuggestion = false
                }
            }

            if (options is AmityPostComposerOptions.AmityPostComposerCreateOptions ||
                options is AmityPostComposerOptions.AmityPostComposerEditOptions
            ) {
                // Show existing videos from post children in edit mode
                if (isInEditMode && post != null) {
                    val videoChildren = post!!.getChildren().filter { child ->
                        child.getData() is AmityPost.Data.VIDEO
                    }

                    if (videoChildren.isNotEmpty()) {
                        AmityExistingVideoDisplay(
                            videoChildren = videoChildren,
                            modifier = Modifier
                                .constrainAs(media) {
                                    top.linkTo(content.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )
                    }
                } else {
                    // Show regular selected media component for create mode or non-video posts
                    AmitySelectedMediaComponent(
                        modifier = Modifier
                            .constrainAs(media) {
                                top.linkTo(content.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }

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
                            AmityPostMedia.Type.ClIP -> AmityPostAttachmentPickerEvent.OpenVideoCamera
                        }
                    )
                }
            }
        }

        if (showMaxUploadLimitReachedDialog) {
            val mediaType = when (maxUploadLimitMediaType) {
                AmityPostMedia.Type.IMAGE -> "images"
                AmityPostMedia.Type.VIDEO -> "videos"
                else -> ""
            }
            val limit = when (maxUploadLimitMediaType) {
                AmityPostMedia.Type.VIDEO -> MEDIA_VIDEO_UPLOAD_LIMIT
                else -> MEDIA_IMAGE_UPLOAD_LIMIT
            }

            AmityAlertDialog(
                dialogTitle = "Maximum upload limit reached",
                dialogText = "You’ve reached the upload limit of $limit $mediaType. Any additional $mediaType will not be saved.",
                dismissText = "Close",
            ) {
                showMaxUploadLimitReachedDialog = false
            }
        }

        if (showDiscardPostDialog) {
            AmityAlertDialog(
                dialogTitle = "Discard changes?",
                dialogText = "Do you want to discard your changes to this post?",
                confirmText = "Discard",
                dismissText = "Keep editing",
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.primary,
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
                dialogText = "Your post has been submitted to the pending list. It will be reviewed by group moderator.",
                dismissText = "OK",
            ) {
                showPendingPostDialog = false
                context.closePageWithResult(Activity.RESULT_OK)
            }
        }

        RenderAltTextConfigSheet(pageScope = getPageScope())
    }


}

fun hasAttachmentsChanged(
    originalIds: List<String>,
    newMedia: List<AmityPostMedia>,
): Boolean {
    // Different sizes => changed
    if (originalIds.size != newMedia.size) return true

    // Compare IDs instead of URIs
    val newIds = newMedia.mapNotNull { it.id }
    return originalIds.toSet() != newIds.toSet()
}

@Composable
fun RenderAltTextConfigSheet(
    pageScope: AmityComposePageScope? = null,
    forcedEditMode: Boolean = false,
    onSuccess: (AmityImage) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val showUpdateAltTextSheet by remember {
        derivedStateOf { viewModel.shouldShowAltTextConfigSheet() }
    }

    val media by remember { derivedStateOf { viewModel.getAltTextMedia() } }

    val altText = if (media is AltTextMedia.Image) {
        (media as? AltTextMedia.Image)?.image?.getAltText()
    } else {
        null
    }
    media?.let { m ->
        if (showUpdateAltTextSheet) {
            val mode = if (altText != null || forcedEditMode) {
                AltTextConfigMode.Edit(altText ?: "", m)
            } else {
                AltTextConfigMode.Create(m)
            }
            AmityAltTextConfigComponent(
                pageScope = pageScope,
                mode = mode,
                result = {
                    onSuccess(it)
                }
            ) {
                viewModel.hideAltTextConfigSheet()
                viewModel.setAltTextMedia(null)
                onDismiss()
            }
        }
    }

}

@Composable
fun AmityExistingVideoDisplay(
    videoChildren: List<AmityPost>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (videoChildren.size == 1) 1 else if (videoChildren.size == 2) 2 else 3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(videoChildren.size) { index ->
                val videoPost = videoChildren[index]
                val videoData = videoPost.getData() as AmityPost.Data.VIDEO

                AmityExistingVideoItem(
                    videoData = videoData,
                    modifier = Modifier.aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun AmityExistingVideoItem(
    videoData: AmityPost.Data.VIDEO,
    modifier: Modifier = Modifier
) {
    val thumbnailUrl = videoData.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)

    Box(
        modifier = modifier
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
    ) {
        if (thumbnailUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
            )
            // Add play button overlay using the same component as AmityPostMediaElement
            AmityPostMediaPlayButton(
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}