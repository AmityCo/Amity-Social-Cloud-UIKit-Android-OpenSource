package com.amity.socialcloud.uikit.community.compose.post

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The classifier against the design's own enumerated cases.
 *
 * Every one of these came from the Logic page rather than from reading the implementation, so a rule
 * change in Figma shows up here as a red test and not as a screenshot somebody happens to look at.
 */
class AmityMediaRatioTest {

    private fun assertRatio(expected: Float, w: Int, h: Int) =
        assertEquals("%d x %d (ratio %.4f)".format(w, h, w.toFloat() / h), expected, AmityMediaRatio.classify(w, h), 0.0001f)

    @Test
    fun `landscape sources classify 16 to 9`() {
        assertRatio(AmityMediaRatio.LANDSCAPE, 1000, 500)   // 2.000
        assertRatio(AmityMediaRatio.LANDSCAPE, 1000, 700)   // 1.429
        assertRatio(AmityMediaRatio.LANDSCAPE, 1920, 1080)  // 1.778
    }

    @Test
    fun `near-square sources classify 1 to 1`() {
        assertRatio(AmityMediaRatio.SQUARE, 1000, 801)      // 1.248
        assertRatio(AmityMediaRatio.SQUARE, 1000, 900)      // 1.111
        assertRatio(AmityMediaRatio.SQUARE, 1000, 1000)     // 1.000
        assertRatio(AmityMediaRatio.SQUARE, 1000, 1100)     // 0.909
        assertRatio(AmityMediaRatio.SQUARE, 1000, 1200)     // 0.833
    }

    @Test
    fun `portrait sources classify 4 to 5`() {
        assertRatio(AmityMediaRatio.PORTRAIT, 1000, 1300)   // 0.769
        assertRatio(AmityMediaRatio.PORTRAIT, 1000, 1500)   // 0.667
        assertRatio(AmityMediaRatio.PORTRAIT, 1080, 1920)   // 0.563
    }

    /**
     * The boundaries have moved twice. Both thresholds sit OUTSIDE the square zone: exactly 1.250 is
     * landscape, exactly 0.800 is portrait.
     */
    @Test
    fun `both thresholds fall outside the square zone`() {
        assertRatio(AmityMediaRatio.LANDSCAPE, 1000, 800)   // 1.250 exactly
        assertRatio(AmityMediaRatio.SQUARE, 1000, 801)      // 1.248 — just inside
        assertRatio(AmityMediaRatio.SQUARE, 1000, 1200)     // 0.833 — just inside
        assertRatio(AmityMediaRatio.PORTRAIT, 1000, 1250)   // 0.800 exactly
    }

    /**
     * A 4:3 photo and a 3:4 photo are the shapes a phone camera produces by default, so they are the
     * likeliest real inputs of all.
     */
    @Test
    fun `phone camera defaults`() {
        assertRatio(AmityMediaRatio.LANDSCAPE, 4032, 3024)  // 1.333
        assertRatio(AmityMediaRatio.PORTRAIT, 3024, 4032)   // 0.750
    }

    /**
     * Missing dimensions must not look like a square source. This is the case that hid every video
     * preview rendering 1:1 for a week -- the value is right, but it means "we don't know".
     */
    @Test
    fun `absent dimensions fall back to square`() {
        assertRatio(AmityMediaRatio.SQUARE, 0, 0)
        assertRatio(AmityMediaRatio.SQUARE, 1000, 0)
        assertRatio(AmityMediaRatio.SQUARE, -1, 100)
    }
}
