package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColor
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
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_avatar",
    ) {
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
                if (painterState !is AsyncImagePainter.State.Success) {
                    Image(
                        painter = placeholder,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        colorFilter = placeHolderTint?.let(ColorFilter::tint),
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(AmityTheme.colors.background)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AmityMessageAvatarViewPreview() {
    AmityMessageAvatarView()
}