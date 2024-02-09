package com.amity.socialcloud.uikit.community.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.community.compose.utils.asColor
import com.amity.socialcloud.uikit.community.compose.utils.shade

val amityColorPrimary = Color(0xFF1054DE)
val amityColorSecondary = Color(0xFF292B32)
val amityColorAlert = Color(0xFFFA4D30)
val amityColorHighlight = Color(0xFF1054DE)
val amityColorBase = Color(0xFF292B32)
val amityColorBaseInverse = Color(0xFFFFFFFF)
val amityColorBaseInverseBlack = Color(0xFF000000)
val amityColorMessageBubble = Color(0xFF1054DE)
val amityColorMessageBubbleInverse = Color(0xFFEBECEF)

val amityColorPrimaryShade1 = Color(0xFF4A82F2)
val amityColorPrimaryShade2 = Color(0xFFA0BDF8)
val amityColorPrimaryShade3 = Color(0xFFD9E5FC)
val amityColorPrimaryShade4 = Color(0xFFFFFFFF)

val amityColorSecondaryShade1 = Color(0xFF636878)
val amityColorSecondaryShade2 = Color(0xFF898E9E)
val amityColorSecondaryShade3 = Color(0xFFA5A9B5)
val amityColorSecondaryShade4 = Color(0xFFEBECEF)

val amityColorBaseShade1 = Color(0xFF636878)
val amityColorBaseShade2 = Color(0xFF898E9E)
val amityColorBaseShade3 = Color(0xFFA5A9B5)
val amityColorBaseShade4 = Color(0xFFEBECEF)

val AmityUIKitColors = AmityColors(
    primary = amityColorPrimary,
    secondary = amityColorSecondary,
    alert = amityColorAlert,
    highlight = amityColorHighlight,
    base = amityColorBase,
    baseInverse = amityColorBaseInverse,
    messageBubble = amityColorMessageBubble,
    messageBubbleInverse = amityColorMessageBubbleInverse,
    divider = amityColorBaseShade4,
    border = amityColorBaseShade4,
    background = amityColorBaseShade4,
    backgroundGrey = amityColorBaseShade4,
    backgroundDark = amityColorBaseInverseBlack.copy(alpha = 0.5f),
    textPrimary = amityColorBase,
    textSecondary = amityColorBaseShade1,
    textTertiary = amityColorBaseShade2,
    textDisabled = amityColorBaseShade3,
    primaryShade1 = amityColorPrimaryShade1,
    primaryShade2 = amityColorPrimaryShade2,
    primaryShade3 = amityColorPrimaryShade3,
    primaryShade4 = amityColorPrimaryShade4,
    secondaryShade1 = amityColorSecondaryShade1,
    secondaryShade2 = amityColorSecondaryShade2,
    secondaryShade3 = amityColorSecondaryShade3,
    secondaryShade4 = amityColorSecondaryShade4,
    baseShade1 = amityColorBaseShade1,
    baseShade2 = amityColorBaseShade2,
    baseShade3 = amityColorBaseShade3,
    baseShade4 = amityColorBaseShade4,
)

@Immutable
data class AmityColors(
    val primary: Color,
    val secondary: Color,
    val alert: Color,
    val highlight: Color,
    val base: Color,
    val baseInverse: Color,
    val messageBubble: Color,
    val messageBubbleInverse: Color,
    val divider: Color,
    val border: Color,
    val background: Color,
    val backgroundGrey: Color,
    val backgroundDark: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val primaryShade1: Color,
    val primaryShade2: Color,
    val primaryShade3: Color,
    val primaryShade4: Color,
    val secondaryShade1: Color,
    val secondaryShade2: Color,
    val secondaryShade3: Color,
    val secondaryShade4: Color,
    val baseShade1: Color,
    val baseShade2: Color,
    val baseShade3: Color,
    val baseShade4: Color,
) {

    fun applyConfiguration(theme: AmityUIKitConfig.UIKitTheme): AmityColors {
        val primary = theme.primaryColor.asColor()
        val secondary = theme.secondaryColor.asColor()

        return AmityUIKitColors.copy(
            primary = primary,
            primaryShade1 = primary.shade(AmityColorShade.SHADE1),
            primaryShade2 = primary.shade(AmityColorShade.SHADE2),
            primaryShade3 = primary.shade(AmityColorShade.SHADE3),
            primaryShade4 = primary.shade(AmityColorShade.SHADE4),
            secondary = secondary,
            secondaryShade1 = secondary.shade(AmityColorShade.SHADE1),
            secondaryShade2 = secondary.shade(AmityColorShade.SHADE2),
            secondaryShade3 = secondary.shade(AmityColorShade.SHADE3),
            secondaryShade4 = secondary.shade(AmityColorShade.SHADE4),
        )
    }

}