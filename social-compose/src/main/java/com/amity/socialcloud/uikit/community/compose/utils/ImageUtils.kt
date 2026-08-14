package com.amity.socialcloud.uikit.community.compose.utils

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

// Work resolution for gaussianBlur(). The blur runs on a bitmap scaled to this width, so cost is
// independent of the source image and the radius below reads the same on every cover.
private const val BLUR_WORK_WIDTH = 200

// Box-blur radius applied at BLUR_WORK_WIDTH. Larger means blurrier.
private const val BLUR_RADIUS = 10

// Three box-blur passes approximate a true Gaussian closely enough to be indistinguishable.
private const val BLUR_PASSES = 3

/**
 * Returns a Gaussian-blurred copy of the bitmap.
 *
 * Modifier.blur needs API 31 and RenderScript is deprecated, so neither covers the API 24 floor
 * this UIKit supports. This scales the bitmap down to [BLUR_WORK_WIDTH], runs three box-blur
 * passes over it (a standard Gaussian approximation), and returns the result to be drawn back at
 * full size. Blurring before the upscale is what keeps the enlarged pixels from looking blocky.
 *
 * The source bitmap is never modified, so Coil's cached copy stays sharp.
 */
fun Bitmap.gaussianBlur(
    workWidth: Int = BLUR_WORK_WIDTH,
    radius: Int = BLUR_RADIUS,
): Bitmap = runCatching {
    if (width <= 0 || height <= 0 || radius <= 0) return this

    val scaled = if (width > workWidth) {
        val scaledHeight = (height * (workWidth.toFloat() / width)).toInt().coerceAtLeast(1)
        Bitmap.createScaledBitmap(this, workWidth, scaledHeight, true)
    } else {
        this
    }

    val w = scaled.width
    val h = scaled.height
    val pixels = IntArray(w * h)
    scaled.getPixels(pixels, 0, w, 0, 0, w, h)

    // Radius must stay inside the bitmap or the sliding window degenerates.
    val safeRadius = radius.coerceAtMost(minOf(w, h) / 2).coerceAtLeast(1)
    repeat(BLUR_PASSES) {
        boxBlurHorizontal(pixels, w, h, safeRadius)
        boxBlurVertical(pixels, w, h, safeRadius)
    }

    Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        .apply { setPixels(pixels, 0, w, 0, 0, w, h) }
}.getOrDefault(this)

private fun boxBlurHorizontal(pixels: IntArray, width: Int, height: Int, radius: Int) {
    val window = radius * 2 + 1
    val row = IntArray(width)
    for (y in 0 until height) {
        val offset = y * width
        var a = 0
        var r = 0
        var g = 0
        var b = 0
        for (i in -radius..radius) {
            val p = pixels[offset + i.coerceIn(0, width - 1)]
            a += p ushr 24 and 0xFF
            r += p ushr 16 and 0xFF
            g += p ushr 8 and 0xFF
            b += p and 0xFF
        }
        for (x in 0 until width) {
            row[x] = (a / window shl 24) or (r / window shl 16) or (g / window shl 8) or (b / window)
            val added = pixels[offset + (x + radius + 1).coerceIn(0, width - 1)]
            val removed = pixels[offset + (x - radius).coerceIn(0, width - 1)]
            a += (added ushr 24 and 0xFF) - (removed ushr 24 and 0xFF)
            r += (added ushr 16 and 0xFF) - (removed ushr 16 and 0xFF)
            g += (added ushr 8 and 0xFF) - (removed ushr 8 and 0xFF)
            b += (added and 0xFF) - (removed and 0xFF)
        }
        System.arraycopy(row, 0, pixels, offset, width)
    }
}

private fun boxBlurVertical(pixels: IntArray, width: Int, height: Int, radius: Int) {
    val window = radius * 2 + 1
    val column = IntArray(height)
    for (x in 0 until width) {
        var a = 0
        var r = 0
        var g = 0
        var b = 0
        for (i in -radius..radius) {
            val p = pixels[i.coerceIn(0, height - 1) * width + x]
            a += p ushr 24 and 0xFF
            r += p ushr 16 and 0xFF
            g += p ushr 8 and 0xFF
            b += p and 0xFF
        }
        for (y in 0 until height) {
            column[y] = (a / window shl 24) or (r / window shl 16) or (g / window shl 8) or (b / window)
            val added = pixels[(y + radius + 1).coerceIn(0, height - 1) * width + x]
            val removed = pixels[(y - radius).coerceIn(0, height - 1) * width + x]
            a += (added ushr 24 and 0xFF) - (removed ushr 24 and 0xFF)
            r += (added ushr 16 and 0xFF) - (removed ushr 16 and 0xFF)
            g += (added ushr 8 and 0xFF) - (removed ushr 8 and 0xFF)
            b += (added and 0xFF) - (removed and 0xFF)
        }
        for (y in 0 until height) {
            pixels[y * width + x] = column[y]
        }
    }
}

/**
 * Draws a bitmap over the whole area. Pass a [gaussianBlur] result to render it blurred.
 */
@Composable
fun BlurImage(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        modifier = modifier
    )
}
