package com.amity.socialcloud.uikit.common.reaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.reaction.elements.AmityReactionListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.pagingLoadStateItem
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityReactionRoot(
    modifier: Modifier = Modifier,
    state: AmityReactionListPageState,
    onAction: (AmityReactionListPageAction) -> Unit = {},
    onUserClick: (String) -> Unit = {},
) {

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityReactionListPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val pagerState = rememberPagerState {
        state.tabItems.size
    }

    LaunchedEffect(key1 = state.currentIndex) {
        pagerState.animateScrollToPage(state.currentIndex)
    }

    LaunchedEffect(key1 = pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            onAction(AmityReactionListPageAction.GoToTab(pagerState.currentPage))
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AmityReactionTab(
            state = state,
            onAction = { action ->
                onAction(action)
            }
        )
        HorizontalDivider(
            color = AmityTheme.colors.baseShade4,
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            val tab = state.tabItems[index]
            val reactionName = if (tab.isAllTab) null else tab.title
            val reactions = remember(viewModel, tab, reactionName) {
                viewModel.getReactions(reactionName)
            }.collectAsLazyPagingItems()

            AmityReactionItems(
                reactions = reactions,
                state = state,
                action = { action ->
                    onAction(action)
                },
                onUserClick = onUserClick,
            )
        }

    }
}

@Composable
fun AmityReactionTab(
    modifier: Modifier = Modifier,
    state: AmityReactionListPageState,
    onAction: (AmityReactionListPageAction) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(state.tabItems) { index: Int, tab: ReactionTab ->
            val isSelected = tab == state.tabItems[state.currentIndex]
            Column(
                modifier = Modifier.clickableWithoutRipple {
                    if (!isSelected) {
                        onAction(AmityReactionListPageAction.GoToTab(state.tabItems.indexOf(tab)))
                    }
                }
            ) {
                val title = if (tab.title == "All") "All" else ""
                val count = if (tab.count == 0) "0" else tab.count.readableNumber()
                val highlightColor = AmityTheme.colors.highlight
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(75.dp)
                        .height(48.dp)
                        .padding(end = 16.dp)
                        .drawBehind {
                            val strokeWidth = 4f
                            val x = size.width - strokeWidth
                            val y = size.height - strokeWidth
                            drawLine(
                                color = if (isSelected) highlightColor else Color.Transparent,
                                start = Offset(0f, y),// bottom-left point of the box
                                end = Offset(x, y),// bottom-right point of the box
                                strokeWidth = strokeWidth
                            )
                        }
                ) {
                    if (title.isEmpty()) {
                        val iconId = AmityMessageReactions.getList()
                            .find { reaction ->
                                reaction.name == tab.title
                            }?.icon ?: R.drawable.amity_ic_message_reaction_missing

                        Image(
                            imageVector = ImageVector.vectorResource(id = iconId),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                    Text(
                        text = "$title $count",
                        style = AmityTheme.typography.titleLegacy.copy(
                            color = if (isSelected) AmityTheme.colors.highlight else AmityTheme.colors.base
                        ),
                        modifier = modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun AmityReactionItems(
    modifier: Modifier = Modifier,
    reactions: LazyPagingItems<AmityReaction>,
    state: AmityReactionListPageState,
    action: (AmityReactionListPageAction) -> Unit = {},
    onUserClick: (String) -> Unit = {},
) {
    if (state.tabItems.first().count == 0) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_add_reaction),
                contentDescription = "no content found",
                modifier = Modifier
                    .size(32.dp)
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "No reactions yet",
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade2,
                ),
            )
            val type = state.referenceType.value
            val text = "Be the first to react to this $type!"
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = text,
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.colors.baseShade2,
                ),
            )
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = reactions.itemCount,
            key = { index -> reactions[index]?.getReactionId() ?: index },
        ) { index ->
            val reaction = reactions[index] ?: return@items
            AmityReactionListItem(
                modifier = modifier,
                reaction = reaction,
                onRemoveReaction = {
                    action(AmityReactionListPageAction.RemoveReaction(reaction.getReactionName()))
                },
                onUserClick = onUserClick,
            )
        }

        pagingLoadStateItem(
            loadState = reactions.loadState.refresh,
            loading = {
                AmityReactionListShimmer()
            },
            error = {
                val error = AmityError.Companion.from(it.error)
                if (error == AmityError.ITEM_NOT_FOUND) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillParentMaxHeight()
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_something_went_wrong),
                            contentDescription = "no content found",
                            modifier = Modifier
                                .size(32.dp)
                        )

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "Unable to load content",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                        val type = state.referenceType.value
                        val text = "Reactions aren't available for this $type"
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = text,
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                } else {
                    if (reactions.itemCount == 0) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = modifier
                                .fillParentMaxHeight()
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_retry),
                                contentDescription = "no content found",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickableWithoutRipple {
                                        reactions.refresh()
                                    }
                            )

                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = "Unable to load reactions",
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    color = AmityTheme.colors.baseShade2,
                                ),
                            )
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun AmityReactionListShimmer() {
    Column {
        repeat(12) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .aspectRatio(1f)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(140.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }

    }
}

data class ReactionTab(
    val title: String,
    val count: Int,
    val isAllTab: Boolean = false
)

