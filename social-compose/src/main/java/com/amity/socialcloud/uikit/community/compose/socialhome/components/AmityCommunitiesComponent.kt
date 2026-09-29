package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.isElementExcluded
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.common.utils.isVisitor

@Composable
fun AmityCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    // Explore belongs to Discovery, My Communities to Community, and this bar had
    // neither gate — Discovery off left the Explore tab selected and its category
    // row drawing underneath, while the recommended and trending sections below
    // correctly disappeared. Same defect iOS carried, same fix.
    val exploreVisible = !pageScope.isElementExcluded("explore_button")

    var selectedTabIndex by remember(exploreVisible) { mutableStateOf(0) }
    val isVisitor = remember { AmityCoreClient.isVisitor() }
    val tabTitles = buildList {
        if (exploreVisible) add(amitySocialString("amity_social_tab_tab_explore"))
        add(amitySocialString("amity_social_tab_tab_my_communities"))
    }

    Column(modifier = modifier.fillMaxSize()) {
        // One tab left means no bar: a two-tab row showing one tab is the seam
        // rule 3 exists to remove.
        if (!isVisitor && tabTitles.size > 1) {
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
        }

        // Content based on selected tab. The index is into the surviving titles,
        // not into a fixed pair, so My Communities is index 0 when Explore is gone.
        when (tabTitles.getOrNull(selectedTabIndex)) {
            amitySocialString("amity_social_tab_tab_explore") -> {
                AmityExploreComponent(
                    pageScope = pageScope,
                )
            }
            else -> {
                AmityMyCommunitiesComponent(
                    pageScope = pageScope,
                )
            }
        }
    }
}
