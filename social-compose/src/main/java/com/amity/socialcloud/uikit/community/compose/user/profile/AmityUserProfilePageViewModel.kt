package com.amity.socialcloud.uikit.community.compose.user.profile

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.follow.AmityMyFollowInfo
import com.amity.socialcloud.sdk.model.core.follow.AmityUserFollowInfo
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AmityUserProfilePageViewModel(val userId: String) : AmityBaseViewModel() {

    private val _userProfileState by lazy {
        MutableStateFlow(UserProfileState(userId))
    }
    val userProfileState get() = _userProfileState

    private val _postListState by lazy {
        MutableStateFlow<PostListState>(PostListState.EMPTY)
    }
    val postListState get() = _postListState

    private val _imagePostListState by lazy {
        MutableStateFlow<PostListState>(PostListState.EMPTY)
    }
    val imagePostListState get() = _imagePostListState

    private val _videoPostListState by lazy {
        MutableStateFlow<PostListState>(PostListState.EMPTY)
    }
    val videoPostListState get() = _videoPostListState

    fun setPostListState(state: PostListState) {
        _postListState.value = state
    }

    fun setImagePostListState(state: PostListState) {
        _imagePostListState.value = state
    }

    fun setVideoPostListState(state: PostListState) {
        _videoPostListState.value = state
    }

    private val fetchUserError = MutableStateFlow<Throwable?>(null)

    fun getFetchErrorState() = fetchUserError.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        compositeDisposable.clear()
        viewModelScope.launch {
            _userProfileState.value = _userProfileState.value.copy(
                isRefreshing = true,
            )
        }
        fetchUserError.value = null

        if (userId == AmityCoreClient.getUserId()) {
            AmityCoreClient.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModelScope.launch {
                        delay(100)
                        _userProfileState.value = _userProfileState.value.copy(
                            user = it,
                            userFollowInfo = null,
                            isRefreshing = false,
                        )
                    }
                }
                .doOnError {
                    viewModelScope.launch {
                        fetchUserError.emit(it)
                    }
                }
                .subscribe()
                .let(compositeDisposable::add)

            getMyFollowInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModelScope.launch {
                        delay(100)
                        _userProfileState.value = _userProfileState.value.copy(
                            myFollowInfo = it,
                            userFollowInfo = null,
                        )
                    }
                }
                .doOnError {
                    // ignore error
                }
                .subscribe()
                .let(compositeDisposable::add)
        } else {
            AmityCoreClient.newUserRepository().getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModelScope.launch {
                        delay(100)
                        _userProfileState.value = _userProfileState.value.copy(
                            user = it,
                            isRefreshing = false,
                        )
                    }
                }
                .doOnError {
                    viewModelScope.launch {
                        fetchUserError.emit(it)
                    }
                }
                .subscribe()
                .let(compositeDisposable::add)

            getMyFollowInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModelScope.launch {
                        delay(100)
                        _userProfileState.value = _userProfileState.value.copy(
                            myFollowInfo = it,
                        )
                    }
                }
                .doOnError {
                    // ignore error
                }
                .subscribe()
                .let(compositeDisposable::add)

            getUserFollowInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModelScope.launch {
                        delay(100)
                        _userProfileState.value = _userProfileState.value.copy(
                            userFollowInfo = it,
                        )
                    }
                }
                .doOnError {
                    // ignore error
                }
                .subscribe()
                .let(compositeDisposable::add)
        }
    }

    fun getUserPosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newFeedRepository()
            .getUserFeed(userId)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getUserImagePosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetUser(userId)
            .types(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.IMAGE.getApiKey())))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun getUserVideoPosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newPostRepository()
            .getPosts()
            .targetUser(userId)
            .types(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.VIDEO.getApiKey())))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    fun followUser(
        targetUserId: String,
        onSuccess: () -> Unit = {},
        onError: (AmityError) -> Unit = {},
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .follow(targetUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElement()
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()

        viewModelScope.launch {
            delay(300)
            refresh()
        }
    }

    fun unfollowUser(
        targetUserId: String,
        onSuccess: () -> Unit = {},
        onError: (AmityError) -> Unit = {},
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .unfollow(targetUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()

        viewModelScope.launch {
            delay(300)
            refresh()
        }
    }

    fun blockUser(
        targetUserId: String,
        onSuccess: () -> Unit = {},
        onError: (AmityError) -> Unit = {},
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .blockUser(targetUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()

        viewModelScope.launch {
            delay(300)
            refresh()
        }
    }

    fun unblockUser(
        targetUserId: String,
        onSuccess: () -> Unit = {},
        onError: (AmityError) -> Unit = {},
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .unblockUser(targetUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()

        viewModelScope.launch {
            delay(300)
            refresh()
        }
    }

    private fun getMyFollowInfo(): Flowable<AmityMyFollowInfo> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .getMyFollowInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getUserFollowInfo(): Flowable<AmityUserFollowInfo> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .getFollowInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
}

data class UserProfileState(
    val userId: String,
    val user: AmityUser? = null,
    val myFollowInfo: AmityMyFollowInfo? = null,
    val userFollowInfo: AmityUserFollowInfo? = null,
    val isRefreshing: Boolean = true,
    val isPrivateProfile: Boolean = false,
) {
    fun isMyUserProfile(): Boolean {
        return userId == AmityCoreClient.getUserId()
    }
}