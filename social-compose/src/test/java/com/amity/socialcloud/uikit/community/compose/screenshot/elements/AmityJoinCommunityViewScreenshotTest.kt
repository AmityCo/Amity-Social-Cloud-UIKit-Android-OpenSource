package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityJoinCommunityView
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AmityJoinCommunityViewScreenshotTest : BaseScreenshotTest() {

    // 0.1% tolerance prevents font-rendering flakiness between JVMs
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
    }

    private fun fakeCommunity(): AmityCommunity = mockk(relaxed = true) {
        every { getDisplayName() } returns "Tech Enthusiasts"
        every { isPublic() } returns true
        every { isOfficial() } returns false
        every { getMemberCount() } returns 1234
        every { getCategories() } returns emptyList()
        every { getCommunityId() } returns "community_123"
        every { getAvatar() } returns null
    }

    @Test
    fun join_community_view_label_in_name_row_not_on_avatar() {
        composeTestRule.setContent {
            AmityJoinCommunityView(
                community = fakeCommunity(),
                label = "01",
                showLabelOnAvatar = false,
                onClick = {},
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/join_community_label_in_name_row_not_on_avatar.png",
                roborazziOptions = screenshotOptions,
            )
    }

    @Test
    fun join_community_view_label_on_avatar_with_gradient() {
        composeTestRule.setContent {
            AmityJoinCommunityView(
                community = fakeCommunity(),
                label = "01",
                showLabelOnAvatar = true,
                onClick = {},
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/join_community_label_on_avatar_with_gradient.png",
                roborazziOptions = screenshotOptions,
            )
    }
}
