package com.amity.socialcloud.uikit.community.compose.user.blocked

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityBlockedUsersPageViewModel : AmityBaseViewModel() {

    private val _blockedListState by lazy {
        MutableStateFlow<BlockedListState>(BlockedListState.SUCCESS)
    }
    val blockedListState get() = _blockedListState

    fun setBlockedListState(state: BlockedListState) {
        _blockedListState.value = state
    }

    fun getBlockedUsers(): Flow<PagingData<AmityUser>> {
        return AmityCoreClient.newUserRepository()
            .getBlockedUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun unblockUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .unblockUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    sealed class BlockedListState {
        object LOADING : BlockedListState()
        object EMPTY : BlockedListState()
        object SUCCESS : BlockedListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): BlockedListState {
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