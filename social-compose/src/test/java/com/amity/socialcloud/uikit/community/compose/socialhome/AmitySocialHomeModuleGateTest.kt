@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.socialhome

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PDT-5571. The Social Home tab row under the module gate.
 *
 * The Following tab is the global feed (`AmityNewsFeedComponent`, component
 * `newsfeed`), which Feed owns. Its chip was gated only on `following_button`,
 * which the module table gives to Post, so with Feed off and Post on the tab
 * survived, was the landing tab once For You went, and drew nothing.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AmitySocialHomeModuleGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
        ModuleEntitlementForTests.clear()
        unmockkObject(AmityCoreClient)
    }

    private fun withhold(vararg keys: String) =
        ModuleEntitlementForTests.withhold(*keys)

    private fun tabs(): List<AmitySocialHomePageTab> {
        var tabs: List<AmitySocialHomePageTab> = emptyList()
        composeTestRule.setContent {
            AmityBasePage(pageId = "social_home_page") {
                tabs = visibleSocialHomeTabs(pageScope = getPageScope(), isSignedInUser = true)
            }
        }
        composeTestRule.waitForIdle()
        return tabs
    }

    @Test
    fun `with every module on the signed-in row is complete`() {
        assertEquals(
            listOf(
                AmitySocialHomePageTab.FOR_YOU,
                AmitySocialHomePageTab.FOLLOWING,
                AmitySocialHomePageTab.COMMUNITIES,
                AmitySocialHomePageTab.EVENTS,
                AmitySocialHomePageTab.CLIPS,
            ),
            tabs(),
        )
    }

    @Test
    fun `with Feed off For You and Following go and the row lands on Communities`() {
        withhold("feed")

        val tabs = tabs()

        assertEquals(
            listOf(
                AmitySocialHomePageTab.COMMUNITIES,
                AmitySocialHomePageTab.EVENTS,
                AmitySocialHomePageTab.CLIPS,
            ),
            tabs,
        )
        // The page's landing rule: the first tab still in the row.
        assertEquals(AmitySocialHomePageTab.COMMUNITIES, tabs.first())
    }

    @Test
    fun `with Post off Following stays hidden`() {
        withhold("post")

        assertEquals(false, AmitySocialHomePageTab.FOLLOWING in tabs())
    }
}
