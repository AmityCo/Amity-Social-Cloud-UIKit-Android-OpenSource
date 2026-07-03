package com.amity.socialcloud.uikit.chat.compose.create

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.compose.create.AmityChannelCreateConversationPageViewModel.UserListState
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class AmitySelectGroupMemberPageViewModel : AmityBaseViewModel() {

    private val _selectedUsers = MutableStateFlow<List<AmityUser>>(emptyList())
    val selectedUsers: StateFlow<List<AmityUser>> = _selectedUsers

    fun searchUsers(keyword: String): Flow<PagingData<AmityUser>> {
        val currentUserId = AmityCoreClient.getUserId()
        return AmityCoreClient.newUserRepository()
            .searchUsers(keyword)
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throttleLatest(300, TimeUnit.MILLISECONDS)
            .asFlow()
            .map { pagingData -> pagingData.filter { it.getUserId() != currentUserId } }
            .cachedIn(viewModelScope)
            .catch { }
    }

    fun toggleUserSelection(user: AmityUser) {
        val current = _selectedUsers.value.toMutableList()
        val existing = current.find { it.getUserId() == user.getUserId() }
        if (existing != null) {
            current.remove(existing)
        } else {
            current.add(user)
        }
        _selectedUsers.value = current
    }

    fun removeUser(userId: String) {
        _selectedUsers.value = _selectedUsers.value.filter { it.getUserId() != userId }
    }

    fun isSelected(userId: String): Boolean {
        return _selectedUsers.value.any { it.getUserId() == userId }
    }

    sealed class UserListState {
        object LOADING : UserListState()
        object SHORT_INPUT : UserListState()
        object EMPTY : UserListState()
        object SUCCESS : UserListState()

        companion object {
            fun from(loadState: LoadState, itemCount: Int, keywordLength: Int, minKeywordLength: Int): UserListState {
                return when {
                    keywordLength in 1 until minKeywordLength -> SHORT_INPUT
                    loadState is LoadState.Loading && itemCount == 0 -> LOADING
                    loadState is LoadState.NotLoading && itemCount == 0 -> EMPTY
                    else -> SUCCESS
                }
            }
        }
    }
}
