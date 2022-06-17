package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Flowable

class AmityCreatePostRoleSelectionViewModel : AmityBaseViewModel() {

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