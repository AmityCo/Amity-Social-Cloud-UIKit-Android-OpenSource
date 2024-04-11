package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityGlobalFeedDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 8.dp,
        color = Color(0xFFEBECEF)
    )
}

@Preview
@Composable
fun AmityGlobalFeedDividerPreview() {
    AmityGlobalFeedDivider()
}