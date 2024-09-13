package com.amity.socialcloud.uikit.community.compose.community.setting.notifications

import com.amity.socialcloud.sdk.model.core.notification.AmityRolesFilter
import com.amity.socialcloud.sdk.model.core.role.AmityRoles
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import com.amity.socialcloud.uikit.common.utils.AmityConstants


enum class AmityCommunityNotificationSettingDataType(val title: String) {
    EVERYONE("Everyone"),
    ONLY_MODERATOR("Only moderator"),
    OFF("Off");

    fun getUpdateModelData(): Pair<Boolean, AmityRolesFilter> {
        val rolesFilter = if (this == ONLY_MODERATOR) {
            AmityRolesFilter.ONLY(
                AmityRoles(
                    listOf(
                        AmityConstants.MODERATOR_ROLE,
                        AmityConstants.COMMUNITY_MODERATOR_ROLE
                    )
                )
            )
        } else {
            AmityRolesFilter.All
        }

        return Pair(this != OFF, rolesFilter)
    }
}


fun AmityCommunityNotificationEvent.getSettingDataType(): AmityCommunityNotificationSettingDataType {
    return if (isEnabled()) {
        val rolesFilter = getRolesFilter()
        val isModerator =
            rolesFilter is AmityRolesFilter.ONLY && rolesFilter.getRoles()
                .any { it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE }

        if (isModerator) {
            AmityCommunityNotificationSettingDataType.ONLY_MODERATOR
        } else {
            AmityCommunityNotificationSettingDataType.EVERYONE
        }
    } else {
        AmityCommunityNotificationSettingDataType.OFF
    }
}