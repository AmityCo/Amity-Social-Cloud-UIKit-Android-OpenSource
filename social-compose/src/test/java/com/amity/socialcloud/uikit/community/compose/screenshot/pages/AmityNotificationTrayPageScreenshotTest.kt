package com.amity.socialcloud.uikit.community.compose.screenshot.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPage
import com.amity.socialcloud.uikit.community.compose.notificationtray.NotificationTrayViewModel
import com.amity.socialcloud.uikit.community.compose.notificationtray.NotificationTrayViewModel.NotificationTrayListItem
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayEmptyState
import com.amity.socialcloud.uikit.community.compose.screenshot.base.BaseScreenshotTest
import com.amity.socialcloud.uikit.community.compose.screenshot.base.FakeViewModelStoreOwner
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Page-level screenshot tests for [AmityNotificationTrayPage].
 *
 * Covers:
 *  1. PDT-1955: user_profile_reset notification row (unseen + seen states)
 *  2. PDT-2244: Notification tray empty state — toolbar (back button + title) + icon + two text lines
 *
 * The toolbar in the empty-state test uses AmityBaseElement(elementId = "back_button") exactly
 * as the real page does, so the back arrow icon is resolved from config and rendered correctly.
 */
class AmityNotificationTrayPageScreenshotTest : BaseScreenshotTest() {

    // 0.1% tolerance — prevents font-rendering flakiness without masking real bugs
    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(
            resultValidator = { result ->
                result.pixelDifferences / result.pixelCount.toFloat() < 0.001f
            }
        )
    )

    // ── Inner fake ViewModel ──────────────────────────────────────────────────

    /**
     * Minimal ViewModel stub that serves a fixed list of [NotificationTrayListItem]
     * without touching any SDK singletons.
     */
    private inner class FakeNotificationTrayViewModel(
        private val items: List<NotificationTrayListItem> = emptyList(),
    ) : NotificationTrayViewModel() {
        override fun getNotificationTrayItem(): Flow<PagingData<NotificationTrayListItem>> =
            flowOf(PagingData.from(items))

        override fun getCommunityInvitations(): Flow<PagingData<AmityInvitation>> =
            flowOf(PagingData.empty())
    }

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

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun fakeUserProfileResetItem(isSeen: Boolean): AmityNotificationTrayItem {
        return mockk<AmityNotificationTrayItem>(relaxed = true) {
            every { getTrayItemCategory() } returns "user_profile_reset"
            every { getActionType() } returns "user_profile_reset"
            every { getText() } returns "Your profile information was reset because it didn't meet our Community Guidelines. Please update your profile."
            every { getTemplatedText() } returns "Your profile information was reset because it didn't meet our Community Guidelines. Please update your profile."
            every { getLastOccurredAt() } returns DateTime(2025, 1, 1, 0, 0, 0)
            every { getUsers() } returns emptyList()
            every { this@mockk.isSeen() } returns isSeen
            every { isRecent() } returns true
            every { uniqueId() } returns "item-profile-reset-page-${if (isSeen) "seen" else "unseen"}"
        }
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    /**
     * Full page with a single unseen user_profile_reset notification.
     * Expects blue-tint row, generic icon, bold phrase, no truncation.
     */
    @Test
    fun notification_tray_page_user_profile_reset_unseen() {
        val item = fakeUserProfileResetItem(isSeen = false)
        val vm = FakeNotificationTrayViewModel(
            listOf(NotificationTrayListItem.NotificationItem(item))
        )
        val fakeOwner = FakeViewModelStoreOwner(
            mapOf(NotificationTrayViewModel::class.java to vm)
        )

        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {
                AmityNotificationTrayPage()
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_page_user_profile_reset_unseen.png",
                roborazziOptions = screenshotOptions,
            )
    }

    /**
     * Full page with a single seen user_profile_reset notification.
     * Expects white row, generic icon, bold phrase, no truncation.
     */
    @Test
    fun notification_tray_page_user_profile_reset_seen() {
        val item = fakeUserProfileResetItem(isSeen = true)
        val vm = FakeNotificationTrayViewModel(
            listOf(NotificationTrayListItem.NotificationItem(item))
        )
        val fakeOwner = FakeViewModelStoreOwner(
            mapOf(NotificationTrayViewModel::class.java to vm)
        )

        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {
                AmityNotificationTrayPage()
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_page_user_profile_reset_seen.png",
                roborazziOptions = screenshotOptions,
            )
    }

    /**
     * Full page view: toolbar (back button + title) + empty state (icon + two text lines).
     *
     * PDT-2244: Renders a full-page view with toolbar + empty state so that the
     * golden can be compared directly against the QA expected screenshot.
     *
     * Instead of driving the full [AmityNotificationTrayPage] with live Paging,
     * we compose a page-shell that mirrors the exact layout of the real page:
     *   - Sticky toolbar (height 58dp) with centered "Notifications" title
     *   - [AmityNotificationTrayEmptyState] filling the remaining space
     *
     * TDD mode: Record-then-fix.
     */
    @Test
    fun notification_tray_page_empty_state() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "notification_tray_page") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AmityTheme.colors.background)
                ) {
                    // Toolbar — mirrors the sticky header in the real AmityNotificationTrayPage
                    Box(
                        modifier = Modifier
                            .height(58.dp)
                            .fillMaxWidth()
                            .background(AmityTheme.colors.background)
                            .padding(horizontal = 16.dp)
                    ) {
                        // Back button — resolved from config key
                        // "notification_tray_page/*/back_button" → backIcon → amity_ic_arrow_back
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "back_button"
                        ) {
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(getConfig().getIcon()),
                                contentDescription = "Back",
                                tint = AmityTheme.colors.base,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterStart)
                            )
                        }

                        Text(
                            text = "Notifications",
                            style = AmityTheme.typography.titleBold,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 36.dp)
                        )
                    }

                    // Empty state fills remaining space
                    AmityNotificationTrayEmptyState(
                        modifier = Modifier.fillMaxSize(),
                        pageScope = getPageScope(),
                    )
                }
            }
        }

        composeTestRule.waitForIdle()

        // Assert all three key UI elements are visible
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("No notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("You're up to date!").assertIsDisplayed()

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/golden/notification_tray_page_empty_state.png",
                roborazziOptions = screenshotOptions,
            )
    }
}
