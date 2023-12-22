package com.amity.socialcloud.uikit.community.compose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.utils.asColor

@Immutable
data class AmityComposeColors(
    val primary: Color,
    val secondary: Color,
)

val LocalAmityColors = staticCompositionLocalOf {
    AmityComposeColors(
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
    )
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

    val amityColors = AmityComposeColors(
        primary = lightTheme.primaryColor.asColor(),
        secondary = lightTheme.secondaryColor.asColor(),
    )

    CompositionLocalProvider(
        LocalAmityColors provides amityColors,
        content = content
    )
}

object AmityComposeTheme {
    val colors: AmityComposeColors
        @Composable
        get() = LocalAmityColors.current
}