package com.amity.socialcloud.uikit.community.compose.discoverywidget.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import org.joda.time.DateTime
import org.joda.time.Duration

private val OPTION_HEIGHT = 80.dp
private val OPTION_GAP = 8.dp
private val BAR_HEIGHT = 8.dp
private val OPTION_ROW_HEIGHT = 20.dp
private val OPTION_DETAILS_GAP = 0.dp
private val FOOTER_HEIGHT = 70.dp
private val RESULTS_ROW_HEIGHT = 40.dp
private val OPTIONS_TO_FOOTER_GAP = 16.dp
private val IMAGE_OPTION_HEIGHT = 181.6.dp
private val IMAGE_OPTION_THUMB_HEIGHT = 107.6.dp

/** The bar is ten discrete tenths, not a proportional fill: 34 % lights three, not 34 % of a width. */
private const val BAR_SEGMENTS = 10

/**
 * Read-only poll results, always revealed — the viewer never votes here and never needs to have
 * voted. An ended poll renders identically to an ongoing one; only the footer status differs.
 */
@Composable
fun AmityDiscoveryWidgetPollBlock(
    poll: AmityPoll,
    modifier: Modifier = Modifier,
) {
    val answers = poll.getAnswers()
    val totalVotes = answers.sumOf { it.voteCount }
    // Leader first. Ties keep authored order, so a zero-vote poll renders exactly as authored and
    // nothing is emphasised.
    val ordered = answers.withIndex()
        .sortedWith(compareByDescending<IndexedValue<AmityPollAnswer>> { it.value.voteCount }
            .thenBy { it.index })
        .map { it.value }
    val leaderId = ordered.firstOrNull()?.takeIf { totalVotes > 0 && it.voteCount > 0 }?.id

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(OPTIONS_TO_FOOTER_GAP),
    ) {
        // Options fill the budget in order; the leader survives the cap, and See full results is
        // always there for the rest.
        val visible = ordered.take(VISIBLE_OPTIONS)
        if (poll.isImagePoll()) {
            Row(horizontalArrangement = Arrangement.spacedBy(OPTION_GAP)) {
                visible.forEach { answer ->
                    ImageOption(
                        answer = answer,
                        totalVotes = totalVotes,
                        isLeading = answer.id == leaderId,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(OPTION_GAP)) {
                visible.forEach { answer ->
                    TextOption(
                        answer = answer,
                        totalVotes = totalVotes,
                        isLeading = answer.id == leaderId,
                    )
                }
            }
        }
        Footer(poll = poll, totalVotes = totalVotes)
    }
}

private const val VISIBLE_OPTIONS = 2

@Composable
private fun TextOption(answer: AmityPollAnswer, totalVotes: Int, isLeading: Boolean) {
    val share = if (totalVotes > 0) answer.voteCount * 100 / totalVotes else 0
    val accent = if (isLeading) AmityTheme.colors.primary else AmityTheme.colors.baseShade1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(OPTION_HEIGHT)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isLeading) 2.dp else 1.dp,
                color = if (isLeading) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(OPTION_DETAILS_GAP),
    ) {
        Row(
            modifier = Modifier.height(OPTION_ROW_HEIGHT),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = answer.data,
                style = AmityTheme.typography.bodyBold.copy(
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$share%",
                style = AmityTheme.typography.bodyBold.copy(color = accent),
                maxLines = 1,
            )
        }
        Text(
            text = if (answer.voteCount == 0) {
                amitySocialString("amity_social_button_image_poll_no_votes")
            } else {
                amitySocialString(
                    "amity_social_label_voted_by_participants",
                    answer.voteCount.readableNumber(),
                )
            },
            style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.height(OPTION_ROW_HEIGHT),
        )
        SegmentedBar(share = share, isLeading = isLeading)
    }
}

@Composable
private fun SegmentedBar(share: Int, isLeading: Boolean) {
    val filled = share / BAR_SEGMENTS
    val track = if (isLeading) AmityTheme.colors.primaryShade3 else AmityTheme.colors.baseShade4
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(BAR_HEIGHT)
            .clip(RoundedCornerShape(4.dp))
            .background(track),
    ) {
        repeat(BAR_SEGMENTS) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(BAR_HEIGHT)
                    .background(
                        if (isLeading && index < filled) AmityTheme.colors.primary
                        else Color.Transparent
                    )
            )
        }
    }
}

@Composable
private fun Footer(poll: AmityPoll, totalVotes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(FOOTER_HEIGHT),
    ) {
        // Decoration, not a control: the card's own click target already covers it.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RESULTS_ROW_HEIGHT)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, AmityTheme.colors.baseShade4, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = amitySocialString("amity_social_label_see_full_results"),
                style = AmityTheme.typography.bodyBold.copy(color = AmityTheme.colors.base),
                maxLines = 1,
            )
        }
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val caption = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2)
            Text(
                text = amitySocialString(
                    "amity_social_button_poll_voters",
                    totalVotes.readableNumber(),
                ),
                style = caption,
                maxLines = 1,
            )
            Text(text = "•", style = caption, modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = poll.statusLabel(), style = caption, maxLines = 1)
        }
    }
}

@Composable
private fun AmityPoll.statusLabel(): String {
    if (getStatus() is AmityPoll.Status.CLOSED) {
        return amitySocialString("amity_social_button_poll_ended")
    }
    val remaining = Duration(DateTime.now(), getClosedAt())
    return when {
        remaining.standardDays > 0 ->
            amitySocialString("amity_social_time_time_left_days", remaining.standardDays.toString())
        remaining.standardHours > 0 ->
            amitySocialString("amity_social_time_time_left_hours", remaining.standardHours.toString())
        remaining.standardMinutes > 0 ->
            amitySocialString("amity_social_time_time_left_minutes", remaining.standardMinutes.toString())
        else -> amitySocialString("amity_social_time_time_left_zero")
    }
}

/**
 * An image option is a card of its own: thumbnail with the share centred over a scrim, then the
 * caption and voter line. Two per row, each half of the content box -- computed, never hardcoded.
 */
@Composable
private fun ImageOption(
    answer: AmityPollAnswer,
    totalVotes: Int,
    isLeading: Boolean,
    modifier: Modifier = Modifier,
) {
    val share = if (totalVotes > 0) answer.voteCount * 100 / totalVotes else 0

    Column(
        modifier = modifier
            .height(IMAGE_OPTION_HEIGHT)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isLeading) 2.dp else 1.dp,
                color = if (isLeading) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IMAGE_OPTION_THUMB_HEIGHT)
                .clip(RoundedCornerShape(4.dp))
                .background(AmityTheme.colors.baseShade4),
            contentAlignment = Alignment.Center,
        ) {
            answer.getImage()?.getUrl(AmityImage.Size.MEDIUM)?.let { url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            // The scrim is what keeps the share legible over an arbitrary image.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Text(
                text = "$share%",
                style = AmityTheme.typography.titleBold.copy(
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = amityColorWhite,
                    textAlign = TextAlign.Center,
                ),
                maxLines = 1,
            )
        }
        Column {
            if (answer.data.isNotBlank()) {
                Text(
                    text = answer.data,
                    style = AmityTheme.typography.bodyBold.copy(
                        color = AmityTheme.colors.base,
                        textAlign = TextAlign.Start,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = when (answer.voteCount) {
                    0 -> amitySocialString("amity_social_button_no_votes")
                    1 -> amitySocialString("amity_social_label_voted_by_1_participant")
                    else -> amitySocialString(
                        "amity_social_label_voted_by_participants",
                        answer.voteCount.readableNumber(),
                    )
                },
                style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** An answer that carries an image is what makes this an image poll; dataType is the fallback. */
private fun AmityPoll.isImagePoll(): Boolean =
    getAnswers().any { it.getImage() != null || it.dataType.equals(IMAGE_ANSWER_TYPE, true) }

private const val IMAGE_ANSWER_TYPE = "image"
