package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityStoryReactionCountElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    count: Int = 0,
    isCommunityJoined: Boolean = true,
    isReactedByMe: Boolean = false,
    onReactChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    var isReacted by remember { mutableStateOf(isReactedByMe) }
    var reactionCount by remember { mutableIntStateOf(count) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(40.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(AmityTheme.colors.secondary)
            .clickableWithoutRipple {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                if (isCommunityJoined) {
                    isReacted = !isReacted
                    onReactChange(isReacted)
                    reactionCount = if (isReacted) reactionCount + 1 else reactionCount - 1
                } else {
                    pageScope?.showSnackbar(
                        message = context.getString(R.string.amity_join_community_to_interact),
                        drawableRes = null,
                    )
                }
            }
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag("reaction_button")
    ) {
        Icon(
            imageVector = if (isReacted) ImageVector.vectorResource(id = R.drawable.amity_ic_story_liked_pressed)
            else ImageVector.vectorResource(id = R.drawable.amity_ic_story_like_normal),
            contentDescription = "Story Reaction Count",
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified,
        )
        Text(
            text = reactionCount.readableNumber(),
            color = Color.White,
            modifier = modifier
                .height(20.dp)
                .padding(start = 4.dp)
                .widthIn(min = 16.dp)
                .testTag("reaction_button_text_view")
        )
    }
}

@Preview
@Composable
fun AmityStoryReactionCountElementPreview() {
    AmityStoryReactionCountElement {}
}