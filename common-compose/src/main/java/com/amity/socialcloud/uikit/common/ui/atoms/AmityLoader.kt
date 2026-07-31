package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Which Loader sub-frame renders.
 *
 * - Spinner: static donut (track ring + coloured arc) spun in code. Binds the
 *   Surface-Loaders-Spinner-Background / -Loader token pair.
 * - Upload: Upload controller ring/arc driven by [AmityLoader.progress], with optional cancel glyph
 *   or (at [AmityLoaderSize.Large]) a countdown numeral. Binds Surface-Loaders-UploadController-*.
 * - Refresher: pull-to-refresh elevated disc + update icon. Binds its own dedicated pair,
 *   Surface-Loaders-Refresher-General (disc) and Icon-Loaders-Refresher-General (glyph) - it does
 *   NOT reuse the Spinner's arc token.
 */
enum class AmityLoaderVariant {
    Spinner,
    Upload,
    // Indeterminate/infinite variant of the Upload controller (designer-confirmed):
    // Upload-controller chrome (2px ring, white UploadController tokens) spun continuously, for
    // media/preview loading where progress is unknown. Working name "uploadSpinner".
    UploadSpinner,
    Refresher,
}

/**
 * Intrinsic box variant. sm/lg apply to Spinner; medium/large apply to Upload; ignored for Refresher
 * (fixed 40x40 frame). Large unlocks the Upload countdown-numeral centre content.
 */
enum class AmityLoaderSize {
    Sm,      // 24x24 - Spinner only
    Lg,      // 40x40 - Spinner only
    Medium,  // 40x40 - Upload only
    Large,   // 72x72 - Upload only
}

/**
 * AmityLoader - the atomic loading indicator (Spinner / Upload controller / Refresher).
 *
 * Presentational and always-animating. Colour comes entirely from the token engine via
 * [AmityTheme.token]; the only interactive target is the Upload controller's cancel hit-area, exposed
 * through [onCancel].
 *
 * @param variant which sub-frame renders (selects the token family). Required.
 * @param modifier layout modifier applied to the loader box.
 * @param size intrinsic box variant. Defaults to Lg for Spinner and Medium for Upload; ignored for
 *   Refresher. Pass sm/lg for Spinner or medium/large for Upload.
 * @param progress upload percentage 0..100. Upload only - drives the determinate arc fill and, at
 *   size Large with no [onCancel], the countdown numeral.
 * @param onCancel Upload only. Supplying it renders the Loading+Cancel cross hit-area and fires on tap;
 *   omit for the plain Loading variant.
 * @param icon optional caller-supplied glyph. Used as the Upload cancel cross (when [onCancel] is set)
 *   and as the Refresher update icon. If null, the Upload cancel cross is drawn with two strokes.
 */
@Composable
fun AmityLoader(
    variant: AmityLoaderVariant,
    modifier: Modifier = Modifier,
    size: AmityLoaderSize = if (variant == AmityLoaderVariant.Upload) AmityLoaderSize.Medium else AmityLoaderSize.Lg,
    progress: Float = 0f,
    onCancel: (() -> Unit)? = null,
    @DrawableRes icon: Int? = null,
) {
    when (variant) {
        AmityLoaderVariant.Spinner -> Spinner(size = size, modifier = modifier)
        AmityLoaderVariant.UploadSpinner -> UploadSpinner(size = size, modifier = modifier)
        AmityLoaderVariant.Upload -> UploadController(
            size = size,
            progress = progress,
            onCancel = onCancel,
            icon = icon,
            modifier = modifier,
        )
        AmityLoaderVariant.Refresher -> Refresher(icon = icon, modifier = modifier)
    }
}

// ---- Spinner --------------------------------------------------------------------------------------

@Composable
private fun Spinner(size: AmityLoaderSize, modifier: Modifier) {
    val boxDp = if (size == AmityLoaderSize.Sm) 24.dp else 40.dp
    val strokeDp = if (size == AmityLoaderSize.Sm) 2.dp else 3.dp

    val trackColor = AmityTheme.token("Surface/Loaders/Spinner/Primary/Background")
    val arcColor = AmityTheme.token("Surface/Loaders/Spinner/Primary/Icon")

    // Indeterminate: a fixed 270-degree arc spun continuously.
    val transition = rememberInfiniteTransition(label = "spinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "spinnerAngle",
    )

    Canvas(modifier = modifier.size(boxDp).rotate(angle)) {
        val stroke = strokeDp.toPx()
        val inset = stroke / 2f
        val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
        val topLeft = Offset(inset, inset)
        // Full-circle track ring.
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
        // Moving 270-degree coloured arc.
        drawArc(
            color = arcColor,
            startAngle = -90f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

// ---- Upload spinner (indeterminate) ---------------------------------------------------------------

/**
 * Indeterminate "uploadSpinner" — the Upload-controller chrome (2px ring, white UploadController
 * tokens) spun continuously, for media/preview loading where progress is unknown (contrast the
 * determinate [UploadController]). Designer-confirmed variant; replaces the chat
 * link-preview media spinner that was previously hand-rolled with raw white.
 */
@Composable
private fun UploadSpinner(size: AmityLoaderSize, modifier: Modifier) {
    val boxDp = when (size) {
        AmityLoaderSize.Sm -> 24.dp
        AmityLoaderSize.Large -> 72.dp
        else -> 40.dp // Lg / Medium
    }
    val strokeDp = 2.dp

    // Same white token pair as the Upload controller: translucent-white track + white moving head.
    val trackColor = AmityTheme.token("Surface/Loaders/UploadController/Background")
    val arcColor = AmityTheme.token("Surface/Loaders/UploadController/Loader")

    val transition = rememberInfiniteTransition(label = "uploadSpinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "uploadSpinnerAngle",
    )

    Canvas(modifier = modifier.size(boxDp).rotate(angle)) {
        val stroke = strokeDp.toPx()
        val inset = stroke / 2f
        val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
        val topLeft = Offset(inset, inset)
        // Full-circle track ring.
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
        // Moving 90-degree head.
        drawArc(
            color = arcColor,
            startAngle = -90f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

// ---- Upload controller ----------------------------------------------------------------------------

@Composable
private fun UploadController(
    size: AmityLoaderSize,
    progress: Float,
    onCancel: (() -> Unit)?,
    @DrawableRes icon: Int?,
    modifier: Modifier,
) {
    val boxDp = if (size == AmityLoaderSize.Large) 72.dp else 40.dp
    val strokeDp = 2.dp

    // Background ring - the token bakes its own alpha (translucent light / opaque dark); it is
    // authoritative, do not apply an additional alpha on top of it.
    val ringColor = AmityTheme.token("Surface/Loaders/UploadController/Background")
    val arcColor = AmityTheme.token("Surface/Loaders/UploadController/Loader")

    val sweep = (progress.coerceIn(0f, 100f) / 100f) * 360f

    Box(modifier = modifier.size(boxDp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(boxDp)) {
            val stroke = strokeDp.toPx()
            val inset = stroke / 2f
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = ringColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Butt),
            )
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Butt),
            )
        }

        // Centre content.
        when {
            onCancel != null -> {
                // The cross-l Icon Button hit-area is 24x24 (Medium) / 32x32 (Large), centered on
                // the ring; the smaller glyph below is its own internal inset, not the tap target.
                val hitAreaDp = if (size == AmityLoaderSize.Large) 32.dp else 24.dp
                val glyphDp = if (size == AmityLoaderSize.Large) 16.dp else 12.dp
                val tint = AmityTheme.token("Icon/Loaders/UploadController/Default")
                Box(
                    modifier = Modifier
                        .size(hitAreaDp)
                        .clickable(onClick = onCancel),
                    contentAlignment = Alignment.Center,
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icon),
                            contentDescription = "Cancel upload",
                            tint = tint,
                            modifier = Modifier.size(glyphDp),
                        )
                    } else {
                        Canvas(modifier = Modifier.size(glyphDp)) {
                            val s = this.size.minDimension
                            val w = 1.5.dp.toPx()
                            drawLine(tint, Offset(0f, 0f), Offset(s, s), strokeWidth = w, cap = StrokeCap.Round)
                            drawLine(tint, Offset(s, 0f), Offset(0f, s), strokeWidth = w, cap = StrokeCap.Round)
                        }
                    }
                }
            }
            size == AmityLoaderSize.Large -> {
                // Countdown numeral: 32/48/700, matching the typography.display role, plus its own
                // tighter letter-spacing.
                val textColor = AmityTheme.token("Text/Loaders/UploadController/Default")
                Text(
                    text = progress.coerceIn(0f, 100f).toInt().toString(),
                    color = textColor,
                    style = AmityTheme.typography.display.copy(letterSpacing = (-0.4).sp),
                )
            }
        }
    }
}

// ---- Refresher ------------------------------------------------------------------------------------

@Composable
private fun Refresher(@DrawableRes icon: Int?, modifier: Modifier) {
    // Fixed 40x40 frame: elevated disc holding a 12px update glyph. Refresher owns its own
    // dedicated token pair - it does not reuse the Spinner's arc token.
    val discDp = 26.67.dp
    val iconDp = 12.dp
    val discColor = AmityTheme.token("Surface/Loaders/Refresher/General")
    val iconColor = AmityTheme.token("Icon/Loaders/Refresher/General")
    Box(modifier = modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(discDp)) {
            drawCircle(color = discColor)
        }
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = "Refresh",
                tint = iconColor,
                modifier = Modifier.size(iconDp),
            )
        }
    }
}
