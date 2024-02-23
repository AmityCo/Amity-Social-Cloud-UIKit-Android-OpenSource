package com.amity.socialcloud.uikit.community.compose.story.target.utils

import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget

enum class AmityStoryTargetRingUiState {
    SEEN,
    HAS_UNSEEN,
    SYNCING,
    FAILED,
}

fun AmityStoryTarget.toRingUiState(): AmityStoryTargetRingUiState {
    return when {
        getFailedStoriesCount() > 0 -> AmityStoryTargetRingUiState.FAILED
        getSyncingStoriesCount() > 0 -> AmityStoryTargetRingUiState.SYNCING
        hasUnseen() -> AmityStoryTargetRingUiState.HAS_UNSEEN
        else -> AmityStoryTargetRingUiState.SEEN
    }
}