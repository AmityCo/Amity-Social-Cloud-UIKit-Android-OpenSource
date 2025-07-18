package com.amity.socialcloud.uikit.community.compose.clip.view.util

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import kotlinx.coroutines.flow.Flow

object SharedClipFeedStore {
    private var sharedPagingData: Flow<PagingData<AmityPost>>? = null
    private var selectedIndex: Int = 0
    private var feedId: String? = null // Can be userId or communityId
    private var feedType: String? = null // "user" or "community"

    fun setClipPagingData(
        feedId: String,
        feedType: String,
        pagingData: Flow<PagingData<AmityPost>>,
        selectedIndex: Int
    ) {
        this.feedId = feedId
        this.feedType = feedType
        this.sharedPagingData = pagingData
        this.selectedIndex = selectedIndex
    }

    fun getClipPagingData(feedId: String, feedType: String): Pair<Flow<PagingData<AmityPost>>?, Int>? {
        return if (this.feedId == feedId && this.feedType == feedType) {
            Pair(sharedPagingData, selectedIndex)
        } else null
    }

    fun clear() {
        sharedPagingData = null
        selectedIndex = 0
        feedId = null
        feedType = null
    }
}