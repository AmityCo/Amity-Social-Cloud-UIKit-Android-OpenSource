package com.amity.socialcloud.uikit.community.compose.community.setting.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommunitySettingItem(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AmityTheme.typography.bodyLegacy,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp)
            .clickableWithoutRipple { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()

            Text(
                text = title,
                style = titleStyle
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.amity_ic_chevron_right),
            contentDescription = "",
            tint = AmityTheme.colors.baseShade2,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommunitySettingItemPreview() {
    AmityCommunitySettingItem(
        title = "Edit profile",
        onClick = {},
        icon = {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_community_post_setting),
                    contentDescription = "",
                    tint = AmityTheme.colors.base,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}