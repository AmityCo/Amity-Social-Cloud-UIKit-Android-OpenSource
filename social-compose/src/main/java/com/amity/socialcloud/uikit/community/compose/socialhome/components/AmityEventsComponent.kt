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
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.isComponentExcluded
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityPastEventsPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityUpcomingEventsPageActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityExploreEventFeedComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityMyEventFeedComponent
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

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

    // R6: the five queries below are built during composition, and the module
    // that sells them is the one being switched off. The tab button is already
    // gated, but a container that still asks is a switched-off module that
    // errors rather than one that is absent.
    val exploreVisible = !isComponentExcluded(pageScope, "explore_event_feed_component")
    val myEventsVisible = !isComponentExcluded(pageScope, "my_event_feed_component")
    if (!exploreVisible && !myEventsVisible) return

    // Explore tab excludes the current user's own events, so it needs its own streams
    // separate from the My event tab's.
    val exploreLiveEvents = remember {
        viewModel.getLiveEvents(AmityEventOriginType.COMMUNITY, excludeOwnEvents = true)
    }.collectAsLazyPagingItems()
    val exploreUpcomingEvents = remember {
        viewModel.getUpcomingEvents(AmityEventOriginType.COMMUNITY, excludeOwnEvents = true)
    }.collectAsLazyPagingItems()

    val liveEvents = remember { viewModel.getLiveEvents(AmityEventOriginType.COMMUNITY) }.collectAsLazyPagingItems()
    val myUpcomingEvents = remember { viewModel.getMyUpcomingEvents(AmityEventOriginType.COMMUNITY) }.collectAsLazyPagingItems()
    val pastEvents = remember { viewModel.getPastEvents(AmityEventOriginType.COMMUNITY, AmityCoreClient.getUserId()) }.collectAsLazyPagingItems()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val isVisitor = remember { AmityCoreClient.isVisitor() }
    // Built from what survives, not indexed. Dropping a tab must not renumber
    // the one after it — selectedTabIndex addresses this list, not the enum.
    val tabs = listOfNotNull(
        (0 to amitySocialString("amity_social_tab_tab_explore")).takeIf { exploreVisible },
        (1 to amitySocialString("amity_social_tab_tab_my_event")).takeIf { myEventsVisible },
    )

    // R5: a module can take the landing tab with it. Correct it here, inside the
    // component, because this is what re-reads the config after it loads.
    if (tabs.none { it.first == selectedTabIndex }) {
        tabs.firstOrNull()?.let { selectedTabIndex = it.first }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (!isVisitor) {
            // Tab Row

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = AmityTheme.colors.background,
                    contentColor = AmityTheme.colors.primary,
                    edgePadding = 0.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[tabs.indexOfFirst { it.first == selectedTabIndex }
                                    .coerceAtLeast(0)]
                            ),
                            color = AmityTheme.colors.primary,
                            height = 2.dp
                        )
                    }
                ) {
                    tabs.forEach { (slot, title) ->
                        Tab(
                            selected = selectedTabIndex == slot,
                            onClick = { selectedTabIndex = slot },
                            text = {
                                Text(
                                    text = title,
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (selectedTabIndex == slot) AmityTheme.colors.primary else AmityTheme.colors.baseShade1
                                )
                            }
                        )
                    }
                }

        }

        // Content based on selected tab
        when (selectedTabIndex) {
            0 -> {
                // Explore tab - shows all events
                AmityExploreEventFeedComponent(
                    pageScope = pageScope,
                    liveEvents = exploreLiveEvents,
                    upcomingEvents = exploreUpcomingEvents,
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
