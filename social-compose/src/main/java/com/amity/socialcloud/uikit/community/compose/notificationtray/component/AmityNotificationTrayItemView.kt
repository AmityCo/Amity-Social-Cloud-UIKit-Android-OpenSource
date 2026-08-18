package com.amity.socialcloud.uikit.community.compose.notificationtray.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomStatus
import com.amity.socialcloud.uikit.common.utils.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityEventAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import com.amity.socialcloud.uikit.community.compose.event.components.AmityEventTypeChip
import com.amity.socialcloud.uikit.community.compose.event.formatEventStartDateTime
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityLiveBadgeRed
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun AmityNotificationTrayItemView(
    modifier: Modifier = Modifier,
    isSeen: Boolean = false,
    data: AmityNotificationTrayItem? = null,
) {
    // Event-creation notifications share actionType == "event" with event reminder/started;
    // only "event_created" renders the dedicated event-creation variant (PDT-3724).
    val isEventCreated = data?.getTrayItemCategory() == "event_created"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isSeen) AmityTheme.colors.background
                else AmityTheme.colors.primary.copy(alpha = 0.2f)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar - system icon for user_profile_reset, community avatar for event creation,
        // user avatar otherwise
        if (data?.getTrayItemCategory() == "user_profile_reset") {
            AmityAvatarView(
                image = null,
                size = 32.dp,
                iconPadding = 8.dp,
                placeholder = CommonComposeR.drawable.amity_ic_default_profile1,
                placeholderTint = amityColorWhite,
                placeholderBackground = AmityTheme.colors.primaryShade2,
            )
        } else if (data?.getActionType() == "event") {
            if (isEventCreated) {
                // Event creation notification shows the community's avatar (PDT-3724)
                AmityCommunityAvatarView(
                    community = rememberEventCommunity(data.getEvent(), data.getTargetId()),
                    size = 32.dp,
                )
            } else {
                data.getEvent()?.let { event ->
                    AmityEventAvatarView(
                        eventCoverImage = event.getCoverImage()
                    )
                }
            }
        } else if (data?.getTrayItemCategory() == "user_profile_reset") {
            Image(
                painter = painterResource(R.drawable.amity_ic_notification_moderation),
                contentDescription = "Moderation notification",
                modifier = Modifier.size(40.dp)
            )
        } else if (data?.getActionType() == "invitation") {
            AmityLiveStreamInvitationAvatarView(
                user = data.getUsers()?.firstOrNull(),
                roomId = if (data.getTargetType() == "room") data.getTargetId() else null,
            )
        } else {
            data?.getUsers()?.firstOrNull()?.let {
                AmityUserAvatarView(
                    user = it,
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            //create annotate string for highlight text
            HighlightText(
                text = data?.getText() ?: "",
                templatedText = data?.getTemplatedText() ?: "",
                category = data?.getTrayItemCategory() ?: ""
            )

            // Event creation notification: secondary line with event type chip
            // followed by the event start date and start time (PDT-3724)
            val event = data?.getEvent()
            if (isEventCreated && event != null) {
                val context = LocalContext.current
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AmityEventTypeChip(type = event.getType())
                    Text(
                        text = formatEventStartDateTime(event.getStartTime(), context),
                        style = AmityTheme.typography.caption.copy(fontSize = 13.sp),
                        color = AmityTheme.colors.baseShade2
                    )
                }
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = data?.getLastOccurredAt()?.readableSocialTimeDiff() ?: "",
            style = AmityTheme.typography.caption.copy(fontSize = 13.sp),
            color = AmityTheme.colors.baseShade2
        )

    }
}

@Composable
@Preview
private fun AmityNotificationTrayItemReview() {
    AmityNotificationTrayItemView(isSeen = true)
}

/**
 * Resolves the community to display for an event notification.
 *
 * The notification tray API response embeds the event but not its community, so
 * the SDK can only resolve [AmityEvent.getTargetCommunity] when the community is
 * already cached (typically public ones surfaced elsewhere). For an uncached
 * community — e.g. a private one the user hasn't loaded through another screen —
 * this fetches it reactively so the avatar fills in when it arrives, without
 * blocking the list from rendering. Falls back to the community placeholder while
 * loading or if the fetch fails.
 */
@Composable
private fun rememberEventCommunity(event: AmityEvent?, targetId: String?): AmityCommunity? {
    val target = event?.getTargetCommunity()
    val communityId = event?.getOriginId() ?: targetId

    val fetched by produceState<AmityCommunity?>(null, communityId) {
        if (communityId.isNullOrEmpty()) {
            value = null
            return@produceState
        }
        val disposable = AmitySocialClient.newCommunityRepository()
            .getCommunity(communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ value = it }, { /* keep placeholder on error */ })
        awaitDispose { disposable.dispose() }
    }

    return target ?: fetched
}

@Composable
fun HighlightText(
    modifier: Modifier = Modifier,
    text: String,
    templatedText: String,
    category: String = "",
) {
    // Improved regex to match {{key:value}} patterns without escaped backslashes
    val regex = "\\{\\{[^}]*\\}\\}".toRegex()
    val placeholders = regex.findAll(templatedText).toList()
    val literalParts = regex.split(templatedText)

    // For user_profile_reset, bold the leading phrase regardless of placeholder presence
    if (category == "user_profile_reset") {
        val boldPhrase = "Your profile information was reset"
        val annotatedString = buildAnnotatedString {
            if (text.startsWith(boldPhrase)) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(boldPhrase)
                }
                append(text.removePrefix(boldPhrase))
            } else {
                append(text)
            }
        }
        Text(
            modifier = modifier,
            text = annotatedString,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 5,
        )
    } else if (placeholders.isNotEmpty()) {
        // Find values that replace placeholders in the rendered text
        val placeholderValues = mutableListOf<String>()
        var remainingText = text
        var currentPos = 0

        for (i in literalParts.indices) {
            if (i < literalParts.size - 1 || literalParts.last().isNotEmpty()) {
                val part = literalParts[i]
                val partPos = remainingText.indexOf(part, currentPos)

                if (partPos >= 0) {
                    if (partPos > currentPos) {
                        // Text between current position and start of literal part is a placeholder value
                        placeholderValues.add(remainingText.substring(currentPos, partPos))
                    }
                    currentPos = partPos + part.length
                }
            }
        }

        // Catch any trailing placeholder value
        if (currentPos < remainingText.length) {
            placeholderValues.add(remainingText.substring(currentPos))
        }

        // Build annotated string with highlighted placeholder values
        val annotatedString = buildAnnotatedString {
            for (i in literalParts.indices) {
                append(literalParts[i])

                if (i < placeholderValues.size) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(placeholderValues[i])
                    }
                }
            }
        }

        Text(
            modifier = modifier,
            text = annotatedString,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
        )
    } else {
        Text(
            text = text,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = modifier
        )
    }
}


@Composable
private fun AmityLiveStreamInvitationAvatarView(
    user: AmityUser?,
    roomId: String?,
) {
    // Observe the room so the live badge only appears while the stream is actually live.
    val roomFlow = remember(roomId) {
        if (roomId.isNullOrEmpty()) {
            emptyFlow<AmityRoom>()
        } else {
            AmityVideoClient.newRoomRepository()
                .getRoom(roomId)
                .asFlow()
                .catch { /* room unavailable (deleted, network error) — keep the badge hidden */ }
        }
    }
    val room by roomFlow.collectAsState(initial = null)
    val isLive = room?.getStatus() == AmityRoomStatus.LIVE

    Box(
        modifier = Modifier.size(32.dp)
    ) {
        AmityUserAvatarView(
            user = user,
            size = 32.dp,
            modifier = Modifier
                .align(Alignment.Center)
        )

        if (isLive) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(16.dp) // Total size of the badge including white border
                    .clip(CircleShape) // Clip the outer box to a circle
                    .background(AmityTheme.colors.background) // This provides the white border
                    .padding(1.dp) // Inner padding to create the white border thickness
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(amityLiveBadgeRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_live_badge),
                        contentDescription = "Live Icon",
                        modifier = Modifier.size(9.dp),
                        tint = amityColorWhite
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightTextPreview() {
    HighlightText(
        text = "Alice and 5 others reacted to your post.",
        templatedText = "{{ userId: 67f4e40030a5dea19d8a187f }} and {{5 others}} reacted to your post."
    )
}