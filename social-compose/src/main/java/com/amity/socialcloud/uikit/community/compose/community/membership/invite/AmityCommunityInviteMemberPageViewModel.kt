package com.amity.socialcloud.uikit.community.compose.community.membership.invite

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityCommunityInviteMemberPageViewModel : AmityBaseViewModel() {

    private val _selectedItemStates by lazy {
        MutableStateFlow<MutableList<AmityCommunityMemberInviteItemState>>(mutableListOf())
    }
    val selectedItemStates get() = _selectedItemStates

    val minKeywordLength = 3

    private val _userListState by lazy {
        MutableStateFlow<UserListState>(UserListState.SUCCESS)
    }

    val userListState get() = _userListState

    fun setUserListState(state: UserListState) {
        _userListState.value = state
    }

    fun updateSelectedItemState(userId: String, isSelected: Boolean) {
        viewModelScope.launch {
            if (_selectedItemStates.value.find { it.userId == userId } == null) {
                _selectedItemStates.value.add(
                    AmityCommunityMemberInviteItemState(userId, isSelected)
                )
            } else {
                _selectedItemStates.value = _selectedItemStates.value.map {
                    if (it.userId == userId) {
                        it.copy(isSelected = isSelected)
                    } else {
                        it
                    }
                }.toMutableList()
            }
        }
    }

    fun searchUsers(keyword: String): Flow<PagingData<AmityUser>> {
        return if (keyword.isEmpty() || keyword.length >= minKeywordLength) {
             AmityCoreClient.newUserRepository()
                .searchUsers(keyword)
                .sortBy(AmityUserSortOption.DISPLAYNAME)
                .build()
                .query()
                .throttleLatest(1, TimeUnit.SECONDS, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
        } else {
            Flowable.just(PagingData.empty<AmityUser>()).asFlow()
        }
    }

    sealed class UserListState {
        object INIT : UserListState()
        object LOADING : UserListState()
        object EMPTY : UserListState()
        object SUCCESS : UserListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): UserListState {
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

data class AmityCommunityMemberInviteItemState(
    val userId: String,
    var isSelected: Boolean = false,
)