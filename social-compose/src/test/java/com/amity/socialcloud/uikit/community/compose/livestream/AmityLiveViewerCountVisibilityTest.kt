package com.amity.socialcloud.uikit.community.compose.livestream

import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountConfig
import com.amity.socialcloud.sdk.model.core.settings.AmityLiveViewerCountMode
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.isCountVisible
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AmityLiveViewerCountVisibilityTest {

    private fun config(mode: AmityLiveViewerCountMode, threshold: Int = 50) =
        mockk<AmityLiveViewerCountConfig> {
            every { this@mockk.mode } returns mode
            every { this@mockk.threshold } returns threshold
        }

    @Test
    fun `always show reveals the count to a viewer at any count`() {
        assertTrue(
            isCountVisible(
                viewerCount = 1,
                isHostOrCoHost = false,
                config = config(AmityLiveViewerCountMode.ALWAYS_SHOW),
            )
        )
    }

    @Test
    fun `hide entirely withholds the count from a viewer at any count`() {
        assertFalse(
            isCountVisible(
                viewerCount = 9_999,
                isHostOrCoHost = false,
                config = config(AmityLiveViewerCountMode.HIDE_ENTIRELY),
            )
        )
    }

    @Test
    fun `above minimum compares inclusively against the threshold`() {
        val aboveMinimum = config(AmityLiveViewerCountMode.SHOW_ABOVE_MINIMUM, threshold = 50)

        assertFalse(isCountVisible(49, isHostOrCoHost = false, config = aboveMinimum))
        assertTrue(isCountVisible(50, isHostOrCoHost = false, config = aboveMinimum))
        assertTrue(isCountVisible(51, isHostOrCoHost = false, config = aboveMinimum))
    }

    @Test
    fun `an unknown count is treated as below any threshold`() {
        assertFalse(
            isCountVisible(
                viewerCount = null,
                isHostOrCoHost = false,
                config = config(AmityLiveViewerCountMode.SHOW_ABOVE_MINIMUM, threshold = 1),
            )
        )
    }

    @Test
    fun `hosts and co-hosts see the count under every mode`() {
        AmityLiveViewerCountMode.values().forEach { mode ->
            assertTrue(
                "role exemption should win over $mode",
                isCountVisible(
                    viewerCount = 1,
                    isHostOrCoHost = true,
                    config = config(mode, threshold = 1000),
                )
            )
        }
    }

    @Test
    fun `a failed config read falls open to showing the count`() {
        assertTrue(isCountVisible(viewerCount = 1, isHostOrCoHost = false, config = null))
    }

}
