package com.amity.socialcloud.uikit.common.config

/**
 * social.plus's own read-outs of the module gate — not a customer API.
 *
 * What the gate holds and why it answered as it did (the held entitlement, the
 * prerequisites it applied, the clip question), for the sample app's read-only
 * module screen and for diagnostics. Nothing behind this can change an answer:
 * the network's plan, read through `getModuleSettings()`, is the only source
 * that withholds a module (module-availability-spec §1), as on Web.
 *
 * TODO(before production release): hide everything carrying this annotation.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "Internal to social.plus: diagnostics for the sample app, hidden before a production release.",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class AmityUIKitInternalApi
