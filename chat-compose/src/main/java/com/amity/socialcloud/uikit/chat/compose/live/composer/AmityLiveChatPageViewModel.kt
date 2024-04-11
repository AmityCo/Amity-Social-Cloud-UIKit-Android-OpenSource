package com.amity.socialcloud.uikit.chat.compose.live.composer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.filter
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

// TODO Rename to AmityMessageListPageViewModel and move to chat/compose/live/common
class AmityLiveChatPageViewModel constructor(private val channelId: String) : ViewModel() {
    
    val replyTo = mutableStateOf<AmityMessage?>(null)
    var showDeleteDialog = mutableStateOf(false)
    var targetDeletedMessage = mutableStateOf<String?>(null)
    val isFetching = mutableStateOf(true)
    
    init {
        subscribeToSubChannel(channelId)
    }
    
    fun createMessage(
        parentId: String?,
        text: String,
        mentionMetadata: List<AmityMentionMetadata> = emptyList(),
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AmityChatClient.newMessageRepository()
            .createTextMessage(
                channelId,
                text
            )
            .apply {
                if (parentId != null) {
                    this.parentId(parentId)
                }
            }
            .apply {
                if (mentionMetadata.isNotEmpty()) {
                    val mentionUsers = mentionMetadata.filterIsInstance<AmityMentionMetadata.USER>()
                    val metadata = AmityMentionMetadataCreator(mentionMetadata).create()
                    this.metadata(metadata)
                    val hasMentionChannel = mentionMetadata.any { it is AmityMentionMetadata.CHANNEL }
                    if(hasMentionChannel) {
                        this.mentionChannel()
                    }
                    this.mentionUsers(mentionUsers.map { it.getUserId() })
                }
            }
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
            .doOnSubscribe {
                isFetching.value = true
            }
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
    }

    fun searchChannelMembers(keyword: String): Flow<PagingData<AmityMentionSuggestion>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .searchMembers(keyword)
            .build()
            .query()
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { pagingData->
                pagingData.filter {
                    it.getUser() != null
                }.map { channelMember ->
                    AmityMentionSuggestion.USER(
                        user = channelMember.getUser()!!
                    ) as AmityMentionSuggestion
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun getChannelMembers(): Flow<PagingData<AmityMentionSuggestion>> {
        val hasPermissionFlowable = AmityCoreClient.hasPermission(AmityPermission.EDIT_CHANNEL_USER)
            .atChannel(channelId)
            .check()
        val membersFlowable = AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembers()
            .build()
            .query()
            .debounce(500, TimeUnit.MILLISECONDS)
            .map {
                it.filter {
                    it.getUser() != null
                }
            }
        return Flowable.zip(hasPermissionFlowable, membersFlowable) { hasPermission, members ->
            members.map { channelMember ->
                AmityMentionSuggestion.USER(
                    user = channelMember.getUser()!!
                ) as AmityMentionSuggestion
            }.let {
                if(hasPermission) {
                    it.insertHeaderItem(TerminalSeparatorType.SOURCE_COMPLETE, AmityMentionSuggestion.CHANNEL(channelId))
                } else {
                    it
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
    
    fun isChannelModerator(): Flow<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.EDIT_CHANNEL_USER)
            .atChannel(channelId)
            .check()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .asFlow()
    }
    
    fun getMessage(messageId: String): Flow<AmityMessage> {
        return AmityChatClient.newMessageRepository()
            .getMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
    
    fun setReplyToMessage(message: AmityMessage) {
        replyTo.value = message
    }
    
    fun dismissReplyMessage() {
        replyTo.value = null
    }
    
    fun deleteMessage(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        targetDeletedMessage.value?.let { messageId ->
            AmityChatClient.newMessageRepository()
                .softDeleteMessage(messageId)
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
    
    fun showDeleteConfirmation(messageId: String) {
        targetDeletedMessage.value = messageId
        showDeleteDialog.value = true
    }
    
    fun dismissDeleteConfirmation() {
        targetDeletedMessage.value = null
        showDeleteDialog.value = false
    }
    
    fun getNetworkConnectionStateFlow(): Flow<NetworkConnectionEvent> {
        return NetworkConnectionEventBus.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
    
    fun isFetching(isFetching: Boolean) {
        if (this.isFetching.value) {
            this.isFetching.value = isFetching
        }
    }

    private fun subscribeToSubChannel(subChannelId: String) {
        AmityChatClient
            .newSubChannelRepository()
            .getSubChannel(subChannelId)
            .distinctUntilChanged { old, current ->
                old.getSubChannelId() == current.getSubChannelId()
            }
            .flatMapCompletable {
                it.subscription().subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
        
    }

    private fun unsubscribeToSubChannel(subChannelId: String) {
        AmityChatClient
            .newSubChannelRepository()
            .getSubChannel(subChannelId)
            .singleElement()
            .flatMapCompletable {
                it.subscription().unsubscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun onStop() {
        unsubscribeToSubChannel(channelId)
    }
    
    companion object {
        enum class MessageAction {
            COPY,
            DELETE
        }
        class Error(val action: MessageAction, val throwable: Throwable)
    }
}