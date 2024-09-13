package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R

@Composable
fun AmityMenuButton(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.amity_ic_close,
    size: Dp = 20.dp,
    iconPadding: Dp = 0.dp,
    tint: Color = Color.White,
    background: Color = Color.Black.copy(alpha = 0.5f),
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(size)
                .align(Alignment.Center)
                .padding(iconPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityMenuButtonPreview() {
    AmityMenuButton()
}