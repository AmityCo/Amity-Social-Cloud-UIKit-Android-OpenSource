package com.amity.socialcloud.uikit.community.setting.user

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityUserSettingsMenuCreator {

    fun createBasicInfoHeader(): AmitySettingsItem.Header
    fun createEditProfileMenu(): AmitySettingsItem.NavigationContent
}