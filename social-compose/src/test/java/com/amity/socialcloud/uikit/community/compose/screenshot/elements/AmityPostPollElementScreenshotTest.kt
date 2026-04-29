package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostPollElementViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostPollElement
import com.amity.socialcloud.uikit.community.compose.screenshot.base.FakeViewModelStoreOwner
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakePostFactory
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
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
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.core.session.model.SessionState

/**
 * Layer 1 screenshot tests for [AmityPostPollElement].
 *
 * [AmityPostPollElement] uses [AmityPostPollElementViewModel] (injected via
 * [LocalViewModelStoreOwner]) for UI interaction state (expanded/result mode/selected options),
 * and reads poll data directly from [AmityPost.Data.POLL.getPoll()].asFlow().
 *
 * The ViewModel has no SDK dependencies and is safe to instantiate directly.
 * [AmityCoreClient] is mockkObject'd because the element calls isVisitor() and getUserId()
 * at compose time to compute canVote and check if the current user is the post creator.
 *
 * Scenarios:
 *   1. poll_open_voting       — OPEN poll, single-choice, not yet voted (voting UI shown)
 *   2. poll_open_result_mode  — OPEN poll that user already voted (result bars shown)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityPostPollElementScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        )
    )

    // ── Setup / Teardown ──────────────────────────────────────────────────────

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        mockkObject(AmityCoreClient)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
        every { AmityCoreClient.getUserId() } returns "user-other"  // not post creator → no "See results" link
        every { AmityCoreClient.getCurrentUser() } returns Flowable.never()
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Provides a [FakeViewModelStoreOwner] pre-populated with a real
     * [AmityPostPollElementViewModel]. The ViewModel is instantiated with the given
     * [postId] — it holds only UI state (expanded/result/selectedOptions) and makes
     * no SDK calls.
     */
    private fun fakeOwnerForPoll(postId: String): FakeViewModelStoreOwner =
        FakeViewModelStoreOwner(
            mapOf(AmityPostPollElementViewModel::class.java to AmityPostPollElementViewModel(postId))
        )

    // ── 1. Open poll, voting UI ───────────────────────────────────────────────

    /**
     * OPEN single-choice poll not yet voted.
     * Voting options (radio buttons), "Vote" button, and footer "N votes • X days left"
     * should all be visible.
     */
    @Test
    fun poll_open_voting() {
        val post = FakePostFactory.pollPost()
        val componentScope = mockk<AmityComposeComponentScope>(relaxed = true)
        val owner = fakeOwnerForPoll(post.getPostId())

        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                AmityPostPollElement(
                    componentScope = componentScope,
                    post = post,
                    style = AmityPostContentComponentStyle.FEED,
                    onClick = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/poll_open_voting.png")
    }

    // ── 2. Result mode — already voted ────────────────────────────────────────

    /**
     * Voted poll shows result bars (vote counts + percentage bars) instead of
     * radio buttons. The "Unvote" link appears in the footer.
     *
     * Uses a separate pollPost variant where isVoted = true so the composable
     * enters isResultState = true from the start.
     */
    @Test
    fun poll_open_result_mode() {
        val post = FakePostFactory.pollPostVoted()
        val componentScope = mockk<AmityComposeComponentScope>(relaxed = true)
        val owner = fakeOwnerForPoll(post.getPostId())

        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                AmityPostPollElement(
                    componentScope = componentScope,
                    post = post,
                    style = AmityPostContentComponentStyle.FEED,
                    onClick = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/poll_open_result_mode.png")
    }
}
