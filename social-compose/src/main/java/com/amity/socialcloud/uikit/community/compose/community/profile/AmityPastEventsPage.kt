package com.amity.socialcloud.uikit.community.compose.community.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.component.EventCardItem
import com.amity.socialcloud.uikit.community.compose.community.profile.component.EventCardStyle
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityEventsComponentViewModel
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventCardListShimmer

/**
 * Page displaying all past events in a list format.
 * Shown when user clicks "View all" under past events section.
 * 
 * @param showAllEvents If true, shows all events in "All" tab. If false, shows user's events in "All" tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPastEventsPage(
    modifier: Modifier = Modifier,
    showAllEvents: Boolean = true,
    pageScope: AmityComposePageScope? = null,
    onBackClick: () -> Unit = {},
    onEventClick: (AmityEvent) -> Unit = {}
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmityEventsComponentViewModel>(
        viewModelStoreOwner = viewModelStoreOwner
    )

    val listState = rememberLazyListState()

    // Tab state
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("All", "Hosting")
    
    // Get events based on selected tab and showAllEvents parameter
    val events = remember(selectedTabIndex, showAllEvents) {
        val currentUserId = AmityCoreClient.getUserId()
        when (selectedTabIndex) {
            0 -> if (showAllEvents) viewModel.getPastEvents(AmityEventOriginType.COMMUNITY) else viewModel.getPastEvents(AmityEventOriginType.USER, currentUserId)
            1 -> viewModel.getPastEvents(AmityEventOriginType.COMMUNITY, currentUserId)
            else -> viewModel.getPastEvents(AmityEventOriginType.COMMUNITY)
        }
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "past_events_page") {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Past events",
                            style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                            color = AmityTheme.colors.base,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(R.drawable.amity_ic_back),
                                contentDescription = "Back",
                                tint = AmityTheme.colors.base
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AmityTheme.colors.background
                    )
                )
            },
            containerColor = AmityTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab Row
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = AmityTheme.colors.background,
                    contentColor = AmityTheme.colors.primary,
                    edgePadding = 0.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = AmityTheme.colors.primary,
                            height = 2.dp
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (selectedTabIndex == index) AmityTheme.colors.primary else AmityTheme.colors.baseShade1
                                )
                            }
                        )
                    }
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when {
                        events.loadState.refresh is LoadState.Loading -> {
                            // Loading state with shimmer
                            AmityEventCardListShimmer(
                                style = EventCardStyle.List,
                                count = 5,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp)
                            )
                        }

                        events.itemCount == 0 && events.loadState.refresh is LoadState.NotLoading -> {
                            // Empty state
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 80.dp)
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

                        else -> {
                            // Content
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(events.itemCount) { index ->
                                    val event = events[index]
                                    EventCardItem(
                                        event = event,
                                        style = EventCardStyle.List,
                                        onClick = { event?.let { onEventClick(it) } }
                                    )
                                }

                                // Loading more indicator
                                if (events.loadState.append is LoadState.Loading) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Loading more...",
                                                style = AmityTheme.typography.caption,
                                                color = AmityTheme.colors.baseShade1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
