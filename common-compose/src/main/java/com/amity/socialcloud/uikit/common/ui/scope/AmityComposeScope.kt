package com.amity.socialcloud.uikit.common.ui.scope

import androidx.annotation.DrawableRes
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

    fun getPageTheme(): AmityUIKitConfig.UIKitTheme?

    fun showSnackbar(
        message: String,
        @DrawableRes drawableRes: Int? = null,
        additionalHeight: Int = 0
    )

    fun showProgressSnackbar(message: String)
}

interface AmityComposeComponentScope : AmityComposeScope {

    fun getComponentScope(): AmityComposeComponentScope {
        return this
    }

    fun getComponentTheme(): AmityUIKitConfig.UIKitTheme?

    fun showSnackbar(
        message: String,
        @DrawableRes drawableRes: Int? = null,
        additionalHeight: Int = 0
    )

    fun getAccessibilityId(viewId: String = ""): String
}

interface AmityComposeElementScope : AmityComposeScope {

    fun getElementScope(): AmityComposeElementScope {
        return this
    }

    fun showSnackbar(message: String, @DrawableRes drawableRes: Int? = null)

    fun getAccessibilityId(viewId: String = ""): String
}