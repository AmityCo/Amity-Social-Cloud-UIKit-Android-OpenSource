package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityFeedRepository
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"
private const val SAVED_FEED_TYPE = "SAVED_FEED_TYPE"

class AmityCommunityFeedViewModel(private val savedState: SavedStateHandle) : AmityFeedViewModel() {

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
        savedState.get<AmityFeedType>(SAVED_FEED_TYPE)?.let { feedType = it }
    }

    var communityId: String = ""
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }
    var feedType: AmityFeedType = AmityFeedType.PUBLISHED
        set(value) {
            savedState.set(SAVED_FEED_TYPE, value)
            field = value
        }
    var latestReviewPermissionState: Boolean? = null

    @ExperimentalPagingApi
    override fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit): Completable {
        val feedRepository: AmityFeedRepository = AmitySocialClient.newFeedRepository()
        return feedRepository.getCommunityFeed(communityId)
            .feedType(feedType)
            .includeDeleted(false)
            .build()
            .getPagingData()
            .map { it.map { post -> createPostItem(post) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }

    fun observeCommunityStatus(
        onReadyToRender: () -> Unit,
        onRefreshNeeded: () -> Unit
    ): Completable {
        val communitySource = AmitySocialClient.newCommunityRepository().getCommunity(communityId)
        val reviewPermissionSource = hasCommunityPermission(
            communitySource,
            getCommunityPermissionSource(communityId, AmityPermission.REVIEW_COMMUNITY_POST)
        )
        var isReadyToRender = false
        var isRefreshNeeded = false
        var lastJoinedState: Boolean? = null
        return Flowable.zip(communitySource, reviewPermissionSource,
            { community, hasReviewPermission ->
                lastJoinedState?.let {
                    if (lastJoinedState != community.isJoined()) {
                        isRefreshNeeded = true
                    }
                }
                lastJoinedState = community.isJoined()

                this.latestReviewPermissionState?.let {
                    if (this.latestReviewPermissionState != hasReviewPermission) {
                        if (feedType == AmityFeedType.REVIEWING) {
                            isRefreshNeeded = true
                        }
                    }
                }
                this.latestReviewPermissionState = hasReviewPermission
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (!isReadyToRender) {
                    isReadyToRender = true
                    onReadyToRender.invoke()
                } else if (isRefreshNeeded) {
                    isRefreshNeeded = false
                    onRefreshNeeded.invoke()
                }
            }
            .ignoreElements()
    }

    override fun createPostFooterItems(post: AmityPost): List<AmityBasePostFooterItem> {
        val footerItems = mutableListOf<AmityBasePostFooterItem>()
        if (feedType == AmityFeedType.REVIEWING) {
            if (latestReviewPermissionState == true) {
                footerItems.add(AmityBasePostFooterItem.POST_REVIEW(post))
            }
        } else {
            footerItems.addAll(super.createPostFooterItems(post))
        }
        return footerItems
    }

}