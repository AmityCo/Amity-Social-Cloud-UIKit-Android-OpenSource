package com.amity.socialcloud.uikit.community.compose.story.target

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.community.compose.story.target.community.AmityStoryCommunityTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryGlobalTabComponent

@Composable
fun AmityStoryTabComponent(
    modifier: Modifier = Modifier,
    type: AmityStoryTabComponentType,
    ) {
    when (type) {
        is AmityStoryTabComponentType.CommunityFeed -> {
            AmityStoryCommunityTabComponent(
                modifier = modifier,
                communityId = type.communityId,
            )
        }

        is AmityStoryTabComponentType.GlobalFeed -> {
            AmityStoryGlobalTabComponent(
                modifier = modifier,
                refreshEventFlow = type.refreshEventFlow,
                onStateChanged = type.onStateChanged
            )
        }
    }
}