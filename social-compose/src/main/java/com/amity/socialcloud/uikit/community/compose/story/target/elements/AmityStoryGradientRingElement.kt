package com.amity.socialcloud.uikit.community.compose.story.target.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityStoryGradientRingElement(
    modifier: Modifier = Modifier,
    isIndeterminate: Boolean,
    colors: List<Color>,
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val strokeWidth = 2.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth / 2

        drawIntoCanvas {
            val gradient = Brush.linearGradient(
                colors = if (colors.size < 2) {
                    listOf(colors.first(), colors.first())
                } else {
                    colors
                },
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height)
            )

            drawCircle(
                brush = gradient,
                radius = radius,
                center = center,
                style = Stroke(strokeWidth)
            )
        }
    }
    if (isIndeterminate) {
        CircularProgressIndicator(
            color = AmityTheme.colors.secondaryShade4,
            trackColor = Color.Transparent,
            modifier = modifier,
            strokeWidth = 2.dp
        )
    }
}

@Preview
@Composable
fun AmityStoryGradientRingElementPreview() {
    AmityStoryGradientRingElement(
        colors = listOf(
            Color(0xFF339AF9),
            Color(0xFF78FA58)
        ),
        isIndeterminate = true,
        modifier = Modifier.size(64.dp)
    )
}