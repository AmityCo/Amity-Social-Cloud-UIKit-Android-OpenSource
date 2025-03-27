package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmityProfileEmptyImageFeed(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_photo_empty),
            tint = AmityTheme.colors.baseShade4,
            contentDescription = "empty feed icon"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No photos yet",
            style = AmityTheme.typography.titleLegacy.copy(
                color = AmityTheme.colors.baseShade3,
            ),
        )
    }
}