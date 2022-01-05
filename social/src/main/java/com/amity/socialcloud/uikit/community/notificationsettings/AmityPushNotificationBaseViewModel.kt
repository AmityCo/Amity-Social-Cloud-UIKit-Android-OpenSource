package com.amity.socialcloud.uikit.community.notificationsettings

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityRolesFilter
import com.amity.socialcloud.sdk.core.user.AmityUserNotificationModule
import com.amity.socialcloud.sdk.core.user.AmityUserNotificationSettings
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunityNotificationEvent
import com.amity.socialcloud.sdk.social.community.AmityCommunityNotificationSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class AmityPushNotificationBaseViewModel : AmityBaseViewModel() {

    var isPostEnabled = false
    var isCommentEnabled = false
    var isGlobalModerator = false
    var isGlobalPushEnabled = true
    var isCommunityPushEnabled = false

    fun getPushNotificationSettings(
        communityId: String,
        onDataLoaded: (value: Boolean) -> Unit,
        onDataError: () -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository().notification(communityId)
            .getSettings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { settings ->
                checkPushSettings(settings)
                onDataLoaded(settings.isEnabled())
            }.doOnError {
                onDataError.invoke()
            }.ignoreElement()
    }

    private fun checkPushSettings(settings: AmityCommunityNotificationSettings) {
        isCommunityPushEnabled = settings.isEnabled()
        settings.getNotificationEvents().forEach { event ->
            when (event) {
                is AmityCommunityNotificationEvent.POST_CREATED,
                is AmityCommunityNotificationEvent.POST_REACTED -> {
                    if (event.isNetworkEnabled()) {
                        isPostEnabled = true
                    }
                }
                is AmityCommunityNotificationEvent.COMMENT_CREATED,
                is AmityCommunityNotificationEvent.COMMENT_REACTED,
                is AmityCommunityNotificationEvent.COMMENT_REPLIED -> {
                    if (event.isNetworkEnabled()) {
                        isCommentEnabled = true
                    }
                }

            }
        }
    }

    fun getGlobalPushNotificationSettings(onSuccess: () -> Unit, onError: () -> Unit): Completable {
        return AmityCoreClient.notification()
            .getSettings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                checkGlobalPushRole(it)
                onSuccess.invoke()
            }.doOnError {
                onError.invoke()
            }.ignoreElement()
    }

    private fun checkGlobalPushRole(notification: AmityUserNotificationSettings) {
        notification.getModules()?.forEach { module ->
            if (module is AmityUserNotificationModule.SOCIAL) {
                isGlobalPushEnabled = module.isEnabled()
                val filter = module.getRolesFilter()
                isGlobalModerator = filter is AmityRolesFilter.ONLY && filter.getRoles()
                    .any { it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE }
            }
        }
    }
}