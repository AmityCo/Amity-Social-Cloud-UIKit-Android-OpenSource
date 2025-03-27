package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.utils.asColor

val AmityUIKitTypography = AmityTypography(
    bodyLegacy = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase
    ),
    captionLegacy = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase
    ),
    titleLegacy = TextStyle(
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    ),
    display = TextStyle(
        fontSize = 32.sp,
        lineHeight = 48.sp,
        fontWeight = FontWeight.Bold,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    ),
    headLine = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    ),
    titleBold = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    ),
    title = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase,
        textAlign = TextAlign.Center,
    ),
    bodyBold = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase
    ),
    body = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase
    ),
    captionBold = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = amityColorBase
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase
    ),
    captionSmall = TextStyle(
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight.Normal,
        color = amityColorBase
    ),
)

@Immutable
data class AmityTypography(
    val bodyLegacy: TextStyle,
    val captionLegacy: TextStyle,
    val titleLegacy: TextStyle,
    val display: TextStyle,
    val headLine: TextStyle,
    val titleBold: TextStyle,
    val title: TextStyle,
    val bodyBold: TextStyle,
    val body: TextStyle,
    val captionBold: TextStyle,
    val caption: TextStyle,
    val captionSmall: TextStyle,
) {

    fun applyConfiguration(theme: AmityUIKitConfig.UIKitTheme): AmityTypography {
        val base = theme.baseColor.asColor()

        return AmityUIKitTypography.copy(
            bodyLegacy = bodyLegacy.copy(color = base),
            captionLegacy = captionLegacy.copy(color = base),
            titleLegacy = titleLegacy.copy(color = base),
            headLine = headLine.copy(color = base),
        )
    }
}