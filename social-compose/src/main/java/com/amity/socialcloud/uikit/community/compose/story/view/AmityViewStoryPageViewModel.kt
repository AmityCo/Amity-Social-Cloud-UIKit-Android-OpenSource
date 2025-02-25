package com.amity.socialcloud.uikit.community.compose.story.view

import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.sdk.model.social.story.AmityStoryItem
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityViewStoryPageViewModel : AmityBaseViewModel() {

    private val _shouldPauseTimer by lazy {
        MutableStateFlow(false)
    }
    val shouldPauseTimer get() = _shouldPauseTimer

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityStoryModalSheetUIState>(AmityStoryModalSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    private val _dialogUIState by lazy {
        MutableStateFlow<AmityStoryModalDialogUIState>(AmityStoryModalDialogUIState.CloseDialog)
    }
    val dialogUIState get() = _dialogUIState

    var community: AmityCommunity? = null
        private set

    fun setCommunity(community: AmityCommunity?) {
        this.community = community
    }

    fun getActiveStories(
        targetId: String,
        targetType: AmityStory.TargetType
    ): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityStory>(
            AmityAdPlacement.STORY,
            targetId.takeIf { targetType == AmityStory.TargetType.COMMUNITY }
        )

        return AmitySocialClient.newStoryRepository()
            .getActiveStories(
                targetId = targetId,
                targetType = targetType,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .throttleLatest(800, TimeUnit.MILLISECONDS)
            .map {
                injector.inject(it)
            }
            .asFlow()
    }

    fun deleteStory(
        storyId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        AmitySocialClient.newStoryRepository()
            .softDeleteStory(storyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun addReaction(storyId: String) {
        AmityCoreClient.newReactionRepository()
            .addReaction(AmityReactionReference.STORY(storyId), AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun removeReaction(storyId: String) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(AmityReactionReference.STORY(storyId), AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun reUploadStory(
        storyId: String,
        story: AmityStory,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        val createStory = when (story.getDataType()) {
            AmityStory.DataType.IMAGE -> {
                val data = story.getData() as AmityStory.Data.IMAGE
                createImageStory(
                    targetId = story.getTargetId(),
                    targetType = story.getTargetType(),
                    fileUri = data.getImage()?.getUri() ?: Uri.EMPTY,
                    imageDisplayMode = data.getImageDisplayMode(),
                    storyItems = story.getStoryItems(),
                )
            }

            AmityStory.DataType.VIDEO -> {
                val data = story.getData() as AmityStory.Data.VIDEO
                createVideoStory(
                    targetId = story.getTargetId(),
                    targetType = story.getTargetType(),
                    fileUri = data.getVideo()?.getUri() ?: Uri.EMPTY,
                    storyItems = story.getStoryItems(),
                )
            }

            AmityStory.DataType.UNKNOWN -> {
                Completable.complete()
            }
        }

        createStory.doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()

        deleteStory(storyId)
    }

    private fun createImageStory(
        targetId: String,
        targetType: AmityStory.TargetType,
        fileUri: Uri,
        imageDisplayMode: AmityStoryImageDisplayMode,
        storyItems: List<AmityStoryItem>,
    ): Completable {
        return AmitySocialClient.newStoryRepository()
            .createImageStory(
                targetType = targetType,
                targetId = targetId,
                imageUri = fileUri,
                imageDisplayMode = imageDisplayMode,
                storyItems = storyItems,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createVideoStory(
        targetId: String,
        targetType: AmityStory.TargetType,
        fileUri: Uri,
        storyItems: List<AmityStoryItem>,
    ): Completable {
        return AmitySocialClient.newStoryRepository()
            .createVideoStory(
                targetType = targetType,
                targetId = targetId,
                videoUri = fileUri,
                storyItems = storyItems,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun checkMangeStoryPermission(communityId: String): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY)
            .atCommunity(communityId)
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun searchModerators(communityId: String): Flow<PagingData<AmityCommunityMember>> {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .getMembers()
            .roles(listOf(AmityConstants.MODERATOR_ROLE, AmityConstants.COMMUNITY_MODERATOR_ROLE))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun handleSegmentTimer(shouldPause: Boolean) {
        viewModelScope.launch {
            _shouldPauseTimer.value = shouldPause
        }
    }

    fun updateSheetUIState(uiState: AmityStoryModalSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = uiState
        }
    }

    fun updateDialogUIState(uiState: AmityStoryModalDialogUIState) {
        viewModelScope.launch {
            _dialogUIState.value = uiState
        }
    }
}

sealed class AmityStoryModalSheetUIState {

    data class OpenOverflowMenuSheet(
        val storyId: String,
    ) : AmityStoryModalSheetUIState()

    data class OpenCommentTraySheet(
        val storyId: String,
        val community: AmityCommunity? = null,
        val shouldAllowInteraction: Boolean,
        val shouldAllowComment: Boolean,
    ) : AmityStoryModalSheetUIState()

    object CloseSheet : AmityStoryModalSheetUIState()
}

sealed class AmityStoryModalDialogUIState {

    data class OpenConfirmDeleteDialog(val storyId: String) : AmityStoryModalDialogUIState()

    object CloseDialog : AmityStoryModalDialogUIState()
}