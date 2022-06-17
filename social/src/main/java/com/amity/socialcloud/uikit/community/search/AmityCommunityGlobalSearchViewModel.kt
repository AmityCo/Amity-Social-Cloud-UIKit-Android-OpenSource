package com.amity.socialcloud.uikit.community.search

import androidx.databinding.ObservableField
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityCommunityGlobalSearchViewModel: AmityBaseViewModel() {
    var searchString = ObservableField("")

    fun searchCommunity(onResult: (list: PagingData<AmityCommunity>) -> Unit): Completable {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.getCommunities()
            .withKeyword(searchString.get() ?: "")
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .getPagingData()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    fun setPropertyChangeCallback() {
        searchString.addOnPropertyChanged {
            triggerEvent(AmityEventIdentifier.SEARCH_COMMUNITY)
        }
    }
}