package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostHeaderItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityCustomPostRankingFeedViewModel : AmityFeedViewModel() {

    @ExperimentalPagingApi
    override fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit): Completable {
        val feedRepository = AmitySocialClient.newFeedRepository()
        return feedRepository.getCustomRankingGlobalFeed()
            .build()
            .getPagingData()
            .map { it.map { post -> createPostItem(post) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }

    override fun createPostHeaderItems(post: AmityPost): List<AmityBasePostHeaderItem> {
        val showHeader = AmitySocialUISettings.getViewHolder(getDataType(post)).enableHeader()
        return if (showHeader) {
            listOf(AmityBasePostHeaderItem(post = post, showTarget = true))
        } else {
            emptyList()
        }
    }


}