package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
fun AmityShortSearchInputComponent(
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
        Icon(
            painter = painterResource(R.drawable.amity_ic_start_search),
            tint = AmityTheme.colors.baseShade4,
            contentDescription = null,
            modifier = modifier.size(60.dp)
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(
            "Start your search by typing at least 3 letters",
            style = AmityTheme.typography.titleLegacy.copy(
                color = AmityTheme.colors.baseShade2
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityShortSearchInputComponentPreview() {
    AmityShortSearchInputComponent()
}