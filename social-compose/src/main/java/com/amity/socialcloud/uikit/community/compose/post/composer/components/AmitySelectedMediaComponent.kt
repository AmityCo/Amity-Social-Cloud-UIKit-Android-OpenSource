package com.amity.socialcloud.uikit.community.compose.post.composer.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia

@Composable
fun AmitySelectedMediaComponent(
    modifier: Modifier = Modifier,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()

    val gridCells by remember(selectedMediaFiles.size) {
        mutableStateOf(
            when (selectedMediaFiles.size) {
                1 -> GridCells.Fixed(1)
                2 -> GridCells.Fixed(2)
                else -> GridCells.Fixed(3)
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = gridCells,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
        ) {
            when (selectedMediaFiles.size) {
                1 -> {
                    items(selectedMediaFiles.size) {
                        AmitySelectedMediaElement(
                            modifier = modifier.aspectRatio(1f),
                            media = selectedMediaFiles[it],
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
                        )
                    }
                }

                2 -> {
                    items(selectedMediaFiles.size) {
                        AmitySelectedMediaElement(
                            modifier = modifier.aspectRatio(0.5f),
                            media = selectedMediaFiles[it],
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
                        )
                    }
                }

                else -> {
                    items(selectedMediaFiles.size) {
                        AmitySelectedMediaElement(
                            modifier = modifier.aspectRatio(1f),
                            media = selectedMediaFiles[it],
                            onRemove = { postMedia ->
                                viewModel.removeMedia(postMedia)
                            },
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
) {
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

            AmityPostMedia.Type.VIDEO -> {
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
                            .padding(start = 6.dp, end = 4.dp)
                    )
                }
            }
        }

        // Add overlay for states that need it
        if (media.uploadState == AmityFileUploadState.FAILED
            || media.uploadState == AmityFileUploadState.UPLOADING && media.currentProgress < 100) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)) // Black overlay with 50% opacity
            )

            if(media.uploadState == AmityFileUploadState.FAILED) {
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
        onRemove = {}
    )
}