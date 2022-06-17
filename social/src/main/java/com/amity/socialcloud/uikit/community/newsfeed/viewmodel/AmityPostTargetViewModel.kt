package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import io.reactivex.Flowable

private const val SAVED_POST_CREATION_TYPE = "SAVED_POST_CREATION_TYPE"

class AmityPostTargetViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    var postCreationType: String = ""
        set(value) {
            savedState.set(SAVED_POST_CREATION_TYPE, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_POST_CREATION_TYPE)?.let { postCreationType = it }
    }

    fun getUser(): AmityUser {
        return AmityCoreClient.getCurrentUser().blockingFirst()
    }


    fun getCommunityList(): Flowable<PagingData<AmityCommunity>> {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.getCommunities()
            .filter(AmityCommunityFilter.MEMBER)
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .getPagingData()
    }
}