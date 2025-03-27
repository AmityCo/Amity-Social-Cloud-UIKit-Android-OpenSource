package com.amity.socialcloud.uikit.community.compose.livestream.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

class AmityLivestreamPlayerViewModel(private val post: AmityPost) : AmityBaseViewModel() {
    val disposable = CompositeDisposable()

    private val _liveStreamFullScreenState by lazy {
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
                    _liveStreamFullScreenState.value.copy(stream = stream)
            }
            .doOnError { error ->
                _liveStreamFullScreenState.value =
                    _liveStreamFullScreenState.value.copy(error = error)
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
                            post = post
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
    val error: Throwable? = null,
) {

    companion object {
        fun initial(post: AmityPost) = LiveStreamFullScreenState(
            post = post,
        )
    }
}