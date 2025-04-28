package com.amity.socialcloud.uikit.community.compose.notificationtray.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityNotificationTrayShimmer(modifier: Modifier = Modifier) {
    // Define shimmer directly within the component for better encapsulation
    val shimmerGroups = mapOf(
        "Header" to listOf("1", "4"),
        "Header2" to listOf("2", "6")
    )

    Column(modifier = modifier.fillMaxWidth().height(500.dp)) {
        shimmerGroups.forEach { (_, items) ->
            // Header shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 18.dp, bottom = 8.dp, end = 16.dp)
            ) {
                Box(
                    Modifier
                        .width(131.dp)
                        .height(10.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

            // Item shimmers
            items.forEach { _ ->
                NotificationItemShimmer()
            }
        }
    }
}

@Composable
private fun NotificationItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar shimmer
        Box(
            modifier = Modifier
                .size(32.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(40.dp)
                )
        )

        Spacer(Modifier.width(12.dp))

        // Content shimmer
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                Modifier
                    .width(233.dp)
                    .height(10.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .width(132.dp)
                    .height(10.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Composable
@Preview
private fun AmityNotificationTrayShimmerPreview() {
    MaterialTheme {
        AmityNotificationTrayShimmer()
    }
}