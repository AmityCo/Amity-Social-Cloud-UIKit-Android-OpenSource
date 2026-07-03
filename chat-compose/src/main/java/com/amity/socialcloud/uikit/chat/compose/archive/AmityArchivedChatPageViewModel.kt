package com.amity.socialcloud.uikit.chat.compose.archive

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class AmityArchivedChatPageViewModel : AmityBaseViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _otherMembers = MutableStateFlow<Map<String, AmityChannelMember?>>(emptyMap())
    val otherMembers: StateFlow<Map<String, AmityChannelMember?>> = _otherMembers

    fun fetchOtherMember(channelId: String) {
        if (_otherMembers.value.containsKey(channelId)) return
        AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembersFromCache()
            .take(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { members: List<AmityChannelMember> ->
                val other = members.firstOrNull { it.getUserId() != AmityCoreClient.getUserId() }
                _otherMembers.value = _otherMembers.value + (channelId to other)
            }
            .subscribe()
    }

    fun unarchiveChannel(
        channelId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newChannelRepository()
            .unarchiveChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    val archivedChannels: Flow<PagingData<AmityChannel>> by lazy {
        AmityChatClient.newChannelRepository()
            .getArchivedChannels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .onEach { list ->
                _isLoading.value = false
            }
            .catch { e ->
                _isLoading.value = false
                emit(emptyList())
            }
            .map { list ->
                if (list.isEmpty()) PagingData.empty()
                else PagingData.from(list)
            }
            .cachedIn(viewModelScope)
    }
}
