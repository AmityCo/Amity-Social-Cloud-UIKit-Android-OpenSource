package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import io.reactivex.rxjava3.core.Flowable

private const val SAVED_STORY_CREATION_TYPE = "SAVED_STORY_CREATION_TYPE"

class AmityStoryTargetViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    var storyCreationType: String = ""
        set(value) {
            savedState[SAVED_STORY_CREATION_TYPE] = value
            field = value
        }

    init {
        savedState.get<String>(SAVED_STORY_CREATION_TYPE)?.let { storyCreationType = it }
    }

    fun getCommunityList(): Flowable<PagingData<AmityCommunity>> {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.getCommunities()
            .filter(AmityCommunityFilter.MEMBER)
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .query()
    }
}