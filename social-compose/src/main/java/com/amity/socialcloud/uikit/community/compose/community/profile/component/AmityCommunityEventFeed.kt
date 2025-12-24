package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventCardShimmer
import org.joda.time.format.DateTimeFormat

fun LazyListScope.amityCommunityEventFeed(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    communityId: String,
    liveEvents: LazyPagingItems<AmityEvent>,
    events: LazyPagingItems<AmityEvent>,
    selectedFilter: String = "Upcoming",
    onFilterChange: (String) -> Unit = {},
    onEventClick: (AmityEvent) -> Unit = {}
) {
    // Happening now events come from separate API call with "live" status
    val happeningNowEvents = liveEvents.itemSnapshotList.items
    val isHappeningNowLoading = liveEvents.loadState.refresh is LoadState.Loading
    
    // All events from the query (filtered by status - Upcoming or Past)
    val filteredEvents = events.itemSnapshotList.items
    val isEventsLoading = events.loadState.refresh is LoadState.Loading
    
    // Happening Now Section - Show shimmer while loading
    if (isHappeningNowLoading) {
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
        
        val eventCount = happeningNowEvents.size
        
        // Use Large card if only 1 event, otherwise use horizontal scroll with Medium cards
        if (eventCount == 1) {
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
                    items(eventCount) { index ->
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
    
    // Filter Chips
    item {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FilterChip(
                label = "Upcoming",
                isSelected = selectedFilter == "Upcoming",
                onClick = { onFilterChange("Upcoming") }
            )
            FilterChip(
                label = "Past",
                isSelected = selectedFilter == "Past",
                onClick = { onFilterChange("Past") }
            )
        }
    }
    
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }
    // Event List - showing filtered events based on selected tab
    if (isEventsLoading) {
        items(3) {
            AmityEventCardShimmer(
                style = EventCardStyle.List
            )
        }
    } else if (filteredEvents.isEmpty()) {
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
    } else {
        // Infinite loading with paging data - no "View all" button needed
        items(filteredEvents.size) { index ->
            val event = filteredEvents.getOrNull(index) as? AmityEvent
            EventCardItem(
                event = event,
                style = EventCardStyle.List,
                onClick = { event?.let { onEventClick(it) } }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) AmityTheme.colors.primary else AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(20.dp)
            )
            .clickableWithoutRipple { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = if (isSelected) AmityTheme.typography.body.copy(fontWeight = FontWeight.Bold) else AmityTheme.typography.body,
            color = if (isSelected) AmityTheme.colors.background else AmityTheme.colors.baseShade1
        )
    }
}
