package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.linkpreview.AmityPreviewUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataCacheItem
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewNoUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewUrlCacheItem
import com.amity.socialcloud.uikit.common.token.AmityUserEngine
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
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Improved screenshot tests for PDT-2228 (link preview).
 * Improvements over AmityPreviewLinkScreenshotTest:
 *
 * 1. Extends BaseScreenshotTest → consistent xxhdpi config.
 * 2. URL-aware mock: getPostPreviewUrl answers{} distinguishes url=null (rejected) from
 *    url!=null (accepted). Old tests used a blanket `returns` mock that always returned a valid
 *    cache item — TC5–TC7 were silently broken (card hidden only because Flowable.never() kept
 *    state at "initial", not because the regex rejected the URL).
 * 3. New TC5: "placeholder" shimmer state. imageUrl="placeholder" PASSES the guard and shows the
 *    card with shimmer. Original TC4 used Flowable.never() → "initial" state (card hidden) —
 *    a completely different outcome.
 * 4. 0.001 threshold on every captureRoboImage call prevents CI font-rendering flakiness.
 */
class AmityPreviewLinkViewImprovedScreenshotTest : BaseScreenshotTest() {

    private val testUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

    private val screenshotOptions = RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.001f)
    )

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
        every { AmityUserEngine.hasNickname() } returns true

        // URL-aware mock: url=null → extractUrls() returned empty (URL rejected by regex) →
        // AmityPreviewNoUrl → AmityPostPreviewLinkView returns early → card not shown.
        // url!=null → URL was extracted → AmityPreviewUrlCacheItem → card shown (if metadata ok).
        // Old tests used `returns AmityPreviewUrlCacheItem(...)` unconditionally, which bypassed
        // URL rejection entirely — TC5–TC7 were testing the wrong thing.
        every { AmityPreviewUrl.getPostPreviewUrl(any(), any(), any()) } answers {
            val url = secondArg<String?>()
            if (url == null) AmityPreviewNoUrl() else AmityPreviewUrlCacheItem(url, null)
        }
    }

    @After
    fun tearDown() {
        unmockkObject(AmityCoreClient)
        unmockkObject(AmityPreviewUrl)
        unmockkObject(AmityUserEngine)
    }

    private fun fakeOwner(post: com.amity.socialcloud.sdk.model.social.post.AmityPost) =
        FakeViewModelStoreOwner(
            mapOf(
                AmityPostDetailPageViewModel::class.java to FakeAmityPostDetailPageViewModel(
                    fakePost = post,
                    fakeInternetState = NetworkConnectionEvent.Connected
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
        timestamp = DateTime.now()
    )

    private fun renderPage(owner: FakeViewModelStoreOwner, postId: String) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
                AmityPostDetailPage(
                    id = postId,
                    style = AmityPostContentComponentStyle.DETAIL,
                    category = AmityPostCategory.GENERAL,
                    hideTarget = true,
                    showLivestreamPostExceeded = false
                )
            }
        }
    }

    @Test
    fun link_preview_shown_with_og_image_v2() {
        val postId = "post-tc1-v2"
        val post = FakePostFactory.textPostWithUrl(postId, testUrl)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
                Flowable.just(metadata("https://img.youtube.com/vi/dQw4w9WgXcQ/maxresdefault.jpg"))
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/link_preview_shown_with_og_image_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    @Test
    fun link_preview_shown_no_og_image_v2() {
        val postId = "post-tc2-v2"
        val post = FakePostFactory.textPostWithUrl(postId, testUrl)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
                Flowable.just(metadata(""))
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/link_preview_shown_no_og_image_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    @Test
    fun link_preview_shown_on_error_v2() {
        val postId = "post-tc3-v2"
        val post = FakePostFactory.textPostWithUrl(postId, testUrl)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
                Flowable.just(metadata("error"))
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/link_preview_shown_on_error_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    /**
     * Tests "initial" state (imageUrl="initial" → guard triggers → card hidden).
     * Correctly named: the old TC4 was called "loading" but it tested "initial" — a different
     * state from "placeholder" (shimmer shown). See link_preview_shimmer_placeholder_shown_v2.
     */
    @Test
    fun link_preview_initial_state_hidden_v2() {
        val postId = "post-tc4-v2"
        val post = FakePostFactory.textPostWithUrl(postId, testUrl)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/link_preview_initial_state_hidden_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    /**
     * NEW — missing from the original test suite.
     * imageUrl="placeholder" PASSES the guard (not "initial", "error", or empty) → card IS shown
     * with shimmer skeleton boxes + placeholder icon. The original TC4 kept imageUrl at "initial"
     * via Flowable.never(), which HIDES the card — a completely different rendering path.
     */
    @Test
    fun link_preview_shimmer_placeholder_shown_v2() {
        val postId = "post-tc5-v2"
        val post = FakePostFactory.textPostWithUrl(postId, testUrl)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
                Flowable.just(metadata("placeholder"))
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/link_preview_shimmer_placeholder_shown_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    // TC6–TC9: URL extraction tests. The setUp mock is url-aware, so when extractUrls()
    // rejects the URL (returns empty list), url=null flows into getPostPreviewUrl → AmityPreviewNoUrl
    // → AmityPostPreviewLinkView returns early → card not shown. This correctly proves URL
    // rejection. The original TC5–TC7 bypassed this path entirely via the blanket mock.
    // fetchMetadataFlow is mocked defensively below; it is never invoked for rejection cases
    // because AmityPostPreviewLinkView returns before calling AmityPreviewLinkView.

    @Test
    fun url_pattern_invalid_protocol_no_preview_v2() {
        val postId = "post-tc6-v2"
        val post = FakePostFactory.textPostWithUrl(postId, "://example.com")
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/url_pattern_invalid_protocol_no_preview_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    @Test
    fun url_pattern_mailto_no_preview_v2() {
        val postId = "post-tc7-v2"
        val post = FakePostFactory.textPostWithUrl(postId, "mailto:t.com")
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/url_pattern_mailto_no_preview_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    @Test
    fun url_pattern_numeric_domain_no_preview_v2() {
        val postId = "post-tc8-v2"
        val post = FakePostFactory.textPostWithUrl(postId, "1.bar")
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/url_pattern_numeric_domain_no_preview_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    @Test
    fun url_pattern_partial_match_shows_preview_v2() {
        val postId = "post-tc9-v2"
        val post = FakePostFactory.textPostWithUrl(postId, "www.google:-t")
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns
                Flowable.just(metadata("https://www.google.com/images/branding/googleg/1x/googleg_standard_color_128dp.png"))
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/url_pattern_partial_match_shows_preview_v2.png",
            roborazziOptions = screenshotOptions
        )
    }

    /**
     * TC10: Real-world multi-URL post — all URLs should be rejected, no preview card shown.
     *
     * Post text mirrors the QA test post "Link Android" which lists every rejection case:
     *   - ://example.com            → invalid protocol
     *   - ///example.com            → too many slashes
     *   - my_http://test            → preceded by underscore
     *   - test://example            → unregistered schema
     *   - http://example.com/path(1 → unbalanced parens  ← THE KEY CASE
     *   - mailto:t.com              → invalid mailto tail
     *   - 1.bar                     → numeric-start domain
     *   - www.google:-t             → partial match, only www.google extracted (no TLD → rejected)
     *
     * Expected: extractUrls() returns empty → getPostPreviewUrl receives url=null →
     * AmityPreviewNoUrl → NO card shown.
     */
    @Test
    fun url_pattern_multi_url_post_all_rejected_no_preview_v2() {
        val postId = "post-tc10-v2"
        val postText = """Link Android
❌ ://example.com → Not matched (invalid protocol)
❌ ///example.com → Not matched (too many slashes)
❌ my_http://test → Not matched (preceded by underscore)
❌ test://example → Not matched (if test: schema not registered)
❌ http://example.com/path(1 → Not matched (not balanced parens)
❌ mailto:t.com → Not matched, invalid mailto: tail
❌ 1.bar → Not matched, we don't have any schmea to support this
www.google:-t → Matched only on www.google"""
        val post = FakePostFactory.textPost(postId = postId, text = postText)
        every { AmityPreviewUrl.fetchMetadataFlow(any(), any()) } returns Flowable.never()
        val owner = fakeOwner(post)
        renderPage(owner, postId)
        composeTestRule.onRoot().captureRoboImage(
            "src/test/golden/url_pattern_multi_url_post_all_rejected_no_preview_v2.png",
            roborazziOptions = screenshotOptions
        )
    }
}
