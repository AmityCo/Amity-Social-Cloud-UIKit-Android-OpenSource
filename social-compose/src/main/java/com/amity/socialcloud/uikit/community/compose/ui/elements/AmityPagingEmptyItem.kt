package com.amity.socialcloud.uikit.community.compose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme


@Composable
fun AmityPagingEmptyItem(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = text,
            style = AmityTheme.typography.body.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade2,
            ),
            modifier = modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityPagingEmptyItemPreview() {
    AmityPagingEmptyItem(
        text = "No comments yet"
    )
}