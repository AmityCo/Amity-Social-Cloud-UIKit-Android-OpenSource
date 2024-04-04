package com.amity.socialcloud.uikit.community.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.community.compose.utils.asColor

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
) {

    fun applyConfiguration(theme: AmityUIKitConfig.UIKitTheme): AmityTypography {
        val base = theme.baseColor.asColor()

        return AmityUIKitTypography.copy(
            body = body.copy(color = base),
            caption = caption.copy(color = base),
            title = title.copy(color = base)
        )
    }
}