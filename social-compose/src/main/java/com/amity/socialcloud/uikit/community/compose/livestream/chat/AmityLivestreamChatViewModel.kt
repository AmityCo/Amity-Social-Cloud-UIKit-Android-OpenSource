package com.amity.socialcloud.uikit.community.compose.livestream.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.events.AmityTopicSubscription
import com.amity.socialcloud.sdk.model.core.events.AmityUserEvents
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.chat.message.AmityPinnedMessage
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
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

class AmityLivestreamChatViewModel constructor(private val channelId: String) : AmityBaseViewModel() {

    var showDeleteDialog = mutableStateOf(false)
    var targetDeletedMessage = mutableStateOf<AmityMessage?>(null)

    /** Held so the same topic is released in [onCleared]. */
    private var currentUserSubscription: AmityTopicSubscription? = null

    init {
        subscribeCurrentUserTopic()
    }

    /**
     * Subscribes the signed-in user's own topic, which carries `user.updated`.
     *
     * PIN_MESSAGE can be granted at network level from the Console. That change reaches the
     * client only on `user.updated`, and nothing subscribes to the user's topic otherwise, so a
     * revoked viewer kept being offered the pin action until the app restarted (PDT-5548).
     * The SDK already handles the event: it writes the permissions onto the user row, and
     * `hasPinPermission` observes that row, so `canPin` recomputes with no further wiring.
     *
     * Scoped to this screen rather than the whole session, matching how the livestream pages
     * already take and release their room, channel and post topics.
     *
     * Not restored after an MQTT reconnect. Nothing records a manual subscription, so none of
     * the livestream topics come back either. A reconnect while the stream is open leaves the
     * pin action as stale as it was before this fix.
     */
    private fun subscribeCurrentUserTopic() {
        AmityCoreClient.getCurrentUser()
            .firstOrError()
            .flatMapCompletable { user ->
                val subscription = user.subscription(AmityUserEvents.USER)
                currentUserSubscription = subscription
                subscription.subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .onErrorComplete()
            .subscribe()
            .let(::addDisposable)
    }

    override fun onCleared() {
        // Deliberately not added to the composite: super disposes it, which would cancel the
        // unsubscribe before the broker sees it.
        currentUserSubscription
            ?.unsubscribeTopic()
            ?.subscribeOn(Schedulers.io())
            ?.onErrorComplete()
            ?.subscribe()
        currentUserSubscription = null
        super.onCleared()
    }

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
        return AmityChatClient.newChannelRepository()
            .joinChannel(channelId = channelId)
            .onErrorResumeNext { error ->
                onError(error)
                AmityChatClient.newChannelRepository().getChannel(channelId).firstOrError()
            }
            .flatMapPublisher {
                AmityChatClient.newMessageRepository()
                    .getMessages(channelId)
                    .sortBy(AmityMessageQuerySortOption.LAST_CREATED)
                    .build()
                    .query()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError(it)
            }
            .asFlow()
            .catch { error ->
                // joinChannel/getChannel can error (e.g. empty or inaccessible channelId for
                // event/recorded rooms). Without this catch, the error propagates out of the
                // collecting coroutine (collectAsLazyPagingItems) and crashes the app. Degrade
                // to an empty message list instead.
                Log.d("AmityErrorTrace", "ChatVM.getMessageList error (channelId='$channelId'): ${error.message}", error)
                emit(PagingData.empty())
            }
    }

    fun getChannelFlow(): Flow<AmityChannel> {
        return AmityChatClient
            .newChannelRepository()
            .getChannel(channelId)
            .distinctUntilChanged { old, new ->
                // Only emit if metadata, mute or the pinned message actually changed. The pin is
                // its own channel field: leaving it out of this comparison swallows every
                // pin / unpin, since neither touches metadata.
                old.getMetadata() == new.getMetadata()
                        && old.isMuted() == new.isMuted()
                        && old.getPinnedMessage()?.getMessageId() == new.getPinnedMessage()?.getMessageId()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {

            }
    }

    fun getMutedMemberIdsFlow(): Flow<List<String>> {
        return getChannelFlow().map { channel ->
            channel.getMetadata()
                ?.getAsJsonArray("mutedMembers")
                ?.mapNotNull { it.asString }
                ?: emptyList()
        }
    }

    fun getPinnedMessageFlow(): Flow<AmityPinnedMessage?> {
        return getChannelFlow().map { it.getPinnedMessage() }
    }

    /**
     * The permission arm of `canPin`. Re-emits when the current user's channel membership row is
     * rewritten, so a revoked role hides the controls without waiting for a 403.
     */
    fun hasPinPermission(): Flow<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.PIN_MESSAGE)
            .atChannel(channelId)
            .check()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch { }
    }

    /**
     * Re-reads the channel from the server so the cached membership row — and with it the
     * channel-scoped permissions `canPin` is derived from — matches what the server enforces.
     *
     * This is the REQ-086 backstop, not the mechanism. A role change normally reaches the client
     * on `channel.roleAdded` / `channel.roleRemoved`, which the SDK writes straight into the
     * membership row, and `hasPinPermission` re-emits off that. This covers the window before
     * that event lands, and an SDK build that does not handle it yet — without it a revoked
     * user keeps being offered pin and unpin until the page is reopened.
     */
    private fun refreshMyChannelPermissions() {
        AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .ignoreElement()
            .onErrorComplete()
            .subscribe()
            .let(::addDisposable)
    }

    /**
     * No error UI on failure by design (Plan 39 Open Question 4): the banner keeps rendering
     * whatever the channel live object holds. A 403 refreshes the membership row so `canPin`
     * recomputes and both controls disappear (REQ-086); [onPermissionDenied] lets a caller hook
     * anything further onto that.
     */
    fun pinMessage(messageId: String, onPermissionDenied: () -> Unit = {}) {
        AmityChatClient.newMessageRepository()
            .pinMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                if (AmityError.from(it) == AmityError.PERMISSION_DENIED) {
                    refreshMyChannelPermissions()
                    onPermissionDenied()
                }
            }
            .onErrorComplete()
            .subscribe()
            .let(::addDisposable)
    }

    fun unpinMessage(messageId: String, onPermissionDenied: () -> Unit = {}) {
        AmityChatClient.newMessageRepository()
            .unpinMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                if (AmityError.from(it) == AmityError.PERMISSION_DENIED) {
                    refreshMyChannelPermissions()
                    onPermissionDenied()
                }
            }
            .onErrorComplete()
            .subscribe()
            .let(::addDisposable)
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
            .let(::addDisposable)
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
                val moderators = channel.getMetadata()?.getAsJsonArray("moderators")?.mapNotNull { it.asString }
                moderators?.contains(userId) ?: false
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
            .let(::addDisposable)
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
            .let(::addDisposable)
    }

    // Tracks the co-host currently reflected in the channel's moderator metadata, so that when
    // the co-host slot clears we know whom to demote.
    // AmityLiveStreamChatViewModel.coHostUserId used by refreshHostAndCoHostId.
    private var trackedCoHostUserId: String? = null

    /**
     * Keeps the channel "moderators" metadata (and the channel-moderator role) in sync with the
     * current co-host
     *
     * Only the host mutates roles. When a co-host is present and not yet a moderator they are
     * promoted; when the co-host slot clears, the previous co-host is demoted. Both branches are
     * idempotent (guarded against the current metadata), so re-accepting as co-host in the same
     * session re-promotes cleanly.
     */
    fun syncCoHostModeratorRole(coHostUserId: String?, isHost: Boolean) {
        val newCoHostUserId = coHostUserId?.takeIf { it.isNotBlank() }
        if (!isHost) {
            // Non-host clients only track the latest co-host; they never mutate roles.
            trackedCoHostUserId = newCoHostUserId
            return
        }
        val previousCoHostUserId = trackedCoHostUserId
        trackedCoHostUserId = newCoHostUserId

        if (newCoHostUserId == null && previousCoHostUserId == null) {
            return
        }

        AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { channel ->
                val moderators = channel.getMetadata()
                    ?.getAsJsonArray("moderators")
                    ?.mapNotNull { it.asString }
                    ?: emptyList()
                when {
                    newCoHostUserId != null && !moderators.contains(newCoHostUserId) -> {
                        promoteToModerator(newCoHostUserId)
                    }
                    newCoHostUserId == null &&
                            previousCoHostUserId != null &&
                            moderators.contains(previousCoHostUserId) -> {
                        demoteToMember(previousCoHostUserId)
                    }
                }
            }
            .doOnError { }
            .subscribe()
            .let(::addDisposable)
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

        data class OpenUserActionsSheet(val userId: String, val displayName: String, val user: AmityUser? = null, val isModerator: Boolean = false, val isMuted: Boolean = false) : AmityLiveStreamSheetUIState("")

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