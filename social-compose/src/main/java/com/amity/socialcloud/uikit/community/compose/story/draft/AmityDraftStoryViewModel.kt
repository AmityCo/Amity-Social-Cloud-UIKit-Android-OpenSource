package com.amity.socialcloud.uikit.community.compose.story.draft

import android.net.Uri
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityDraftStoryViewModel : AmityBaseViewModel() {

    fun createImageStory(
        communityId: String,
        fileUri: Uri,
        imageDisplayMode: AmityStoryImageDisplayMode,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        AmitySocialClient.newStoryRepository()
            .createImageStory(
                targetType = AmityStory.TargetType.COMMUNITY,
                targetId = communityId,
                imageUri = fileUri,
                imageDisplayMode = imageDisplayMode
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
        communityId: String,
        fileUri: Uri,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        AmitySocialClient.newStoryRepository()
            .createVideoStory(
                targetType = AmityStory.TargetType.COMMUNITY,
                targetId = communityId,
                videoUri = fileUri
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError {
                onError((it as AmityException).message ?: "Unknown error occurred.")
            }.subscribe()
    }
}