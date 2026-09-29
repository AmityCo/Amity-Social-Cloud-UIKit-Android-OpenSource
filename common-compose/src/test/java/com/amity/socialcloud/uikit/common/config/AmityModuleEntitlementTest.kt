@file:OptIn(AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.sdk.model.core.module.AmityModuleKind
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.CHAINS
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.catalog
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.definition
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.enforce
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.entitlement
import com.amity.socialcloud.uikit.common.config.ModuleGateFixtures.grantAllExcept
import com.google.gson.Gson
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Spec: UIKit module availability — the network's plan is the one source that
 * withholds a module (§1, #310), resolved over the catalog's chains.
 */
class AmityModuleEntitlementTest {

    @Before
    fun setUp() = ModuleGateFixtures.reset()

    @After
    fun tearDown() = ModuleGateFixtures.reset()

    private fun availability(feature: AmityUIKitFeature) =
        AmityUIKitConfigController.moduleAvailability(feature)

    // tc: TC-uikit-mavail-001 — the plan is the only source
    @Test
    fun `a features block in config json withholds nothing`() {
        AmityUIKitConfigController.setConfigForTesting(
            Gson().fromJson(
                """{"features": {"chat": {"enabled": false}}, "excludes": []}""",
                AmityUIKitConfig::class.java,
            ),
        )
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.CHAT))
        assertFalse(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
    }

    // tc: TC-uikit-mavail-002
    @Test
    fun `ungranted under enforce reports notGranted and hides the pages it owns`() {
        grantAllExcept("chat")
        assertEquals(
            AmityUIKitModuleAvailability.NotGranted(AmityUIKitFeature.CHAT),
            availability(AmityUIKitFeature.CHAT),
        )
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("post_detail_page/*/*"))
    }

    // tc: TC-uikit-mavail-003
    @Test
    fun `a granted module is available`() {
        grantAllExcept()
        assertEquals(
            AmityUIKitModuleAvailability.Available(AmityUIKitFeature.LIVE),
            availability(AmityUIKitFeature.LIVE),
        )
    }

    // tc: TC-uikit-mavail-004
    @Test
    fun `off with no grants leaves every module available`() {
        AmityUIKitConfigController.setModuleEntitlementForTesting(
            entitlement(AmityModuleEnforcementMode.OFF, emptySet()),
        )
        AmityUIKitFeature.values().forEach {
            assertTrue("$it withheld under off", AmityUIKitConfigController.isFeatureEnabled(it))
        }
    }

    // tc: TC-enum-modmode-004 — shadow is a dry run
    @Test
    fun `shadow resolves exactly as off`() {
        AmityUIKitConfigController.setModuleEntitlementForTesting(
            entitlement(AmityModuleEnforcementMode.SHADOW, emptySet()),
        )
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.CHAT))
    }

    // tc: TC-uikit-mavail-005 — REQ-012
    @Test
    fun `with no payload every module is available and nothing cascades`() {
        AmityUIKitFeature.values().forEach {
            assertEquals(AmityUIKitModuleAvailability.Available(it), availability(it))
        }
        // The cascade a payload would state is not invented without one: an
        // enforcing network that withholds community but states no chains does
        // not take post with it.
        enforce(CHAINS.keys - "community", catalog(CHAINS.mapValues { emptyList() }))
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.POST))
    }

    // tc: TC-uikit-mavail-006 — REQ-017
    @Test
    fun `the catalog's requires is any-of, so a story-only network keeps Comment`() {
        enforce(setOf("community", "story", "comment"))
        assertTrue(AmityUIKitConfigController.isFeatureEnabled(AmityUIKitFeature.COMMENT))
    }

    // tc: TC-uikit-mavail-007 — REQ-013; the catalog is the only table now
    @Test
    fun `a chain the build never shipped is applied from the catalog`() {
        enforce(CHAINS.keys - "post", catalog(mapOf("chat" to listOf("post"))))
        assertEquals(
            AmityUIKitModuleAvailability.PrerequisiteUnavailable(AmityUIKitFeature.CHAT, listOf("post")),
            availability(AmityUIKitFeature.CHAT),
        )
    }

    // tc: TC-uikit-mavail-008 — §6.2
    @Test
    fun `clip follows Post and nothing else switches it`() {
        grantAllExcept()
        assertTrue(AmityUIKitConfigController.isClipEnabled())
        assertFalse(AmityUIKitConfigController.isExcluded("clip_feed_page/*/*"))

        // Post unavailable: every clip surface goes with it.
        grantAllExcept("post")
        assertFalse(AmityUIKitConfigController.isClipEnabled())
        assertTrue(AmityUIKitConfigController.isExcluded("clip_feed_page/*/*"))
        assertTrue(AmityUIKitConfigController.isExcluded("social_home_page/*/clipsfeed_button"))

        // Post available: every clip surface is on, and there is no separate clip switch.
        grantAllExcept()
        assertTrue(AmityUIKitConfigController.isClipEnabled())
        assertFalse(AmityUIKitConfigController.isExcluded("create_clip_post_page/*/*"))
        assertFalse(AmityUIKitConfigController.isExcluded("community_profile_page/community_clip_feed/*"))

        // Never a public module, never asked of the grants.
        assertTrue(AmityUIKitFeature.values().none { it.key == "clip" })
        assertTrue(AmityUIKitConfigController.moduleAvailabilities().none { it.feature.key == "clip" })
    }

    // tc: TC-uikit-mavail-009 — REQ-015
    @Test
    fun `a kind setting catalog entry is absent from moduleAvailability`() {
        enforce(
            CHAINS.keys,
            catalog() + mapOf("aiInsight" to definition(emptyList(), AmityModuleKind.SETTING)),
        )
        assertTrue(AmityUIKitConfigController.moduleAvailabilities().none { it.feature.key == "aiInsight" })
        assertEquals(AmityUIKitFeature.values().size, AmityUIKitConfigController.moduleAvailabilities().size)
    }

    // tc: TC-uikit-mavail-010 — REQ-023
    @Test
    fun `prerequisiteUnavailable carries every unsatisfied entry`() {
        enforce(setOf("community", "product"))
        assertEquals(
            AmityUIKitModuleAvailability.PrerequisiteUnavailable(
                AmityUIKitFeature.PRODUCT,
                listOf("post", "story"),
            ),
            availability(AmityUIKitFeature.PRODUCT),
        )
    }

    // tc: TC-uikit-mavail-010a — REQ-023; getModuleSettings REQ-011
    @Test
    fun `an unknown prerequisite key is carried raw, not dropped`() {
        enforce(
            CHAINS.keys,
            catalog(mapOf("product" to listOf("externalContent"), "externalContent" to emptyList())),
        )
        assertEquals(
            AmityUIKitModuleAvailability.PrerequisiteUnavailable(
                AmityUIKitFeature.PRODUCT,
                listOf("externalContent"),
            ),
            availability(AmityUIKitFeature.PRODUCT),
        )
    }

    // tc: TC-uikit-mavail-011 — REQ-010
    @Test
    fun `ungranted and short of its prerequisites reports notGranted`() {
        enforce(setOf("chat"))
        assertEquals(
            AmityUIKitModuleAvailability.NotGranted(AmityUIKitFeature.FEED),
            availability(AmityUIKitFeature.FEED),
        )
    }

    // tc: TC-uikit-mavail-012b / TC-uikit-mavail-013 — the memo cannot outlive
    // the grants it was built on
    @Test
    fun `a new entitlement replaces the old one and clears the exclusion memo`() {
        grantAllExcept("chat")
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
        grantAllExcept()
        assertFalse(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
    }

    // chat user action REQ-008 / REQ-008a
    @Test
    fun `withholding userRelationship removes the block row and keeps report`() {
        grantAllExcept("userRelationship")
        assertFalse(AmityUIKitConfigController.isChatUserActionAvailable("block"))
        assertTrue(AmityUIKitConfigController.isChatUserActionAvailable("report"))
        assertTrue(AmityUIKitConfigController.isChatUserActionAvailable("mute"))
        assertTrue(AmityUIKitConfigController.hasAnyEnabledChatUserAction())
        assertTrue(AmityUIKitConfigController.isExcluded("user_profile_page/*/block_user_button"))
        assertTrue(AmityUIKitConfigController.isExcluded("user_profile_page/*/manage_blocked_users_button"))
        assertTrue(AmityUIKitConfigController.isExcluded("user_profile_page/*/unfollow_user_button"))
    }
}
