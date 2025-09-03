package com.amity.socialcloud.uikit.common.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.flow.catch

@OptIn(ExperimentalComposeUiApi::class)
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
    val sessionState by AmityCoreClient.observeSessionState().filter {
        it == SessionState.Established
    }.asFlow()
        .catch {
            it.printStackTrace()
        }
        .collectAsState(AmityCoreClient.getCurrentSessionState())

    val comp = rememberAmityComposeScopeProvider(
        pageScope = pageScope,
        componentId = componentId,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )

    AmityComposeTheme(
        pageScope = pageScope,
        componentScope = comp,
        sessionState = sessionState
    ) {
        if (!comp.isExcluded()) {
            if (needScaffold) {
                Scaffold(
                    containerColor = AmityTheme.colors.background,
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .padding(bottom = 64.dp)
                                .padding(horizontal = 16.dp),
                        ) {
                            when (it.visuals) {
                                is AmitySnackbarVisuals -> {
                                    AmitySnackbar(data = it.visuals as AmitySnackbarVisuals)
                                }

                                is AmityProgressSnackbarVisuals -> {
                                    AmityProgressSnackbar(data = it.visuals as AmityProgressSnackbarVisuals)
                                }
                            }
                        }
                    },
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
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
                Box(
                    modifier = modifier.semantics {
                        testTagsAsResourceId = true
                    }
                ) {
                    content(comp)
                }
            }
        }
    }
}