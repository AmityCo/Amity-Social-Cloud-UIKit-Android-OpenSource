package com.amity.socialcloud.uikit.community.setting

interface AmityCommunitySettingsMenuCreator {
    fun createEditProfileMenu(communityId: String): AmitySettingsItem.NavigationContent
    fun createMembersMenu(communityId: String): AmitySettingsItem.NavigationContent
    fun createNotificationMenu(communityId: String, value: Int): AmitySettingsItem.NavigationContent
    fun createPostReviewMenu(communityId: String): AmitySettingsItem.NavigationContent
    fun createLeaveCommunityMenu(communityId: String, hasDeletePermission: Boolean): AmitySettingsItem.TextContent
    fun createCloseCommunityMenu(communityId: String): AmitySettingsItem.TextContent
}