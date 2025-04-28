package com.amity.socialcloud.uikit.common.ui.scope

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

interface AmityComposePageScope : AmityComposeScope, SnackbarScope {

    fun getPageScope(): AmityComposePageScope {
        return this
    }

    fun getPageTheme(): AmityUIKitConfig.UIKitTheme?



}

interface AmityComposeComponentScope : AmityComposeScope, SnackbarScope {

    fun getComponentScope(): AmityComposeComponentScope {
        return this
    }

    fun getComponentTheme(): AmityUIKitConfig.UIKitTheme?

    fun getAccessibilityId(viewId: String = ""): String
}

interface AmityComposeElementScope : AmityComposeScope {

    fun getElementScope(): AmityComposeElementScope {
        return this
    }

    fun getAccessibilityId(viewId: String = ""): String
}

