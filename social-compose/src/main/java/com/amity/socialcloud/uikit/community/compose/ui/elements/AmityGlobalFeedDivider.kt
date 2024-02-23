package com.amity.socialcloud.uikit.community.compose.ui.elements

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@Composable
fun AmityGlobalFeedDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 8.dp,
        color = AmityTheme.colors.divider
    )
}

@Preview
@Composable
fun AmityGlobalFeedDividerPreview() {
    AmityGlobalFeedDivider()
}