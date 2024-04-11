package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
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
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityMessageAvatarView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    size: Dp = 32.dp,
    avatarUrl: String? = null,
    avatarType: AmityAvatarType = AmityAvatarType.USER,
    placeholder: Painter = painterResource(id = R.drawable.amity_ic_chat_avatar_placeholder),
    placeHolderTint: Color? = null,
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
        if(avatarType == AmityAvatarType.MENTION_ALL) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_mention_all),
                contentDescription = "Mention all",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar Image",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
            if (painter.state !is AsyncImagePainter.State.Success) {
                Image(
                    painter = placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = placeHolderTint?.let(ColorFilter::tint),
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(Color(0xFF191919))
// TODO uncomment this when implementing dark mode
//                        .background(
//                            pageScope
//                                ?.getPageScope()
//                                ?.getConfig()
//                                ?.getBackgroundColor()
//                                ?: AmityTheme.colors.baseInverse
//                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun AmityMessageAvatarViewPreview() {
    AmityMessageAvatarView()
}