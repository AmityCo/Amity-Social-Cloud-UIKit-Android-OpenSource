package com.amity.socialcloud.uikit.chat.compose.home.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityChatListItem(
    modifier: Modifier = Modifier,
    channel: AmityChannel,
    otherMember: AmityChannelMember? = null,
    searchMessage: AmityMessage? = null,
    searchQuery: String = "",
    isArchived: Boolean = false,
    onClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val isConversation = channel.getChannelType() == AmityChannel.Type.CONVERSATION

    Row(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .fillMaxWidth()
            .height(82.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar — use other member's avatar for conversation channels
        if (isConversation) {
            AmityUserAvatarView(
                avatarUrl = otherMember?.getUser()?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM),
                displayName = getDisplayName(channel, otherMember, context),
                isDeleted = otherMember?.getUser()?.isDeleted() == true,
                size = 40,
            )
        } else {
            AmityChannelAvatarView(
                channel = channel,
                size = 40,
                isPrivate = !channel.isPublic(),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name + preview
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            // Display name row
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (searchMessage == null && searchQuery.isNotEmpty()) {
                    Text(
                        text = buildHighlightedPreview(
                            text = getDisplayName(channel, otherMember, context),
                            query = searchQuery,
                            highlightColor = AmityTheme.colors.primary,
                            normalColor = if (otherMember?.getUser()?.isDeleted() == true) AmityTheme.colors.baseShade2
                                    else AmityTheme.colors.baseInverse
                        ),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                } else {
                    Text(
                        text = getDisplayName(channel, otherMember, context),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = if (otherMember?.getUser()?.isDeleted() == true) AmityTheme.colors.baseShade2
                                else AmityTheme.colors.baseInverse,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                }
                if (channel.getChannelType() == AmityChannel.Type.COMMUNITY) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${channel.getMemberCount()})",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = AmityTheme.colors.baseShade2,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Message preview with icon
            // When a searchMessage is provided (Messages tab), show that message's content.
            // Otherwise fall back to the channel's lastMessage preview.
            val rawPreviewData = getMessagePreviewData(channel, context, searchMessage)
            // Cache the last valid preview to avoid flashing "No message yet" during
            // PagingData refresh when the channel object is briefly missing its preview.
            val previewData = remember(channel.getChannelId()) { mutableStateOf(rawPreviewData) }
            val hasPreview = searchMessage != null || channel.getMessagePreview() != null
            if (hasPreview || channel.getUnreadCount() == 0) {
                previewData.value = rawPreviewData
            }
            val currentPreview = previewData.value
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentPreview.iconResId != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = currentPreview.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AmityTheme.colors.baseShade2,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                // Bold-highlight the matched query substring when showing a search message result
                val shouldHighlight = searchMessage != null && searchQuery.isNotEmpty()
                if (shouldHighlight) {
                    Text(
                        text = buildHighlightedPreview(currentPreview.text, searchQuery, AmityTheme.colors.base, AmityTheme.colors.baseShade2),
                        style = AmityTheme.typography.bodyLegacy.copy(fontSize = 13.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = buildMentionHighlightedText(currentPreview.text, AmityTheme.colors.baseShade2),
                        style = AmityTheme.typography.bodyLegacy.copy(fontSize = 13.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Timestamp + unread count
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = channel.getLastActivity().readableSocialTimeDiff(),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 12.sp,
                    color = AmityTheme.colors.baseShade2,
                ),
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Archived badge — shown in search results when channel is archived
            if (isArchived) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(20.dp),
                        )
                        .padding(start = 4.dp, end = 6.dp, top = 3.5.dp, bottom = 3.5.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_archive),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = AmityTheme.colors.baseShade1,
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = amityChatString("chat.archived.badge.label"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 11.sp,
                            color = AmityTheme.colors.baseShade1,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            val unreadCount = channel.getUnreadCount()
            val isMentioned = channel.isMentioned()

            if (isMentioned || unreadCount > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isMentioned && unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = AmityTheme.colors.primaryShade3,
                                    shape = CircleShape,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.amity_ic_chat_room_mention),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = AmityTheme.colors.primary,
                            )
                        }

                        if (unreadCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = AmityTheme.colors.alert,
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .padding(horizontal = 6.dp, vertical = 1.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = amityColorWhite,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmityUserAvatarView(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    displayName: String?,
    isDeleted: Boolean = false,
    size: Int = 40,
) {
    AmityAvatarView(
        modifier = modifier,
        avatarUrl = avatarUrl,
        displayName = displayName,
        isDeleted = isDeleted,
        size = size.dp,
    )
}

@Composable
fun AmityChannelAvatarView(
    modifier: Modifier = Modifier,
    channel: AmityChannel,
    size: Int = 40,
    isPrivate: Boolean = false,
) {
    val avatarUrl = channel.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
    val shape = RoundedCornerShape(8.dp)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(avatarUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Box(
        modifier = modifier.size(size.dp),
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(shape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Channel Avatar",
                modifier = Modifier
                    .size(size.dp)
                    .clip(shape),
            )
            if (painterState !is AsyncImagePainter.State.Success) {
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .clip(shape)
                        .background(AmityTheme.colors.primaryShade3),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        if (isPrivate) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_chat_private_community),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

private fun getDisplayName(
    channel: AmityChannel,
    otherMember: AmityChannelMember?,
    context: android.content.Context,
): String {
    // For conversation channels, use the other member's display name
    if (channel.getChannelType() == AmityChannel.Type.CONVERSATION && otherMember != null) {
        val user = otherMember.getUser()
        if (user?.isDeleted() == true) {
            return DefaultAmityChatStringProvider.getInstance().getString("chat.deleted.user")
        }
        val name = user?.getDisplayName()
        if (!name.isNullOrEmpty()) return name
        return DefaultAmityChatStringProvider.getInstance().getString("chat.deleted.user")
    }
    // For group channels, use channel display name
    val displayName = channel.getDisplayName()
    if (displayName.isNullOrEmpty()) {
        return DefaultAmityChatStringProvider.getInstance().getString("chat.deleted.user")
    }
    return displayName
}

private data class MessagePreviewData(
    val text: String,
    val iconResId: Int? = null,
)

private fun getMessagePreviewData(
    channel: AmityChannel,
    context: android.content.Context,
    searchMessage: AmityMessage? = null,
): MessagePreviewData {
    // Path 1: searchMessage is AmityMessage — use it directly
    if (searchMessage != null) {
        if (searchMessage.isDeleted()) return MessagePreviewData(
            text = context.getString(R.string.amity_chat_preview_deleted),
            iconResId = R.drawable.amity_ic_chat_preview_deleted,
        )
        return when (val data = searchMessage.getData()) {
            is AmityMessage.Data.TEXT -> MessagePreviewData(text = data.getText())
            is AmityMessage.Data.IMAGE -> MessagePreviewData(
                text = context.getString(R.string.amity_chat_preview_sent_photo),
                iconResId = R.drawable.amity_ic_chat_preview_image,
            )
            is AmityMessage.Data.VIDEO -> MessagePreviewData(
                text = context.getString(R.string.amity_chat_preview_sent_video),
                iconResId = R.drawable.amity_ic_chat_preview_video,
            )
            else -> MessagePreviewData(text = "")
        }
    }

    // Path 2: fall back to channel.getMessagePreview() which is AmityMessagePreview
    val preview = channel.getMessagePreview()
        ?: return MessagePreviewData(text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.no.message"))
    if (preview.isDeleted()) return MessagePreviewData(
        text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.deleted"),
        iconResId = R.drawable.amity_ic_chat_preview_deleted,
    )
    return when (val data = preview.getData()) {
        is AmityMessage.Data.TEXT -> MessagePreviewData(text = data.getText())
        is AmityMessage.Data.IMAGE -> MessagePreviewData(
            text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.sent.photo"),
            iconResId = R.drawable.amity_ic_chat_preview_image,
        )
        is AmityMessage.Data.VIDEO -> MessagePreviewData(
            text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.sent.video"),
            iconResId = R.drawable.amity_ic_chat_preview_video,
        )
        else -> MessagePreviewData(text = "")
    }
}

/**
 * Builds an AnnotatedString with the matching query substrings bolded and in base color.
 * Matches the query anywhere in the text (case-insensitive), consistent with the
 * server-side search which matches substrings at any position (e.g. "test" in "Web-Test").
 * Overlapping matches are skipped; the cursor advances past each match.
 */
private fun buildHighlightedPreview(
    text: String,
    query: String,
    highlightColor: androidx.compose.ui.graphics.Color,
    normalColor: androidx.compose.ui.graphics.Color,
): AnnotatedString {
    if (text.isEmpty() || query.isEmpty()) return AnnotatedString(text)
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()
    // Find all non-overlapping match positions, anywhere in the text
    val matches = mutableListOf<Int>()
    var i = lowerText.indexOf(lowerQuery)
    while (i >= 0) {
        matches.add(i)
        i = lowerText.indexOf(lowerQuery, i + lowerQuery.length)
    }
    if (matches.isEmpty()) return buildAnnotatedString {
        withStyle(SpanStyle(color = normalColor)) { append(text) }
    }
    return buildAnnotatedString {
        var cursor = 0
        for (matchStart in matches) {
            if (matchStart > cursor) {
                withStyle(SpanStyle(color = normalColor)) {
                    append(text.substring(cursor, matchStart))
                }
            }
            withStyle(SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold)) {
                append(text.substring(matchStart, matchStart + query.length))
            }
            cursor = matchStart + query.length
        }
        if (cursor < text.length) {
            withStyle(SpanStyle(color = normalColor)) { append(text.substring(cursor)) }
        }
    }
}

/**
 * Builds an AnnotatedString where @mention patterns are rendered in bold.
 */
private fun buildMentionHighlightedText(
    text: String,
    normalColor: androidx.compose.ui.graphics.Color,
): AnnotatedString {
    if (text.isEmpty()) return AnnotatedString(text)
    val mentionPattern = Regex("@\\S+")
    val matches = mentionPattern.findAll(text).toList()
    if (matches.isEmpty()) return buildAnnotatedString {
        withStyle(SpanStyle(color = normalColor)) { append(text) }
    }
    return buildAnnotatedString {
        var cursor = 0
        for (match in matches) {
            if (match.range.first > cursor) {
                withStyle(SpanStyle(color = normalColor)) {
                    append(text.substring(cursor, match.range.first))
                }
            }
            withStyle(SpanStyle(color = normalColor, fontWeight = FontWeight.Bold)) {
                append(match.value)
            }
            cursor = match.range.last + 1
        }
        if (cursor < text.length) {
            withStyle(SpanStyle(color = normalColor)) { append(text.substring(cursor)) }
        }
    }
}