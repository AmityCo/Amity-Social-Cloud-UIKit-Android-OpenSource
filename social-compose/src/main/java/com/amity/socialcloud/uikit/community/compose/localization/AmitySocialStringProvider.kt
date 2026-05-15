package com.amity.socialcloud.uikit.community.compose.localization

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

interface AmitySocialStringProvider {
    fun getString(key: String, vararg args: Any): String
}

class DefaultAmitySocialStringProvider private constructor(
    private val context: Context?,
    private val stringKeyToResId: Map<String, Int>,
) : AmitySocialStringProvider {

    private val overrides = mutableMapOf<String, String>()
    private val localeOverrides = mutableMapOf<String, Map<String, String>>()
    private var activeLocale: String? = null

    constructor(context: Context) : this(
        context = context,
        stringKeyToResId = AmitySocialStrings.buildStringKeyMap(),
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
        private var instance: DefaultAmitySocialStringProvider? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DefaultAmitySocialStringProvider(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): DefaultAmitySocialStringProvider {
            return instance ?: throw IllegalStateException(
                "AmitySocialStringProvider not initialized. Call initialize(context) first."
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

val LocalAmitySocialStringProvider = staticCompositionLocalOf<AmitySocialStringProvider> {
    try {
        DefaultAmitySocialStringProvider.getInstance()
    } catch (_: IllegalStateException) {
        object : AmitySocialStringProvider {
            override fun getString(key: String, vararg args: Any): String = key
        }
    }
}

@Composable
fun amitySocialString(key: String, vararg args: Any): String {
    return LocalAmitySocialStringProvider.current.getString(key, *args)
}
