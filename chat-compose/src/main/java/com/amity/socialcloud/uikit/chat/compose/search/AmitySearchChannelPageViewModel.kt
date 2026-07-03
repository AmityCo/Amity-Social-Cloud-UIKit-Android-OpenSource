package com.amity.socialcloud.uikit.chat.compose.search

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SearchTab { CHATS, MESSAGES }

data class SearchChannelState(
    val channels: List<AmityChannel> = emptyList(),
    val messages: List<AmityMessage> = emptyList(),
    val indexToMessageMap: Map<Int, AmityMessage> = emptyMap(),
    val channelMembers: Map<String, AmityChannelMember?> = emptyMap(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val query: String = "",
    val lastValidSearchText: String = "",
    val archivedChannelIds: List<String> = emptyList(),
    val activeTab: SearchTab = SearchTab.CHATS,
    val nextPageToken: String? = null,
    val hasNextPage: Boolean = false,
) {
    fun isChannelArchived(channelId: String) = archivedChannelIds.contains(channelId)
    fun getSearchMessageForIndex(index: Int) = indexToMessageMap[index]
}

class AmitySearchChannelPageViewModel : AmityBaseViewModel() {

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _searchState = MutableStateFlow(SearchChannelState())
    val searchState: StateFlow<SearchChannelState> = _searchState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchArchivedChannelIds()
    }

    fun onSearchKeywordChanged(keyword: String) {
        _searchKeyword.value = keyword
        searchJob?.cancel()
        if (keyword.trim().length < 3) {
            _searchState.update { it.copy(query = keyword) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            searchChannels(keyword.trim())
        }
    }

    fun changeTab(tab: SearchTab) {
        if (_searchState.value.activeTab == tab) return
        _searchState.update {
            it.copy(
                activeTab = tab,
                channels = emptyList(),
                messages = emptyList(),
                indexToMessageMap = emptyMap(),
                isLoading = false,
                isLoadingMore = false,
                nextPageToken = null,
                hasNextPage = false,
            )
        }
        val currentQuery = _searchState.value.query.trim()
        if (currentQuery.length >= 3) {
            viewModelScope.launch { searchChannels(currentQuery) }
        }
    }

    fun loadMore() {
        val state = _searchState.value
        if (state.isLoadingMore || state.isLoading || !state.hasNextPage) return
        val token = state.nextPageToken ?: return
        _searchState.update { it.copy(isLoadingMore = true) }
        val query = state.lastValidSearchText
        if (state.activeTab == SearchTab.CHATS) {
            loadMoreChannels(query, token)
        } else {
            loadMoreMessages(query, token)
        }
    }

    fun archiveChannel(channelId: String,
                       onSuccess: () -> Unit = {},
                       onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newChannelRepository()
            .archiveChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
                markChannelArchived(channelId)
            }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun unarchiveChannel(channelId: String,
                         onSuccess: () -> Unit = {},
                         onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            AmityChatClient.newChannelRepository()
                .unarchiveChannel(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess()
                    markChannelUnarchived(channelId)
                }, {
                    onError(it)
                })
        }
    }

    fun markChannelArchived(channelId: String) {
        _searchState.update {
            it.copy(archivedChannelIds = it.archivedChannelIds + channelId)
        }
    }

    fun markChannelUnarchived(channelId: String) {
        _searchState.update {
            it.copy(archivedChannelIds = it.archivedChannelIds - channelId)
        }
    }

    private fun fetchArchivedChannelIds() {
        AmityChatClient.newChannelRepository()
            .getArchivedChannelIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ids ->
                _searchState.update { it.copy(archivedChannelIds = ids) }
            }, {})
    }

    private fun searchChannels(query: String) {
        _searchState.update {
            it.copy(
                isLoading = true,
                query = query,
                lastValidSearchText = query,
                channels = emptyList(),
                messages = emptyList(),
                indexToMessageMap = emptyMap(),
                nextPageToken = null,
                hasNextPage = false,
            )
        }
        if (_searchState.value.activeTab == SearchTab.CHATS) {
            searchChatChannels(query, token = null, append = false)
        } else {
            searchMessages(query, token = null, append = false)
        }
    }

    private fun searchChatChannels(query: String, token: String?, append: Boolean) {
        AmityChatClient.newChannelRepository()
            .searchChannels()
            .withQuery(query)
            .memberOnly(true)
            .types(listOf(AmityChannel.Type.CONVERSATION, AmityChannel.Type.COMMUNITY))
            .sortBy("lastActivity")
            .orderBy("desc")
            .limit(20)
            .also { if (token != null) it.token(token) }
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ channels ->
                val nextToken = if (channels.size >= PAGE_SIZE) deriveNextToken(token, channels.size) else null
                _searchState.update { state ->
                    val updatedChannels = if (append) state.channels + channels else channels
                    state.copy(
                        channels = updatedChannels,
                        isLoading = false,
                        isLoadingMore = false,
                        nextPageToken = nextToken,
                        hasNextPage = nextToken != null,
                    )
                }
                // The search response persists its `channelUsers` before emitting
                // (ChannelQueryPersister), so members are readable from cache here.
                fetchChannelMembers(channels)
            }, {
                _searchState.update { it.copy(isLoading = false, isLoadingMore = false) }
            })
    }

    private fun searchMessages(query: String, token: String?, append: Boolean) {
        AmityChatClient.newMessageRepository()
            .searchMessages(query)
            .also { if (token != null) it.token(token) }
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ messages ->
                val nextToken = if (messages.size >= PAGE_SIZE) deriveNextToken(token, messages.size) else null
                val filteredMessages = messages.filter { message ->
                    val text = (message.getData() as? AmityMessage.Data.TEXT)?.getText()
                        ?: return@filter false
                    text.contains(query, ignoreCase = true)
                }
                val offset = if (append) _searchState.value.messages.size else 0
                val indexToMessageMap = filteredMessages.mapIndexed { i, msg -> (i + offset) to msg }.toMap()
                val orderedChannels = filteredMessages.mapNotNull { it.getChannel() }
                val currentUserId = AmityCoreClient.getUserId()
                val memberMap = filteredMessages
                    .filter { it.getChannel()?.getChannelType() == AmityChannel.Type.CONVERSATION }
                    .associate { msg ->
                        val otherMember = msg.getChannelMembers()
                            .firstOrNull { it.getUserId() != currentUserId }
                        msg.getChannelId() to otherMember
                    }

                _searchState.update { state ->
                    val updatedMessages = if (append) state.messages + filteredMessages else filteredMessages
                    val updatedMap = if (append) state.indexToMessageMap + indexToMessageMap else indexToMessageMap
                    val updatedChannels = if (append) state.channels + orderedChannels else orderedChannels
                    val updatedMembers = if (append) state.channelMembers + memberMap else memberMap
                    state.copy(
                        messages = updatedMessages,
                        indexToMessageMap = updatedMap,
                        channels = updatedChannels,
                        channelMembers = updatedMembers,
                        isLoading = false,
                        isLoadingMore = false,
                        nextPageToken = nextToken,
                        hasNextPage = nextToken != null,
                    )
                }
            }, {
                _searchState.update { it.copy(isLoading = false, isLoadingMore = false) }
            })
    }

    private fun loadMoreChannels(query: String, token: String) {
        searchChatChannels(query, token = token, append = true)
    }

    private fun loadMoreMessages(query: String, token: String) {
        searchMessages(query, token = token, append = true)
    }

    private fun fetchChannelMembers(channels: List<AmityChannel>) {
        val currentUserId = AmityCoreClient.getUserId()
        channels
            .filter { it.getChannelType() == AmityChannel.Type.CONVERSATION }
            .forEach { channel ->
                AmityChatClient.newChannelRepository()
                    .membership(channel.getChannelId())
                    .getMembersFromCache()
                    .firstElement()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ members ->
                        val otherMember = members.firstOrNull { it.getUserId() != currentUserId }
                        _searchState.update { state ->
                            state.copy(
                                channelMembers = state.channelMembers + (channel.getChannelId() to otherMember)
                            )
                        }
                    }, {
                        _searchState.update { state ->
                            state.copy(
                                channelMembers = state.channelMembers + (channel.getChannelId() to null)
                            )
                        }
                    })
            }
    }

    // The SDK Single returns all results up to `limit`; we use a fixed page size and
    // derive pagination by checking if a full page was returned.
    private fun deriveNextToken(currentToken: String?, resultSize: Int): String? {
        return if (resultSize >= PAGE_SIZE) {
            // Use offset-based token: encode the next offset as a string
            val currentOffset = currentToken?.toIntOrNull() ?: 0
            (currentOffset + PAGE_SIZE).toString()
        } else null
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
