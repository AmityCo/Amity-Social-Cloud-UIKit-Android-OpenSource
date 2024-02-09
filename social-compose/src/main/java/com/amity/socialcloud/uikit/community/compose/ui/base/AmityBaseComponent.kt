package com.amity.socialcloud.uikit.community.compose.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityComposeTheme

@Composable
fun AmityBaseComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentId: String,
    needScaffold: Boolean = false,
    content: @Composable AmityComposeComponentScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val comp = rememberAmityComposeScopeProvider(
        pageScope = pageScope,
        componentId = componentId,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )

    AmityComposeTheme(componentScope = comp) {
        if (!comp.isExcluded()) {
            if (needScaffold) {
                Scaffold(
                    modifier = modifier,
                    containerColor = Color.White,
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .padding(bottom = 64.dp)
                                .padding(horizontal = 16.dp),
                        ) {
                            (it.visuals as? AmitySnackbarVisuals)?.let { data ->
                                AmitySnackbar(data = data)
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = modifier
                            .padding(bottom = innerPadding.calculateBottomPadding())
                            .consumeWindowInsets(innerPadding)
                    ) {
                        content(comp)
                    }
                }
            } else {
                content(comp)
            }
        }
    }
}