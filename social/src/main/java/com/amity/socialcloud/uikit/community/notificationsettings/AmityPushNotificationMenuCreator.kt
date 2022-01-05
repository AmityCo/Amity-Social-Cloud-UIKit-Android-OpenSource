package com.amity.socialcloud.uikit.community.notificationsettings

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.Flowable

interface AmityPushNotificationMenuCreator {
    fun createAllNotificationsMenu(communityId : String, isToggled: Flowable<Boolean>): AmitySettingsItem.ToggleContent
    fun createPostMenu(communityId: String): AmitySettingsItem.NavigationContent
    fun createCommentMenu(communityId: String): AmitySettingsItem.NavigationContent
}