package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.storytarget.AmityGlobalStoryTargetsQueryOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class AmityStoryGlobalTargetTabViewModel : AmityBaseViewModel() {

    fun getTargets(): Flow<PagingData<AmityStoryTarget>> {
        return AmitySocialClient.newStoryRepository()
            .getGlobalStoryTargets(
                queryOption = AmityGlobalStoryTargetsQueryOption.SMART
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun prefetchStoriesFromTargets(targets: List<AmityStoryTarget>) {
        compositeDisposable.clear()
        targets.chunked(10)
            .map {
                AmitySocialClient.newStoryRepository()
                    .getStoriesByTargets(
                        targets = it.map { target ->
                            target.getTargetType() to target.getTargetId()
                        }
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                    .let(compositeDisposable::add)
            }
    }
}