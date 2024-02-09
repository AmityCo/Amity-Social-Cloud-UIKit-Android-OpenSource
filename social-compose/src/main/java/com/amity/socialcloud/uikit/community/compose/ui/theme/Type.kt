package com.amity.socialcloud.uikit.community.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val AmityUIKitTypography = AmityTypography(
    body = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase
    ),
    caption = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase
    ),
    title = TextStyle(
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    )
)

@Immutable
data class AmityTypography(
    val body: TextStyle,
    val caption: TextStyle,
    val title: TextStyle
)