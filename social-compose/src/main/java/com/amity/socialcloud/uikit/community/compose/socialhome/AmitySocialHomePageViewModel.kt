package com.amity.socialcloud.uikit.community.compose.socialhome

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTraySeen
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryGlobalTabViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmitySocialHomePageViewModel : AmityBaseViewModel() {

    private val _postListState by lazy {
        MutableStateFlow<PostListState>(PostListState.EMPTY)
    }
    val postListState get() = _postListState

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(CommunityListState.EMPTY)
    }
    val communityListState get() = _communityListState

    private val _isGlobalFeedRefreshing by lazy {
        MutableStateFlow(false)
    }

    val isGlobalFeedRefreshing get() = _isGlobalFeedRefreshing

    private val _notificationTraySeen = MutableStateFlow<AmityNotificationTraySeen?>(null)
    val notificationTraySeen: StateFlow<AmityNotificationTraySeen?> get() = _notificationTraySeen.asStateFlow()

    private val _isPullRefreshIndicatorVisible by lazy {
        MutableStateFlow(false)
    }

    val isPullRefreshIndicatorVisible get() = _isPullRefreshIndicatorVisible

    private var storyTabState: AmityStoryGlobalTabViewModel.TargetListState =
        AmityStoryGlobalTabViewModel.TargetListState.EMPTY

    private val _storyTabVisible by lazy {
        MutableStateFlow(true)
    }
    val isStoryTabVisible get() = _storyTabVisible

    fun setPostListState(state: PostListState) {
        _postListState.value = state
    }

    fun setStoryTabState(state: AmityStoryGlobalTabViewModel.TargetListState) {
        if (storyTabState == state) {
            return
        }
        storyTabState = state

        when (storyTabState) {
            AmityStoryGlobalTabViewModel.TargetListState.LOADING -> {
                _storyTabVisible.value = true
            }

            AmityStoryGlobalTabViewModel.TargetListState.SUCCESS -> {
                _storyTabVisible.value = true
            }

            AmityStoryGlobalTabViewModel.TargetListState.EMPTY -> {
                _storyTabVisible.value = false
            }
        }
    }

    fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    fun setGlobalFeedRefreshing(showIndicator: Boolean = true) {
        viewModelScope.launch {
            _isGlobalFeedRefreshing.value = true
            if (showIndicator) {
                _isPullRefreshIndicatorVisible.value = true
            }
            delay(1500)
            _isGlobalFeedRefreshing.value = false
            _isPullRefreshIndicatorVisible.value = false
        }
    }

    fun getMyCommunities(): Flow<PagingData<AmityCommunity>> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunities()
            .filter(AmityCommunityFilter.MEMBER)
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getGlobalFeed(): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = null,
        )

        return AmitySocialClient.newFeedRepository()
            .getGlobalFeed()
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

    fun getGlobalPinnedPosts(): Flow<List<AmityPinnedPost>> {
        return AmitySocialClient.newPostRepository()
            .getGlobalPinnedPosts()
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun scheduleNotificationTraySeen() {
        viewModelScope.launch {
            while (true) {
                getNotificationTraySeen()
                delay(60000)
            }
        }
    }

    private fun getNotificationTraySeen() {
        viewModelScope.launch {
            AmityCoreClient.notificationTray()
                .getNotificationTraySeen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
                .collectLatest {
                    _notificationTraySeen.update { currentState ->
                        it
                    }
                }
        }
    }

    fun markTraySeen() {
        viewModelScope.launch {
            addDisposable(
                AmityCoreClient.notificationTray()
                    .markTraySeen()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    suspend fun refreshGlobalPinnedPosts() {
        getGlobalPinnedPosts().collectLatest {}
    }

    sealed class PostListState {
        object LOADING : PostListState()
        object SUCCESS : PostListState()
        object EMPTY : PostListState()
        object ERROR : PostListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): PostListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }

    sealed class CommunityListState {
        object LOADING : CommunityListState()
        object SUCCESS : CommunityListState()
        object EMPTY : CommunityListState()
        object ERROR : CommunityListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): CommunityListState {
                return if (loadState is LoadState.Loading) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0 && loadState.endOfPaginationReached) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }
}