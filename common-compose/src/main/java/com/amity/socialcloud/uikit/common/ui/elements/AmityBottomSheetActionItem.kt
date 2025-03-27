package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityBottomSheetActionItem(
    modifier: Modifier = Modifier,
    icon: Int?,
    text: String,
    color: Color? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(horizontal = 4.dp, vertical = 16.dp)
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = color ?: AmityTheme.colors.base,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                color = color ?: AmityTheme.colors.base
            )
        )
    }
}

@Composable
fun AmityBottomSheetActionItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: String,
    color: Color? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(16.dp)
    ) {
        icon()

        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                color = color ?: AmityTheme.colors.base
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityBottomSheetActionItemPreview() {
    AmityBottomSheetActionItem(
        text = "Delete story",
        onClick = {},
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_camera2),
                contentDescription = null,
                tint = AmityTheme.colors.base,
                modifier = Modifier.size(24.dp)
            )
        }
    )
}