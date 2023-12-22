package com.amity.socialcloud.uikit.community.compose.ui.base

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityComposeTheme


@Composable
fun AmityBasePage(
    pageId: String,
    content: @Composable AmityComposePageScope.() -> Unit
) {
    val comp = rememberAmityComposeScopeProvider(
        pageId = pageId
    )
    AmityComposeTheme(pageScope = comp) {
        if (!comp.isExcluded()) {
            content(comp)
        }
    }
}