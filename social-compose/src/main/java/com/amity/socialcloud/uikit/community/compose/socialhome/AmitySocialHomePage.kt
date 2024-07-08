package com.amity.socialcloud.uikit.community.compose.socialhome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityExploreComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityNewsFeedComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmitySocialHomeTabButton

@Composable
fun AmitySocialHomePage(
    modifier: Modifier = Modifier,
) {
    var selectedNavigationItem by remember {
        mutableStateOf(AmitySocialHomeTabItem.NEWSFEED)
    }

    AmityBasePage(
        pageId = "social_home_page"
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            AmitySocialHomeTopNavigationComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                selectedTab = selectedNavigationItem
            )

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                item {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "newsfeed_button"
                    ) {
                        AmitySocialHomeTabButton(
                            title = getConfig().getText(),
                            item = AmitySocialHomeTabItem.NEWSFEED,
                            isSelected = selectedNavigationItem == AmitySocialHomeTabItem.NEWSFEED,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedNavigationItem = it
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
                            item = AmitySocialHomeTabItem.EXPLORE,
                            isSelected = selectedNavigationItem == AmitySocialHomeTabItem.EXPLORE,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedNavigationItem = it
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
                            item = AmitySocialHomeTabItem.MY_COMMUNITIES,
                            isSelected = selectedNavigationItem == AmitySocialHomeTabItem.MY_COMMUNITIES,
                            modifier = modifier.testTag(getAccessibilityId()),
                        ) {
                            selectedNavigationItem = it
                        }
                    }
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                when (selectedNavigationItem) {
                    AmitySocialHomeTabItem.NEWSFEED -> {
                        AmityNewsFeedComponent(
                            pageScope = getPageScope(),
                        )
                    }

                    AmitySocialHomeTabItem.EXPLORE -> {
                        AmityExploreComponent(
                            pageScope = getPageScope(),
                        )
                    }

                    AmitySocialHomeTabItem.MY_COMMUNITIES -> {
                        AmityMyCommunitiesComponent(
                            pageScope = getPageScope(),
                        )
                    }
                }
            }
        }
    }
}