package com.amity.socialcloud.uikit.community.compose.community.setting.notifications

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationSettings
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

open class AmityCommunityNotificationViewModel(open val communityId: String) :
    AmityBaseViewModel() {

    fun getCommunityNotificationSettings(): Single<AmityCommunityNotificationSettings> {
        return AmityCoreClient.notifications()
            .community(communityId)
            .getSettings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserNotificationSettings(): Single<AmityUserNotificationSettings> {
        return AmityCoreClient.notifications()
            .user()
            .getSettings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateCommunityNotificationSetting(
        enable: Boolean,
        onSuccess: () -> Unit,
        onError: (AmityError) -> Unit,
    ) {
        val communityNotification = AmityCoreClient.notifications()
            .community(communityId)

        val settingsCompletable = if (enable) {
            communityNotification.enable()
        } else {
            communityNotification.disable()
        }

        settingsCompletable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError { onError.invoke(AmityError.from(it)) }
            .subscribe()
    }
}