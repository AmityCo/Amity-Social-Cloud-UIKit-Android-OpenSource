package com.amity.socialcloud.uikit.community.compose.socialhome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPage
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityEventsComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityExploreComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityNewsFeedComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmitySocialHomeTabButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmitySocialHomePage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val behavior = remember {
        AmitySocialBehaviorHelper.socialHomePageBehavior
    }

    var selectedTab by remember {
        if (AmityCoreClient.isSignedIn())
            mutableStateOf(AmitySocialHomePageTab.NEWSFEED)
        else
            mutableStateOf(AmitySocialHomePageTab.COMMUNITIES)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmitySocialHomePageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner
    )
     val isSignedInUser = remember {
         AmityCoreClient.isSignedIn()
     }

    LaunchedEffect(Unit) {
        if (isSignedInUser) {
            viewModel.scheduleNotificationTraySeen()
        }
    }

    val notiTraySeen by viewModel.notificationTraySeen.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = if (isSignedInUser) 0 else 1 ,
        pageCount = { 4 }
    )
    val scrollScope = rememberCoroutineScope()

    AmityBasePage(
        pageId = "social_home_page"
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            AmitySocialHomeTopNavigationComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                selectedTab = selectedTab,
                searchButtonAction = {
                    when (selectedTab) {
                        AmitySocialHomePageTab.NEWSFEED,
                        AmitySocialHomePageTab.COMMUNITIES,
                        AmitySocialHomePageTab.EVENTS
                            ->
                            behavior.goToGlobalSearchPage(context)

                        AmitySocialHomePageTab.CLIPS -> behavior.goToClipsFeedPage(context)

                    }
                },
                isSeen = if (notiTraySeen != null) notiTraySeen?.isSeen() == true else true,
                notificationButton = {
                    viewModel.markTraySeen()
                    behavior.goToNotificationTrayPage(context)
                }
            )

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = modifier
                    .wrapContentHeight()
            ) {
                if (isSignedInUser) {
                    item {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "newsfeed_button"
                        ) {
                            AmitySocialHomeTabButton(
                                title = getConfig().getText(),
                                item = AmitySocialHomePageTab.NEWSFEED,
                                isSelected = selectedTab == AmitySocialHomePageTab.NEWSFEED,
                                modifier = modifier.testTag(getAccessibilityId()),
                            ) {
                                selectedTab = it
                                scrollScope.launch {
                                    pagerState.scrollToPage(0)
                                }
                            }
                        }
                    }
                }
                item {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "communities_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomePageTab.COMMUNITIES,
                            isSelected = selectedTab == AmitySocialHomePageTab.COMMUNITIES,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedTab = it
                            scrollScope.launch {
                                pagerState.scrollToPage(1)
                            }
                        }
                    }
                }
                item {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "events_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomePageTab.EVENTS,
                            isSelected = selectedTab == AmitySocialHomePageTab.EVENTS,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedTab = it
                            scrollScope.launch {
                                pagerState.scrollToPage(2)
                            }
                        }
                    }
                }
                item {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "clips_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomePageTab.CLIPS,
                            isSelected = selectedTab == AmitySocialHomePageTab.CLIPS,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            behavior.goToClipsFeedPage(context)
                        }
                    }
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 6,
                    userScrollEnabled = false,
                ) { page ->
                    when (page) {
                        0 -> {
                            // NewsFeed
                            AmityNewsFeedComponent(
                                pageScope = getPageScope(),
                                onExploreRequested = {
                                    selectedTab = AmitySocialHomePageTab.COMMUNITIES
                                }
                            )
                        }

                        1 -> {
                            // Communities (with Explore and My Communities sub-tabs)
                            AmityCommunitiesComponent(
                                pageScope = getPageScope(),
                            )
                        }

                        2 -> {
                            // Events
                            AmityEventsComponent(
                                pageScope = getPageScope(),
                            )
                        }

                        else -> {

                        }
                    }
                }

            }
        }
    }
}