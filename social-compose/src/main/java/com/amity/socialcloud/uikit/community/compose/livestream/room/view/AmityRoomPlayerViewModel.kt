package com.amity.socialcloud.uikit.community.compose.livestream.room.view

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
                            AmityUIKitSnackbar.publishSnackbarMessage(message = "Co-host left the live stream.")
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                invitation = null,
                                cohostUserId = null,
                                cohostUser = null,
                                isStreamerMode = if (event is AmityCoHostEvent.CoHostRemoved) {
                                    false
                                } else {
                                    currentState.isStreamerMode
                                }
                            )
                        }
                    }
                    is AmityCoHostEvent.CoHostPermissionUpdated -> {
                        if (event.cohostCanManageProduct) {
                            AmityUIKitSnackbar.publishSnackbarMessage(
                                message = "You can now manage tagged products in this live stream."
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
                                    message = "This invitation is no longer available."
                                )
                            }
                        }
                    } ?: if (wasInvited) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = "This invitation is no longer available."
                    )
                } else {
                }
            }
            .doOnError {
                if (wasInvited) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = "This invitation is no longer available."
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
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
            .subscribe()
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
        getRoomBroadcasterData(room.getRoomId())
            .doOnSuccess { broadcastData ->

                val coHostData = broadcastData as? AmityRoomBroadcastData.CoHosts
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
                onJoinedCompleted.invoke(it, viewModelScope)
            }, { error ->
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
        AmityVideoClient.newRoomRepository()
            .leaveRoom(roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    message = "You left the stage and are now watching as a viewer."
                )
                onSuccess()
            }
            .doOnError {
                AmityUIKitSnackbar.publishSnackbarErrorMessage("Something went wrong. Please try again.")
                onError()
            }
            .subscribe()
            .let(::addDisposable)
    }

    fun onLiveKitRoomChange(room: Room? = null) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    liveKitRoom = room
                )
            }
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

    fun setIsStreamerMode(isStreamerMode: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState.isStreamerMode && !isStreamerMode) {
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
                    isStreamerMode = isStreamerMode
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
                    message = "Failed to add product tags. Please try again."
                )
            }
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = "Product tags added."
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
                    message = "Failed to remove product tag. Please try again."
                )
            }
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = "Product tag removed."
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
                        message = "Product tag unpinned."
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        offsetFromBottom = 50,
                        message = "Product tag pinned."
                    )
                }
            }
            .doOnError {
                if (productId == null) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        offsetFromBottom = 50,
                        message = "Failed to unpin product tag. Please try again."
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        offsetFromBottom = 50,
                        message = "Failed to pin product tag. Please try again."
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