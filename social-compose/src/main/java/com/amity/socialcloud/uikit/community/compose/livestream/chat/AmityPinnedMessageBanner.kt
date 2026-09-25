package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.message.AmityPinnedMessage
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityLivestreamChatBubbleBackground
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ColorFilter
import com.amity.socialcloud.uikit.common.R as CommonR

/**
 * The pinned message banner above the live chat feed.
 *
 * Anchored above the message list and outside it, so it never scrolls with the messages. The
 * whole bubble is the tap target for expand / collapse: "more" and "less" are labels, not
 * buttons. The "X" is the only separate target inside the bubble, and it only appears for users
 * who can pin.
 */
@Composable
fun AmityPinnedMessageBanner(
    modifier: Modifier = Modifier,
    pinnedMessage: AmityPinnedMessage?,
    canPin: Boolean,
    isLive: Boolean,
    mutedMemberIds: List<String> = emptyList(),
    hostUserId: String? = null,
    coHostUserId: String? = null,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    onUnpinClick: (String) -> Unit = {},
    onExpandedChanged: (String, Boolean) -> Unit = { _, _ -> },
) {
    // The server keeps the pin after the stream ends; hiding it off-live is a UIKit rule.
    if (pinnedMessage == null || !isLive) return

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "pinned_message_banner"
    ) {
        val messageId = pinnedMessage.getMessageId()
        // Keyed to the message, so a pin that replaces another re-collapses without a remount.
        var isExpanded by remember(messageId) { mutableStateOf(false) }
        var hasOverflow by remember(messageId) { mutableStateOf(false) }

        val author by remember(pinnedMessage.getCreatorPublicId()) {
            AmityCoreClient.newUserRepository()
                .getUser(pinnedMessage.getCreatorPublicId())
                .subscribeOn(Schedulers.io())
                .asFlow()
                .catch { }
        }.collectAsState(initial = null)

        val text = (pinnedMessage.getData() as? AmityMessage.Data.TEXT)?.getText() ?: ""
        val isAuthorMuted = mutedMemberIds.contains(pinnedMessage.getCreatorPublicId())
        val isAuthorHost = hostUserId != null && pinnedMessage.getCreatorPublicId() == hostUserId
        val isAuthorCoHost = coHostUserId != null && pinnedMessage.getCreatorPublicId() == coHostUserId

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    // Derived from the chat feed's own background so a config theme override
                    // carries the banner with it; the wash keeps the video readable behind.
                    color = AmityTheme.colors.background.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .testTag(getAccessibilityId())
        ) {
            // The tap target is the whole bubble, but it sits BEHIND the content rather
            // than on the container: Modifier.clickable merges the container's semantics
            // and would drop the byline, body and more/less label out of the
            // accessibility tree. Non-interactive content does not consume taps, so they
            // fall through to here, while the "X" in front keeps its own hit area.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickableWithoutRipple {
                        // A body that fits on one line has nothing to expand.
                        if (hasOverflow) {
                            isExpanded = !isExpanded
                            onExpandedChanged(messageId, isExpanded)
                        }
                    }
            )
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = author?.getDisplayName()
                            ?: DefaultAmitySocialStringProvider.getInstance()
                                .getString("amity_social_button_unknown_user_lowercase"),
                        color = AmityTheme.colors.baseShade1,
                        style = AmityTheme.typography.captionSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    if (isAuthorHost || isAuthorCoHost) {
                        HostBadge(
                            isCoHost = isAuthorCoHost && !isAuthorHost,
                            onCohostBadgeClick = {},
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    if (isAuthorMuted) {
                        MutedBadge()
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PinnedPill()
                        if (canPin) {
                            AmityBaseElement(
                                componentScope = componentScope,
                                elementId = "unpin_message_button"
                            ) {
                                Box(
                                    // Its own target: unpinning must not also toggle the bubble.
                                    // The box keeps a usable tap area around a glyph deliberately
                                    // smaller than the "Pinned" pill beside it.
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .size(16.dp)
                                        .clickableWithoutRipple { onUnpinClick(messageId) }
                                        .testTag(getAccessibilityId()),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = CommonR.drawable.amity_ic_close3),
                                        contentDescription = DefaultAmitySocialStringProvider
                                            .getInstance()
                                            .getString("amity_social_button_unpin_message"),
                                        colorFilter = ColorFilter.tint(AmityTheme.colors.baseInverse),
                                        modifier = Modifier.size(12.dp)
                                            .padding(vertical = 2.dp),
                                    )
                                }
                            }
                        }
                    }
                }
                // Body and label as one paragraph: the label sits inline at the end of the
                // truncated line and underlined, matching AmityExpandableText (readMoreInline,
                // readMoreUnderlined). That component only expands, so the collapse half is here.
                BoxWithConstraints(modifier = Modifier.padding(top = 2.dp)) {
                    val bodyStyle = AmityTheme.typography.caption
                    val measurer = rememberTextMeasurer()
                    val layout = remember(text, constraints.maxWidth) {
                        measurer.measure(
                            text = text,
                            style = bodyStyle,
                            constraints = Constraints(maxWidth = constraints.maxWidth)
                        )
                    }
                    val overflows = layout.lineCount > 1
                    LaunchedEffect(overflows) { hasOverflow = overflows }

                    val label = DefaultAmitySocialStringProvider.getInstance().getString(
                        if (isExpanded) {
                            "amity_social_label_pinned_message_less"
                        } else {
                            "amity_social_label_pinned_message_more"
                        }
                    )
                    val maxWidth = constraints.maxWidth
                    val body = remember(text, maxWidth, isExpanded, label) {
                        when {
                            !overflows -> text
                            isExpanded -> "$text "
                            else -> trimForInlineLabel(text, layout.getLineEnd(0), label) { candidate ->
                                measurer.measure(
                                    text = candidate,
                                    style = bodyStyle,
                                    constraints = Constraints(maxWidth = maxWidth)
                                ).lineCount <= 1
                            }
                        }
                    }

                    Text(
                        text = buildAnnotatedString {
                            append(body)
                            if (overflows) {
                                withStyle(
                                    SpanStyle(
                                        color = AmityTheme.colors.baseInverse,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) { append(label) }
                            }
                        },
                        color = AmityTheme.colors.baseInverse,
                        style = bodyStyle,
                    )
                }
            }
        }
    }
}

@Composable
private fun PinnedPill(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(
                // Same base as the message bubbles it sits above, lifted so the pill reads
                // against the banner rather than against the video.
                color = amityLivestreamChatBubbleBackground.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_product_tagging_pin_filled),
            contentDescription = null,
            tint = AmityTheme.colors.baseInverse,
            modifier = Modifier
                .size(14.dp)
                .padding(end = 2.dp)
        )
        Text(
            text = DefaultAmitySocialStringProvider.getInstance()
                .getString("amity_social_label_pinned_message_badge"),
            color = AmityTheme.colors.baseInverse,
            style = AmityTheme.typography.captionSmall,
        )
    }
}

/**
 * Cuts [text] back until it and the trailing label fit the first line together, so "more" sits
 * inline at the end of the ellipsis rather than wrapping onto a line of its own. Same intent as
 * the private getTrimmedText in AmityExpandableText, but measured rather than estimated: at one
 * preview line there is no slack for its character-count guess.
 */
private fun trimForInlineLabel(
    text: String,
    firstLineEnd: Int,
    label: String,
    fitsOneLine: (String) -> Boolean,
): String {
    var cut = firstLineEnd.coerceAtMost(text.length)
    while (cut > 1) {
        val head = text.take(cut).trimEnd()
        val ellipsised = if (head.endsWith("\u2026") || head.endsWith("...")) head else "$head\u2026"
        if (fitsOneLine("$ellipsised $label")) return "$ellipsised "
        cut -= 2
    }
    return "\u2026 "
}
