package com.amity.socialcloud.uikit.community.followrequest

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityItemFollowRequestViewModel : AmityBaseViewModel() {

    fun accept(
        userId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository().relationship()
            .me()
            .accept(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError{
                onError.invoke()
            }
    }

    fun decline(
        userId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmityCoreClient.newUserRepository().relationship()
            .me()
            .decline(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError {
                onError.invoke()
            }
    }
}