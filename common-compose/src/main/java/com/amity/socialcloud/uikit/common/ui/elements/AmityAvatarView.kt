package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    avatarUrl: String? = null,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(avatarUrl)
            .dispatcher(Dispatchers.IO)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
        )
        if (painter.state !is AsyncImagePainter.State.Success) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_default_profile1),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.primaryShade3)
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun AmityAvatarViewPreview() {
    AmityAvatarView()
}