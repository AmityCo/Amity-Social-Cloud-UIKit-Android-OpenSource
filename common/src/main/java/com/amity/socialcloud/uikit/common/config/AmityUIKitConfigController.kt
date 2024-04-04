package com.amity.socialcloud.uikit.common.config

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object AmityUIKitConfigController {

    private val GSON = GsonBuilder().create()
    private lateinit var config: AmityUIKitConfig

    private val uiKitTheme: AmityUIKitTheme by lazy {
        AmityUIKitTheme.enumOf(config.preferredTheme)
    }

    private var isSystemInDarkTheme = false

    fun setup(context: Context) {
        parseConfig(context)
    }

    fun setSystemInDarkTheme(isDarkTheme: Boolean) {
        isSystemInDarkTheme = isDarkTheme
    }

    fun getGlobalTheme(): AmityUIKitConfig.UIKitTheme {
        val global = config.globalTheme
        return when (uiKitTheme) {
            AmityUIKitTheme.DARK -> global.darkTheme
            AmityUIKitTheme.LIGHT -> global.lightTheme
            AmityUIKitTheme.DEFAULT -> if (isSystemInDarkTheme) global.darkTheme else global.lightTheme
        }
    }

    fun getCustomizationConfig(configId: String): JsonObject {
        return config.customizations.getAsJsonObject(configId)
    }

    fun getTheme(configId: String): AmityUIKitConfig.UIKitTheme? {
        val jsonObject = getCustomizationConfig(configId).get("theme")
        val type = object : TypeToken<AmityUIKitConfig.GlobalTheme?>() {}.type

        val theme = GSON.fromJson<AmityUIKitConfig.GlobalTheme>(jsonObject, type)
        return when (uiKitTheme) {
            AmityUIKitTheme.DARK -> theme.darkTheme
            AmityUIKitTheme.LIGHT -> theme.lightTheme
            AmityUIKitTheme.DEFAULT -> if (isSystemInDarkTheme) theme.darkTheme else theme.lightTheme
        }
    }

    fun isExcluded(configId: String): Boolean {
        return config.excludes.find { it.asString == configId } != null
    }

    private fun parseConfig(context: Context) {
        val configStr = readConfigFromAssets(context)
        val type = object : TypeToken<AmityUIKitConfig>() {}.type

        config = GSON.fromJson(configStr, type)
    }


    private fun readConfigFromAssets(context: Context): String {
        val assetManager = context.assets

        return try {
            val inputStream = assetManager.open("config.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}