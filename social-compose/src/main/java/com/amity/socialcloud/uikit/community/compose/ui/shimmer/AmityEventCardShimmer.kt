package com.amity.socialcloud.uikit.community.compose.ui.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.community.profile.component.EventCardStyle

/**
 * Shimmer loading skeleton for event cards
 * Supports different styles: Large, Medium, and List
 */
@Composable
fun AmityEventCardShimmer(
    style: EventCardStyle,
    modifier: Modifier = Modifier
) {
    when (style) {
        EventCardStyle.Large -> EventCardLargeShimmer(modifier)
        EventCardStyle.Medium -> EventCardMediumShimmer(modifier)
        EventCardStyle.List -> EventCardListShimmer(modifier)
    }
}

/**
 * Shimmer for large event card
 */
@Composable
private fun EventCardLargeShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )

        )
        Spacer(modifier = Modifier.height(22.dp))
        // Cover Image Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(193.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // First text line shimmer
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Second text line shimmer
        Box(
            modifier = Modifier
                .width(244.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Third text line shimmer (shorter)
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

/**
 * Shimmer for medium event card
 */
@Composable
private fun EventCardMediumShimmer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(251.dp)
            .height(221.dp)
    ) {
        Column {
            // Cover Image Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            )
            
            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Time shimmer
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(10.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Title shimmer
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Creator name shimmer
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(10.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}

/**
 * Shimmer for list event card
 */
@Composable
private fun EventCardListShimmer(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail Shimmer
        Box(
            modifier = Modifier
                .width(142.dp)
                .height(80.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        
        // Content Section
        Column(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Time shimmer
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title shimmer
            Box(
                modifier = Modifier
                    .width(162.dp)
                    .height(12.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Creator name shimmer
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
}

/**
 * Multiple event card shimmers for loading states
 */
@Composable
fun AmityEventCardListShimmer(
    style: EventCardStyle = EventCardStyle.List,
    count: Int = 3,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        repeat(count) {
            AmityEventCardShimmer(style = style)
        }
    }
}
