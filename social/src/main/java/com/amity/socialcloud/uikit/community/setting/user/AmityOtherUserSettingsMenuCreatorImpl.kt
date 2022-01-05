package com.amity.socialcloud.uikit.community.setting.user

import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmityOtherUserSettingsMenuCreatorImpl(private val fragment: AmityUserSettingsFragment) :
    AmityOtherUserSettingsMenuCreator {
    override fun createManageHeader(): AmitySettingsItem.Header {
        return AmitySettingsItem.Header(
            title = R.string.amity_manage
        )
    }

    override fun createNotificationMenu(
        userId: String,
        value: Int
    ): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_bell,
            title = R.string.amity_notifications,
            value = value,
            callback = { }
        )
    }

    override fun createUnfollowMenu(userId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            title = R.string.amity_unfollow,
            icon = R.drawable.amity_ic_unfollow,
            iconNavigation = null,
            callback = { fragment.showUnfollowDialog(userId) }
        )
    }

    override fun createReportUserMenu(user: AmityUser): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            title = if (user.isFlaggedByMe()) {
                R.string.amity_un_report_user
            } else {
                R.string.amity_report_user
            },
            icon = R.drawable.amity_ic_report,
            iconNavigation = null,
            callback = { fragment.reportUser(user) }
        )
    }

    override fun createOthersHeader(): AmitySettingsItem.Header {
        return AmitySettingsItem.Header(
            title = R.string.amity_others
        )
    }

    override fun createShareProfileMenu(userId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            title = R.string.amity_share_profile,
            icon = R.drawable.amity_ic_share_profile,
            callback = { fragment.shareUserProfile(userId) }
        )
    }
}