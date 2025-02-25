package com.amity.socialcloud.uikit.community.compose.story.target

import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryGlobalTabViewModel
import kotlinx.coroutines.flow.MutableStateFlow

sealed class AmityStoryTabComponentType {
    data class CommunityFeed(val communityId: String) : AmityStoryTabComponentType()
    data class GlobalFeed(
        val refreshEventFlow: MutableStateFlow<Boolean> = MutableStateFlow(false),
        val onStateChanged: (AmityStoryGlobalTabViewModel.TargetListState) -> Unit = {}
    ) : AmityStoryTabComponentType()
}