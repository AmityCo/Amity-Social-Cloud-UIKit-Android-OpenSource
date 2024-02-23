package com.amity.socialcloud.uikit.community.compose.story.view.elements


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@Composable
fun AmityStoryViewCountElement(
    modifier: Modifier = Modifier,
    count: String = "0",
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_story_view_count),
            contentDescription = "Story View Count",
            modifier = Modifier.size(20.dp),
            tint = AmityTheme.colors.secondaryShade3,
        )
        Text(
            text = count,
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun AmityStoryViewCountElementPreview() {
    AmityStoryViewCountElement {}
}