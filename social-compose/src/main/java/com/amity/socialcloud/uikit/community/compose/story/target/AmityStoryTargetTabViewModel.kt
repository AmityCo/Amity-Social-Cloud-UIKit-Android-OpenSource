package com.amity.socialcloud.uikit.community.compose.story.target

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityStoryTargetTabViewModel : AmityBaseViewModel() {

    private val _hasManageStoryPermission by lazy {
        MutableStateFlow(false)
    }
    val hasManageStoryPermission get() = _hasManageStoryPermission

    private val _stories by lazy {
        MutableStateFlow(PagingData.empty<AmityStory>())
    }
    val stories get() = _stories

    private val _storyTargetRingUiState by lazy {
        MutableStateFlow(AmityStoryTargetRingUiState.SEEN)
    }
    val storyTargetRingUiState get() = _storyTargetRingUiState

    fun observeStory(communityId: String) {
        AmitySocialClient.newStoryRepository()
            .getStoryTarget(AmityStory.TargetType.COMMUNITY, communityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { storyTarget ->
                val state = when {
                    storyTarget.getFailedStoriesCount() > 0 -> AmityStoryTargetRingUiState.FAILED
                    storyTarget.getSyncingStoriesCount() > 0 -> AmityStoryTargetRingUiState.SYNCING
                    storyTarget.hasUnseen() -> AmityStoryTargetRingUiState.HAS_UNSEEN
                    else -> AmityStoryTargetRingUiState.SEEN
                }
                _storyTargetRingUiState.value = state
            }
            .doOnError {

            }
            .subscribe()
    }

    fun checkMangeStoryPermission(communityId: String) {
        AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY)
            .atCommunity(communityId)
            .check()
            .doOnNext {
                viewModelScope.launch {
                    _hasManageStoryPermission.value = it
                }
            }
            .subscribe()
    }


    fun getStories(communityId: String) {
        AmitySocialClient.newStoryRepository()
            .getActiveStories(
                targetType = AmityStory.TargetType.COMMUNITY,
                targetId = communityId
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                viewModelScope.launch {
                    _stories.value = it
                }
            }
            .subscribe()
    }
}

enum class AmityStoryTargetRingUiState {
    SEEN,
    HAS_UNSEEN,
    SYNCING,
    FAILED,
}