package com.amity.socialcloud.uikit.community.compose.post

import com.amity.socialcloud.sdk.model.core.file.AmityVideo

/**
 * The carousel's frame-ratio classifier, in one place.
 *
 * It lived twice -- once for the published carousel, once for the composer preview -- and the two
 * copies drifted: a threshold change landed in one and not the other. Sharing it makes that
 * impossible rather than merely unlikely.
 *
 * Both sources state their dimensions before rotation, so both need it applied: a locally picked file
 * through its extractor, an uploaded one through [dimensionsOf]. Keeping that in here means the two
 * paths cannot disagree about what a rotated video's shape is.
 */
object AmityMediaRatio {

    const val LANDSCAPE_THRESHOLD = 1.25f
    const val PORTRAIT_THRESHOLD = 0.80f

    const val LANDSCAPE = 16f / 9f
    const val SQUARE = 1f
    const val PORTRAIT = 0.8f

    /** Ordered conditions, matching the design's own phrasing. Both thresholds sit outside SQUARE. */
    fun classify(width: Int, height: Int): Float {
        if (width <= 0 || height <= 0) return SQUARE
        val ratio = width.toFloat() / height.toFloat()
        return when {
            ratio >= LANDSCAPE_THRESHOLD -> LANDSCAPE
            ratio <= PORTRAIT_THRESHOLD -> PORTRAIT
            else -> SQUARE
        }
    }

    /**
     * A video's **display** dimensions, from the stored pair its record carries and the rotation to
     * apply to them.
     *
     * The record states the encoded frame, so a phone-shot portrait clip reads landscape until the
     * turn is applied. Only a quarter turn changes the shape -- a half turn must not swap, which is
     * why this asks for `% 180` rather than for any rotation at all.
     */
    fun dimensionsOf(video: AmityVideo?): Pair<Int, Int>? =
        video?.let {
            val w = it.getWidth()
            val h = it.getHeight()
            if (it.getRotation() % 180 != 0) h to w else w to h
        }?.takeIf { it.first > 0 && it.second > 0 }
}
