package com.amity.socialcloud.uikit.common.ui.theme

import kotlin.math.roundToInt

/**
 * AmityTokenResolver — resolves a semantic color token to a hex value.
 *
 * Pure JVM (no Android / Gson imports) so it unit-tests without Robolectric. The Android
 * layer converts the bundled JSON artifacts into the plain-map inputs below and calls
 * [resolveToken] / [backfillThemeDefaults].
 *
 * Resolution chain: semantic cell -> literal hex, or "{Alias}" -> alias table ->
 * "{theme.<key>}" -> theme-key lookup through the scope cascade (exact scope,
 * wildcard-component, wildcard-page, global), then an optional "@alpha:<0..1>" modifier
 * (a semantic-level alpha wins over an alias-level one). Both JSON artifacts are
 * machine-generated, so anything that doesn't match these shapes resolves to
 * [MISSING_COLOR] — the visible magenta is the debug signal for a broken token.
 */
object AmityTokenResolver {

    const val MISSING_COLOR = "#FF00FF"

    /** SDK-vendored table artifact: amity-uikit-design-tokens.json. */
    data class Table(
        /** aliasName -> "{theme.<key>}(@alpha:x)?" */
        val alias: Map<String, String>,
        /** tokenPath -> mode -> cell ("{Alias}(@alpha:x)?" or literal hex) */
        val semantic: Map<String, Map<String, String>>,
    )

    /**
     * The effective config to resolve against.
     * @param theme mode ("light"|"dark") -> themeKey -> hex
     * @param customizations scopeId ("page/component/element") -> mode -> themeKey -> hex
     */
    data class Config(
        val theme: Map<String, Map<String, String>>,
        val customizations: Map<String, Map<String, Map<String, String>>>,
    )

    data class Resolved(val value: String, val source: String)

    private fun isHex(value: String?): Boolean =
        value != null && (value.length == 7 || value.length == 9) &&
            value[0] == '#' && value.drop(1).all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }

    /** "{Name}" from "{Name}@alpha:x", or null when the shape doesn't match. */
    private fun refName(cell: String): String? =
        if (cell.startsWith("{") && cell.contains("}")) cell.substringAfter("{").substringBefore("}")
        else null

    /** The "@alpha:x" suffix as a Double, or null when absent/unparseable. */
    private fun refAlpha(cell: String): Double? =
        cell.substringAfter("@alpha:", "").toDoubleOrNull()

    /** Append the alpha byte as #RRGGBBAA (overwriting any existing alpha channel). */
    private fun applyAlpha(hex: String, alpha: Double?): String =
        if (alpha == null) hex
        else hex.take(7) + "%02X".format((alpha.coerceIn(0.0, 1.0) * 255).roundToInt())

    /**
     * Ordered cascade candidates, most-specific first:
     * exact scope, then wildcard-component, then wildcard-page, then null (global).
     */
    private fun cascadeCandidates(scopeId: String?): List<String?> {
        val parts = (scopeId ?: "").split("/")
        val page = parts.getOrNull(0)?.ifEmpty { "*" } ?: "*"
        val component = parts.getOrNull(1)?.ifEmpty { "*" } ?: "*"
        val element = parts.getOrNull(2)?.ifEmpty { "*" } ?: "*"
        val exact = "$page/$component/$element"
        val byComponent = "*/$component/*"
        val byPage = "$page/*/*"
        val candidates = mutableListOf<String?>(exact)
        if (byComponent != exact) candidates.add(byComponent)
        if (byPage != exact && byPage != byComponent) candidates.add(byPage)
        candidates.add(null) // global
        return candidates
    }

    /**
     * Walk the cascade candidates for [key], reading customizations[candidate].theme[mode][key]
     * (or config.theme[mode][key] for the global/null candidate). A value present but failing
     * the hex check is skipped, falling through to the next cascade level.
     */
    fun resolveThemeKey(config: Config, scopeId: String?, mode: String, key: String): Resolved {
        for (cId in cascadeCandidates(scopeId)) {
            val themeBlock = if (cId == null) config.theme[mode] else config.customizations[cId]?.get(mode)
            val v = themeBlock?.get(key) ?: continue
            if (isHex(v)) {
                val source = if (cId == null) "theme:$key@global" else "theme:$key@scope:$cId"
                return Resolved(v.uppercase(), source)
            }
        }
        return Resolved(MISSING_COLOR, "missing")
    }

    /** The semantic/alias/theme chain — see the file header. */
    fun resolveToken(
        config: Config,
        table: Table,
        scopeId: String?,
        mode: String,
        tokenPath: String,
    ): Resolved {
        val cell = table.semantic[tokenPath]?.get(mode)
            ?: return Resolved(MISSING_COLOR, "missing")

        if (isHex(cell)) return Resolved(cell.uppercase(), "literal")

        val aliasName = refName(cell) ?: return Resolved(MISSING_COLOR, "missing")
        val aliasTarget = table.alias[aliasName] ?: return Resolved(MISSING_COLOR, "missing")
        val themeKey = refName(aliasTarget)?.takeIf { it.startsWith("theme.") }?.removePrefix("theme.")
            ?: return Resolved(MISSING_COLOR, "missing")

        val resolved = resolveThemeKey(config, scopeId, mode, themeKey)
        if (resolved.source == "missing") return resolved

        val alpha = refAlpha(cell) ?: refAlpha(aliasTarget) // semantic wins over alias
        return Resolved(applyAlpha(resolved.value, alpha).uppercase(), resolved.source)
    }

    /**
     * Build the EFFECTIVE config to resolve against. A customer's injected config REPLACES the
     * SDK's bundled config wholesale, so it can be missing some theme keys. This fills any global
     * theme key absent from [config] with the value from [defaults] (the SDK's bundled default),
     * per mode. Customer-set values always win; `customizations` is left untouched. Pure — returns
     * a new object, mutates nothing.
     */
    fun backfillThemeDefaults(config: Config, defaults: Config): Config {
        val out = HashMap<String, Map<String, String>>()
        val modes = defaults.theme.keys + config.theme.keys
        for (mode in modes) {
            val merged = LinkedHashMap<String, String>()
            defaults.theme[mode]?.let { merged.putAll(it) }
            config.theme[mode]?.let { merged.putAll(it) } // customer wins
            out[mode] = merged
        }
        return Config(theme = out, customizations = config.customizations)
    }
}
