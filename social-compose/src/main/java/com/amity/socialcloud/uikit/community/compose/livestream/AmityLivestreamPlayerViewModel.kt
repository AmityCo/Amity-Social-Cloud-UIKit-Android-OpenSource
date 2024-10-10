package com.amity.socialcloud.uikit.community.compose.livestream

import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
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

class AmityLivestreamPlayerViewModel(private val post: AmityPost) :
    AmityBaseViewModel() {
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