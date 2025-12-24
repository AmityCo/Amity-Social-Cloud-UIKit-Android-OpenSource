package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

/**
 * Shimmer loading skeleton for event detail page header
 * Shows loading state for cover image, title, and details sections
 */
@Composable
fun AmityEventDetailShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
    ) {
        // Cover Image Shimmer with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // Cover image shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(0.dp)
                    )
            )
            
            // Back button shimmer (top left)
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = CircleShape
                    )
                    .align(Alignment.TopStart)
            )
        }
        
        // Title and Details Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .width(156.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(6.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(279.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            
            Spacer(modifier = Modifier.height(35.dp))

            repeat(3) { index ->
                Column (
                    modifier = Modifier.padding(bottom = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(57.dp)
                            .height(8.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .width(163.dp)
                            .height(12.dp)
                            .shimmerBackground(
                                color = AmityTheme.colors.baseShade4,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
