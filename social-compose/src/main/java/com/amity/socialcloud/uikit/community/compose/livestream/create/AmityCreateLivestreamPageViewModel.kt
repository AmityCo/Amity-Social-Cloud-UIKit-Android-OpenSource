package com.amity.socialcloud.uikit.community.compose.livestream.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.core.reaction.live.AmityLiveReaction
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.StreamBroadcaster
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.sdk.video.model.AmityStreamBroadcasterState
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.AmityCreateLivestreamPageUiState
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.LivestreamThumbnailUploadUiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
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

class AmityCreateLivestreamPageViewModel : AmityBaseViewModel() {
    private val _uiState = MutableStateFlow(AmityCreateLivestreamPageUiState())
    val uiState: StateFlow<AmityCreateLivestreamPageUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { _ ->
                AmityCreateLivestreamPageUiState(
                    isLive = false,
                    targetName = null,
                    communityId = null,
                    liveTitle = null,
                    liveDesc = null,
                    createPostId = null,
                    thumbnailId = null,
                    channelId = null,
                    error = null,
                    broadcasterState = AmityStreamBroadcasterState.IDLE(),
                    thumbnailUploadUiState = LivestreamThumbnailUploadUiState.Idle
                )
            }
        }
        observeNetwork()
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

    fun subscribeBroadcaster(streamBroadcaster: StreamBroadcaster) {
        viewModelScope.launch {
            addDisposable(
                streamBroadcaster.getStateFlowable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        updateStreamBroadCasterState(it)
                    }
                    .subscribe()
            )
        }
    }

    private fun updateStreamBroadCasterState(state: AmityStreamBroadcasterState) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    broadcasterState = state
                )
            }
        }
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

    fun createLiveStreamingPost(
        title: String,
        description: String,
        isReadOnly: Boolean,
        onCreateCompleted: (streamId: String?) -> Unit,
        onCreateFailed: (String) -> Unit,
        //descriptionUserMentions: List<AmityMentionMetadata.USER>,
    ) {
        updateStreamBroadCasterState(AmityStreamBroadcasterState.CONNECTING())
        _uiState.update { currentState ->
            currentState.copy(
                channelId = null
            )
        }

        var streamId: String? = null
        addDisposable(
            createStream(title, description)
                .doOnSuccess { stream ->
                    streamId = stream.getStreamId()
                    observeStream(stream.getStreamId())
                }
                .flatMap { stream ->
                    createPost(stream)
                }
                .doOnSuccess { post ->
                    getPost(post.getPostId())
                    subscribePostRT(post)
                }
                .flatMap { post ->
                    createLiveChannel(
                        livestreamTitle = title,
                        postId = post.getPostId(),
                        liveStreamId = streamId ?: ""
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
                .flatMapCompletable { channel ->
                    if (isReadOnly) {
                        AmityChatClient.newChannelRepository()
                            .muteChannel(
                                channelId = channel.getChannelId(),
                                timeout = Duration.standardDays(7)
                            )
                    } else {
                        Completable.complete()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onCreateCompleted.invoke(streamId)
                }, { error ->
                    onCreateFailed(error.message ?: "")
                    updateStreamBroadCasterState(AmityStreamBroadcasterState.IDLE())
                })
        )
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

    private fun createLiveChannel(livestreamTitle: String, postId: String, liveStreamId: String): Single<AmityChannel> {
        return AmityChatClient.newChannelRepository()
            .createChannel(livestreamTitle)
            .live()
            .postId(postId)
            .videoStreamId(liveStreamId)
            .build()
            .create()
    }

    private fun createPost(
        stream: AmityStream,
        //descriptionUserMentions: List<AmityMentionMetadata.USER>,
    ): Single<AmityPost> {
        //val userMentions = getUserMentions(stream, descriptionUserMentions)
        return if (_uiState.value.communityId == null) {
            createUserSelfPost(
                stream = stream,
                //userMentions = userMentions
            )
        } else {
            createCommunityPost(
                stream = stream,
                //userMentions = userMentions
            )
        }
    }

    private fun createCommunityPost(
        stream: AmityStream,
        //userMentions: List<AmityMentionMetadata.USER>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createLiveStreamPost(
                targetType = AmityPost.TargetType.COMMUNITY,
                targetId = _uiState.value.communityId ?: "",
                streamId = stream.getStreamId(),
                text = generatePostText(stream)
                //, metadata = null, mentionUserIds = setOf()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setPostId(it.getPostId()) }
    }

    private fun createUserSelfPost(
        stream: AmityStream,
        //userMentions: List<AmityMentionMetadata.USER>
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createLiveStreamPost(
                targetType = AmityPost.TargetType.USER,
                targetId = _uiState.value.userId ?: "",
                streamId = stream.getStreamId(),
                text = generatePostText(stream)
                //, metadata = null, mentionUserIds = setOf()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setPostId(it.getPostId()) }
    }

    fun observeStream(
        streamId: String,
    ) {
        addDisposable(
            AmityVideoClient.newStreamRepository()
                .getStream(streamId)
                .doOnNext { stream ->
                    viewModelScope.launch {
                        _uiState.update { currentState ->
                            currentState.copy(
                                streamObj = stream,
                                streamModeration = stream.getStreamModeration(),
                            )
                        }
                        if (stream.getStreamModeration()?.getTerminateLabels()
                                ?.isNotEmpty() == true
                        ) {
                            updateStreamBroadCasterState(AmityStreamBroadcasterState.DISCONNECTED())
                        }
                    }
                }.doOnError { error ->
                    println(error)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun generatePostText(stream: AmityStream): String {
        return if (stream.getTitle().isNullOrBlank()
            && stream.getDescription().isNullOrBlank()
        ) {
            ""
        } else {
            stream.getTitle().plus("\n\n").plus(stream.getDescription())
        }
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
}

