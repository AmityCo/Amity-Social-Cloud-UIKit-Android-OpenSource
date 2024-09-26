package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityToolBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: (() -> Unit) = {},
    rightMenuAction: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.amity_ic_back),
            contentDescription = "Close",
            modifier = modifier
                .size(24.dp)
                .align(Alignment.CenterStart)
                .clickableWithoutRipple {
                    onBackClick()
                }
        )

        Text(
            text = title,
            style = AmityTheme.typography.title,
            modifier = modifier.align(Alignment.Center)
        )

        if (rightMenuAction != null) {
            Box(
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                rightMenuAction()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityToolBarPreview() {
    AmityToolBar(
        title = "All members"
    )
}