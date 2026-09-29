@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.community.profile

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityInfoView
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import org.junit.Assert.assertEquals
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * PDT-5571. The community profile under the module gate.
 *
 * Feed is the global feed and For You only (module-availability-spec, #310):
 * withholding it leaves the community page as it is while Post is on. Post owns
 * the page's post surfaces — the tabs and the post count —
 * so withholding Post hides them, member count and all the Community-owned rest
 * staying.
 *
 * The post count used to render inside `community_info`, which Community owns,
 * so it survived Post being switched off.
 *
 * The floating "+" was Post's too, until PDT-5617: it opens story and event as
 * well, so it now belongs to no module and is drawn while its sheet has a row.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AmityCommunityProfileModuleGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val page = "community_profile_page"
    private val header = "community_header"

    private val community: AmityCommunity = mockk(relaxed = true) {
        every { getPostCount() } returns 42
        every { getMemberCount() } returns 7
        every { isJoined() } returns true
    }

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        // AmityBaseComponent reads the session; there is none on the JVM.
        mockkObject(AmityCoreClient)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
    }

    @After
    fun tearDown() {
        ModuleEntitlementForTests.clear()
        unmockkObject(AmityCoreClient)
    }

    // Whatever the bundle resolves to on the JVM, read during composition.
    private lateinit var postsLabel: String
    private lateinit var membersLabel: String

    private fun withhold(vararg keys: String) =
        ModuleEntitlementForTests.withhold(*keys)

    private fun excluded(component: String, element: String) =
        AmityUIKitConfigController.isExcluded("$page/$component/$element")

    private fun renderInfo() {
        composeTestRule.setContent {
            postsLabel = amitySocialString("amity_social_label_community_posts_label")
            membersLabel = amitySocialString("amity_social_label_community_members_label")
            AmityBasePage(pageId = page) {
                AmityBaseComponent(pageScope = getPageScope(), componentId = header) {
                    AmityCommunityInfoView(
                        pageScope = getPageScope(),
                        componentScope = getComponentScope(),
                        community = community,
                    )
                }
            }
        }
    }

    @Test
    fun `with Feed off and Post on the community page keeps its post surfaces`() {
        withhold("feed")

        assertFalse(excluded("*", "community_feed_tab_button"))
        assertFalse(excluded("*", "community_pin_tab_button"))
        assertFalse(excluded("*", "community_media_tab_button"))
        assertFalse(excluded("*", "community_create_post_button"))
        assertFalse(excluded(header, "community_info_posts"))
        assertFalse(excluded("community_feed", "*"))
        assertFalse(excluded("community_image_feed", "*"))
        // What Feed does own: the global feed and For You.
        assertTrue(AmityUIKitConfigController.isExcluded("*/global_feed_component/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("*/amity_for_you_feed_component/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("*/*/for_you_button"))
    }

    @Test
    fun `with Feed off the post count still shows`() {
        withhold("feed")
        renderInfo()

        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText(postsLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText(membersLabel).assertIsDisplayed()
    }

    @Test
    fun `with Post off the post count is hidden and the member count stays`() {
        withhold("post")
        renderInfo()

        composeTestRule.onNodeWithText("42").assertDoesNotExist()
        composeTestRule.onNodeWithText(postsLabel).assertDoesNotExist()
        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText(membersLabel).assertIsDisplayed()
        // The divider went with the count: members start the row, at its 16dp
        // padding, rather than after an orphaned rule and its 32dp of margin.
        assertEquals(
            16.dp,
            composeTestRule.onNodeWithText("7").getUnclippedBoundsInRoot().left,
        )
    }

    @Test
    fun `with Post off the post tabs go and the create button stays for the rest`() {
        withhold("post")

        assertTrue(excluded("*", "community_feed_tab_button"))
        assertTrue(excluded("*", "community_pin_tab_button"))
        assertTrue(excluded("*", "community_media_tab_button"))
        // The floating "+" belongs to no module (PDT-5617): its sheet still offers
        // story and event, so it is drawn while one of them is left.
        assertFalse(excluded("*", "community_create_post_button"))
        // The page itself and the header are Community's.
        assertFalse(excluded(header, "community_info"))
    }
}
