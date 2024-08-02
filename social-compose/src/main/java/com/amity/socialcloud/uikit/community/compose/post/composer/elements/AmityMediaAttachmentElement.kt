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
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityDetailedMediaAttachmentComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityMediaAttachmentComponent

@Composable
fun AmityMediaAttachmentElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val isKeyboardOpen by isKeyboardVisible()
    val keyboardHeight by getKeyboardHeight()
    var showDetailedView by remember { mutableStateOf(true) }
    var verticalDragAmount by remember { mutableFloatStateOf(0f) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)


    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) {
            showDetailedView = false
        }
    }

    Column(
        modifier = modifier
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        val isDragUp = verticalDragAmount < 0
                        if (isDragUp) {
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
                            Color.Black.copy(alpha = 0.08f),
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
                )
            } else {
                AmityMediaAttachmentComponent(
                    pageScope = pageScope,
                    viewModel = viewModel,
                )

                Box(
                    modifier = modifier.size(
                        keyboardHeight.minus(if (isKeyboardOpen) 20.dp else 0.dp)
                    )
                )
                Box(modifier = modifier.size(6.dp))
            }
        }
    }
}