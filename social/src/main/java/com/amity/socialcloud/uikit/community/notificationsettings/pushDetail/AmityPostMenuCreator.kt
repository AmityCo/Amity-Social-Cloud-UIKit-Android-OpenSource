package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityPostMenuCreator {
    fun createReactPostMenu(communityId: String): AmitySettingsItem.TextContent
    fun createReactPostRadioMenu(communityId: String, choices: List<Pair<Int, Boolean>>): AmitySettingsItem.RadioContent
    fun createNewPostMenu(communityId: String): AmitySettingsItem.TextContent
    fun createNewPostRadioMenu(communityId: String, choices: List<Pair<Int, Boolean>>): AmitySettingsItem.RadioContent
}