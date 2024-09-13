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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityCommunityAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    community: AmityCommunity?,
) {
    AmityAvatarView(
        modifier = modifier,
        size = size,
        image = community?.getAvatar(),
        placeholder = R.drawable.amity_ic_default_community_avatar_circular
    )
}

@Composable
fun AmityUserAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    user: AmityUser?,
) {
    AmityAvatarView(
        modifier = modifier,
        size = size,
        iconPadding = 10.dp,
        image = user?.getAvatar(),
        placeholder = R.drawable.amity_ic_default_profile1
    )
}

@Composable
fun AmityAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    iconPadding: Dp = 0.dp,
    image: AmityImage?,
    placeholder: Int = R.drawable.amity_ic_default_profile1,
) {
    val url = image?.getUrl(AmityImage.Size.FULL)?.ifEmpty { null }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
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
                painter = painterResource(id = placeholder),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.primaryShade3)
                    .padding(iconPadding)
            )
        }
    }
}