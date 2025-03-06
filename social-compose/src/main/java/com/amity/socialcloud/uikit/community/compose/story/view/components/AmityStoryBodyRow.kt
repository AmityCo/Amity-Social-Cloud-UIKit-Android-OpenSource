package com.amity.socialcloud.uikit.community.compose.story.view.components

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.sdk.model.social.story.AmityStoryItem
import com.amity.socialcloud.uikit.common.utils.toComposeColor
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements.AmityStoryHyperlinkView
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryBodyGestureBox
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AmityStoryBodyRow(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    dataType: AmityStory.DataType,
    data: AmityStory.Data,
    state: AmityStory.State,
    items: List<AmityStoryItem>,
    isVisible: Boolean,
    onTap: (Boolean) -> Unit,
    onHold: (Boolean) -> Unit,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
    onHyperlinkClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (dataType) {
            AmityStory.DataType.IMAGE -> {
                AmityStoryBodyImageView(
                    data = data as AmityStory.Data.IMAGE,
                    syncState = state,
                    modifier = modifier.testTag("image_view"),
                )
            }

            AmityStory.DataType.VIDEO -> {
                AmityStoryBodyVideoView(
                    exoPlayer = exoPlayer,
                    isVisible = isVisible,
                    modifier = modifier.testTag("video_view"),
                )
            }

            AmityStory.DataType.UNKNOWN -> {}
        }

        AmityStoryBodyGestureBox(
            modifier = modifier,
            onTap = onTap,
            onHold = onHold,
            onSwipeUp = onSwipeUp,
            onSwipeDown = onSwipeDown,
        )

        if (items.isNotEmpty() && items.first() is AmityStoryItem.HYPERLINK) {
            AmityStoryBodyHyperlinkView(
                hyperlinkItem = items.first() as AmityStoryItem.HYPERLINK,
                onHyperlinkClick = onHyperlinkClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }

        if (state != AmityStory.State.SYNCED) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.5f))
            )
        }
    }
}

@Composable
fun AmityStoryBodyImageView(
    modifier: Modifier = Modifier,
    data: AmityStory.Data.IMAGE,
    syncState: AmityStory.State,
) {
    val contentScale = remember {
        when (data.getImageDisplayMode()) {
            AmityStoryImageDisplayMode.FIT -> ContentScale.Fit
            AmityStoryImageDisplayMode.FILL -> ContentScale.Crop
        }
    }
    val imagePath = remember {
        when (syncState) {
            AmityStory.State.SYNCED -> {
                data.getImage()?.getUrl(AmityImage.Size.LARGE)
            }

            AmityStory.State.SYNCING,
            AmityStory.State.FAILED -> {
                data.getImage()?.getUri()
            }
        }
    }

    var palette by remember { mutableStateOf<Palette?>(null) }
    val painter = rememberAsyncImagePainter(
        contentScale = contentScale,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath)
            .allowHardware(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    val painterState by painter.state.collectAsState()

    LaunchedEffect(painter, painterState) {
        if (painterState is AsyncImagePainter.State.Success) {
            val bitmap = (painterState as AsyncImagePainter.State.Success).result.image.toBitmap()
            launch(Dispatchers.Default) {
                palette = Palette.Builder(bitmap).generate()
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
        Image(
            painter = painter,
            contentScale = contentScale,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )
    }
}

@Composable
fun AmityStoryBodyVideoView(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    isVisible: Boolean
) {
    AmityStoryVideoPlayer(
        modifier = modifier,
        exoPlayer = exoPlayer,
        isVisible = isVisible,
    )
}

@Composable
fun AmityStoryBodyHyperlinkView(
    modifier: Modifier = Modifier,
    hyperlinkItem: AmityStoryItem.HYPERLINK,
    onHyperlinkClick: () -> Unit = {},
) {
    val context = LocalContext.current

    var displayText = hyperlinkItem.getCustomText()
    if (displayText == null) {
        val matcher = Patterns.DOMAIN_NAME.matcher(hyperlinkItem.getUrl())
        displayText = if (matcher.find()) {
            matcher.group()
        } else {
            hyperlinkItem.getUrl()
        }
    }

    AmityStoryHyperlinkView(
        text = displayText ?: "",
        modifier = modifier,
        onClick = {
            onHyperlinkClick()

            var url = hyperlinkItem.getUrl()
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    )
}