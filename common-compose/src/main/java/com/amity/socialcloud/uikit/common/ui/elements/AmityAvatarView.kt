package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

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
        placeholder = R.drawable.amity_ic_default_community_avatar_circular,

        )
}

@Composable
fun AmityCommunityAvatarWithRoundedCornerView(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    community: AmityCommunity?,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(4.dp),
) {
    val url = community?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(size)
                .clip(roundedCornerShape)
        )
        if (painterState !is AsyncImagePainter.State.Success) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(roundedCornerShape)
                    .background(AmityTheme.colors.baseShade3)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_community_placeholder),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                )
            }
        }
    }
}

@Composable
fun AmityCommunityAvatarWithLabelView(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    community: AmityCommunity?,
    label: String?,
) {
    Box(
        modifier = Modifier
            .size(size)
    ) {
        AmityCommunityAvatarWithRoundedCornerView(
            modifier = modifier,
            size = size,
            community = community,
            roundedCornerShape = RoundedCornerShape(4.dp),
        )

        if (!label.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x66000000)
                            )
                        )
                    )
            ) {
                Text(
                    text = label,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }

    }

}

@Composable
fun AmityUserAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    user: AmityUser?,
) {
    val url = user?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    val painterState by painter.state.collectAsState()

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
        )
        if (painterState !is AsyncImagePainter.State.Success) {
            val displayNameFirstCharacter =
                (user?.getDisplayName()?.trim() ?: "").firstOrNull()?.uppercase() ?: ""

            val fontSize = when (size) {
                64.dp -> 32.sp
                56.dp -> 32.sp
                40.dp -> 20.sp
                32.dp -> 17.sp
                28.dp -> 15.sp
                16.dp -> 10.sp
                else -> 10.sp
            }

            val lineHeight = when (size) {
                64.dp -> 40.sp
                56.dp -> 40.sp
                40.dp -> 24.sp
                32.dp -> 20.sp
                28.dp -> 20.sp
                16.dp -> 13.sp
                else -> 13.sp
            }

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.primaryShade2)
            ) {
                Text(
                    text = displayNameFirstCharacter,
                    style = TextStyle(
                        fontSize = fontSize,
                        lineHeight = lineHeight,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    ),
                    modifier = Modifier.align(Alignment.Center),
                )

            }
        }
    }
}

@Composable
fun AmityCategoryAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    category: AmityCommunityCategory?,
    roundedCornerShape: RoundedCornerShape = CircleShape,
) {
    val url = category?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()
    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(size)
                .clip(roundedCornerShape)
        )
        if (painterState !is AsyncImagePainter.State.Success) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_category_placeholder),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(roundedCornerShape)
            )
        }
    }
}

@Composable
fun AmityAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    iconPadding: Dp = 0.dp,
    image: AmityImage?,
    roundedCornerShape: RoundedCornerShape = CircleShape,
    placeholder: Int = R.drawable.amity_ic_default_profile1,
    placeholderTint: Color = Color.White,
    placeholderBackground: Color = AmityTheme.colors.primaryShade3,
) {
    val url = image?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(size)
                .clip(roundedCornerShape)
        )
        if (painterState !is AsyncImagePainter.State.Success) {
            Icon(
                painter = painterResource(id = placeholder),
                contentDescription = null,
                tint = placeholderTint,
                modifier = Modifier
                    .size(size)
                    .clip(roundedCornerShape)
                    .background(placeholderBackground)
                    .padding(iconPadding)
            )
        }
    }
}