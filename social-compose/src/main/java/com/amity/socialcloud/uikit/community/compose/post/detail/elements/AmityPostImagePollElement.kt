package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityFileType
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.components.setImageDrawable
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.R
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlin.compareTo
import kotlin.hashCode
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow
import kotlin.text.chunked

@Composable
fun AmityPostImagePollElement(
    modifier: Modifier = Modifier,
    answers: List<AmityPollAnswer> = emptyList(),
    selectedVote: (Int) -> Unit = {},
    selectedIndex: List<Int> = emptyList(),
    isExpanded: Boolean = false,
    isSingleSelected: Boolean = true,
    viewResultMode: Boolean = false,
    canVote: Boolean = true,
    totalVoteCount: Int = 0,
    columns: Int = 2,
    itemSpacing: Int = 8,
    imagePreviewClick: (AmityImage) -> Unit = {},
    onExpandClick: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val totalSpacing = (itemSpacing * (columns - 1)).dp
    val itemWidth = (screenWidth - totalSpacing - 32.dp) / columns

    // State to store the maximum height
//    var maxHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // Use a more stable key for remember
    val stableAnswersKey = remember(answers) {
        answers.map { "${it.id}_${it.voteCount}_${it.isVotedByUser}" }.hashCode()
    }

    // State for expand/collapse
    //var internalExpanded by remember { mutableStateOf(isExpanded) }
    val maxItemsToShow = 4
    val shouldShowExpandButton = answers.size > maxItemsToShow && !isExpanded

    // Pre-calculate grouped polls to avoid recalculation
    val groupedPolls = remember(stableAnswersKey, columns, isExpanded) {
        val itemsToShow = if (answers.size > 4 && !isExpanded) {
            answers.take(4)
        } else {
            answers
        }
        itemsToShow.chunked(columns)
    }

    // Optimize height calculation with stable keys
    val rowHeights = remember(stableAnswersKey, columns, isExpanded) {
        mutableStateOf(List(answers.chunked(columns).size) { 0.dp })
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing.dp)
    ) {
        groupedPolls.forEachIndexed { rowIndex, rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(itemSpacing.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEachIndexed { columnIndex, answer ->
                    val actualIndex = rowIndex * columns + columnIndex
                    val maxVoteCount = answers.maxOfOrNull { it.voteCount } ?: 0
                    val isTopAnswer = answer.voteCount > 0 && answer.voteCount == maxVoteCount

                    PollItem(
                        answer = answer,
                        modifier = Modifier
                            .width(itemWidth)
                            .then(
                                //Logic to handle height by all element using most max height dynamically
//                                if (maxHeight > 0.dp) Modifier.height(maxHeight)
//                                else Modifier.onGloballyPositioned { coordinates ->
//                                    val height = with(density) { coordinates.size.height.toDp() }
//                                    if (height > maxHeight) {
//                                        maxHeight = height
//                                    }
//                                }

                                if (rowHeights.value[rowIndex] > 0.dp) {
                                    Modifier.height(rowHeights.value[rowIndex])
                                } else {
                                    Modifier.onGloballyPositioned { coordinates ->
                                        val height =
                                            with(density) { coordinates.size.height.toDp() }
                                        if (height > rowHeights.value[rowIndex]) {
                                            val newHeights = rowHeights.value.toMutableList()
                                            newHeights[rowIndex] = height
                                            rowHeights.value = newHeights
                                        }
                                    }
                                }
                            ),
                        canVote = canVote,
                        isSingleSelected = isSingleSelected,
                        viewResultMode = viewResultMode,
                        isSelected = if (viewResultMode) isTopAnswer else selectedIndex.contains(
                            actualIndex
                        ),
                        totalVoteCount = totalVoteCount,
                        imagePreviewClick = {
                            imagePreviewClick(it)
                        },
                        selectAnswer = {
                            selectedVote(actualIndex)
                        }
                    )
                }

                // Fill remaining space if last row has fewer items
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.width(itemWidth))
                }
            }
        }

        // Show expand/collapse button if needed
        if (shouldShowExpandButton) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = AmityTheme.colors.baseShade3,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickableWithoutRipple {
                        //internalExpanded = !internalExpanded
                        onExpandClick()
                    }
                    .padding(vertical = 10.dp), //inner padding,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (viewResultMode) "See full results" else "See ${answers.size - maxItemsToShow} more options",
                    style = AmityTheme.typography.bodyBold,
                    color = AmityTheme.colors.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PollItem(
    modifier: Modifier = Modifier,
    answer: AmityPollAnswer,
    isSelected: Boolean = false,
    isSingleSelected: Boolean = true,
    canVote: Boolean = true,
    viewResultMode: Boolean = false,
    totalVoteCount: Int = 0,
    imagePreviewClick: (AmityImage) -> Unit = {},
    selectAnswer: () -> Unit = {},
) {
    val context = LocalContext.current

    // Stabilize the selected state
    var isLocalSelected by remember(isSelected) {
        mutableStateOf(isSelected)
    }

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

    // Only compute these values when viewResultMode is true
    // Optimize vote calculation with stable keys
    val (formattedPercentage, votedBy) = remember(
        viewResultMode,
        answer.voteCount,
        totalVoteCount,
        answer.isVotedByUser,
        answer.id // Add stable ID
    ) {
        if (viewResultMode) {
            val voteCount = answer.voteCount
            val calculatedPercentage =
                if (totalVoteCount > 0) (voteCount / totalVoteCount.toDouble()) * 100 else 0.0
            val calculatedFormattedPercentage = if (voteCount == 0) {
                "0%"
            } else if (calculatedPercentage % 1.0 == 0.0) {
                "${calculatedPercentage.toInt()}%"
            } else {
                "%.2f%%".format(calculatedPercentage)
            }

            val calculatedVotedBy = when {
                voteCount == 0 -> "No votes"
                voteCount == 1 && answer.isVotedByUser -> "Voted by you"
                voteCount == 1 -> "1 voter"
                else -> {
                    val nextThreshold = getNextThreshold(voteCount)
                    val kSign = if (voteCount > nextThreshold) "k" else ""
                    val displayVoteCount = if (answer.isVotedByUser) {
                        (voteCount - 1).readableNumber()
                    } else {
                        voteCount.readableNumber()
                    }
                    var text = "$displayVoteCount${kSign} voters"
                    if (displayVoteCount == "1") {
                        text = "1 voter"
                    }
                    text
                }
            }

            Pair(calculatedFormattedPercentage, calculatedVotedBy)
        } else {
            Pair("", "")
        }
    }

    // Optimize image loading with better caching
    val painter = rememberAsyncImagePainter(
        model = remember(answer.id) {
            ImageRequest.Builder(context)
                .data(answer.getImage()?.getUrl(AmityImage.Size.MEDIUM))
                .crossfade(300) // Reduce crossfade duration
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .placeholderMemoryCacheKey("poll_placeholder")
                .build()
        }
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, AmityTheme.colors.primary)
        } else {
            BorderStroke(1.dp, AmityTheme.colors.baseShade4)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .background(AmityTheme.colors.baseShade4, shape = RoundedCornerShape(4.dp))
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(4.dp))
                .clickableWithoutRipple(enabled = canVote && !viewResultMode) {
                    isLocalSelected = !isLocalSelected
                    selectAnswer()
                }
        ) {

            answer.getImage()?.let { image ->
                when (painter.state.value) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmerBackground(
                                    color = AmityTheme.colors.baseShade4,
                                )
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(R.drawable.amity_v4_poll_image_failed),
                            contentDescription = "Error Loading Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (viewResultMode) Modifier.blur(2.dp)
                                    else Modifier
                                ),
                            painter = painter,
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> { /* Do nothing, image will be displayed below */ }
                }
            }

            if (viewResultMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isSelected) AmityTheme.colors.primary.copy(alpha = 0.3f) else Color.Black.copy(
                                alpha = 0.3f
                            )
                        )
                ) {
                    Text(
                        text = formattedPercentage,
                        style = AmityTheme.typography.headLine,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.amity_v4_expand),
                contentDescription = "Image preview Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .clickableWithoutRipple {
                        answer.getImage()?.let {
                            imagePreviewClick(it)
                        }
                    }
            )

            if (isSelected && !viewResultMode) {
                Image(
                    painter = painterResource(if (isSingleSelected) R.drawable.amity_v4_radio_button else R.drawable.amity_v4_poll_multiple_select),
                    contentDescription = "Selected Icon",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopStart)
                )
            }
        }

        answer.data.let {
            if (it.isNotEmptyOrBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    text = it,
                    style = AmityTheme.typography.bodyBold,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (canVote) AmityTheme.colors.base else AmityTheme.colors.baseShade3
                )
            }

            if (!viewResultMode) {
                Spacer(Modifier.height(12.dp))
            }
        }

        if (viewResultMode) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                maxLines = 2,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier,
                    text = votedBy,
                    maxLines = 1,
                    color = AmityTheme.colors.baseShade2,
                    style = AmityTheme.typography.caption
                )
                if (answer.isVotedByUser && answer.voteCount > 1) {
                    Text(
                        modifier = Modifier,
                        text = " and you",
                        maxLines = 1,
                        color = AmityTheme.colors.baseShade2,
                        style = AmityTheme.typography.caption
                    )
                }
                Spacer(Modifier.width(4.dp))
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
        }
    }
}