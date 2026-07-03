package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import kotlinx.coroutines.flow.Flow
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

/**
 * Renders the quoted/parent message above a reply bubble.
 *
 * Shows:
 * - Reply attribution row: reply icon + "You replied to {name}" / "{name} replied to you"
 * - Quoted content: text (2-line max), image thumbnail, video thumbnail, or "Message deleted"
 * - Loading shimmer while the parent message is being fetched
 */
@Composable
fun AmityChatQuotedMessage(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    parentMessageFlow: Flow<AmityMessage>,
    isCurrentUser: Boolean,
    isGroupChat: Boolean = false,
    onQuotedClick: ((AmityMessage) -> Unit)? = null,
) {
    val parentMessage by parentMessageFlow.collectAsState(initial = null)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
    ) {
        // Reply attribution row
        ReplyAttributionRow(
            message = message,
            parentMessage = parentMessage,
            isCurrentUser = isCurrentUser,
            isGroupChat = isGroupChat,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Quoted content
        val parent = parentMessage
        if (parent == null) {
            // Loading
            QuotedMessageLoading()
        } else if (parent.isDeleted()) {
            QuotedMessageDeleted()
        } else {
            val clickModifier = if (onQuotedClick != null) {
                Modifier.clickable { onQuotedClick(parent) }
            } else Modifier
            Box(modifier = clickModifier) {
                QuotedMessageContent(parentMessage = parent)
            }
        }
    }
}

@Composable
private fun ReplyAttributionRow(
    message: AmityMessage,
    parentMessage: AmityMessage?,
    isCurrentUser: Boolean,
    isGroupChat: Boolean,
) {
    val currentUserId = AmityCoreClient.getUserId()
    val isParentCurrentUser = parentMessage?.getCreatorId() == currentUserId

    val replyText = when {
        parentMessage == null -> ""
        parentMessage.isDeleted() -> {
            if (isCurrentUser) amityChatString("chat.reply.you.to.deleted")
            else amityChatString("chat.reply.to.deleted")
        }
        !isGroupChat -> {
            if (isParentCurrentUser) {
                if (isCurrentUser) amityChatString("chat.reply.you.to.yourself")
                else amityChatString("chat.reply.to.you")
            } else {
                if (isCurrentUser) amityChatString("chat.reply.you")
                else amityChatString("chat.reply.to.themself")
            }
        }
        else -> {
            val parentName = truncateDisplayName(parentMessage.getCreator()?.getDisplayName() ?: "")
            val senderName = truncateDisplayName(message.getCreator()?.getDisplayName() ?: "")
            if (isParentCurrentUser) {
                if (isCurrentUser) amityChatString("chat.reply.you.to.yourself")
                else amityChatString("chat.reply.name.to.you", senderName)
            } else {
                if (isCurrentUser) amityChatString("chat.reply.you.to.name", parentName)
                else if (parentMessage.getCreatorId() == message.getCreatorId()) {
                    amityChatString("chat.reply.name.to.themself", senderName)
                } else {
                    amityChatString("chat.reply.name.to.name", senderName, parentName)
                }
            }
        }
    }

    if (replyText.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_reply_message_filled),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = replyText,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 12.sp,
                    color = AmityTheme.colors.baseShade1,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun QuotedMessageContent(
    parentMessage: AmityMessage,
) {
    when (val data = parentMessage.getData()) {
        is AmityMessage.Data.TEXT -> {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmityTheme.colors.backgroundShade1),
            ) {
                Text(
                    text = data.getText(),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        color = AmityTheme.colors.baseInverse,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(AmityTheme.colors.background.copy(alpha = 0.4f)),
                )
            }
        }
        is AmityMessage.Data.IMAGE -> {
            val imageUrl = data.getImage()?.getUrl(AmityImage.Size.MEDIUM)
            val context = LocalContext.current
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build()
            )
            val painterState by painter.state.collectAsState()
            val aspectRatio = remember(painterState) {
                val size = (painterState as? AsyncImagePainter.State.Success)?.painter?.intrinsicSize
                if (size != null && size.width > 0f && size.height > 0f &&
                    !size.width.isInfinite() && !size.height.isInfinite()
                ) size.width / size.height else 1f
            }
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Quoted image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .aspectRatio(aspectRatio),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(AmityTheme.colors.background.copy(alpha = 0.4f)),
                )
            }
        }
        is AmityMessage.Data.VIDEO -> {
            val thumbnailUrl = data.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmityTheme.colors.baseShade4),
                contentAlignment = Alignment.Center,
            ) {
                if (!thumbnailUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(thumbnailUrl)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "Video thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(120.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(amityColorBlack.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.amity_ic_chat_video_play,
                        ),
                        contentDescription = "Video",
                        modifier = Modifier.size(16.dp),
                        tint = amityColorWhite,
                    )
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(AmityTheme.colors.background.copy(alpha = 0.4f)),
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmityTheme.colors.baseShade4),
            ) {
                Text(
                    text = amityChatString("chat.unsupported.message"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        color = AmityTheme.colors.baseShade2,
                    ),
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(AmityTheme.colors.background.copy(alpha = 0.4f)),
                )
            }
        }
    }
}

@Composable
private fun QuotedMessageDeleted() {
    Box {
        Row(
            modifier = Modifier
                .background(Color.Transparent, RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AmityTheme.colors.baseShade2,
            )
            Text(
                text = amityChatString("chat.message.deleted"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    color = AmityTheme.colors.baseShade2,
                ),
            )
        }
        Box(
            Modifier.matchParentSize()
                .background(AmityTheme.colors.background.copy(alpha = 0.4f))
        ) {}
    }
}

@Composable
private fun QuotedMessageLoading() {
    Column(
        modifier = Modifier
            .widthIn(max = 228.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AmityTheme.colors.baseShade4)
            .padding(12.dp),
    ) {
        Box(
            modifier = Modifier
                .height(12.dp)
                .width(120.dp)
                .shimmerBackground(shape = RoundedCornerShape(6.dp)),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(12.dp)
                .width(180.dp)
                .shimmerBackground(shape = RoundedCornerShape(6.dp)),
        )
    }
}

private fun truncateDisplayName(name: String, maxLength: Int = 10): String {
    return if (name.length <= maxLength) name else "${name.substring(0, maxLength)}..."
}
