package com.amity.socialcloud.uikit.community.mycommunity.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import io.reactivex.Flowable

class AmityMyCommunityListViewModel : AmityBaseViewModel() {

    var myCommunityItemClickListener: AmityMyCommunityItemClickListener? = null
    val searchString = ObservableField("")
    val emptyCommunity = ObservableBoolean(false)

    fun getCommunityList(): Flowable<PagedList<AmityCommunity>> {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.getCommunities()
            .withKeyword(searchString.get() ?: "")
            .filter(AmityCommunityFilter.MEMBER).sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build().query()
    }

    fun setPropertyChangeCallback() {
        searchString.addOnPropertyChanged {
            triggerEvent(AmityEventIdentifier.SEARCH_STRING_CHANGED)
        }
    }
}