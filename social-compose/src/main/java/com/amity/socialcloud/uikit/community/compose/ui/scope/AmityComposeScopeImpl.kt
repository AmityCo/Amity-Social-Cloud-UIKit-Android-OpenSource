package com.amity.socialcloud.uikit.community.compose.ui.scope

import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController

internal class AmityComposePageScopeImpl(
    private val pageId: String,
) : AmityComposePageScope {

    override fun getId(): String {
        return pageId
    }

    override fun getConfigId(): String {
        return "$pageId/*/*"
    }

    override fun getPageTheme(): AmityUIKitConfig.GlobalTheme? {
        return try {
            AmityUIKitConfigController.getPageTheme(getConfigId())
        } catch (e: Exception) {
            null
        }
    }
}

internal class AmityComposeComponentScopeImpl(
    pageScope: AmityComposePageScope? = null,
    private val componentId: String,
) : AmityComposeComponentScope {

    private val pageId = pageScope?.getId() ?: "*"

    override fun getId(): String {
        return componentId
    }

    override fun getConfigId(): String {
        return "$pageId/$componentId/*"
    }

    override fun getComponentTheme(): AmityUIKitConfig.GlobalTheme? {
        return try {
            AmityUIKitConfigController.getComponentTheme(getConfigId())
        } catch (e: Exception) {
            null
        }
    }
}

internal class AmityComposeElementScopeImpl(
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    private val elementId: String,
) : AmityComposeElementScope {

    private val pageId = pageScope?.getId() ?: "*"
    private val componentId = componentScope?.getId() ?: "*"

    override fun getId(): String {
        return elementId
    }

    override fun getConfigId(): String {
        return "$pageId/$componentId/$elementId"
    }
}