package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.common.readableTimeLeft
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.elements.AmityRoundCheckbox
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostPollElementViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.any
import kotlin.collections.find
import kotlin.collections.firstOrNull
import kotlin.collections.forEachIndexed
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.partition
import kotlin.collections.plus
import kotlin.collections.sortedWith
import kotlin.collections.sumOf
import kotlin.collections.take
import kotlin.comparisons.thenBy
import kotlin.let
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow
import kotlin.text.format
import kotlin.text.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPostPollElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    boldedText: String? = null,
    onClick: () -> Unit,
    onMentionedUserClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityPostPollElementViewModel>(
        factory = AmityPostPollElementViewModel.create(post.getPostId()),
        viewModelStoreOwner = viewModelStoreOwner
    )

    // Stabilize poll state with better keys
    val pollStateKey = remember(post.getPostId(), post.getUpdatedAt()) {
        "${post.getPostId()}_${post.getUpdatedAt()}"
    }

    val pollStateUiState by viewModel.uiState.collectAsState()

    val MAX_OPTION_PREVIEWS = 4
    val context = LocalContext.current
    val data = post.getChildren().firstOrNull()?.getData()
    val isPollPost = (data is AmityPost.Data.POLL)
    if (!isPollPost) return

    val mentionGetter = AmityMentionMetadataGetter(post.getMetadata() ?: JsonObject())
    val hashtagGetter = AmityHashtagMetadataGetter(post.getMetadata() ?: JsonObject())
    // Use more specific keys and avoid unnecessary recompositions
    val pollData by remember(post.getPostId()) {
        (data as? AmityPost.Data.POLL)!!.getPoll().asFlow()
    }.collectAsState(initial = null)

    var isOptionsExpanded by remember(pollStateUiState) {
        mutableStateOf(
            style == AmityPostContentComponentStyle.DETAIL || post.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW || pollStateUiState.find { it.postId == post.getPostId() }?.isExpanded == true
        )
    }
    // Optimize selectedIndices with stable state
    val selectedIndices = remember(pollStateKey) {
        mutableStateListOf<Int>()
    }
    var isVoting by remember {
        mutableStateOf(false)
    }
    val canVote by remember(isVoting, post.getPostId(), post.getUpdatedAt()) {
        derivedStateOf {
            if (post.getTarget() is AmityPost.Target.COMMUNITY) {
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

    val textTypePoll = poll.getAnswers().any { it.dataType == "text" }

    val isEndedState = mutableStateOf(
        poll.getClosedAt().isBefore(DateTime.now()) || poll.getStatus() == AmityPoll.Status.CLOSED
    )

    val isEnded by remember {
        isEndedState
    }

    var isResultState by remember {
        mutableStateOf(
            post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
                    && (poll.isVoted()
                    || poll.getClosedAt().isBefore(DateTime.now())
                    || poll.getStatus() == AmityPoll.Status.CLOSED
                    || (style == AmityPostContentComponentStyle.DETAIL && AmitySocialBehaviorHelper.showPollResultInDetailFirst)
                    || pollStateUiState.find { it.postId == post.getPostId() }?.isResultMode == true
                    || isEnded
                    )
        )
    }

    var showMediaDialog by remember { mutableStateOf(false) }
    var selectedPreviewImage by remember { mutableStateOf<AmityImage?>(null) }

    LaunchedEffect(poll.isVoted(), poll.getClosedAt(), poll.getStatus(), post.getReviewStatus()) {
        if (poll.isVoted() || poll.getClosedAt()
                .isBefore(DateTime.now()) || poll.getStatus() == AmityPoll.Status.CLOSED
        ) {
            if (post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW) isResultState = true
        } else if (!poll.isVoted()) {
            isResultState = false
        }
    }

    // Use derivedStateOf for expensive calculations
    val totalVote by remember {
        derivedStateOf {
            pollData?.getAnswers()?.sumOf { it.voteCount } ?: 0
        }
    }
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        if (style == AmityPostContentComponentStyle.DETAIL && AmitySocialBehaviorHelper.showPollResultInDetailFirst) {
            AmitySocialBehaviorHelper.showPollResultInDetailFirst = false
        }

        // Get title from post first, then from poll if not available
        val title = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
            (post.getData() as? AmityPost.Data.TEXT)?.getTitle() ?: poll.getTitle() ?: ""
        }

        // Display title if available
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = AmityTheme.typography.titleBold.copy(
                    fontSize = 17.sp,
                    textAlign = TextAlign.Start
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        AmityExpandableText(
            modifier = modifier,
            text = (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: poll.getQuestion(),
            mentionGetter = mentionGetter,
            mentionees = post.getMentionees(),
            hashtagGetter = hashtagGetter,
            style = AmityTheme.typography.bodyLegacy,
            boldWhenMatches = boldedText?.let { listOf(it) } ?: emptyList(),
            intialExpand = style == AmityPostContentComponentStyle.DETAIL,
            onClick = onClick,
            onMentionedUserClick = onMentionedUserClick,
            onHashtagClick = onHashtagClick,
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

            if (textTypePoll) {
                val showExpandedButton =
                    poll.getAnswers().size > MAX_OPTION_PREVIEWS && !isOptionsExpanded
                val itemsToShow = if (showExpandedButton) {
                    poll.getAnswers().take(MAX_OPTION_PREVIEWS)
                } else {
                    poll.getAnswers()
                }

                itemsToShow.forEachIndexed { index, answer ->
                    val text = answer.data
                    val isOptionSelected =
                        pollStateUiState.find { it.postId == post.getPostId() }?.selectedOption?.contains(
                            index
                        ) ?: selectedIndices.contains(index)
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .heightIn(min = 48.dp)
                            .clickableWithoutRipple {
                                if (canVote) {
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
                                viewModel.updatePollState(
                                    post.getPostId(),
                                    isExpanded = isOptionsExpanded,
                                    isResultMode = isResultState,
                                    selectedOption = selectedIndices,
                                )
                            }
                            .border(
                                if (isOptionSelected) 2.dp else 1.dp,
                                if (isOptionSelected) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (canVote) AmityTheme.colors.base else AmityTheme.colors.baseShade3
                            ),
                            overflow = TextOverflow.Ellipsis,
                            text = answer.data,
                        )

                        if (poll.getAnswerType() == AmityPoll.AnswerType.SINGLE) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                                RadioButton(
                                    modifier = modifier.testTag(text),
                                    enabled = canVote,
                                    selected = isOptionSelected,//selectedIndices.contains(index),
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
                                isChecked = isOptionSelected,//selectedIndices.contains(index),
                                onValueChange = {
                                    if (selectedIndices.contains(index)) {
                                        selectedIndices.remove(index)
                                    } else {
                                        selectedIndices.add(index)
                                    }
                                },
                            )
                        }
                    }
                }
                if (showExpandedButton) {
                    Spacer(modifier = Modifier.height(8.dp))

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
                                //onClick() it should go to post detail page due to state of expanded cannot survive recomposition
                                isOptionsExpanded = true
                                viewModel.updatePollState(
                                    post.getPostId(),
                                    isExpanded = isOptionsExpanded,
                                    isResultMode = isResultState
                                )
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
            } else {
                AmityPostImagePollElement(
                    modifier = Modifier.padding(bottom = 8.dp),
                    answers = poll.getAnswers(),
                    canVote = canVote,
                    isExpanded = isOptionsExpanded,
                    isSingleSelected = poll.getAnswerType() == AmityPoll.AnswerType.SINGLE,
                    selectedIndex = pollStateUiState.find { it.postId == post.getPostId() }?.selectedOption
                        ?: selectedIndices,//selectedIndices,
                    selectedVote = { index ->
                        if (canVote) {
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
                        viewModel.updatePollState(
                            post.getPostId(),
                            isExpanded = isOptionsExpanded,
                            isResultMode = isResultState,
                            selectedOption = selectedIndices,
                        )
                    },
                    imagePreviewClick = {
                        showMediaDialog = true
                        selectedPreviewImage = it
                    },
                    onExpandClick = {
                        isOptionsExpanded = true
                        viewModel.updatePollState(
                            post.getPostId(),
                            isExpanded = isOptionsExpanded,
                            isResultMode = isResultState
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary,
                    disabledContainerColor = AmityTheme.colors.primaryShade2,
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                enabled = canVote && selectedIndices.isNotEmpty() || pollStateUiState.find { it.postId == post.getPostId() }?.selectedOption?.isNotEmpty() == true,
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
                                    pollStateUiState.find { it.postId == post.getPostId() }?.selectedOption?.map { poll.getAnswers()[it].id }
                                        ?: selectedIndices.map { poll.getAnswers()[it].id }
                                )
                                .await()

                            isResultState = true
                        } catch (e: Exception) {
                            if (e is AmityException) {
                                if (e.code == AmityError.BAD_REQUEST_ERROR.code) {
                                    isEndedState.value = true
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                        message = "Poll ended.",
                                        offsetFromBottom = 52,
                                    )
                                    return@launch
                                } else {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                        message = "Oops, something went wrong.",
                                        offsetFromBottom = 52,
                                    )
                                }
                            }
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
            val results =
                votedOptions.sortedWith(compareByDescending<AmityPollAnswer> { it.voteCount }
                    .thenBy { answersOrder.indexOf(it) }) + noVoteOptions

            if (textTypePoll) {
                results.forEachIndexed { index, answer ->
                    if (index + 1 > MAX_OPTION_PREVIEWS && !isOptionsExpanded) {
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
                                    if (displayVoteCount == "1") {
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
                                    user = AmityCoreClient.getCurrentUser().blockingFirst()
                                } catch (e: Exception) {
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
            } else {
                AmityPostImagePollElement(
                    modifier = Modifier.padding(bottom = 8.dp),
                    answers = results,
                    canVote = canVote,
                    isExpanded = isOptionsExpanded,
                    viewResultMode = true,
                    totalVoteCount = totalVote,
                    imagePreviewClick = {
                        showMediaDialog = true
                        selectedPreviewImage = it
                    },
                    onExpandClick = {
                        isOptionsExpanded = true
                        viewModel.updatePollState(
                            post.getPostId(),
                            isExpanded = isOptionsExpanded,
                            isResultMode = isResultState
                        )
                    }
                )
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
                    .isBefore(DateTime.now()) || poll.getStatus() == AmityPoll.Status.CLOSED || isEnded
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

            if (poll.getStatus() != AmityPoll.Status.CLOSED
                && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW
            ) {
                if (
                    post.getCreatorId() == AmityCoreClient.getUserId()
                    && !poll.isVoted()
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            if (style == AmityPostContentComponentStyle.DETAIL) {
                                // switch to result
                                isResultState = !isResultState
                            } else {
                                // go to detail page
                                isResultState = !isResultState
                                viewModel.updatePollState(
                                    post.getPostId(),
                                    isExpanded = isOptionsExpanded,
                                    isResultMode = isResultState
                                )
                            }
                        },
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        text = if (!isResultState) "See results" else "Back to vote",
                    )
                } else if (poll.isVoted()) {
                    Text(
                        modifier = Modifier.clickable {
                            scope.launch {
                                isVoting = true
                                try {
                                    AmitySocialClient.newPollRepository()
                                        .unvotePoll(poll.getPollId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnComplete {
                                            selectedIndices.clear()
                                            isResultState = false
                                            val text = "Vote removed."
                                            pageScope?.showSnackbar(
                                                message = text,
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                                additionalHeight = 16,
                                            )
                                        }
                                        .doOnError { e ->
                                            val text = if (e is AmityException && e.code == AmityError.BAD_REQUEST_ERROR.code) {
                                                isEndedState.value = true
                                                "Poll ended."
                                            } else {
                                                "Oops, something went wrong."
                                            }
                                            pageScope?.showSnackbar(
                                                message = text,
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                                additionalHeight = 16,
                                            )
                                        }
                                        .doFinally {
                                            isVoting = false
                                        }
                                        .subscribe()

                                    // Reset result state to show voting options
                                    isResultState = false
                                } catch (e: Exception) {

                                } finally {

                                }
                            }
                        },
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        text = "Unvote",
                    )
                }
            }
        }

        if (style != AmityPostContentComponentStyle.DETAIL) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (showMediaDialog) {
            selectedPreviewImage?.let {
                AmityPollMediaPreviewDialog(
                    image = it,
                    isPostCreator = post.getCreatorId() == AmityCoreClient.getUserId(),
                    onDismiss = {
                        showMediaDialog = false
                        selectedPreviewImage = null
                    },
                )
            }

        }

    }
}
