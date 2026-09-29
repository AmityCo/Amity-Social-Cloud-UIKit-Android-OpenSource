@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.notificationtray

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The tray belongs to no module, so each item is gated on tap: by its subject's
 * module and by the page it would open.
 */
class AmityNotificationTrayGateTest {

    private fun item(actionType: String, category: String = "", targetType: String = "") =
        mockk<AmityNotificationTrayItem>().also {
            every { it.getActionType() } returns actionType
            every { it.getTrayItemCategory() } returns category
            every { it.getTargetType() } returns targetType
        }

    private fun withhold(vararg keys: String) =
        ModuleEntitlementForTests.withhold(*keys)

    @After
    fun tearDown() {
        ModuleEntitlementForTests.clear()
    }

    @Test
    fun `an item whose subject's module is withheld does not open`() {
        withhold("comment")
        assertFalse(item("comment").canOpen("post_detail_page"))
        assertFalse(item("mention", category = "mention_in_comment").canOpen("post_detail_page"))
        // A post item is still Post's, and Post is available.
        assertTrue(item("post").canOpen("post_detail_page"))
    }

    @Test
    fun `a follow item goes with userRelationship`() {
        withhold("userRelationship")
        assertFalse(item("follow", category = "follow").canOpen("user_profile_page"))
    }

    @Test
    fun `an item whose destination page is withheld does not open`() {
        withhold("post")
        // Post owns post_detail_page, so a reaction on a post has nowhere to go.
        assertFalse(item("reaction", category = "reaction_on_post").canOpen("post_detail_page"))
    }

    @Test
    fun `a room invitation goes with Live, and has no page id to check`() {
        withhold("live")
        assertFalse(item("invitation", targetType = "room").canOpen(null))
    }

    @Test
    fun `with nothing withheld every item opens`() {
        assertTrue(item("comment").canOpen("post_detail_page"))
        assertTrue(item("event").canOpen("event_detail_page"))
        assertTrue(item("user").canOpen("edit_user_profile_page"))
    }
}
