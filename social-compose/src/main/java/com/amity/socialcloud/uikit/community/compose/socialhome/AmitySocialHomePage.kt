package com.amity.socialcloud.uikit.community.compose.socialhome

import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
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
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPage
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityEventsComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityExploreComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityForYouFeedComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityNewsFeedComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmitySocialHomeTabButton
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
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

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmitySocialHomePageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner
    )
    val isSignedInUser = remember {
        AmityCoreClient.isSignedIn()
    }
    val isForYouEnabled by viewModel.isForYouEnabled.collectAsState()

    // Default tab — REQ-006: optimistic For You for signed-in users; fall back
    // to Following when the feature flips disabled mid-session (REQ-023, handled
    // by the LaunchedEffect below).
    var selectedTab by remember {
        mutableStateOf(
            if (isSignedInUser) AmitySocialHomePageTab.FOR_YOU
            else AmitySocialHomePageTab.COMMUNITIES
        )
    }

    LaunchedEffect(Unit) {
        if (isSignedInUser) {
            viewModel.scheduleNotificationTraySeen()
        }
    }

    val notiTraySeen by viewModel.notificationTraySeen.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = when {
            isSignedInUser && isForYouEnabled != false -> 0  // For You (or loading)
            isSignedInUser -> 1                              // Following
            else -> 2                                        // Communities (visitor/bot)
        },
        pageCount = { 5 }
    )
    val scrollScope = rememberCoroutineScope()

    val isForYouSettingLoading = isSignedInUser && isForYouEnabled == null

    // REQ-023/028: when setting resolves to disabled or feed signals
    // feature-disabled, move to Following.
    LaunchedEffect(isForYouEnabled) {
        if (isForYouEnabled == false && selectedTab == AmitySocialHomePageTab.FOR_YOU) {
            selectedTab = AmitySocialHomePageTab.FOLLOWING
            pagerState.scrollToPage(1)
        }
    }

    AmityBasePage(
        pageId = "social_home_page"
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            AmitySocialHomeTopNavigationComponent(
                modifier = Modifier,
                pageScope = getPageScope(),
                selectedTab = selectedTab,
                searchButtonAction = {
                    // REQ-021: For You routes to global search just like Following.
                    when (selectedTab) {
                        AmitySocialHomePageTab.FOR_YOU,
                        AmitySocialHomePageTab.FOLLOWING,
                        AmitySocialHomePageTab.COMMUNITIES,
                        AmitySocialHomePageTab.EVENTS
                            ->
                            behavior.goToGlobalSearchPage(context)

                        AmitySocialHomePageTab.CLIPS -> behavior.goToClipsFeedPage(context)

                    }
                },
                isSeen = notiTraySeen?.isSeen() == true,
                notificationButton = {
                    if (notiTraySeen?.isSeen() == false) {
                        viewModel.markTraySeen()
                    }
                    behavior.goToNotificationTrayPage(context)
                }
            )

            if (isForYouSettingLoading) {
                // REQ-026: shimmer placeholders for tab bar while resolving
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    repeat(4) { i ->
                        Box(
                            modifier = Modifier
                                .width(if (i == 0) 72.dp else 88.dp)
                                .height(36.dp)
                                .shimmerBackground(
                                    color = AmityTheme.colors.baseShade4,
                                    shape = RoundedCornerShape(24.dp)
                                )
                        )
                    }
                }
            } else LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                // REQ-018/019: For You hidden for visitor/bot AND when feature disabled.
                if (isSignedInUser && isForYouEnabled == true) {
                    item {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "for_you_button"
                        ) {
                            AmitySocialHomeTabButton(
                                title = amitySocialConfigString("amity_social_button_social_home_for_you_button"),
                                item = AmitySocialHomePageTab.FOR_YOU,
                                isSelected = selectedTab == AmitySocialHomePageTab.FOR_YOU,
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
                // Following (renamed from Newsfeed, REQ-003). Hidden for visitor/bot.
                if (isSignedInUser) {
                    item {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "following_button"
                        ) {
                            AmitySocialHomeTabButton(
                                title = amitySocialConfigString("amity_social_button_social_home_following_button"),
                                item = AmitySocialHomePageTab.FOLLOWING,
                                isSelected = selectedTab == AmitySocialHomePageTab.FOLLOWING,
                                modifier = modifier.testTag(getAccessibilityId()),
                            ) {
                                selectedTab = it
                                scrollScope.launch {
                                    pagerState.scrollToPage(1)
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
                            title = amitySocialConfigString("amity_social_button_social_home_communities_button"),
                            item = AmitySocialHomePageTab.COMMUNITIES,
                            isSelected = selectedTab == AmitySocialHomePageTab.COMMUNITIES,
                            modifier = Modifier.testTag(getAccessibilityId()),
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
                        elementId = "events_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = amitySocialConfigString("amity_social_button_social_home_events_button"),
                            item = AmitySocialHomePageTab.EVENTS,
                            isSelected = selectedTab == AmitySocialHomePageTab.EVENTS,
                            modifier = Modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedTab = it
                            scrollScope.launch {
                                pagerState.scrollToPage(3)
                            }
                        }
                    }
                }
                val isClipEnabledAll = AmityUIKitConfigController.getClipFeatureFlags().get("can_view_tab").asString == "all"
                if (isClipEnabledAll || AmityCoreClient.isSignedIn()) {
                    item {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "clipsfeed_button"
                        ) {
                            AmitySocialHomeTabButton(
                                title = amitySocialConfigString("amity_social_button_social_home_clips_button"),
                                item = AmitySocialHomePageTab.CLIPS,
                                isSelected = selectedTab == AmitySocialHomePageTab.CLIPS,
                                modifier = Modifier.testTag(getAccessibilityId()),
                            ) {
                                behavior.goToClipsFeedPage(context)
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (isForYouSettingLoading) {
                    // REQ-026: content shimmer while resolving enablement
                    Column(modifier = Modifier.fillMaxSize()) {
                        repeat(3) { AmityPostShimmer() }
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 0,
                    userScrollEnabled = false,
                ) { page ->
                    when (page) {
                        0 -> {
                            // REQ-022: For You tab renders AmityForYouFeedComponent.
                            // REQ-023 + REQ-016: onFeatureDisabled drops the tab via
                            // the viewmodel; onSwitchToFollowingRequested switches
                            // the active tab from the end-of-feed nudge.
                            //
                            // Conditionally render: once isForYouEnabled flips false,
                            // unmount the component so it stops re-subscribing to the
                            // failing endpoint (beyondViewportPageCount keeps unfocused
                            // pages alive, so without this guard the disabled-error
                            // path would keep firing).
                            if (isForYouEnabled == true) {
                                AmityForYouFeedComponent(
                                    pageScope = getPageScope(),
                                    onFeatureDisabled = { viewModel.onForYouFeatureDisabled() },
                                    onSwitchToFollowingRequested = {
                                        selectedTab = AmitySocialHomePageTab.FOLLOWING
                                        scrollScope.launch {
                                            pagerState.scrollToPage(1)
                                        }
                                    },
                                )
                            } else {
                                Box(modifier = Modifier.fillMaxSize())
                            }
                        }

                        1 -> {
                            // Following (formerly Newsfeed — REQ-003).
                            AmityNewsFeedComponent(
                                pageScope = getPageScope(),
                                onExploreRequested = {
                                    selectedTab = AmitySocialHomePageTab.COMMUNITIES
                                }
                            )
                        }

                        2 -> {
                            // Communities (with Explore and My Communities sub-tabs)
                            AmityCommunitiesComponent(
                                pageScope = getPageScope(),
                            )
                        }

                        3 -> {
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