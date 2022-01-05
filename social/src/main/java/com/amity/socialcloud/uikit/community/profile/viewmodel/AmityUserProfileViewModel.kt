package com.amity.socialcloud.uikit.community.profile.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityMyFollowInfo
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserFollowInfo
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.profile.listener.AmityEditUserProfileClickListener
import com.amity.socialcloud.uikit.community.profile.listener.AmityFeedFragmentDelegate
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val SAVED_USER_ID = "SAVED_USER_ID"

class AmityUserProfileViewModel (private val savedState: SavedStateHandle): AmityBaseViewModel() {
    var feedFragmentDelegate: AmityFeedFragmentDelegate? = null
    var editUserProfileClickListener: AmityEditUserProfileClickListener? = null
    var userId: String = ""
        set(value) {
            savedState.set(SAVED_USER_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_USER_ID)?.let { userId = it }
    }

    fun getUser(): Flowable<AmityUser> {
        return if (isSelfUser()) {
            AmityCoreClient.getCurrentUser()
        } else {
            AmityCoreClient.newUserRepository().getUser(userId)
        }
    }

    fun isSelfUser(): Boolean {
        return AmityCoreClient.getUserId() == userId
    }

    fun getMyFollowInfo(
        onSuccess: (AmityMyFollowInfo) -> Unit,
        onError: (Throwable) -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .me()
            .getFollowInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onSuccess)
            .doOnError(onError)
            .ignoreElements()

    }

    fun getUserFollowInfo(
        onSuccess: (AmityUserFollowInfo) -> Unit,
        onError: (Throwable) -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .user(userId)
            .getFollowInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(onSuccess)
            .doOnError(onError)
            .ignoreElements()
    }

    fun sendFollowRequest(
        onSuccess: (AmityFollowStatus) -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .user(userId)
            .follow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError { onError.invoke() }
            .ignoreElement()
    }

    fun unFollow(): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .me()
            .unfollow(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}