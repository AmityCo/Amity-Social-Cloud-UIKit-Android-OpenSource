package com.amity.socialcloud.uikit.community.notificationsettings

import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.notificationsettings.pushDetail.AmityCommunityPostNotificationSettingsActivity
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.Flowable

class AmityPushNotificationMenuCreatorImpl(private val fragment: AmityCommunityNotificationSettingsFragment): AmityPushNotificationMenuCreator {

    override fun createAllNotificationsMenu(communityId: String, isToggled: Flowable<Boolean>): AmitySettingsItem.ToggleContent {
        return AmitySettingsItem.ToggleContent(
            title = R.string.amity_allow_notifications,
            description = R.string.amity_notifications_description,
            isToggled = isToggled,
            isTitleBold = true,
            callback = fragment::toggleAllSettings
        )
    }

    override fun createPostMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_new_posts,
            title = R.string.amity_posts,
            callback = { fragment.navigateToNewPostSettings(communityId, AmityCommunityPostNotificationSettingsActivity.SettingType.POSTS)}
        )
    }

    override fun createCommentMenu(communityId: String): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            icon = R.drawable.amity_ic_push_comments,
            title = R.string.amity_comments,
            callback = { fragment.navigateToNewPostSettings(communityId, AmityCommunityPostNotificationSettingsActivity.SettingType.COMMENTS)}
        )
    }
}