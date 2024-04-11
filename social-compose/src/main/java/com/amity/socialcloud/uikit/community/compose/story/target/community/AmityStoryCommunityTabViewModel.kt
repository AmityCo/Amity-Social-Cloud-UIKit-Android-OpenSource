package com.amity.socialcloud.uikit.community.compose.story.target.community

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class AmityStoryCommunityTabViewModel : AmityBaseViewModel() {

    fun observeStoryTarget(communityId: String): Flowable<AmityStoryTarget> {
        return AmitySocialClient.newStoryRepository()
            .getStoryTarget(AmityStory.TargetType.COMMUNITY, communityId)
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

    fun getStories(communityId: String): Flow<PagingData<AmityStory>> {
        return AmitySocialClient.newStoryRepository()
            .getActiveStories(
                targetType = AmityStory.TargetType.COMMUNITY,
                targetId = communityId
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
}
