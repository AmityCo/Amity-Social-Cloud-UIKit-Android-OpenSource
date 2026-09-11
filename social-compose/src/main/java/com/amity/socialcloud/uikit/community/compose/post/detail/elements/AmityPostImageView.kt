package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.R as CommonR

@Composable
fun AmityPostImageView(
    modifier: Modifier = Modifier,
    post: AmityPost,
    onClick: () -> Unit,
) {
    val data = post.getData()
    if(data is AmityPost.Data.IMAGE) {
        val imageUrl = data.getImage()?.getUrl(AmityImage.Size.MEDIUM)
        AmityPostMediaFrame(
            modifier = modifier,
            imageUrl = imageUrl,
            contentScale = ContentScale.Crop,
            onClick = onClick,
        )
    } else if(data is AmityPost.Data.VIDEO) {
        val thumbnail = data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
        AmityPostMediaFrame(
            modifier = modifier,
            imageUrl = thumbnail,
            contentScale = ContentScale.Crop,
            onClick = onClick,
        )
//            val imageLoader = ImageLoader.Builder(LocalContext.current)
//                .components {
//                    add(VideoFrameDecoder.Factory())
//                }
//                .build()
//
//            val media = data.getVideo().blockingGet()
//            AsyncImage(
//                model = media.getUrl() ?: "",
//                imageLoader = imageLoader,
//                contentDescription = "Video Thumbnail",
//                contentScale = ContentScale.Crop,
//                modifier = modifier
//                    .fillMaxSize()
//                    .background(AmityTheme.colors.baseShade4)
//                    .clickableWithoutRipple { onClick() }
//            )

    } else if (data is AmityPost.Data.CLIP) {
        val thumbnail = data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
        val aspectRatio = data.getDisplayMode()
        AmityPostMediaFrame(
            modifier = modifier,
            imageUrl = thumbnail,
            contentScale = if (aspectRatio == AmityClip.DisplayMode.FILL) ContentScale.Crop else ContentScale.Fit,
            backgroundColor = amityMediaSurface,
            onClick = onClick,
        )
    } else if (data is AmityPost.Data.LIVE_STREAM) {
        val thumbnail by produceState(initialValue = "") {
            data.getStream().asFlow().collect { stream ->
                value = stream.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
            }
        }
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(thumbnail)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Stream Thumbnail",
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() }
                .semantics {
                    role = Role.Image
                }
        )
    }
    else if (data is AmityPost.Data.ROOM) {
        val thumbnail = data.getRoom()?.getThumbnail()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(thumbnail)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Stream Thumbnail",
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() }
                .semantics {
                    role = Role.Image
                }
        )
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() }
                .semantics {
                    role = Role.Image
                },
        )
    }
}

/**
 * Loading keeps the background fill so the frame never collapses before the asset resolves;
 * Broken swaps in a placeholder rather than rendering blank, and still consumes the click so a
 * failed frame doesn't block swiping past it. The placeholder look itself is an undesigned stand-in.
 */
@Composable
private fun AmityPostMediaFrame(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentScale: ContentScale = ContentScale.Crop,
    backgroundColor: Color = AmityTheme.colors.baseShade4,
    onClick: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()
    val isBroken = imageUrl.isNullOrBlank() || painterState is AsyncImagePainter.State.Error

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickableWithoutRipple { onClick() }
            .semantics {
                role = Role.Image
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isBroken) {
            // Non-null so a screen reader announces the failure, not silence — merges into the
            // frame's own description since the parent Box is clickable (mergeDescendants).
            Icon(
                painter = painterResource(id = CommonR.drawable.amity_ic_image_not_available),
                contentDescription = "Image failed to load",
                tint = AmityTheme.colors.baseShade2,
                modifier = Modifier.size(32.dp)
            )
        } else {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}