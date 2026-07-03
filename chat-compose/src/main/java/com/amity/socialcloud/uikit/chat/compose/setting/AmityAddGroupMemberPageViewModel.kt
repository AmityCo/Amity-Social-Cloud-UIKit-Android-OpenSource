package com.amity.socialcloud.uikit.chat.compose.setting

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
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class AmityAddGroupMemberPageViewModel : AmityBaseViewModel() {

    private val _selectedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedUserIds: StateFlow<Set<String>> = _selectedUserIds

    private val _selectedUsers = MutableStateFlow<List<AmityUser>>(emptyList())
    val selectedUsers: StateFlow<List<AmityUser>> = _selectedUsers

    fun searchUsers(keyword: String): Flow<PagingData<AmityUser>> {
        return if (keyword.isEmpty() || keyword.length >= 1) {
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

    fun toggleUser(user: AmityUser) {
        val userId = user.getUserId()
        val current = _selectedUserIds.value.toMutableSet()
        val currentUsers = _selectedUsers.value.toMutableList()
        if (current.contains(userId)) {
            current.remove(userId)
            currentUsers.removeAll { it.getUserId() == userId }
        } else {
            current.add(userId)
            currentUsers.add(user)
        }
        _selectedUserIds.value = current
        _selectedUsers.value = currentUsers
    }

    fun removeUser(userId: String) {
        val current = _selectedUserIds.value.toMutableSet()
        val currentUsers = _selectedUsers.value.toMutableList()
        current.remove(userId)
        currentUsers.removeAll { it.getUserId() == userId }
        _selectedUserIds.value = current
        _selectedUsers.value = currentUsers
    }
}
