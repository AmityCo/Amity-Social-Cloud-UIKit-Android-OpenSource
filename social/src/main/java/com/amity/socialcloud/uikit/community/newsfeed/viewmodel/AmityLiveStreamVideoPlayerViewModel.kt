package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers


class AmityLiveStreamVideoPlayerViewModel :
    AmityBaseViewModel() {

    var streamId: String = ""

    fun checkStreamStatus(
        onValidStatus: (isLive: Boolean) -> Unit,
        onInvalidStatus: () -> Unit
    ): Completable {
        AmityVideoClient.newStreamRepository().fetchStream(streamId).subscribe()
        return AmityVideoClient.newStreamRepository()
            .getStream(streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged { old, new ->
                old.getStatus() == new.getStatus()
            }
            .doOnNext {
                when(it.getStatus()) {
                    AmityStream.Status.LIVE -> {
                        onValidStatus.invoke(true)
                    }
                    AmityStream.Status.RECORDED -> {
                        onValidStatus.invoke(false)
                    }
                    AmityStream.Status.IDLE -> {
                        onInvalidStatus.invoke()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
            .ignoreElements()
    }

    fun observeInvalidStream(
        onStreamEnded: () -> Unit,
        onStreamDeleted: () -> Unit
    ): Completable {
        return AmityVideoClient.newStreamRepository()
            .getStream(streamId)
            .filter { it.getStatus() == AmityStream.Status.ENDED || it.isDeleted() }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.isDeleted()) {
                    onStreamDeleted.invoke()
                } else if (it.getStatus() == AmityStream.Status.ENDED) {
                    onStreamEnded.invoke()
                }
            }
            .ignoreElement()
    }
}
