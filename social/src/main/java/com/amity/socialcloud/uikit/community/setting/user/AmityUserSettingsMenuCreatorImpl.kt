package com.amity.socialcloud.uikit.community.setting.user

import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmityUserSettingsMenuCreatorImpl(private val fragment: AmityUserSettingsFragment): AmityUserSettingsMenuCreator {

    override fun createBasicInfoHeader(): AmitySettingsItem.Header {
        return AmitySettingsItem.Header(
            title = R.string.amity_basic_info
        )
    }

    override fun createEditProfileMenu(): AmitySettingsItem.NavigationContent {
        return AmitySettingsItem.NavigationContent(
            title = R.string.amity_edit_profile,
            icon = R.drawable.amity_ic_edit_user_profile,
            iconNavigation = null,
            callback = { fragment.editProfile() }
        )
    }
}