package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import org.joda.time.DateTime


val LocalAmityColors = staticCompositionLocalOf {
    AmityUIKitColors
}

val LocalAmityTypography = staticCompositionLocalOf {
    AmityUIKitTypography
}

val LocalAmityShapes = staticCompositionLocalOf {
    AmityUIKitShapes
}

val LocalAmityIsUIKitInDarkTheme = staticCompositionLocalOf {
    AmityUIKitConfigController.shouldUIKitInDarkTheme()
}

@Composable
fun AmityComposeTheme(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    isUIKitInDarkTheme: Boolean = isUIKitInDarkTheme(),
    lastThemeUpdate: DateTime = DateTime.now(),
    content: @Composable () -> Unit
) {

    AmityUIKitConfigController.setSystemInDarkTheme(isSystemInDarkTheme)

    val theme = componentScope?.getComponentTheme()
        ?: (pageScope?.getPageTheme()
            ?: AmityUIKitConfigController.getGlobalTheme())

    val amityColors = AmityUIKitColors.applyConfiguration(theme, isUIKitInDarkTheme)
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


@Composable
@ReadOnlyComposable
fun isUIKitInDarkTheme(): Boolean {
    return LocalAmityIsUIKitInDarkTheme.current
}