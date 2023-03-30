package com.amity.socialcloud.uikit.community.followrequest

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

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
            .doOnError {
                if (it is AmityException) {
                    onError.invoke()
                }
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
                if (it is AmityException) {
                    onError.invoke()
                }
            }
    }
}