package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityPagingLoadingItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            color = AmityTheme.colors.primary,
            trackColor = Color.White,
            strokeWidth = 3.dp,
            modifier = modifier
                .size(30.dp)
                .align(alignment = Alignment.Center)
        )
    }
}

@Preview
@Composable
fun AmityPagingLoadingItemPreview() {
    AmityPagingLoadingItem()
}