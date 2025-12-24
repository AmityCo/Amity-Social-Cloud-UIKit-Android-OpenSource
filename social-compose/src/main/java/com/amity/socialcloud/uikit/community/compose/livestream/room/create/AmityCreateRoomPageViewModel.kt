package com.amity.socialcloud.uikit.community.compose.livestream.room.create

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.AmityCoHostEvent
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.core.events.AmityRoomEvents
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitationStatus
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.core.reaction.live.AmityLiveReaction
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.LivestreamThumbnailUploadUiState
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.model.AmityCreateRoomPageUiState
import com.google.gson.JsonObject
import io.livekit.android.room.Room
import io.livekit.android.room.track.CameraPosition
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.Duration
import java.util.concurrent.TimeUnit

class AmityCreateRoomPageViewModel constructor(val postId: String? = null) : AmityBaseViewModel() {
    private val _uiState = MutableStateFlow(AmityCreateRoomPageUiState())
    val uiState: StateFlow<AmityCreateRoomPageUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { _ ->
                AmityCreateRoomPageUiState(
                    isLive = false,
                    targetName = null,
                    communityId = null,
                    liveTitle = null,
                    liveDesc = null,
                    createPostId = null,
                    thumbnailId = null,
                    postId = postId,
                    channelId = null,
                    error = null,
                    thumbnailUploadUiState = LivestreamThumbnailUploadUiState.Idle,
                    isPreparingInitialData = postId != null,
                )
            }
        }
        observeNetwork()
        postId?.let(::getRoomPost)
    }

    fun setupScreen(communityId: String? = null) {
        communityId?.let {
            getCommunity(it)
        } ?: run {
            getCurrentUser()
        }
    }

    private fun observeNetwork() {
        NetworkConnectionEventBus.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            networkConnection = it
                        )
                    }
                }
            }
            .subscribe()
    }

    private fun getCommunity(communityId: String) {
        viewModelScope.launch {
            addDisposable(
                AmitySocialClient.newCommunityRepository()
                    .getCommunity(communityId)
                    .firstOrError()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        updateTargetInformation(
                            name = it.getDisplayName(),
                            communityId = it.getCommunityId()
                        )
                    }
                    .subscribe()
            )
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            addDisposable(
                AmityCoreClient.getCurrentUser()
                    .firstOrError()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { user ->
                        user.getDisplayName()?.let {
                            updateTargetInformation(name = "My timeline", userId = user.getUserId())
                        }
                    }
                    .subscribe()
            )
        }
    }

    fun createRoomPost(
        title: String,
        description: String,
        isReadOnly: Boolean,
        onCreateCompleted: (AmityRoomBroadcastData, CoroutineScope) -> Unit,
        onCreateFailed: (String) -> Unit,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                channelId = null
            )
        }

        var roomId: String? = null
        createRoom(title, description)
            .doOnSuccess { room ->
                roomId = room.getRoomId()
                observeRoom(room.getRoomId())
            }
            .doOnError { }
            .flatMap { room ->
                _uiState.update { currentState ->
                    currentState.copy(
                        room = room
                    )
                }
                createPost(room)
                    .map { post ->
                        Pair(room, post)
                    }
            }
            .flatMap { roomAndPost ->
                onRoomPostReady(
                    room = roomAndPost.first,
                    post = roomAndPost.second,
                    isReadOnly = isReadOnly,
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onCreateCompleted.invoke(it, viewModelScope)
            }, { error ->
                onCreateFailed(error.message ?: "")
            })
            .let(::addDisposable)
    }

    fun getRoomPost(
        postId: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                channelId = null,
                postId = postId,
            )
        }

        AmitySocialClient.newPostRepository()
            .getPost(postId = postId)
            .distinctUntilChanged { old, new ->
                old.getPostId() == new.getPostId()
            }
            .doOnNext { post ->
                post.getChildren()
                    .firstOrNull {
                        it.getData() is AmityPost.Data.ROOM
                    }
                    ?.let {
                        it.getData() as AmityPost.Data.ROOM
                    }
                    ?.getRoom()
                    ?.let { room ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                room = room,
                                post = post,
                                isPreparingInitialData = false,
                            )
                        }
                    }
                    ?.let { room->
                        Pair(room, post)
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let(::addDisposable)
    }

    fun startRoom(
        room: AmityRoom,
        post: AmityPost,
        isReadOnly: Boolean,
        onCreateCompleted: (AmityRoomBroadcastData, CoroutineScope) -> Unit,
        onCreateFailed: (String) -> Unit,
    ) {
        onRoomPostReady(
            room = room,
            post = post,
            isReadOnly = isReadOnly,
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onCreateCompleted.invoke(it, viewModelScope)
            }, { error ->
                onCreateFailed(error.message ?: "")
            })
            .let(::addDisposable)
    }

    private fun onRoomPostReady(
        room: AmityRoom,
        post: AmityPost,
        isReadOnly: Boolean,
    ): Single<AmityRoomBroadcastData> {
        return Single.just(Pair(room, post))
            .doOnSuccess { roomAndPost ->
                _uiState.update { currentState ->
                    currentState.copy(
                        postId = roomAndPost.second.getPostId()
                    )
                }
                getPost(roomAndPost.second.getPostId())
                subscribeRoomStreamerRT(roomAndPost.first, roomAndPost.second)
            }
            .flatMap {
                createLiveChannel(
                    livestreamTitle = room.getTitle() ?: "No title",
                    postId = post.getPostId(),
                    roomId = room.getRoomId()
                )
            }
            .doOnSuccess { channel ->
                _uiState.update { currentState ->
                    currentState.copy(
                        channelId = channel.getChannelId()
                    )
                }
                subscribeChannelRT(channel)
            }
            .flatMap { channel ->
                if (isReadOnly) {
                    AmityChatClient.newChannelRepository()
                        .muteChannel(
                            channelId = channel.getChannelId(),
                            timeout = Duration.standardDays(7)
                        )
                        .andThen(Single.just(true))
                } else {
                    Single.just(true)
                }
            }
            .flatMap {
                room.getRoomId().let(::getRoomBroadcasterData)
            }
            .doOnSuccess { broadcastData ->
                room.getRoomId().let(::observeCohostInvitationStatus)
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            broadcasterData = broadcastData
                        )
                    }
                }
                room.getRoomId().let(::startObserveViewerCount)
            }
    }

    fun inviteCohost(userId: String, user: AmityUser?) {
        viewModelScope.launch {
            try {
                _uiState.run {
                    value.room
                        ?.createInvitation(userId = userId)
                        ?.await()

                    AmityUIKitSnackbar.publishSnackbarMessage(
                        message = "Co-host invitation successfully sent."
                    )
//                    if (user == null) {
//                        fetchCohostUser(userId)
//                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        cohostUserId = null,
                        cohostUser = null
                    )
                }
            }
        }
    }

    fun cancelInvitation(invitation: AmityInvitation) {
        invitation
            .cancel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    message = "Co-host invitation cancelled."
                )
            }
            .subscribe()
            .let(::addDisposable)
    }

    private fun observeCohostInvitationStatus(roomId: String) {
        AmityCoreClient.newInvitationRepository()
            .getInvitations(
                targetId = roomId,
                targetType = AmityInvitation.TargetType.ROOM.value
            )
            .doOnNext { invitations ->
                invitations
                    .firstOrNull()
                    ?.let { invitation ->
                        if (invitation.getStatus() == AmityInvitationStatus.PENDING) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    cohostUserId = invitation.getInvitedUserId(),
                                    cohostUser = invitation.getInvitedUser(),
                                    invitation = invitation,
                                )
                            }
                        } else if (invitation.getStatus() == AmityInvitationStatus.CANCELLED) {
                            _uiState.update {
                                it.copy(
                                    cohostUserId = null,
                                    cohostUser = null,
                                    invitation = null,
                                )
                            }
                        } else if (invitation.getStatus() == AmityInvitationStatus.REJECTED) {
                            AmityUIKitSnackbar.publishSnackbarMessage(
                                message = "Co-host declined the invitation."
                            )
                            _uiState.update {
                                it.copy(
                                    cohostUserId = null,
                                    cohostUser = null,
                                    invitation = null,
                                )
                            }
                        } else if (invitation.getStatus() == AmityInvitationStatus.APPROVED) {
                            _uiState.update {
                                it.copy(
                                    invitation = null,
                                    cohostUserId = invitation.getInvitedUserId(),
                                    cohostUser = invitation.getInvitedUser(),
                                )
                            }
                        }
                    }

            }
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    private fun getPost(postId: String) {
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
                            isPendingApproval = post.getReviewStatus() != AmityReviewStatus.PUBLISHED
                        )
                    }
                }
        }
    }

    fun subscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST).subscribeTopic()
            .andThen(
                post.subscription(events = AmityPostEvents.LIVE_REACTION)
                    .subscribeTopic()
            )
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun unSubscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST).unsubscribeTopic()
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun subscribeRoomStreamerRT(room: AmityRoom, post: AmityPost) {
        room.subscription(events = AmityRoomEvents.STREAMER)
            .subscribeTopic()
            .onErrorComplete()
            .andThen(
                room
                    .subscription(events = AmityRoomEvents.VIEWER)
                    .subscribeTopic()
                    .onErrorComplete()
            )
            .andThen(
                post
                    .subscription(events = AmityPostEvents.LIVE_REACTION)
                    .subscribeTopic()
                    .onErrorComplete()
            )
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun subscribeChannelRT(channel: AmityChannel) {
        // random delay from 1 to 10
        val delay = (1..10).random().toLong()
        Single.just(true)
            .delay(delay,TimeUnit.SECONDS)
            .flatMapCompletable {
                channel.subscription()
                    .subscribeTopic()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        val subChannelId = channel.getChannelId()
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

    fun setIsMutedChannel(channelId: String, isMuted: Boolean) {
        AmityChatClient.newChannelRepository()
            .let { repository ->
                if (isMuted) {
                    repository.muteChannel(
                        channelId = channelId,
                        timeout = Duration.standardDays(7)
                    )
                } else {
                    repository.unmuteChannel(channelId = channelId)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun createStream(title: String, description: String): Single<AmityStream> {
        return AmityVideoClient.newStreamRepository()
            .createStream(
                title = title,
                description = description,
                resolution = AmityBroadcastResolution.HD_720P,
                thumbnailImage = _uiState.value.thumbnailImage,
                channelEnabled = true,
            )
    }

    private fun createRoom(title: String, description: String): Single<AmityRoom> {
        return AmityVideoClient.newRoomRepository()
            .createRoom(
                title = title,
                description = description,
                liveChatEnabled = true,
            )
    }

    private fun createLiveChannel(livestreamTitle: String, postId: String, roomId: String): Single<AmityChannel> {
        return AmityChatClient.newChannelRepository()
            .createChannel(livestreamTitle)
            .live()
            .postId(postId = postId)
            .roomId(roomId = roomId)
            .build()
            .create()
            .doOnSuccess { channel ->
                updateChannelMetadata(
                    channel = channel,
                    updateMetadata = addUserToMetadataList("moderators", AmityCoreClient.getUserId()),
                    onSuccess = {},
                    onError = {},
                )
            }
    }

    private fun updateChannelMetadata(
        channel: AmityChannel,
        updateMetadata: (JsonObject) -> JsonObject,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val currentMetadata = channel.getMetadata() ?: JsonObject()
        val updatedMetadata = updateMetadata(currentMetadata)

        AmityChatClient.newChannelRepository()
            .editChannel(channel.getChannelId())
            .metadata(updatedMetadata)
            .build()
            .apply()
            .ignoreElement()
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

    private fun createPost(
        room: AmityRoom,
        //descriptionUserMentions: List<AmityMentionMetadata.USER>,
    ): Single<AmityPost> {
        //val userMentions = getUserMentions(stream, descriptionUserMentions)
        return if (_uiState.value.communityId == null) {
            createUserSelfPost(
                room = room,
                //userMentions = userMentions
            )
        } else {
            createCommunityPost(
                room = room,
                //userMentions = userMentions
            )
        }
    }

    private fun createCommunityPost(
        room: AmityRoom,
        //userMentions: List<AmityMentionMetadata.USER>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createRoomPost(
                targetType = AmityPost.TargetType.COMMUNITY,
                targetId = _uiState.value.communityId ?: "",
                roomId = room.getRoomId(),
                title = _uiState.value.liveTitle,
                text = _uiState.value.liveDesc ?: "",
                //, metadata = null, mentionUserIds = setOf()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setPostId(it.getPostId()) }
    }

    private fun createUserSelfPost(
        room: AmityRoom,
        //userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createRoomPost(
                targetType = AmityPost.TargetType.USER,
                targetId = _uiState.value.userId ?: "",
                roomId = room.getRoomId(),
                title = _uiState.value.liveTitle,
                text = _uiState.value.liveDesc ?: "",
                //, metadata = null, mentionUserIds = setOf()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setPostId(it.getPostId()) }
    }

    private fun getRoomBroadcasterData(roomId: String): Single<AmityRoomBroadcastData> {
        return AmityVideoClient.newRoomRepository()
            .getBroadcasterData(roomId = roomId)
            .subscribeOn(Schedulers.io())
    }

    fun observeRoom(
        roomId: String,
    ) {
        AmityVideoClient.newRoomRepository()
            .getRoom(roomId)
            .doOnNext { room ->
                viewModelScope.launch {
                    _uiState.update { currentState ->
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
                    if (room.getModeration()?.terminateLabels?.isNotEmpty() == true
                    ) {
                        // TODO: Disconnect live kit when got moderation terminate labels
                    }
                }
            }.doOnError { error ->
                println(error)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let(::addDisposable)

        AmityVideoClient.newRoomRepository()
            .getCoHostEvent(roomId)
            .doOnNext { event ->
                when (event) {
                    is AmityCoHostEvent.CoHostInvited -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                cohostUserId = event.invitation.getInvitedUserId(),
                                cohostUser = event.invitation.getInvitedUser(),
                            )
                        }
                    }
                    is AmityCoHostEvent.CoHostInviteAccepted -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                cohostUserId = event.invitation.getInvitedUserId(),
                                cohostUser = event.invitation.getInvitedUser(),
                            )
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
                        _uiState.update { currentState ->
                            currentState.copy(
                                cohostUserId = null,
                                cohostUser = null,
                            )
                        }
                    }
                    else -> {

                    }
                }
                when (event) {
                    is AmityCoHostEvent.CoHostInviteRejected -> {
                        AmityUIKitSnackbar.publishSnackbarMessage(
                            message = "Co-host declined the invitation."
                        )
                    }
                    is AmityCoHostEvent.CoHostLeft -> {
                        val hostInternalId = event.room.getParticipants().firstOrNull { it.type == AmityRoom.ParticipantType.HOST }?.userInternalId
                        val actorInternalId = event.actorInternalId
                        if (hostInternalId != actorInternalId) {
                            AmityUIKitSnackbar.publishSnackbarMessage(
                                message = "Co-host left the stage."
                            )
                        }
                    }
                    is AmityCoHostEvent.CoHostInviteAccepted -> {
                        AmityUIKitSnackbar.publishSnackbarMessage(
                            message = "Co-host accepted the invitation."
                        )
                    }
                    else -> {

                    }
                }
            }
            .onErrorComplete()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(::addDisposable)
    }

    fun disposeRoom(roomId: String) {
        AmityVideoClient.newRoomRepository()
            .stopRoom(roomId = roomId)
            .onErrorComplete()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let(::addDisposable)
    }

    private fun setPostId(postId: String? = null) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    createPostId = postId
                )
            }
        }
    }

    fun removeParticipant(roomId: String, userId: String) {
        AmityVideoClient.newRoomRepository()
            .removeRoomParticipant(roomId = roomId, userId = userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                AmityUIKitSnackbar.publishSnackbarMessage("Your co-host was removed from your live stream.")
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

    fun onCameraPositionChange(cameraPosition: CameraPosition) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    cameraPosition = cameraPosition
                )
            }
        }
    }

    fun uploadThumbnail(uri: Uri, onUploadFailed: (Int?) -> Unit = {}) {
        viewModelScope.launch {
            if (_uiState.value.networkConnection is NetworkConnectionEvent.Connected) {
                updateThumbnailUploadState(
                    state = LivestreamThumbnailUploadUiState.Uploading,
                    thumbnailUri = uri
                )

                addDisposable(
                    AmityFileService().uploadImage(uri)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { uploadResult ->
                            when (uploadResult) {
                                is AmityUploadResult.ERROR -> {
                                    val error = uploadResult.getError().code
                                    onUploadFailed(error)
                                    removeThumbnail()
                                }

                                is AmityUploadResult.CANCELLED -> {
                                    onUploadFailed(null)
                                    removeThumbnail()
                                }

                                is AmityUploadResult.COMPLETE -> {
                                    updateThumbnailUploadState(
                                        state = LivestreamThumbnailUploadUiState.Success(
                                            uploadResult.getFile().getFileId()
                                        ),
                                        thumbnailUri = uri,
                                        thumbnailImage = uploadResult.getFile() as? AmityImage
                                    )
                                }

                                else -> {}
                            }
                        }
                        .doOnError {
                            onUploadFailed(null)
                            removeThumbnail()
                        }
                        .ignoreElements()
                        .subscribe()
                )
            } else {
                onUploadFailed(null)
                removeThumbnail()
            }
        }
    }

    private fun updateThumbnailUploadState(
        state: LivestreamThumbnailUploadUiState,
        thumbnailUri: Uri? = null,
        thumbnailImage: AmityImage? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    thumbnailUri = thumbnailUri,
                    thumbnailImage = thumbnailImage,
                    thumbnailId = if (state is LivestreamThumbnailUploadUiState.Success) state.thumbnailId else null,
                    thumbnailUploadUiState = state,
                )
            }
        }
    }

    private fun updateTargetInformation(
        name: String,
        userId: String? = null,
        communityId: String? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    targetName = name,
                    userId = userId,
                    communityId = communityId
                )
            }
        }
    }

    fun setTitleText(value: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    liveTitle = value
                )
            }
        }
    }

    fun setDescText(value: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    liveDesc = value
                )
            }
        }
    }

    fun removeThumbnail() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    thumbnailUri = null,
                    thumbnailId = null,
                    thumbnailUploadUiState = LivestreamThumbnailUploadUiState.Idle,
                )
            }
        }
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

    fun getRoomViewers(roomId: String) {
        AmityCoreClient.newRoomPresenceRepository()
            .roomId(roomId = roomId)
            .getOnlineUsersSnapshot()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _uiState.update { currentState ->
                    currentState.copy(
                        viewers = it.getUsers()
                    )
                }
            }
            .subscribe()
            .let(::addDisposable)
    }

    fun startObserveViewerCount(roomId: String) {
        AmityCoreClient.newRoomPresenceRepository()
            .roomId(roomId = roomId)
            .observeOnlineUsersCount(5)
            .subscribeOn(Schedulers.io())
            .doOnNext { count ->
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            viewerCount = count
                        )
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let(::addDisposable)
    }
}

