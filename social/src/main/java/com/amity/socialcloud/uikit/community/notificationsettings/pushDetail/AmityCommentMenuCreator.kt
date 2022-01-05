package com.amity.socialcloud.uikit.community.notificationsettings.pushDetail

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

interface AmityCommentMenuCreator {

    fun createReactCommentsMenu(communityId: String): AmitySettingsItem.TextContent
    fun createReactCommentsRadioMenu(communityId: String, choices: List<Pair<Int, Boolean>>): AmitySettingsItem.RadioContent
    fun createNewCommentsMenu(communityId: String): AmitySettingsItem.TextContent
    fun createNewCommentsRadioMenu(communityId: String, choices: List<Pair<Int, Boolean>>): AmitySettingsItem.RadioContent
    fun createReplyCommentsMenu(communityId: String): AmitySettingsItem.TextContent
    fun createReplyCommentsRadioMenu(communityId: String, choices: List<Pair<Int, Boolean>>): AmitySettingsItem.RadioContent
}