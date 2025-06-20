package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun AmityEmptyPendingPostsElement(
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_empty_pending_posts),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade4,
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = title?: "No pending posts",
                style = AmityTheme.typography.titleLegacy.copy(
                    color = AmityTheme.colors.baseShade3,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityEmptyPendingPostsElementPreview() {
    AmityEmptyPendingPostsElement()
}