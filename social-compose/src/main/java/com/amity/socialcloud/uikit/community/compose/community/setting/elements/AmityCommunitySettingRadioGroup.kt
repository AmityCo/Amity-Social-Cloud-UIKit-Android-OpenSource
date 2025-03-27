package com.amity.socialcloud.uikit.community.compose.community.setting.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityCommunitySettingRadioGroup(
    modifier: Modifier = Modifier,
    items: List<AmityCommunitySettingRadioDataItem>,
    selectedKey: AmityCommunitySettingRadioDataItem,
    setSelectedKey: (AmityCommunitySettingRadioDataItem) -> Unit,
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(count = items.size) {
            val data = items[it]
            AmityCommunitySettingRadioGroupItem(
                text = data.title,
                isSelected = selectedKey.index == data.index,
            ) {
                setSelectedKey(data)
            }
        }
    }
}

@Composable
fun AmityCommunitySettingRadioGroupItem(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onSelected() },
    ) {
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy,
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp)
                .testTag(text)
        )
        RadioButton(
            modifier = modifier.testTag(text),
            selected = isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = AmityTheme.colors.highlight,
                unselectedColor = AmityTheme.colors.baseShade2,
            ),
            onClick = onSelected,
        )
    }
}

data class AmityCommunitySettingRadioDataItem(
    val index: Int,
    val setting: Any,
    val title: String
)