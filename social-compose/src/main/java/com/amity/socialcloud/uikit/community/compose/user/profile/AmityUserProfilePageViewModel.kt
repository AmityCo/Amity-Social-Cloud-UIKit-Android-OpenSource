package com.amity.socialcloud.uikit.community.compose.user.profile

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AmityUserProfilePageViewModel(val userId: String) : AmityBaseViewModel() {

    private val _userProfileState by lazy {
        MutableStateFlow(UserProfileState(userId))
    }
    val userProfileState get() = _userProfileState

    init {
        refresh()
    }

    fun refresh() {
        compositeDisposable.clear()
        _userProfileState.value = _userProfileState.value.copy(
            isRefreshing = true,
        )

        if (userId == AmityCoreClient.getUserId()) {
            Flowable.combineLatest(
                AmityCoreClient.newUserRepository().getUser(userId),
                getMyFollowInfo(),
            ) { user, myFollowInfo ->
                _userProfileState.value = _userProfileState.value.copy(
                    user = user,
                    myFollowInfo = myFollowInfo,
                    userFollowInfo = null,
                    isRefreshing = false,
                )
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .let(compositeDisposable::add)
        } else {
            Flowable.combineLatest(
                AmityCoreClient.newUserRepository().getUser(userId),
                getMyFollowInfo(),
                getUserFollowInfo(),
            ) { user, myFollowInfo, userFollowInfo ->
                _userProfileState.value = _userProfileState.value.copy(
                    user = user,
                    myFollowInfo = myFollowInfo,
                    userFollowInfo = userFollowInfo,
                    isRefreshing = false,
                )
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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