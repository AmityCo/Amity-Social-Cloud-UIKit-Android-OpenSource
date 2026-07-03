package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.databinding.AmityExoChatPlayerBinding
import com.amity.socialcloud.uikit.chat.compose.live.elements.CenterConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import kotlinx.coroutines.launch
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityChatMediaPreviewDialog(
    media: List<Any>, // List of AmityImage or AmityVideo
    selectedIndex: Int = 0,
    isCurrentUser: Boolean = false,
    onDeleteMessage: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isVideoMedia = media.any { it is AmityVideo }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }
    var isAudioMuted by remember { mutableStateOf(false) }
    var verticalDragAmount by remember { mutableFloatStateOf(0f) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(initialPage = selectedIndex) { media.size }

    // Prepare video URLs
    LaunchedEffect(media) {
        exoPlayer.clearMediaItems()
        media.filterIsInstance<AmityVideo>().forEach { video ->
            video.getVideoUrlWithFallbackQuality()?.let { url ->
                exoPlayer.addMediaItem(MediaItem.fromUri(url))
            }
        }
        exoPlayer.prepare()
    }

    LaunchedEffect(pagerState.currentPage) {
        // Find video index within the video items for ExoPlayer
        val currentItem = media.getOrNull(pagerState.currentPage)
        if (currentItem is AmityVideo) {
            val videoIndex = media.take(pagerState.currentPage + 1).count { it is AmityVideo } - 1
            exoPlayer.seekTo(videoIndex, 0)
            exoPlayer.pause()
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirm) {
        CenterConfirmDeletePopup(
            onCancel = {
                showDeleteConfirm = false
            },
            onDelete = {
                showDeleteConfirm = false
                onDeleteMessage?.invoke()
                onDismiss()
            }
        )
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                key = { it },
                modifier = Modifier
                    .fillMaxSize()
                    .background(amityColorBlack)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (verticalDragAmount > 0) {
                                    onDismiss()
                                }
                                verticalDragAmount = 0f
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            verticalDragAmount += dragAmount
                        }
                    }
            ) { index ->
                when (val item = media.getOrNull(index)) {
                    is AmityImage -> {
                        ImagePreviewPage(image = item)
                    }

                    is AmityVideo -> {
                        VideoPreviewPage(
                            exoPlayer = exoPlayer,
                            isVisible = pagerState.currentPage == index,
                        )
                    }

                    else -> Unit
                }
            }

            // Top bar with close button, mute toggle, page counter, save and delete buttons
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(amityColorBlack.copy(alpha = 0.5f)),
            ) {
                val (closeBtn, muteBtn, counter) = createRefs()

                AmityMenuButton(
                    size = 32.dp,
                    iconPadding = 8.dp,
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE)
                        .constrainAs(closeBtn) {
                            top.linkTo(parent.top, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                ) {
                    onDismiss()
                }

                if (isVideoMedia) {
                    Image(
                        painter = painterResource(
                            id = if (isAudioMuted) com.amity.socialcloud.uikit.common.R.drawable.amity_ic_story_audio_mute
                            else com.amity.socialcloud.uikit.common.R.drawable.amity_ic_story_audio_unmute
                        ),
                        contentDescription = "Video Audio",
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(muteBtn) {
                                top.linkTo(parent.top, margin = 64.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            }
                            .clickableWithoutRipple {
                                isAudioMuted = !isAudioMuted
                                exoPlayer.volume = if (isAudioMuted) 0f else 1f
                            },
                    )
                }

                if (media.size > 1) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${media.size}",
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = amityColorWhite,
                        ),
                        modifier = Modifier
                            .semantics {
                                contentDescription =
                                    "Photo ${pagerState.currentPage + 1} of ${media.size}"
                            }
                            .constrainAs(counter) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(closeBtn.bottom)
                            }
                    )
                }
            }

            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(amityColorBlack.copy(alpha = 0.5f)),
            ) {
                val (saveBtn, deleteBtn) = createRefs()

                // Save button — bottom right
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_save_image),
                    contentDescription = "Save",
                    tint = amityColorWhite,
                    modifier = Modifier
                        .constrainAs(saveBtn) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(16.dp)
                        .size(32.dp)
                        .clickableWithoutRipple {
                            val currentItem = media.getOrNull(pagerState.currentPage)
                            scope.launch {
                                when (currentItem) {
                                    is AmityImage -> saveImageToGallery(context, currentItem)
                                    is AmityVideo -> saveVideoToGallery(context, currentItem)
                                }
                            }
                        },
                )

                // Delete button — bottom left, only for own messages
                if (isCurrentUser && onDeleteMessage != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
                        contentDescription = "Delete",
                        tint = amityColorWhite,
                        modifier = Modifier
                            .constrainAs(deleteBtn) {
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(16.dp)
                            .size(32.dp)
                            .clickableWithoutRipple {
                                showDeleteConfirm = true
                            },
                    )
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }

            BackHandler {
                onDismiss()
            }
        }
    }
}

@Composable
private fun ImagePreviewPage(
    image: AmityImage,
    modifier: Modifier = Modifier,
) {
    val imageUrl = image.getUrl(AmityImage.Size.LARGE) ?: image.getUrl(AmityImage.Size.MEDIUM)
    var aspectRatio by remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            val imageBoxModifier = if (aspectRatio != null) {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio!!)
            } else {
                Modifier.fillMaxSize()
            }

            Box(modifier = imageBoxModifier) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Image Preview",
                    contentScale = ContentScale.Fit,
                    onSuccess = { result ->
                        val size = result.painter.intrinsicSize
                        if (size.width > 0 && size.height > 0) {
                            aspectRatio = size.width / size.height
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(rememberZoomState()),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .background(AmityTheme.colors.baseShade4),
            )
        }
    }
}

@Composable
private fun VideoPreviewPage(
    exoPlayer: ExoPlayer,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        AndroidViewBinding(
            modifier = Modifier.fillMaxSize(),
            factory = AmityExoChatPlayerBinding::inflate,
        ) {
            if (isVisible) {
                this.exoPlayer.player = exoPlayer
            } else {
                this.exoPlayer.player = null
            }
        }
    }
}
