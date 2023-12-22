package com.amity.socialcloud.uikit.community.compose.ui.base

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeElementScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.rememberAmityComposeScopeProvider


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
        content(comp)
    }
}