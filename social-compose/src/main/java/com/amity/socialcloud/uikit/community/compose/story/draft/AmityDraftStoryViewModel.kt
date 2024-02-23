package com.amity.socialcloud.uikit.community.compose.story.draft

import android.net.Uri
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.sdk.model.social.story.AmityStoryItem
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow

class AmityDraftStoryViewModel : AmityBaseViewModel() {

    private val _hyperlinkUrl by lazy {
        MutableStateFlow("")
    }
    val hyperlinkUrl get() = _hyperlinkUrl

    private val _hyperlinkText by lazy {
        MutableStateFlow("")
    }
    val hyperlinkText get() = _hyperlinkText

    fun createImageStory(
        targetId: String,
        targetType: AmityStory.TargetType,
        fileUri: Uri,
        imageDisplayMode: AmityStoryImageDisplayMode,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        val storyItems = mutableListOf<AmityStoryItem>()
        if (_hyperlinkUrl.value.isNotEmpty()) {
            storyItems.add(
                AmityStoryItem.HYPERLINK(
                    url = _hyperlinkUrl.value,
                    customText = _hyperlinkText.value.takeIf { it.isNotEmpty() },
                )
            )
        }
        AmitySocialClient.newStoryRepository()
            .createImageStory(
                targetType = targetType,
                targetId = targetId,
                imageUri = fileUri,
                imageDisplayMode = imageDisplayMode,
                storyItems = storyItems,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError {
                onError((it as AmityException).message ?: "Unknown error occurred.")
            }
            .subscribe()
    }

    fun createVideoStory(
        targetId: String,
        targetType: AmityStory.TargetType,
        fileUri: Uri,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        val storyItems = mutableListOf<AmityStoryItem>()
        if (_hyperlinkUrl.value.isNotEmpty()) {
            storyItems.add(
                AmityStoryItem.HYPERLINK(
                    url = _hyperlinkUrl.value,
                    customText = _hyperlinkText.value.takeIf { it.isNotEmpty() },
                )
            )
        }
        AmitySocialClient.newStoryRepository()
            .createVideoStory(
                targetType = targetType,
                targetId = targetId,
                videoUri = fileUri,
                storyItems = storyItems,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError {
                onError((it as AmityException).message ?: "Unknown error occurred.")
            }.subscribe()
    }

    fun saveStoryHyperlinkItem(url: String, text: String) {
        _hyperlinkUrl.value = url
        _hyperlinkText.value = text
    }

    fun getTarget(
        targetId: String,
        targetType: AmityStory.TargetType
    ): Flowable<AmityStoryTarget> {
        return AmitySocialClient.newStoryRepository()
            .getStoryTarget(
                targetId = targetId,
                targetType = targetType
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}