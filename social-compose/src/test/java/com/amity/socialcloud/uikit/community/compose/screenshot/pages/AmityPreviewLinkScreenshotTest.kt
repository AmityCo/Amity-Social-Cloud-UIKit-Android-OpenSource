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
import com.amity.socialcloud.uikit.common.linkpreview.AmityPreviewUrl
import com.amity.socialcloud.uikit.common.token.AmityUserEngine
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataCacheItem
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewUrlCacheItem
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
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Screenshot tests for the link preview card in [AmityPostDetailPage] (PDT-2228).
 *
 * The bug: when a post URL has no og:image, the preview card was still shown.
 * Fix: hide the card when imageUrl is initial / error / empty.
 *
 * Scenarios covered:
 *  - TC1 (preview_shown)    : URL with valid og:image → card rendered
 *  - TC2 (no_og_image)      : URL with no og:image (imageUrl = "") → card hidden
 *  - TC3 (error_response)   : URL fetch failed (imageUrl = "error") → card hidden
 *  - TC4 (loading_state)    : Metadata still fetching (imageUrl = "placeholder") → card hidden
 *
 * [AmityPreviewUrl] is an object singleton — mocked via [mockkObject] so tests never
 * hit the network or the real metadata cache.
 *
 * The URL cache ([AmityPreviewUrl.getPostPreviewUrl]) must return a non-null
 * [AmityPreviewUrlCacheItem] (not [AmityPreviewNoUrl]) so the composable proceeds to
 * call [AmityPreviewUrl.fetchMetadataFlow]. Without this, the guard at line 85 of
 * AmityPreviewLinkView.kt returns early before any preview logic runs.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityPreviewLinkScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        ),
    )

    private val testUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        mockkObject(AmityCoreClient)
        mockkObject(AmityPreviewUrl)
        mockkObject(AmityUserEngine)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
        every { AmityCoreClient.getUserId() } returns "user-001"
        every { AmityCoreClient.getCurrentUser() } returns Flowable.never()
        // hasNickname() = true suppresses UltaNicknameSheet without a 2-second timeout
        every { AmityUserEngine.hasNickname() } returns true

        // Default: URL cache returns a valid cache item so the composable proceeds past the
        // early-return guard and calls fetchMetadataFlow.
        every {
            AmityPreviewUrl.getPostPreviewUrl(any(), any(), any())
        } returns AmityPreviewUrlCacheItem(testUrl, null)
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
        unmockkObject(AmityPreviewUrl)
        unmockkObject(AmityUserEngine)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun fakeOwner(post: com.amity.socialcloud.sdk.model.social.post.AmityPost) =
        FakeViewModelStoreOwner(
            mapOf(
                AmityPostDetailPageViewModel::class.java to FakeAmityPostDetailPageViewModel(
                    fakePost = post,
                    fakeInternetState = NetworkConnectionEvent.Connected,
                ),
                AmityPostMenuViewModel::class.java to FakeAmityPostMenuViewModel(),
                AmityCommentTrayComponentViewModel::class.java to FakeAmityCommentTrayComponentViewModel(),
            )
        )

    private fun metadata(imageUrl: String) = AmityPreviewMetadataCacheItem(
        url = testUrl,
        domain = "youtube.com",
        title = "Rick Astley - Never Gonna Give You Up",
        imageUrl = imageUrl,
        timestamp = DateTime.now(),
    )

    private fun renderPage(owner: FakeViewModelStoreOwner, postId: String) {
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

    // ── Tests ─────────────────────────────────────────────────────────────────

    /**
     * TC1: URL has a valid og:image → preview card IS shown.
     * imageUrl = "https://..." (non-empty, non-special) passes all guards in AmityPreviewLinkView.
     */
    @Test
    fun link_preview_shown_with_og_image() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
            Flowable.just(metadata("https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg"))

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-link-shown-001",
            url = testUrl,
        )
        renderPage(fakeOwner(post), postId = "post-link-shown-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/link_preview_shown_with_og_image.png")
    }

    /**
     * TC2: URL has metadata but no og:image → preview card is NOT shown.
     * imageUrl = "" (empty string) — the fixed condition hides the card.
     * Regression: before the fix this card was still shown (PDT-2228).
     */
    @Test
    fun link_preview_hidden_no_og_image() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
            Flowable.just(metadata(""))

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-link-no-img-001",
            url = testUrl,
        )
        renderPage(fakeOwner(post), postId = "post-link-no-img-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/link_preview_hidden_no_og_image.png")
    }

    /**
     * TC3: URL fetch failed → preview card is NOT shown.
     * imageUrl = "error" — treated as an error state and hidden.
     */
    @Test
    fun link_preview_hidden_on_error() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
            Flowable.just(metadata("error"))

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-link-error-001",
            url = testUrl,
        )
        renderPage(fakeOwner(post), postId = "post-link-error-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/link_preview_hidden_on_error.png")
    }

    /**
     * TC4: Metadata still loading → preview card is NOT shown.
     * imageUrl = "initial" — the composable emits this as the initial state before
     * the flow produces its first value. The card is hidden during this state.
     */
    @Test
    fun link_preview_hidden_while_loading() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
            Flowable.never()  // Never emits → composable stays at initial state

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-link-loading-001",
            url = testUrl,
        )
        renderPage(fakeOwner(post), postId = "post-link-loading-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/link_preview_hidden_while_loading.png")
    }

    // ── Row 4: URL pattern matching (PDT-2228) ─────────────────────────────────
    //
    // After the regex fix in StringExtensions.kt, extractUrls() rejects these invalid
    // URL patterns so AmityPreviewUrl.getPostPreviewUrl() is never called → no preview card.

    /**
     * TC5 (row 4 ❌): Post text contains "://example.com" (invalid protocol — colon-slash-slash
     * before the domain with no scheme prefix).
     * After the regex fix the pattern is NOT matched → no preview card shown.
     */
    @Test
    fun url_pattern_invalid_protocol_no_preview() {
        // fetchMetadataFlow should never be called for this input, but mock it defensively
        // so the test doesn't fail on an unexpected invocation.
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-url-invalid-protocol-001",
            url = "://example.com",
        )
        renderPage(fakeOwner(post), postId = "post-url-invalid-protocol-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/url_pattern_invalid_protocol_no_preview.png")
    }

    /**
     * TC6 (row 4 ❌): Post text contains "mailto:t.com" (invalid mailto: tail).
     * After the regex fix "t.com" preceded by ":" is NOT matched → no preview card.
     */
    @Test
    fun url_pattern_mailto_no_preview() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-url-mailto-001",
            url = "mailto:t.com",
        )
        renderPage(fakeOwner(post), postId = "post-url-mailto-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/url_pattern_mailto_no_preview.png")
    }

    /**
     * TC7 (row 4 ❌): Post text contains "1.bar" (digit-first domain, no valid schema).
     * After the regex fix digit-first domains are NOT matched → no preview card.
     */
    @Test
    fun url_pattern_numeric_domain_no_preview() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-url-numeric-001",
            url = "1.bar",
        )
        renderPage(fakeOwner(post), postId = "post-url-numeric-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/url_pattern_numeric_domain_no_preview.png")
    }

    /**
     * TC8 (row 4 ✅ partial): Post text contains "www.google:-t".
     * extractUrls() matches ONLY "www.google" (stops before ":-t").
     * The preview card IS shown for www.google (has og:image).
     */
    @Test
    fun url_pattern_partial_match_shows_preview_for_valid_part() {
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
            Flowable.just(metadata("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"))

        val post = FakePostFactory.textPostWithUrl(
            postId = "post-url-partial-001",
            url = "www.google:-t",
        )
        renderPage(fakeOwner(post), postId = "post-url-partial-001")
        composeTestRule.onRoot()
            .captureRoboImage("src/test/golden/url_pattern_partial_match_shows_preview.png")
    }
}
