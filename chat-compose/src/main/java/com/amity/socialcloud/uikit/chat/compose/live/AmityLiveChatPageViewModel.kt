package com.amity.socialcloud.uikit.chat.compose.live

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.filter
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.member.query.AmityChannelMembership
import com.amity.socialcloud.sdk.api.chat.member.query.AmityChannelMembershipFilter
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit

class AmityLiveChatPageViewModel constructor(private val channelId: String) : ViewModel() {

    val replyTo = mutableStateOf<AmityMessage?>(null)
    var showDeleteDialog = mutableStateOf(false)
    var targetDeletedMessage = mutableStateOf<AmityMessage?>(null)
    val messageListState = mutableStateOf<MessageListState>(MessageListState.INITIAL)

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
                    val hasMentionChannel =
                        mentionMetadata.any { it is AmityMentionMetadata.CHANNEL }
                    if (hasMentionChannel) {
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

    fun searchChannelMembers(keyword: String): Flow<PagingData<AmityMentionSuggestion>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityChannelMembership.MEMBER, AmityChannelMembership.MUTED))
            .includeDeleted(false)
            .build()
            .query()
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { pagingData ->
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
            .catch {
            
            }
    }

    fun getChannelMembers(): Flow<PagingData<AmityMentionSuggestion>> {
        val hasPermissionFlowable = AmityCoreClient.hasPermission(AmityPermission.MUTE_CHANNEL)
            .atChannel(channelId)
            .check()
        val membersFlowable = AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembers()
            .filter(AmityChannelMembershipFilter.MEMBER)
            .includeDeleted(false)
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
                if (hasPermission) {
                    it.insertHeaderItem(
                        TerminalSeparatorType.SOURCE_COMPLETE,
                        AmityMentionSuggestion.CHANNEL(channelId)
                    )
                } else {
                    it
                }
            }
        }.subscribeOn(Schedulers.io())
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

    fun getMessage(messageId: String): Flow<AmityMessage> {
        return AmityChatClient.newMessageRepository()
            .getMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {
            
            }
    }

    fun observeGlobalBanEvent(): Flow<Boolean> {
        return AmityCoreClient.getGlobalBanEvents()
            .map {
                it.userId == AmityCoreClient.getUserId()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {
            
            }
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

    fun showDeleteConfirmation(message: AmityMessage) {
        targetDeletedMessage.value = message
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
            .catch {
            
            }
    }

    private fun subscribeToSubChannel(subChannelId: String) {
        AmityChatClient
            .newChannelRepository()
            .getChannel(subChannelId)
            .firstOrError()
            .flatMapCompletable {
                it.subscription().subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .doOnError {}
            .subscribe()

        AmityChatClient
            .newSubChannelRepository()
            .getSubChannel(subChannelId)
            .firstOrError()
            .flatMapCompletable {
                it.subscription().subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .doOnError {}
            .subscribe()
    }

    private fun unsubscribeToSubChannel(subChannelId: String) {
        AmityChatClient
            .newChannelRepository()
            .getChannel(subChannelId)
            .firstOrError()
            .flatMapCompletable {
                it.subscription().unsubscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .doOnError {}
            .subscribe()

        AmityChatClient
            .newSubChannelRepository()
            .getSubChannel(subChannelId)
            .firstOrError()
            .flatMapCompletable {
                it.subscription().unsubscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .doOnError { }
            .subscribe()
    }
    
    fun flagMessage(
        message: AmityMessage,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newMessageRepository()
            .flagMessage(message.getMessageId())
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

    fun unFlagMessage(
        message: AmityMessage,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newMessageRepository()
            .unflagMessage(message.getMessageId())
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

    fun observeMembership() : Flow<AmityChannelMember> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMyMembership()
            .asFlow()
            .catch {

            }
    }
    
    fun addMessageReaction(message: AmityMessage, reactionName: String) {
        val reactionRepository = AmityCoreClient.newReactionRepository()
        val ref = AmityReactionReference.MESSAGE(message.getMessageId())
        val myReaction = message.getMyReactions().firstOrNull()
        Completable.defer {
            if (myReaction != null) {
                reactionRepository.removeReaction(ref, myReaction)
            } else {
                Completable.complete()
            }
        }.andThen(
            reactionRepository
                .addReaction(ref, reactionName)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
            }
            .subscribe()
    }
    
    fun removeMessageReaction(message: AmityMessage, reactionName: String) {
        val ref = AmityReactionReference.MESSAGE(message.getMessageId())
        AmityCoreClient.newReactionRepository()
            .removeReaction(ref, reactionName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
            }
            .subscribe()
    }

    fun setMessageListState(state: MessageListState) {
        messageListState.value = state
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
                return if (loadState is LoadState.Loading && itemCount == 0)  {
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