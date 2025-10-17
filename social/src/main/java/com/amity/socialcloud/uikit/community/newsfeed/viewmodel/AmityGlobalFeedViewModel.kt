package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostHeaderItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityGlobalFeedViewModel : AmityFeedViewModel() {

    @ExperimentalPagingApi
    override fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit): Completable {
        val feedRepository = AmitySocialClient.newFeedRepository()
        return feedRepository.getGlobalFeed()
            .dataTypes(AmitySocialBehaviorHelper.supportedPostTypes.filter { it != AmityPost.DataType.CLIP })
            .build()
            .query()
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