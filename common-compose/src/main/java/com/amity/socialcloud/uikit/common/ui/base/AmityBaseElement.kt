package com.amity.socialcloud.uikit.common.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeElementScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.LocalAmityColors


@Composable
fun AmityBaseElement(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    elementId: String,
    content: @Composable AmityComposeElementScope.() -> Unit
) {
    val comp = rememberAmityComposeScopeProvider(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = elementId
    )
    if (!comp.isExcluded()) {
        // Unlike AmityBaseComponent, elements don't establish their own
        // AmityComposeTheme, so AmityTheme.colors would otherwise stay pinned to
        // the parent page/component theme. When the element defines its own theme
        // in config, merge it on top of the inherited colors so element-level
        // customizations take effect.
        val elementTheme = comp.getElementTheme()
        if (elementTheme != null) {
            val mergedColors = AmityTheme.colors.applyElementConfiguration(elementTheme)
            CompositionLocalProvider(LocalAmityColors provides mergedColors) {
                content(comp)
            }
        } else {
            content(comp)
        }
    }
}