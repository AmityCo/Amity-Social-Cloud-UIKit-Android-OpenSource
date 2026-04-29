package com.amity.socialcloud.uikit.community.compose.screenshot.pages

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPage
import com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPageMode
import com.amity.socialcloud.uikit.community.compose.screenshot.base.FakeViewModelStoreOwner
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Page-level screenshot tests for [AmityEventSetupPage] — Event Details field.
 *
 * Bug (PDT-1913): The "Event details" text field was styled using the Ulta-style
 * pattern — a grey rounded-rectangle container (AmityBasicTextField inside a Column
 * with baseShade4 background) — instead of the main UIKit style (AmityTextField with
 * transparent background + HorizontalDivider). The character count was also rendered
 * below the field instead of in the header Row alongside the title.
 *
 * Fix: Replace AmityBasicTextField + grey Column with AmityTextField (transparent),
 * move char count "0/1,000" to the header Row, add HorizontalDivider below the field.
 *
 * Renders [AmityEventSetupPage] in Create mode (no communityId) so LaunchedEffect
 * SDK calls are skipped — only AmityCoreClient is mocked to satisfy composables that
 * read session state at composition time.
 *
 * TDD mode: Record-then-fix
 *
 * States:
 *   1. event_setup_page_create_mode — full Create Event page (empty state)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityEventDetailsFieldScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        ),
    )

    // ── Setup / Teardown ──────────────────────────────────────────────────────

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        mockkObject(AmityCoreClient)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
        every { AmityCoreClient.getUserId() } returns "user-001"
        every { AmityCoreClient.getCurrentUser() } returns Flowable.never()
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    /**
     * Full Create Event page in empty state.
     *
     * Verifies the Event Details field uses AmityTextField (transparent background,
     * HorizontalDivider) with "0/1,000" char count in the header Row — not a grey
     * rounded-rectangle container with char count below the field.
     */
    @Test
    fun event_setup_page_create_mode() {
        val fakeOwner = FakeViewModelStoreOwner(emptyMap())
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {
                AmityEventSetupPage(
                    mode = AmityEventSetupPageMode.Create(),
                )
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/event_setup_page_create_mode.png")
    }
}
