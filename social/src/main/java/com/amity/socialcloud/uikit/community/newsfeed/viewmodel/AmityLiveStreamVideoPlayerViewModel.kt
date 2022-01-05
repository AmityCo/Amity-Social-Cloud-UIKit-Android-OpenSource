package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.video.AmityVideoClient
import com.amity.socialcloud.sdk.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.stream.AmityWatcherData
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AmityLiveStreamVideoPlayerViewModel :
    AmityBaseViewModel() {

    var streamId: String = ""

    fun getVideoURL(
        onLoadURLSuccess: (String) -> Unit,
        onLoadURLError: () -> Unit
    ): Completable {
        AmityVideoClient.newStreamRepository().fetchStream(streamId).subscribe()
        return AmityVideoClient.newStreamRepository()
            .observeStream(streamId)
            .map {
                if (it.getStatus() == AmityStream.Status.LIVE) {
                    return@map it.getWatcherData()?.getUrl(AmityWatcherData.Format.FLV) ?: EMPTY_URL
                } else {
                    return@map EMPTY_URL
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it != EMPTY_URL) {
                    onLoadURLSuccess.invoke(it)
                } else {
                    onLoadURLError.invoke()
                }
            }
            .ignoreElements()
    }

    fun observeInvalidStream(
        onStreamEnded: () -> Unit,
        onStreamDeleted: () -> Unit
    ): Completable {
        return AmityVideoClient.newStreamRepository()
            .observeStream(streamId)
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

private const val EMPTY_URL = ""