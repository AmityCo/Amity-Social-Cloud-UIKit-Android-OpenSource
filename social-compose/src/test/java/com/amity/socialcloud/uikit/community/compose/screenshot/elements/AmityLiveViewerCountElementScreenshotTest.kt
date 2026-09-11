package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountConfig
import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountMode
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityLiveViewerCountElement
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 * Covers the two forms the pill takes — count and LIVE-only — across every path that decides
 * between them, plus the pre-read state where nothing renders yet.
 */
class AmityLiveViewerCountElementScreenshotTest : BaseScreenshotTest() {

    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(
            resultValidator = { result ->
                result.pixelDifferences / result.pixelCount.toFloat() < 0.001f
            }
        )
    )

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        // Without this the LIVE-only pill renders the raw string key instead of "LIVE".
        DefaultAmitySocialStringProvider.initialize(ApplicationProvider.getApplicationContext())
    }

    private fun config(mode: AmityLiveViewerCountMode, threshold: Int = 50) =
        mockk<AmityLiveViewerCountConfig> {
            every { this@mockk.mode } returns mode
            every { this@mockk.threshold } returns threshold
        }

    private fun capture(
        name: String,
        viewerCount: Int?,
        isHostOrCoHost: Boolean = false,
        config: AmityLiveViewerCountConfig?,
        isConfigResolved: Boolean = true,
    ) {
        composeTestRule.setContent {
            AmityBasePage(pageId = "livestream_player_page") {
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    AmityLiveViewerCountElement(
                        pageScope = getPageScope(),
                        viewerCount = viewerCount,
                        isHostOrCoHost = isHostOrCoHost,
                        config = config,
                        isConfigResolved = isConfigResolved,
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/$name.png", roborazziOptions = screenshotOptions)
    }

    @Test
    fun live_viewer_count_element_always_show() {
        capture(
            name = "live_viewer_count_element_always_show",
            viewerCount = 8,
            config = config(AmityLiveViewerCountMode.ALWAYS_SHOW),
        )
    }

    @Test
    fun live_viewer_count_element_hide_entirely() {
        capture(
            name = "live_viewer_count_element_hide_entirely",
            viewerCount = 8,
            config = config(AmityLiveViewerCountMode.HIDE_ENTIRELY),
        )
    }

    @Test
    fun live_viewer_count_element_above_minimum_below_threshold() {
        capture(
            name = "live_viewer_count_element_above_minimum_below_threshold",
            viewerCount = 49,
            config = config(AmityLiveViewerCountMode.SHOW_ABOVE_MINIMUM, threshold = 50),
        )
    }

    @Test
    fun live_viewer_count_element_above_minimum_at_threshold() {
        capture(
            name = "live_viewer_count_element_above_minimum_at_threshold",
            viewerCount = 50,
            config = config(AmityLiveViewerCountMode.SHOW_ABOVE_MINIMUM, threshold = 50),
        )
    }

    @Test
    fun live_viewer_count_element_host_sees_count_under_hide_entirely() {
        capture(
            name = "live_viewer_count_element_host_sees_count_under_hide_entirely",
            viewerCount = 8,
            isHostOrCoHost = true,
            config = config(AmityLiveViewerCountMode.HIDE_ENTIRELY),
        )
    }

    @Test
    fun live_viewer_count_element_failed_read_shows_count() {
        capture(
            name = "live_viewer_count_element_failed_read_shows_count",
            viewerCount = 8,
            config = null,
        )
    }

    @Test
    fun live_viewer_count_element_awaiting_config_renders_nothing() {
        capture(
            name = "live_viewer_count_element_awaiting_config_renders_nothing",
            viewerCount = 8,
            config = null,
            isConfigResolved = false,
        )
    }

}
