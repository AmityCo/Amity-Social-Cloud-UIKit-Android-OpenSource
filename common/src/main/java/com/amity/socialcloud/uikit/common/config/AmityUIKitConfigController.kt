package com.amity.socialcloud.uikit.common.config

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object AmityUIKitConfigController {

    private val GSON = GsonBuilder().create()
    private lateinit var config: AmityUIKitConfig

    fun setup(context: Context) {
        parseConfig(context)
    }

    fun getGlobalTheme(): AmityUIKitConfig.GlobalTheme {
        return config.globalTheme
    }

    fun getCustomizationConfig(configId: String): JsonObject {
        return config.customizations.getAsJsonObject(configId)
    }

    fun getPageTheme(configId: String): AmityUIKitConfig.GlobalTheme? {
        val jsonObject = getCustomizationConfig(configId).get("page_theme")
        val type = object : TypeToken<AmityUIKitConfig.GlobalTheme?>() {}.type

        return GSON.fromJson(jsonObject, type)
    }

    fun getComponentTheme(configId: String): AmityUIKitConfig.GlobalTheme? {
        val jsonObject = getCustomizationConfig(configId).get("component_theme")
        val type = object : TypeToken<AmityUIKitConfig.GlobalTheme?>() {}.type

        return GSON.fromJson(jsonObject, type)
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