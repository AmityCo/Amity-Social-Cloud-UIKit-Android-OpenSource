package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R


@Composable
fun AmitySocialHomeNavigationButton(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.amity_ic_plus,
    iconSize: Dp = 20.dp,
    tint: Color = AmityTheme.colors.base,
    background: Color = Color(0xFFEBECEF),
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .clickableWithoutRipple {
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
fun AmitySocialHomeNavigationButtonPreview() {
    AmitySocialHomeNavigationButton()
}

@Preview(showBackground = true)
@Composable
fun AmitySocialHomeNavigationNotificationButtonPreview() {
    Image(
        painter = painterResource(R.drawable.amity_ic_notification_tray_default),
        contentDescription = "",
        modifier =  Modifier
            .clip(CircleShape)
            .size(32.dp)
    )
}