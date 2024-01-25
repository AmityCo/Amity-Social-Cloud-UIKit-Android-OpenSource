package com.amity.socialcloud.uikit.community.notificationsettings

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.notification.AmityRolesFilter
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationModule
import com.amity.socialcloud.sdk.model.core.notification.AmityUserNotificationSettings
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

open class AmityPushNotificationBaseViewModel : AmityBaseViewModel() {

    var isPostEnabled = false
    var isCommentEnabled = false
    var isStoryEnabled = false
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

                is AmityCommunityNotificationEvent.STORY_COMMENT_CREATED,
                is AmityCommunityNotificationEvent.STORY_CREATED,
                is AmityCommunityNotificationEvent.STORY_REACTED -> {
                    if (event.isNetworkEnabled()) {
                        isStoryEnabled = true
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