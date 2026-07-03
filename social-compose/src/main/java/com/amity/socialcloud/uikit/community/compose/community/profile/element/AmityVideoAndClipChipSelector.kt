package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmityVideoAndClipChipSelector(
    tabTitles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tabTitles.forEachIndexed { index, title ->
            val elementId = when (title) {
                DefaultAmitySocialStringProvider.getInstance().getString("amity_social_tab_tab_clips") -> "clips_button"
                DefaultAmitySocialStringProvider.getInstance().getString("amity_social_tab_tab_videos") -> "videos_button"
                else -> "photos_button"
            }
            val isSelected = selectedTabIndex == index
            AmityBaseElement(
                elementId = elementId,
                componentScope = componentScope
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) AmityTheme.colors.highlight else AmityTheme.colors.baseShade4,
                    modifier = Modifier.clickableWithoutRipple {
                        if (!isSelected) {
                            onTabSelected(index)
                        }
                    }
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) amityColorWhite else AmityTheme.colors.baseShade1,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = if (isSelected) AmityTheme.typography.bodyBold else AmityTheme.typography.body
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun AmityVideoAndClipChipSelectorPreview() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Videos", "Clips")

    AmityVideoAndClipChipSelector(
        tabTitles = tabTitles,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { index ->
            selectedTabIndex = index
        },
    )
}