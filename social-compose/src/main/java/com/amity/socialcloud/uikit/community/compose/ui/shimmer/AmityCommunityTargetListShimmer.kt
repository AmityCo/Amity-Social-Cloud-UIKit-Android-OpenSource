package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityCommunityTargetListShimmer(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        repeat(12) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .aspectRatio(1f)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(140.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}