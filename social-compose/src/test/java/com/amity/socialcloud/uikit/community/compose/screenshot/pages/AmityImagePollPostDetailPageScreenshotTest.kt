package com.amity.socialcloud.uikit.community.compose.screenshot.pages

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPage
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.amity.socialcloud.uikit.community.compose.screenshot.base.FakeViewModelStoreOwner
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityPostMenuViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakePostFactory
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Layer 2 full-page screenshot tests for image-poll posts on [AmityPostDetailPage].
 *
 * Covers all meaningful states of image-poll posts:
 *   - Voting mode: single-select unvoted, multi-select unvoted
 *   - Result mode: voted (single/multi), showPollResultInDetailFirst, closed, expired
 *   - Edge cases: zero votes, max options (10), under-review
 *
 * Three ViewModels are injected via [FakeViewModelStoreOwner].
 * [AmityPostPollElementViewModel] is created by its own factory (passed as explicit
 * factory to viewModel()) so it does not need to be registered in FakeViewModelStoreOwner.
 */
class AmityImagePollPostDetailPageScreenshotTest : BaseScreenshotTest() {

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
        // Ensure the flag is always reset before each test
        AmitySocialBehaviorHelper.showPollResultInDetailFirst = false
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
        // Always reset the singleton flag so test order doesn't matter
        AmitySocialBehaviorHelper.showPollResultInDetailFirst = false
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Allow up to 0.1% pixel change (≈3295 pixels) to tolerate minor
     * font-rendering differences between JVM processes on macOS.
     */
    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(
            resultValidator = { result ->
                result.pixelDifferences / result.pixelCount.toFloat() < 0.001f
            }
        )
    )

    private fun fakeOwner(
        pageVM: FakeAmityPostDetailPageViewModel = FakeAmityPostDetailPageViewModel(),
        menuVM: FakeAmityPostMenuViewModel = FakeAmityPostMenuViewModel(),
        commentVM: FakeAmityCommentTrayComponentViewModel = FakeAmityCommentTrayComponentViewModel(),
    ) = FakeViewModelStoreOwner(
        mapOf(
            AmityPostDetailPageViewModel::class.java to pageVM,
            AmityPostMenuViewModel::class.java to menuVM,
            AmityCommentTrayComponentViewModel::class.java to commentVM,
        )
    )

    private fun renderPage(
        owner: FakeViewModelStoreOwner,
        postId: String = "post-imgpoll-001",
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                AmityPostDetailPage(
                    id = postId,
                    style = AmityPostContentComponentStyle.DETAIL,
                    category = AmityPostCategory.GENERAL,
                    hideTarget = true,
                    showLivestreamPostExceeded = false,
                )
            }
        }
    }

    // ── Group 1: Voting mode — Single-select ─────────────────────────────────

    /**
     * SINGLE-select image-poll, OPEN, user tapped answer[0] but has NOT submitted yet.
     * The radio button next to answer[0] must be visible and selected.
     * This test verifies the fix for the bug where AmityPostPollElementViewModel.init
     * stored PollStateUiState(postId = null) instead of PollStateUiState(postId = postId),
     * causing find { it.postId == ... } to always return null and selectedIndex to be empty
     * → radio/checkbox was never rendered on first composition.
     */
    @Test
    fun image_poll_single_selected_not_submitted() {
        val postId = "post-imgpoll-sel-s-001"
        val post = FakePostFactory.imagePollPost(postId = postId)
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = postId)
        // Simulate tapping the first answer card; answer id pattern from FakePostFactory line 583.
        composeTestRule.onNodeWithTag("poll_answer_answer-img-${postId}-0")
            .performClick()
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_single_selected_not_submitted.png", roborazziOptions = screenshotOptions)
    }

    /**
     * MULTIPLE-select image-poll, OPEN, user tapped answers[0] and [1] but has NOT submitted.
     * Checkboxes next to both tapped answers must be visible and checked.
     */
    @Test
    fun image_poll_multi_selected_not_submitted() {
        val postId = "post-imgpoll-sel-m-001"
        val post = FakePostFactory.imagePollPostMulti(postId = postId)
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = postId)
        // Tap answer[0] then answer[1]
        composeTestRule.onNodeWithTag("poll_answer_answer-img-${postId}-0")
            .performClick()
        composeTestRule.onNodeWithTag("poll_answer_answer-img-${postId}-1")
            .performClick()
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_multi_selected_not_submitted.png", roborazziOptions = screenshotOptions)
    }

    /**
     * SINGLE-select image-poll, OPEN, not yet voted.
     * Voting UI should be visible — answer cards with image placeholders, no result overlay.
     */
    @Test
    fun image_poll_single_unvoted() {
        val post = FakePostFactory.imagePollPost()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_single_unvoted.png", roborazziOptions = screenshotOptions)
    }

    /**
     * SINGLE-select image-poll, OPEN, user already voted answer[0].
     * isVoted = true → composable enters result mode on first composition.
     * answer[0] should show a primary-colored highlight / voted indicator.
     */
    @Test
    fun image_poll_single_voted() {
        val post = FakePostFactory.imagePollPostVotedSingle()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_single_voted.png", roborazziOptions = screenshotOptions)
    }

    // ── Group 2: Voting mode — Multi-select ──────────────────────────────────

    /**
     * MULTIPLE-select image-poll, OPEN, not yet voted.
     * Voting UI with checkbox (not radio) affordance.
     */
    @Test
    fun image_poll_multi_unvoted() {
        val post = FakePostFactory.imagePollPostMulti()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_multi_unvoted.png", roborazziOptions = screenshotOptions)
    }

    /**
     * MULTIPLE-select image-poll, OPEN, user voted answers[0] and [1].
     * isVoted = true → result mode. Both voted answers highlighted.
     */
    @Test
    fun image_poll_multi_voted() {
        val post = FakePostFactory.imagePollPostVotedMulti()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_multi_voted.png", roborazziOptions = screenshotOptions)
    }

    // ── Group 3: Result mode variants ─────────────────────────────────────────

    /**
     * Result mode — MULTIPLE voted.
     * Both voted answers (index 0, 1) highlighted in result view.
     */
    @Test
    fun image_poll_result_voted_multi() {
        val post = FakePostFactory.imagePollPostVotedMulti(postId = "post-imgpoll-result-m")
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_result_voted_multi.png", roborazziOptions = screenshotOptions)
    }

    /**
     * showPollResultInDetailFirst = true forces result mode even when the user hasn't voted.
     * The flag is reset to false in tearDown.
     */
    @Test
    fun image_poll_result_view_first() {
        AmitySocialBehaviorHelper.showPollResultInDetailFirst = true
        val post = FakePostFactory.imagePollPost(postId = "post-imgpoll-viewfirst")
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_result_view_first.png", roborazziOptions = screenshotOptions)
    }

    // ── Group 4: Closed / Expired ─────────────────────────────────────────────

    /**
     * Poll status = CLOSED. Composable enters result mode regardless of isVoted.
     * "Poll ended" or equivalent label should be visible.
     */
    @Test
    fun image_poll_closed_status() {
        val post = FakePostFactory.imagePollPostClosed()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_closed_status.png", roborazziOptions = screenshotOptions)
    }

    /**
     * Poll closedAt is in the past (expired by time). Composable enters result mode
     * via the `poll.getClosedAt().isBefore(DateTime.now())` branch.
     */
    @Test
    fun image_poll_expired_time() {
        val post = FakePostFactory.imagePollPostExpired()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_expired_time.png", roborazziOptions = screenshotOptions)
    }

    // ── Group 5: Edge cases ───────────────────────────────────────────────────

    /**
     * All vote counts are 0. In result mode, percentage bars should render at 0%.
     * Verifies divide-by-zero safety and "0%" label rendering.
     */
    @Test
    fun image_poll_zero_votes() {
        val post = FakePostFactory.imagePollPostZeroVotes()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_zero_votes.png", roborazziOptions = screenshotOptions)
    }

    /**
     * 10 answers (maximum). Initial view shows 4 options; remaining 6 are collapsed.
     * Verifies "See all options" / expand button is visible.
     */
    @Test
    fun image_poll_max_options() {
        val post = FakePostFactory.imagePollPostMaxOptions()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_max_options.png", roborazziOptions = screenshotOptions)
    }

    /**
     * Post under review (reviewStatus = UNDER_REVIEW).
     * The composable blocks result mode — voting UI is shown even if isVoted = false.
     */
    @Test
    fun image_poll_under_review() {
        val post = FakePostFactory.imagePollPostUnderReview()
        val owner = fakeOwner(pageVM = FakeAmityPostDetailPageViewModel(fakePost = post))
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_under_review.png", roborazziOptions = screenshotOptions)
    }
}
