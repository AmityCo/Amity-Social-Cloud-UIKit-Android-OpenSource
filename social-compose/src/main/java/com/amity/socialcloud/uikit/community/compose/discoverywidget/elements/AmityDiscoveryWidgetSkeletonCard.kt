package com.amity.socialcloud.uikit.community.compose.discoverywidget.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

private val CARD_HEIGHT = 480.dp
private val HEADER_HEIGHT = 52.dp

/** Two slots, not three: the engagement bar is absent while loading and the text slot takes its 36. */
private val TEXT_HEIGHT = 428.dp

private val BAR_HEIGHT = 8.dp

/**
 * The placeholder card shown while the pool is outstanding. Every shape is a fully rounded
 * Base/Shade4 pill, and no count or engagement row appears — the skeleton promises nothing the
 * loaded card might not have.
 */
@Composable
fun AmityDiscoveryWidgetSkeletonCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(CARD_HEIGHT)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.background)
            .border(1.dp, AmityTheme.colors.baseShade4, RoundedCornerShape(8.dp))
            .clearAndSetSemantics { },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HEADER_HEIGHT)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .shimmerBackground(
                        shape = CircleShape,
                        color = AmityTheme.colors.baseShade4,
                    )
            )
            Column(
                modifier = Modifier.height(32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Bar(width = 180.dp, topPadding = 4.dp)
                Bar(width = 64.dp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(TEXT_HEIGHT)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Bar(width = 240.dp)
            Bar(width = 180.dp)
            Bar(width = 297.dp)
        }
    }
}

@Composable
private fun Bar(width: Dp, topPadding: Dp = 0.dp) {
    Box(
        modifier = Modifier
            .padding(top = topPadding)
            .width(width)
            .height(BAR_HEIGHT)
            .shimmerBackground(
                shape = CircleShape,
                color = AmityTheme.colors.baseShade4,
            )
    )
}
