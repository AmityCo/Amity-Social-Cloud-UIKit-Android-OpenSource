package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventCardListShimmer
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventCardShimmer

@Composable
fun AmityExploreEventFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    liveEvents: LazyPagingItems<AmityEvent>,
    upcomingEvents: LazyPagingItems<AmityEvent>,
    onEventClick: (AmityEvent) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    // Happening now events
    val happeningNowEvents = liveEvents.itemSnapshotList.items
    
    // Recommended events - show only first 5 upcoming events
    val recommendedEvents = upcomingEvents.itemSnapshotList.items.take(5)
    
    val listState = rememberLazyListState()
    val isLiveEventsLoading = liveEvents.loadState.refresh is LoadState.Loading
    val isUpcomingEventsLoading = upcomingEvents.loadState.refresh is LoadState.Loading
    
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        
        // Happening Now Section
        if (isLiveEventsLoading) {
            item {
                Text(
                    text = "Happening now",
                    style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            item {
                AmityEventCardShimmer(
                    style = EventCardStyle.Large,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        } else if (happeningNowEvents.isNotEmpty()) {
            item {
                Text(
                    text = "Happening now",
                    style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            
            // Use Large card if only 1 event, otherwise use horizontal scroll with Medium cards
            if (happeningNowEvents.size == 1) {
                item {
                    val event = happeningNowEvents.firstOrNull()
                    EventCardItem(
                        event = event,
                        style = EventCardStyle.Large,
                        onClick = { event?.let { onEventClick(it) } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(happeningNowEvents.size) { index ->
                            val event = happeningNowEvents.getOrNull(index)
                            EventCardItem(
                                event = event,
                                style = EventCardStyle.Medium,
                                onClick = { event?.let { onEventClick(it) } }
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // Recommended for you Section
        item {
            Text(
                text = "Recommended for you",
                style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                color = AmityTheme.colors.base,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        
        // Show shimmer while loading upcoming events
        if (isUpcomingEventsLoading) {
            item {
                AmityEventCardListShimmer(
                    style = EventCardStyle.List,
                    count = 3
                )
            }
        } else if (recommendedEvents.isEmpty()) {
            // Empty state
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_event_empty),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No events yet",
                        style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                        color = AmityTheme.colors.baseShade3
                    )
                }
            }
        }
        // Event List - showing upcoming events (limit 5)
        else {
            items(recommendedEvents.size) { index ->
                val event = recommendedEvents.getOrNull(index)
                EventCardItem(
                    event = event,
                    style = EventCardStyle.List,
                    onClick = { event?.let { onEventClick(it) } }
                )
            }
        }
        
        // View All button - positioned after the list, only show if there are 5 or more events
        if (upcomingEvents.itemCount >= 5) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .border(
                            width = 1.dp,
                            color = AmityTheme.colors.baseShade3,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickableWithoutRipple { onViewAllClick() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View all",
                        style = AmityTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                        color = AmityTheme.colors.base
                    )
                }
            }
        }
    }
}
