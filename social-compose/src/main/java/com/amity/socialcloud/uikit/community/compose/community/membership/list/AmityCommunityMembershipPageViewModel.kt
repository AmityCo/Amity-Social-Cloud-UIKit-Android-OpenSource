package com.amity.socialcloud.uikit.community.compose.community.membership.list

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.member.query.AmityCommunityMembershipSortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembershipFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityCommunityMembershipPageViewModel(val communityId: String) : AmityBaseViewModel() {

    private val _memberListState by lazy {
        MutableStateFlow<MembershipListState>(MembershipListState.SUCCESS)
    }

    val memberListState get() = _memberListState

    private val _moderatorListState by lazy {
        MutableStateFlow<MembershipListState>(MembershipListState.SUCCESS)
    }

    val moderatorListState get() = _moderatorListState

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityCommunityMembershipSheetUIState>(AmityCommunityMembershipSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    private val _triggerPagingRefresh by lazy {
        MutableStateFlow(false)
    }
    val triggerPagingRefresh get() = _triggerPagingRefresh

    fun setMemberListState(state: MembershipListState) {
        _memberListState.value = state
    }

    fun setModeratorListState(state: MembershipListState) {
        _memberListState.value = state
    }

    fun searchMembers(keyword: String): Flow<PagingData<AmityCommunityMember>> {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .searchMembers(keyword)
            .sortBy(AmityCommunityMembershipSortOption.DISPLAY_NAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getModerators(): Flow<PagingData<AmityCommunityMember>> {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .getMembers()
            .filter(AmityCommunityMembershipFilter.MEMBER)
            .sortBy(AmityCommunityMembershipSortOption.DISPLAY_NAME)
            .roles(
                listOf(
                    AmityConstants.CHANNEL_MODERATOR_ROLE,
                    AmityConstants.COMMUNITY_MODERATOR_ROLE
                )
            )
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { }
            .asFlow()
    }

    fun hasEditPermission(): Flowable<Boolean> {
        return hasPermissionAtCommunity(AmityPermission.EDIT_COMMUNITY_USER, communityId)
    }

    fun hasRemovePermission(): Flowable<Boolean> {
        return hasPermissionAtCommunity(AmityPermission.REMOVE_COMMUNITY_USER, communityId)
    }

    fun updateSheetUIState(uiState: AmityCommunityMembershipSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = uiState
        }
    }

    fun flagUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .flagUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
                triggerPagingRefresh()
            }
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    fun unflagUser(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmityCoreClient.newUserRepository()
            .unflagUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
                triggerPagingRefresh()
            }
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

    fun promoteModerator(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .moderation(communityId)
            .addRoles(
                roles = listOf(
                    AmityConstants.CHANNEL_MODERATOR_ROLE,
                    AmityConstants.COMMUNITY_MODERATOR_ROLE
                ),
                userIds = listOf(userId)
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
            }
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

    fun demoteModerator(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .moderation(communityId)
            .removeRoles(
                roles = listOf(
                    AmityConstants.CHANNEL_MODERATOR_ROLE,
                    AmityConstants.COMMUNITY_MODERATOR_ROLE
                ),
                userIds = listOf(userId)
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
            }
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

    fun removeMember(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .removeMembers(listOf(userId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
                triggerPagingRefresh()
            }
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

    fun addMembers(
        userIds: List<String>,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit
    ) {
        AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .addMembers(userIds)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
                triggerPagingRefresh()
            }
            .doOnError {
                onError(AmityError.from(it))
            }
            .subscribe()
    }

    private fun triggerPagingRefresh() {
        viewModelScope.launch {
            delay(100)
            _triggerPagingRefresh.value = true
            delay(200)
            _triggerPagingRefresh.value = false
        }
    }

    sealed class MembershipListState {
        object LOADING : MembershipListState()
        object EMPTY : MembershipListState()
        object SUCCESS : MembershipListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): MembershipListState {
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

sealed class AmityCommunityMembershipSheetUIState {
    data class OpenSheet(val member: AmityCommunityMember) : AmityCommunityMembershipSheetUIState()

    object CloseSheet : AmityCommunityMembershipSheetUIState()
}
