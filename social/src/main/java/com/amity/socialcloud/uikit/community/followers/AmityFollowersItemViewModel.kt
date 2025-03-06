package com.amity.socialcloud.uikit.community.followers

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityFollowersItemViewModel : AmityBaseViewModel() {

    fun reportUser(user: AmityUser, onSuccess: () -> Unit): Completable {
        return AmityCoreClient.newUserRepository()
            .flagUser(user.getUserId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
    }

    fun unReportUser(user: AmityUser, onSuccess: () -> Unit): Completable {
        return  AmityCoreClient.newUserRepository()
            .unflagUser(user.getUserId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
    }

    fun removeUser(userId: String): Completable {
        return AmityCoreClient.newUserRepository()
            .relationship()
            .me()
            .removeFollower(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}