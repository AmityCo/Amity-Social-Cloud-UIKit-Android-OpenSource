package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.formatVideoDuration
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaVideoPlayer

@Composable
fun AmityCommunityVideoFeedItem(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.VIDEO,
) {
    val thumbnailUrl = remember(data) {
        data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
    }
    val videoData by remember {
        data.getVideo()
    }.subscribeAsState(initial = null)

    val videoDuration by remember(videoData) {
        derivedStateOf {
            videoData?.getMetadata()
                ?.getAsJsonObject("video")
                ?.get("duration")
                ?.asNumber
                ?: 0
        }
    }

    var showPopupDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.baseShade4)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(thumbnailUrl)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Video Post",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple {
                    showPopupDialog = true
                }
        )

        Text(
            text = videoDuration.formatVideoDuration(),
            style = AmityTheme.typography.bodyLegacy.copy(
                color = Color.White,
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(8.dp, (-8).dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }

    if (showPopupDialog) {
        AmityCommunityVideoPreviewDialog(data = data) {
            showPopupDialog = false
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityCommunityVideoPreviewDialog(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.VIDEO,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }
    val video by remember {
        data.getVideo()
    }.subscribeAsState(initial = null)

    var isAudioMuted by remember { mutableStateOf(false) }

    LaunchedEffect(exoPlayer) {
        AmityPostVideoPlayerHelper.setup(exoPlayer)
    }

    LaunchedEffect(video) {
        video?.let {
            AmityPostVideoPlayerHelper.add(it)
            AmityPostVideoPlayerHelper.playMediaItem(0)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            AmityPostVideoPlayerHelper.clear()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AmityPostMediaVideoPlayer(
                exoPlayer = exoPlayer,
                isVisible = true,
            )

            AmityMenuButton(
                icon = R.drawable.amity_ic_close2,
                size = 32.dp,
                iconPadding = 10.dp,
                tint = Color.Black.copy(0.5f),
                background = Color.White.copy(0.8f),
                modifier = modifier
                    .align(Alignment.TopStart)
                    .offset(16.dp, 32.dp),
                onClick = {
                    onDismiss()
                }
            )
        }
    }
}