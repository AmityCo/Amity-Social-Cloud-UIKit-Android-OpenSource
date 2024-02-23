package com.amity.socialcloud.uikit.community.setting.user

import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityOtherUserSettingsMenuCreator {

    fun createManageHeader(): AmitySettingsItem.Header
    fun createNotificationMenu(userId: String, value: Int): AmitySettingsItem.NavigationContent
    fun createUnfollowMenu(userId: String): AmitySettingsItem.NavigationContent
    fun createReportUserMenu(user: AmityUser): AmitySettingsItem.NavigationContent
    fun createOthersHeader(): AmitySettingsItem.Header
    fun createShareProfileMenu(userId: String): AmitySettingsItem.NavigationContent

    fun createBlockUserMenu(followStatus: AmityFollowStatus): AmitySettingsItem.NavigationContent

}