package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
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
    // Opt-in profile masking ring (Border/Avatar/Profile/Default, resolved by the atom). This view
    // is shared by both list/member rows and in-conversation message-bubble avatars — only the
    // former should pass 2; message bubbles must keep the default 0.
    borderWidth: Int = 0,
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
                // Footprint is pinned to the requested `size` (Dp) so surrounding layout/badge
                // anchors are unaffected; the atom itself only supports 8 intrinsic sizes, so the
                // rendered avatar snaps to the nearest one and is centered within the footprint.
                val initials = displayName.toChatAvatarInitial().orEmpty()
                Box(
                    modifier = Modifier.size(size),
                    contentAlignment = Alignment.Center,
                ) {
                    AmityAvatar(
                        variant = when {
                            !avatarUrl.isNullOrEmpty() -> AmityAvatarVariant.Image
                            initials.isNotEmpty() -> AmityAvatarVariant.Text
                            else -> AmityAvatarVariant.Icon
                        },
                        imageUrl = avatarUrl,
                        initials = initials,
                        icon = CommonComposeR.drawable.amity_ic_user_r,
                        size = nearestAvatarSize(size),
                        borderWidth = borderWidth,
                    )
                }
            }
        }
    }
}

/** Snaps an arbitrary [Dp] to the closest of the atom's 8 intrinsic [AmityAvatarSize] values. */
private fun nearestAvatarSize(target: Dp): AmityAvatarSize {
    return AmityAvatarSize.entries.minByOrNull { abs(it.dp.value - target.value) } ?: AmityAvatarSize.Size32
}

@Preview
@Composable
fun AmityMessageAvatarViewPreview() {
    AmityMessageAvatarView()
}
