package com.amity.socialcloud.uikit.community.compose.ui.base

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityComposeTheme

@Composable
fun AmityBaseComponent(
    pageScope: AmityComposePageScope? = null,
    componentId: String,
    content: @Composable AmityComposeComponentScope.() -> Unit
) {
    val comp = rememberAmityComposeScopeProvider(
        pageScope = pageScope,
        componentId = componentId
    )
    AmityComposeTheme(componentScope = comp) {
        if (!comp.isExcluded()) {
            content(comp)
        }
    }
}