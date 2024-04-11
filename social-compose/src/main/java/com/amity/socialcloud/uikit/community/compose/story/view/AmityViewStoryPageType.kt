package com.amity.socialcloud.uikit.community.compose.story.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityViewStoryPageType : Parcelable {
    data class CommunityFeed(val communityId: String) : AmityViewStoryPageType()
    data class GlobalFeed(val communityId: String) : AmityViewStoryPageType()
}