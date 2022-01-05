package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostGalleryAdapter
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.TARGET_COMMUNITY
import com.amity.socialcloud.uikit.community.newsfeed.model.TARGET_USER
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

private const val SAVED_TARGET_TYPE = "SAVED_TARGET_TYPE"
private const val SAVED_TARGET_ID = "SAVED_TARGET_ID"
private const val SAVED_POST_TYPE = "SAVED_POST_TYPE"
private const val SAVED_INCLUDE_DELETE = "SAVED_INCLUDE_DELETE"


class AmityMediaPostViewModel(private val savedState: SavedStateHandle) : AmityBaseViewModel() {

    private val postGalleryClickPublisher = PublishSubject.create<PostGalleryClickEvent>()

    var targetType = ""
        set(value) {
            savedState.set(SAVED_TARGET_TYPE, value)
            field = value
        }
    var targetId = ""
        set(value) {
            savedState.set(SAVED_TARGET_ID, value)
            field = value
        }
    var postType = AmityPost.DataType.IMAGE.getApiKey()
        set(value) {
            savedState.set(SAVED_POST_TYPE, value)
            field = value
        }
    var includeDelete = false
        set(value) {
            savedState.set(SAVED_INCLUDE_DELETE, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_TARGET_TYPE)?.let { targetType = it }
        savedState.get<String>(SAVED_TARGET_ID)?.let { targetId = it }
        savedState.get<String>(SAVED_POST_TYPE)?.let { postType = it }
        savedState.get<Boolean>(SAVED_INCLUDE_DELETE)?.let { includeDelete = it }
    }

    fun createAdapter(): AmityPostGalleryAdapter {
        return AmityPostGalleryAdapter(postGalleryClickPublisher)
    }

    fun getPostGalleryClickEvents(onReceivedEvent: (PostGalleryClickEvent) -> Unit): Completable {
        return postGalleryClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onReceivedEvent.invoke(it) }
            .ignoreElements()
    }

    @ExperimentalPagingApi
    fun getPosts(onPageLoaded: (posts: PagingData<AmityPost>) -> Unit): Completable {
        return when (targetType) {
            TARGET_USER -> getUserPosts(onPageLoaded)
            TARGET_COMMUNITY -> getCommunityPosts(onPageLoaded)
            else -> Completable.error(Exception("Target type mismatch"))
        }
    }

    @ExperimentalPagingApi
    private fun getUserPosts(onPageLoaded: (posts: PagingData<AmityPost>) -> Unit): Completable {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetUser(targetId)
            .includeDeleted(includeDelete)
            .types(listOf(AmityPost.DataType.sealedOf(postType)))
            .build()
            .getPagingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }

    @ExperimentalPagingApi
    private fun getCommunityPosts(onPageLoaded: (posts: PagingData<AmityPost>) -> Unit): Completable {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetCommunity(targetId)
            .includeDeleted(includeDelete)
            .types(listOf(AmityPost.DataType.sealedOf(postType)))
            .build()
            .getPagingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }
}
