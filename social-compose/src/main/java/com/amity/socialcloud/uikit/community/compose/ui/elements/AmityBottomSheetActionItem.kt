package com.amity.socialcloud.uikit.community.compose.ui.elements

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple

@Composable
fun AmityBottomSheetActionItem(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
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
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = AmityTheme.colors.base,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text,
            style = AmityTheme.typography.body.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityBottomSheetActionItemPreview() {
    AmityBottomSheetActionItem(
        icon = R.drawable.amity_ic_delete_story,
        text = "Delete story",
        onClick = {}
    )
}