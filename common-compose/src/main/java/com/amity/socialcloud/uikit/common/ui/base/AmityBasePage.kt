package com.amity.socialcloud.uikit.common.ui.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import org.joda.time.DateTime
import java.util.UUID


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
    val keyboardHeight by getKeyboardHeight()
    var additionalHeight by remember { mutableIntStateOf(0) }

    var lastThemeUpdate by remember { mutableStateOf( DateTime.now() ) }

    DisposableEffect(Unit) {
        val id = UUID.randomUUID().toString()
        val callback = { lastThemeUpdate = DateTime.now() }
        AmityUIKitConfigController.registerChangeCallback(id, callback)

        // Cleanup when the composable leaves composition
        onDispose {
            AmityUIKitConfigController.unregisterChangeCallback(id)
        }
    }

    AmityComposeTheme(pageScope = comp, lastThemeUpdate = lastThemeUpdate) {
        Scaffold(
            containerColor = AmityTheme.colors.background,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .padding(bottom = keyboardHeight + additionalHeight.dp + 16.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    when (it.visuals) {
                        is AmitySnackbarVisuals -> {
                            val data = it.visuals as AmitySnackbarVisuals
                            additionalHeight = data.additionalHeight
                            AmitySnackbar(data = data)
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
        ) {
            if (!comp.isExcluded()) {
                content(comp)
            }
        }
    }
}