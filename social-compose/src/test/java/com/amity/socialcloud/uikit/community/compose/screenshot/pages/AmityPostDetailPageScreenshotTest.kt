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
 * Layer 2 full-page screenshot tests for [AmityPostDetailPage].
 *
 * Each test renders the entire page with a single scenario:
 *   - Post type variants: text, image, video, poll, livestream, clip, room
 *   - Page-level states: loading (shimmer), post error, deleted post, unsupported type
 *   - UX variants: offline banner, target label visible, post categories
 *   - Content variants: long text, edited post, mention, hashtag
 *
 * Three ViewModels are injected via [FakeViewModelStoreOwner] so no SDK
 * singletons are ever called during the test.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityPostDetailPageScreenshotTest {

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
        postId: String = "post-001",
        hideTarget: Boolean = true,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
        showLivestreamPostExceeded: Boolean = false,
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                AmityPostDetailPage(
                    id = postId,
                    style = AmityPostContentComponentStyle.DETAIL,
                    category = category,
                    hideTarget = hideTarget,
                    showLivestreamPostExceeded = showLivestreamPostExceeded,
                )
            }
        }
    }

    // ── 1. Page states ────────────────────────────────────────────────────────

    /** Post not yet loaded (null) — shimmer should be visible. */
    @Test
    fun page_loading_state() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(fakePost = null)
        )
        renderPage(owner, postId = "post-loading")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/page_loading_state.png")
    }

    /** postErrorState = true → AmityPostErrorPage rendered. */
    @Test
    fun page_post_error() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
                fakePostError = true,
            )
        )
        renderPage(owner)
        composeTestRule.onRoot().captureRoboImage("src/test/golden/page_post_error.png")
    }

    /** post.isDeleted() = true → AmityPostErrorPage rendered. */
    @Test
    fun page_deleted_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.deletedPost(),
            )
        )
        renderPage(owner, postId = "post-deleted-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/page_deleted_post.png")
    }

    /** structureType not in supportedStructureTypes → AmityPostErrorPage rendered. */
    @Test
    fun page_unsupported_type() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.unsupportedTypePost(),
            )
        )
        renderPage(owner, postId = "post-unsupported-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/page_unsupported_type.png")
    }

    /** Offline — NetworkConnectionEvent.Disconnected triggers snackbar. */
    @Test
    fun page_offline_banner() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
                fakeInternetState = NetworkConnectionEvent.Disconnected,
            )
        )
        renderPage(owner)
        composeTestRule.onRoot().captureRoboImage("src/test/golden/page_offline_banner.png")
    }

    // ── 2. Text post variants ─────────────────────────────────────────────────

    /** Happy path: plain short text post, target hidden. */
    @Test
    fun text_post_happy_path() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
            )
        )
        renderPage(owner, postId = "post-text-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_happy_path.png")
    }

    /** Community target label visible (hideTarget = false). */
    @Test
    fun text_post_show_target() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
            )
        )
        renderPage(owner, postId = "post-text-001", hideTarget = false)
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_show_target.png")
    }

    /** Long text — "See more" truncation should appear. */
    @Test
    fun text_post_long_text() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.longTextPost(),
            )
        )
        renderPage(owner, postId = "post-long-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_long_text.png")
    }

    /** Edited post — "(Edited)" badge should appear. */
    @Test
    fun text_post_edited() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.editedPost(),
            )
        )
        renderPage(owner, postId = "post-edited-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_edited.png")
    }

    /** Post with @mention. */
    @Test
    fun text_post_with_mention() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.mentionPost(),
            )
        )
        renderPage(owner, postId = "post-mention-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_with_mention.png")
    }

    /** Post with #hashtags. */
    @Test
    fun text_post_with_hashtag() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.hashtagPost(),
            )
        )
        renderPage(owner, postId = "post-hashtag-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_with_hashtag.png")
    }

    // ── 3. Post category variants ─────────────────────────────────────────────

    @Test
    fun text_post_category_pin() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
            )
        )
        renderPage(owner, category = AmityPostCategory.PIN)
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post_category_pin.png")
    }

    @Test
    fun text_post_category_announcement() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.textPost(),
            )
        )
        renderPage(owner, category = AmityPostCategory.ANNOUNCEMENT)
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/text_post_category_announcement.png")
    }

    // ── 4. Media post types ───────────────────────────────────────────────────

    /** Image post — media element rendered via AmityPostMediaElement. */
    @Test
    fun image_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.imagePost(),
            )
        )
        renderPage(owner, postId = "post-image-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/image_post.png")
    }

    /** Video post — thumbnail placeholder rendered via AmityPostMediaElement. */
    @Test
    fun video_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.videoPost(),
            )
        )
        renderPage(owner, postId = "post-video-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/video_post.png")
    }

    /** Poll post — poll UI rendered via AmityPostPollElement. */
    @Test
    fun poll_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.pollPost(),
            )
        )
        renderPage(owner, postId = "post-poll-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/poll_post.png")
    }

    /** Livestream post — stream UI rendered via AmityPostLivestreamElement. */
    @Test
    fun livestream_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.livestreamPost(),
            )
        )
        renderPage(owner, postId = "post-livestream-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/livestream_post.png")
    }

    /** Clip post — clip media rendered via AmityPostMediaElement (clip branch). */
    @Test
    fun clip_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.clipPost(),
            )
        )
        renderPage(owner, postId = "post-clip-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/clip_post.png")
    }

    /** Room post — room UI rendered via AmityPostLivestreamElement (same branch as livestream). */
    @Test
    fun room_post() {
        val owner = fakeOwner(
            pageVM = FakeAmityPostDetailPageViewModel(
                fakePost = FakePostFactory.roomPost(),
            )
        )
        renderPage(owner, postId = "post-room-001")
        composeTestRule.onRoot().captureRoboImage("src/test/golden/room_post.png")
    }
}
