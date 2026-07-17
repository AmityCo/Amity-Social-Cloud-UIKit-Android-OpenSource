package com.amity.socialcloud.uikit.community.compose.livestream.room.view

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.AmityCoHostEvent
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.core.events.AmityRoomEvents
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitationStatus
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.core.reaction.live.AmityLiveReaction
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
import com.amity.socialcloud.sdk.model.video.room.AmityRoomModeration
import com.amity.socialcloud.sdk.model.video.room.AmityRoomStatus
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamChatViewModel.MessageListState
import io.livekit.android.room.Room
import io.livekit.android.room.track.CameraPosition
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.collections.plus
import java.util.Date
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

class AmityRoomPlayerViewModel(private val post: AmityPost) : AmityBaseViewModel() {
    val disposable = CompositeDisposable()
    val roomPresenceDisposable = CompositeDisposable()

    private val _uiState by lazy {
        MutableStateFlow(RoomPlayerState.initial(post))
    }

    val roomPlayerState get() = _uiState

    val fetchRecordedUrlsRelay = PublishProcessor.create<String>()

    // Watch minute tracking
    private var watchSessionId: String? = null
    private var watchSessionStartTime: DateTime? = null
    private var watchTrackingJob: Job? = null
    private var isWatchingPaused: Boolean = false
    private var accumulatedWatchTimeSeconds: Int = 0
    private var lastResumeTime: DateTime? = null
    private val watchTrackingLock = Any() // Lock to prevent concurrent access
    private var isUpdatingSession: Boolean = false // Flag to prevent duplicate updates
    private var watchingRoomId: String? = null // Track which room we're currently tracking to avoid duplicate sessions
    private var heartbeatDisposable = CompositeDisposable() // Separate disposable for heartbeat to manage it independently

    init {
        refresh()
    }

    fun refresh() {
        disposable.clear()
        _uiState.value = RoomPlayerState.initial(post)
        getPost(post.getPostId())
        observeRoom()
        observeRecordedUrls()
        fetchProductCatalogueSettings()
    }

    private fun observeRoom() {
        getRoomFlow(post)
            .distinctUntilChanged()
            .doOnNext { room ->
                _uiState.update { currentState ->
                    if (currentState.room?.getRoomId() != room.getRoomId()) {
                        val currentRoomId = currentState.room?.getRoomId()
                        if (!currentRoomId.isNullOrBlank()) {
                            unsubscribeRoomRT(currentState.room)
                        }
                        subscribeRoomRT(room)
                        observeRoomEvent(room.getRoomId())
                    }

                    val coHost = room.getParticipants().find { participant ->
                        participant.type == AmityRoom.ParticipantType.CoHost
                    }
                    val coHostUserId = coHost?.userId ?: currentState.cohostUserId
                    val coHostUser = coHost?.user ?: currentState.cohostUser
                    currentState.copy(
                        room = room,
                        roomModeration = room.getModeration(),
                        hostUserId = room.getCreatorId(),
                        hostUser = room.getCreator() ?: currentState.hostUser,
                        cohostUserId = coHostUserId,
                        cohostUser = coHostUser,
                    )
                }
            }
            .doOnError { error ->
                _uiState.update { currentState ->
                    currentState.copy(error = error)
                }
            }
            .map { room ->
                if (room.getStatus() != AmityRoomStatus.RECORDED) {
                    room.getChannelId() ?: ""
                } else {
                    ""
                }
            }
            .distinctUntilChanged()
            .doOnNext { channelId ->
                if (channelId.isNotEmpty()) {
                    _uiState.update { currentState ->
                        currentState.copy(channelId = channelId)
                    }
                    subscribeToSubChannel(channelId)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let { disposable.add(it) }
    }

    private fun observeRoomEvent(roomId: String) {
        AmityVideoClient.newRoomRepository()
            .getCoHostEvent(roomId)
            .doOnNext { event ->
                when (event) {
                    is AmityCoHostEvent.CoHostInvited -> {
                        if (event.invitation.getInvitedUserId() == AmityCoreClient.getUserId()) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    invitation = event.invitation,
                                )
                            }
                        }
                    }
                    is AmityCoHostEvent.CoHostInviteAccepted -> {
                        if (event.invitation.getInvitedUserId() == AmityCoreClient.getUserId()) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    cohostUserId = event.invitation.getInvitedUserId(),
                                    cohostUser = event.invitation.getInvitedUser()
                                )
                            }
                        }
                    }
                    is AmityCoHostEvent.CoHostJoined -> {
                        event.room
                            .getParticipants()
                            .firstOrNull {
                                it.type == AmityRoom.ParticipantType.CoHost
                            }
                            ?.let { cohostParticipant ->
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        cohostUserId = cohostParticipant.userId,
                                        cohostUser = cohostParticipant.user,
                                    )
                                }
                            }
                    }
                    is AmityCoHostEvent.CoHostRemoved,
                    is AmityCoHostEvent.CoHostLeft,
                    is AmityCoHostEvent.CoHostInviteCancelled,
                    is AmityCoHostEvent.CoHostInviteRejected -> {
                        if (event is AmityCoHostEvent.CoHostLeft) {
                            AmityUIKitSnackbar.publishSnackbarMessage(message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_cohost_left"))
                        }
                        val wasRemoved = event is AmityCoHostEvent.CoHostRemoved
                        if (wasRemoved) {
                            Log.d(TAG, "CoHostRemoved: dropping stale room + token")
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                invitation = null,
                                cohostUserId = null,
                                cohostUser = null,
                                isStreamerMode = if (wasRemoved) {
                                    false
                                } else {
                                    currentState.isStreamerMode
                                },
                                // Being force-removed ends the local streaming session the same way
                                // leaving does; drop the released room + stale token so a re-invite
                                // and re-join start clean.
                                liveKitRoom = if (wasRemoved) null else currentState.liveKitRoom,
                                broadcasterData = if (wasRemoved) null else currentState.broadcasterData,
                            )
                        }
                    }
                    is AmityCoHostEvent.CoHostPermissionUpdated -> {
                        if (event.cohostCanManageProduct) {
                            AmityUIKitSnackbar.publishSnackbarMessage(
                                message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_status_product_tagging_enabled")
                            )
                        }
                    }
                    else -> {}
                }
            }
            .onErrorComplete()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun getPost(postId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(postId)
                .asFlow()
                .catch { error ->
                    _uiState.update { currentState ->
                        currentState.copy(error = error)
                    }
                }
                .collectLatest { post ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            post = post,
                            reviewStatus = post.getReviewStatus(),
                        )
                    }
                }
        }
    }

    fun subscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST)
            .subscribeTopic()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun unSubscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST)
            .unsubscribeTopic()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun fetchRoomInvitation(
        room: AmityRoom,
        wasInvited: Boolean,
    ) {
        room.getInvitation()
            .doOnSuccess { invitations ->
                invitations
                    .firstOrNull {
                        it.getInvitedUserId() == AmityCoreClient.getUserId()
                    }
                    ?.let { invitation ->
                        if (invitation.getStatus() == AmityInvitationStatus.PENDING) {
                            _uiState.update { currentState ->
                                currentState.copy(invitation = invitation)
                            }
                        } else {
                            // If navigate from invitation notification and invitation is not pending should show error toast
                            if (wasInvited) {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_error_community_invitation_unavailable_error")
                                )
                            }
                        }
                    } ?: if (wasInvited) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_error_community_invitation_unavailable_error")
                    )
                } else {
                }
            }
            .doOnError {
                if (wasInvited) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_error_community_invitation_unavailable_error")
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
            .let(::addDisposable)
    }

    fun subscribeRoomRT(room: AmityRoom) {
        room
            .subscription(events = AmityRoomEvents.VIEWER)
            .subscribeTopic()
            .doOnComplete {
            }
            .doOnError {
            }
            .onErrorComplete()
            .andThen(
                post
                    .subscription(events = AmityPostEvents.LIVE_REACTION)
                    .subscribeTopic()
                    .onErrorComplete()
            )
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)

        room.observeInvitation()
            .doOnNext { invitation ->
                if (invitation.getInvitedUserId() == AmityCoreClient.getUserId()) {
                    _uiState.update { currentState ->
                        currentState.copy(invitation = invitation)
                    }
                }
            }
            .doOnError {
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
            .let { disposable.add(it) }
    }

    fun unsubscribeRoomRT(room: AmityRoom) {
        room
            .subscription(events = AmityRoomEvents.VIEWER)
            .unsubscribeTopic()
            .onErrorComplete()
            .andThen(
                post
                    .subscription(events = AmityPostEvents.LIVE_REACTION)
                    .unsubscribeTopic()
                    .onErrorComplete()
            )
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun subscribeRoomStreamerRT(room: AmityRoom, post: AmityPost) {
        room.subscription(events = AmityRoomEvents.STREAMER)
            .subscribeTopic()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    private fun getRoomFlow(post: AmityPost): Flowable<AmityRoom> {
        return (post.getChildren()
            .map { it.getData() }
            .find { it is AmityPost.Data.ROOM }
                as? AmityPost.Data.ROOM)
            ?.getRoomId()
            ?.let { roomId ->
                getRoomFlow(roomId = roomId)
            }
            ?: Flowable.empty()
    }

    private fun getRoomFlow(roomId: String): Flowable<AmityRoom> {
        return AmityVideoClient.newRoomRepository()
            .getRoom(roomId = roomId)
    }

    fun getNetworkConnectionStateFlow(): Flow<NetworkConnectionEvent> {
        return NetworkConnectionEventBus.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {

            }
    }

    fun addReaction() {
        AmityCoreClient.newReactionRepository()
    }

    val replyTo = mutableStateOf<AmityMessage?>(null)
    var showDeleteDialog = mutableStateOf(false)
    var targetDeletedMessage = mutableStateOf<AmityMessage?>(null)
    val messageListState = mutableStateOf<MessageListState>(MessageListState.INITIAL)

    fun dismissDeleteConfirmation() {
        targetDeletedMessage.value = null
        showDeleteDialog.value = false
    }

    private fun subscribeToSubChannel(subChannelId: String) {
        // random delay from 1 to 10
        val delay = (1..10).random().toLong()
        val channelRepo = AmityChatClient.newChannelRepository()
        channelRepo
            .joinChannel(subChannelId)
            .delay(delay, TimeUnit.SECONDS)
            .flatMapCompletable {
                it.subscription().subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .onErrorComplete()
            .subscribe()

        AmityChatClient
            .newSubChannelRepository()
            .getSubChannel(subChannelId)
            .firstOrError()
            .flatMapCompletable {
                it.subscription().subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .onErrorComplete()
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
        onError: (Throwable) -> Unit = {},
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
        onError: (Throwable) -> Unit = {},
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
        _uiState.value.channelId?.let(::unsubscribeToSubChannel)
    }

    fun observeLiveReactions(
        referenceId: String,
    ): Flow<List<AmityLiveReaction>> {
        return AmityCoreClient.newLiveReactionRepository()
            .getReactions(
                referenceId = referenceId,
                referenceType = AmityLiveReactionReferenceType.POST
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun fetchRecordedUrls(roomId: String) {
        fetchRecordedUrlsRelay.onNext(roomId)
    }

    fun observeRecordedUrls() {
        fetchRecordedUrlsRelay
            .distinctUntilChanged()
            .flatMapSingle { roomId ->
                AmityVideoClient.newRoomRepository()
                    .getRecordedUrls(roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { urls ->
                        _uiState.update { currentState ->
                            if (currentState.room?.getRoomId() == roomId) {
                                currentState.copy(
                                    recordedUrls = urls,
                                )
                            } else {
                                currentState
                            }
                        }
                    }
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(disposable::add)
    }

    fun joinRoom(
        room: AmityRoom,
        post: AmityPost,
        onJoinedCompleted: (AmityRoomBroadcastData, CoroutineScope) -> Unit,
        onJoinedFailed: (String) -> Unit,
        //descriptionUserMentions: List<AmityMentionMetadata.USER>,
    ) {
        Log.d(TAG, "joinRoom: fetching broadcaster data for roomId=${room.getRoomId()}")
        getRoomBroadcasterData(room.getRoomId())
            .doOnSuccess { broadcastData ->

                val coHostData = broadcastData as? AmityRoomBroadcastData.CoHosts
                // Log token presence/length only (never the raw token) to diagnose a stale or
                // empty co-host token on the second join without leaking credentials.
                Log.d(
                    TAG,
                    "joinRoom: broadcaster data received roomId=${room.getRoomId()} " +
                            "isCoHosts=${coHostData != null} " +
                            "tokenLength=${coHostData?.getCoHostToken()?.length ?: 0} " +
                            "url=${coHostData?.getCoHostUrl()}"
                )
                // Decode the LiveKit JWT payload (signature omitted) so join-1 vs join-2 tokens
                // can be compared claim-by-claim — iat/exp/nbf/jti reveal whether the backend is
                // re-issuing a fresh grant or returning a stale/revoked token that LiveKit 401s.
                coHostData?.getCoHostToken()?.let { logTokenClaims(token = it) }
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            broadcasterData = broadcastData
                        )
                    }
                }
                subscribeRoomStreamerRT(room, post)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "joinRoom: broadcaster data ready, invoking onJoinedCompleted roomId=${room.getRoomId()}")
                onJoinedCompleted.invoke(it, viewModelScope)
            }, { error ->
                Log.e(TAG, "joinRoom: failed to fetch broadcaster data roomId=${room.getRoomId()}", error)
                onJoinedFailed(error.message ?: "")
            })
            .let(::addDisposable)
    }

    private fun getRoomBroadcasterData(roomId: String): Single<AmityRoomBroadcastData> {
        return AmityVideoClient.newRoomRepository()
            .getBroadcasterData(roomId = roomId)
            .subscribeOn(Schedulers.io())
    }

    fun leaveRoom(
        roomId: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        Log.d(TAG, "leaveRoom: leaving roomId=$roomId")
        AmityVideoClient.newRoomRepository()
            .leaveRoom(roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Log.d(TAG, "leaveRoom: completed roomId=$roomId")
                AmityUIKitSnackbar.publishSnackbarMessage(
                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_left_stage")
                )
                onSuccess()
            }
            .doOnError {
                Log.e(TAG, "leaveRoom: failed roomId=$roomId", it)
                AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_something_went_wrong"))
                onError()
            }
            .subscribe()
            .let(::addDisposable)
    }

    /**
     * Decodes and logs the payload (claims) of a LiveKit JWT co-host token for debugging.
     *
     * Only the middle (payload) segment is base64url-decoded — the signature is never
     * touched and no verification is performed. Compare the emitted `iat`/`exp`/`nbf`/`jti`
     * across the first (working) and second (401) join: identical `iat`/`jti` means the
     * backend is handing back a stale/cached token, differing claims point at a grant or
     * session that LiveKit has already revoked.
     */
    private fun logTokenClaims(token: String) {
        try {
            val parts = token.split(".")
            if (parts.size < 2) {
                Log.w(TAG, "logTokenClaims: token is not a JWT (segments=${parts.size})")
                return
            }
            val payload = String(
                android.util.Base64.decode(
                    parts[1],
                    android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP
                )
            )
            Log.d(TAG, "logTokenClaims: JWT payload = $payload")

            // Compute time-to-expiry against the device clock so the token TTL can be read
            // directly, without depending on the device's timezone offset. A value at or
            // below zero (or only a few seconds) means the LiveKit region-settings validation
            // can land after expiry on a slightly slower join -> the 401 seen on rejoin.
            val exp = org.json.JSONObject(payload).optLong("exp", 0L)
            if (exp > 0L) {
                val nowSeconds = System.currentTimeMillis() / 1000L
                Log.d(
                    TAG,
                    "logTokenClaims: exp=$exp nowEpoch=$nowSeconds secondsUntilExpiry=${exp - nowSeconds}"
                )
            }
        } catch (e: Throwable) {
            Log.w(TAG, "logTokenClaims: failed to decode token payload", e)
        }
    }

    fun onLiveKitRoomChange(room: Room? = null) {
        Log.d(TAG, "onLiveKitRoomChange: room=${if (room == null) "null" else "instance@${System.identityHashCode(room)} state=${room.state}"}")
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    liveKitRoom = room
                )
            }
        }
    }

    /**
     * Clears the LiveKit room reference and broadcaster credentials.
     *
     * Called when the local user leaves the stage (or is removed). LiveKit's [RoomScope]
     * disconnects and releases the underlying [Room] when [AmityStreamerView] leaves
     * composition, but the previously reported instance and the stale co-host token stay
     * in [RoomPlayerState] otherwise. Reusing them on a subsequent join makes
     * [Room.connect] target a released room / rejected token, which surfaces as the
     * LiveKit "Could not fetch region settings: 401" crash. Resetting here forces the next
     * join to use the freshly created room and a freshly fetched token.
     */
    fun resetLiveKitState() {
        Log.d(TAG, "resetLiveKitState: clearing stale liveKitRoom and broadcasterData")
        _uiState.update { currentState ->
            currentState.copy(
                liveKitRoom = null,
                broadcasterData = null,
            )
        }
    }

    fun acceptInvitation(invitation: AmityInvitation) {
        invitation.accept()
            .doOnComplete {
                _uiState.update { currentState ->
                    currentState.copy(
                        cohostUser = invitation.getInvitedUser()
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let(::addDisposable)
    }

    fun rejectInvitation(
        invitation: AmityInvitation,
        onSuccess: () -> Unit,
    ) {
        invitation.reject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess()
            }
            .doOnError {

            }
            .subscribe()
            .let(::addDisposable)
    }

    fun setIsLeaving(isLeaving: Boolean) {
        _uiState.update { it.copy(isLeaving = isLeaving) }
    }

    fun setIsStreamerMode(isStreamerMode: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val leavingStreamerMode = currentState.isStreamerMode && !isStreamerMode
                if (leavingStreamerMode) {
                    Log.d(TAG, "setIsStreamerMode: leaving streamer mode, dropping stale room + token")
                    // If switching from streamer to viewer, change observing online user count interval to 20s
                    currentState
                        .room
                        ?.getRoomId()
                        ?.let { roomId ->
                            observeOnlineUsersCount(
                                roomId = roomId,
                            )
                        }
                }
                currentState.copy(
                    isStreamerMode = isStreamerMode,
                    // Drop the released LiveKit room and stale co-host token when leaving the
                    // stage so the next join starts from a freshly created room + fresh token.
                    liveKitRoom = if (leavingStreamerMode) null else currentState.liveKitRoom,
                    broadcasterData = if (leavingStreamerMode) null else currentState.broadcasterData,
                )
            }
        }
    }

    fun startRoomHeartbeat(roomId: String) {
        heartbeatDisposable.clear()
        AmityCoreClient.newRoomPresenceRepository()
            .roomId(roomId = roomId)
            .startHeartbeat()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(heartbeatDisposable::add)

        observeOnlineUsersCount(roomId = roomId)
    }

    fun stopRoomHeartbeat(roomId: String) {
        heartbeatDisposable.clear()
        AmityCoreClient.newRoomPresenceRepository()
            .roomId(roomId = roomId)
            .stopHeartbeat()
    }

    fun observeOnlineUsersCount(roomId: String, interval: Int = 20) {
        roomPresenceDisposable.clear()
        AmityCoreClient.newRoomPresenceRepository()
            .roomId(roomId = roomId)
            .observeOnlineUsersCount()
            .subscribeOn(Schedulers.io())
            .doOnNext { count ->
                _uiState.update { currentState ->
                    currentState.copy(
                        viewerCount = count
                    )
                }
            }
            .subscribe()
            .let { subscription ->
                roomPresenceDisposable.add(subscription)
                addDisposable(subscription)
            }
    }

    fun onCameraPositionChange(cameraPosition: CameraPosition) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    cameraPosition = cameraPosition
                )
            }
        }
    }

    // Watch minute tracking functions

    /**
     * Start watch session tracking for viewer
     * Only call when user is in viewer mode (not co-host)
     * Room must be LIVE or RECORDED
     */
    fun startWatchTracking() {
        val room = _uiState.value.room ?: return
        val roomId = room.getRoomId()
        val roomStatus = room.getStatus()

        // Only track for LIVE or RECORDED rooms
        if (roomStatus != AmityRoomStatus.LIVE && roomStatus != AmityRoomStatus.RECORDED) {
            return
        }

        // Skip if already tracking this room (prevents duplicate sessions from LaunchedEffect re-triggers)
        if (watchSessionId != null && watchingRoomId == roomId) {
            return
        }

        // Stop any existing tracking
        stopWatchTracking()

        // Track which room we're watching
        watchingRoomId = roomId

        viewModelScope.launch {
            try {
                // Create watch session
                watchSessionStartTime = DateTime.now()
                isWatchingPaused = false
                accumulatedWatchTimeSeconds = 0
                lastResumeTime = DateTime.now()

                room.analytics().createWatchSession(watchSessionStartTime!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ sessionId ->
                        watchSessionId = sessionId

                        // Start 1-second interval updates
                        watchTrackingJob = viewModelScope.launch {
                            while (isActive) {
                                delay(1000) // 1 second
                                updateCurrentWatchSession()
                            }
                        }
                    }, { error ->
                        // Handle error silently - don't interrupt user experience
                        watchSessionId = null
                        watchSessionStartTime = null
                        isWatchingPaused = false
                        accumulatedWatchTimeSeconds = 0
                        lastResumeTime = null
                        watchingRoomId = null
                    })
                    .let { disposable.add(it) }
            } catch (e: Exception) {
                // Handle error silently - don't interrupt user experience
                watchSessionId = null
                watchSessionStartTime = null
                isWatchingPaused = false
                accumulatedWatchTimeSeconds = 0
                lastResumeTime = null
                watchingRoomId = null
            }
        }
    }

    fun addTaggedProducts(
        taggedProducts: List<AmityProduct>
    ) {
        val currentTaggedProduct = _uiState.value.getRoomPost()?.getProducts().orEmpty().map {
            AmityProductTag.Media(it.getProductId())
        }
        AmitySocialClient.newPostRepository()
            .editPost(
                postId = _uiState.value.getRoomPost()?.getPostId() ?: "",
            )
            .taggedProducts(
                productTags = (currentTaggedProduct + taggedProducts.map { AmityProductTag.Media(it.getProductId()) }).distinct(),
            )
            .build()
            .apply()
            .doOnError {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_product_tag_add_failed")
                )
            }
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tags_added")
                )
            }
            .subscribe()
    }

    fun removeTaggedProduct(productId: String) {
        // straight hit api
        AmitySocialClient.newPostRepository()
            .editPost(
                postId = _uiState.value.getRoomPost()?.getPostId() ?: "",
            )
            .taggedProducts(
                productTags = _uiState.value.getRoomPost()?.getProducts()
                    ?.filter { it.getProductId() != productId }
                    ?.map { AmityProductTag.Media(it.getProductId()) }
                    .orEmpty(),
            )
            .build()
            .apply()
            .doOnError {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_product_tag_remove_failed")
                )
            }
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tag_removed")
                )
            }
            .subscribe()
    }

    fun togglePinProduct(
        productId: String?
    ) {
        AmitySocialClient.newPostRepository()
            .let {
                if (productId == null) {
                    it.unpinProduct(
                        postId = _uiState.value.getRoomPost()?.getPostId() ?: ""
                    )
                } else {
                    it.pinProduct(
                        productId = productId,
                        postId = _uiState.value.getRoomPost()?.getPostId() ?: ""
                    )
                }
            }
            .doOnComplete {
                if (productId == null) {
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        offsetFromBottom = 50,
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tag_unpinned")
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        offsetFromBottom = 50,
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_product_tag_pinned")
                    )
                }
            }
            .doOnError {
                if (productId == null) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        offsetFromBottom = 50,
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_unpin_product_tag_failed")
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        offsetFromBottom = 50,
                        message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_pin_product_tag_failed")
                    )
                }
            }
            .subscribe()
            .let(::addDisposable)

    }

    fun fetchProductCatalogueSettings(
        onEnabledAction: (() -> Unit)? = null,
        onDisabledAction: (() -> Unit)? = null
    ) {
        AmityCoreClient.getProductCatalogueSetting()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { settings ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isProductCatalogueEnabled = settings.enabled
                    )
                }
                if (settings.enabled) {
                    onEnabledAction?.invoke()
                } else {
                    onDisabledAction?.invoke()
                }
            }
            .subscribe()
            .let(::addDisposable)
    }

    /**
     * Stop watch session tracking
     * Call when user becomes co-host or leaves the page
     */
    fun stopWatchTracking(shouldSync: Boolean = false) {
        watchTrackingJob?.cancel()
        watchTrackingJob = null

        // Final update before stopping
        if (watchSessionId != null) {
            viewModelScope.launch {
                try {
                    updateCurrentWatchSession()

                    if (shouldSync) {
                        // Sync pending sessions when leaving the page
                        _uiState.value.room?.analytics()?.syncPendingWatchSessions()
                    }
                } catch (e: Exception) {
                    // Handle error silently
                } finally {
                    watchSessionId = null
                    watchSessionStartTime = null
                    isWatchingPaused = false
                    accumulatedWatchTimeSeconds = 0
                    lastResumeTime = null
                    watchingRoomId = null
                }
            }
        }
    }

    /**
     * Update the current watch session with elapsed time
     * Only accumulates time when video is not paused
     * Uses synchronized block to prevent concurrent duplicate counting
     */
    private fun updateCurrentWatchSession() {
        synchronized(watchTrackingLock) {
            // Prevent concurrent updates
            if (isUpdatingSession) return

            val sessionId = watchSessionId ?: return
            val room = _uiState.value.room ?: return

            // Skip update if paused - only accumulate and send when playing
            if (isWatchingPaused) return

            isUpdatingSession = true

            try {
                // Accumulate time since last resume
                val lastResume = lastResumeTime ?: return
                val nowMillis = DateTime.now().millis
                val elapsedSinceResumeSeconds = ((nowMillis - lastResume.millis) / 1000).toInt()
                accumulatedWatchTimeSeconds += elapsedSinceResumeSeconds
                lastResumeTime = DateTime.now()

                room.analytics()
                    .updateWatchSession(
                        sessionId = sessionId,
                        duration = accumulatedWatchTimeSeconds.toLong(),
                        endedAt = DateTime.now()
                    )
                    .subscribeOn(Schedulers.io())
                    .doOnError {
                        // ignore
                    }
                    .subscribe()
            } finally {
                isUpdatingSession = false
            }
        }
    }

    /**
     * Pause watch tracking when user pauses the video
     * Accumulates the elapsed time before pausing but does NOT sync
     * Sync only happens on page exit or becoming co-host
     */
    fun pauseWatchTracking() {
        synchronized(watchTrackingLock) {
            if (watchSessionId == null || isWatchingPaused) return

            // Accumulate time up to this point (no sync, just save locally)
            lastResumeTime?.let {
                val nowMillis = DateTime.now().millis
                val elapsedSinceResumeSeconds = ((nowMillis - it.millis) / 1000).toInt()
                accumulatedWatchTimeSeconds += elapsedSinceResumeSeconds
            }

            isWatchingPaused = true
            lastResumeTime = null
        }
    }

    /**
     * Resume watch tracking when user resumes the video
     * Restarts the timer from current time
     */
    fun resumeWatchTracking() {
        synchronized(watchTrackingLock) {
            if (watchSessionId == null || !isWatchingPaused) return

            isWatchingPaused = false
            lastResumeTime = DateTime.now()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Stop tracking and sync when ViewModel is destroyed
        stopWatchTracking(shouldSync = true)
    }

    companion object {
        // Shared tag with the SDK (AmityCohostTrace) so the whole co-host join/leave
        // flow can be filtered end-to-end in logcat.
        private const val TAG = "AmityCohostTrace"

        fun create(post: AmityPost): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AmityRoomPlayerViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return AmityRoomPlayerViewModel(post) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class RoomPlayerState(
    val post: AmityPost,
    val room: AmityRoom? = null,
    val channelId: String? = null,
    val isBanned: Boolean? = null,
    val isPendingApproval: Boolean? = null,
    val reviewStatus: AmityReviewStatus? = null,
    val recordedUrls: List<String> = emptyList(),
    val invitation: AmityInvitation? = null,
    val error: Throwable? = null,
    val broadcasterData: AmityRoomBroadcastData? = null,
    val liveKitRoom: Room? = null,
    val roomModeration: AmityRoomModeration? = null,
    val isStreamerMode: Boolean = false,
    val isLeaving: Boolean = false,
    val hostUserId: String? = null,
    val hostUser: AmityUser? = null,
    val cohostUserId: String? = null,
    val cohostUser: AmityUser? = null,
    val viewerCount: Int? = null,
    val cameraPosition: CameraPosition = CameraPosition.FRONT,
    val isProductCatalogueEnabled: Boolean = false
) {

    fun getRoomPost() : AmityPost? {
        return post.getChildren().firstOrNull{ it.getData() is AmityPost.Data.ROOM }
    }

    fun isCoHostCanManageProducts() : Boolean {
        return room?.getParticipants()?.find { it.userId == cohostUserId }?.canManageProductTags == true
    }

    companion object {
        fun initial(post: AmityPost) = RoomPlayerState(
            post = post,
            reviewStatus = post.getReviewStatus()
        )
    }
}