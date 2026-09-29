package com.amity.socialcloud.uikit.common.config

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.mutableStateOf
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.sdk.model.core.module.AmityModuleSettings
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

    /**
     * isExcluded answers the same question for the same config id on every
     * recomposition, and measured at 1.7–2.4 microseconds a call on a desktop
     * JVM — several times that on a mid-range device, times every element on a
     * scrolling feed. The answer only changes when the config does, and the
     * config is assigned in exactly one place, so the cache is cleared there.
     */
    private val excludedCache = java.util.concurrent.ConcurrentHashMap<String, Boolean>()

    /**
     * One entry per [AmityUIKitFeature], rebuilt whenever the entitlement changes.
     *
     * The bundle walk happens here rather than per read. Every gated page,
     * component and element asks this question while views are being built, and
     * this UIKit has already measured what recursing per call costs — 2,435ns
     * against 25ns for a map lookup. Since the entitlements now live in the
     * SDK's row rather than in its memory, a per-read walk would also be a
     * database query per gated surface, on the main thread.
     *
     * Behind an atomic reference because it is written from setup and read from
     * the main thread at view-build time; a torn read of a half-rebuilt
     * snapshot would gate inconsistently inside one frame.
     */
    private val moduleSnapshot =
        java.util.concurrent.atomic.AtomicReference<Map<String, AmityUIKitModuleAvailability>>(emptyMap())

    /**
     * What the network was granted, or null when no row has been written for it
     * — a genuine first launch, a log-out, or a network switch. Null resolves
     * every module available with no prerequisite cascade (REQ-012).
     */
    @Volatile
    private var entitlement: AmityModuleSettings? = null

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

    private var entitlementDisposable: Disposable? = null
    private var entitlementSessionDisposable: Disposable? = null
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

    /**
     * Follow the entitlement row, the way every other consumer of a persisted
     * network setting does.
     *
     * `AmitySocialClient.getSettings()` is a Room Flowable and its callers are
     * pushed each write; this is the same read. It matters here because the
     * row is written after `setup()` has returned — a gate that read once at
     * setup holds "no entitlements" for the whole of a first launch.
     *
     * Replaces any previous subscription, because setup() runs again when the
     * network changes.
     *
     * Forgets what it held first, and again on every log-out. The row goes with
     * the store on log-out, and a single-row Room query emits nothing for a row
     * that is absent — so a gate that only listened kept the last network's
     * grants for the next user, until that network's own fetch landed, or for
     * the rest of the process if it never did.
     */
    fun readModuleEntitlements() {
        forgetEntitlement()
        entitlementSessionDisposable?.dispose()
        entitlementSessionDisposable = AmityCoreClient.observeSessionState()
            .distinctUntilChanged()
            // The state at subscription is where the app already is, not a
            // log-out; only a transition into NotLoggedIn is one.
            .skip(1)
            .filter { it == SessionState.NotLoggedIn }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { forgetEntitlement() },
                { /* session stream errors are non-actionable here */ }
            )
    }

    private fun forgetEntitlement() {
        onEntitlement(null)
        // A fresh subscription, not the old one: distinctUntilChanged would
        // swallow the same row written again by a login to the same network.
        entitlementDisposable?.dispose()
        entitlementDisposable = AmityCoreClient.getModuleSettings()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { settings -> onEntitlement(settings) },
                { /* no row, no entitlements: every module resolves available */ }
            )
    }

    private fun onEntitlement(settings: AmityModuleSettings?) {
        entitlement = settings
        rebuildModuleSnapshot()
        // The gate memoises exclusion per config id, and every answer it holds
        // was computed under the old grants — for exactly the ids nobody would
        // think to re-check.
        excludedCache.clear()
        AmityUIKitRealtimeSubscriptions.sync()
        callbacks.values.forEach { it.invoke() }
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

    /**
     * Whether a module is available: granted by the network's plan, with its
     * prerequisites available too (REQ-010, REQ-011).
     *
     * The one question every gated surface asks (REQ-007). Answered from the
     * snapshot, so it is synchronous and touches no store (REQ-019, REQ-027).
     */
    fun isFeatureEnabled(feature: AmityUIKitFeature): Boolean {
        return isFeatureEnabled(feature.key)
    }

    fun isFeatureEnabled(key: String): Boolean {
        moduleSnapshot.get()[key]?.let { return it.isAvailable }
        // A key with no entry is a module this build's enum does not name — an
        // owner map pointing at one the catalog gained server-side. Resolve it
        // directly rather than defaulting; the walk is pure and touches no I/O.
        return verdict(key, entitlement, emptySet()).isAvailable
    }

    fun moduleAvailability(feature: AmityUIKitFeature): AmityUIKitModuleAvailability {
        return moduleSnapshot.get()[feature.key]
            ?: verdict(feature.key, entitlement, emptySet()).toAvailability(feature)
    }

    /**
     * Every module this UIKit gates, for a host rendering a settings screen.
     *
     * `kind: setting` catalog entries are Console and Dashboard capabilities
     * with no UIKit surface; none of them is an [AmityUIKitFeature], so none of
     * them can appear here.
     */
    fun moduleAvailabilities(): List<AmityUIKitModuleAvailability> {
        val held = moduleSnapshot.get()
        return AmityUIKitFeature.values().map { feature ->
            held[feature.key] ?: AmityUIKitModuleAvailability.Available(feature)
        }
    }

    /**
     * Test seam. `config` is assigned in exactly one place in production, and
     * that place also rebuilds the snapshot and clears the memo; a test that
     * poked the field directly got a stale answer from both.
     */
    @VisibleForTesting
    internal fun setConfigForTesting(newConfig: AmityUIKitConfig) {
        config = newConfig
        rebuildModuleSnapshot()
        excludedCache.clear()
    }

    /**
     * Test seam: stands in for the SDK's row, through the same path the row
     * takes. Visible so other modules' tests can fake the network's answer, and
     * behind [AmityUIKitInternalApi] so no host compiles a call to it without an
     * explicit opt-in: a host that could hand the gate its own answer would have
     * the lever this gate no longer has. The network's module settings, read
     * through `getModuleSettings()`, are the only source that withholds a module.
     */
    @VisibleForTesting
    @AmityUIKitInternalApi
    fun setModuleEntitlementForTesting(settings: AmityModuleSettings?) = onEntitlement(settings)

    private fun rebuildModuleSnapshot() {
        // Once, not per key: a snapshot whose entries were resolved against two
        // different payloads is a snapshot nobody can reason about.
        val entitlement = this.entitlement
        moduleSnapshot.set(
            AmityUIKitFeature.values().associate { feature ->
                feature.key to verdict(feature.key, entitlement, emptySet())
                    .toAvailability(feature)
            }
        )
    }

    /**
     * The network's plan is the one source that withholds a module (§1).
     *
     * One walk, over the resolved answer: the entitlement contributes the leaf
     * — is this module granted, given the enforcement mode — and the chain is
     * walked here, because the id tables it has to act on are the UIKit's
     * (REQ-011). A module granted but held off by its own prerequisite cannot
     * satisfy a dependent either.
     */
    private fun verdict(
        key: String,
        entitlement: AmityModuleSettings?,
        visiting: Set<String>,
    ): Verdict {
        // A cycle reads as unavailable rather than recursing, and still names
        // the whole requirement — an empty reason reads as "no prerequisites",
        // the opposite of what a cycle means.
        if (key in visiting) return Verdict.PrerequisiteUnavailable(requiresOf(key, entitlement))
        // The grant before the chain, so a revoked module is reported as itself
        // and not through a prerequisite that fell with it (REQ-010).
        if (!isModuleGranted(key, entitlement)) return Verdict.NotGranted

        // Any one entry satisfies it: the catalog's `requires` is any-of (REQ-017).
        val requires = requiresOf(key, entitlement)
        val seen = visiting + key
        if (requires.isNotEmpty() && requires.none { verdict(it, entitlement, seen).isAvailable }) {
            // The whole list: any one of them would have done (REQ-023).
            return Verdict.PrerequisiteUnavailable(requires)
        }
        return Verdict.Available
    }

    /**
     * The prerequisites, from the network's catalog and nowhere else (§3.3).
     *
     * No payload means no prerequisites (REQ-012): a cascade the network has not
     * stated is one the client has no authority to invent. `features.json` keeps
     * its own table for apollo, and no client reads it.
     */
    private fun requiresOf(key: String, entitlement: AmityModuleSettings?): List<String> =
        entitlement?.catalog?.get(key)?.requires ?: emptyList()

    /**
     * Why a module resolved the way it did, in keys rather than features: a
     * prerequisite can be a catalog key this build's enum does not name.
     */
    private sealed class Verdict {
        object Available : Verdict()
        object NotGranted : Verdict()
        data class PrerequisiteUnavailable(val unsatisfied: List<String>) : Verdict()

        val isAvailable: Boolean get() = this is Available

        fun toAvailability(feature: AmityUIKitFeature): AmityUIKitModuleAvailability =
            when (this) {
                is Available -> AmityUIKitModuleAvailability.Available(feature)
                is NotGranted -> AmityUIKitModuleAvailability.NotGranted(feature)
                // Raw keys, carried not filtered: dropping the ones this build's
                // enum lacks would hide the very prerequisite that is missing.
                is PrerequisiteUnavailable ->
                    AmityUIKitModuleAvailability.PrerequisiteUnavailable(feature, unsatisfied)
            }
    }

    /**
     * The entitlement the gate is holding — diagnostics only, never a gate.
     *
     * Nothing may decide availability from this (REQ-007); that is
     * [isFeatureEnabled]'s job. It exists so a debug screen can say why the gate
     * answered as it did. Not a fresh read from the SDK: on the launch that
     * first fetches a payload the two differ, and a screen reporting the fresh
     * one showed twelve modules withheld above rows that were all on.
     */
    @AmityUIKitInternalApi
    fun heldEntitlement(): AmityModuleSettings? = entitlement

    /** The prerequisites the gate itself applied for a module — catalog only. */
    @AmityUIKitInternalApi
    fun moduleRequires(key: String): List<String> = requiresOf(key, entitlement)

    /**
     * Clip is part of Post, so Post's availability governs it (§6.2), as on Web.
     * Never looked up in the grants — the network is never asked to grant clip —
     * and in no public API. Kept as its own question because clip queries ask it
     * by name ([AmityUIKitDataGate.isClipOn]).
     */
    @AmityUIKitInternalApi
    fun isClipEnabled(): Boolean = isFeatureEnabled(AmityUIKitFeature.POST)

    /**
     * The leaf grant: is this module granted, given what the network is under.
     *
     * Nothing held means no row has been written for this network — a genuine
     * first launch, a log-out, a network switch — and that reads as granted: the
     * gate is asked while views are being built, long before a round trip can
     * finish, and failing closed would blank paid surfaces on every cold start.
     * Under `enforce` the backend refuses the calls anyway, so the exposure is a
     * revoked module that is visible but not usable.
     */
    private fun isModuleGranted(key: String, entitlement: AmityModuleSettings?): Boolean {
        val settings = entitlement ?: return true
        // `off` and `shadow` both record the grants and withhold nothing;
        // shadow is a dry run that core logs server-side.
        if (settings.enforcement != AmityModuleEnforcementMode.ENFORCE) return true
        // A key the catalog does not carry is not an entitlement: the network is
        // never asked to grant it, so an absent grant says nothing about it.
        if (!settings.catalog.containsKey(key)) return true
        return settings.modules[key] == true
    }

    /**
     * A configId is always "pageId/componentId/elementId", and every page,
     * component and element in the UIKit resolves through here. So a withheld
     * module excludes the pages it owns — and with them everything nested under
     * those pages — by the route the customer's own `excludes` list already
     * takes. No per-page guard to add, and none to forget when the next page
     * lands.
     */
    fun isExcluded(configId: String): Boolean =
        excludedCache.getOrPut(configId) { computeExcluded(configId) }

    @OptIn(AmityUIKitInternalApi::class)
    private fun computeExcluded(configId: String): Boolean {
        // Both, not the first that matches: a component can belong to a different
        // module than the page hosting it — Chat's feed renders inside Live's
        // player — and falling back only when the page is unowned left every such
        // component ungated.
        // All three segments. Reading only the first two emptied a module's
        // pages and left every door into them standing — the Clips tab, the
        // Create Story button and the follow button are elements, and they sit
        // on pages owned by other modules.
        val id = configId.split('/')
        val owners = listOfNotNull(
            AMITY_PAGE_MODULE[id.getOrNull(0)],
            AMITY_COMPONENT_MODULE[id.getOrNull(1)],
            AMITY_ELEMENT_MODULE[id.getOrNull(2)],
        )
        if (owners.any { !isFeatureEnabled(it) }) {
            return true
        }
        if (!::config.isInitialized) return false
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

    /**
     * Whether a 1-on-1 chat user action is shown: its config switch, and for
     * Block its module too. Blocking is a user relationship, so withholding
     * `userRelationship` removes the row whichever label it carries; Report is
     * moderation and never reads the module (chat user action REQ-008, REQ-008a).
     */
    fun isChatUserActionAvailable(actionName: String): Boolean {
        if (!isConversationUserActionEnabled(actionName)) return false
        return actionName != "block" || isFeatureEnabled(AmityUIKitFeature.USER_RELATIONSHIP)
    }

    fun hasAnyEnabledChatUserAction(): Boolean {
        return listOf("mute", "report", "block").any { isChatUserActionAvailable(it) }
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

    @VisibleForTesting
    internal fun parseConfig(context: Context) {
        val configStr = readConfigFromAssets(context)
        val type = object : TypeToken<AmityUIKitConfig>() {}.type
        config = GSON.fromJson(configStr, type)
        // The config is assigned in exactly one place, so the snapshot and the
        // exclusion memo are rebuilt in exactly one place.
        rebuildModuleSnapshot()
        excludedCache.clear()
        // The realtime handles are reconciled on every parse as well as on every
        // entitlement change, so a setup() that runs again cannot leave a
        // withheld module's subscriptions open.
        AmityUIKitRealtimeSubscriptions.sync()
        AmityUIKitRealtimeSubscriptions.watchSession()
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