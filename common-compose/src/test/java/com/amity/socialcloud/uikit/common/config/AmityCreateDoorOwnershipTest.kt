package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.grantAllExcept
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

/**
 * PDT-5867, PDT-5617. The two create doors — the Social Home "+"
 * (`post_creation_button`) with the menu it opens (`create_post_menu`), and the
 * community page's floating "+" (`community_create_post_button`) — belong to no
 * module. Each row behind them keeps its own owner, so the gate decides the rows
 * and the door is drawn while one of them is left (module-availability-spec §10).
 *
 * Read against the staging catalog fixture, where Poll, Live and Feed need Post:
 * with Post withheld the rows left are Story and Event.
 */
class AmityCreateDoorOwnershipTest {

    // Page left as "*": withholding Community takes community_profile_page, and
    // every door on it, with it. That is the page's owner, not the door's.
    private val doors = listOf(
        "*/top_navigation/post_creation_button",
        "*/create_post_menu/*",
        "*/*/community_create_post_button",
    )

    private val rows = listOf(
        "create_post_button",
        "create_poll_button",
        "create_livestream_button",
        "create_story_button",
        "create_clip_button",
        "create_event_button",
    )

    @Before
    fun setUp() = ModuleGateFixtures.reset()

    @After
    fun tearDown() = ModuleGateFixtures.reset()

    private fun rowsLeft(page: String, component: String) =
        rows.filterNot { AmityUIKitConfigController.isExcluded("$page/$component/$it") }

    @Test
    fun `no module withheld takes a door with it`() {
        for (module in ModuleGateFixtures.CHAINS.keys) {
            grantAllExcept(module)
            for (door in doors) {
                assertFalse("$module is withheld and took $door with it", AmityUIKitConfigController.isExcluded(door))
            }
        }
    }

    @Test
    fun `with Post withheld the rows left are Story and Event`() {
        grantAllExcept("post")

        assertEquals(
            listOf("create_story_button", "create_event_button"),
            rowsLeft("social_home_page", "create_post_menu"),
        )
        assertEquals(
            listOf("create_story_button", "create_event_button"),
            rowsLeft("community_profile_page", "*"),
        )
    }

    @Test
    fun `with every row's module withheld no row is left and the doors are still unowned`() {
        grantAllExcept("post", "story", "events")

        assertEquals(emptyList<String>(), rowsLeft("social_home_page", "create_post_menu"))
        assertEquals(emptyList<String>(), rowsLeft("community_profile_page", "*"))
        // The gate leaves the door; the door's own predicate hides it.
        for (door in doors) {
            assertFalse(AmityUIKitConfigController.isExcluded(door))
        }
    }

    @Test
    fun `with nothing withheld every row is left`() {
        grantAllExcept()

        assertEquals(rows, rowsLeft("social_home_page", "create_post_menu"))
        assertEquals(rows, rowsLeft("community_profile_page", "*"))
    }
}
