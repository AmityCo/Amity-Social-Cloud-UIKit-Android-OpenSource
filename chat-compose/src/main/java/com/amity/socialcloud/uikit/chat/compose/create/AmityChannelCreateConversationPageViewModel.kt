package com.amity.socialcloud.uikit.chat.compose.create

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class AmityChannelCreateConversationPageViewModel : AmityBaseViewModel() {

    val minKeywordLength = 3

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword

    private val _creationState = MutableStateFlow<CreationState>(CreationState.Idle)
    val creationState: StateFlow<CreationState> = _creationState

    fun setKeyword(text: String) {
        _keyword.value = text
    }

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

    fun createConversation(
        userId: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit = {},
    ) {
        _creationState.value = CreationState.Loading
        AmityChatClient.newChannelRepository()
            .createChannel(userId)
            .conversation(userId = userId)
            .build()
            .create()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { channel ->
                _creationState.value = CreationState.Success(channel.getChannelId())
                onSuccess(channel.getChannelId())
            }
            .doOnError { error ->
                _creationState.value = CreationState.Error(error)
                onError(error)
            }
            .subscribe()
    }

    sealed class CreationState {
        object Idle : CreationState()
        object Loading : CreationState()
        data class Success(val channelId: String) : CreationState()
        data class Error(val throwable: Throwable) : CreationState()
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
