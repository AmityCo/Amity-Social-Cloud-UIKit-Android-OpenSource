package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityEmptySearchResultComponent(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(32.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.amity_ic_no_search_result),
            contentDescription = null,
            modifier = modifier.size(60.dp)
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(
            "No results found",
            style = AmityTheme.typography.titleLegacy.copy(
                color = AmityTheme.colors.baseShade3
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityEmptySearchResultComponentPreview() {
    AmityEmptySearchResultComponent()
}