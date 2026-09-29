@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.socialhome

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.permission.AmityGlobalPermissionValidator
import com.amity.socialcloud.sdk.api.core.permission.AmityPermissionValidator
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.chat.settings.AmitySocialSettings
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.user.AmityUserType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponent
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PDT-5867. The Social Home "+" (`post_creation_button`) and the menu it opens
 * (`create_post_menu`) belong to no module. The "+" is drawn while the menu has
 * at least one row to show — the module gate on each row AND the per-user checks
 * the rows carry (the story setting, the create-event permission) — so it can
 * neither vanish with Post nor open an empty menu.
 *
 * The override layer holds no catalog, so it resolves no prerequisites: switching
 * Post off here leaves Poll and Live on. [withholdPostAsStagingDoes] names the
 * dependents staging's catalog takes with Post (Poll, Live, Feed), so those cases
 * read the way the network does.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AmitySocialHomeCreateButtonGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val door = "social_home_page/top_navigation/post_creation_button"
    private val postRow = "social_home_page/create_post_menu/create_post_button"
    private val storyRow = "social_home_page/create_post_menu/create_story_button"
    private val eventRow = "social_home_page/create_post_menu/create_event_button"

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        AmityUIKitConfigController.setup(ApplicationProvider.getApplicationContext())
        // Before any label is read: LocalAmitySocialStringProvider caches its
        // default on first read, and the fallback echoes keys for every test
        // that shares this Robolectric sandbox afterwards.
        DefaultAmitySocialStringProvider.initialize(ApplicationProvider.getApplicationContext())
        mockkObject(AmityCoreClient)
        mockkObject(AmitySocialClient)
        every { AmityCoreClient.getCurrentSessionState() } returns SessionState.NotLoggedIn
        every { AmityCoreClient.observeSessionState() } returns Flowable.never()
        every { AmityCoreClient.getCurrentUserType() } returns AmityUserType.SIGNED_IN
        permissions(story = Flowable.just(true), event = Flowable.just(true))
    }

    @After
    fun tearDown() {
        ModuleEntitlementForTests.clear()
        unmockkObject(AmitySocialClient)
        unmockkObject(AmityCoreClient)
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    private fun permissions(story: Flowable<Boolean>, event: Flowable<Boolean>) {
        val settings = mockk<AmitySocialSettings> {
            every { getStorySettings().isAllowAllUserToCreateStory() } returns false
        }
        every { AmitySocialClient.getSettings() } returns Flowable.just(settings)
        every { AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY) } returns
            validator(story)
        every { AmityCoreClient.hasPermission(AmityPermission.CREATE_EVENT) } returns
            validator(event)
    }

    private fun validator(result: Flowable<Boolean>): AmityPermissionValidator {
        val global = mockk<AmityGlobalPermissionValidator> { every { check() } returns result }
        return mockk { every { atGlobal() } returns global }
    }

    private fun withhold(vararg keys: String) =
        ModuleEntitlementForTests.withhold(*keys)

    private fun withholdPostAsStagingDoes(vararg more: String) =
        withhold("post", "poll", "live", "feed", *more)

    private fun render() {
        composeTestRule.setContent {
            AmityBasePage(pageId = "social_home_page") {
                AmitySocialHomeTopNavigationComponent(
                    pageScope = getPageScope(),
                    selectedTab = AmitySocialHomePageTab.EVENTS,
                    searchButtonAction = {},
                    notificationButton = {},
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    private fun assertDoor(drawn: Boolean) {
        val node = composeTestRule.onNodeWithTag(door)
        if (drawn) node.assertExists() else node.assertDoesNotExist()
    }

    @Test
    fun `with nothing withheld the plus is drawn`() {
        render()
        assertDoor(drawn = true)
    }

    @Test
    fun `with Post withheld the plus is drawn and opens Story and Event`() {
        withhold("post")
        render()

        assertDoor(drawn = true)
        composeTestRule.onNodeWithTag(door).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(storyRow, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(eventRow, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(postRow, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun `with Post withheld as staging withholds it the plus is still drawn`() {
        withholdPostAsStagingDoes()
        render()
        assertDoor(drawn = true)
    }

    @Test
    fun `with every row's module withheld the plus is not drawn`() {
        withhold("post", "poll", "live", "story", "events")
        render()
        assertDoor(drawn = false)
    }

    @Test
    fun `with Post and Story withheld and no create-event permission the plus is not drawn`() {
        withholdPostAsStagingDoes("story")
        permissions(story = Flowable.just(true), event = Flowable.just(false))
        render()
        assertDoor(drawn = false)
    }

    @Test
    fun `with Post and Story withheld and the create-event permission the plus opens Event`() {
        withholdPostAsStagingDoes("story")
        permissions(story = Flowable.just(true), event = Flowable.just(true))
        render()

        assertDoor(drawn = true)
        composeTestRule.onNodeWithTag(door).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(eventRow, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(storyRow, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun `with Post withheld and no story or event permission the plus is not drawn`() {
        withholdPostAsStagingDoes()
        permissions(story = Flowable.just(false), event = Flowable.just(false))
        render()
        assertDoor(drawn = false)
    }

    @Test
    fun `while the only remaining row's permission is loading the plus stays hidden`() {
        withholdPostAsStagingDoes("story")
        permissions(story = Flowable.just(true), event = Flowable.never())
        render()
        assertDoor(drawn = false)
    }
}
