package com.amity.socialcloud.uikit.community.compose.community.setting.elements

import androidx.compose.foundation.layout.Arrangement
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
import com.amity.socialcloud.uikit.community.compose.ui.components.radio.AmityFilledRadioIndicator

@Composable
fun AmityCommunitySettingRadioGroup(
    modifier: Modifier = Modifier,
    items: List<AmityCommunitySettingRadioDataItem>,
    selectedKey: AmityCommunitySettingRadioDataItem,
    setSelectedKey: (AmityCommunitySettingRadioDataItem) -> Unit,
) {
    LazyColumn(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
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

        AmityFilledRadioIndicator(
            modifier = modifier.testTag(text),
            selected = isSelected,
            onClick = onSelected
        )
    }
}

data class AmityCommunitySettingRadioDataItem(
    val index: Int,
    val setting: Any,
    val title: String
)