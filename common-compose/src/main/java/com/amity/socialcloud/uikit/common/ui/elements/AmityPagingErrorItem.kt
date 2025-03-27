package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmityPagingErrorItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Unable to load content",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade2,
            ),
            modifier = modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityPagingErrorItemPreview() {
    AmityPagingErrorItem()
}