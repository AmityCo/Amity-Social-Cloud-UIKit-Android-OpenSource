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

/**
 * Whether an element under this scope would render.
 *
 * A container is not an element. The divider between a post's reaction summary
 * and its action row, and the padding around them, carry no config id of their
 * own, so switching Reaction and Comment off left a rule and an empty band
 * where the buttons had been. A host asks here what its children will do and
 * disappears with the last one.
 *
 * Null-tolerant because most components take their scope as an optional
 * parameter; with no scope there is nothing to exclude against.
 */
fun AmityComposeScope?.isElementExcluded(elementId: String): Boolean {
    val scope = this ?: return false
    val id = scope.getConfigId().split('/')
    val page = id.getOrNull(0)?.takeIf { it.isNotEmpty() } ?: "*"
    val component = id.getOrNull(1)?.takeIf { it.isNotEmpty() } ?: "*"
    return AmityUIKitConfigController.isExcluded("$page/$component/$elementId")
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

    fun getPageScope() : AmityComposePageScope?
}

interface AmityComposeElementScope : AmityComposeScope {

    fun getElementScope(): AmityComposeElementScope {
        return this
    }

    fun getAccessibilityId(viewId: String = ""): String

    fun getElementTheme(): AmityUIKitConfig.UIKitTheme?
}

/**
 * Whether any of these elements would render.
 *
 * The other half of the container problem: a menu whose every item belongs to a
 * module, and a button whose only job is to open that menu. Gating the items
 * alone leaves a floating action button that opens an empty popup.
 */
fun AmityComposeScope?.anyElementVisible(vararg elementIds: String): Boolean =
    elementIds.any { !isElementExcluded(it) }

/**
 * Whether a component would render, asked by a host that does not wrap it.
 *
 * Wrapping is the normal way and stays the normal way. Some components are a
 * single view rendered deep inside a paged list — the feed ad, the comment ad —
 * where a wrapper would mean re-indenting the whole body to gain nothing the
 * question does not already answer.
 */
fun isComponentExcluded(
    pageScope: AmityComposePageScope? = null,
    componentId: String,
): Boolean {
    val page = pageScope?.getConfigId()?.split('/')?.getOrNull(0)?.takeIf { it.isNotEmpty() } ?: "*"
    return AmityUIKitConfigController.isExcluded("$page/$componentId/*")
}


/**
 * Whether an element would render, asked from outside the page's scope.
 *
 * The sibling of [isComponentExcluded], for the other case a wrapper cannot
 * reach: a subscription opened above `AmityBasePage`. The livestream pages
 * subscribe to live reactions before the scope exists, so the overlay's wrapper
 * cannot stop the stream — rule 6 wants the module off to mean no request at
 * all, which means asking here with the page id spelled out.
 */
fun isElementExcludedOnPage(pageId: String, elementId: String): Boolean =
    AmityUIKitConfigController.isExcluded("$pageId/*/$elementId")
