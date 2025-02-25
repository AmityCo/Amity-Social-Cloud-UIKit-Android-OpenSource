package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityTrendingCommunityShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(156.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }

        repeat(2) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .aspectRatio(1f)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .width(196.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .width(93.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }

            HorizontalDivider(
                color = AmityTheme.colors.divider,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }
    }
}