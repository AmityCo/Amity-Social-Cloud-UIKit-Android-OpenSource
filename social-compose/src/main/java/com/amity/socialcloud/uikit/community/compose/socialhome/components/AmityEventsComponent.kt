package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityPastEventsPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityUpcomingEventsPageActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityExploreEventFeedComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityMyEventFeedComponent

@Composable
fun AmityEventsComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    val context = LocalContext.current
    val behavior = remember {
        AmitySocialBehaviorHelper.socialHomePageBehavior
    }
    
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel: AmityEventsComponentViewModel = viewModel()
    
    val liveEvents = remember { viewModel.getLiveEvents(AmityEventOriginType.COMMUNITY) }.collectAsLazyPagingItems()
    val upcomingEvents = remember { viewModel.getUpcomingEvents(AmityEventOriginType.COMMUNITY) }.collectAsLazyPagingItems()
    val myUpcomingEvents = remember { viewModel.getMyUpcomingEvents(AmityEventOriginType.COMMUNITY) }.collectAsLazyPagingItems()
    val pastEvents = remember { viewModel.getPastEvents(AmityEventOriginType.COMMUNITY, AmityCoreClient.getUserId()) }.collectAsLazyPagingItems()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Explore", "My event")

    Column(modifier = modifier.fillMaxSize()) {
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

        // Content based on selected tab
        when (selectedTabIndex) {
            0 -> {
                // Explore tab - shows all events
                AmityExploreEventFeedComponent(
                    pageScope = pageScope,
                    liveEvents = liveEvents,
                    upcomingEvents = upcomingEvents,
                    onEventClick = { event ->
                        context.startActivity(AmityEventDetailPageActivity.newIntent(context, event.getEventId()))
                    },
                    onViewAllClick = {
                        context.startActivity(AmityUpcomingEventsPageActivity.newIntent(context, showAllEvents = true))
                    }
                )
            }
            1 -> {
                // My event tab - shows only user's events
                AmityMyEventFeedComponent(
                    pageScope = pageScope,
                    liveEvents = liveEvents,
                    upcomingEvents = myUpcomingEvents,
                    pastEvents = pastEvents,
                    onEventClick = { event ->
                        context.startActivity(AmityEventDetailPageActivity.newIntent(context, event.getEventId()))
                    },
                    onViewAllUpcomingClick = {
                        context.startActivity(AmityUpcomingEventsPageActivity.newIntent(context, showAllEvents = false))
                    },
                    onViewAllPastClick = {
                        context.startActivity(AmityPastEventsPageActivity.newIntent(context))
                    }
                )
            }
        }
    }
}
