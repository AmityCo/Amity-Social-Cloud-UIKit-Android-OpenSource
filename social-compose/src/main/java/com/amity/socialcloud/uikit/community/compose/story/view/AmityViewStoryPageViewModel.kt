package com.amity.socialcloud.uikit.community.compose.story.view

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

class AmityViewStoryPageViewModel : AmityBaseViewModel() {

    private val _stories by lazy {
        MutableStateFlow(PagingData.empty<AmityStory>())
    }
    val stories get() = _stories

    private val _hasManageStoryPermission by lazy {
        MutableStateFlow(false)
    }
    val hasManageStoryPermission get() = _hasManageStoryPermission

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

    fun deleteStory(storyId: String) {
        AmitySocialClient.newStoryRepository()
            .softDeleteStory(storyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
}