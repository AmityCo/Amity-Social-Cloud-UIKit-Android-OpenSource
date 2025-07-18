package com.amity.socialcloud.uikit.community.compose.clip.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityClipFeedPageType : Parcelable {
    data class CommunityFeed(val postId: String, val communityId: String) : AmityClipFeedPageType()
    object GlobalFeed : AmityClipFeedPageType()
    data class NewsFeed(val postId: String) : AmityClipFeedPageType()
    data class UserNewsFeed(val postId: String) : AmityClipFeedPageType()
    @Parcelize
    data class UserClipFeed(val userId: String) : AmityClipFeedPageType()
    @Parcelize
    data class CommunityClipFeed(val communityId: String) : AmityClipFeedPageType()
}