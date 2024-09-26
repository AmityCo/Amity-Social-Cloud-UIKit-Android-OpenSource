package com.amity.socialcloud.uikit.common.ui.base

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeElementScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider


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