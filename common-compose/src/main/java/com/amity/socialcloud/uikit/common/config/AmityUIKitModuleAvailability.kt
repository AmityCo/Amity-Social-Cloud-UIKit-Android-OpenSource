package com.amity.socialcloud.uikit.common.config

/**
 * Whether a module is available in this app, and when it is not, why.
 *
 * `available`, not `enabled`: a module is withheld by the network's plan, not
 * switched off by anyone at hand, and "enabled" invites the question *enabled
 * by whom* (REQ-021).
 *
 * The reason matters because it names a different module. `NotGranted` is a
 * conversation with an account manager about this one; `PrerequisiteUnavailable`
 * is about another, and naming the wrong one sends the customer to ask for
 * something they already have (REQ-022).
 */
sealed class AmityUIKitModuleAvailability {

    abstract val feature: AmityUIKitFeature
    abstract val isAvailable: Boolean

    data class Available(
        override val feature: AmityUIKitFeature,
    ) : AmityUIKitModuleAvailability() {
        override val isAvailable = true
    }

    /** The network's plan does not include this module, and core is enforcing. */
    data class NotGranted(
        override val feature: AmityUIKitFeature,
    ) : AmityUIKitModuleAvailability() {
        override val isAvailable = false
    }

    /**
     * `unsatisfied` carries the whole requirement, because any one entry would
     * have satisfied it — naming a single module misdirects. Telling a
     * Community-only customer that Comment needs Post hides that Story would
     * also have done.
     *
     * Raw catalog keys, not [AmityUIKitFeature]: a prerequisite can be a key
     * this build's enum does not name, and dropping it would hide the very
     * module that is missing (REQ-023).
     */
    data class PrerequisiteUnavailable(
        override val feature: AmityUIKitFeature,
        val unsatisfied: List<String>,
    ) : AmityUIKitModuleAvailability() {
        override val isAvailable = false
    }
}
