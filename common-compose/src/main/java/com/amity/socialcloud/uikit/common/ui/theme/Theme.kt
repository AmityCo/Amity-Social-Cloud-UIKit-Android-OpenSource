package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope


val LocalAmityColors = staticCompositionLocalOf {
    AmityUIKitColors
}

val LocalAmityTypography = staticCompositionLocalOf {
    AmityUIKitTypography
}

val LocalAmityShapes = staticCompositionLocalOf {
    AmityUIKitShapes
}

@Composable
fun AmityComposeTheme(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AmityUIKitConfigController.setSystemInDarkTheme(isSystemInDarkTheme)

    val theme = componentScope?.getComponentTheme()
        ?: (pageScope?.getPageTheme()
            ?: AmityUIKitConfigController.getGlobalTheme())

    val amityColors = AmityUIKitColors.applyConfiguration(theme, isSystemInDarkTheme)
    val amityTypography = AmityUIKitTypography.applyConfiguration(theme)

    CompositionLocalProvider(
        LocalAmityColors provides amityColors,
        LocalAmityTypography provides amityTypography,
        LocalAmityShapes provides AmityUIKitShapes,
    ) {
        content()
    }
}

object AmityTheme {

    val colors: AmityColors
        @Composable
        get() = LocalAmityColors.current

    val typography: AmityTypography
        @Composable
        get() = LocalAmityTypography.current

    val shapes: AmityShapes
        @Composable
        get() = LocalAmityShapes.current
}