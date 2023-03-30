package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val SAVED_VIDEO_POST_ID = "SAVED_VIDEO_POST_ID"
private const val SAVED_VIDEO_POSITION = "SAVED_VIDEO_POSITION"

class AmityVideoPostPlayerViewModel(private val savedState: SavedStateHandle) :
    AmityBaseViewModel() {

    var videoDataList: List<AmityPost.Data.VIDEO> = listOf()

    var postId = ""
        set(value) {
            savedState.set(SAVED_VIDEO_POST_ID, value)
            field = value
        }

    var videoPos = 0
        set(value) {
            savedState.set(SAVED_VIDEO_POSITION, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_VIDEO_POST_ID)?.let { postId = it }
        savedState.get<Int>(SAVED_VIDEO_POSITION)?.let { videoPos = it }
    }

    fun getVideoData(onVideoDataReady: (List<AmityPost.Data.VIDEO>) -> Unit): Completable {
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
            .firstOrError()
            .map {
                val videoDataList = mutableListOf<AmityPost.Data.VIDEO>()
                it.getChildren().forEach { childPost ->
                    when (val postData = childPost.getData()) {
                        is AmityPost.Data.VIDEO -> {
                            videoDataList.add(postData)
                        }
                        else -> {}
                    }
                }
                this.videoDataList = videoDataList
                return@map videoDataList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { onVideoDataReady(it) }
            .ignoreElement()
    }
}