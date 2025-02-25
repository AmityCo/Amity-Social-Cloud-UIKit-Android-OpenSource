package com.amity.socialcloud.uikit.community.compose.story.create.elements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AmityStoryCameraShutterButtonElement(
    modifier: Modifier = Modifier,
    isImageCaptureMode: Boolean = true,
    maxRecordDurationSeconds: Int = 0,
    onImageCaptureClicked: () -> Unit = {},
    onVideoCaptureStarted: () -> Unit = {},
    onVideoCaptureStopped: () -> Unit = {},
) {
    val haptics = LocalHapticFeedback.current

    var isRecording by remember { mutableStateOf(false) }
    val animateFloat = remember { Animatable(0f) }

    LaunchedEffect(animateFloat, isRecording, isImageCaptureMode) {
        if (isImageCaptureMode) return@LaunchedEffect
        if (isRecording) {
            animateFloat.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = maxRecordDurationSeconds * 1000,
                    easing = LinearEasing
                )
            )
        }
        if (animateFloat.value == 1f && isRecording) {
            isRecording = false
        }
    }

    LaunchedEffect(isRecording) {
        if (isImageCaptureMode) return@LaunchedEffect
        if (isRecording) {
            onVideoCaptureStarted()
        } else {
            onVideoCaptureStopped()
            animateFloat.animateTo(0f)
        }
    }

    Canvas(modifier = modifier
        .size(72.dp)
        .pointerInput(isImageCaptureMode) {
            detectTapGestures(
                onPress = {
                    if (!isImageCaptureMode) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        isRecording = true
                        tryAwaitRelease()
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        isRecording = false
                    }
                },
                onTap = {
                    if (isImageCaptureMode) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onImageCaptureClicked()
                    }
                }
            )
        }
    ) {
        val center = Offset(size.width / 2, size.height / 2)

        if (isRecording) {
            val outerCircleStrokeWidth = 4.dp.toPx()

            val innerCircleRadius = 24.dp.toPx()
            val radius = 72.dp.toPx()

            drawIntoCanvas {
                drawCircle(
                    color = Color(0xFF606170).copy(alpha = 0.16f),
                    radius = size.minDimension / 2,
                    center = center,
                )

                drawArc(
                    color = Color(0xFFFF305A),
                    startAngle = 270f,
                    sweepAngle = 360f * animateFloat.value,
                    useCenter = false,
                    topLeft = Offset(outerCircleStrokeWidth / 2, outerCircleStrokeWidth / 2),
                    size = Size(
                        radius - outerCircleStrokeWidth,
                        radius - outerCircleStrokeWidth
                    ),
                    style = Stroke(outerCircleStrokeWidth)
                )

                drawCircle(
                    color = Color.White,
                    radius = innerCircleRadius,
                    center = center,
                )

                drawRoundRect(
                    color = Color(0xFFF72C40),
                    topLeft = Offset(center.x - 12.dp.toPx(), center.y - 12.dp.toPx()),
                    size = size.copy(width = 24.dp.toPx(), height = 24.dp.toPx()),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        } else {
            val outerCircleStrokeWidth = 4.dp.toPx()

            val outerCircleRadius = size.minDimension / 2 - outerCircleStrokeWidth / 2
            val innerCircleRadius = 30.dp.toPx()

            drawIntoCanvas {
                drawCircle(
                    color = Color.White,
                    radius = outerCircleRadius,
                    center = center,
                    style = Stroke(outerCircleStrokeWidth)
                )

                drawCircle(
                    color = if (isImageCaptureMode) Color.White else Color(0xFFF72C40),
                    radius = innerCircleRadius,
                    center = center,
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFabc, showBackground = true)
@Composable
fun AmityStoryCameraShutterButtonElementPreview() {
    AmityStoryCameraShutterButtonElement(
        isImageCaptureMode = false,
    )
}