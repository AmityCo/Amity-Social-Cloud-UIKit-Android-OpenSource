@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.common.config

import com.google.gson.Gson
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// A `*` in an `excludes` entry stands for any page or component, as it does on iOS.
//
// Before, matching was exact, so `*` only matched an element that happened to have no
// page scope. Giving the create-post menu items their page scope (PDT-5431) silently
// stopped the page-wildcard entry for the livestream button from hiding it (PDT-6219).
class AmityExcludesWildcardTest {

    @After
    fun tearDown() = ModuleGateFixtures.reset()

    private fun exclude(vararg ids: String) {
        val list = ids.joinToString { "\"$it\"" }
        AmityUIKitConfigController.setConfigForTesting(
            Gson().fromJson("""{"excludes": [$list]}""", AmityUIKitConfig::class.java),
        )
        AmityUIKitConfigController.setModuleEntitlementForTesting(null)
    }

    @Test
    fun `an exact entry still matches`() {
        exclude("social_home_page/create_post_menu/create_livestream_button")
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_livestream_button"))
    }

    @Test
    fun `a page wildcard matches the element on any page`() {
        exclude("*/create_post_menu/create_livestream_button", "*/create_post_menu/create_clip_button")
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_livestream_button"))
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_clip_button"))
        assertTrue(AmityUIKitConfigController.isExcluded("*/create_post_menu/create_clip_button"))
    }

    @Test
    fun `a component wildcard matches every element of that component`() {
        exclude("*/create_post_menu/*")
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_poll_button"))
    }

    @Test
    fun `a page and component wildcard matches the element anywhere`() {
        exclude("*/*/create_livestream_button")
        assertTrue(AmityUIKitConfigController.isExcluded("event_detail_page/*/create_livestream_button"))
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_livestream_button"))
    }

    @Test
    fun `a wildcard does not reach a different element or component`() {
        exclude("*/create_post_menu/create_livestream_button")
        assertFalse(AmityUIKitConfigController.isExcluded("social_home_page/create_post_menu/create_post_button"))
        assertFalse(AmityUIKitConfigController.isExcluded("community_profile_page/*/create_livestream_button"))
    }

    @Test
    fun `a page-qualified entry does not leak to other pages`() {
        exclude("social_home_page/create_post_menu/create_livestream_button")
        assertFalse(AmityUIKitConfigController.isExcluded("event_detail_page/*/create_livestream_button"))
    }
}
