package com.amity.socialcloud.uikit.community.compose.screenshot.elements

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostContentElement
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakePostFactory
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Layer 1 screenshot tests for [AmityPostContentElement].
 *
 * Pure-parameter Composable — no ViewModel, no SDK singletons.
 * Scenarios:
 *   1. text_post            — short plain text
 *   2. long_text_post       — multi-line text that can be expanded
 *   3. mention_post         — text with @mention highlight
 *   4. hashtag_post         — text with #hashtag highlight
 *   5. edited_post          — post marked as edited
 *   6. detail_style         — same post rendered in DETAIL style (wider font)
 *   7. product_tag_post     — text with product tag highlights
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityPostContentElementScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/golden",
            captureType = RoborazziRule.CaptureType.None,
        )
    )

    // ── 1. Short text post, FEED style ────────────────────────────────────────

    @Test
    fun text_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.textPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/text_post.png")
    }

    // ── 2. Long text — "See more" button should appear ────────────────────────

    @Test
    fun long_text_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.longTextPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/long_text_post.png")
    }

    // ── 3. Post with @mention ─────────────────────────────────────────────────

    @Test
    fun mention_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.mentionPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/mention_post.png")
    }

    // ── 4. Post with #hashtag ─────────────────────────────────────────────────

    @Test
    fun hashtag_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.hashtagPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/hashtag_post.png")
    }

    // ── 5. Edited post — "(Edited)" badge should be visible ───────────────────

    @Test
    fun edited_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.editedPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/edited_post.png")
    }

    // ── 6. DETAIL style — larger font, no tap-to-expand ───────────────────────

    @Test
    fun detail_style() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.textPost(),
                style = AmityPostContentComponentStyle.DETAIL,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/detail_style.png")
    }

    // ── 7. Product tag post — "Nike Sneakers" and "Adidas Hoodie" highlighted ─

    @Test
    fun product_tag_post() {
        composeTestRule.setContent {
            AmityPostContentElement(
                post = FakePostFactory.productTagPost(),
                style = AmityPostContentComponentStyle.FEED,
                onClick = {},
            )
        }
        composeTestRule.onRoot().captureRoboImage("src/test/golden/product_tag_post.png")
    }
}
