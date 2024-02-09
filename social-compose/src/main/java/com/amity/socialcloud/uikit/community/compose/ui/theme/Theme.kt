package com.amity.socialcloud.uikit.community.compose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope


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
    content: @Composable () -> Unit
) {
    val lightTheme = componentScope?.getComponentTheme()?.lightTheme
        ?: (pageScope?.getPageTheme()?.lightTheme
            ?: AmityUIKitConfigController.getGlobalTheme().lightTheme)

    val amityColors = AmityUIKitColors.applyConfiguration(lightTheme)

    CompositionLocalProvider(
        LocalAmityColors provides amityColors,
        LocalAmityTypography provides AmityUIKitTypography,
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