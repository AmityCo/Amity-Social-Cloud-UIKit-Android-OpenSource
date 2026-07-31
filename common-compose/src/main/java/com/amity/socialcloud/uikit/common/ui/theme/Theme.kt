package com.amity.socialcloud.uikit.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
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

// Scope id ("page/component/element") in effect for the current composition subtree, used by
// AmityTheme.token() to run the per-scope theme cascade. Defaults to global ("*/*/*").
val LocalAmityScopeId = staticCompositionLocalOf { "*/*/*" }

// Resolved dark-mode flag for the current subtree, provided by AmityComposeTheme. Read by
// AmityTheme.token() so tokens re-resolve (recompose) when dark mode flips.
val LocalAmityDarkMode = staticCompositionLocalOf { false }

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

    // Most-specific scope in effect drives the token cascade: component, else page, else global.
    val scopeId = componentScope?.getConfigId()
        ?: pageScope?.getConfigId()
        ?: "*/*/*"

    CompositionLocalProvider(
        LocalAmityColors provides amityColors,
        LocalAmityTypography provides amityTypography,
        LocalAmityShapes provides AmityUIKitShapes,
        LocalAmityScopeId provides scopeId,
        LocalAmityDarkMode provides isUIKitInDarkTheme,
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

    /**
     * Resolve a semantic token path to a [Color] against the effective theme and the SDK-vendored
     * token table. String engine + escape hatch for the typed [token] overload. Reads the current
     * scope + dark-mode CompositionLocals, so it MUST be called inside a @Composable — it re-resolves
     * (recomposes) when theme or dark mode changes. Unknown tokens resolve to a loud magenta.
     */
    @Composable
    fun token(path: String): Color {
        val scopeId = LocalAmityScopeId.current
        val mode = if (LocalAmityDarkMode.current) "dark" else "light"
        return AmityUIKitConfigController.resolveToken(scopeId, mode, path).value.toAmityTokenColor()
    }

    /** Typed sugar: symbol -> path -> the string engine above. Zero extra resolution logic. */
    @Composable
    fun token(t: AmityColorToken): Color = token(t.path)
}

/**
 * Convert a resolver hex string to a Compose [Color]. The token system emits 6-digit "#RRGGBB" or
 * 8-digit "#RRGGBBAA" (alpha LAST). Compose's Color(Long) wants 0xAARRGGBB, so 8-digit values are
 * reordered. Anything malformed (incl. the MISSING_COLOR path) falls back to loud magenta.
 */
internal fun String.toAmityTokenColor(): Color {
    val hex = removePrefix("#")
    return try {
        when (hex.length) {
            6 -> Color("FF$hex".toLong(16))
            8 -> {
                val rgb = hex.substring(0, 6)
                val alpha = hex.substring(6, 8)
                Color("$alpha$rgb".toLong(16))
            }
            else -> Color(0xFFFF00FF)
        }
    } catch (e: Exception) {
        Color(0xFFFF00FF)
    }
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