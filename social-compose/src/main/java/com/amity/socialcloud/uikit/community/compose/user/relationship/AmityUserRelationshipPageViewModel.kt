package com.amity.socialcloud.uikit.community.compose.user.relationship

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowRelationship
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityUserRelationshipPageViewModel(val userId: String) : AmityBaseViewModel() {

    private val _followingListState by lazy {
        MutableStateFlow<FollowListState>(FollowListState.SUCCESS)
    }
    val followingListState get() = _followingListState

    private val _followerListState by lazy {
        MutableStateFlow<FollowListState>(FollowListState.SUCCESS)
    }
    val followerListState get() = _followerListState

    fun setFollowingListState(state: FollowListState) {
        _followingListState.value = state
    }

    fun setFollowerListState(state: FollowListState) {
        _followerListState.value = state
    }


    fun getUser(): Flow<AmityUser> {
        return AmityCoreClient.newUserRepository()
            .getUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getUserFollowings(): Flow<PagingData<AmityFollowRelationship>> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .getFollowings(userId)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun getUserFollowers(): Flow<PagingData<AmityFollowRelationship>> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .getFollowers(userId)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun reportUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .flagUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    fun unreportUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .unflagUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    fun blockUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .blockUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    sealed class FollowListState {
        object LOADING : FollowListState()
        object EMPTY : FollowListState()
        object SUCCESS : FollowListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): FollowListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    EMPTY
                } else {
                    SUCCESS
                }
            }
        }
    }
}