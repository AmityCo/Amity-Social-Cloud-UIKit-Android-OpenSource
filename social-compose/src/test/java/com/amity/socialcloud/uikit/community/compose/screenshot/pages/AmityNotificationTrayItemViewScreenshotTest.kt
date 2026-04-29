package com.amity.socialcloud.uikit.community.compose.screenshot.pages

import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayItemView
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Element-level screenshot tests for [AmityNotificationTrayItemView].
 *
 * Bug (PDT-1955): Three display issues for user_profile_reset notifications:
 *   1. "Your profile information was reset" phrase must be bold
 *   2. Avatar must show a generic system icon (not the user's profile photo)
 *   3. Full message must not be truncated (maxLines = 5, not 3)
 *
 * These tests render the full item row (avatar + text + timestamp) so that
 * all three fixes are visible in one golden image per state.
 *
 * States:
 *   1. notification_tray_item_user_profile_reset_unseen
 *      — isSeen=false; expects generic icon, bold phrase, blue-tint background
 *   2. notification_tray_item_user_profile_reset_seen
 *      — isSeen=true; expects generic icon, bold phrase, white background
 *   3. notification_tray_item_regular_reaction
 *      — isSeen=true, category=reaction; expects user avatar, placeholder-bold text
 *      — regression guard: placeholder-based bold must still work
 */
class AmityNotificationTrayItemViewScreenshotTest : BaseScreenshotTest() {

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

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun fakeUserProfileResetItem(): AmityNotificationTrayItem {
        return mockk<AmityNotificationTrayItem>(relaxed = true) {
            every { getTrayItemCategory() } returns "user_profile_reset"
            every { getActionType() } returns "user_profile_reset"
            every { getText() } returns "Your profile information was reset because it didn't meet our Community Guidelines. Please update your profile."
            every { getTemplatedText() } returns "Your profile information was reset because it didn't meet our Community Guidelines. Please update your profile."
            every { getLastOccurredAt() } returns DateTime(2025, 1, 1, 0, 0, 0)
            every { getUsers() } returns emptyList()
            every { isSeen() } returns false
            every { uniqueId() } returns "item-profile-reset-001"
        }
    }

    private fun fakeReactionItem(): AmityNotificationTrayItem {
        val fakeUser = mockk<AmityUser>(relaxed = true) {
            every { getUserId() } returns "alice-001"
            every { getDisplayName() } returns "Alice"
            every { getAvatar() } returns null
        }
        return mockk<AmityNotificationTrayItem>(relaxed = true) {
            every { getTrayItemCategory() } returns "reaction"
            every { getActionType() } returns "reaction"
            every { getText() } returns "Alice and 5 others reacted to your post."
            every { getTemplatedText() } returns "{{userId: alice-001}} and {{5 others}} reacted to your post."
            every { getLastOccurredAt() } returns DateTime(2025, 1, 1, 0, 0, 0)
            every { getUsers() } returns listOf(fakeUser)
            every { isSeen() } returns true
            every { uniqueId() } returns "item-reaction-001"
        }
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    /**
     * user_profile_reset, unseen (blue-tint background):
     *   - Generic system icon (no user avatar)
     *   - "Your profile information was reset" in bold
     *   - Full message shown without truncation
     *   - Blue-tint (primaryShade3 @ 30%) background
     */
    @Test
    fun notification_tray_item_user_profile_reset_unseen() {
        composeTestRule.setContent {
            AmityNotificationTrayItemView(
                isSeen = false,
                data = fakeUserProfileResetItem(),
            )
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_item_user_profile_reset_unseen.png",
                roborazziOptions = screenshotOptions,
            )
    }

    /**
     * user_profile_reset, seen (white background):
     *   - Generic system icon (no user avatar)
     *   - "Your profile information was reset" in bold
     *   - Full message shown without truncation
     *   - White (background) background
     */
    @Test
    fun notification_tray_item_user_profile_reset_seen() {
        val item = fakeUserProfileResetItem().also {
            every { it.isSeen() } returns true
        }
        composeTestRule.setContent {
            AmityNotificationTrayItemView(
                isSeen = true,
                data = item,
            )
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_item_user_profile_reset_seen.png",
                roborazziOptions = screenshotOptions,
            )
    }

    /**
     * Regular reaction notification (regression guard):
     *   - User avatar shown (not generic icon)
     *   - "Alice" and "5 others" bolded via placeholder mechanism
     *   - White background (isSeen = true)
     */
    @Test
    fun notification_tray_item_regular_reaction() {
        composeTestRule.setContent {
            AmityNotificationTrayItemView(
                isSeen = true,
                data = fakeReactionItem(),
            )
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_item_regular_reaction.png",
                roborazziOptions = screenshotOptions,
            )
    }
}
