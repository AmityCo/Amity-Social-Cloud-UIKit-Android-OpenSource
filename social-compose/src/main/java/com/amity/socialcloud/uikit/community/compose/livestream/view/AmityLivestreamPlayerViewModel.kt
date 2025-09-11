package com.amity.socialcloud.uikit.community.compose.livestream.view

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.chat.message.query.AmityMessageQuerySortOption
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.reaction.AmityLiveReactionReferenceType
import com.amity.socialcloud.sdk.model.core.reaction.live.AmityLiveReaction
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import com.amity.socialcloud.uikit.community.compose.livestream.chat.AmityLivestreamChatViewModel.MessageListState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityLivestreamPlayerViewModel(private val post: AmityPost) : AmityBaseViewModel() {
    val disposable = CompositeDisposable()

    private val  _liveStreamFullScreenState by lazy {
        MutableStateFlow(LiveStreamFullScreenState.initial(post))
    }

    val liveStreamFullScreenState get() = _liveStreamFullScreenState

    init {
        refresh()
    }

    fun refresh() {
        disposable.clear()
        _liveStreamFullScreenState.value = LiveStreamFullScreenState.initial(post)
        getPost(post.getPostId())
        observeStream()
    }

    private fun observeStream() {
        getStreamFlow(post)
            .map { it.getStreamId() }
            .distinctUntilChanged()
            .flatMap { streamId ->
                AmityVideoClient.newStreamRepository()
                    .getStream(streamId)
            }
            .doOnNext { stream ->
                _liveStreamFullScreenState.value =
                    _liveStreamFullScreenState.value.copy(
                        stream = stream,
                        isBanned = stream.isBanned(),
                    )
            }
            .doOnError { error ->
                _liveStreamFullScreenState.value =
                    _liveStreamFullScreenState.value.copy(error = error)
            }
            .map { stream ->
                if (stream.getStatus() != AmityStream.Status.RECORDED) {
                    stream.getChannelId() ?: ""
                } else {
                    ""
                }
            }
            .distinctUntilChanged()
            .doOnNext { channelId ->
                if (channelId.isNotEmpty()) {
                    _liveStreamFullScreenState.value =
                        _liveStreamFullScreenState.value.copy(channelId = channelId)
                    subscribeToSubChannel(channelId)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let { disposable.add(it) }
    }

    private fun getPost(postId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(postId)
                .asFlow()
                .catch { error ->
                    _liveStreamFullScreenState.update { currentState ->
                        currentState.copy(error = error)
                    }
                }
                .collectLatest { post ->
                    _liveStreamFullScreenState.update { currentState ->
                        currentState.copy(
                            post = post,
                            reviewStatus = post.getReviewStatus(),
                        )
                    }
                }
        }
    }

    fun subscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST).subscribeTopic()
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun unSubscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST).unsubscribeTopic()
            .subscribeOn(Schedulers.io()).subscribe()
    }

    private fun getStreamFlow(post: AmityPost): Flowable<AmityStream> {
        return (post.getChildren().firstOrNull()?.getData() as? AmityPost.Data.LIVE_STREAM)
            ?.getStream()
            ?: Flowable.empty()
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
//            .doOnComplete {
//                AmityUIKitSnackbar.publishSnackbarErrorMessage(
//                    message = "Subscribed to channel: $subChannelId with delay: $delay s",
//                    offsetFromBottom = 130
//                )
//            }
//            .doOnError {
//                AmityUIKitSnackbar.publishSnackbarErrorMessage(
//                    message = "Error subscribing to channel: $subChannelId with delay: $delay s",
//                    offsetFromBottom = 130
//                )
//            }
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
//            .doOnComplete {
//                AmityUIKitSnackbar.publishSnackbarErrorMessage(
//                    message = "Subscribed to subchannel: $subChannelId with delay: $delay s",
//                    offsetFromBottom = 130
//                )
//            }
//            .doOnError {
//                AmityUIKitSnackbar.publishSnackbarErrorMessage(
//                    message = "Error subscribing to subchannel: $subChannelId with delay: $delay s",
//                    offsetFromBottom = 130
//                )
//            }
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
        _liveStreamFullScreenState.value.channelId?.let(::unsubscribeToSubChannel)
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

    companion object {
        fun create(post: AmityPost): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AmityLivestreamPlayerViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return AmityLivestreamPlayerViewModel(post) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class LiveStreamFullScreenState(
    val post: AmityPost,
    val streamId: String? = null,
    val stream: AmityStream? = null,
    val channelId: String? = null,
    val isBanned: Boolean? = null,
    val isPendingApproval: Boolean? = null,
    val reviewStatus: AmityReviewStatus? = null,
    val error: Throwable? = null,
) {

    companion object {
        fun initial(post: AmityPost) = LiveStreamFullScreenState(
            post = post,
            reviewStatus = post.getReviewStatus()
        )
    }
}