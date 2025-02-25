package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityNewsFeedDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 8.dp,
        color = AmityTheme.colors.newsfeedDivider,
    )
}