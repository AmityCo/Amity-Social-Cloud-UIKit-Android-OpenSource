package com.amity.socialcloud.uikit.community.compose.community.pending.pending_invite

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AmityCommunityPendingInviteMemberPageViewModel : AmityBaseViewModel() {

    private val _selectedItemStates by lazy {
        MutableStateFlow<MutableList<AmityCommunityPendingInviteItemState>>(mutableListOf())
    }
    val selectedItemStates get() = _selectedItemStates

    private val _userListState by lazy {
        MutableStateFlow<UserListState>(UserListState.SUCCESS)
    }

    val userListState get() = _userListState

    fun setUserListState(state: UserListState) {
        _userListState.value = state
    }

    fun getInvitations(community: AmityCommunity): Flow<PagingData<AmityInvitation>> {
        return community
            .getMemberInvitations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
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

data class AmityCommunityPendingInviteItemState(
    val userId: String,
)