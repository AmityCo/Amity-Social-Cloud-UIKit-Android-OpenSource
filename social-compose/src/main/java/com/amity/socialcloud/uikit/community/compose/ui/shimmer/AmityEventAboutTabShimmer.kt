package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

/**
 * Shimmer loading skeleton for event about/image tab content
 * Shows loading state for text lines representing event information
 */
@Composable
fun AmityEventAboutTabShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
        
        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )

        Spacer(modifier = Modifier.height(7.dp))

        Box(
            modifier = Modifier
                .width(244.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )

        Spacer(modifier = Modifier.height(7.dp))
        
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}
