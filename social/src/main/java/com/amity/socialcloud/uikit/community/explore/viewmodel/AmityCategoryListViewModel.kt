package com.amity.socialcloud.uikit.community.explore.viewmodel

import androidx.paging.PagedList
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategorySortOption
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import io.reactivex.Flowable

class AmityCategoryListViewModel : AmityBaseViewModel() {

    var categoryItemClickListener: AmityCategoryItemClickListener? = null
    private val communityRepository: AmityCommunityRepository =
        AmitySocialClient.newCommunityRepository()

    fun getCategories(): Flowable<PagedList<AmityCommunityCategory>> {
        return communityRepository.getCategories()
            .sortBy(AmityCommunityCategorySortOption.NAME)
            .includeDeleted(false)
            .build()
            .query()
    }

    fun getCommunityByCategory(parentCategoryId: String): Flowable<PagingData<AmityCommunity>> {
        return communityRepository
            .getCommunities()
            .categoryId(parentCategoryId)
            .includeDeleted(false)
            .build()
            .getPagingData()
    }
}