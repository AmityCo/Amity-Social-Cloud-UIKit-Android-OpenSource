@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.amity.socialcloud.uikit.community.compose.post.composer

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
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
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
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
import com.amity.socialcloud.sdk.model.core.link.AmityLink
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityPreviewLinkViewWithMetadata
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextConfigMode
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityAltTextConfigComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmitySelectedMediaComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityClipAttachmentElement
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaAttachmentElement
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityMediaCameraSelectionSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaPlayButton
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.product.AmityProductTagSelectionComponent
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityFloatingMentionSuggestionCard
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionTextField
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.ProductMentionData
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.UrlHighlight
import com.google.gson.JsonObject

@OptIn(UnstableApi::class)
@UnstableApi
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
    var selectedProductToMention by remember { mutableStateOf<AmityProduct?>(null) }

    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata.USER>>(emptyList()) }
    var hashtags by remember { mutableStateOf<List<AmityHashtag>>(emptyList()) }
    var productMentions by remember { mutableStateOf<List<ProductMentionData>>(emptyList()) }

    // Product catalogue setting
    var isProductCatalogueEnabled by remember { mutableStateOf(false) }

    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var showMaxUploadLimitReachedDialog by remember { mutableStateOf(false) }
    var maxUploadLimitMediaType by remember { mutableStateOf<AmityPostMedia.Type?>(null) }
    var showDiscardPostDialog by remember { mutableStateOf(false) }
    var showPendingPostDialog by remember { mutableStateOf(false) }
    var showLinkLimitDialog by remember { mutableStateOf(false) }
    var showProductSelectionDialog by remember { mutableStateOf(false) }
    var showProductCatalogueDisabledDialog by remember { mutableStateOf(false) }
    var showProductDiscardConfirmationDialog by remember { mutableStateOf(false) }
    var selectedMediaForTagging by remember { mutableStateOf<AmityPostMedia?>(null) }
    var hasUnsavedProductTagChanges by remember { mutableStateOf(false) }
    var showAllProductTagsDialog by remember { mutableStateOf(false) }
    var showProductLimitDialog by remember { mutableStateOf(false) }
    var selectedProductForWebView by remember { mutableStateOf<AmityProduct?>(null) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val productTagSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue ->
            if (sheetValue == SheetValue.Hidden && hasUnsavedProductTagChanges) {
                showProductDiscardConfirmationDialog = true
                false
            } else {
                true
            }
        }
    )
    val allTagsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    // Fetch product catalogue settings once when page opens
    LaunchedEffect(Unit) {
        AmityCoreClient.getProductCatalogueSetting()
            .subscribe(
                { settings -> isProductCatalogueEnabled = settings.enabled },
                { /* Ignore error, keep default false */ }
            )
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

    // Get product tags from existing post for edit mode
    // Use post from ViewModel to ensure we get updated data after post is loaded
    // Map product objects from post.getProducts() to each tag for full product data
    val productTagsForText by remember(post) {
        derivedStateOf {
            if (isInEditMode || isEditClipMode) {
                val textTags = post?.getProductTags()?.filterIsInstance<AmityProductTag.Text>() ?: emptyList()
                val productsMap = post?.getProducts()?.associateBy { it.getProductId() } ?: emptyMap()

                // Create new tags with product objects from getProducts()
                textTags.map { tag ->
                    AmityProductTag.Text(
                        productId = tag.productId,
                        text = tag.text,
                        index = tag.index,
                        length = tag.length,
                        product = productsMap[tag.productId] ?: tag.product
                    )
                }
            } else {
                emptyList()
            }
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

    // Track initial link preview state in edit mode
    val initialLinkPreviewWasShown = remember {
        if (options is AmityPostComposerOptions.AmityPostComposerEditOptions) {
            val initialLinks = options.post.getLinks()?.toList() ?: emptyList()
            initialLinks.any { it.getRenderPreview() }
        } else {
            false
        }
    }

    // Initialize detectedUrls from post's existing links in edit mode
    LaunchedEffect(post) {
        if (post != null) {
            viewModel.updateDetectedUrls(listOf())
            /* Remove until support hyperlink edit
            val initialLinks = post?.getLinks()?.toList() ?: emptyList()
            if (initialLinks.isNotEmpty()) {
                viewModel.updateDetectedUrls(initialLinks)
            }*/
        }
    }

    // Get link preview state from ViewModel
    val detectedUrls by viewModel.detectedUrls.collectAsState()
    val previewMetadata by viewModel.linkPreviewMetadata.collectAsState()
    val isLinkPreviewDismissed by viewModel.isLinkPreviewDismissed.collectAsState()

    // Title field state
    var titleText by remember { mutableStateOf(postTitle) }

    val postAttachmentPickerEvent by viewModel.postAttachmentPickerEvent.collectAsState()
    val postCreationEvent by viewModel.postCreationEvent.collectAsState()
    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()
    val isAllMediaSuccessfullyUploaded by viewModel.isAllMediaSuccessfullyUploaded.collectAsState()
    val currentMediaProductTags by viewModel.mediaProductTags.collectAsState()
    val currentTextProductTags by viewModel.textProductTags.collectAsState()
    val originalMediaProductTagIds: Map<String, Set<String>> by viewModel.originalMediaProductTagIds.collectAsState()
    val originalTextProductTagIds: List<String> by viewModel.originalTextProductTagIds.collectAsState()
    val totalDistinctProductTagCount by viewModel.totalDistinctProductTagCount.collectAsState(initial = 0)

    val shouldAllowToPost by remember(
        isInEditMode,
        isEditClipMode,
        titleText,
        localPostText,
        selectedMediaFiles,
        isAllMediaSuccessfullyUploaded,
        postCreationEvent,
        previewMetadata,
        detectedUrls.size,
        isLinkPreviewDismissed,
        initialLinkPreviewWasShown,
        currentMediaProductTags,
        currentTextProductTags,
        originalMediaProductTagIds,
        originalTextProductTagIds
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

        // Check if we're waiting for link preview metadata
        val isWaitingForLinkMetadata = detectedUrls.isNotEmpty() &&
                !isLinkPreviewDismissed &&
                previewMetadata == null

        derivedStateOf {
            if (isOperationInProgress) {
                // Disable button during operations
                false
            } else if (isWaitingForLinkMetadata) {
                // Disable button while fetching link preview metadata
                false
            } else if (isInEditMode) {
                // Edit mode validation
                val hasTitleChanged = titleText.trim() != postTitle.trim()
                val hasTextChanged = localPostText.trim() != postBodyText.trim()
                val hasAttachmentsChanged =
                    hasAttachmentsChanged(originalAttachmentIds.value, selectedMediaFiles)
                val hasAttachmentProductTagsChanged = run {
                    val currentMediaTagIds = currentMediaProductTags.mapValues { entry ->
                        entry.value.map { it.getProductId() }.toSet()
                    }
                    val currentTextTagIds = currentTextProductTags.map { it.getProductId() }
                    currentMediaTagIds != originalMediaProductTagIds ||
                            currentTextTagIds != originalTextProductTagIds
                }

                // Check if link preview state changed:
                // - Case 1: Preview was shown initially, then dismissed
                // - Case 2: Preview was not shown initially, but now will be shown (not dismissed and has metadata or waiting for it)
                val currentlyShowingPreview = detectedUrls.isNotEmpty() && !isLinkPreviewDismissed
                val hasLinkPreviewChanged = (initialLinkPreviewWasShown && isLinkPreviewDismissed) ||
                                           (!initialLinkPreviewWasShown && currentlyShowingPreview)

                val isContentValid = isContentReady(titleText, localPostText, selectedMediaFiles)

                (hasTitleChanged || hasTextChanged || hasAttachmentsChanged || hasLinkPreviewChanged || hasAttachmentProductTagsChanged) && isContentValid
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

    LaunchedEffect(selectedMediaFiles) {
        if (selectedMediaFiles.isEmpty()) {
            existingAttachmentType = null
        } else {
            // Update to the type of the first selected media
            existingAttachmentType = selectedMediaFiles.first().type
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
            // Automatically dismiss link preview when media is added
            viewModel.dismissLinkPreview()
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

        // Replaces the old ConstraintLayout structure with Box/Column primitives.
        Box(modifier = modifier.fillMaxSize()) {
            var attachmentHeightPx by remember { mutableStateOf(0) }
            val attachmentHeightDp = with(androidx.compose.ui.platform.LocalDensity.current) {
                attachmentHeightPx.toDp()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.background)
            ) {
                // Header (pinned to top)
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.background)
                        .padding(start = 12.dp, end = 16.dp)
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
                        style = AmityTheme.typography.titleBold,
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
                                // Check if URL count exceeds limit before posting
                                if (detectedUrls.size > 100) {
                                    showLinkLimitDialog = true
                                    return@clickableWithoutRipple
                                }

                                // leading chars removed by trim() — all stored indices must be adjusted
                                val trimOffset = localPostText.length - localPostText.trimStart().length

                                if (isInEditMode || isEditClipMode) {
                                    // For edit mode, pass title and text separately
                                    // Build product tags from productMentions
                                    val productTags = productMentions.map {
                                        AmityProductTag.Text(
                                            productId = it.productId,
                                            text = it.displayName,
                                            index = it.startPosition - trimOffset,
                                            length = it.length,
                                        )
                                    }
                                    // Build attachmentProductTags from ViewModel state
                                    val attachmentProductTags = viewModel.buildAttachmentProductTags()
                                    viewModel.updatePost(
                                        postText = localPostText.trim(),
                                        postTitle = titleText.trim(),
                                        mentionedUsers = mentionedUsers,
                                        hashtags = hashtags.parseHashtagIndices(localPostText),
                                        links = detectedUrls,
                                        productTags = if (isProductCatalogueEnabled) productTags else null,
                                        attachmentProductTags = if (isProductCatalogueEnabled) attachmentProductTags else null,
                                    )
                                } else {
                                    // Check if post has product tags (text or attachment)
                                    val hasProductTags = productMentions.isNotEmpty() || viewModel.hasAttachmentProductTags()

                                    if (hasProductTags) {
                                        // Re-check product catalogue setting before creating post
                                        AmityCoreClient.getProductCatalogueSetting()
                                            .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                                            .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                                            .doOnSuccess { settings ->
                                                if (settings.enabled) {
                                                    // Product catalogue still enabled, create post normally
                                                    val productTags = productMentions.map {
                                                        AmityProductTag.Text(
                                                            productId = it.productId,
                                                            text = it.displayName,
                                                            index = it.startPosition - trimOffset,
                                                            length = it.length,
                                                        )
                                                    }
                                                    val attachmentProductTags = viewModel.buildAttachmentProductTags()
                                                    viewModel.createPost(
                                                        postText = localPostText.trim(),
                                                        postTitle = titleText.trim(),
                                                        mentionedUsers = mentionedUsers,
                                                        hashtags = hashtags.parseHashtagIndices(localPostText),
                                                        links = detectedUrls,
                                                        productTags = productTags,
                                                        attachmentProductTags = attachmentProductTags,
                                                    )
                                                } else {
                                                    // Product catalogue disabled, update flag and show dialog
                                                    isProductCatalogueEnabled = false
                                                    showProductCatalogueDisabledDialog = true
                                                }
                                            }
                                            .doOnError {
                                                // On error, proceed with post creation
                                                val productTags = productMentions.map {
                                                    AmityProductTag.Text(
                                                        productId = it.productId,
                                                        text = it.displayName,
                                                        index = it.startPosition - trimOffset,
                                                        length = it.length,
                                                    )
                                                }
                                                val attachmentProductTags = viewModel.buildAttachmentProductTags()
                                                viewModel.createPost(
                                                    postText = localPostText.trim(),
                                                    postTitle = titleText.trim(),
                                                    mentionedUsers = mentionedUsers,
                                                    hashtags = hashtags.parseHashtagIndices(localPostText),
                                                    links = detectedUrls,
                                                    productTags = emptyList(),
                                                    attachmentProductTags = null,
                                                )
                                            }
                                            .subscribe()
                                    } else {
                                        // No product tags, create post normally
                                        viewModel.createPost(
                                            postText = localPostText.trim(),
                                            postTitle = titleText.trim(),
                                            mentionedUsers = mentionedUsers,
                                            hashtags = hashtags,
                                            links = detectedUrls,
                                        )
                                    }
                                }
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
            }

            // Body (fills remaining space, padded so bottom attachment never covers content)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = attachmentHeightDp)
            ) {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (!(isEditClipMode || isCreateClipMode)) {
                        item {
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
                )
            }
        }

        //Show clip thumbnail only in create clip mode
        if (isCreateClipMode) {
            item {
                val aspect =
                    (options as AmityPostComposerOptions.AmityPostComposerCreateClipOptions).aspectRatio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
            }
        } else if (isEditClipMode) {
            item {
                AmityClipAttachmentElement(
                    post = post,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        if (isInEditMode || isEditClipMode) {
            if (post != null) {
                item {
                    AmityMentionTextField(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        value = localPostText,
                        maxLines = 30,
                        hintText = "What's on your mind?",
                        mentionedUser = selectedUserToMention,
                        mentionedProduct = selectedProductToMention,
                        mentionMetadata = mentionGetter.getMentionedUsers(),
                        mentionees = post?.getMentionees() ?: emptyList(),
                        productMetadata = productTagsForText, // Pass product tags for edit mode
                        hashtagMetadata = hashtagGetter.getHashtags(),
                        totalProductTagCount = totalDistinctProductTagCount,
                        maxProductTags = AmityPostComposerPageViewModel.MAX_TOTAL_PRODUCT_TAGS,
                        onProductTagLimitReached = {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                message = "You can only tag up to ${AmityPostComposerPageViewModel.MAX_TOTAL_PRODUCT_TAGS} products per post."
                            )
                        },
                        textStyle = AmityTheme.typography.body.copy(
                            color = AmityTheme.colors.base,
                            fontSize = 16.sp  // Match original post composer font size
                        ),
                        contentPadding = PaddingValues(0.dp), // Post composer has minimal padding
                        verticalPadding = 0.dp,
                        horizontalPadding = 0.dp,
                        backgroundColor = Color.Transparent,
                        enableUrlHighlighting = true,
                        urlColor = AmityTheme.colors.primary,
                        onValueChange = {
                            localPostText = it
                            if (selectedProductToMention != null) selectedProductToMention = null
                        },
                        onUrlsDetected = { urls ->
                            // Convert UrlHighlight to AmityLink
                            // This callback is already debounced by AmityMentionTextField (2 seconds)
                            if (urls.isNotEmpty()) {
                                val links = urls.mapNotNull { urlHighlight ->
                                    try {
                                        AmityLink(
                                            index = urlHighlight.start,
                                            length = urlHighlight.end - urlHighlight.start,
                                            url = urlHighlight.url,
                                            renderPreview = true,
                                            domain = null,
                                            title = null,
                                            imageUrl = null
                                        )
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                                viewModel.updateDetectedUrls(links)
                            } else if (detectedUrls.isNotEmpty() && previewMetadata != null) {
                                // Preserve existing link with metadata when text is removed
                                viewModel.preserveUrlWithMetadata()
                            }
                        },
                        onMentionAdded = {
                            selectedUserToMention = null
                        },
                        onQueryToken = {
                            queryToken = it ?: ""
                            shouldShowSuggestion = (it != null) // kept for backward compatibility; popup is handled by text field
                        },
                        onUserMentions = {
                            mentionedUsers = it
                        },
                        onHashtags = {
                            hashtags = it
                        },
                        onProductMentions = {
                            productMentions = it
                            // Update text product tags in viewModel for allDistinctTags aggregation
                            // Include startPosition for sorting by index in text
                            viewModel.setTextProductTags(
                                it.mapNotNull { mention ->
                                    mention.product?.let { product -> mention.startPosition to product }
                                }
                            )
                        },
                        mentionSuggestions = { dismiss ->
                             AmityFloatingMentionSuggestionCard(
                                 modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 220.dp),
                                 community = viewModel.community,
                                 keyword = queryToken,
                                 productEnabled = isProductCatalogueEnabled,
                                 alreadyTaggedProductIds = productMentions.map { it.productId }.toSet() +
                                     currentMediaProductTags.values.flatten().map { it.getProductId() }.toSet(),
                                 onDismiss = dismiss,
                                 onUserSelected = { user: AmityUser ->
                                     selectedUserToMention = user
                                     // Clear product selection to avoid double-processing.
                                     selectedProductToMention = null
                                     dismiss()
                                  },
                                  onProductSelected = { product: AmityProduct ->
                                      if (!viewModel.canAddMoreProducts()) {
                                          showProductLimitDialog = true
                                          dismiss()
                                      } else {
                                          selectedProductToMention = product
                                          // Clear user selection to avoid double-processing.
                                          selectedUserToMention = null
                                          dismiss()
                                      }
                                  },
                              )
                          },
                     )
                }
            }
        } else {
            item {
                AmityMentionTextField(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    value = localPostText,
                    maxLines = 30,
                    hintText = "What's on your mind?",
                    mentionedUser = selectedUserToMention,
                    mentionedProduct = selectedProductToMention,
                    mentionMetadata = mentionGetter.getMentionedUsers(),
                    mentionees = post?.getMentionees() ?: emptyList(),
                    hashtagMetadata = hashtagGetter.getHashtags(),
                    totalProductTagCount = totalDistinctProductTagCount,
                    maxProductTags = AmityPostComposerPageViewModel.MAX_TOTAL_PRODUCT_TAGS,
                    onProductTagLimitReached = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(
                            message = "You can only tag up to ${AmityPostComposerPageViewModel.MAX_TOTAL_PRODUCT_TAGS} products per post."
                        )
                    },
                    textStyle = AmityTheme.typography.body.copy(
                        color = AmityTheme.colors.base,
                        fontSize = 16.sp  // Match original post composer font size
                    ),
                    contentPadding = PaddingValues(0.dp), // Post composer has minimal padding
                    verticalPadding = 0.dp,
                    horizontalPadding = 0.dp,
                    backgroundColor = Color.Transparent,
                    enableUrlHighlighting = true,
                    urlColor = AmityTheme.colors.primary,
                    onValueChange = {
                        localPostText = it
                        if (selectedProductToMention != null) selectedProductToMention = null
                    },
                    onUrlsDetected = { urls ->
                        // Convert UrlHighlight to AmityLink
                        if (urls.isNotEmpty()) {
                            // ViewModel handles debouncing for metadata fetching
                            val links = urls.mapNotNull { urlHighlight ->
                                try {
                                    AmityLink(
                                        index = urlHighlight.start,
                                        length = urlHighlight.end - urlHighlight.start,
                                        url = urlHighlight.url,
                                        renderPreview = false,
                                        domain = null,
                                        title = null,
                                        imageUrl = null
                                    )
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            viewModel.updateDetectedUrls(links)
                        } else if (detectedUrls.isNotEmpty() && previewMetadata != null) {
                            // Preserve existing link with metadata when text is removed
                            viewModel.preserveUrlWithMetadata()
                        }
                    },
                    onMentionAdded = {
                        selectedUserToMention = null
                    },
                    onQueryToken = {
                        queryToken = it ?: ""
                        shouldShowSuggestion = (it != null) // kept for backward compatibility; popup is handled by text field
                    },
                    onUserMentions = {
                        mentionedUsers = it
                    },
                    onHashtags = {
                        hashtags = it
                    },
                    onProductMentions = {
                        productMentions = it
                        // Update text product tags in viewModel for allDistinctTags aggregation
                        // Include startPosition for sorting by index in text
                        viewModel.setTextProductTags(
                            it.mapNotNull { mention ->
                                mention.product?.let { product -> mention.startPosition to product }
                            }
                        )
                    },
                    mentionSuggestions = { dismiss ->
                         AmityFloatingMentionSuggestionCard(
                             modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp),
                             community = viewModel.community,
                             keyword = queryToken,
                             productEnabled = isProductCatalogueEnabled,
                             alreadyTaggedProductIds = productMentions.map { it.productId }.toSet() +
                                 currentMediaProductTags.values.flatten().map { it.getProductId() }.toSet(),
                             onDismiss = dismiss,
                             onUserSelected = { user: AmityUser ->
                                 selectedUserToMention = user
                                 dismiss()
                              },
                              onProductSelected = { product: AmityProduct ->
                                  if (!viewModel.canAddMoreProducts()) {
                                      showProductLimitDialog = true
                                      dismiss()
                                  } else {
                                      selectedProductToMention = product
                                      selectedUserToMention = null
                                      dismiss()
                                  }
                              },
                          )
                      },
                )
            }
        }

        // Display link preview for the first detected URL or previously loaded preview
        // Check if we have valid metadata to show (any of domain/title/imageUrl)
        val firstLink = detectedUrls.firstOrNull()
        val domain = firstLink?.getDomain() ?: previewMetadata?.getDomain()
        val title = firstLink?.getTitle() ?: previewMetadata?.getTitle()
        val imageUrl = firstLink?.getImageUrl() ?: previewMetadata?.getImageUrl()
        val hasValidMetadata = !domain.isNullOrEmpty() || !title.isNullOrEmpty() || !imageUrl.isNullOrEmpty()
        val isLoadingMetadata = detectedUrls.isNotEmpty() && previewMetadata == null && !isLinkPreviewDismissed



        if ((detectedUrls.isNotEmpty() || previewMetadata != null) && !isLinkPreviewDismissed && (hasValidMetadata || isLoadingMetadata) && !isInEditMode) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (isLoadingMetadata) {
                            // Loading state with shimmer
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = AmityTheme.colors.baseShade4,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                // Image shimmer
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .shimmerBackground(
                                            color = AmityTheme.colors.baseShade4,
                                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                        )
                                )

                                // Text content shimmer
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.8f)
                                            .height(16.dp)
                                            .shimmerBackground(
                                                color = AmityTheme.colors.baseShade4,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.4f)
                                            .height(14.dp)
                                            .shimmerBackground(
                                                color = AmityTheme.colors.baseShade4,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                            }
                        } else {
                            // Display link preview using AmityPreviewLinkViewWithMetadata
                            val url = firstLink?.getUrl() ?: ""

                            AmityPreviewLinkViewWithMetadata(
                                modifier = Modifier,
                                url = url,
                                domain = domain,
                                title = title,
                                imageUrl = imageUrl
                            )
                        }

                        // Floating close button on top of the entire preview
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = -8.dp)
                                .size(32.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .clickableWithoutRipple {
                                    viewModel.dismissLinkPreview()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_dismiss_preview),
                                contentDescription = "Remove link preview",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }


        if (options is AmityPostComposerOptions.AmityPostComposerCreateOptions ||
            options is AmityPostComposerOptions.AmityPostComposerEditOptions
        ) {
            item {
                AmitySelectedMediaComponent(
                    modifier = Modifier.fillMaxWidth(),
                    isProductCatalogueEnabled = isProductCatalogueEnabled,
                    onTagProductClick = { media ->
                        selectedMediaForTagging = media
                        showProductSelectionDialog = true
                        hasUnsavedProductTagChanges = false
                    }
                )
            }
        }
    } // Close LazyColumn
    } // Close Body Box
    } // Close Column

    // Product tags button - positioned above bottom bar at right (OUTSIDE Column, INSIDE root Box)
    if (options is AmityPostComposerOptions.AmityPostComposerCreateOptions ||
        options is AmityPostComposerOptions.AmityPostComposerEditOptions
    ) {
        val allDistinctTags by viewModel.allDistinctProductTags.collectAsState(initial = emptyList())
        if (allDistinctTags.isNotEmpty()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = attachmentHeightDp + 2.dp)
                .clickableWithoutRipple { showAllProductTagsDialog = true },
        ) {
            // Circular background with icon - light gray color
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_product_tag),
                    contentDescription = "Product tags",
                    tint = Color(0xFF636878),
                    modifier = Modifier.size(32.dp)
                )
            }

            // Badge with count at top right - dark gray color
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(20.dp)
                    .background(
                        color = AmityTheme.colors.base,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = allDistinctTags.size.toString(),
                    style = AmityTheme.typography.captionBold.copy(
                        color = Color.White,
                        fontSize = 10.sp
                    ),
                )
            }
        }
        }
    }

    // Attachment bar - positioned at bottom center (OUTSIDE Column, INSIDE root Box)
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .onSizeChanged { size ->
                attachmentHeightPx = size.height
            }
    ) {
        AmityMediaAttachmentElement(
            modifier = Modifier.fillMaxWidth(),
            pageScope = getPageScope(),
        )
    }
    } // Close root Box

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
        dialogText = "You've reached the upload limit of $limit $mediaType. Any additional $mediaType will not be saved.",
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

    if (showLinkLimitDialog) {
        AmityAlertDialog(
            dialogTitle = "Link limit reached",
            dialogText = "You can only add link up to 100 links per post.",
            dismissText = "OK",
        ) {
            showLinkLimitDialog = false
        }
    }

    if (showProductCatalogueDisabledDialog) {
        AmityAlertDialog(
            dialogTitle = "Unable to tag products",
            dialogText = "Product tagging is currently not available. Would you like to publish this post without tagging any products?",
            confirmText = "Publish",
            dismissText = "Review post",
            confirmTextColor = AmityTheme.colors.primary,
            dismissTextColor = AmityTheme.colors.baseShade1,
            onConfirmation = {
                showProductCatalogueDisabledDialog = false
                // Create post without product tags
                viewModel.createPost(
                    postText = localPostText.trim(),
                    postTitle = titleText.trim(),
                    mentionedUsers = mentionedUsers,
                    hashtags = hashtags,
                    links = detectedUrls,
                    productTags = null, // Don't pass any product tags since catalogue is disabled
                    attachmentProductTags = null,
                )
            },
            onDismissRequest = {
                // Just dismiss dialog, let user review post
                showProductCatalogueDisabledDialog = false
            }
        )
    }

    if (showProductLimitDialog) {
        AmityAlertDialog(
            dialogTitle = "Product tag limit reached",
            dialogText = "You can only tag up to 20 products per post.",
            dismissText = "OK",
            onDismissRequest = { showProductLimitDialog = false }
        )
    }

    if (showProductSelectionDialog && selectedMediaForTagging != null) {
        val mediaId = selectedMediaForTagging?.id ?: ""
        val existingTags = viewModel.getProductTagsForMedia(mediaId)
        val savedLabel = stringResource(R.string.amity_v4_product_already_tagged)
        // Products tagged elsewhere (text mentions + other media) — shown as "Already tagged"
        val existingTagIds = existingTags.map { it.getProductId() }.toSet()
        val otherTaggedProducts = (currentTextProductTags + currentMediaProductTags.values.flatten())
            .distinctBy { it.getProductId() }
            .filter { it.getProductId() !in existingTagIds }

        // Delegate slot calculation to ViewModel: respects both the per-media cap (5)
        // and the global post limit (20), excluding products already on this media
        // from the "other media" count so they can be freely re-selected.
        val remainingSlots = viewModel.getMaxSelectionForMedia(mediaId)

        ModalBottomSheet(
            onDismissRequest = {
                if (hasUnsavedProductTagChanges) {
                    showProductDiscardConfirmationDialog = true
                } else {
                    showProductSelectionDialog = false
                    selectedMediaForTagging = null
                    hasUnsavedProductTagChanges = false
                }
            },
            sheetState = productTagSheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.navigationBars },
            modifier = modifier
                .padding(0.dp)
                .statusBarsPadding()
                .semantics {
                },
        ) {
            AmityProductTagSelectionComponent(
                pageScope = getPageScope(),
                selectedProductTags = existingTags,
                savedProducts = otherTaggedProducts,
                savedProductsLabel = savedLabel,
                title = "Tag products",
                maxSelection = remainingSlots,
                instanceKey = "media_$mediaId", // Unique key per media
                onClose = {
                    showProductSelectionDialog = false
                    selectedMediaForTagging = null
                    hasUnsavedProductTagChanges = false
                },
                onDone = { selectedProducts ->
                    selectedMediaForTagging?.id?.let { id ->
                        viewModel.setProductTagsForMedia(id, selectedProducts)
                    }
                    showProductSelectionDialog = false
                    selectedMediaForTagging = null
                    hasUnsavedProductTagChanges = false
                    getPageScope().showSnackbar("Product tags have been added")
                },
                onProductToggled = {
                    hasUnsavedProductTagChanges = true
                },
                onDiscardRequest = {
                    showProductDiscardConfirmationDialog = true
                }
            )
        }
    }

    // Discard product tag dialog (rendered outside ModalBottomSheet)
    if (showProductDiscardConfirmationDialog) {
        AmityAlertDialog(
            dialogTitle = "Discard product tags",
            dialogText = "You have tagged products that haven't been saved yet. If you leave now, your changes will be lost.",
            confirmText = "Discard",
            dismissText = "Keep editing",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.primary,
            onDismissRequest = {
                showProductDiscardConfirmationDialog = false
            },
            onConfirmation = {
                showProductDiscardConfirmationDialog = false
                showProductSelectionDialog = false
                selectedMediaForTagging = null
                hasUnsavedProductTagChanges = false
            },
        )
    }

    // Show all product tags dialog
    if (showAllProductTagsDialog) {
        val allDistinctTags by viewModel.allDistinctProductTags.collectAsState(initial = emptyList())

        AmityProductTagListComponent(
            productTags = allDistinctTags,
            onDismiss = {
                showAllProductTagsDialog = false
            },
            onProductClick = { product ->
                selectedProductForWebView = product
            }
        )
    }

    selectedProductForWebView?.let { product ->
        AmityProductWebViewBottomSheet(
            product = product,
            onDismiss = { selectedProductForWebView = null }
        )
    }

    if (showPendingPostDialog) {
        AmityAlertDialog(
            dialogTitle = "Posts sent for review",
            dialogText = "Your post has been submitted to the pending list. It will be published once approved by the community moderator.",
            dismissText = "OK",
        ) {
            showPendingPostDialog = false
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    RenderAltTextConfigSheet(pageScope = getPageScope())
    } // Close AmityBasePage
} // Close function

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

// shift left hashtags index if localText have leading spaces, because in post composer we trim leading spaces
fun List<AmityHashtag>.parseHashtagIndices(
    localText: String
): List<AmityHashtag> {
    val leadingSpaces = localText.indexOfFirst { !it.isWhitespace() }.let {
        if (it == -1) localText.length else it
    }
    if (leadingSpaces == 0) {
        return this.map { hashtag ->
            // Truncate hashtag text and length to 100 characters maximum
            if (hashtag.getLength() > 100) {
                val truncatedText = hashtag.getText().take(100)
                hashtag.copy(text = truncatedText, length = 100)
            } else {
                hashtag
            }
        }
    }
    return this.map { hashtag ->
        val adjustedHashtag = if (hashtag.getLength() > 100) {
            val truncatedText = hashtag.getText().take(100)
            hashtag.copy(
                text = truncatedText,
                index = hashtag.getIndex() - leadingSpaces,
                length = 100
            )
        } else {
            hashtag.copy(
                index = hashtag.getIndex() - leadingSpaces
            )
        }
        adjustedHashtag
    }
}