package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AmityLivestreamChatViewModel constructor(private val channelId: String) : ViewModel() {

    var showDeleteDialog = mutableStateOf(false)
    var targetDeletedMessage = mutableStateOf<AmityMessage?>(null)

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityLiveStreamSheetUIState>(AmityLiveStreamSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    fun createMessage(
        text: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AmityChatClient.newMessageRepository()
            .createTextMessage(
                channelId,
                text
            )
            .build()
            .send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError(it)
            }
            .doOnComplete {
                onSuccess()
            }
            .subscribe()
    }

    fun getMessageList(
        onError: (Throwable) -> Unit = {},
    ): Flow<PagingData<AmityMessage>> {
        return AmityChatClient.newMessageRepository()
            .getMessages(channelId)
            .sortBy(AmityMessageQuerySortOption.LAST_CREATED)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError(it)
            }
            .asFlow()
    }

    fun getChannelFlow(): Flow<AmityChannel> {
        return AmityChatClient
            .newChannelRepository()
            .getChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {

            }
    }

    fun isChannelModerator(): Flow<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.MUTE_CHANNEL)
            .atChannel(channelId)
            .check()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch {

            }
    }

    fun deleteMessage(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        targetDeletedMessage.value?.let { message ->
            AmityChatClient.newMessageRepository()
                .softDeleteMessage(message.getMessageId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    onSuccess()
                }
                .doOnError {
                    onError(it)
                }
                .doOnSubscribe {
                    dismissDeleteConfirmation()
                }
                .subscribe()
        }
    }

    fun setTargetDeletedMessage(message: AmityMessage?) {
        targetDeletedMessage.value = message
    }

    fun showDeleteConfirmation(message: AmityMessage) {
        targetDeletedMessage.value = message
        showDeleteDialog.value = true
    }

    fun dismissDeleteConfirmation() {
        targetDeletedMessage.value = null
        showDeleteDialog.value = false
    }

    fun flagMessage(
        messageId: String,
        reason: AmityContentFlagReason,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newMessageRepository()
            .flagMessage(messageId, reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
            }
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun unflagMessage(
        messageId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newMessageRepository()
            .unflagMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
            }
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun observeMembership(): Flow<AmityChannelMember> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMyMembership()
            .asFlow()
            .catch {

            }
    }

    companion object {
        fun create(channelId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AmityLivestreamChatViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return AmityLivestreamChatViewModel(channelId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }


    fun updateSheetUIState(uiState: AmityLiveStreamSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = uiState
        }
    }

    sealed class AmityLiveStreamSheetUIState(val messageId: String) {

        data class OpenSheet(val message: AmityMessage, val isChannelModerator: Boolean) : AmityLiveStreamSheetUIState(message.getMessageId())

        data class OpenReportSheet(val id: String) : AmityLiveStreamSheetUIState(id)

        data class OpenReportOtherReasonSheet(val id: String) : AmityLiveStreamSheetUIState(id)

        object CloseSheet : AmityLiveStreamSheetUIState("")
    }

    sealed class MessageListState {
        object BANNED : MessageListState()
        object MUTED : MessageListState()
        object ERROR : MessageListState()
        object SUCCESS : MessageListState()
        object LOADING : MessageListState()
        object INITIAL : MessageListState()

        companion object {
            fun from(
                member: AmityChannelMember?,
                loadState: LoadState,
                itemCount: Int,
            ): MessageListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (member?.isBanned() == true) {
                    BANNED
                } else if (member?.isMuted() == true) {
                    MUTED
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }

}