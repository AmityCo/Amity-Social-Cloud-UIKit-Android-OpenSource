package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostImagePollElement
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Layer 1 screenshot tests for [AmityPostImagePollElement].
 *
 * [AmityPostImagePollElement] is a pure-parameter composable — it takes all data directly
 * (answers, selectedIndex, isSingleSelected, etc.) and makes no ViewModel calls.
 * The only SDK dependency is [AmityCoreClient.getCurrentUser()] called via produceState,
 * which is mocked to Flowable.never() to prevent any real SDK initialisation.
 *
 * These tests verify the PDT-841 fix: radio buttons are shown correctly for single-select
 * polls and checkboxes for multi-select polls, with proper styling:
 *
 * Scenarios:
 *   1. image_poll_single_select_unvoted   — single-select, no answer selected (all gray)
 *   2. image_poll_single_select_voted     — single-select, answer[0] selected (blue radio)
 *   3. image_poll_multi_select_voted      — multi-select, answer[0] selected (checkbox icon)
 *   4. image_poll_result_mode             — result mode (percentage overlay, no radio/checkbox)
 */
class AmityPostImagePollElementScreenshotTest : BaseScreenshotTest() {

    // ── Pixel tolerance ───────────────────────────────────────────────────────

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

    // ── Setup / Teardown ──────────────────────────────────────────────────────

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        mockkObject(AmityCoreClient)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
        every { AmityCoreClient.getUserId() } returns "user-001"
        // Flowable.never() prevents the produceState collector from blocking / timing out
        every { AmityCoreClient.getCurrentUser() } returns Flowable.never()
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Builds two fake poll answers with image = null (no Coil network request needed).
     * relaxed = true means unmocked methods return sensible defaults.
     */
    private fun fakeAnswers(
        answer1Text: String = "Option A",
        answer2Text: String = "Option B",
        votedIndex: Int? = null,
    ): List<AmityPollAnswer> {
        val answer1 = mockk<AmityPollAnswer>(relaxed = true) {
            every { data } returns answer1Text
            every { dataType } returns "image"
            every { voteCount } returns 8
            every { isVotedByUser } returns (votedIndex == 0)
            every { id } returns "answer-img-001"
            every { getImage() } returns null
        }
        val answer2 = mockk<AmityPollAnswer>(relaxed = true) {
            every { data } returns answer2Text
            every { dataType } returns "image"
            every { voteCount } returns 3
            every { isVotedByUser } returns (votedIndex == 1)
            every { id } returns "answer-img-002"
            every { getImage() } returns null
        }
        return listOf(answer1, answer2)
    }

    // ── 1. Single-select, nothing selected ───────────────────────────────────

    /**
     * PDT-841: unvoted single-select poll.
     * No answer is selected — radio buttons should NOT be visible (they only appear on
     * the selected item). Both cards render with a gray border.
     */
    @Test
    fun image_poll_single_select_unvoted() {
        val answers = fakeAnswers()

        composeTestRule.setContent {
            AmityPostImagePollElement(
                answers = answers,
                selectedIndex = emptyList(),
                isSingleSelected = true,
                viewResultMode = false,
                canVote = true,
                totalVoteCount = 11,
            )
        }
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_single_select_unvoted.png", roborazziOptions = screenshotOptions)
    }

    // ── 2. Single-select, first answer selected ───────────────────────────────

    /**
     * PDT-841 core fix: single-select poll with answer[0] selected.
     * A blue RadioButton should appear in the top-left corner of answer[0]'s card.
     * answer[1]'s card should NOT show a radio button (no selected indicator).
     * The selected card should have a blue border (primary color).
     */
    @Test
    fun image_poll_single_select_voted() {
        val answers = fakeAnswers(votedIndex = 0)

        composeTestRule.setContent {
            AmityPostImagePollElement(
                answers = answers,
                selectedIndex = listOf(0),
                isSingleSelected = true,
                viewResultMode = false,
                canVote = true,
                totalVoteCount = 11,
            )
        }
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_single_select_voted.png", roborazziOptions = screenshotOptions)
    }

    // ── 3. Multi-select, first answer selected ────────────────────────────────

    /**
     * Multi-select poll with answer[0] selected.
     * A checkbox drawable (amity_v4_poll_multiple_select) should appear — NOT a RadioButton.
     * This verifies the multi-select path is unaffected by the PDT-841 fix.
     */
    @Test
    fun image_poll_multi_select_voted() {
        val answers = fakeAnswers(votedIndex = 0)

        composeTestRule.setContent {
            AmityPostImagePollElement(
                answers = answers,
                selectedIndex = listOf(0),
                isSingleSelected = false,
                viewResultMode = false,
                canVote = true,
                totalVoteCount = 11,
            )
        }
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_multi_select_voted.png", roborazziOptions = screenshotOptions)
    }

    // ── 4. Result mode ────────────────────────────────────────────────────────

    /**
     * Result mode (viewResultMode = true): percentage overlay is shown on each card,
     * image is blurred, and NO radio button or checkbox appears.
     * answer[0] gets the primary overlay because it has the highest voteCount (8 vs 3) —
     * in result mode, isSelected is driven by isTopAnswer (max voteCount), NOT by
     * selectedIndex or isVotedByUser. selectedIndex is irrelevant here.
     */
    @Test
    fun image_poll_result_mode() {
        val answers = fakeAnswers(votedIndex = 0)

        composeTestRule.setContent {
            AmityPostImagePollElement(
                answers = answers,
                selectedIndex = emptyList(), // ignored in result mode; isTopAnswer drives overlay
                isSingleSelected = true,
                viewResultMode = true,
                canVote = false,
                totalVoteCount = 11,
            )
        }
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/image_poll_result_mode.png", roborazziOptions = screenshotOptions)
    }
}
