package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityPostImageView(
    modifier: Modifier = Modifier,
    post: AmityPost,
    onClick: () -> Unit,
) {
    val data = post.getData()
    if(data is AmityPost.Data.IMAGE) {
        val imageUrl = data.getImage()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Image Post",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() }
        )
    } else if(data is AmityPost.Data.VIDEO) {
        val thumbnail = data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(thumbnail)
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() }
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

    } else if (data is AmityPost.Data.LIVE_STREAM) {
        val thumbnail = data.getStream().blockingFirst().getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
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
        )
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onClick() },
        )
    }
}