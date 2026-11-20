package com.amity.socialcloud.uikit.community.compose.post.autoplay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The gating table of FeedVideoAutoPlayElement v1 REQ-002, REQ-005 and REQ-006. */
class AmityFeedAutoPlayGateTest {

    private fun allowed(
        switchOn: Boolean = true,
        choice: AmityFeedAutoPlayChoice = AmityFeedAutoPlayChoice.WIFI_AND_DATA,
        wifi: Boolean = true,
        dataSaver: Boolean = false,
    ) = isFeedAutoPlayAllowed(
        isNetworkSwitchOn = switchOn,
        choice = choice,
        isOnWifi = wifi,
        isDataSaverOn = dataSaver,
    )

    // tc: TC-feed-video-autoplay-035
    @Test
    fun wifiAndDataPlaysOnBothConnections() {
        assertTrue(allowed(choice = AmityFeedAutoPlayChoice.WIFI_AND_DATA, wifi = true))
        assertTrue(allowed(choice = AmityFeedAutoPlayChoice.WIFI_AND_DATA, wifi = false))
    }

    // tc: TC-feed-video-autoplay-035
    @Test
    fun wifiOnlyPlaysOnWifiOnly() {
        assertTrue(allowed(choice = AmityFeedAutoPlayChoice.WIFI_ONLY, wifi = true))
        assertFalse(allowed(choice = AmityFeedAutoPlayChoice.WIFI_ONLY, wifi = false))
    }

    // tc: TC-feed-video-autoplay-035
    @Test
    fun neverNeverPlays() {
        assertFalse(allowed(choice = AmityFeedAutoPlayChoice.NEVER, wifi = true))
        assertFalse(allowed(choice = AmityFeedAutoPlayChoice.NEVER, wifi = false))
    }

    // tc: TC-feed-video-autoplay-026
    @Test
    fun theNetworkSwitchOffOverridesEveryChoice() {
        AmityFeedAutoPlayChoice.entries.forEach { choice ->
            assertFalse(allowed(switchOn = false, choice = choice, wifi = true))
        }
    }

    // tc: TC-feed-video-autoplay-020
    @Test
    fun dataSaverStopsAutoplay() {
        assertFalse(allowed(dataSaver = true))
    }

    @Test
    fun offlineShowsTheThumbnail() {
        assertFalse(
            isFeedAutoPlayAllowed(
                isNetworkSwitchOn = true,
                choice = AmityFeedAutoPlayChoice.WIFI_AND_DATA,
                isOnWifi = false,
                isDataSaverOn = false,
                isOnline = false,
            )
        )
    }

    // tc: TC-feed-video-autoplay-040
    @Test
    fun withNothingSavedTheChoiceIsWifiAndData() {
        assertEquals(AmityFeedAutoPlayChoice.WIFI_AND_DATA, AmityFeedAutoPlayChoice.DEFAULT)
    }
}
