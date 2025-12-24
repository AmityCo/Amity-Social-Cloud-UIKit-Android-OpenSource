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
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCommunitiesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Explore", "My communities")

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
                // Explore tab
                AmityExploreComponent(
                    pageScope = pageScope,
                )
            }
            1 -> {
                // My communities tab
                AmityMyCommunitiesComponent(
                    pageScope = pageScope,
                )
            }
        }
    }
}
