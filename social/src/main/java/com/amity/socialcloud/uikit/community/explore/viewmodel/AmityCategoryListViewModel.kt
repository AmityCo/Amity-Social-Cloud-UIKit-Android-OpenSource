package com.amity.socialcloud.uikit.community.explore.viewmodel

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.category.query.AmityCommunityCategorySortOption
import com.amity.socialcloud.sdk.api.social.community.AmityCommunityRepository
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import io.reactivex.rxjava3.core.Flowable

class AmityCategoryListViewModel : AmityBaseViewModel() {

    var categoryItemClickListener: AmityCategoryItemClickListener? = null
    private val communityRepository: AmityCommunityRepository =
        AmitySocialClient.newCommunityRepository()

    fun getCategories(): Flowable<PagingData<AmityCommunityCategory>> {
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
            .query()
    }
}