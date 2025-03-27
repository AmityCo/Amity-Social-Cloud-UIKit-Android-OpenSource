package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageTab

@Composable
fun AmitySocialHomeTabButton(
    modifier: Modifier = Modifier,
    title: String,
    item: AmitySocialHomePageTab,
    isSelected: Boolean = false,
    onClick: (AmitySocialHomePageTab) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .border(
                border = BorderStroke(
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else AmityTheme.colors.baseShade4,
                ),
                shape = RoundedCornerShape(size = 24.dp)
            )
            .background(
                color = if (isSelected) AmityTheme.colors.primary else Color.Transparent,
                shape = RoundedCornerShape(size = 24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickableWithoutRipple { onClick(item) }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 6.dp),
            text = title,
            style = AmityTheme.typography.titleLegacy.copy(
                color = if (isSelected) Color.White else AmityTheme.colors.secondaryShade1,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
        )
    }
}