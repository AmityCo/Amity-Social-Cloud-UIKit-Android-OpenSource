package com.amity.socialcloud.uikit.community.followrequest

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_FOLLOW_REQUEST_USER_ID"

class AmityFollowRequestsViewModel (private val savedState: SavedStateHandle) : AmityBaseViewModel() {

    var userId: String = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }

    fun getFollowRequests(
        onSuccess: (PagedList<AmityFollowRelationship>) -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .me()
            .getFollowers()
            .status(AmityFollowStatusFilter.PENDING)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onSuccess)
            .doOnError {
                onError.invoke()
            }.ignoreElements()

    }
}