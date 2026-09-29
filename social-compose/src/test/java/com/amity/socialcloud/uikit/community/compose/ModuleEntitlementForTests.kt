@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.community.compose

import com.amity.socialcloud.sdk.model.core.module.AmityModuleDefinition
import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.sdk.model.core.module.AmityModuleKind
import com.amity.socialcloud.sdk.model.core.module.AmityModuleSettings
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
import io.mockk.every
import io.mockk.mockk

/**
 * The network's module settings, faked for this module's gate tests.
 *
 * The gate has one source — the network's answer — so withholding a module in a
 * test means answering as an enforcing network that did not grant it. The
 * catalog carries every gated module with no prerequisite chains, so exactly the
 * named modules are withheld and nothing cascades: each test is about one
 * surface asking about one module, not about the bundle rules.
 */
object ModuleEntitlementForTests {

    fun withhold(vararg keys: String) {
        val withheld = keys.toSet()
        val all = AmityUIKitFeature.values().map { it.key }
        val catalog = all.associateWith {
            mockk<AmityModuleDefinition>().also { definition ->
                every { definition.requires } returns emptyList()
                every { definition.kind } returns AmityModuleKind.API
                every { definition.label } returns it
            }
        }
        val settings = mockk<AmityModuleSettings>().also {
            every { it.enforcement } returns AmityModuleEnforcementMode.ENFORCE
            every { it.modules } returns all.associateWith { key -> key !in withheld }
            every { it.catalog } returns catalog
        }
        AmityUIKitConfigController.setModuleEntitlementForTesting(settings)
    }

    /** No answer from the network: every module available. */
    fun clear() = AmityUIKitConfigController.setModuleEntitlementForTesting(null)
}
