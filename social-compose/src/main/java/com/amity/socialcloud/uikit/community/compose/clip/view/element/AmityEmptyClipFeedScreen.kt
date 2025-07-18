package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityEmptyClipFeedScreen(
    modifier: Modifier = Modifier,
    onExploreCommunityClick: () -> Unit = {},
    onCreateCommunityClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmityTheme.colors.baseShade1)
    ) {
        // Top Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(102.dp) // adjust as needed
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )

        // Bottom Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(168.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                    )
                )
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.amity_v4_empty_clip_feed),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(160.dp)
            )
            Text(
                text = "Your feed is empty",
                style = AmityTheme.typography.titleBold,
                color = Color.White
            )
            Text(
                text = "Find community or create your own. ",
                style = AmityTheme.typography.caption,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 16.dp)
                    .clickableWithoutRipple {
                        onExploreCommunityClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_v4_language_icon),
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Explore community",
                    style = AmityTheme.typography.bodyBold,
                    color = Color.White,
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Create community",
                style = AmityTheme.typography.bodyBold,
                color = Color.White,
                modifier = Modifier.clickableWithoutRipple {
                    onCreateCommunityClick()
                }
            )

        }


    }
}

@Preview
@Composable
private fun AmityEmptyClipFeedScreenPreview() {
    // Preview of the empty clip feed screen.
    AmityEmptyClipFeedScreen()
}

