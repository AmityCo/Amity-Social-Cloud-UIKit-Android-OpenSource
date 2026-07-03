package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.utils.asColor
import com.amity.socialcloud.uikit.common.utils.shade

val amityColorPrimary = Color(0xFF1054DE)
val amityColorSecondary = Color(0xFF292B32)
val amityColorAlert = Color(0xFFFA4D30)
val amityColorHighlight = Color(0xFF1054DE)
val amityColorBase = Color(0xFF292B32)
val amityColorBaseInverse = Color(0xFFFFFFFF)

val amityColorBackground = Color(0xFFFFFFFF)
val amityColorBackgroundShade1 = Color(0xFFF6F7F8)
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

val amityColorToastBackground = Color(0xFF292B32)
val amityColorToastBackgroundDark = Color(0xFF40434E)

val amityColorHighlightDark = Color(0xFF4A82F2)

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
    newsfeedDivider = amityColorBaseShade4,
    border = amityColorBaseShade4,
    background = amityColorBackground,
    backgroundShade1 = amityColorBackgroundShade1,
    sheetBackground = amityColorBaseShade4,
    toastBackground = amityColorToastBackground,
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
    val newsfeedDivider: Color,
    val border: Color,
    val background: Color,
    val backgroundShade1: Color,
    val sheetBackground: Color,
    val toastBackground: Color,
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

    fun applyConfiguration(
        theme: AmityUIKitConfig.UIKitTheme,
        isUIKitInDarkTheme: Boolean,
    ): AmityColors {
        val primary = theme.primaryColor?.asColor() ?: amityColorPrimary
        val secondary = theme.secondaryColor?.asColor() ?: amityColorSecondary
        val base = theme.baseColor?.asColor() ?: amityColorBase
        val baseShade1 = theme.baseShade1Color?.asColor() ?: amityColorBaseShade1
        val baseShade2 = theme.baseShade2Color?.asColor() ?: amityColorBaseShade2
        val baseShade3 = theme.baseShade3Color?.asColor() ?: amityColorBaseShade3
        val baseShade4 = theme.baseShade4Color?.asColor() ?: amityColorBaseShade4
        val baseInverse = theme.baseInverseColor?.asColor() ?: amityColorBaseInverse
        val secondaryShade2 = theme.secondaryShade2?.asColor() ?: amityColorSecondary.shade(AmityColorShade.SHADE2)
        val secondaryShade3 = theme.secondaryShade3?.asColor() ?: amityColorSecondary.shade(AmityColorShade.SHADE3)
        val secondaryShade4 = theme.secondaryShade4?.asColor() ?: amityColorSecondary.shade(AmityColorShade.SHADE4)
        val alert = theme.alertColor?.asColor() ?: amityColorAlert
        val divider = theme.baseShade4Color?.asColor() ?: amityColorBaseShade4
        val background = theme.backgroundColor?.asColor() ?: amityColorBackground
        val backgroundShade1 = theme.backgroundShade1Color?.asColor() ?: amityColorBackgroundShade1
        val toastBackground = theme.toastBackgroundColor?.asColor()
            ?: if (isUIKitInDarkTheme) amityColorToastBackgroundDark else amityColorToastBackground
        val highlight = theme.highlightColor?.asColor()
            ?: if (isUIKitInDarkTheme) amityColorHighlightDark else amityColorHighlight

        val newsfeedDivider = if (isUIKitInDarkTheme) {
            amityColorBlack
        } else {
            baseShade4
        }

        return AmityUIKitColors.copy(
            primary = primary,
            primaryShade1 = primary.shade(AmityColorShade.SHADE1),
            primaryShade2 = primary.shade(AmityColorShade.SHADE2),
            primaryShade3 = amityColorPrimaryShade3,
            primaryShade4 = primary.shade(AmityColorShade.SHADE4),
            secondary = secondary,
            secondaryShade1 = secondaryShade1,
            secondaryShade2 = secondaryShade2,
            secondaryShade3 = secondaryShade3,
            secondaryShade4 = secondaryShade4,
            base = base,
            baseShade1 = baseShade1,
            baseShade2 = baseShade2,
            baseShade3 = baseShade3,
            baseShade4 = baseShade4,
            baseInverse = baseInverse,
            alert = alert,
            highlight = highlight,
            background = background,
            backgroundShade1 = backgroundShade1,
            sheetBackground = background,
            toastBackground = toastBackground,
            divider = divider,
            newsfeedDivider = newsfeedDivider,
        )
    }

}