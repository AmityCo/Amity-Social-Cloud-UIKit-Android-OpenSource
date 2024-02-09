package com.amity.socialcloud.uikit.community.compose.ui.scope

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope


@Composable
internal fun rememberAmityComposeScopeProvider(
    pageId: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
): AmityComposePageScope {
    return remember(pageId) {
        AmityComposePageScopeImpl(
            pageId = pageId,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
        )
    }
}

@Composable
internal fun rememberAmityComposeScopeProvider(
    pageScope: AmityComposePageScope? = null,
    componentId: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
): AmityComposeComponentScope {
    return remember(pageScope, componentId) {
        AmityComposeComponentScopeImpl(
            pageScope = pageScope,
            componentId = componentId,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
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