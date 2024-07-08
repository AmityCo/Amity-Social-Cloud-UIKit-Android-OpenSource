package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityPostImageView(
    modifier: Modifier = Modifier,
    image: AmityImage,
    onClick: () -> Unit,
) {
    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(image.getUrl(AmityImage.Size.MEDIUM))
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
}