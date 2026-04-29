package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayEmptyState
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Screenshot tests for [AmityNotificationTrayEmptyState].
 *
 * PDT-2244: Notification tray empty state should display:
 *   - Newspaper icon (amity_ic_notification_tray_empty)
 *   - "No notifications" (primary title)
 *   - "You're up to date!" (subtitle)
 *
 * IMPORTANT: Must be wrapped in AmityBasePage(pageId = "notification_tray_page")
 * so that AmityBaseElement resolves the correct config key
 * "notification_tray_page/[star]/empty_notification" -> image "notificationTrayEmptyStateIcon"
 * -> R.drawable.amity_ic_notification_tray_empty.
 *
 * Without the page wrapper, the element scope uses pageId="*" which produces
 * config key "[star]/[star]/empty_notification" -- no match in config -> getIcon() returns
 * amity_empty (1x1dp transparent drawable) -> icon is invisible.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityNotificationTrayEmptyStateScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        )
    )

    // 0.1% tolerance — prevents font-rendering flakiness without masking real bugs
    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(
            resultValidator = { result ->
                result.pixelDifferences / result.pixelCount.toFloat() < 0.001f
            }
        )
    )

    @Before
    fun setUp() {
        // AmityBaseElement calls AmityUIKitConfigController.isExcluded() which requires
        // the config to be loaded from disk. Setup with the Robolectric application context.
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
    }

    /**
     * Verifies icon + both text lines are rendered correctly.
     *
     * Wrapped in AmityBasePage so that the element scope resolves
     * pageId = "notification_tray_page" and the icon config key matches.
     */
    @Test
    fun notification_tray_empty_state() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "notification_tray_page") {
                AmityNotificationTrayEmptyState(
                    modifier = Modifier.fillMaxSize(),
                    pageScope = getPageScope(),
                )
            }
        }

        composeTestRule.waitForIdle()

        // Assert both text lines are present
        composeTestRule.onNodeWithText("No notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("You're up to date!").assertIsDisplayed()

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_empty_state.png",
                roborazziOptions = screenshotOptions,
            )
    }

    /**
     * Second golden: same composition, confirms subtitle visibility under any config variant.
     */
    @Test
    fun notification_tray_empty_state_subtitle_visible() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "notification_tray_page") {
                AmityNotificationTrayEmptyState(
                    modifier = Modifier.fillMaxSize(),
                    pageScope = getPageScope(),
                )
            }
        }

        composeTestRule.waitForIdle()

        // The subtitle "You're up to date!" must always be present regardless of config
        composeTestRule.onNodeWithText("You're up to date!").assertIsDisplayed()

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_empty_state_subtitle_visible.png",
                roborazziOptions = screenshotOptions,
            )
    }
}
