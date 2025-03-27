package com.amity.socialcloud.uikit.community.compose.community.setting.notifications.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.community.setting.elements.AmityCommunitySettingRadioGroupItem
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingDataType

@Composable
fun AmityCommunityNotificationSettingMenuItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    selectedSetting: AmityCommunityNotificationSettingDataType,
    onSelected: (AmityCommunityNotificationSettingDataType) -> Unit,
) {
    Column(modifier.fillMaxWidth()) {
        Spacer(modifier.height(8.dp))
        Text(
            text = title,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier.height(4.dp))
        Text(
            text = description,
            style = AmityTheme.typography.captionLegacy.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade1,
            ),
            modifier = modifier.padding(horizontal = 16.dp)
        )

        AmityCommunitySettingRadioGroupItem(
            text = AmityCommunityNotificationSettingDataType.EVERYONE.title,
            isSelected = selectedSetting == AmityCommunityNotificationSettingDataType.EVERYONE,
        ) {
            onSelected(AmityCommunityNotificationSettingDataType.EVERYONE)
        }

        AmityCommunitySettingRadioGroupItem(
            text = AmityCommunityNotificationSettingDataType.ONLY_MODERATOR.title,
            isSelected = selectedSetting == AmityCommunityNotificationSettingDataType.ONLY_MODERATOR,
        ) {
            onSelected(AmityCommunityNotificationSettingDataType.ONLY_MODERATOR)
        }

        AmityCommunitySettingRadioGroupItem(
            text = AmityCommunityNotificationSettingDataType.OFF.title,
            isSelected = selectedSetting == AmityCommunityNotificationSettingDataType.OFF,
        ) {
            onSelected(AmityCommunityNotificationSettingDataType.OFF)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommunityNotificationSettingMenuItemPreview() {
    AmityCommunityNotificationSettingMenuItem(
        title = "React posts",
        description = "receive notifications when someone make a reaction to your post",
        selectedSetting = AmityCommunityNotificationSettingDataType.OFF,
    ) {}
}