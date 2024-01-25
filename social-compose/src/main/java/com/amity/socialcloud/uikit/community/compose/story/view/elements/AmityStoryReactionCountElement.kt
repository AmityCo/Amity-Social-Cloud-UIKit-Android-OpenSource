package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityStoryReactionCountElement(
    modifier: Modifier = Modifier,
    count: String = "0",
    isSelected: Boolean = false,
    onSelectedChanged: (Boolean) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(40.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(Color(0xFF292B32))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onSelectedChanged(!isSelected)
            }
    ) {
        Icon(
            imageVector = if (isSelected) ImageVector.vectorResource(id = R.drawable.amity_ic_story_liked_pressed)
            else ImageVector.vectorResource(id = R.drawable.amity_ic_story_like_normal),
            contentDescription = "Story Reaction Count",
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified,
        )
        Text(
            text = count,
            color = Color.White,
            modifier = modifier
                .height(20.dp)
                .padding(start = 4.dp)
                .widthIn(min = 16.dp)
        )
    }
}

@Preview
@Composable
fun AmityStoryReactionCountElementPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AmityStoryReactionCountElement(
        isSelected = isSelected,
        onSelectedChanged = { isSelected = it }
    )
}