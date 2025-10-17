package com.amity.socialcloud.uikit.community.compose.livestream.chat

import android.util.Log
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
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.joda.time.Duration

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
            .distinctUntilChanged { old, new ->
                // Only emit if metadata actually changed
                old.getMetadata() == new.getMetadata()
            }
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

    fun getChannelOwnerId(): Flow<String?> {
        // For testing: return current user ID to make everyone appear as owner
        return flowOf(AmityCoreClient.getUserId())
    }
    
    private fun updateChannelMetadata(
        updateMetadata: (JsonObject) -> JsonObject,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .firstOrError()
            .flatMapCompletable { channel ->
                val currentMetadata = channel.getMetadata() ?: JsonObject()
                val updatedMetadata = updateMetadata(currentMetadata)
                
                AmityChatClient.newChannelRepository()
                    .editChannel(channelId)
                    .metadata(updatedMetadata)
                    .build()
                    .apply()
                    .ignoreElement()
            }
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
    
    private fun addUserToMetadataList(key: String, userId: String): (JsonObject) -> JsonObject {
        return { metadata ->
            val newMetadata = metadata.deepCopy()
            val list = metadata.getAsJsonArray(key)?.map { it.asString }?.toMutableList() ?: mutableListOf()
            if (!list.contains(userId)) {
                list.add(userId)
            }
            val jsonArray = com.google.gson.JsonArray()
            list.forEach { jsonArray.add(it) }
            newMetadata.add(key, jsonArray)
            newMetadata
        }
    }
    
    private fun removeUserFromMetadataList(key: String, userId: String): (JsonObject) -> JsonObject {
        return { metadata ->
            val newMetadata = metadata.deepCopy()
            val list = metadata.getAsJsonArray(key)?.map { it.asString }?.toMutableList() ?: mutableListOf()
            list.remove(userId)
            val jsonArray = com.google.gson.JsonArray()
            list.forEach { jsonArray.add(it) }
            newMetadata.add(key, jsonArray)
            newMetadata
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

    fun isUserModerator(userId: String): Flow<Boolean> {
        return getChannelFlow()
            .map { channel ->
                Log.d("--F", "isUserModerator from channel flow ${channel.getMetadata()}")
                val metadata = channel.getMetadata()
                val moderators = metadata?.getAsJsonArray("moderators")
                moderators?.any { it.asString == userId } ?: false
            }
            .distinctUntilChanged()
            .catch {
                emit(false)
            }
    }

    fun isUserMuted(userId: String): Flow<Boolean> {
        Log.d("--F", "isUserMuted")
        return getChannelFlow()
            .map { channel ->
                Log.d("--F", "isUserMuted from channel flow ${channel.getMetadata()}")
                val metadata = channel.getMetadata()
                
                // Check if user is a moderator first - moderators cannot be muted
                val moderators = metadata?.getAsJsonArray("moderators")
                val isModerator = moderators?.any { it.asString == userId } ?: false
                
                // If user is a moderator, they are not considered muted
                if (isModerator) {
                    return@map false
                }
                
                // Otherwise, check if they are in the muted list
                val mutedMembers = metadata?.getAsJsonArray("mutedMembers")
                mutedMembers?.any { it.asString == userId } ?: false
            }
            .distinctUntilChanged()
            .catch {
                emit(false)
            }
    }

    fun promoteToModerator(
        userId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newChannelRepository()
            .moderation(channelId)
            .addRole("channel-moderator", listOf(userId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                // Update channel metadata to trigger RTE
                updateChannelMetadata(
                    updateMetadata = addUserToMetadataList("moderators", userId),
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun demoteToMember(
        userId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmityChatClient.newChannelRepository()
            .moderation(channelId)
            .removeRole("channel-moderator", listOf(userId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                // Update channel metadata to trigger RTE
                updateChannelMetadata(
                    updateMetadata = removeUserFromMetadataList("moderators", userId),
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun muteUser(
        userId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        // Only update channel metadata, don't use SDK's built-in mute
        updateChannelMetadata(
            updateMetadata = addUserToMetadataList("mutedMembers", userId),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun unmuteUser(
        userId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        // Only update channel metadata, don't use SDK's built-in unmute
        updateChannelMetadata(
            updateMetadata = removeUserFromMetadataList("mutedMembers", userId),
            onSuccess = onSuccess,
            onError = onError
        )
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

        data class OpenUserActionsSheet(val userId: String, val displayName: String, val isModerator: Boolean = false, val isMuted: Boolean = false) : AmityLiveStreamSheetUIState("")

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
                isUserMutedInMetadata: Boolean = false,
            ): MessageListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (member?.isBanned() == true) {
                    BANNED
                } else if (member?.isMuted() == true || isUserMutedInMetadata) {
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