package com.amity.socialcloud.uikit.chat.compose.group

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.TerminalSeparatorType
import androidx.paging.filter
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.member.query.AmityChannelMembership
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.settings.AmityChatSettings
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityGroupChatPageViewModel(
    private val channelId: String,
    private val jumpToMessageId: String? = null,
) : AmityBaseViewModel() {

    val replyTo = mutableStateOf<AmityMessage?>(null)
    private val _replyToMessage = MutableStateFlow<AmityMessage?>(null)
    val replyToMessage: StateFlow<AmityMessage?> = _replyToMessage

    private val _editingMessage = MutableStateFlow<AmityMessage?>(null)
    val editingMessage: StateFlow<AmityMessage?> = _editingMessage.asStateFlow()

    fun startEditingMessage(message: AmityMessage) {
        _replyToMessage.value = null
        replyTo.value = null
        _editingMessage.value = message
    }

    fun cancelEditingMessage() {
        _editingMessage.value = null
    }

    val showDeleteDialog = mutableStateOf(false)
    val targetDeletedMessage = mutableStateOf<AmityMessage?>(null)

    private val _isFetching = MutableStateFlow(true)
    val isFetching: StateFlow<Boolean> get() = _isFetching

    fun finishLoading() {
        _isFetching.value = false
    }

    private var latestReadSegment = 0
    private var latestReadMessage: AmityMessage? = null

    val messageList: Flow<PagingData<AmityMessage>> by lazy {
        AmityChatClient.newMessageRepository()
            .getMessages(channelId)
            .sortBy(AmityMessageQuerySortOption.LAST_CREATED)
            .also { if (jumpToMessageId != null) it.aroundMessageId(jumpToMessageId) }
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch { }
            .cachedIn(viewModelScope)
    }

    init {
        startReading()
    }

    fun startReading() {
        AmityChatClient.newSubChannelRepository()
            .startMessageReceiptSync(channelId)
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
    }

    fun stopReading() {
        AmityChatClient.newSubChannelRepository()
            .stopMessageReceiptSync(channelId)
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
    }

    fun markMessageAsRead(message: AmityMessage) {
        val segment = message.getSegment()
        if (segment > latestReadSegment) {
            message.markRead()
            latestReadSegment = segment
            latestReadMessage = message
        }
    }

    private val _channelFlow: Flow<AmityChannel> by lazy {
        AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun getChannelFlow(): Flow<AmityChannel> = _channelFlow

    fun observeChannelMuted(): Flow<Boolean> {
        return _channelFlow.map { it.isMuted() }.distinctUntilChanged()
    }

    fun observeMembership(): Flow<AmityChannelMember> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMyMembership()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun isChannelModerator(): Flow<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.MUTE_CHANNEL)
            .atChannel(channelId)
            .check()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch { }
    }

    fun getMembersFromCache(): Flow<List<AmityChannelMember>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembersFromCache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { emit(emptyList()) }
    }

    fun getMemberRoles(): Flow<Map<String, List<String>>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .getMembersFromCache()
            .map { members ->
                members.associate { member ->
                    (member.getUserId()) to (member.getRoles())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { emit(emptyMap()) }
    }

    fun searchChannelMembers(keyword: String): Flow<PagingData<AmityMentionSuggestion>> {
        return AmityChatClient.newChannelRepository()
            .membership(channelId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityChannelMembership.MEMBER, AmityChannelMembership.MUTED))
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
            .catch { }
    }

    fun getChannelMembers(): Flow<PagingData<AmityMentionSuggestion>> {
        val isMentionChannelEnabledFlowable = AmityChatClient.getSettings()
            .map { it.isMentionChannelEnabled() }
            .onErrorReturn { true }
        val membersFlowable = AmityChatClient.newChannelRepository()
            .membership(channelId)
            .searchMembers("")
            .membershipFilter(listOf(AmityChannelMembership.MEMBER, AmityChannelMembership.MUTED))
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
        return Flowable.combineLatest(isMentionChannelEnabledFlowable, membersFlowable) { isMentionChannelEnabled, members ->
            if (isMentionChannelEnabled) {
                members.insertHeaderItem(
                    TerminalSeparatorType.SOURCE_COMPLETE,
                    AmityMentionSuggestion.CHANNEL(channelId)
                )
            } else {
                members
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun createTextMessage(
        text: String,
        parentId: String? = null,
        mentionMetadata: List<AmityMentionMetadata> = emptyList(),
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newMessageRepository()
            .createTextMessage(channelId, text)
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
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun sendImageMessage(
        imageUri: Uri,
        parentId: String? = null,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newMessageRepository()
            .createImageMessage(channelId, imageUri)
            .apply {
                if (parentId != null) {
                    this.parentId(parentId)
                }
            }
            .build()
            .send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    // Stores video URIs for thumbnail generation during upload
    private val _sentVideoUris = MutableStateFlow<List<Uri>>(emptyList())
    val sentVideoUris: StateFlow<List<Uri>> = _sentVideoUris.asStateFlow()

    fun sendVideoMessage(
        videoUri: Uri,
        parentId: String? = null,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        _sentVideoUris.value = (_sentVideoUris.value + videoUri).takeLast(10)
        AmityChatClient.newMessageRepository()
            .createVideoMessage(channelId, videoUri)
            .apply {
                if (parentId != null) {
                    this.parentId(parentId)
                }
            }
            .build()
            .send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun cancelUpload(message: AmityMessage) {
        AmityFileService().cancelUpload(message.getMessageId())
    }

    fun resendMessage(message: AmityMessage) {
        val parentId = message.getParentId()
        when (val data = message.getData()) {
            is AmityMessage.Data.TEXT -> {
                val text = data.getText()
                AmityChatClient.newMessageRepository()
                    .softDeleteMessage(message.getMessageId())
                    .andThen(
                        AmityChatClient.newMessageRepository()
                            .createTextMessage(channelId, text)
                            .apply { if (parentId != null) parentId(parentId) }
                            .build()
                            .send()
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, { handleResendError(it) })
            }
            is AmityMessage.Data.IMAGE -> {
                val uri = data.getImage()?.getUri()
                if (uri != null) {
                    val androidUri = uri as? android.net.Uri ?: android.net.Uri.parse(uri.toString())
                    AmityChatClient.newMessageRepository()
                        .softDeleteMessage(message.getMessageId())
                        .andThen(
                            AmityChatClient.newMessageRepository()
                                .createImageMessage(channelId, androidUri)
                                .apply { if (parentId != null) parentId(parentId) }
                                .build()
                                .send()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, { handleResendError(it) })
                }
            }
            is AmityMessage.Data.VIDEO -> {
                val uri = data.getVideo()?.getUri()
                if (uri != null) {
                    val androidUri = uri as? android.net.Uri ?: android.net.Uri.parse(uri.toString())
                    AmityChatClient.newMessageRepository()
                        .softDeleteMessage(message.getMessageId())
                        .andThen(
                            AmityChatClient.newMessageRepository()
                                .createVideoMessage(channelId, androidUri)
                                .apply { if (parentId != null) parentId(parentId) }
                                .build()
                                .send()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, { handleResendError(it) })
                }
            }
            else -> {}
        }
    }

    private fun handleResendError(error: Throwable) {
        if (error is AmityException && error.code == AmityError.ITEM_NOT_FOUND.code) {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                message = DefaultAmityChatStringProvider.getInstance()
                    .getString("chat.toast.reply.parent.deleted")
            )
        }
    }

    fun deleteFailedMessage(message: AmityMessage) {
        AmityChatClient.newMessageRepository()
            .softDeleteMessage(message.getMessageId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun deleteMessage(
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        targetDeletedMessage.value?.let { message ->
            AmityChatClient.newMessageRepository()
                .softDeleteMessage(message.getMessageId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    dismissDeleteConfirmation()
                    onSuccess()
                }
                .doOnError {
                    dismissDeleteConfirmation()
                    onError(it)
                }
                .subscribe()
        }
    }

    fun setReplyToMessage(message: AmityMessage) {
        _editingMessage.value = null
        replyTo.value = message
        _replyToMessage.value = message
    }

    fun updateReplyToMessage(message: AmityMessage) {
        _replyToMessage.value = message
    }

    fun dismissReplyMessage() {
        replyTo.value = null
        _replyToMessage.value = null
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
        message: AmityMessage,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newMessageRepository()
            .flagMessage(message.getMessageId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun flagMessageWithReason(
        message: AmityMessage,
        reason: AmityContentFlagReason,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newMessageRepository()
            .flagMessage(message.getMessageId(), reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun unflagMessage(
        message: AmityMessage,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityChatClient.newMessageRepository()
            .unflagMessage(message.getMessageId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { onSuccess() }
            .doOnError { onError(it) }
            .subscribe()
    }

    fun addReaction(
        message: AmityMessage,
        reactionName: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        val ref = AmityReactionReference.MESSAGE(message.getMessageId())
        val myReaction = message.getMyReactions().firstOrNull()
        Completable.defer {
            if (myReaction != null) {
                AmityCoreClient.newReactionRepository().removeReaction(ref, myReaction)
                    .onErrorComplete()
            } else {
                Completable.complete()
            }
        }.andThen(
            AmityCoreClient.newReactionRepository().addReaction(ref, reactionName)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess() }, { onError(it) })
    }

    fun removeReaction(
        message: AmityMessage,
        reactionName: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        val ref = AmityReactionReference.MESSAGE(message.getMessageId())
        AmityCoreClient.newReactionRepository()
            .removeReaction(ref, reactionName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess() }, { onError(it) })
    }

    private fun subscribeToChannel(channelId: String) {
        AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .firstOrError()
            .flatMapCompletable { it.subscription().subscribeTopic() }
            .subscribeOn(Schedulers.io())
            .doOnError { }
            .subscribe()

        AmityChatClient.newSubChannelRepository()
            .getSubChannel(channelId)
            .firstOrError()
            .flatMapCompletable { it.subscription().subscribeTopic() }
            .subscribeOn(Schedulers.io())
            .doOnError { }
            .subscribe()
    }

    fun onStop() {
        latestReadMessage?.let { it.markRead() }
        stopReading()
    }

    fun getMessage(messageId: String): Flow<AmityMessage> {
        return AmityChatClient.newMessageRepository()
            .getMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun getNetworkConnectionStateFlow(): Flow<NetworkConnectionEvent> {
        return NetworkConnectionEventBus.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }
}
