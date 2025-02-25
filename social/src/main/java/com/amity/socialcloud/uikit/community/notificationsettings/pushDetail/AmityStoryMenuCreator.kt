package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityStoryMenuCreator {
    fun createNewStoriesMenu(communityId: String): AmitySettingsItem.TextContent
    fun createNewStoriesRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent

    fun createStoryReactionsMenu(communityId: String): AmitySettingsItem.TextContent
    fun createStoryReactionsRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent

    fun createStoryCommentsMenu(communityId: String): AmitySettingsItem.TextContent
    fun createStoryCommentsRadioMenu(
        communityId: String,
        choices: List<Pair<Int, Boolean>>
    ): AmitySettingsItem.RadioContent
}