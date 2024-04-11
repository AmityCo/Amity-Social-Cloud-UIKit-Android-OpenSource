package com.amity.socialcloud.uikit.community.compose.story.target

sealed class AmityStoryTabComponentType {
    data class CommunityFeed(val communityId: String) : AmityStoryTabComponentType()
    object GlobalFeed : AmityStoryTabComponentType()
}