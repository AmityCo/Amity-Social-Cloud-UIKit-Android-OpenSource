package com.amity.socialcloud.uikit.community.compose.ui.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityComposeTheme


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AmityBasePage(
    pageId: String,
    content: @Composable AmityComposePageScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val comp = rememberAmityComposeScopeProvider(
        pageId = pageId,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )

    AmityComposeTheme(pageScope = comp) {
        Scaffold(
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
            },
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            }
        ) {
            if (!comp.isExcluded()) {
                content(comp)
            }
        }
    }
}