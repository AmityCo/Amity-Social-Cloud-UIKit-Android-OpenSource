package com.amity.socialcloud.uikit.common.ui.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.localization.LocalAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityToast
import com.amity.socialcloud.uikit.common.ui.atoms.AmityToastVariant
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityProgressSnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmitySnackbarVisuals
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.rememberAmityComposeScopeProvider
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.ui.platform.LocalDensity
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import org.joda.time.DateTime
import java.util.UUID


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AmityBasePage(
    pageId: String,
    useAmityToast: Boolean = false,
    // How far the toast clears the bottom of the screen. A page with a compose bar has to lift it by
    // the bar's own height, so the value belongs to the page rather than to the toast.
    toastBottomPadding: Dp = if (useAmityToast) 72.dp else 16.dp,
    content: @Composable AmityComposePageScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val comp = rememberAmityComposeScopeProvider(
        pageId = pageId,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )
    // Keyboard lift for the toast: the ime inset is measured from the PHYSICAL screen bottom and
    // therefore includes the navigation-bar region — but the Scaffold already pads the snackbar
    // slot by systemBars, so adding the raw ime height double-counts the nav bar and floats the
    // toast too high while typing. Chat (useAmityToast) lifts only by the keyboard's excess over
    // the nav bar; the legacy snackbar path keeps the raw value it always used.
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val navBottom = WindowInsets.navigationBars.getBottom(density)
    val keyboardHeight = with(density) {
        (if (useAmityToast) (imeBottom - navBottom).coerceAtLeast(0) else imeBottom).toDp()
    }
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
        CompositionLocalProvider(
            LocalAmityCommonStringProvider provides DefaultAmityCommonStringProvider.getInstance()
        ) {
            Scaffold(
                containerColor = AmityTheme.colors.background,
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            // A toast sitting at bottom-16 overlaps any compose bar, since the offset
                            // already accounts for keyboardHeight and the bar sits above that. Pages
                            // with a composer lift it by composer + gap; the rest keep 16.
                            // Values are approximate pending on-device/design confirmation (QA
                            // toast-position node is stale post-rebaseline).
                            .padding(bottom = keyboardHeight + additionalHeight.dp + toastBottomPadding)
                            .padding(horizontal = 16.dp),
                    ) {
                        when (it.visuals) {
                            is AmitySnackbarVisuals -> {
                                val data = it.visuals as AmitySnackbarVisuals
                                additionalHeight = data.additionalHeight
                                if (useAmityToast) {
                                    // Chat opt-in: render the AmityToast atom instead of the legacy
                                    // AmitySnackbar visual. Dismiss timing stays owned by
                                    // snackbarHostState (data.duration), so the atom's own
                                    // auto-dismiss timer is disabled here.
                                    val isError =
                                        data.drawableRes == R.drawable.amity_ic_snack_bar_warning
                                    AmityToast(
                                        message = data.message,
                                        variant = if (isError) {
                                            AmityToastVariant.ERROR
                                        } else {
                                            AmityToastVariant.INFORMATIVE
                                        },
                                        // The legacy warning drawable still selects the variant, but
                                        // it is a different glyph from the design's exclamation
                                        // circle (25x24, tapered stem). Swap it here rather than
                                        // editing the shared drawable, which social also renders.
                                        icon = if (isError) {
                                            R.drawable.amity_ic_exclamation_circle_r
                                        } else {
                                            data.drawableRes
                                        },
                                        duration = null,
                                    )
                                } else if ((it.visuals as AmitySnackbarVisuals).dismissable) {
                                    SwipeToDismissBox(
                                        state = rememberSwipeToDismissBoxState(
                                            SwipeToDismissBoxValue.Settled,
                                            SwipeToDismissBoxDefaults.positionalThreshold
                                        ),
                                        backgroundContent = {}
                                    ) {
                                        AmitySnackbar(data = it.visuals as AmitySnackbarVisuals)
                                    }
                                } else {
                                    AmitySnackbar(data = data)
                                }
                            }

                            is AmityProgressSnackbarVisuals -> {
                                if (useAmityToast) {
                                    AmityToast(
                                        message = (it.visuals as AmityProgressSnackbarVisuals).message,
                                        variant = AmityToastVariant.LOADING,
                                        duration = null,
                                    )
                                } else {
                                    AmityProgressSnackbar(data = it.visuals as AmityProgressSnackbarVisuals)
                                }
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
}