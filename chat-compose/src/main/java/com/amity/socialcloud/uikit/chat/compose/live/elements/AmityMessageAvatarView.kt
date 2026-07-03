package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityMessageAvatarView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    size: Dp = 32.dp,
    avatarUrl: String? = null,
    displayName: String? = null,
    avatarType: AmityAvatarType = AmityAvatarType.USER,
    shape: Shape = CircleShape,
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_avatar",
    ) {
        Box(modifier = modifier) {
            if (avatarType == AmityAvatarType.MENTION_ALL) {
                Image(
                    painter = painterResource(id = R.drawable.amity_ic_mention_all),
                    contentDescription = "Mention all",
                    modifier = Modifier
                        .size(size)
                        .clip(shape)
                )
            } else {
                AmityAvatarView(
                    avatarUrl = avatarUrl,
                    displayName = displayName,
                    size = size,
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
