package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.amity.socialcloud.uikit.community.compose.utils.toComposeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@UnstableApi
@Composable
fun AmityStoryBodyRow(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    story: AmityStory,
    isVisible: Boolean,
) {
    var palette by remember { mutableStateOf<Palette?>(null) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                when (story.getDataType()) {
                    AmityStory.DataType.IMAGE -> {
                        (story.getData() as AmityStory.Data.IMAGE).getImage()
                            ?.getUrl(AmityImage.Size.FULL) ?: ""
                    }

                    else -> ""
                }
            )
            .allowHardware(false)
            .build()
    )

    LaunchedEffect(painter) {
        val bitmap = painter.imageLoader.execute(painter.request).drawable?.toBitmap()

        if (bitmap != null) {
            launch(Dispatchers.Default) {
                palette = Palette.Builder(bitmap)
                    .generate()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        (palette?.mutedSwatch?.toComposeColor() ?: Color.Black),
                        (palette?.darkMutedSwatch?.toComposeColor() ?: Color.Black)
                    )
                )
            )
    ) {
        when (story.getDataType()) {
            AmityStory.DataType.IMAGE -> {
                val contentScale =
                    when ((story.getData() as AmityStory.Data.IMAGE).getImageDisplayMode()) {
                        AmityStoryImageDisplayMode.FIT -> ContentScale.Fit
                        AmityStoryImageDisplayMode.FILL -> ContentScale.Crop
                    }

                Image(
                    painter = painter,
                    contentScale = contentScale,
                    contentDescription = "Story Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                )
            }

            AmityStory.DataType.VIDEO -> {
                AmityStoryVideoPlayer(
                    exoPlayer = exoPlayer,
                    isVisible = isVisible,
                )
            }

            AmityStory.DataType.UNKNOWN -> {}
        }
    }
}

@Preview
@Composable
fun AmityStoryBodyRowPreview() {
//    AmityStoryBodyRow(
//        exoPlayer = null,
//        isVisible = true,
//        shouldPauseVideo = false
//    )
}