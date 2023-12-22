package com.amity.socialcloud.uikit.community.compose.story.create.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmityStoryCameraRelatedButtonElement(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.amity_ic_close,
    iconSize: Dp = 20.dp,
    tint: Color = Color.White,
    background: Color = Color(0x80000000),
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
                .size(iconSize)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityStoryCameraRelatedButtonElementPreview() {
    AmityStoryCameraRelatedButtonElement()
}