package com.amity.socialcloud.uikit.chat.compose.home

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelFilter
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationModule
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

class AmityChatHomePageViewModel : AmityBaseViewModel() {

    private val _channelListState by lazy {
        MutableStateFlow<ChannelListState>(ChannelListState.LOADING)
    }
    val channelListState get() = _channelListState

    private val _otherMembers = MutableStateFlow<Map<String, AmityChannelMember?>>(emptyMap())
    val otherMembers: StateFlow<Map<String, AmityChannelMember?>> = _otherMembers

    private val _isChatNotificationEnabled = MutableStateFlow(true)
    val isChatNotificationEnabled: StateFlow<Boolean> = _isChatNotificationEnabled

    init {
        loadChatNotificationSettings()
    }

    private fun loadChatNotificationSettings() {
        addDisposable(
            AmityCoreClient.notifications().user()
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { settings ->
                    val userLevelEnabled = settings.isEnabled()
                    val chatModule = settings.getModules()
                        ?.filterIsInstance<AmityUserNotificationModule.CHAT>()
                        ?.firstOrNull()
                    _isChatNotificationEnabled.value = userLevelEnabled && (chatModule?.isEnabled() ?: true)
                }
                .subscribe({}, {})
        )
    }

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

    fun setChannelListState(state: ChannelListState) {
        _channelListState.value = state
    }

    fun getNetworkConnectionStateFlow(): Flow<NetworkConnectionEvent> {
        return NetworkConnectionEventBus.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    val allChannels: Flow<PagingData<AmityChannel>> by lazy {
        getChannels(
            types = listOf(
                AmityChannel.Type.CONVERSATION,
                AmityChannel.Type.COMMUNITY,
            )
        ).cachedIn(viewModelScope)
    }

    val conversationChannels: Flow<PagingData<AmityChannel>> by lazy {
        getChannels(
            types = listOf(AmityChannel.Type.CONVERSATION)
        ).cachedIn(viewModelScope)
    }

    val groupChannels: Flow<PagingData<AmityChannel>> by lazy {
        getChannels(
            types = listOf(AmityChannel.Type.COMMUNITY)
        ).cachedIn(viewModelScope)
    }

    private fun getChannels(
        types: List<AmityChannel.Type>,
    ): Flow<PagingData<AmityChannel>> {
        return AmityChatClient.newChannelRepository()
            .getChannels()
            .types(types)
            .filter(AmityChannelFilter.MEMBER)
            .includeDeleted(false)
            .excludeArchived(true)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch { }
    }

    fun archiveChannel(
        channelId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newChannelRepository()
            .archiveChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    sealed class ChannelListState {
        object LOADING : ChannelListState()
        object SUCCESS : ChannelListState()
        object EMPTY : ChannelListState()
        object ERROR : ChannelListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): ChannelListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else if (itemCount > 0) {
                    SUCCESS
                } else {
                    LOADING
                }
            }
        }
    }
}
