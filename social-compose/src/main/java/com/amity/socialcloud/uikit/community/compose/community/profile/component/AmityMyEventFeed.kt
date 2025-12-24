package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun AmityMyEventFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    liveEvents: LazyPagingItems<AmityEvent>,
    upcomingEvents: LazyPagingItems<AmityEvent>,
    pastEvents: LazyPagingItems<AmityEvent>,
    onEventClick: (AmityEvent) -> Unit = {},
    onViewAllUpcomingClick: () -> Unit = {},
    onViewAllPastClick: () -> Unit = {}
) {
    // Upcoming events - show events user created or joined (limit 5)
    val myUpcomingEvents = upcomingEvents.itemSnapshotList.items.take(5)
    
    // Past events - show events user created or joined (limit 5)
    val myPastEvents = pastEvents.itemSnapshotList.items.take(5)
    
    val listState = rememberLazyListState()
    val isUpcomingLoading = upcomingEvents.loadState.refresh is LoadState.Loading
    val isPastLoading = pastEvents.loadState.refresh is LoadState.Loading
    
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        
        // Upcoming Events Section
        item {
            Text(
                text = "Upcoming events",
                style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                color = AmityTheme.colors.base,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        
        // Show shimmer while loading upcoming events
        if (isUpcomingLoading) {
            item {
                AmityEventCardListShimmer(
                    style = EventCardStyle.List,
                    count = 3
                )
            }
        }
        // Upcoming Event List
        else if (myUpcomingEvents.isNotEmpty()) {
            items(myUpcomingEvents.size) { index ->
                val event = myUpcomingEvents.getOrNull(index)
                EventCardItem(
                    event = event,
                    style = EventCardStyle.List,
                    onClick = { event?.let { onEventClick(it) } }
                )
            }
            
            // View All button for upcoming events - only show if there are 5 or more events
            if (myUpcomingEvents.size >= 5) {
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
                            .clickableWithoutRipple { onViewAllUpcomingClick() }
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
        } else {
            // Empty state for upcoming events
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
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
        }

        // Past Events Section
        item {
            Text(
                text = "Past events",
                style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                color = AmityTheme.colors.base,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        
        // Show shimmer while loading past events
        if (isPastLoading) {
            item {
                AmityEventCardListShimmer(
                    style = EventCardStyle.List,
                    count = 3
                )
            }
        }
        // Past Event List
        else if (myPastEvents.isNotEmpty()) {
            items(myPastEvents.size) { index ->
                val event = myPastEvents.getOrNull(index)
                EventCardItem(
                    event = event,
                    style = EventCardStyle.List,
                    onClick = { event?.let { onEventClick(it) } }
                )
            }
            
            // View All button for past events - only show if there are 5 or more events
            if (myPastEvents.size >= 5) {
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
                            .clickableWithoutRipple { onViewAllPastClick() }
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
        } else {
            // Empty state for past events
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
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
        }
    }
}
