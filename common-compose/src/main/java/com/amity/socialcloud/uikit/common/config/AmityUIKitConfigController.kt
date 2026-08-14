package com.amity.socialcloud.uikit.common.config

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.shareablelink.AmityShareableLinkConfiguration
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.model.AmityReactionType
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.networkconfig.AmityNetworkConfigService
import com.amity.socialcloud.uikit.common.ui.theme.AmityTokenResolver
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

object AmityUIKitConfigController {

    private val GSON = GsonBuilder().create()
    private lateinit var config: AmityUIKitConfig

    private val uiKitTheme: AmityUIKitTheme by lazy {
        AmityUIKitTheme.enumOf(config.preferredTheme)
    }

    private var isSystemInDarkTheme = false

    // New design-token system: SDK-vendored token table + the effective (backfilled) theme
    // config to resolve semantic tokens against. Built once at setup(); null until then.
    @Volatile
    private var tokenTable: AmityTokenResolver.Table? = null

    @Volatile
    private var effectiveTokenConfig: AmityTokenResolver.Config? = null

    private var callbacks = mutableMapOf<String,() -> Unit>()

    // Compose snapshot state: composables that read the link pattern (via getPostLink /
    // getCommunityLink / getUserLink) recompose when the async fetch lands.
    private val _shareableLinkPattern = mutableStateOf<AmityShareableLinkConfiguration?>(null)

    var shareableLinkPattern: AmityShareableLinkConfiguration?
        get() = _shareableLinkPattern.value
        set(value) {
            _shareableLinkPattern.value = value
        }

    private var shareableLinkSessionDisposable: Disposable? = null
    private var shareableLinkFetchDisposable: Disposable? = null

    fun initializeShareableLinkPattern() {
        // setup() can be called again (e.g. switching networks); replace any previous subscription
        shareableLinkSessionDisposable?.dispose()
        shareableLinkSessionDisposable = AmityCoreClient.observeSessionState()
            .distinctUntilChanged()
            .filter { it == SessionState.Established }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { fetchShareableLinkConfig() },
                { /* ignored: session stream errors are non-actionable here */ }
            )
    }

    private fun fetchShareableLinkConfig() {
        // SDK 7.23.0-alpha03: getShareableLinkConfiguration() returns the configuration
        // Single directly (the AmityShareableLink intermediate step is deprecated).
        shareableLinkFetchDisposable?.dispose()
        shareableLinkFetchDisposable = AmityCoreClient.getShareableLinkConfiguration()
            .retry(3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { shareableLink -> shareableLinkPattern = shareableLink },
                { /* keep the last known pattern; the next session establishment refetches */ }
            )
    }

    fun registerChangeCallback(id: String, callback: () -> Unit) {
        callbacks[id] = callback
    }

    fun unregisterChangeCallback(id: String) {
        callbacks.remove(id)
    }

    fun setup(context: Context) {
        DefaultAmityCommonStringProvider.initialize(context)
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

    fun isConversationUserActionEnabled(actionName: String): Boolean {
        if (!::config.isInitialized) return true
         val actions = config.featureFlags.chat.conversationChatUserActions
        for (i in 0 until actions.size()) {
            val action = actions[i] as? JsonObject ?: continue
            val name = action.get("name")?.asString ?: continue
            if (name == actionName) {
                return action.get("enabled")?.asBoolean ?: true
            }
        }
        return true // default: enabled if not listed
    }

    fun hasAnyEnabledChatUserAction(): Boolean {
        return listOf("mute", "report", "block").any { isConversationUserActionEnabled(it) }
    }

    fun getEnabledChannelTypes(): List<String> {
        if (!::config.isInitialized) return listOf("conversation", "community")
        val known = setOf("conversation", "community")
        val types = config.featureFlags.chat.enabledChannelTypes
            .filter { it in known }
        return types.ifEmpty { listOf("conversation", "community") }
    }

    fun getConversationChatUserActions(): List<Pair<String, Boolean>> {
        if (!::config.isInitialized) return emptyList()
        val result = mutableListOf<Pair<String, Boolean>>()
        val actions = config.featureFlags.chat.conversationChatUserActions
        for (i in 0 until actions.size()) {
            val action = actions[i] as? JsonObject ?: continue
            val name = action.get("name")?.asString ?: continue
            val enabled = action.get("enabled")?.asBoolean ?: true
            result.add(name to enabled)
        }
        return result
    }

    private fun parseConfig(context: Context) {
        val configStr = readConfigFromAssets(context)
        val type = object : TypeToken<AmityUIKitConfig>() {}.type
        config = GSON.fromJson(configStr, type)
        var networkJson: JsonObject? = null
        try {
            val cachedConfig = AmityNetworkConfigService.getNetworkConfig()?.config
            if (cachedConfig != null) {
                networkJson = when (cachedConfig) {
                    is JsonObject -> cachedConfig
                    else -> runCatching {
                        GSON.fromJson(cachedConfig.toString(), JsonObject::class.java)
                    }.getOrNull()
                }
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
        try {
            buildTokenSystem(context, configStr, networkJson)
        } catch (e: Exception) {
            Log.d("UIKitConfig", "Error building token system: ${e.message}")
        }
    }

    /**
     * Build the design-token system: load the SDK-vendored token table, then layer the runtime
     * config on top — the bundled config.json theme, then the network config theme (network wins
     * per key). config.json ships the complete colors-v2 palette and is the single source of theme
     * values; a customer that wholesale-replaces it owns supplying the full palette.
     */
    private fun buildTokenSystem(context: Context, localConfigStr: String, networkJson: JsonObject?) {
        val tableJson = GSON.fromJson(readAsset(context, "amity-uikit-design-tokens.json"), JsonObject::class.java)
        tokenTable = parseTokenTable(tableJson)

        val localCfg = extractTokenConfig(GSON.fromJson(localConfigStr, JsonObject::class.java))
        val networkCfg = networkJson?.let { extractTokenConfig(it) }

        val customerTheme = HashMap<String, Map<String, String>>()
        for (mode in listOf("light", "dark")) {
            val merged = LinkedHashMap<String, String>()
            localCfg.theme[mode]?.let { merged.putAll(it) }
            networkCfg?.theme?.get(mode)?.let { merged.putAll(it) } // network wins
            if (merged.isNotEmpty()) customerTheme[mode] = merged
        }
        val customerCustomizations = HashMap<String, Map<String, Map<String, String>>>()
        customerCustomizations.putAll(localCfg.customizations)
        networkCfg?.customizations?.let { customerCustomizations.putAll(it) }

        effectiveTokenConfig = AmityTokenResolver.Config(customerTheme, customerCustomizations)
    }

    private fun parseTokenTable(root: JsonObject): AmityTokenResolver.Table {
        val alias = LinkedHashMap<String, String>()
        root.getAsJsonObject("alias")?.entrySet()?.forEach { (k, el) ->
            if (el.isJsonPrimitive) alias[k] = el.asString
        }
        val semantic = LinkedHashMap<String, Map<String, String>>()
        root.getAsJsonObject("semantic")?.entrySet()?.forEach { (path, el) ->
            (el as? JsonObject)?.let { semantic[path] = jsonObjectToStringMap(it) }
        }
        return AmityTokenResolver.Table(alias, semantic)
    }

    private fun extractTokenConfig(root: JsonObject): AmityTokenResolver.Config {
        val theme = LinkedHashMap<String, Map<String, String>>()
        root.getAsJsonObject("theme")?.let { themeObj ->
            for (mode in listOf("light", "dark")) {
                themeObj.getAsJsonObject(mode)?.let { theme[mode] = jsonObjectToStringMap(it) }
            }
        }
        val customizations = LinkedHashMap<String, Map<String, Map<String, String>>>()
        root.getAsJsonObject("customizations")?.entrySet()?.forEach { (scopeId, el) ->
            val themeBlock = (el as? JsonObject)?.getAsJsonObject("theme") ?: return@forEach
            val modeMap = LinkedHashMap<String, Map<String, String>>()
            for (mode in listOf("light", "dark")) {
                themeBlock.getAsJsonObject(mode)?.let { modeMap[mode] = jsonObjectToStringMap(it) }
            }
            if (modeMap.isNotEmpty()) customizations[scopeId] = modeMap
        }
        return AmityTokenResolver.Config(theme, customizations)
    }

    private fun jsonObjectToStringMap(obj: JsonObject): Map<String, String> {
        val map = LinkedHashMap<String, String>()
        obj.entrySet().forEach { (k, el) ->
            if (el.isJsonPrimitive && el.asJsonPrimitive.isString) map[k] = el.asString
        }
        return map
    }

    /**
     * Resolve a semantic token path to a hex string against the effective theme + vendored table.
     * Returns [AmityTokenResolver.MISSING_COLOR] if the token is unknown or the system is not yet
     * initialized. scopeId is "page/component/element"; mode is "light" | "dark".
     */
    fun resolveToken(scopeId: String, mode: String, tokenPath: String): AmityTokenResolver.Resolved {
        val table = tokenTable
        val cfg = effectiveTokenConfig
        if (table == null || cfg == null) {
            return AmityTokenResolver.Resolved(AmityTokenResolver.MISSING_COLOR, "missing")
        }
        return AmityTokenResolver.resolveToken(cfg, table, scopeId, mode, tokenPath)
    }

    private fun readAsset(context: Context, name: String): String {
        return try {
            context.assets.open(name).use { input ->
                input.readBytes().toString(Charsets.UTF_8)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
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

    fun getClipFeatureFlags() : JsonObject {
        return config.featureFlags.post.clip
    }
}