package com.amity.socialcloud.uikit.community.followers

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowRelationship
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatusFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_FOLLOWER_USER_ID"

class AmityUserFollowerViewModel (private val savedState: SavedStateHandle): AmityBaseViewModel() {

    var userId = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }

    fun getFollowersList(onSuccess: (PagingData<AmityFollowRelationship>) -> Unit): Completable {
        val query = if (isSelf()) {
            AmityCoreClient.newUserRepository().relationship()
                .me()
                .getFollowers()
                .status(AmityFollowStatusFilter.ACCEPTED)
                .build()
                .query()
        }else {
            AmityCoreClient.newUserRepository().relationship()
                .user(userId)
                .getFollowers()
                .build()
                .query()
        }
        return query.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onSuccess)
            .ignoreElements()
    }

    fun searchFollowers(input: String) {

    }

    fun isSelf(): Boolean = AmityCoreClient.getUserId() == userId
}