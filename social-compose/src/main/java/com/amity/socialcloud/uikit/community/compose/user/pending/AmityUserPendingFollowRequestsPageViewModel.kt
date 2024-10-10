package com.amity.socialcloud.uikit.community.compose.user.pending

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowRelationship
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatusFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class AmityUserPendingFollowRequestsPageViewModel : AmityBaseViewModel() {

    fun getPendingRequests(): Flow<PagingData<AmityFollowRelationship>> {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .getMyFollowers()
            .status(AmityFollowStatusFilter.PENDING)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun acceptFollow(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .acceptMyFollower(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }

    fun declineFollow(
        userId: String,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        AmityCoreClient.newUserRepository()
            .relationship()
            .declineMyFollower(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError(AmityError.from(it)) }
            .subscribe()
    }
}