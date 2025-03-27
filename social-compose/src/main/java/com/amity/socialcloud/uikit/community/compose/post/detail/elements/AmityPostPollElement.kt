package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.common.readableTimeLeft
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.elements.AmityRoundCheckbox
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPostPollElement(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    onClick: () -> Unit,
    onMentionedUserClick: (String) -> Unit = {},
) {
    val MAX_OPTION_PREVIEWS = 4
    val context = LocalContext.current
    val data = post.getChildren().firstOrNull()?.getData()
    val isPollPost = (data is AmityPost.Data.POLL)
    if (!isPollPost) return

    val mentionGetter = AmityMentionMetadataGetter(post.getMetadata() ?: JsonObject())
    val pollData by (data as? AmityPost.Data.POLL)!!.getPoll().asFlow().collectAsState(
        initial = null
    )
    var isOptionsExpanded by remember {
        mutableStateOf(style == AmityPostContentComponentStyle.DETAIL || post.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW)
    }
    val selectedIndices = remember { mutableStateListOf<Int>() }
    var isVoting by remember {
        mutableStateOf(false)
    }
    val canVote by remember(isVoting, post) {
        derivedStateOf {
            if(post.getTarget() is AmityPost.Target.COMMUNITY) {
                (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()?.isJoined() ?: false
                        && !isVoting
                        && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
            } else {
                !isVoting && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
            }
        }
    }

    val scope = rememberCoroutineScope()

    if (pollData == null) return

    val poll = pollData!!

    var isResultState by remember {
        mutableStateOf(
            post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
                    && (poll.isVoted()
                    || poll.getClosedAt().isBefore(DateTime.now())
                    || poll.getStatus() == AmityPoll.Status.CLOSED
                    || (style == AmityPostContentComponentStyle.DETAIL && AmitySocialBehaviorHelper.showPollResultInDetailFirst)
                    )
        )
    }

    LaunchedEffect(poll.isVoted(), poll.getClosedAt(), poll.getStatus(), post.getReviewStatus()) {
        if (poll.isVoted() || poll.getClosedAt().isBefore(DateTime.now()) || poll.getStatus() == AmityPoll.Status.CLOSED) {
            if(post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW) isResultState = true
        }
    }

    var totalVote by remember(pollData) {
        mutableIntStateOf(poll.getAnswers().sumOf { it.voteCount })
    }
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        if (style == AmityPostContentComponentStyle.DETAIL && AmitySocialBehaviorHelper.showPollResultInDetailFirst) {
            AmitySocialBehaviorHelper.showPollResultInDetailFirst = false
        }

        AmityExpandableText(
            modifier = modifier,
            text = (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: poll.getQuestion(),
            mentionGetter = mentionGetter,
            mentionees = post.getMentionees(),
            style = AmityTheme.typography.bodyLegacy,
            intialExpand = style == AmityPostContentComponentStyle.DETAIL,
            onClick = onClick,
            onMentionedUserClick = onMentionedUserClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isResultState) {

            Text(
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.colors.baseShade2,
                    fontWeight = FontWeight.SemiBold
                ),
                text = if (poll.getAnswerType() == AmityPoll.AnswerType.SINGLE) "Select one option" else "Select one or more options",
            )

            Spacer(modifier = Modifier.height(8.dp))

            poll.getAnswers().forEachIndexed { index, answer ->
                if (index + 1 > MAX_OPTION_PREVIEWS && !isOptionsExpanded) {
                    return@forEachIndexed
                }
                val text = answer.data
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .heightIn(min = 48.dp)
                        .clickableWithoutRipple {
                            if(canVote) {
                                if (poll.getAnswerType() == AmityPoll.AnswerType.SINGLE) {
                                    selectedIndices.clear()
                                    selectedIndices.add(index)
                                } else if (poll.getAnswerType() == AmityPoll.AnswerType.MULTIPLE) {
                                    if (selectedIndices.contains(index)) {
                                        selectedIndices.remove(index)
                                    } else {
                                        selectedIndices.add(index)
                                    }
                                } else {
                                    // Do nothing
                                }
                            }
                        }
                        .border(
                            1.dp,
                            if (selectedIndices.contains(index)) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal =  12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if(canVote) AmityTheme.colors.base else AmityTheme.colors.baseShade3
                        ),
                        overflow = TextOverflow.Ellipsis,
                        text = answer.data,
                    )

                    if (poll.getAnswerType() == AmityPoll.AnswerType.SINGLE) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                            RadioButton(
                                modifier = modifier.testTag(text),
                                enabled = canVote,
                                selected = selectedIndices.contains(index),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AmityTheme.colors.primary,
                                    unselectedColor = AmityTheme.colors.baseShade2,
                                ),
                                onClick = {
                                    selectedIndices.clear()
                                    selectedIndices.add(index)
                                },
                            )
                        }
                    } else {
                        AmityRoundCheckbox(
                            enabled = canVote,
                            isChecked = selectedIndices.contains(index),
                            onValueChange = {
                                if (selectedIndices.contains(index)) {
                                    selectedIndices.remove(index)
                                } else {
                                    selectedIndices.add(index)
                                }
                            }
                        )
                    }
                }

            }

            if (poll.getAnswers().size > MAX_OPTION_PREVIEWS && !isOptionsExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, AmityTheme.colors.baseShade3),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickableWithoutRipple {
                            // go to detail page
                            onClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "See ${poll.getAnswers().size - MAX_OPTION_PREVIEWS} more options",//getConfig().getText(),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.secondary,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary,
                    disabledContainerColor = AmityTheme.colors.primaryShade2,
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                enabled = canVote && selectedIndices.isNotEmpty(),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                onClick = {
                    scope.launch {
                        isVoting = true
                        try {
                            AmitySocialClient.newPollRepository()
                                .votePoll(
                                    poll.getPollId(),
                                    selectedIndices.map { poll.getAnswers()[it].id }
                                )
                                .await()

                            isResultState = true
                        } catch (e: Exception) {
                            val text =
                                "Failed to vote poll. Please try again."
                            componentScope.showSnackbar(
                                message = text,
                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                additionalHeight = 52,
                            )
                        } finally {
                            isVoting = false
                        }
                    }
                }
            ) {
                Text(
                    text = "Vote",
                    style = AmityTheme.typography.captionLegacy.copy(
                        color = Color.White,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

        } else { // result state
            val answersOrder = poll.getAnswers()
            val (votedOptions, noVoteOptions) = answersOrder.partition { it.voteCount > 0 }
            val results = votedOptions.sortedWith(compareByDescending<AmityPollAnswer> { it.voteCount }
                .thenBy { answersOrder.indexOf(it) }) + noVoteOptions
            results.forEachIndexed { index, answer ->
                if (index + 1 > MAX_OPTION_PREVIEWS
                    && !isOptionsExpanded
                ) {
                    return@forEachIndexed
                }
                val isTopAnswer = answer.voteCount > 0 && !poll.getAnswers()
                    .any { it.voteCount > answer.voteCount }
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .border(
                            1.dp,
                            if (isTopAnswer) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = answer.data,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.base,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        val percentage = (answer.voteCount / totalVote.toDouble()) * 100
                        val formattedPercentage = if (answer.voteCount == 0) {
                            "0%"
                        } else if (percentage % 1.0 == 0.0) {
                            // If the percentage is a whole number, format without decimal places
                            "${percentage.toInt()}%"
                        } else {
                            // Otherwise, format to 2 decimal places
                            "%.2f%%".format(percentage)
                        }

                        Text(
                            text = formattedPercentage,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = if (isTopAnswer) AmityTheme.colors.primary else AmityTheme.colors.baseShade1,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        val voteCount = answer.voteCount
                        fun getNextThreshold(count: Int): Int {
                            // If count is below 1000, just return it as-is
                            if (count < 1000) return count

                            // Calculate exponent as in Flutter: floor(log(count) / log(1000))
                            val exp = floor(log(count.toDouble(), 1000.0)).toInt()

                            // Calculate the “scale” (1000^exp)
                            val scale = 1000.0.pow(exp)

                            // Next threshold is scale * ceil(count / scale)
                            return (scale * ceil(count / scale)).toInt()
                        }
                        val votedBy = when {
                            voteCount == 0 -> {
                                "No votes"
                            }
                            voteCount == 1 && answer.isVotedByUser -> {
                                "Voted by you"
                            }
                            voteCount == 1 -> {
                                "Voted by 1 participant"
                            }
                            else -> {
                                // Determine if plus sign is needed
                                val nextThreshold = getNextThreshold(voteCount)
                                val plusSign = if (voteCount > nextThreshold) "+" else ""

                                // Adjust display count if current user is among the voters
                                val displayVoteCount = if (answer.isVotedByUser) {
                                    (voteCount - 1).readableNumber()
                                } else {
                                    voteCount.readableNumber()
                                }

                                // Construct the base text
                                var text = "Voted by $displayVoteCount${plusSign} participants"
                                if(displayVoteCount == "1") {
                                    text = "Voted by 1 participant"
                                }

                                // Append "and you" if the current user is also a voter
                                if (answer.isVotedByUser) {
                                    text += " and you"
                                }
                                text
                            }
                        }

                        Text(
                            text = votedBy,
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        if (answer.isVotedByUser) {
                            var user: AmityUser? = null
                            try {
                                user =  AmityCoreClient.getCurrentUser().blockingFirst()
                            } catch (e : Exception) {
                                // missing user
                            }
                            AmityUserAvatarView(
                                size = 16.dp,
                                user = user
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        if (answer.voteCount > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(answer.voteCount / totalVote.toFloat())
                                    .height(8.dp)
                                    .background(
                                        color = if (isTopAnswer) AmityTheme.colors.primary else AmityTheme.colors.baseShade1,
                                        shape = if (answer.voteCount == totalVote) RoundedCornerShape(
                                            99.dp
                                        ) else RoundedCornerShape(
                                            topStart = 99.dp,
                                            bottomStart = 99.dp
                                        )
                                    )
                            )
                        }
                        val remainingVoteWeight =
                            if (totalVote == 0) 1f else (1 - (answer.voteCount / totalVote.toFloat()))
                        if (remainingVoteWeight > 0.0f) {
                            Box(
                                modifier = Modifier
                                    .weight(remainingVoteWeight)
                                    .height(8.dp)
                                    .background(
                                        color = if (isTopAnswer) AmityTheme.colors.primaryShade3 else AmityTheme.colors.baseShade4,
                                        shape = if (answer.voteCount == 0) RoundedCornerShape(99.dp) else RoundedCornerShape(
                                            topEnd = 99.dp,
                                            bottomEnd = 99.dp
                                        )
                                    )
                            )
                        }
                    }
                }
            }

            if (
                poll.getAnswers().size > MAX_OPTION_PREVIEWS
                && style == AmityPostContentComponentStyle.FEED
                && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    enabled = true,
                    border = BorderStroke(1.dp, AmityTheme.colors.baseShade3),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    onClick = {
                        // go to detail page
                        onClick()
                    }
                ) {
                    Text(
                        text = "See full results",
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.secondary,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // footer
        Row {
            val endText = if (post.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                val formatter = DateTimeFormat.forPattern("dd MMM 'at' h:mm a")
                    .withLocale(context.resources.configuration.locale)
                "Ends on ${poll.getClosedAt().toString(formatter)}"
            } else if (poll.getClosedAt()
                    .isBefore(DateTime.now()) || poll.getStatus() == AmityPoll.Status.CLOSED
            ) {
                "Ended"
            } else {
                poll.getClosedAt().readableTimeLeft(DateTime.now())
            }
            val totalVotes = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalVote)

            Text(
                modifier = Modifier.weight(1f),
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.colors.baseShade2,
                    fontWeight = FontWeight.SemiBold
                ),
                text = "$totalVotes votes" + " • " + endText,
            )

            if (
                post.getCreatorId() == AmityCoreClient.getUserId()
                && !poll.isVoted()
                && poll.getStatus() != AmityPoll.Status.CLOSED
                && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
            ) {
                Text(
                    modifier = Modifier.clickable {
                        if (style == AmityPostContentComponentStyle.DETAIL) {
                            // switch to result
                            isResultState = !isResultState
                        } else {
                            // go to detail page
                            AmitySocialBehaviorHelper.showPollResultInDetailFirst = true
                            onClick()
                        }
                    },
                    style = AmityTheme.typography.captionLegacy.copy(
                        color = AmityTheme.colors.primary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    text = if (!isResultState) "See results" else "Back to vote",
                )
            }
        }

        if (style != AmityPostContentComponentStyle.DETAIL) {
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

