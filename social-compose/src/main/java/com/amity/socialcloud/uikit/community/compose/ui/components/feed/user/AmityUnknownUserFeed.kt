package com.amity.socialcloud.uikit.community.compose.ui.components.feed.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmityUnknownUserFeed(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.amity_ic_unknown_user_feed),
            tint = AmityTheme.colors.baseShade4,
            contentDescription = "unkown feed icon",
            modifier = modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Something went wrong",
            style = AmityTheme.typography.titleLegacy.copy(
                color = AmityTheme.colors.baseShade3,
            ),
        )

        Text(
            text = "We couldn’t recognize this feed.",
            style = AmityTheme.typography.captionLegacy.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade3,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityUnknownUserFeedPreview() {
    AmityUnknownUserFeed()
}