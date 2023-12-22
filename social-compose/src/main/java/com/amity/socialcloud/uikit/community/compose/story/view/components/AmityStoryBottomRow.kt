package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryCommentCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryReactionCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryViewCountElement

@Composable
fun AmityStoryBottomRow(
    modifier: Modifier = Modifier,
    story: AmityStory,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp, top = 8.dp)
    ) {

        AmityStoryViewCountElement(
            count = "0",
            modifier = Modifier.align(Alignment.CenterStart)
        ) {

        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            AmityStoryCommentCountElement(
//                count = story.getCommentCount().readableNumber(),
                count = "0",
            ) {

            }

            AmityStoryReactionCountElement(
//                count = story.getReactionCount().readableNumber(),
                count = "0",
                isSelected = false,
                onSelectedChanged = { }
            )
        }
    }
}

@Preview
@Composable
fun AmityStoryBottomRowPreview() {
//    AmityStoryBottomRow()
}