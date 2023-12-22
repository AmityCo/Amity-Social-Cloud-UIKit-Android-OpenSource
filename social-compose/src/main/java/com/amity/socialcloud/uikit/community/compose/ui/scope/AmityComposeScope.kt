package com.amity.socialcloud.uikit.community.compose.ui.scope

import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.google.gson.JsonObject

interface AmityComposeScope {

    fun getId(): String

    fun getConfigId(): String

    fun getConfig(): JsonObject {
        return AmityUIKitConfigController.getCustomizationConfig(getConfigId())
    }

    fun isExcluded(): Boolean {
        return AmityUIKitConfigController.isExcluded(getConfigId())
    }
}

interface AmityComposePageScope : AmityComposeScope {

    fun getPageScope(): AmityComposePageScope {
        return this
    }

    fun getPageTheme(): AmityUIKitConfig.GlobalTheme?
}

interface AmityComposeComponentScope : AmityComposeScope {

    fun getComponentScope(): AmityComposeComponentScope {
        return this
    }

    fun getComponentTheme(): AmityUIKitConfig.GlobalTheme?
}

interface AmityComposeElementScope : AmityComposeScope {

    fun getElementScope(): AmityComposeElementScope {
        return this
    }
}