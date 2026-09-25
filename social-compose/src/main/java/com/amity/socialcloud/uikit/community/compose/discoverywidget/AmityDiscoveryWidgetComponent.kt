package com.amity.socialcloud.uikit.community.compose.discoverywidget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import androidx.compose.foundation.layout.width
import com.amity.socialcloud.uikit.community.compose.discoverywidget.elements.AmityDiscoveryWidgetSkeletonCard
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.discoverywidget.elements.AmityDiscoveryWidgetNavButton
import kotlinx.coroutines.launch

/** The 769 boundary is on the container, not the device — a tablet pane can be either. */
private const val EXPANDED_MIN_WIDTH_DP = 769

/** Fixed at both breakpoints: the track scrolls, the card does not resize with its container. */
private val CARD_WIDTH_COMPACT = 343.dp
private val CARD_WIDTH_EXPANDED = 375.dp

/** UC 10 AC1 is "any part of the widget", so a single pixel on screen counts. */
private const val WIDGET_VISIBLE_PERCENT = 1

/** The house default that the feed's own post impression uses. */
private const val CARD_VISIBLE_PERCENT = 60

private val TITLE_SKELETON_WIDTH = 72.dp
private val TITLE_SKELETON_HEIGHT = 16.dp

/** Figma draws four; enough to fill the widest breakpoint without the track ever scrolling. */
private const val SKELETON_CARD_COUNT = 4

@Composable
fun AmityDiscoveryWidgetComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    topicId: String,
    showHeader: Boolean = true,
    minVisibilityThreshold: Int = 3,
    onCardClick: ((topicId: String, post: AmityPost) -> Unit)? = null,
) {
    val context = LocalContext.current
    val behavior = remember { AmitySocialBehaviorHelper.discoveryWidgetComponentBehavior }
    val threshold = minVisibilityThreshold.coerceAtLeast(1)
    val viewModel = viewModel<AmityDiscoveryWidgetViewModel>(
        key = "discovery_widget_$topicId",
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AmityDiscoveryWidgetViewModel(topicId, threshold) as T
        },
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(topicId) { viewModel.load() }

    // Absent is zero height, not a collapsed wrapper: the host page must look as though the widget
    // was never placed. Returning before AmityBaseComponent keeps even its scaffold out of the tree.
    if (state is AmityDiscoveryWidgetViewModel.State.Absent) return

    val rendered = state as? AmityDiscoveryWidgetViewModel.State.Rendered

    var widgetOnScreen by remember(rendered?.topic?.topicId) { mutableStateOf(false) }
    LaunchedEffect(widgetOnScreen, rendered?.topic?.topicId) {
        if (widgetOnScreen) rendered?.let { viewModel.onWidgetVisible(it.topic) }
    }

    AmityBaseComponent(
        modifier = modifier.isVisible(threshold = WIDGET_VISIBLE_PERCENT) { widgetOnScreen = it },
        pageScope = pageScope,
        componentId = "discovery_widget_component",
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val expanded = maxWidth.value >= EXPANDED_MIN_WIDTH_DP
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()
            val sideInset = if (expanded) 0.dp else 16.dp
            val cardGap = if (expanded) 16.dp else 8.dp
            val cardWidth = if (expanded) CARD_WIDTH_EXPANDED else CARD_WIDTH_COMPACT
            // One page is however many whole cards are on screen, never fewer than one.
            val pageSize = ((maxWidth + cardGap) / (cardWidth + cardGap)).toInt().coerceAtLeast(1)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if (expanded) 24.dp else 16.dp),
                verticalArrangement = Arrangement.spacedBy(if (expanded) 24.dp else 16.dp),
            ) {
                if (showHeader) {
                    Header(
                        title = rendered?.title.orEmpty(),
                        loading = rendered == null,
                        expanded = expanded,
                        sideInset = sideInset,
                        canGoBack = rendered != null && listState.canScrollBackward,
                        canGoForward = rendered != null && listState.canScrollForward,
                        onPrevious = {
                            scope.launch {
                                listState.animateScrollToItem(
                                    (listState.firstVisibleItemIndex - pageSize).coerceAtLeast(0)
                                )
                            }
                        },
                        onNext = {
                            scope.launch {
                                listState.animateScrollToItem(
                                    (listState.firstVisibleItemIndex + pageSize)
                                        .coerceAtMost(rendered?.posts?.lastIndex ?: 0)
                                )
                            }
                        },
                    )
                }

                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = sideInset),
                    horizontalArrangement = Arrangement.spacedBy(cardGap),
                ) {
                    if (rendered == null) {
                        // Enough to fill the track at either breakpoint; the row does not scroll
                        // while loading because canScrollForward is false either way.
                        items(SKELETON_CARD_COUNT) {
                            AmityDiscoveryWidgetSkeletonCard(
                                modifier = Modifier.width(cardWidth),
                            )
                        }
                        return@LazyRow
                    }
                    items(rendered.posts.size) { index ->
                        val post = rendered.posts[index]
                        var cardOnScreen by remember(post.getPostId()) { mutableStateOf(false) }
                        LaunchedEffect(cardOnScreen, post.getPostId()) {
                            if (cardOnScreen) viewModel.onPostVisible(rendered.topic, post)
                        }
                        AmityDiscoveryWidgetPostCardComponent(
                            pageScope = pageScope,
                            modifier = Modifier
                                .size(width = cardWidth, height = 480.dp)
                                .isVisible(threshold = CARD_VISIBLE_PERCENT) { cardOnScreen = it },
                            post = post,
                            onClick = {
                                // Enqueued before navigating, and never awaited.
                                viewModel.onPostClick(rendered.topic, post)
                                // The callback wins when a host supplied one; otherwise the
                                // behaviour class owns the destination.
                                if (onCardClick != null) {
                                    onCardClick.invoke(topicId, post)
                                } else {
                                    behavior.goToDestination(
                                        context = AmityDiscoveryWidgetComponentBehavior.Context(context),
                                        topicId = topicId,
                                        post = post,
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    title: String,
    loading: Boolean,
    expanded: Boolean,
    sideInset: androidx.compose.ui.unit.Dp,
    canGoBack: Boolean,
    canGoForward: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (expanded) 32.dp else 24.dp)
            .padding(horizontal = sideInset),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (loading) {
            // The title is unknown until the pool returns, so the heading gets its own skeleton
            // rather than an empty line that would collapse the row.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .size(width = TITLE_SKELETON_WIDTH, height = TITLE_SKELETON_HEIGHT)
                    .shimmerBackground(
                        shape = RoundedCornerShape(12.dp),
                        color = AmityTheme.colors.baseShade4,
                    )
            )
        } else {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
            style = AmityTheme.typography.titleLegacy.copy(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AmityTheme.colors.base,
                textAlign = TextAlign.Start,
            ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // Below 769 the buttons do not exist at all — they are not merely hidden.
        if (expanded) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AmityDiscoveryWidgetNavButton(back = true, enabled = canGoBack, onClick = onPrevious)
                AmityDiscoveryWidgetNavButton(back = false, enabled = canGoForward, onClick = onNext)
            }
        }
    }
}
