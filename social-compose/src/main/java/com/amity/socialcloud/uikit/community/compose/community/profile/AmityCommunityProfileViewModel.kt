package com.amity.socialcloud.uikit.community.compose.community.profile

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityCommunityProfileViewModel(private val communityId: String) :
    AmityBaseViewModel() {
    val disposable = CompositeDisposable()

    private val _communityProfileState by lazy {
        MutableStateFlow(CommunityProfileState(communityId))
    }

    val communityProfileState get() = _communityProfileState

    init {
        refresh()
    }

    fun refresh() {
        disposable.clear()
        viewModelScope.launch {
            _communityProfileState.value = _communityProfileState.value.copy(
                isRefreshing = true
            )
        }

        Flowable.combineLatest(
            AmitySocialClient.newCommunityRepository().getCommunity(communityId).doOnError {},
            AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY).atCommunity(communityId)
                .check().timeout (1, TimeUnit.SECONDS).onErrorReturn { communityProfileState.value.isModerator }
        ) { community, hasPermission -> Pair(community, hasPermission) }
            .doOnNext { (community, isModerator) ->
                val isMember = community.isJoined()
                viewModelScope.launch {
                    delay(100)
                    _communityProfileState.value = _communityProfileState.value.copy(
                        communityId = communityId,
                        community = community,
                        isRefreshing = false,
                        isMember = isMember,
                        isModerator = isModerator
                    )
                }
            }
            .doOnError { error ->
                if (error is AmityException) {
                    _communityProfileState.update {
                        it.copy(
                            error = it.error,
                        )
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let { disposable.add(it) }
    }

    fun getAnnouncement(): Flow<PagingData<AmityPinnedPost>> {
        return AmitySocialClient.newPostRepository()
            .getPinnedPosts(
                communityId = communityId,
                placement = AmityPinnedPost.PinPlacement.ANNOUNCEMENT.value
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getPin(): Flow<PagingData<AmityPinnedPost>> {
        return AmitySocialClient.newPostRepository()
            .getPinnedPosts(
                communityId = communityId,
                placement = AmityPinnedPost.PinPlacement.DEFAULT.value
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getCommunityPosts(): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = communityId,
        )

        return AmitySocialClient.newFeedRepository()
            .getCommunityFeed(communityId)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .map { injector.inject(it) }
            .asFlow()
            .catch {}
    }

    fun getCommunityImagePosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetCommunity(communityId)
            .types(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.IMAGE.getApiKey())))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getCommunityVideoPosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetCommunity(communityId)
            .types(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.VIDEO.getApiKey())))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }
}

data class CommunityProfileState(
    val communityId: String,
    val community: AmityCommunity? = null,
    val isRefreshing: Boolean = true,
    val isMember: Boolean = false,
    val isModerator: Boolean = false,
    val error: AmityError? = null,
)