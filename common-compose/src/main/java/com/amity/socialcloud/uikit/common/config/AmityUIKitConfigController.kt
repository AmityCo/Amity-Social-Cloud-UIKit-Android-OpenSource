package com.amity.socialcloud.uikit.common.config

import android.content.Context
import android.util.Log
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.model.AmityReactionType
import com.amity.socialcloud.uikit.common.networkconfig.AmityNetworkConfigService
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

    private var callbacks = mutableMapOf<String,() -> Unit>()

    fun registerChangeCallback(id: String, callback: () -> Unit) {
        callbacks[id] = callback
    }

    fun unregisterChangeCallback(id: String) {
        callbacks.remove(id)
    }

    fun setup(context: Context) {
        parseConfig(context)
        initMessageReactions()
        callbacks.values.forEach {
            it.invoke()
        }
    }

    fun setSystemInDarkTheme(isDarkTheme: Boolean) {
        isSystemInDarkTheme = isDarkTheme
    }

    fun shouldUIKitInDarkTheme(): Boolean {
        return when (uiKitTheme) {
            AmityUIKitTheme.DARK -> true
            AmityUIKitTheme.LIGHT -> false
            AmityUIKitTheme.DEFAULT -> isSystemInDarkTheme
        }
    }

    fun getGlobalTheme(): AmityUIKitConfig.UIKitTheme {
        val global = config.globalTheme
        return if (shouldUIKitInDarkTheme()) {
            global.darkTheme
        } else {
            global.lightTheme
        }
    }

    fun getCustomizationConfig(configId: String): JsonObject {
        return config.customizations.getAsJsonObject(configId) ?: JsonObject()
    }

    fun getTheme(configId: String): AmityUIKitConfig.UIKitTheme? {
        val jsonObject = getCustomizationConfig(configId).get("theme")
        val type = object : TypeToken<AmityUIKitConfig.GlobalTheme?>() {}.type

        val theme = GSON.fromJson<AmityUIKitConfig.GlobalTheme>(jsonObject, type)
        return if (shouldUIKitInDarkTheme()) {
            theme.darkTheme
        } else {
            theme.lightTheme
        }
    }

    fun isExcluded(configId: String): Boolean {
        return config.excludes.find { it.asString == configId } != null
    }

    private fun parseConfig(context: Context) {
        val configStr = readConfigFromAssets(context)
        val type = object : TypeToken<AmityUIKitConfig>() {}.type
        config = GSON.fromJson(configStr, type)
        try {
            val cachedConfig = AmityNetworkConfigService.getNetworkConfig()?.config
            if (cachedConfig != null) {
                val networkConfigString = cachedConfig.toString()
                val networkConfig: AmityUIKitConfig = GSON.fromJson(networkConfigString, type)
                config.preferredTheme = networkConfig.preferredTheme
                config.globalTheme.lightTheme = networkConfig.globalTheme.lightTheme
                config.globalTheme.darkTheme = networkConfig.globalTheme.darkTheme
            } else {
                Log.d("UIKitConfig", "No network config, rely on configuratio file")
            }
        } catch (e: Exception) {
            Log.d("UIKitConfig", "Error parsing network config: ${e.message}")
        }
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

    private fun initMessageReactions() {
        config.messageReactions.map {
            (it as? JsonObject)?.let { reaction: JsonObject ->
                val name = reaction.get("name")?.asString
                val image = reaction.get("image")?.asString
                if (name != null && image != null) {
                    AmityMessageReactions.addReaction(AmityReactionType(name,
                        AmityUIKitDrawableResolver.getDrawableRes(image)
                    ))
                }
            }
        }
    }
}