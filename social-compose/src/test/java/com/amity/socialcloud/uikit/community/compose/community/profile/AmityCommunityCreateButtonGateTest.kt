@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.community.profile

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.CLIP
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.EVENT
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.LIVESTREAM
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.POLL
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.POST
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCreateAction.STORY
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileActionsContainer
import com.amity.socialcloud.uikit.community.compose.community.profile.element.communityCreateActions
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import io.mockk.every
import io.mockk.mockk
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
 * PDT-5617. The community page's floating "+" (`community_create_post_button`)
 * belongs to no module. It is drawn exactly when the sheet it opens has a row —
 * [communityCreateActions] is the one list both read: the module gate on each
 * row AND the page's per-user checks (post permission, story setting or
 * permission, create-event permission).
 *
 * The override layer holds no catalog, so switching Post off here leaves Poll
 * and Live on. [withholdPostAsStagingDoes] names the dependents staging's catalog
 * takes with Post (Poll, Live, Feed).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AmityCommunityCreateButtonGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val page = mockk<AmityComposePageScope> {
        every { getId() } returns "community_profile_page"
    }

    @Before
    fun setUp() {
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        // Before any label is read: LocalAmitySocialStringProvider caches its
        // default on first read, and the fallback echoes keys for every test
        // that shares this Robolectric sandbox afterwards.
        DefaultAmitySocialStringProvider.initialize(ApplicationProvider.getApplicationContext())
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

    private fun withholdPostAsStagingDoes(vararg more: String) =
        withhold("post", "poll", "live", "feed", *more)

    private fun actions(
        post: Boolean = true,
        story: Boolean = true,
        event: Boolean = true,
    ): List<AmityCommunityCreateAction> =
        communityCreateActions(page, null, canCreatePost = post, canCreateStory = story, canCreateEvent = event)

    @Test
    fun `with nothing withheld the plus offers every row`() {
        assertEquals(listOf(POST, POLL, LIVESTREAM, STORY, CLIP, EVENT), actions())
    }

    @Test
    fun `with Post withheld the plus stays for what Post does not own`() {
        withhold("post")
        // Poll and Live are their own modules and the override layer has no
        // catalog to take them with Post; clip rides Post.
        assertEquals(listOf(POLL, LIVESTREAM, STORY, EVENT), actions())
    }

    @Test
    fun `with Post withheld as staging withholds it the plus offers Story and Event`() {
        withholdPostAsStagingDoes()
        assertEquals(listOf(STORY, EVENT), actions())
    }

    @Test
    fun `with Post withheld an Event-only member still gets the plus`() {
        // The case the old post-or-story condition missed: no story permission,
        // Post gone, the create-event permission held.
        withholdPostAsStagingDoes()
        assertEquals(listOf(EVENT), actions(story = false))
    }

    @Test
    fun `with every row's module withheld the plus is not drawn`() {
        withhold("post", "poll", "live", "story", "events")
        assertEquals(emptyList<AmityCommunityCreateAction>(), actions())
    }

    @Test
    fun `with Post and Story withheld and no create-event permission the plus is not drawn`() {
        withholdPostAsStagingDoes("story")
        assertEquals(emptyList<AmityCommunityCreateAction>(), actions(event = false))
    }

    @Test
    fun `with Post and Story withheld and the create-event permission the plus offers Event`() {
        withholdPostAsStagingDoes("story")
        assertEquals(listOf(EVENT), actions(event = true))
    }

    @Test
    fun `with every module on but no permission the plus is not drawn`() {
        // What the page holds while its permission reads are loading: all false.
        assertEquals(
            emptyList<AmityCommunityCreateAction>(),
            actions(post = false, story = false, event = false),
        )
    }

    @Test
    fun `with Post withheld the sheet draws the same rows the plus counted`() {
        withholdPostAsStagingDoes()
        val community = mockk<AmityCommunity>(relaxed = true)
        lateinit var labels: Map<AmityCommunityCreateAction, String>
        composeTestRule.setContent {
            labels = mapOf(
                POST to amitySocialString("amity_social_button_social_home_create_post_button"),
                STORY to amitySocialString("amity_social_button_story"),
                EVENT to amitySocialString("amity_social_button_event"),
            )
            AmityBasePage(pageId = "community_profile_page") {
                AmityCommunityProfileActionsContainer(
                    pageScope = getPageScope(),
                    community = community,
                    shouldShowPostCreationButton = true,
                    shouldShowStoryCreationButton = true,
                    shouldShowEventCreationButton = true,
                    onDismiss = {},
                )
            }
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(labels.getValue(STORY)).assertExists()
        composeTestRule.onNodeWithText(labels.getValue(EVENT)).assertExists()
        composeTestRule.onNodeWithText(labels.getValue(POST)).assertDoesNotExist()
    }
}
