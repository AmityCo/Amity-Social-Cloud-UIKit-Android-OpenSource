package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Scroll-to-bottom FAB — 40×40 circle with down arrow.
 * Shown when user scrolled up and no new message notification is active.
 */
@Composable
fun AmityChatScrollToBottomFab(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    clip = true,
                )
                .background(
                    color = AmityTheme.colors.baseShade4,
                    shape = CircleShape,
                )
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.base.copy(alpha = 0.1f),
                    shape = CircleShape,
                )
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = com.amity.socialcloud.uikit.common.R.drawable.amity_arrow_down),
                contentDescription = "Scroll to bottom",
                modifier = Modifier.size(20.dp),
                tint = AmityTheme.colors.base,
            )
        }
    }
}

/**
 * New message notification banner — 40dp high pill with avatar + preview.
 * Shown when a new message arrives while user is scrolled up.
 * Tapping scrolls to the latest message.
 */
@Composable
fun AmityChatNewMessageNotification(
    message: AmityMessage?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier,
    ) {
        message?.let { msg ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = true,
                    )
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = AmityTheme.colors.base.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp),
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onClick)
                    .padding(horizontal = 8.dp),
            ) {
                // Avatar
                val avatarUrl = msg.getCreator()?.getAvatar()?.getUrl(AmityImage.Size.SMALL)
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(AmityTheme.colors.baseShade3, CircleShape),
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Message preview
                Text(
                    text = getMessagePreviewText(msg),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.baseInverse,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Down arrow
                Icon(
                    painter = painterResource(id = com.amity.socialcloud.uikit.common.R.drawable.amity_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = AmityTheme.colors.base,
                )
            }
        }
    }
}

private fun getMessagePreviewText(message: AmityMessage): String {
    val senderName = message.getCreator()?.getDisplayName() ?: DefaultAmityChatStringProvider.getInstance().getString("chat.unknown.user")
    val preview = when (val data = message.getData()) {
        is AmityMessage.Data.TEXT -> data.getText()
        is AmityMessage.Data.IMAGE -> "\uD83D\uDCF7 Photo"
        is AmityMessage.Data.VIDEO -> "\uD83C\uDFA5 Video"
        else -> ""
    }
    return "$senderName: $preview"
}
