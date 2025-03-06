package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import io.reactivex.rxjava3.core.Flowable

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

    fun getUser(onUserFound: (AmityUser) -> Unit, onUserNotFound: () -> Unit) {
        AmityCoreClient.getCurrentUser()
            .doOnNext {
                onUserFound(it)
            }
            .doOnError {
                onUserNotFound()
            }
            .subscribe()
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