package com.amity.socialcloud.uikit.community.compose.post.autoplay

import android.content.Context
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class AmityFeedAutoPlayControllerTest {

    private lateinit var controller: AmityFeedAutoPlayController
    private val source = AmityFeedAutoPlaySource.Room(playbackUrl = "https://example.test/a.m3u8", isLive = false)

    @Before
    fun setUp() {
        // activate() resolves the url on Rx schedulers; run them in place on the JVM.
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        AmityFeedAutoPlayConfig.visibleThreshold = 0.5f
        controller = AmityFeedAutoPlayController(mockk<Context>(relaxed = true))
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun thePlayingFrameKeepsThePlayerWhileItStaysAtHalf() {
        controller.report(key = "playing", fraction = 0.6f, top = 0f, source = source)
        controller.activate("playing")
        controller.report(key = "other", fraction = 1f, top = 400f, source = source)

        assertEquals("playing", controller.pickCandidate())
    }

    @Test
    fun theMostVisibleFrameTakesOverOnceThePlayingOneDropsBelowHalf() {
        controller.report(key = "playing", fraction = 0.6f, top = 0f, source = source)
        controller.activate("playing")
        controller.report(key = "other", fraction = 1f, top = 400f, source = source)
        controller.report(key = "playing", fraction = 0.4f, top = 0f, source = source)

        assertEquals("other", controller.pickCandidate())
    }

    @Test
    fun noFrameIsPickedBelowTheThreshold() {
        controller.report(key = "a", fraction = 0.49f, top = 0f, source = source)

        assertNull(controller.pickCandidate())
    }

    @Test
    fun theMostVisibleFrameIsPicked() {
        controller.report(key = "a", fraction = 0.7f, top = 0f, source = source)
        controller.report(key = "b", fraction = 0.9f, top = 500f, source = source)

        assertEquals("b", controller.pickCandidate())
    }

    @Test
    fun theTopmostFrameWinsWhenTwoAreEquallyVisible() {
        controller.report(key = "lower", fraction = 1f, top = 900f, source = source)
        controller.report(key = "upper", fraction = 1f, top = 100f, source = source)

        assertEquals("upper", controller.pickCandidate())
    }

    @Test
    fun anUnregisteredFrameIsNoLongerPicked() {
        controller.report(key = "a", fraction = 1f, top = 0f, source = source)
        controller.unregister("a")

        assertNull(controller.pickCandidate())
    }

    @Test
    fun aFrameHalfOnScreenCounts() {
        assertEquals(0.5f, visibleFraction(visibleHeight = 300f, frameHeight = 600f, screenHeight = 2000f), 0.001f)
    }

    @Test
    fun aFrameTallerThanTheScreenCountsOnceItFillsHalfOfIt() {
        // A 9:16 clip 2400 px tall on a 2000 px screen, 1000 px of it visible: 42% of the frame,
        // but half the screen.
        assertEquals(0.5f, visibleFraction(visibleHeight = 1000f, frameHeight = 2400f, screenHeight = 2000f), 0.001f)
    }

    @Test
    fun aFrameOffScreenDoesNotCount() {
        assertEquals(0f, visibleFraction(visibleHeight = 0f, frameHeight = 600f, screenHeight = 2000f), 0.001f)
    }

    @Test
    fun aPinnedHeaderHidesThePartOfTheFrameUnderIt() {
        // The frame shows from 100 to 400 px, and a header is pinned down to 250 px.
        assertEquals(150f, uncoveredHeight(visibleTop = 100f, visibleBottom = 400f, coveredTop = 250f), 0.001f)
        assertEquals(300f, uncoveredHeight(visibleTop = 100f, visibleBottom = 400f, coveredTop = 0f), 0.001f)
        assertEquals(0f, uncoveredHeight(visibleTop = 100f, visibleBottom = 400f, coveredTop = 500f), 0.001f)
    }

    @Test
    fun theLowestPinnedHeaderSetsTheCoveredTop() {
        assertEquals(0f, controller.coveredTop, 0.001f)
        controller.reportCover(id = "top_bar", bottom = 150f)
        controller.reportCover(id = "tab_row", bottom = 280f)
        assertEquals(280f, controller.coveredTop, 0.001f)

        controller.removeCover("tab_row")
        assertEquals(150f, controller.coveredTop, 0.001f)
    }
}
