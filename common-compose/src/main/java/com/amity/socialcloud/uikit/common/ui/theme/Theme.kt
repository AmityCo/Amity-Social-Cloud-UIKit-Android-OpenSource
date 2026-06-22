package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
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

@Composable
fun AmityComposeTheme(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    // The controller's isSystemInDarkTheme field defaults to false at process start,
    // so shouldUIKitInDarkTheme() returns false for preferred_theme="default" until
    // setSystemInDarkTheme(...) is called. Default parameters are evaluated before
    // the function body, so we have to push the system theme into the controller
    // here — otherwise the parameter resolves to the stale value.
    isUIKitInDarkTheme: Boolean = run {
        AmityUIKitConfigController.setSystemInDarkTheme(isSystemInDarkTheme)
        AmityUIKitConfigController.shouldUIKitInDarkTheme()
    },
    lastThemeUpdate: DateTime = DateTime.now(),
    sessionState: SessionState = SessionState.Established,
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
    // Read the controller directly. Don't route through a staticCompositionLocalOf
    // default — that lambda fires once per process and caches forever (Kotlin lazy),
    // so the first read at startup (before setSystemInDarkTheme has run) pins the
    // value as false for the lifetime of the process.
    return AmityUIKitConfigController.shouldUIKitInDarkTheme()
}