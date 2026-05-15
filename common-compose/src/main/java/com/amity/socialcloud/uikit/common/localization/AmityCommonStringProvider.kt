package com.amity.socialcloud.uikit.common.localization

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface AmityCommonStringProvider {
    fun getString(key: String, vararg args: Any): String
}

class DefaultAmityCommonStringProvider private constructor(
    private val context: Context?,
    private val stringKeyToResId: Map<String, Int>,
) : AmityCommonStringProvider {

    private val overrides = mutableMapOf<String, String>()
    private val localeOverrides = mutableMapOf<String, Map<String, String>>()
    private var activeLocale: String? = null

    constructor(context: Context) : this(
        context = context,
        stringKeyToResId = AmityCommonStrings.buildStringKeyMap(),
    )

    internal constructor(stringKeyToResId: Map<String, Int>) : this(
        context = null,
        stringKeyToResId = stringKeyToResId,
    )

    fun setOverrides(overrides: Map<String, String>) {
        this.overrides.putAll(overrides)
    }

    fun clearOverrides() {
        overrides.clear()
    }

    fun setLocale(locale: String, strings: Map<String, String>) {
        localeOverrides[locale] = strings
        activeLocale = locale
    }

    fun clearLocale() {
        activeLocale = null
    }

    override fun getString(key: String, vararg args: Any): String {
        val raw = overrides[key]
            ?: localeOverrides[activeLocale]?.get(key)
            ?: getAndroidStringResource(key)
        return if (args.isNotEmpty()) String.format(raw, *args) else raw
    }

    private fun getAndroidStringResource(key: String): String {
        val resId = stringKeyToResId[key] ?: return key
        return context?.getString(resId) ?: key
    }

    companion object {
        @Volatile
        private var instance: DefaultAmityCommonStringProvider? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DefaultAmityCommonStringProvider(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): DefaultAmityCommonStringProvider {
            return instance ?: throw IllegalStateException(
                "AmityCommonStringProvider not initialized. Call initialize(context) first."
            )
        }

        fun setOverrides(overrides: Map<String, String>) {
            getInstance().setOverrides(overrides)
        }

        fun setLocale(locale: String, strings: Map<String, String>) {
            getInstance().setLocale(locale, strings)
        }
    }
}

val LocalAmityCommonStringProvider = staticCompositionLocalOf<AmityCommonStringProvider> {
    try {
        DefaultAmityCommonStringProvider.getInstance()
    } catch (_: IllegalStateException) {
        object : AmityCommonStringProvider {
            override fun getString(key: String, vararg args: Any): String = key
        }
    }
}

@Composable
fun amityCommonString(key: String, vararg args: Any): String {
    return LocalAmityCommonStringProvider.current.getString(key, *args)
}

/**
 * Resolves a localized display name for a reaction key.
 *
 * Looks up "amity_common_button_reaction__key_" through the full localization chain.
 * Falls back to title-cased key name for unknown/custom reactions.
 *
 * Third-party developers can provide translations for custom reaction keys
 * by adding "amity_common_button_reaction__key_" entries to their locale bundles.
 */
@Composable
fun amitySocialReactionDisplayName(reactionKey: String): String {
    val stringKey = when(reactionKey) {
        "like" -> "amity_social_button_reaction_like"
        "love" -> "amity_social_button_reaction_love"
        "fire" -> "amity_social_button_reaction_fire"
        "happy" -> "amity_social_button_reaction_happy"
        "sad" -> "amity_social_button_reaction_sad"
        else -> "amity_social_button_reaction___reactionKey_"
    }
    val resolved = LocalAmityCommonStringProvider.current.getString(stringKey)
    // If getString returns the raw key, it means no translation was found — fall back to title-case
    return if (resolved == stringKey) {
        reactionKey.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    } else {
        resolved
    }
}
