package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.formatVideoDuration
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaVideoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityProfileVideoFeedItem(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.VIDEO,
    postId: String? = null,
    parentPostId: String? = null,
    onPostClick: ((String) -> Unit)? = null,
    openDialog: ((AmityPost.Data.VIDEO, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit)? = null,
) {
    val thumbnailUrl = remember(data) {
        data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val painterState by painter.state.collectAsState()

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

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.baseShade4)
    ) {
        Image(
            painter = painter,
            contentDescription = "Video Post",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple {
                    openDialog?.invoke(data, postId) {
                        showBottomSheet = true
                    }
                }
        )

        if (painterState !is AsyncImagePainter.State.Success) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.baseShade4)
            )
        }

        Text(
            text = videoDuration.formatVideoDuration(),
            style = AmityTheme.typography.caption.copy(
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

    if (showBottomSheet && parentPostId != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            ) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_view_post,
                    text = "View original post",
                    modifier = Modifier,
                    onClick = {
                        showBottomSheet = false
                        parentPostId.let { onPostClick?.invoke(it) }
                    }
                )
            }
        }
    }
}

@UnstableApi
@Composable
fun AmityProfileVideoFeedItemPreviewDialog(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.VIDEO,
    postId: String? = null,
    onPostClick: ((String) -> Unit)? = null,
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
            Modifier
                .height(32.dp)
                .offset(y = (-32).dp)
                .fillMaxWidth()
                .background(Color.Black)
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AmityPostMediaVideoPlayer(
                exoPlayer = exoPlayer,
                isVisible = true,
            )

            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                val (closeBtn, menuBtn) = createRefs()

                AmityMenuButton(
                    icon = R.drawable.amity_ic_close2,
                    size = 32.dp,
                    iconPadding = 8.dp,
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE).constrainAs(closeBtn) {
                            top.linkTo(parent.top, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                    onClick = {
                        onDismiss()
                    }
                )

                AmityMenuButton(
                    icon = R.drawable.amity_ic_more_horiz,
                    size = 32.dp,
                    iconPadding = 2.dp,
                    modifier = Modifier.constrainAs(menuBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                    onClick = {
                        if (postId != null && onPostClick != null) {
                            onPostClick(postId)
                        }
                    }
                )
            }
        }
    }
}