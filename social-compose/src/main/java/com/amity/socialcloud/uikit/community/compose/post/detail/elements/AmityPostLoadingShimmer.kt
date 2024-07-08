package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityPostLoadingShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row {
                Box(
                    modifier = modifier
                        .size(40.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(40.dp)
                        )
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .height(40.dp)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Box(
                        Modifier
                            .width(120.dp)
                            .height(8.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                    Box(
                        Modifier
                            .width(88.dp)
                            .height(8.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }
            }

            Box(
                Modifier
                    .width(240.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
            Box(
                Modifier
                    .width(300.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
            Box(
                Modifier
                    .width(180.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }

        Spacer(modifier = modifier.height(50.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AmityPostLoadingShimmerPreview() {
    AmityPostLoadingShimmer()
}