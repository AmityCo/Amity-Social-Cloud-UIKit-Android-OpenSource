package com.amity.socialcloud.uikit.community.compose.post.composer.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityProductTagBadge
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmitySelectedMediaComponent(
    modifier: Modifier = Modifier,
    isProductCatalogueEnabled: Boolean = false,
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

    var containerWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val columnCount = when (selectedMediaFiles.size) {
        1 -> 1
        2 -> 2
        else -> 3
    }

    val spacing = 8.dp
    val spacingPx = with(density) { spacing.toPx() }
    val itemWidth = with(density) {
        if (containerWidth > 0) {
            ((containerWidth - spacingPx * (columnCount - 1)) / columnCount).toInt().toDp()
        } else {
            0.dp
        }
    }

    val shouldShowMediaError by remember {
        derivedStateOf {
            selectedMediaFiles.any { it.uploadState == AmityFileUploadState.FAILED }
        }
    }

    val mediaErrorTitle by remember {
        derivedStateOf {
            if(shouldShowMediaError) {
                if(selectedMediaFiles.firstOrNull()?.type == AmityPostMedia.Type.IMAGE) {
                    "Upload failed. Photo must:"
                } else {
                    "Upload failed. Video must:"
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {


        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (shouldShowMediaError) {
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
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_error),
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

        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .onSizeChanged { containerWidth = it.width },
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            when (selectedMediaFiles.size) {
                1 -> {
                    selectedMediaFiles.forEach { media ->
                        val productTagCount = mediaProductTags[media.id]?.size ?: 0
                        AmitySelectedMediaElement(
                            modifier = Modifier
                                .width(itemWidth)
                                .aspectRatio(1f),
                            media = media,
                            productTagCount = productTagCount,
                            isProductCatalogueEnabled = isProductCatalogueEnabled,
                            isPostProductTagLimitReached = isPostProductTagLimitReached,
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
                            onTagProductClick = onTagProductClick,
                        )
                    }
                }

                2 -> {
                    selectedMediaFiles.forEach { media ->
                        val productTagCount = mediaProductTags[media.id]?.size ?: 0
                        AmitySelectedMediaElement(
                            modifier = Modifier
                                .width(itemWidth)
                                .aspectRatio(0.5f),
                            media = media,
                            productTagCount = productTagCount,
                            isProductCatalogueEnabled = isProductCatalogueEnabled,
                            isPostProductTagLimitReached = isPostProductTagLimitReached,
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
                            onTagProductClick = onTagProductClick,
                        )
                    }
                }

                else -> {
                    selectedMediaFiles.forEach { media ->
                        val productTagCount = mediaProductTags[media.id]?.size ?: 0
                        AmitySelectedMediaElement(
                            modifier = Modifier
                                .width(itemWidth)
                                .aspectRatio(1f),
                            media = media,
                            productTagCount = productTagCount,
                            isProductCatalogueEnabled = isProductCatalogueEnabled,
                            isPostProductTagLimitReached = isPostProductTagLimitReached,
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
                            onTagProductClick = onTagProductClick,
                        )
                    }
                }
            }
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
    Box(
        modifier = modifier
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(4.dp)
            )
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
                        .clip(RoundedCornerShape(4.dp))
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
                            .clip(RoundedCornerShape(4.dp))
                    )
                } else {
                    // Display placeholder for video without thumbnail
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_play_v4),
                            contentDescription = "Video placeholder",
                            tint = AmityTheme.colors.baseShade2,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                        )
                    }
                }

                // Add play button overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                        .background(
                            color = Color(0x88000000),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_play_v4),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                    )
                }
            }
        }

        // Add overlay for states that need it
        if (media.uploadState == AmityFileUploadState.FAILED
            || media.uploadState == AmityFileUploadState.UPLOADING && media.currentProgress < 100
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)) // Black overlay with 50% opacity
            )

            if (media.uploadState == AmityFileUploadState.FAILED) {
                // Show warning icon for failed uploads
                Image(
                    painter = painterResource(id = R.drawable.amity_ic_warning),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            }

            // Progress indicator for uploads in progress
            if (media.uploadState == AmityFileUploadState.UPLOADING && media.currentProgress < 100) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    color = Color.White, // Track color
                    progress = 1.0f, // Full circle
                    strokeWidth = 3.dp
                )

                // Then draw the actual progress indicator
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    color = AmityTheme.colors.highlight,
                    progress = media.currentProgress / 100f,
                    strokeWidth = 3.dp
                )
            }
        }

        // Always show the remove button, regardless of media state
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(20.dp)
                .background(
                    color = Color(0x88000000),
                    shape = CircleShape
                )
                .clickableWithoutRipple {
                    onRemove(media)
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_close),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.Center),
            )
        }
        val isExistingMedia = isEditMode && media.id != null && media.id == media.uploadId
        if (!isExistingMedia && media.uploadState == AmityFileUploadState.COMPLETE && media.uploadId != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .height(24.dp)
                    .background(
                        color = Color(0x88000000),
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
                    text = "ALT",
                    style = AmityTheme.typography.captionBold.copy(
                        color = Color.White,
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
                                tint = Color.White,
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
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onTagProductClick(media) }
                )
            }
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(8.dp)
//                    .size(24.dp)
//                    .background(
//                        color = Color(0x88000000),
//                        shape = CircleShape
//                    )
//                    .clickableWithoutRipple {
//                        onTagProductClick(media)
//                    },
//                contentAlignment = Alignment.Center,
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.amity_ic_product_tag),
//                    contentDescription = "Tag products",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(14.dp)
//                        .align(Alignment.Center),
//                )
//            }
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