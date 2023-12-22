package com.amity.socialcloud.uikit.community.detailpage

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.data.CommunityProfileData
import com.amity.socialcloud.uikit.community.data.PostReviewBannerData
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostCountAdapter
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.PermissionViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AmityCommunityProfileViewModel(private val savedState: SavedStateHandle) :
    AmityBaseViewModel(), PermissionViewModel {

    var communityId: String? = null
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let { communityId = it }
    }

    fun refreshCommunity() {
        getCommunityDetail()
            .ignoreElements()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun joinCommunity(): Completable {
        return AmitySocialClient.newCommunityRepository().joinCommunity(communityId!!)
    }

    private fun getCommunityDetail(): Flowable<AmityCommunity> {
        return AmitySocialClient.newCommunityRepository().getCommunity(communityId!!)
    }

    @ExperimentalPagingApi
    private fun hasPendingPosts(
        adapter: AmityPostCountAdapter,
        publisher: BehaviorSubject<Boolean>
    ): Flowable<Boolean> {
        return AmitySocialClient.newFeedRepository()
            .getCommunityFeed(communityId!!)
            .feedType(AmityFeedType.REVIEWING)
            .includeDeleted(false)
            .build()
            .query()
            .map {
                CoroutineScope(Dispatchers.IO).launch {
                    adapter.submitData(it)
                }
            }.flatMap {
                publisher.toFlowable(BackpressureStrategy.BUFFER)
            }
    }

    private fun getReviewingFeedPostCount(): Flowable<Int> {
        return getCommunityDetail().flatMap {
            it.getPostCount(AmityFeedType.REVIEWING)
        }
    }

    @ExperimentalPagingApi
    fun observeCommunity(onLoaded: (CommunityProfileData) -> Unit): Completable {
        val communitySource = getCommunityDetail()

        val adapter = AmityPostCountAdapter()
        val hasPendingPostPublisher = BehaviorSubject.create<Boolean>()
        adapter.addLoadStateListener {
            hasPendingPostPublisher.onNext(adapter.itemCount > 0)
        }

        var lastPostCount: Int? = null
        var lastJoinedState: Boolean? = null
        var lastReviewEnabledState: Boolean? = null

        return Flowable.combineLatest(
            communitySource,
            hasPendingPosts(adapter, hasPendingPostPublisher).startWith(Single.just(false)),
            getReviewingFeedPostCount().startWith(Single.just(0)),
            hasCommunityPermission(
                communitySource, getCommunityPermissionSource(
                    communityId!!,
                    AmityPermission.REVIEW_COMMUNITY_POST
                )
            ),
            hasCommunityPermission(
                communitySource, getCommunityPermissionSource(
                    communityId!!,
                    AmityPermission.EDIT_COMMUNITY
                )
            ),
        ) { community, hasPendingPosts, postCount, isReviewer, canEditCommunity ->
            var shouldRefreshHasPendingPost = false

            lastPostCount?.let {
                if ((postCount == 0 && it > 0) || (postCount > 0 && it == 0)) {
                    shouldRefreshHasPendingPost = true
                }
            }
            lastPostCount = postCount

            lastJoinedState?.let {
                if (it != community.isJoined()) {
                    shouldRefreshHasPendingPost = true
                }
            }
            lastJoinedState = community.isJoined()

            lastReviewEnabledState?.let {
                if (it != isPostReviewEnabled(community)) {
                    shouldRefreshHasPendingPost = true
                }
            }

            lastReviewEnabledState = isPostReviewEnabled(community)

            if (shouldRefreshHasPendingPost) {
                adapter.refresh()
            }

            val shouldShowBanner =
                hasPendingPosts && community.isJoined() && isPostReviewEnabled(community)
            val bannerData = PostReviewBannerData(isReviewer, postCount, shouldShowBanner)
            CommunityProfileData(community, canEditCommunity, bannerData)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onLoaded.invoke(it)
            }
            .doOnError {
                Timber.e(it)
            }
            .ignoreElements()
    }

    private fun isPostReviewEnabled(community: AmityCommunity): Boolean {
        return community.getPostSettings() == AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED
    }
}

private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"