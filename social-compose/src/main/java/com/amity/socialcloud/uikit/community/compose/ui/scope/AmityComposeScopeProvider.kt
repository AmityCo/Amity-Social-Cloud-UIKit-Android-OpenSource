package com.amity.socialcloud.uikit.community.compose.ui.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
internal fun rememberAmityComposeScopeProvider(
    pageId: String,
): AmityComposePageScope {
    return remember(pageId) {
        AmityComposePageScopeImpl(
            pageId = pageId
        )
    }
}

@Composable
internal fun rememberAmityComposeScopeProvider(
    pageScope: AmityComposePageScope? = null,
    componentId: String,
): AmityComposeComponentScope {
    return remember(pageScope, componentId) {
        AmityComposeComponentScopeImpl(
            pageScope = pageScope,
            componentId = componentId,
        )
    }
}

@Composable
internal fun rememberAmityComposeScopeProvider(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    elementId: String,
): AmityComposeElementScope {
    return remember(pageScope, componentScope, elementId) {
        AmityComposeElementScopeImpl(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = elementId,
        )
    }
}