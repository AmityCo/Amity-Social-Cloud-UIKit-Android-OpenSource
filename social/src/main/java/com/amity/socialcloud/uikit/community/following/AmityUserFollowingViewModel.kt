package com.amity.socialcloud.uikit.community.following

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowRelationship
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_FOLLOWING_USER_ID"

class AmityUserFollowingViewModel (private val savedState: SavedStateHandle): AmityBaseViewModel() {

    var userId = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }
    fun getFollowingList(onSuccess: (PagedList<AmityFollowRelationship>) -> Unit): Completable {
        val query = if (AmityCoreClient.getUserId() == userId) {
            AmityCoreClient.newUserRepository().relationship()
                .me()
                .getFollowings()
                .status(AmityFollowStatusFilter.ACCEPTED)
                .build()
                .query()
        }else {
            AmityCoreClient.newUserRepository().relationship()
                .user(userId)
                .getFollowings()
                .build()
                .query()
        }
        return query.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onSuccess)
            .ignoreElements()
    }

    fun searchFollowing(input: String) {

    }
}