package com.amity.socialcloud.uikit.chat.compose.home.element

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadge
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeFamily
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgePreset
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeShape
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeVariant
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.chat.compose.common.readableChatTimeDiff
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl

@Composable
fun AmityChatListItem(
    modifier: Modifier = Modifier,
    channel: AmityChannel,
    otherMember: AmityChannelMember? = null,
    searchMessage: AmityMessage? = null,
    searchQuery: String = "",
    isArchived: Boolean = false,
    isMuted: Boolean = channel.isMuted(),
    isCompact: Boolean = false,
    onClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val isConversation = channel.getChannelType() == AmityChannel.Type.CONVERSATION

    Row(
        modifier = modifier
            .background(AmityTheme.token(AmityColorToken.SurfaceListDefaultDefault))
            .fillMaxWidth()
            .height(if (isCompact) 62.dp else 82.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isConversation) {
            val isOtherMemberModerator = otherMember?.getRoles()
                ?.contains(AmityConstants.CHANNEL_MODERATOR_ROLE) == true
            AmityUserAvatarView(
                avatarUrl = otherMember?.getUser()?.resolvedAvatarUrl(AmityImage.Size.MEDIUM),
                displayName = getDisplayName(channel, otherMember, context),
                isDeleted = otherMember?.getUser()?.isDeleted() == true,
                size = 40,
                borderWidth = 2,
                isModerator = isOtherMemberModerator,
            )
        } else {
            AmityChannelAvatarView(
                channel = channel,
                size = 40,
                isPrivate = !channel.isPublic(),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (searchMessage == null && searchQuery.isNotEmpty()) {
                    Text(
                        text = buildHighlightedPreview(
                            text = getDisplayName(channel, otherMember, context),
                            query = searchQuery,
                            highlightColor = AmityTheme.token(AmityColorToken.TextListHeaderDefaultHighlight),
                            normalColor = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault)
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
                            color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                }
                if (isMuted) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_bell_slash_s),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AmityTheme.token(AmityColorToken.IconListHeaderGeneral),
                    )
                }
                if (channel.getChannelType() == AmityChannel.Type.COMMUNITY) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${channel.getMemberCount()})",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = AmityTheme.token(AmityColorToken.TextListSubheadDefaultDefault),
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // searchMessage (from message search), when provided, takes priority over the channel's
            // cached last-message preview.
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
                        tint = AmityTheme.token(AmityColorToken.IconListDescriptionGeneral),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                val shouldHighlight = searchMessage != null && searchQuery.isNotEmpty()
                if (shouldHighlight) {
                    Text(
                        text = buildHighlightedPreview(currentPreview.text, searchQuery, AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultHighlight), AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault)),
                        style = AmityTheme.typography.bodyLegacy.copy(fontSize = 13.sp),
                        maxLines = if (isCompact) 1 else 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = buildMentionHighlightedText(currentPreview.text, AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault)),
                        style = AmityTheme.typography.bodyLegacy.copy(fontSize = 13.sp),
                        // Clamps at 2 lines, matching the highlighted branch above; compact rows
                        // stay single-line.
                        maxLines = if (isCompact) 1 else 2,
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
                text = channel.getLastActivity().readableChatTimeDiff(),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 12.sp,
                    color = AmityTheme.token(AmityColorToken.TextListTrailingSubtextDefault),
                ),
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (isArchived) {
                AmityBadge(
                    variant = AmityBadgeVariant.LABEL,
                    label = amityChatString("chat.archived.badge.label"),
                    leadingIcon = CommonR.drawable.amity_ic_archive_r,
                    shape = AmityBadgeShape.ROUND,
                    size = AmityBadgeSize.SIZE_20,
                    preset = AmityBadgePreset(
                        family = AmityBadgeFamily.CHAT,
                        case = "Archived",
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            val unreadCount = channel.getUnreadCount()
            val isMentioned = channel.isMentioned()

            if (isMentioned || unreadCount > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isMentioned && unreadCount > 0) {
                        AmityBadge(
                            variant = AmityBadgeVariant.ICON,
                            icon = CommonR.drawable.amity_ic_at_r,
                            shape = AmityBadgeShape.ROUND,
                            size = AmityBadgeSize.SIZE_24,
                            preset = AmityBadgePreset(
                                family = AmityBadgeFamily.CHAT,
                                case = "Mention",
                            ),
                        )

                        if (unreadCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    if (unreadCount > 0) {
                        AmityBadge(
                            variant = AmityBadgeVariant.LABEL,
                            label = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            shape = AmityBadgeShape.ROUND,
                            size = AmityBadgeSize.SIZE_20,
                            preset = AmityBadgePreset(
                                family = AmityBadgeFamily.GENERAL,
                                case = "Notification",
                            ),
                        )
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
    // Opt-in profile masking ring resolved by the atom; list/member rows pass 2, header/profile-
    // display avatars leave this at 0.
    borderWidth: Int = 0,
    isModerator: Boolean = false,
) {
    // Footprint is pinned to the requested `size`; the atom only supports 8 intrinsic sizes, so the
    // avatar snaps to the nearest one and centers within the footprint. Deleted users always show
    // the generic glyph, never initials.
    val initials = displayName.toChatAvatarInitial().orEmpty()
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center,
    ) {
        AmityAvatar(
            variant = when {
                !avatarUrl.isNullOrEmpty() -> AmityAvatarVariant.Image
                isDeleted || initials.isEmpty() -> AmityAvatarVariant.Icon
                else -> AmityAvatarVariant.Text
            },
            imageUrl = avatarUrl,
            initials = initials,
            icon = if (isDeleted) CommonR.drawable.amity_ic_user_s else CommonR.drawable.amity_ic_user_r,
            size = nearestAvatarSize(size.dp),
            borderWidth = borderWidth,
        )

        if (isModerator) {
            // Occupies the same indicator slot as the private/locked-chat badge and is rendered
            // only where that badge isn't (mutually exclusive per row).
            AmityBadge(
                variant = AmityBadgeVariant.ICON,
                icon = CommonR.drawable.amity_ic_shield_check_s,
                shape = AmityBadgeShape.ROUND,
                size = AmityBadgeSize.SIZE_16,
                preset = AmityBadgePreset(
                    family = AmityBadgeFamily.USER_STATUS,
                    case = "Moderator",
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .border(
                        width = 1.dp,
                        color = AmityTheme.token(AmityColorToken.BorderAvatarIndicatorDefault),
                        shape = CircleShape,
                    ),
            )
        }
    }
}

/** Snaps an arbitrary [Dp] to the closest of the atom's 8 intrinsic [AmityAvatarSize] values. */
private fun nearestAvatarSize(target: Dp): AmityAvatarSize {
    return AmityAvatarSize.entries.minByOrNull { abs(it.dp.value - target.value) } ?: AmityAvatarSize.Size40
}

@Composable
fun AmityChannelAvatarView(
    modifier: Modifier = Modifier,
    channel: AmityChannel,
    size: Int = 40,
    isPrivate: Boolean = false,
) {
    val avatarUrl = channel.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)

    AmityAvatar(
        modifier = modifier,
        variant = if (!avatarUrl.isNullOrEmpty()) AmityAvatarVariant.Image else AmityAvatarVariant.Icon,
        imageUrl = avatarUrl,
        icon = CommonR.drawable.amity_ic_comments_alt_s,
        style = AmityAvatarStyle.Squared,
        size = nearestAvatarSize(size.dp),
        indicator = if (isPrivate) {
            {
                AmityBadge(
                    variant = AmityBadgeVariant.ICON,
                    icon = CommonR.drawable.amity_ic_lock_keyhole_s,
                    shape = AmityBadgeShape.ROUND,
                    size = AmityBadgeSize.SIZE_16,
                    preset = AmityBadgePreset(
                        family = AmityBadgeFamily.CHAT,
                        case = "Private",
                    ),
                    modifier = Modifier
                        .offset(x = 2.dp, y = 2.dp)
                        .border(
                            width = 1.dp,
                            color = AmityTheme.token(AmityColorToken.BorderAvatarIndicatorDefault),
                            shape = CircleShape,
                        ),
                )
            }
        } else null,
    )
}

private fun getDisplayName(
    channel: AmityChannel,
    otherMember: AmityChannelMember?,
    context: android.content.Context,
): String {
    if (channel.getChannelType() == AmityChannel.Type.CONVERSATION && otherMember != null) {
        val user = otherMember.getUser()
        if (user?.isDeleted() == true) {
            return DefaultAmityChatStringProvider.getInstance().getString("chat.deleted.user")
        }
        val name = user?.getDisplayName()
        if (!name.isNullOrEmpty()) return name
        return DefaultAmityChatStringProvider.getInstance().getString("chat.deleted.user")
    }
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
    if (searchMessage != null) {
        if (searchMessage.isDeleted()) return MessagePreviewData(
            text = context.getString(R.string.amity_chat_preview_deleted),
            iconResId = CommonR.drawable.amity_ic_trash_s,
        )
        return when (val data = searchMessage.getData()) {
            is AmityMessage.Data.TEXT -> MessagePreviewData(text = data.getText())
            is AmityMessage.Data.IMAGE -> MessagePreviewData(
                text = context.getString(R.string.amity_chat_preview_sent_photo),
                iconResId = CommonR.drawable.amity_ic_image_s,
            )
            is AmityMessage.Data.VIDEO -> MessagePreviewData(
                text = context.getString(R.string.amity_chat_preview_sent_video),
                iconResId = CommonR.drawable.amity_ic_circle_play_s,
            )
            else -> MessagePreviewData(text = "")
        }
    }

    val preview = channel.getMessagePreview()
        ?: return MessagePreviewData(text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.no.message"))
    if (preview.isDeleted()) return MessagePreviewData(
        text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.deleted"),
        iconResId = CommonR.drawable.amity_ic_trash_s,
    )
    return when (val data = preview.getData()) {
        is AmityMessage.Data.TEXT -> MessagePreviewData(text = data.getText())
        is AmityMessage.Data.IMAGE -> MessagePreviewData(
            text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.sent.photo"),
            iconResId = CommonR.drawable.amity_ic_image_s,
        )
        is AmityMessage.Data.VIDEO -> MessagePreviewData(
            text = DefaultAmityChatStringProvider.getInstance().getString("chat.preview.sent.video"),
            iconResId = CommonR.drawable.amity_ic_circle_play_s,
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