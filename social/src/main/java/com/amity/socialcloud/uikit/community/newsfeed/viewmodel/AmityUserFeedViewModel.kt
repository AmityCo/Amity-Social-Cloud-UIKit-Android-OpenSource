package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_USER_ID"

class AmityUserFeedViewModel(private val savedState: SavedStateHandle) : AmityFeedViewModel() {

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }

    var userId: String = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }

    @ExperimentalPagingApi
    override fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit): Completable {
        return AmitySocialClient
            .newPostRepository()
            .getPosts()
            .targetUser(userId)
            .includeDeleted(false)
            .build()
            .query()
            .map { it.map { post -> createPostItem(post) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }

}