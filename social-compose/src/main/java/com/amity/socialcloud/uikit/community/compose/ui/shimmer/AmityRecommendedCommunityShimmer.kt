package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground


@Composable
fun AmityRecommendedCommunityShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        AmityNewsFeedDivider()
        Row(modifier = Modifier.padding(start = 16.dp)) {
            repeat(2) {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 24.dp, start = 0.dp, end = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(278.dp)
                            .height(131.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .width(278.dp)
                            .height(82.dp)
                            .border(
                                width = 1.dp,
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(83.dp)
                                .shimmerBackground(
                                    color = AmityTheme.colors.baseShade4,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(83.dp)
                                .shimmerBackground(
                                    color = AmityTheme.colors.baseShade4,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(83.dp)
                                .shimmerBackground(
                                    color = AmityTheme.colors.baseShade4,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )

                    }
                }
            }
        }
        AmityNewsFeedDivider()

    }
}