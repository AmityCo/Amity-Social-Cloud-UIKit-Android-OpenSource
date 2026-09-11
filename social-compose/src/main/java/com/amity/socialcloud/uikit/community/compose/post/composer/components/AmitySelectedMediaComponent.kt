package com.amity.socialcloud.uikit.community.compose.post.composer.components

import android.graphics.BitmapFactory
import android.util.Log
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.video.VideoFrameDecoder
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.AmityMediaRatio
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityProductTagBadge
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AmitySelectedMediaComponent(
    modifier: Modifier = Modifier,
    isProductCatalogueEnabled: Boolean = false,
    // Gates the media-error banner in addition to shouldShowMediaError.
    // Defaults to false so the error section is hidden at all times unless a
    // caller explicitly opts back in with showMediaError = true.
    showMediaError: Boolean = false,
    onTagProductClick: (AmityPostMedia) -> Unit = {},
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()
    val mediaProductTags by viewModel.mediaProductTags.collectAsState()
    val textProductTags by viewModel.textProductTags.collectAsState()

    val isPostProductTagLimitReached by remember(mediaProductTags, textProductTags) {
        derivedStateOf { viewModel.isProductTagLimitReached() }
    }

    val coroutineScope = rememberCoroutineScope()
    val carouselListState = rememberLazyListState()

    // The frame the member tapped to open the full-screen viewer. Captured once, at tap time, and
    // never updated while swiping inside the viewer, because closing must return to THIS frame --
    // the inverse of the published feed, which returns to the LAST frame viewed. Both are
    // deliberate; don't unify them.
    var tappedMedia by remember { mutableStateOf<AmityPostMedia?>(null) }

    val onFrameClick: (AmityPostMedia) -> Unit = { media ->
        tappedMedia = media
    }

    val onFullScreenDismiss: () -> Unit = {
        val tappedKey = tappedMedia?.url?.toString()
        tappedMedia = null
        // The carousel's own scroll position is never touched while the viewer is open (nothing
        // else scrolls it), so the tapped frame is normally still on screen right where it was --
        // this only steps in if it somehow isn't (e.g. removed elsewhere while previewing).
        // Scrolling unconditionally would be wrong: it would snap the frame to the LEADING edge
        // even when it was already visible mid-carousel, moving the view the member never asked
        // to move.
        tappedKey?.let { key ->
            val index = selectedMediaFiles.indexOfFirst { it.url.toString() == key }
            val alreadyVisible = carouselListState.layoutInfo.visibleItemsInfo.any { it.index == index }
            if (index >= 0 && !alreadyVisible) {
                coroutineScope.launch { carouselListState.scrollToItem(index) }
            }
        }
    }

    var containerWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val carouselGap = 8.dp
    val carouselSidePadding = 16.dp
    val carouselVerticalPadding = 12.dp

    // Every frame takes the ratio of the FIRST attachment and holds it, so adding or removing a
    // later image never reshapes the ones already there -- and the preview matches what publishes.
    val lockedRatio = rememberClassifiedMediaRatio(selectedMediaFiles.firstOrNull())

    // Frame width is solved backwards from the measured container so the next frame always
    // peeks by a fixed 31dp (the swipe affordance) instead of hardcoding the 320dp this
    // resolves to at the 375dp reference width.
    val framePeek = 31.dp
    val frameWidth = with(density) {
        if (containerWidth > 0) {
            containerWidth.toDp() - carouselSidePadding - carouselGap - framePeek
        } else {
            0.dp
        }
    }
    val frameHeight = frameWidth / lockedRatio

    val shouldShowMediaError by remember {
        derivedStateOf {
            selectedMediaFiles.any { it.uploadState == AmityFileUploadState.FAILED }
        }
    }

    val uploadFailedPhotoTitle = amitySocialString("amity_social_error_upload_failed_photo_must")
    val uploadFailedVideoTitle = amitySocialString("amity_social_error_upload_failed_video_must")
    val mediaErrorTitle by remember(uploadFailedPhotoTitle, uploadFailedVideoTitle) {
        derivedStateOf {
            if(shouldShowMediaError) {
                if(selectedMediaFiles.firstOrNull()?.type == AmityPostMedia.Type.IMAGE) {
                    uploadFailedPhotoTitle
                } else {
                    uploadFailedVideoTitle
                }
            } else {
                ""
            }
        }
    }
    val mediaErrorMessage by remember {
        derivedStateOf {
            if (shouldShowMediaError) {
                if (selectedMediaFiles.firstOrNull()?.type == AmityPostMedia.Type.IMAGE) {
                    "• Be a JPG or PNG\n" +
                            "• Be under 30MB\n" +
                            "• Not contain offensive or explicit content"
                } else {
                    "• Be in a supported format (3GP, AVI, F4V, FLV, M4V, MOV, MP4, OGV, 3G2, WMV, VOB, WEBM, and MKV).\n" +
                            "• Be under 1GB and 2-hour long.\n" +
                            "• Not contain offensive or explicit content"
                }
            } else {
                ""
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {

        if (shouldShowMediaError && showMediaError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val borderColor = AmityTheme.colors.alert
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .drawBehind {
                        val strokeWidth = 4.dp.toPx() // Border thickness
                        drawLine(
                            color = borderColor,
                            start = center.copy(x = 0f, y = 0f), // Left side start
                            end = center.copy(x = 0f, y = size.height), // Left side end
                            strokeWidth = strokeWidth
                        )
                    }
                    .background(color = AmityTheme.colors.alert.copy(alpha = 0.1f))) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_error),
                        contentDescription = "media_error",
                        modifier = Modifier
                            .padding(start = 24.dp, top = 16.dp)
                            .size(16.dp),
                        tint = AmityTheme.colors.alert
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = mediaErrorTitle,
                            style = AmityTheme.typography.body.copy(
                                color = AmityTheme.colors.base,
                            ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mediaErrorMessage,
                            style = AmityTheme.typography.body.copy(
                                color = AmityTheme.colors.base,
                            ),
                        )
                    }

                }
            }
        }

        if (selectedMediaFiles.size == 1) {
            // Only the carousel chrome goes away for a single attachment — the frame is still
            // full-bleed and still classified, so adding a second image does not restyle the first.
            val media = selectedMediaFiles[0]
            val productTagCount = mediaProductTags[media.id]?.size ?: 0
            Box(modifier = Modifier.fillMaxWidth()) {
                AmitySelectedMediaElement(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(lockedRatio),
                    mediaCornerRadius = 0.dp,
                    media = media,
                    productTagCount = productTagCount,
                    isProductCatalogueEnabled = isProductCatalogueEnabled,
                    isPostProductTagLimitReached = isPostProductTagLimitReached,
                    onRemove = { postMedia ->
                        viewModel.removeMedia(postMedia)
                    },
                    onTagProductClick = onTagProductClick,
                    onClick = onFrameClick,
                )
            }
        } else if (selectedMediaFiles.isNotEmpty()) {
            LazyRow(
                state = carouselListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { containerWidth = it.width },
                contentPadding = PaddingValues(
                    start = carouselSidePadding,
                    top = carouselVerticalPadding,
                    end = carouselSidePadding,
                    bottom = carouselVerticalPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(carouselGap),
                verticalAlignment = Alignment.Top,
                // Snap so a frame always rests at the same offset: the fixed peek only reads as a
                // "there is more" affordance from a consistent position, and returning to a given
                // frame after closing the viewer is only well-defined if frames are addressable.
                flingBehavior = rememberSnapFlingBehavior(carouselListState),
            ) {
                itemsIndexed(selectedMediaFiles, key = { _, it -> it.url.toString() }) { index, media ->
                    val productTagCount = mediaProductTags[media.id]?.size ?: 0
                    val isVideo = media.type != AmityPostMedia.Type.IMAGE
                    AmitySelectedMediaElement(
                        modifier = Modifier
                            .width(frameWidth)
                            .height(frameHeight)
                            .semantics {
                                role = Role.Image
                                contentDescription = if (isVideo) {
                                    "Video ${index + 1} of ${selectedMediaFiles.size}"
                                } else {
                                    "Photo ${index + 1} of ${selectedMediaFiles.size}"
                                }
                            },
                        media = media,
                        productTagCount = productTagCount,
                        isProductCatalogueEnabled = isProductCatalogueEnabled,
                        isPostProductTagLimitReached = isPostProductTagLimitReached,
                        onRemove = { postMedia ->
                            viewModel.removeMedia(postMedia)
                        },
                        onTagProductClick = onTagProductClick,
                        onClick = onFrameClick,
                        // Carousel frames are full-bleed per the design — no tile rounding.
                        mediaCornerRadius = 0.dp,
                        productTagInset = 8.dp,
                    )
                }
            }
        }
    }

    // tapping a frame opens the matching full-screen viewer; both attachment
    // types are mutually exclusive per post, so the tapped frame's type holds
    // for every page in the list.
    tappedMedia?.let { tapped ->
        when (tapped.type) {
            AmityPostMedia.Type.IMAGE -> AmitySelectedMediaPreviewDialog(
                mediaList = selectedMediaFiles,
                initialMediaKey = tapped.url.toString(),
                mediaProductTags = mediaProductTags,
                isProductCatalogueEnabled = isProductCatalogueEnabled,
                onDismiss = onFullScreenDismiss,
                onTagProductClick = onTagProductClick,
            )

            AmityPostMedia.Type.VIDEO, AmityPostMedia.Type.ClIP -> AmitySelectedMediaVideoPlayerPage(
                mediaList = selectedMediaFiles,
                initialMediaKey = tapped.url.toString(),
                mediaProductTags = mediaProductTags,
                isProductCatalogueEnabled = isProductCatalogueEnabled,
                onDismiss = onFullScreenDismiss,
                onTagProductClick = onTagProductClick,
            )
        }
    }
}

@Composable
fun AmitySelectedMediaElement(
    modifier: Modifier = Modifier,
    media: AmityPostMedia,
    onRemove: (AmityPostMedia) -> Unit,
    productTagCount: Int,
    isProductCatalogueEnabled: Boolean = false,
    isPostProductTagLimitReached: Boolean = false,
    onTagProductClick: (AmityPostMedia) -> Unit = {},
    onClick: (AmityPostMedia) -> Unit = {},
    // Defaults match the single-attachment preview's original rounded tile; the carousel
    // frame is full-bleed (0.dp) with an 8dp control inset instead of 12dp.
    mediaCornerRadius: Dp = 4.dp,
    productTagInset: Dp = 8.dp,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val totalDistinctProductTagCount by viewModel.totalDistinctProductTagCount
        .collectAsState(initial = 0)
    val isGlobalTagLimitReached = totalDistinctProductTagCount >= AmityPostComposerPageViewModel.MAX_TOTAL_PRODUCT_TAGS
    val isEditMode by remember {
        derivedStateOf { viewModel.post.value != null }
    }
    val mediaShape = RoundedCornerShape(mediaCornerRadius)
    Box(
        modifier = modifier
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = mediaShape
            )
            .clickableWithoutRipple { onClick(media) }
    ) {
        // 1. First draw the media content
        when (media.type) {
            AmityPostMedia.Type.IMAGE -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(media.url)
                        .allowHardware(false)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxSize()
                        .testTag("image_view")
                        .clip(mediaShape)
                )
            }

            AmityPostMedia.Type.VIDEO, AmityPostMedia.Type.ClIP -> {
                if (media.url != Uri.EMPTY) {
                    // Display video thumbnail if available
                    val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .components {
                            add(VideoFrameDecoder.Factory())
                        }
                        .build()

                    AsyncImage(
                        model = media.url,
                        imageLoader = imageLoader,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxSize()
                            .testTag("image_view")
                            .clip(mediaShape)
                    )
                } else {
                    // Display placeholder for video without thumbnail
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                color = AmityTheme.colors.baseShade4,
                                shape = mediaShape
                            )
                            .clip(mediaShape)
                    ) {
                        Icon(
                            painter = painterResource(id = CommonComposeR.drawable.amity_ic_play_v4),
                            contentDescription = "Video placeholder",
                            tint = AmityTheme.colors.baseShade2,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                        )
                    }
                }

                // The drawable already carries its own 50%-black disc and white glyph at 40dp --
                // wrapping it in another circle and tinting it flattens both into a white blob.
                Image(
                    painter = painterResource(id = CommonComposeR.drawable.amity_ic_play_v4),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                )
            }
        }

        // Add overlay for states that need it
        if (media.uploadState == AmityFileUploadState.FAILED
            || media.uploadState == AmityFileUploadState.UPLOADING && media.currentProgress < 100
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(amityColorBlack.copy(alpha = 0.5f)) // Black overlay with 50% opacity
            )

            if (media.uploadState == AmityFileUploadState.FAILED) {
                Image(
                    painter = painterResource(id = CommonR.drawable.amity_ic_warning),
                    contentDescription = "media_upload_retry",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                        .clickableWithoutRipple { viewModel.retryMediaUpload(media) }
                )
            }

            // Progress indicator for uploads in progress
            if (media.uploadState == AmityFileUploadState.UPLOADING && media.currentProgress < 100) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(22.dp),
                    color = amityColorWhite, // Track color
                    progress = 1.0f, // Full circle
                    strokeWidth = 3.dp
                )

                // Then draw the actual progress indicator
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(22.dp),
                    color = AmityTheme.colors.primary,
                    progress = media.currentProgress / 100f,
                    strokeWidth = 3.dp
                )
            }
        }

        // Always show the remove button, regardless of media state
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
                .size(20.dp)
                .background(
                    color = amityColorBlack.copy(alpha = 0.53f),
                    shape = CircleShape
                )
                .clickableWithoutRipple {
                    onRemove(media)
                },
        ) {
            Icon(
                painter = painterResource(id = CommonR.drawable.amity_ic_close),
                contentDescription = null,
                tint = amityColorWhite,
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.Center),
            )
        }
        // Alt text is only supported for image attachments, not videos or clips -- and only for media
        // added in this session. Already-published media offers no control at all when editing.
        val isExistingMedia = isEditMode && media.id != null && media.id == media.uploadId
        if (media.type == AmityPostMedia.Type.IMAGE && !isExistingMedia && media.uploadState == AmityFileUploadState.COMPLETE && media.uploadId != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 8.dp)
                    .height(24.dp)
                    .background(
                        color = amityColorBlack.copy(alpha = 0.53f),
                        shape = RoundedCornerShape(size = 9999.dp)
                    )
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                    .clickableWithoutRipple {
                        (media.media as? AmityPostMedia.Media.Image)
                            ?.image
                            ?.let {
                                viewModel.setAltTextMedia(
                                    AltTextMedia.Image(it)
                                )
                                viewModel.showAltTextConfigSheet()
                            }
                    },
            ) {
                Text(
                    text = amitySocialString("amity_social_button_alt"),
                    style = AmityTheme.typography.captionBold.copy(
                        color = amityColorWhite,
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentSize()
                )
                (media.media as? AmityPostMedia.Media.Image)
                    ?.image?.getAltText()?.let {
                        // If alt text is set, show the check icon
                        if (it.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_alt_check),
                                contentDescription = "Check Icon",
                                tint = amityColorWhite,
                            )
                        }
                    }
            }
        }

        if (media.uploadState == AmityFileUploadState.COMPLETE && media.uploadId != null) {
            // Product tag button at bottom right - showWhenEmpty=true so users can add
            // the first product tag in create mode (count = 0 hides the button otherwise)
            // Hide the badge when global limit is reached and this media has no tags
            val shouldShowTagBadge = productTagCount > 0 || !isPostProductTagLimitReached

            if (isProductCatalogueEnabled && shouldShowTagBadge) {
                AmityProductTagBadge(
                    count = productTagCount,
                    showWhenEmpty = !isGlobalTagLimitReached,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = productTagInset, bottom = productTagInset),
                    onClick = { onTagProductClick(media) }
                )
            }
        }
    }
}

@Preview
@Composable
fun AmitySelectedMediaElementPreview() {
    AmitySelectedMediaElement(
        media = AmityPostMedia(
            url = Uri.EMPTY,
            uploadId = "",
            type = AmityPostMedia.Type.VIDEO,
        ),
        productTagCount = 2,
        onRemove = {}
    )
}
/**
 * Frame ratio for a single-attachment preview, classified the same way the published carousel
 * classifies: threshold bands on w/h, snapped to 16:9, 1:1 or 4:5.
 *
 * The SDK only carries dimensions once an upload completes, so a freshly picked image is measured
 * from its own header off the main thread. Without that the preview would sit at 1:1 until the
 * upload finished and then jump — the exact thing a locked ratio exists to prevent.
 */
@Composable
private fun rememberClassifiedMediaRatio(media: AmityPostMedia?): Float {
    val context = LocalContext.current
    if (media == null) return 1f
    // Already-published media arrives with its record, so nothing needs measuring -- dimensionsOf
    // applies the record's rotation, the same turn the extractor below applies to a local file.
    val known = when (val attached = media.media) {
        is AmityPostMedia.Media.Image -> (attached.image.getWidth() to attached.image.getHeight())
            .takeIf { it.first > 0 && it.second > 0 }
        is AmityPostMedia.Media.Video -> AmityMediaRatio.dimensionsOf(attached.video)
        else -> null
    }

    var measured by remember(media.url) { mutableStateOf<Pair<Int, Int>?>(null) }
    LaunchedEffect(media.url, known) {
        if (known != null) return@LaunchedEffect
        measured = withContext(Dispatchers.IO) {
            runCatching {
                if (media.type == AmityPostMedia.Type.IMAGE) {
                    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    context.contentResolver.openInputStream(media.url)?.use {
                        BitmapFactory.decodeStream(it, null, opts)
                    }
                    (opts.outWidth to opts.outHeight).takeIf { it.first > 0 && it.second > 0 }
                } else {
                    MediaMetadataRetriever().use { retriever ->
                        retriever.setDataSource(context, media.url)
                        fun key(k: Int) = retriever.extractMetadata(k)?.toIntOrNull() ?: 0
                        val w = key(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                        val h = key(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        // A phone-shot portrait video is stored landscape with a 90/270 rotation
                        // flag; without swapping, every one of them classifies as 16:9.
                        val rotated = key(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) % 180 != 0
                        (if (rotated) h to w else w to h).takeIf { it.first > 0 && it.second > 0 }
                    }
                }
            }.getOrNull().also {
                // Logged once the measurement has actually finished, not on the pass before it
                // returns -- otherwise every normal load reports a failure it then recovers from.
                if (it == null) {
                    Log.w("AmityMediaRatio", "could not measure ${media.type} at ${media.url} — frame stays 1:1")
                }
            }
        }
    }

    val dimensions = known ?: measured
    return AmityMediaRatio.classify(dimensions?.first ?: 0, dimensions?.second ?: 0)
}
