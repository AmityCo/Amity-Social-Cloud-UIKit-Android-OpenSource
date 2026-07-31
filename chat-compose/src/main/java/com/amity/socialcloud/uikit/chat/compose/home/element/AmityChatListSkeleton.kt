package com.amity.socialcloud.uikit.chat.compose.home.element

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
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityChatListSkeleton(
    modifier: Modifier = Modifier,
    itemCount: Int = 8,
) {
    Column(modifier = modifier) {
        repeat(itemCount) {
            SkeletonItem()
        }
    }
}

@Composable
private fun SkeletonItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Matches the populated row so the list does not shift when loading ends.
            .height(82.dp)
            .background(AmityTheme.token(AmityColorToken.SurfaceListSkeletonSkeleton))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar skeleton
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmerBackground(
                    shape = CircleShape,
                    color = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault),
                ),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Name skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(10.dp)
                    .shimmerBackground(
                        shape = RoundedCornerShape(4.dp),
                        color = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault),
                    ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Preview skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(10.dp)
                    .shimmerBackground(
                        shape = RoundedCornerShape(4.dp),
                        color = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault),
                    ),
            )
        }
    }
}
