package com.amity.socialcloud.uikit.community.compose.reaction.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityReactionTab(
    modifier: Modifier = Modifier,
    reactionCount: Int
) {
    TabRow(
        selectedTabIndex = 0,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = modifier.tabIndicatorOffset(tabPositions[0]),
                color = AmityTheme.colors.primary
            )
        },
        contentColor = AmityTheme.colors.primary,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .width(100.dp)
    ) {
        Tab(
            selected = true,
            onClick = {}
        ) {
            val count = if (reactionCount == 0) "" else reactionCount.readableNumber()
            Text(
                text = "All $count",
                style = AmityTheme.typography.title.copy(
                    color = AmityTheme.colors.primary
                ),
                modifier = modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityReactionTabPreview() {
    AmityReactionTab(
        reactionCount = 10
    )
}
