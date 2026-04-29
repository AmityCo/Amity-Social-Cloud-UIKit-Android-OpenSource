package com.amity.socialcloud.uikit.community.compose.screenshot.base

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.RoborazziRule.Options
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Abstract base for all screenshot tests in social-compose.
 *
 * - Runs on JVM via Robolectric — no emulator required
 * - Screen: 411dp × 891dp @ xxhdpi (Pixel 4 equivalent)
 * - API 33 (Android 13) for stable Compose rendering
 * - Golden images: social-compose/src/test/golden/
 *
 * Commands:
 *   Record:  ./gradlew :social-compose:recordRoborazziDebug
 *   Verify:  ./gradlew :social-compose:verifyRoborazziDebug
 *
 * After an intentional UI change: run recordRoborazziDebug, inspect PNGs, commit updated goldens.
 *
 * Note: Each subclass test must call
 *   composeTestRule.onRoot().captureRoboImage("src/test/golden/<test_name>.png")
 * at the end of the test body. The RoborazziRule here only sets the output directory;
 * CaptureType.None disables automatic capture to prevent "No compose hierarchies found" errors.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
abstract class BaseScreenshotTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @get:Rule
    val roborazziRule: RoborazziRule = RoborazziRule(
        options = Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        )
    )
}
