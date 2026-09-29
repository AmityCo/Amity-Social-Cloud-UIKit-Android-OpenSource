package com.amity.socialcloud.uikit.community.compose.socialhome

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTraySeen
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryGlobalTabViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.amity.socialcloud.uikit.common.config.AmityUIKitDataGate
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
import com.amity.socialcloud.uikit.community.compose.dropGatedPostTypes
import com.amity.socialcloud.uikit.community.compose.dropGatedPinnedPosts

class AmitySocialHomePageViewModel : AmityBaseViewModel() {

    private val feedOwnerId = AmityPostComposerHelper.attachFeed()

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

    private val _isForYouEnabledFromSettings = MutableStateFlow<Boolean?>(null)
    private val _isForYouEnabledFromFeed = MutableStateFlow(true)

    val isForYouEnabled: StateFlow<Boolean?> = combine(
        _isForYouEnabledFromSettings,
        _isForYouEnabledFromFeed,
    ) { fromSettings, fromFeed ->
        if (fromSettings == null) null else fromSettings && fromFeed
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // MUST stay above init{}: Kotlin runs property initialisers and init blocks in declaration
    // order, and init calls loadGlobalPinnedPosts(), which reads _globalPinnedPostsState. Declared
    // below, the backing field is still null when init runs and the page crashes on open with an
    // NPE. The compiler does not catch it because the access goes through a function call.
    private var globalPinnedPostsJob: Job? = null

    private val _globalPinnedPostsState = MutableStateFlow<GlobalPinnedPostsState>(
        GlobalPinnedPostsState.Loading(),
    )
    val globalPinnedPostsState: StateFlow<GlobalPinnedPostsState> =
        _globalPinnedPostsState.asStateFlow()

    val globalPinnedPosts: StateFlow<List<AmityPinnedPost>> = globalPinnedPostsState
        .map { it.posts }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        fetchForYouFeedSetting()
        loadGlobalPinnedPosts(isRefresh = false)
    }

    override fun onCleared() {
        // Locally created posts are a hand-off from the composer to THIS feed page. Once the page
        // is gone they have no owner, so they must not survive into the next visit.
        AmityPostComposerHelper.detachFeed(feedOwnerId)
        super.onCleared()
    }

    fun clearCreatedPostsForRefresh() {
        AmityPostComposerHelper.clearForRefresh(feedOwnerId)
    }

    private fun fetchForYouFeedSetting() {
        if (AmityCoreClient.getCurrentUserType() != AmityUserType.SIGNED_IN) {
            _isForYouEnabledFromSettings.value = false
            return
        }
        addDisposable(
            AmityCoreClient.getForYouFeedSetting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { setting -> _isForYouEnabledFromSettings.value = setting.enabled },
                    { _isForYouEnabledFromSettings.value = false }
                )
        )
    }

    fun onForYouFeatureDisabled() {
        _isForYouEnabledFromFeed.value = false
    }

    private val _notificationTraySeen = MutableStateFlow<AmityNotificationTraySeen?>(null)
    val notificationTraySeen: StateFlow<AmityNotificationTraySeen?> get() = _notificationTraySeen.asStateFlow()

    private var notificationTraySeenJob: kotlinx.coroutines.Job? = null

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

    fun getMyInvitations(): Flow<PagingData<AmityInvitation>> {
        return AmityCoreClient.newInvitationRepository()
            .getMyCommunityInvitations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getMyCommunities(): Flow<PagingData<AmityCommunity>> =
        AmityUIKitDataGate.paging(AmityUIKitFeature.COMMUNITY) {
        AmitySocialClient.newCommunityRepository()
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

    fun getForYouFeed(): Flow<PagingData<AmityListItem>> =
        AmityUIKitDataGate.paging(AmityUIKitFeature.FEED) {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = null,
        )
        AmitySocialClient.newFeedRepository()
            .getForYouFeed()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .map { injector.inject(it) }
            .asFlow()
            .catch {}
            .cachedIn(viewModelScope)
    }

    fun getGlobalFeed(): Flow<PagingData<AmityListItem>> =
        AmityUIKitDataGate.paging(AmityUIKitFeature.FEED) {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = null,
        )

        AmitySocialClient.newFeedRepository()
            .getGlobalFeed()
            // No dataTypes here on purpose. /api/v4/me/global-feeds takes a
            // media-type filter, not a post-type filter: naming the full list
            // returns 422, and the list cannot name TEXT, so there is no way to
            // say "everything except clip" without dropping every text post.
            .build()
            .query()
            .dropGatedPostTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .map { injector.inject(it) }
            .asFlow()
            .catch {}
    }

    private fun queryGlobalPinnedPosts(): Flow<List<AmityPinnedPost>> {
        return AmitySocialClient.newPostRepository()
            .getGlobalPinnedPosts()
            .dropGatedPinnedPosts()
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getGlobalPinnedPosts(): Flow<List<AmityPinnedPost>> {
        return queryGlobalPinnedPosts()
            .catch {}
    }

    private fun loadGlobalPinnedPosts(isRefresh: Boolean) {
        globalPinnedPostsJob?.cancel()
        val previousPosts = _globalPinnedPostsState.value.posts
        _globalPinnedPostsState.value = GlobalPinnedPostsState.Loading(
            posts = previousPosts,
            isRefresh = isRefresh,
        )
        globalPinnedPostsJob = viewModelScope.launch {
            queryGlobalPinnedPosts()
                .catch { error ->
                    _globalPinnedPostsState.value = GlobalPinnedPostsState.Error(
                        posts = previousPosts,
                        error = error,
                    )
                }
                .collectLatest { posts ->
                    _globalPinnedPostsState.value = GlobalPinnedPostsState.Success(posts)
                }
        }
    }

    fun scheduleNotificationTraySeen() {
        // The tray belongs to Feed. The bell disappears with the module, but this
        // poll is started by the page and would otherwise keep asking the server
        // once a minute for a badge nothing can draw.
        if (!AmityUIKitDataGate.isOn(AmityUIKitFeature.FEED)) return
        viewModelScope.launch {
            while (true) {
                getNotificationTraySeen()
                delay(61000)
            }
        }
    }

    private fun getNotificationTraySeen() {
        notificationTraySeenJob?.cancel()
        notificationTraySeenJob = viewModelScope.launch {
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
        loadGlobalPinnedPosts(isRefresh = true)
    }

    enum class AuxiliaryContentState {
        LOADING,
        READY,
        ERROR,
    }

    sealed class GlobalPinnedPostsState {
        abstract val posts: List<AmityPinnedPost>
        abstract val contentState: AuxiliaryContentState

        data class Loading(
            override val posts: List<AmityPinnedPost> = emptyList(),
            val isRefresh: Boolean = false,
        ) : GlobalPinnedPostsState() {
            override val contentState = AuxiliaryContentState.LOADING
        }

        data class Success(
            override val posts: List<AmityPinnedPost>,
        ) : GlobalPinnedPostsState() {
            override val contentState = AuxiliaryContentState.READY
        }

        data class Error(
            override val posts: List<AmityPinnedPost>,
            val error: Throwable,
        ) : GlobalPinnedPostsState() {
            override val contentState = AuxiliaryContentState.ERROR
        }
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
