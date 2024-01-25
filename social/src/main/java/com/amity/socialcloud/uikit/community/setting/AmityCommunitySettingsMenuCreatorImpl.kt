package com.amity.socialcloud.uikit.community.setting

import com.amity.socialcloud.uikit.community.R

class AmityCommunitySettingsMenuCreatorImpl(private val fragment: AmityCommunitySettingsFragment) :
    AmityCommunitySettingsMenuCreator {

    override fun createEditProfileMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_pen,
            title = R.string.amity_edit_profile,
            callback = { fragment.navigateToCommunityProfile(communityId) }
        )
    }

    override fun createMembersMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_user_friends,
            title = R.string.amity_members_capital,
            callback = { fragment.navigateToCommunityMemberSettings(communityId) }
        )
    }

    override fun createNotificationMenu(
        communityId: String,
        value: Int
    ): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_bell,
            title = R.string.amity_notifications,
            value = value,
            callback = { fragment.navigateToPushNotificationSettings(communityId) }
        )
    }

    override fun createPostReviewMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_clipboard_check,
            title = R.string.amity_post_review,
            callback = { fragment.navigateToPostReview() }
        )
    }

    override fun createStorySettingMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_story_setting,
            title = R.string.amity_story_setting,
            callback = { fragment.navigateToStorySetting() }
        )
    }

    override fun createLeaveCommunityMenu(
        communityId: String,
        hasDeletePermission: Boolean
    ): AmitySettingsItem.TextContent {
        return if (hasDeletePermission) {
            AmitySettingsItem.TextContent(
                title = R.string.amity_leave_community,
                titleTextColor = R.color.amityColorAlert,
                callback = { fragment.confirmModeratorLeaveCommunity() }
            )
        } else {
            AmitySettingsItem.TextContent(
                title = R.string.amity_leave_community,
                titleTextColor = R.color.amityColorAlert,
                callback = { fragment.confirmLeaveCommunity() }
            )
        }
    }

    override fun createCloseCommunityMenu(communityId: String): AmitySettingsItem.TextContent {
        return AmitySettingsItem.TextContent(
            title = R.string.amity_close_community,
            titleTextColor = R.color.amityColorAlert,
            description = R.string.amity_close_community_description,
            callback = { fragment.confirmCloseCommunity() }
        )
    }
}