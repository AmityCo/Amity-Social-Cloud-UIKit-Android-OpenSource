package com.amity.socialcloud.uikit.community.compose.post.composer.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityDetailedMediaAttachmentComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityMediaAttachmentComponent
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

@Composable
fun AmityMediaAttachmentElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    productTagCount: Int = 0,
    onProductTagClick: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val isKeyboardOpen by isKeyboardVisible()
    var showDetailedView by remember { mutableStateOf(true) }
    var verticalDragAmount by remember { mutableFloatStateOf(0f) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val selectedMediaFiles by viewModel.selectedMediaFiles.collectAsState()
    val hasMediaAttached by remember {
        derivedStateOf { selectedMediaFiles.isNotEmpty() }
    }

    LaunchedEffect(isKeyboardOpen, hasMediaAttached) {
        // Collapsing when the keyboard opens or the first attachment lands is a starting point, not a
        // lock: the expanded sheet would cover the preview, so it gets out of the way by default and
        // the member can drag it back up. Keyed so a manual expand survives until one of these changes.
        showDetailedView = !isKeyboardOpen && !hasMediaAttached
    }

    Column(
        modifier = modifier
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        val isDragUp = verticalDragAmount < 0
                        if (isDragUp) {
                            // Reachable with media attached: the labelled rows -- Tag products in
                            // particular -- exist only in the expanded sheet, so blocking it here hid
                            // them exactly when they are wanted. Still suppressed under the keyboard,
                            // which already owns that space.
                            if (!isKeyboardOpen) {
                                showDetailedView = true
                            }
                        } else {
                            if (isKeyboardOpen) {
                                keyboardController?.hide()
                            } else {
                                showDetailedView = false
                            }
                        }
                        verticalDragAmount = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    verticalDragAmount += dragAmount
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .offset(y = 12.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            amityColorBlack.copy(alpha = 0.08f),
                        )
                    )
                )
        )
        Column(
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = AmityTheme.colors.sheetBackground,
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            color = AmityTheme.colors.baseShade3,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .align(Alignment.Center)
                )
            }

            if (showDetailedView) {
                AmityDetailedMediaAttachmentComponent(
                    pageScope = pageScope,
                    viewModel = viewModel,
                    productTagCount = productTagCount,
                    onProductTagClick = onProductTagClick,
                )
            } else {
                AmityMediaAttachmentComponent(
                    pageScope = pageScope,
                    viewModel = viewModel,
                    productTagCount = productTagCount,
                    onProductTagClick = onProductTagClick,
                )
            }
        }
    }
}