package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlin.math.roundToInt

/**
 * Which Progress sub-frame to render.
 *
 * - Scrubber: the determinate Video Controller — a Bar (Empty/Filled paint split) plus a draggable
 *   Knob marking the playback position. Binds the Surface-Progress-* family.
 * - Spinner: the indeterminate loading/buffering state. Has NO Knob and is not draggable, and sits
 *   outside the Progress token family entirely — it binds the Loaders family
 *   (Surface-Loaders-Spinner-Icon / Background). This capability is currently ungrounded against a
 *   real determinate/indeterminate component pairing and is kept as-is pending design confirmation.
 */
enum class AmityProgressVariant {
    Scrubber,
    Spinner,
}

/**
 * Bar-segment visual state for the Scrubber.
 *
 * - Empty: unplayed remainder of the Bar — binds Surface-Progress-Bar-Empty, corner radius 2dp.
 * - Filled: played portion of the Bar — binds Surface-Progress-Bar-Filled, corner radius 3dp.
 * - Loading: buffering overlay on the played segment. There is no dedicated Semantic-tier token for
 *   this state: it renders as a two-stop horizontal gradient from the Filled token's resolved
 *   color (opaque) down to a 10%-alpha tint of that same color, sharing the Filled segment's 3dp
 *   corner radius.
 */
enum class AmityProgressState {
    Empty,
    Filled,
    Loading,
}

/**
 * AmityProgress — the atomic linear-progress / video-scrubber control.
 *
 * Renders either the determinate [AmityProgressVariant.Scrubber] (Bar + draggable Knob) or the
 * indeterminate [AmityProgressVariant.Spinner]. Colour comes entirely from the token engine via
 * [AmityTheme.token].
 *
 * The Knob drag interaction is exposed via callbacks rather than managed internally: the caller
 * owns the authoritative [value] and updates it from [onSeek] / [onChange].
 *
 * @param variant which sub-frame to render.
 * @param modifier layout modifier applied to the control container.
 * @param value playback/progress position, 0..100 (%). Drives the Empty/Filled paint split on the
 *   Bar. Ignored when [variant] is [AmityProgressVariant.Spinner].
 * @param state Bar-segment visual state. Ignored when [variant] is [AmityProgressVariant.Spinner].
 * @param onSeek called continuously while the user drags the Knob (Scrubber only); the value is the
 *   live 0..100 position under the drag.
 * @param onChange called once when the user releases the Knob (Scrubber only), committing the
 *   new 0..100 position.
 */
@Composable
fun AmityProgress(
    variant: AmityProgressVariant,
    modifier: Modifier = Modifier,
    value: Float = 0f,
    state: AmityProgressState = AmityProgressState.Filled,
    onSeek: (Float) -> Unit = {},
    onChange: (Float) -> Unit = {},
) {
    when (variant) {
        AmityProgressVariant.Spinner -> AmityProgressSpinner(modifier = modifier)
        AmityProgressVariant.Scrubber -> AmityProgressScrubber(
            modifier = modifier,
            value = value,
            state = state,
            onSeek = onSeek,
            onChange = onChange,
        )
    }
}

@Composable
private fun AmityProgressSpinner(modifier: Modifier = Modifier) {
    // Spinner binds the Loaders family, not Progress.
    val loader = AmityTheme.token("Surface/Loaders/Spinner/Primary/Icon")
    val background = AmityTheme.token("Surface/Loaders/Spinner/Primary/Background")
    CircularProgressIndicator(
        modifier = modifier.size(24.dp),
        color = loader,
        trackColor = background,
        strokeWidth = 3.dp,
    )
}

@Composable
private fun AmityProgressScrubber(
    modifier: Modifier = Modifier,
    value: Float,
    state: AmityProgressState,
    onSeek: (Float) -> Unit,
    onChange: (Float) -> Unit,
) {
    // Outer frame: 20dp tall, 12dp horizontal inset on the fillable track. The Bar renders as two
    // independently-radiused segments (Empty / Filled-or-Loading) rather than one continuous
    // full-width track, and the Knob is positioned per the value-driven formula below rather than
    // clamped inside the track bounds.
    val emptyColor = AmityTheme.token("Surface/Progress/Bar/Empty")
    val filledColor = AmityTheme.token("Surface/Progress/Bar/Filled")
    val knobColor = AmityTheme.token("Surface/Progress/Knob/Default")
    val playedColor = when (state) {
        AmityProgressState.Empty -> emptyColor
        AmityProgressState.Filled, AmityProgressState.Loading -> filledColor
    }

    val trackHeight = 4.dp
    val emptyRadius = 2.dp
    val filledRadius = 3.dp
    val playedRadius = if (state == AmityProgressState.Empty) emptyRadius else filledRadius
    val knobDiameter = 24.dp
    val frameHeight = 20.dp
    val horizontalPadding = 12.dp
    // The Knob's center sits 4dp past the Filled segment's end; the Empty segment then starts
    // 8dp past the Knob's own leading edge (16dp past its rel-x) — this constant offset is what
    // produces the 8dp reveal gap straddling the Knob at every value.
    val knobLeadOffset = 4.dp
    val emptyLeadOffset = 16.dp

    var widthPx by remember { mutableFloatStateOf(0f) }
    val fraction = (value / 100f).coerceIn(0f, 1f)

    val density = LocalDensity.current
    val paddingPx = with(density) { horizontalPadding.toPx() }
    val knobLeadOffsetPx = with(density) { knobLeadOffset.toPx() }
    val emptyLeadOffsetPx = with(density) { emptyLeadOffset.toPx() }

    fun fractionFor(x: Float): Float {
        val trackWidthPx = (widthPx - 2 * paddingPx).coerceAtLeast(0f)
        return if (trackWidthPx <= 0f) 0f else ((x - paddingPx) / trackWidthPx).coerceIn(0f, 1f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(frameHeight)
            .onSizeChanged { widthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { onChange(fraction * 100f) },
                    onDrag = { change, _ ->
                        change.consume()
                        onSeek(fractionFor(change.position.x) * 100f)
                    },
                )
            },
    ) {
        val trackWidthPx = (widthPx - 2 * paddingPx).coerceAtLeast(0f)
        val trackEndPx = widthPx - paddingPx
        // value=0 is a special-cased home position: the Knob sits flush at rel-x 0 rather than
        // the general formula's rel-x 4 (which would otherwise place it 4dp too far right).
        val knobRelX = if (fraction <= 0f) 0f else knobLeadOffsetPx + trackWidthPx * fraction
        val filledRelX = paddingPx
        val filledWidthPx = if (fraction <= 0f) 0f else trackWidthPx * fraction
        // Naturally clamps to 0 at value=100 (the formula would otherwise compute a negative
        // width past the track's own right edge) — render nothing rather than a stray sliver.
        val emptyRelX = knobRelX + emptyLeadOffsetPx
        val emptyWidthPx = (trackEndPx - emptyRelX).coerceAtLeast(0f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(frameHeight)
                .drawBehind {
                    val trackY = (frameHeight.toPx() - trackHeight.toPx()) / 2f
                    val trackHeightPx = trackHeight.toPx()

                    if (emptyWidthPx > 0f) {
                        drawRoundRect(
                            color = emptyColor,
                            topLeft = Offset(emptyRelX, trackY),
                            size = Size(emptyWidthPx, trackHeightPx),
                            cornerRadius = CornerRadius(emptyRadius.toPx(), emptyRadius.toPx()),
                        )
                    }

                    if (filledWidthPx > 0f) {
                        val segmentTopLeft = Offset(filledRelX, trackY)
                        val segmentSize = Size(filledWidthPx, trackHeightPx)
                        val segmentRadius = CornerRadius(playedRadius.toPx(), playedRadius.toPx())
                        if (state == AmityProgressState.Loading) {
                            val gradient = Brush.horizontalGradient(
                                colors = listOf(playedColor, playedColor.copy(alpha = 0.1f)),
                                startX = filledRelX,
                                endX = filledRelX + filledWidthPx,
                            )
                            drawRoundRect(
                                brush = gradient,
                                topLeft = segmentTopLeft,
                                size = segmentSize,
                                cornerRadius = segmentRadius,
                            )
                        } else {
                            drawRoundRect(
                                color = playedColor,
                                topLeft = segmentTopLeft,
                                size = segmentSize,
                                cornerRadius = segmentRadius,
                            )
                        }
                    }
                }
        )

        // Knob: 24dp circle, vertically overflowing the 20dp frame by 2dp top/bottom (centered on
        // the frame's own mid-line), horizontally positioned per knobRelX above rather than
        // clamped inside the track bounds.
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val y = (frameHeight.toPx() - placeable.height) / 2f
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(knobRelX.roundToInt(), y.roundToInt())
                    }
                }
                .size(knobDiameter)
                .drawBehind {
                    drawCircle(
                        color = knobColor,
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = size.minDimension / 2f,
                    )
                }
        )
    }
}
