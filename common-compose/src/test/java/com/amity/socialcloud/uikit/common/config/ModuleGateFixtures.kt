@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.sdk.model.core.module.AmityModuleDefinition
import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.sdk.model.core.module.AmityModuleKind
import com.amity.socialcloud.sdk.model.core.module.AmityModuleSettings
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk

/**
 * The module gate's one source, the network's entitlements, for tests.
 *
 * Every network launches `mode: "off"`, so an enforcing network is reachable
 * only through the seam these use.
 */
object ModuleGateFixtures {

    /**
     * Core's catalog chains for the 15 `api` keys the UIKit gates, as the
     * staging payload carried them on 2026-09-24. `requires` is any-of.
     */
    val CHAINS: Map<String, List<String>> = mapOf(
        "community" to emptyList(),
        "chat" to emptyList(),
        "live" to listOf("post"),
        "poll" to listOf("post"),
        "userRelationship" to emptyList(),
        "pushNotification" to emptyList(),
        "post" to listOf("community"),
        "story" to listOf("community"),
        "events" to listOf("community"),
        "feed" to listOf("post"),
        "comment" to listOf("post", "story"),
        "reaction" to listOf("post", "comment", "chat", "story"),
        "product" to listOf("post", "story"),
        "ads" to listOf("post", "story"),
        "discovery" to listOf("community", "post", "chat"),
    )

    fun definition(requires: List<String>, kind: AmityModuleKind = AmityModuleKind.API) =
        mockk<AmityModuleDefinition>().also {
            every { it.requires } returns requires
            every { it.kind } returns kind
            every { it.label } returns "label"
        }

    fun catalog(
        overrides: Map<String, List<String>> = emptyMap(),
    ): Map<String, AmityModuleDefinition> =
        (CHAINS + overrides).mapValues { (_, requires) -> definition(requires) }

    fun entitlement(
        mode: AmityModuleEnforcementMode,
        granted: Set<String>,
        catalog: Map<String, AmityModuleDefinition> = catalog(),
    ): AmityModuleSettings = mockk<AmityModuleSettings>().also {
        every { it.enforcement } returns mode
        every { it.modules } returns granted.associateWith { true }
        every { it.catalog } returns catalog
    }

    /** An enforcing network that granted exactly [granted]. */
    fun enforce(
        granted: Set<String>,
        catalog: Map<String, AmityModuleDefinition> = catalog(),
    ) = AmityUIKitConfigController.setModuleEntitlementForTesting(
        entitlement(AmityModuleEnforcementMode.ENFORCE, granted, catalog),
    )

    /** An enforcing network that granted every catalog module but [withheld]. */
    fun grantAllExcept(vararg withheld: String) = enforce(CHAINS.keys - withheld.toSet())

    /** No payload, the bundled config's shape. */
    fun reset() {
        AmityUIKitConfigController.setConfigForTesting(
            // Gson skips the constructor, so a field the JSON omits is null rather
            // than its default — the shipped config carries feature_flags.
            Gson().fromJson(
                """{"excludes": [], "feature_flags": {"post": {}, "chat": {"conversation_chat_user_actions": []}}}""",
                AmityUIKitConfig::class.java,
            ),
        )
        AmityUIKitConfigController.setModuleEntitlementForTesting(null)
    }
}
