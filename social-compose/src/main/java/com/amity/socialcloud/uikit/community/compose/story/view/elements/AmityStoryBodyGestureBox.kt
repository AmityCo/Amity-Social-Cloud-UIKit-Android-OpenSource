package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.utils.interceptTap

@Composable
fun AmityStoryBodyGestureBox(
    modifier: Modifier = Modifier,
    onTap: (Boolean) -> Unit,
    onHold: (Boolean) -> Unit,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    var verticalDragAmount by remember {
        mutableFloatStateOf(0f)
    }

    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .padding(top = 64.dp)
            .pointerInput(Unit) {
                interceptTap {
                    val screenWidth = configuration.screenWidthDp.dp.toPx()
                    val tapWidthOffset = it.x
                    val isTapRight = tapWidthOffset > screenWidth / 2
                    onTap(isTapRight)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onHold(true)
                        tryAwaitRelease()
                        onHold(false)
                    }
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (verticalDragAmount > 0) {
                            onSwipeDown()
                        } else {
                            onSwipeUp()
                        }
                        verticalDragAmount = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    verticalDragAmount += dragAmount
                }
            }
    )
}