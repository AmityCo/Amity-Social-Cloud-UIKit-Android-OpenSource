package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground


@Composable
fun AmityUserProfileHeaderShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(56.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = CircleShape,
                    )
            )

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
            )
        }


        Box(
            modifier = Modifier
                .width(240.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(size = 12.dp)
                )
        )

        Box(
            modifier = Modifier
                .width(300.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(size = 12.dp)
                )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityUserProfileHeaderShimmerPreview() {
    AmityUserProfileHeaderShimmer()
}