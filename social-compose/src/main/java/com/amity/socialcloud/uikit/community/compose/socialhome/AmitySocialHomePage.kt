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
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
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
        mutableStateOf(AmitySocialHomePageTab.NEWSFEED)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmitySocialHomePageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner
    )

    LaunchedEffect(Unit) {
        viewModel.scheduleNotificationTraySeen()
    }

    val notiTraySeen by viewModel.notificationTraySeen.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = { 3 }
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
                        AmitySocialHomePageTab.EXPLORE,
                        AmitySocialHomePageTab.NEWSFEED,
                            ->
                            behavior.goToGlobalSearchPage(context)

                        AmitySocialHomePageTab.MY_COMMUNITIES ->
                            behavior.goToMyCommunitiesSearchPage(context)

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
                item {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "explore_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomePageTab.EXPLORE,
                            isSelected = selectedTab == AmitySocialHomePageTab.EXPLORE,
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
                        elementId = "my_communities_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomePageTab.MY_COMMUNITIES,
                            isSelected = selectedTab == AmitySocialHomePageTab.MY_COMMUNITIES,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedTab = it
                            scrollScope.launch {
                                pagerState.scrollToPage(2)
                            }
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
                            AmityNewsFeedComponent(
                                pageScope = getPageScope(),
                                onExploreRequested = {
                                    selectedTab = AmitySocialHomePageTab.EXPLORE
                                }
                            )
                        }

                        1 -> {
                            AmityExploreComponent(
                                pageScope = getPageScope(),
                            )
                        }

                        2 -> {
                            AmityMyCommunitiesComponent(
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