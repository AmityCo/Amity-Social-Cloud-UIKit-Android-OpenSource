package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.grantAllExcept
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * A withheld module is only withheld if its surfaces actually go. Apollo can
 * prove the three platforms generate the same owner maps; it cannot prove the
 * lookup over them is right. This asserts that, through the exact call every
 * page, component and element already makes.
 */
class AmityModuleFlagTest {

    @Before
    fun setUp() = ModuleGateFixtures.reset()

    @After
    fun tearDown() = ModuleGateFixtures.reset()

    @Test
    fun `nothing withheld hides nothing`() {
        grantAllExcept()
        assertFalse(AmityUIKitConfigController.isExcluded("post_detail_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("clip_feed_page/*/*"))
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.ADS))
    }

    @Test
    fun `a withheld module hides the pages it owns, and nothing else`() {
        grantAllExcept("chat")
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("chat_home_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("post_detail_page/*/*"))
    }

    @Test
    fun `a prerequisite going takes its dependents with it`() {
        grantAllExcept("community")
        // post requires community; feed requires post; clip rides post.
        assertTrue(AmityUIKitConfigController.isExcluded("community_profile_page/*/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("post_detail_page/*/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("clip_feed_page/*/*"))
        // chat depends on nothing, so it survives.
        assertFalse(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
    }

    @Test
    fun `a module with no page of its own is gated at its components`() {
        grantAllExcept("feed")
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/newsfeed_component/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/global_feed_component/*"))
        // The page hosting the feed is not the feed, and stays.
        assertFalse(AmityUIKitConfigController.isExcluded("social_home_page/*/*"))
    }

    @Test
    fun `Live and Poll follow Post, as core's catalog says`() {
        grantAllExcept("post")
        assertFalse(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.LIVE))
        assertFalse(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.POLL))
        // Community sells alone and is untouched by Post going.
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.COMMUNITY))
    }

    @Test
    fun `an any-of requirement survives while one of its alternatives is on`() {
        grantAllExcept("post", "story")
        // reaction needs one of post/comment/chat/story — chat is still on.
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.REACTION))
        // product needs one of post/story — both are gone.
        assertFalse(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.PRODUCT))
    }

    @Test
    fun `the notification tray belongs to no module, and preferences to Push Notification`() {
        // Owned by any one module the tray would empty whole when that module
        // went, items from modules the customer still has included. It is gated
        // per item instead, on tap.
        grantAllExcept("feed")
        assertFalse(AmityUIKitConfigController.isExcluded("notification_tray_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("social_home_page/*/notification_tray_button"))

        grantAllExcept("pushNotification")
        assertTrue(AmityUIKitConfigController.isExcluded("notification_preference_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("notification_tray_page/*/*"))
    }

    @Test
    fun `a component renders inside another module's page and is still gated`() {
        // The page table alone could not reach these: the story tab renders in
        // the newsfeed, the live chat feed inside the livestream player.
        grantAllExcept("story")
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/story_tab_component/*"))

        grantAllExcept("chat")
        assertTrue(AmityUIKitConfigController.isExcluded("live_stream_page/livestream_chat_feed/*"))
        // Live itself is untouched.
        assertFalse(AmityUIKitConfigController.isExcluded("livestream_player_page/*/*"))
    }

    @Test
    fun `an unmapped page is never gated`() {
        grantAllExcept("community")
        assertFalse(AmityUIKitConfigController.isExcluded("visitor_usage_limit_page/*/*"))
    }
}
