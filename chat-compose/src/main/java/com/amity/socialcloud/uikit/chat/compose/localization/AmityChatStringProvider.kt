package com.amity.socialcloud.uikit.chat.compose.localization

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface AmityChatStringProvider {
    fun getString(key: String, vararg args: Any): String
}

class DefaultAmityChatStringProvider private constructor(
    private val context: Context?,
    private val stringKeyToResId: Map<String, Int>,
) : AmityChatStringProvider {

    private val overrides = mutableMapOf<String, String>()
    private val localeOverrides = mutableMapOf<String, Map<String, String>>()
    private var activeLocale: String? = null

    constructor(context: Context) : this(
        context = context,
        stringKeyToResId = AmityChatStrings.buildStringKeyMap(),
    )

    // Test-only constructor
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
        private var instance: DefaultAmityChatStringProvider? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DefaultAmityChatStringProvider(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): DefaultAmityChatStringProvider {
            return instance ?: throw IllegalStateException(
                "AmityChatStringProvider not initialized. Call initialize(context) first."
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

val LocalAmityChatStringProvider = staticCompositionLocalOf<AmityChatStringProvider> {
    try {
        DefaultAmityChatStringProvider.getInstance()
    } catch (_: IllegalStateException) {
        object : AmityChatStringProvider {
            override fun getString(key: String, vararg args: Any): String = key
        }
    }
}

@Composable
fun amityChatString(key: String, vararg args: Any): String {
    return LocalAmityChatStringProvider.current.getString(key, *args)
}
