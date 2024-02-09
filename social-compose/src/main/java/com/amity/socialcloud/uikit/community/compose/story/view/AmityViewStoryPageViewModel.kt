package com.amity.socialcloud.uikit.community.compose.story.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.sdk.model.social.story.analytics
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityViewStoryPageViewModel : AmityBaseViewModel() {

    private val _shouldPauseTimer by lazy {
        MutableStateFlow(false)
    }
    val shouldPauseTimer get() = _shouldPauseTimer

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

    fun markAsSeen(story: AmityStory) {
        story.analytics().markAsSeen()
    }

    fun deleteStory(storyId: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
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

    fun reuploadStory(storyId: String) {
        // TODO: 4/1/24 implement reupload story
        Log.d("TT", "reuploadStory: storyId = $storyId")
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

    fun handleSegmentTimer(shouldPause: Boolean) {
        viewModelScope.launch {
            _shouldPauseTimer.value = shouldPause
        }
    }

    fun getPostedCommunity(story: AmityStory): AmityCommunity? {
        val target = story.getTarget()
        if (target is AmityStoryTarget.COMMUNITY) {
            return target.getCommunity()
        }
        return null
    }
}