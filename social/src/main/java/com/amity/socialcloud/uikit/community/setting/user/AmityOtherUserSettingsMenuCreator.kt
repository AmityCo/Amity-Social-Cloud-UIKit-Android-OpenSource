package com.amity.socialcloud.uikit.community.setting.user

import androidx.annotation.StringRes
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityOtherUserSettingsMenuCreator {

    fun createManageHeader(): AmitySettingsItem.Header
    fun createNotificationMenu(userId: String, value: Int): AmitySettingsItem.NavigationContent
    fun createUnfollowMenu(userId: String): AmitySettingsItem.NavigationContent
    fun createReportUserMenu(user: AmityUser): AmitySettingsItem.NavigationContent
    fun createOthersHeader(): AmitySettingsItem.Header
    fun createShareProfileMenu(userId: String): AmitySettingsItem.NavigationContent
}