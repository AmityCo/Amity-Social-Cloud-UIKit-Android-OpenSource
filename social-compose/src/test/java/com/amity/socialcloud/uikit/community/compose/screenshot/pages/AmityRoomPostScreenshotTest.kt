package com.amity.socialcloud.uikit.community.compose.screenshot.pages

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPage
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.base.FakeViewModelStoreOwner
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fakes.FakeAmityPostMenuViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakePostFactory
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
 * Screenshot tests for all [AmityChildRoomPostElement] render states.
 *
 * Coverage:
 *   Terminal / error states (fixed-height 203dp containers):
 *     - room == null          → element renders nothing (empty)
 *     - room.isDeleted()      → AmityLivestreamUnavailableView
 *     - terminateLabels set   → AmityLivestreamTerminatedView
 *     - status ENDED          → AmityLivestreamEndedView
 *     - status ERROR          → AmityLivestreamReplayUnavailableView
 *
 *   Active states (thumbnail + badge overlay):
 *     - status IDLE           → "UPCOMING LIVE" badge, no play button, placeholder thumbnail
 *     - status LIVE           → "LIVE" badge, play button, placeholder thumbnail
 *     - status LIVE (thumb)   → "LIVE" badge, Mux live thumbnail
 *     - status WAITING_RECONNECT → "LIVE" badge, play button, placeholder thumbnail
 *     - status RECORDED       → "RECORDED" badge, play button, placeholder thumbnail
 *     - creator thumbnail     → creator-uploaded thumbnail takes priority over status thumbnails
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityRoomPostScreenshotTest {

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
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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
        postId: String,
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

    // ── Terminal / error states ───────────────────────────────────────────────

    /**
     * room == null → AmityChildRoomPostElement returns early without rendering the status box.
     * The page still renders (header, reactions, etc.) but the media area is empty.
     */
    @Test
    fun room_post_null_room() {
        val post = FakePostFactory.roomPost_nullRoom()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_null_room.png")
    }

    /**
     * room.isDeleted() = true → renders AmityLivestreamUnavailableView
     * (warning icon + "unavailable" text on black 203dp box).
     */
    @Test
    fun room_post_deleted_room() {
        val post = FakePostFactory.roomPost_deleted()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_deleted_room.png")
    }

    /**
     * terminateLabels non-empty → renders AmityLivestreamTerminatedView
     * (terminated message on black 203dp box).
     */
    @Test
    fun room_post_terminated() {
        val post = FakePostFactory.roomPost_terminated()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_terminated.png")
    }

    /**
     * status ENDED → renders AmityLivestreamEndedView
     * ("processing" message on black 203dp box).
     */
    @Test
    fun room_post_ended() {
        val post = FakePostFactory.roomPost_ended()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_ended.png")
    }

    /**
     * status ERROR → renders AmityLivestreamReplayUnavailableView
     * ("replay unavailable" message on black 203dp box).
     */
    @Test
    fun room_post_error() {
        val post = FakePostFactory.roomPost_error()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_error.png")
    }

    // ── Active states ─────────────────────────────────────────────────────────

    /**
     * status IDLE → "UPCOMING LIVE" badge (top-end), no play button,
     * default placeholder thumbnail.
     */
    @Test
    fun room_post_idle() {
        val post = FakePostFactory.roomPost_idle()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_idle.png")
    }

    /**
     * status LIVE, no thumbnail URL → "LIVE" badge (top-end), play button overlay,
     * default placeholder thumbnail (fallback 4).
     */
    @Test
    fun room_post_live_no_thumbnail() {
        val post = FakePostFactory.roomPost_live()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_live_no_thumbnail.png")
    }

    /**
     * status LIVE with liveThumbnailUrl set → "LIVE" badge, play button,
     * AsyncImage loading from Mux live thumbnail URL (fallback 2).
     * Coil will fail to load the fake URL and fall back to the placeholder drawable.
     */
    @Test
    fun room_post_live_with_thumbnail() {
        val post = FakePostFactory.roomPost_liveWithThumbnail()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_live_with_thumbnail.png")
    }

    /**
     * status WAITING_RECONNECT → same "LIVE" badge as LIVE status,
     * play button overlay, default placeholder thumbnail.
     */
    @Test
    fun room_post_waiting_reconnect() {
        val post = FakePostFactory.roomPost_waitingReconnect()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_waiting_reconnect.png")
    }

    /**
     * status RECORDED → "RECORDED" badge (top-end), play button overlay,
     * default placeholder thumbnail (no recordedThumbnailUrl set).
     */
    @Test
    fun room_post_recorded_no_thumbnail() {
        val post = FakePostFactory.roomPost_recorded()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_recorded_no_thumbnail.png")
    }

    /**
     * Creator-uploaded thumbnail set → thumbnail takes priority (fallback 1),
     * rendered via AsyncImage with platform-default aspect ratio.
     * "UPCOMING LIVE" badge since status is IDLE.
     */
    @Test
    fun room_post_with_creator_thumbnail() {
        val post = FakePostFactory.roomPost_withCreatorThumbnail()
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = post)
        )
        renderPage(owner, postId = post.getPostId())
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/room_post_with_creator_thumbnail.png")
    }
}
