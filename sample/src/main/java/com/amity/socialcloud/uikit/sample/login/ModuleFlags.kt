@file:OptIn(AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.sample.login

import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.config.AmityUIKitModuleAvailability
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature

/**
 * The Phase 1 modules as the network reports them, read-only.
 *
 * Everything here reads AmityUIKitConfigController directly, so the screen
 * cannot show a module as on that the UIKit will treat as off. The list, the
 * dependencies and the verdicts all come from the UIKit; only the labels live
 * here.
 *
 * There is nothing to switch. The network's module settings, read through
 * `getModuleSettings()`, are the only source that withholds a module, as on
 * Web: to see a module withheld, change it in OPS on a network set to
 * `enforce` and relaunch.
 */
object ModuleFlags {

    /** A row on the screen: one module. */
    data class Row(val key: String)

    /** Reading order, coarse to fine: what sells alone, then what builds on it. */
    val order: List<Row> = listOf(
        AmityUIKitFeature.COMMUNITY,
        AmityUIKitFeature.CHAT,
        AmityUIKitFeature.LIVE,
        AmityUIKitFeature.POLL,
        AmityUIKitFeature.USER_RELATIONSHIP,
        AmityUIKitFeature.PUSH_NOTIFICATION,
        AmityUIKitFeature.POST,
        AmityUIKitFeature.STORY,
        AmityUIKitFeature.EVENTS,
        AmityUIKitFeature.FEED,
        AmityUIKitFeature.COMMENT,
        AmityUIKitFeature.REACTION,
        AmityUIKitFeature.PRODUCT,
        AmityUIKitFeature.ADS,
        AmityUIKitFeature.DISCOVERY,
    ).map { Row(it.key) }

    private val labels = mapOf(
        "community" to "Community",
        "chat" to "Chat",
        "live" to "Live",
        "poll" to "Poll",
        "userRelationship" to "User Relationship",
        "pushNotification" to "Push Notification",
        "post" to "Post",
        "story" to "Story",
        "events" to "Events",
        "feed" to "Feed",
        "comment" to "Comment",
        "reaction" to "Reaction",
        "product" to "Product",
        "ads" to "Ads",
        "discovery" to "Discovery",
        // A catalog key with no UIKit surface: never a row, but it is named in
        // the withheld list and the counts.
        "externalContent" to "External Content",
    )

    private val notes = mapOf(
        "post" to "includes clip",
        "feed" to "feed surfaces, notification tray, For You",
        "discovery" to "search, trending, recommendation",
        "pushNotification" to "OS-level push settings",
    )

    fun label(key: String): String = labels[key] ?: key

    fun note(key: String): String? = notes[key]

    /** The requirement the gate itself applied — the network's catalog, and nothing without one. */
    fun requirementText(key: String): String? =
        AmityUIKitConfigController.moduleRequires(key)
            .joinToString(" or ") { label(it) }
            .ifEmpty { null }

    /** The network's state in a sentence, and the grants under it. */
    fun entitlementSummary(): Pair<String, String?> {
        val settings = AmityUIKitConfigController.heldEntitlement()
            ?: return "No answer yet — this network has not returned module settings. " +
                "Every module is available until it does." to null
        val mode = when (settings.enforcement) {
            AmityModuleEnforcementMode.ENFORCE ->
                "Enforcing — a module the network did not grant is off."
            AmityModuleEnforcementMode.SHADOW ->
                "Shadow — nothing is withheld. The backend records what enforcing " +
                    "would have done."
            AmityModuleEnforcementMode.OFF ->
                "Off — nothing is withheld. The grants below are recorded but not applied."
        }
        return mode to grantedCount()
    }

    /**
     * "Granted N of M." over `kind: api` only, or null with no answer yet.
     *
     * The grants carry every module the network owns, `kind: setting` ones
     * included — Console and Dashboard capabilities with no UIKit surface — and
     * counting those against a denominator of api modules reads "Granted 18 of 15".
     */
    fun grantedCount(): String? {
        val settings = AmityUIKitConfigController.heldEntitlement() ?: return null
        val apiKeys = settings.catalog.filterValues { it.kind.value == "api" }.keys
        val granted = settings.modules.filterValues { it }.keys.count { it in apiKeys }
        val api = apiKeys.size
        return when (granted) {
            0 -> "Granted none of $api."
            api -> "Granted all $api."
            else -> "Granted $granted of $api."
        }
    }

    /** One line for the entry cards: the grants, then how many rows the gate has off. */
    fun summaryLine(): String {
        val off = order.count { !isAvailable(it.key) }
        val rows = if (off == 0) "all ${order.size} on" else "$off of ${order.size} off"
        val granted = grantedCount()?.removeSuffix(".") ?: "No answer yet"
        return "$granted · $rows"
    }

    /** The api modules this network withholds — empty unless it is enforcing. */
    fun withheldByNetwork(): List<String> {
        val settings = AmityUIKitConfigController.heldEntitlement()
            ?: return emptyList()
        if (settings.enforcement != AmityModuleEnforcementMode.ENFORCE) return emptyList()
        return settings.catalog
            .filter { it.value.kind.value == "api" && settings.modules[it.key] != true }
            .keys.sorted().map { label(it) }
    }

    /** What the backend said about this one module, whatever the gate made of it. */
    fun networkValue(key: String): String {
        val settings = AmityUIKitConfigController.heldEntitlement()
            ?: return "no answer yet"
        if (!settings.catalog.containsKey(key)) {
            return "not in the catalog — the network is never asked about it"
        }
        val value = when (settings.modules[key]) {
            true -> "granted"
            false -> "not granted"
            null -> "not in the response (counts as not granted)"
        }
        // Without this a tester files a bug against a shadow network.
        return if (settings.enforcement == AmityModuleEnforcementMode.ENFORCE) {
            value
        } else {
            "$value · not enforced"
        }
    }

    /** The UIKit's own verdict for one module — the network and the bundle rules. */
    fun verdict(key: String): AmityUIKitModuleAvailability? {
        val feature = AmityUIKitFeature.values().find { it.key == key } ?: return null
        return runCatching { AmityUIKit4Manager.moduleAvailability(feature) }.getOrNull()
    }

    /** Whether the UIKit resolves this module on — the gate's own answer. */
    fun isAvailable(key: String): Boolean = verdict(key)?.isAvailable ?: true

    /** Why a row is off. Null when it is on. */
    fun reasonOf(verdict: AmityUIKitModuleAvailability?): String? = when (verdict) {
        null, is AmityUIKitModuleAvailability.Available -> null
        is AmityUIKitModuleAvailability.NotGranted -> "Off — the network does not grant it"
        is AmityUIKitModuleAvailability.PrerequisiteUnavailable -> unmetText(verdict)
    }

    private fun unmetText(v: AmityUIKitModuleAvailability.PrerequisiteUnavailable): String {
        val names = v.unsatisfied.map { label(it) }
        val requires = AmityUIKitConfigController.moduleRequires(v.feature.key)
        val isAnyOf = requires.isNotEmpty() && requires.map { label(it) } == names
        return when {
            names.isEmpty() -> "Off — its requirements cannot be resolved"
            isAnyOf && names.size == 2 -> "Off — neither ${names[0]} nor ${names[1]} is on"
            isAnyOf -> "Off — none of ${names.joinToString(", ")} is on"
            names.size == 1 -> "Off — ${names[0]} is off"
            names.size == 2 -> "Off — ${names[0]} and ${names[1]} are off"
            else -> "Off — ${names.dropLast(1).joinToString(", ")} and ${names.last()} are off"
        }
    }
}
