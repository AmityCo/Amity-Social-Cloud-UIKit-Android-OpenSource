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

class AmityDraftStoryViewModel : AmityBaseViewModel() {

    fun createImageStory(
        targetId: String,
        targetType: AmityStory.TargetType,
        fileUri: Uri,
        imageDisplayMode: AmityStoryImageDisplayMode,
        hyperlinkUrlText: String,
        hyperlinkCustomText: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        val storyItems = mutableListOf<AmityStoryItem>()
        if (hyperlinkUrlText.isNotEmpty()) {
            storyItems.add(
                AmityStoryItem.HYPERLINK(
                    url = hyperlinkUrlText,
                    customText = hyperlinkCustomText.takeIf { it.isNotEmpty() },
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
        hyperlinkUrlText: String,
        hyperlinkCustomText: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        val storyItems = mutableListOf<AmityStoryItem>()
        if (hyperlinkUrlText.isNotEmpty()) {
            storyItems.add(
                AmityStoryItem.HYPERLINK(
                    url = hyperlinkUrlText,
                    customText = hyperlinkCustomText.takeIf { it.isNotEmpty() },
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