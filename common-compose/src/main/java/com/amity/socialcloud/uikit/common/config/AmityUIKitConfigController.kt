package com.amity.socialcloud.uikit.common.config

import android.content.Context
import android.util.Log
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.shareablelink.AmityShareableLinkConfiguration
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.model.AmityReactionType
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.networkconfig.AmityNetworkConfigService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

object AmityUIKitConfigController {

    private val GSON = GsonBuilder().create()
    private lateinit var config: AmityUIKitConfig

    private val uiKitTheme: AmityUIKitTheme by lazy {
        AmityUIKitTheme.enumOf(config.preferredTheme)
    }

    private var isSystemInDarkTheme = false

    private var callbacks = mutableMapOf<String,() -> Unit>()

    @Volatile
    private var _shareableLinkPattern: AmityShareableLinkConfiguration? = null

    var shareableLinkPattern: AmityShareableLinkConfiguration?
        get() = _shareableLinkPattern
        set(value) {
            _shareableLinkPattern = value
        }

    fun initializeShareableLinkPattern() {
        AmityCoreClient.observeSessionState()
            .filter { it == SessionState.Established }
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (_shareableLinkPattern == null) {
                    fetchShareableLinkConfig()
                }
            }
            .doOnError {

            }
            .subscribe()
    }

    private fun fetchShareableLinkConfig() {
        AmityCoreClient.getShareableLinkConfiguration()
            .getShareableLink()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error ->
                // Handle the error gracefully instead of crashing
                Log.d("link pattern", "error: $error")
                // Set to null or a default configuration
                _shareableLinkPattern = null
            }
            .doOnSuccess { shareableLink ->
                //println("Shareable link pattern api calling success $shareableLink")
                _shareableLinkPattern = shareableLink
            }
            .subscribe()
    }

    fun registerChangeCallback(id: String, callback: () -> Unit) {
        callbacks[id] = callback
    }

    fun unregisterChangeCallback(id: String) {
        callbacks.remove(id)
    }

    fun setup(context: Context) {
        parseConfig(context)
        initReactions()
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
                val networkConfig: AmityUIKitConfig? = GSON.fromJson(networkConfigString, type)
                config.preferredTheme = networkConfig?.preferredTheme ?: config.preferredTheme
                networkConfig?.globalTheme?.lightTheme?.let {
                    config.globalTheme.lightTheme = it
                }
                networkConfig?.globalTheme?.darkTheme?.let {
                    config.globalTheme.darkTheme = it
                }
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

    private fun initReactions() {
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
        config.socialReactions.map {
            (it as? JsonObject)?.let { reaction: JsonObject ->
                val name = reaction.get("name")?.asString
                val image = reaction.get("image")?.asString
                if (name != null && image != null) {
                    AmitySocialReactions.addReaction(AmityReactionType(name,
                        AmityUIKitDrawableResolver.getDrawableRes(image)
                    ))
                }
            }
        }
    }

    fun getPostLink(post: AmityPost) : String {
        val domain = shareableLinkPattern?.getDomain()
        val pattern = shareableLinkPattern?.getPatterns()?.get("posts")
        val postLink = if (!domain.isNullOrBlank() && !pattern.isNullOrBlank()) {
            val finalPattern = pattern.replace("{postId}", post.getPostId())
            "$domain$finalPattern"
        } else { "" }
        return postLink
    }

    fun getCommunityLink(community: AmityCommunity) : String {
        val domain = shareableLinkPattern?.getDomain()
        val pattern = shareableLinkPattern?.getPatterns()?.get("communities")
        val communityLink = if (!domain.isNullOrBlank() && !pattern.isNullOrBlank()) {
            val finalPattern = pattern.replace("{communityId}", community.getCommunityId())
            "$domain$finalPattern"
        } else { "" }
        return communityLink
    }

    fun getUserLink(user: AmityUser) : String {
        val domain = shareableLinkPattern?.getDomain()
        val pattern = shareableLinkPattern?.getPatterns()?.get("users")
        val userLink = if (!domain.isNullOrBlank() && !pattern.isNullOrBlank()) {
            val finalPattern = pattern.replace("{userId}", user.getUserId())
            "$domain$finalPattern"
        } else { "" }
        return userLink
    }

}