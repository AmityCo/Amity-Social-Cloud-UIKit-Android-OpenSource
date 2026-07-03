package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityAvatarPlaceholderBackground
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityDeletedAvatarPlaceholderBackground
import com.amity.socialcloud.uikit.common.ui.theme.amityEventAvatarPlaceholderBackground

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
                    tint = amityColorWhite,
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
                                amityColorBlack.copy(alpha = 0.4f)
                            )
                        )
                    )
            ) {
                Text(
                    text = label,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = amityColorWhite
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }

    }

}

/**
 * Base avatar composable for displaying a user avatar with:
 *  - a remote image loaded from [avatarUrl]
 *  - a text-initial fallback using the first non-space character of [displayName] (trimmed)
 *  - a placeholder icon fallback for deleted users or when [displayName] is blank
 *
 * All higher-level avatar components (chat, social, etc.) should delegate to this function
 * so that avatar rendering logic is centralised in one place.
 */
@Composable
fun AmityAvatarView(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    displayName: String?,
    isDeleted: Boolean = false,
    size: Dp = 40.dp,
    placeholderRes: Int = R.drawable.amity_ic_default_profile1,
) {
    // Trim early — ensures leading/trailing spaces in display names never produce a blank initial
    val trimmedName = displayName?.trim().orEmpty()
    val initial = trimmedName.firstOrNull()?.uppercase() ?: ""
    // val url = user?.resolvedAvatarUrl()?.ifEmpty { null }
    val url = avatarUrl?.ifEmpty { null }

    val fontSize = when {
        size >= 120.dp -> 64.sp
        size >= 64.dp -> 32.sp
        size >= 56.dp -> 32.sp
        size >= 40.dp -> 20.sp
        size >= 32.dp -> 17.sp
        size >= 28.dp -> 15.sp
        else -> 10.sp
    }

    val lineHeight = when {
        size >= 120.dp -> 56.sp
        size >= 64.dp -> 40.sp
        size >= 56.dp -> 40.sp
        size >= 40.dp -> 24.sp
        size >= 32.dp -> 20.sp
        size >= 28.dp -> 20.sp
        else -> 13.sp
    }

    Box(modifier = modifier) {
        if (!avatarUrl.isNullOrEmpty()) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(avatarUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build()
            )
            val painterState by painter.state.collectAsState()

            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar Image",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
            )

            if (painterState !is AsyncImagePainter.State.Success) {
                AvatarFallback(
                    size = size,
                    initial = initial,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    isDeleted = isDeleted,
                    placeholderRes = placeholderRes,
                )
            }
        } else {
            AvatarFallback(
                size = size,
                initial = initial,
                fontSize = fontSize,
                lineHeight = lineHeight,
                isDeleted = isDeleted,
                placeholderRes = placeholderRes,
            )
        }
    }
}

@Composable
private fun AvatarFallback(
    size: Dp,
    initial: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    lineHeight: androidx.compose.ui.unit.TextUnit,
    isDeleted: Boolean,
    placeholderRes: Int,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (isDeleted) amityDeletedAvatarPlaceholderBackground()
                else amityAvatarPlaceholderBackground(),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (!isDeleted && initial.isNotEmpty()) {
            Text(
                text = initial,
                style = TextStyle(
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                ),
            )
        } else {
            Image(
                painter = painterResource(id = placeholderRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.Center),
            )
        }
    }
}

/** Convenience overload that accepts an [AmityUser] directly. */
@Composable
fun AmityUserAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    user: AmityUser?,
) {
    AmityAvatarView(
        modifier = modifier,
        avatarUrl = user?.resolvedAvatarUrl()?.ifEmpty { null },
        displayName = user?.getDisplayName(),
        size = size,
    )
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
                    .background(amityAvatarPlaceholderBackground(), CircleShape)
                    .clip(roundedCornerShape)
            )
        }
    }
}

@Composable
fun AmityEventAvatarView(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    eventCoverImage: AmityImage?,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(6.dp),
) {
    val url = eventCoverImage?.getUrl(AmityImage.Size.MEDIUM)?.ifEmpty { null }

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
            contentDescription = "Event cover",
            modifier = Modifier
                .size(size)
                .clip(roundedCornerShape)
        )
        if (painterState !is AsyncImagePainter.State.Success) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(roundedCornerShape)
                    .background(amityEventAvatarPlaceholderBackground)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.amity_ic_event_list_placeholder),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(roundedCornerShape)
                )
            }
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
    placeholderTint: Color = amityColorWhite,
    placeholderBackground: Color = amityAvatarPlaceholderBackground(),
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
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.primaryShade2)
            ) {
                Image(
                    painter = painterResource(id = placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(placeholderTint),
                    modifier = Modifier
                        .size(size)
                        .clip(roundedCornerShape)
                        .background(placeholderBackground)
                        .padding(iconPadding)
                        .align(Alignment.Center)
                )
            }
        }
    }
}