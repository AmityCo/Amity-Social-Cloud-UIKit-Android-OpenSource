package com.amity.socialcloud.uikit.community.explore.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategorySortOption
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityExploreCommunityViewModel : AmityBaseViewModel() {

    val emptyRecommendedList = ObservableBoolean(false)
    val emptyTrendingList = ObservableBoolean(false)
    val emptyCategoryList = ObservableBoolean(false)


    fun getRecommendedCommunity(onRecommendedCommunitiesLoaded: (List<AmityCommunity>) -> Unit): Completable {
        return AmitySocialClient.newCommunityRepository()
            .getRecommendedCommunities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onRecommendedCommunitiesLoaded.invoke(it) }
            .doOnError { }
            .ignoreElements()
    }

    fun getTrendingCommunity(onTrendingCommunitiesLoaded: (List<AmityCommunity>) -> Unit): Completable {
        return AmitySocialClient.newCommunityRepository()
            .getTrendingCommunities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onTrendingCommunitiesLoaded.invoke(it) }
            .doOnError { }
            .ignoreElements()
    }

    fun getCommunityCategory(onCommunityCategoriesLoaded: (PagedList<AmityCommunityCategory>) -> Unit): Completable {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.getCategories()
            .sortBy(AmityCommunityCategorySortOption.NAME)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onCommunityCategoriesLoaded.invoke(it) }
            .doOnError { }
            .ignoreElements()
    }
}