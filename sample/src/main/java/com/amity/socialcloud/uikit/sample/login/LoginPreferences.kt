package com.amity.socialcloud.uikit.sample.login

import android.content.Context
import android.content.SharedPreferences
import com.amity.socialcloud.uikit.sample.env.SampleAPIKey
import com.amity.socialcloud.uikit.sample.env.SampleUploadUrl

object LoginPreferences {

    private const val PREF_NAME = "login_uikit_preferences"

    private const val KEY_USER_ID = "user_id"
    private const val KEY_DISPLAY_NAME = "display_name"
    private const val KEY_USER_TYPE = "user_type"
    private const val KEY_API_REGION = "api_region"
    private const val KEY_API_KEY = "api_key"
    private const val KEY_UPLOAD_URL = "upload_url"
    private const val KEY_SECURE_MODE = "secure_mode"
    private const val KEY_AUTH_SIGNATURE_URL = "auth_signature_url"
    private const val KEY_VISITOR_CAN_VIEW_CLIP = "visitor_can_view_clip"
    private const val KEY_HIDE_EXPLORE = "hide_explore"
    private const val KEY_SOCIAL_COMMUNITY_CREATION = "social_community_creation"
    private const val KEY_THEME = "theme"
    private const val KEY_SYNC_NETWORK_CONFIG = "sync_network_config"
    private const val KEY_LAST_SETUP_API_KEY = "last_setup_api_key"
    private const val KEY_LAST_SETUP_REGION = "last_setup_region"
    private const val KEY_LAST_SETUP_UPLOAD_URL = "last_setup_upload_url"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveConfig(context: Context, config: LoginConfig) {
        prefs(context).edit().apply {
            putString(KEY_USER_ID, config.userId)
            putString(KEY_DISPLAY_NAME, config.displayName)
            putString(KEY_USER_TYPE, config.userType.name)
            putString(KEY_API_REGION, config.apiRegion.name)
            putString(KEY_API_KEY, config.apiKey)
            putString(KEY_UPLOAD_URL, config.uploadUrl)
            putBoolean(KEY_SECURE_MODE, config.secureMode)
            putString(KEY_AUTH_SIGNATURE_URL, config.authSignatureUrl)
            putBoolean(KEY_VISITOR_CAN_VIEW_CLIP, config.visitorCanViewClip)
            putBoolean(KEY_HIDE_EXPLORE, config.hideExplore)
            putBoolean(KEY_SOCIAL_COMMUNITY_CREATION, config.socialCommunityCreationButtonVisible)
            putString(KEY_THEME, config.theme.name)
            putBoolean(KEY_SYNC_NETWORK_CONFIG, config.syncNetworkConfig)
            apply()
        }
    }

    fun loadConfig(context: Context): LoginConfig {
        val p = prefs(context)
        val region = try {
            ApiRegion.valueOf(p.getString(KEY_API_REGION, ApiRegion.STAGING.name)!!)
        } catch (_: Exception) {
            ApiRegion.STAGING
        }
        return LoginConfig(
            userId = p.getString(KEY_USER_ID, "") ?: "",
            displayName = p.getString(KEY_DISPLAY_NAME, "") ?: "",
            userType = try {
                UserType.valueOf(p.getString(KEY_USER_TYPE, UserType.SIGNED_IN.name)!!)
            } catch (_: Exception) {
                UserType.SIGNED_IN
            },
            apiRegion = region,
            apiKey = p.getString(KEY_API_KEY, SampleAPIKey.get(region.envKey)) ?: "",
            uploadUrl = p.getString(KEY_UPLOAD_URL, SampleUploadUrl.get(region.envKey)) ?: "",
            secureMode = p.getBoolean(KEY_SECURE_MODE, false),
            authSignatureUrl = p.getString(KEY_AUTH_SIGNATURE_URL, "") ?: "",
            visitorCanViewClip = p.getBoolean(KEY_VISITOR_CAN_VIEW_CLIP, false),
            hideExplore = p.getBoolean(KEY_HIDE_EXPLORE, false),
            socialCommunityCreationButtonVisible = p.getBoolean(KEY_SOCIAL_COMMUNITY_CREATION, true),
            theme = try {
                AppTheme.valueOf(p.getString(KEY_THEME, AppTheme.DEFAULT.name)!!)
            } catch (_: Exception) {
                AppTheme.DEFAULT
            },
            syncNetworkConfig = p.getBoolean(KEY_SYNC_NETWORK_CONFIG, false),
        )
    }

    fun saveLastSetupEnv(context: Context, apiKey: String, region: String, uploadUrl: String) {
        prefs(context).edit().apply {
            putString(KEY_LAST_SETUP_API_KEY, apiKey)
            putString(KEY_LAST_SETUP_REGION, region)
            putString(KEY_LAST_SETUP_UPLOAD_URL, uploadUrl)
            apply()
        }
    }

    fun getLastSetupApiKey(context: Context): String =
        prefs(context).getString(KEY_LAST_SETUP_API_KEY, "") ?: ""

    fun getLastSetupRegion(context: Context): String =
        prefs(context).getString(KEY_LAST_SETUP_REGION, "") ?: ""

    fun getLastSetupUploadUrl(context: Context): String =
        prefs(context).getString(KEY_LAST_SETUP_UPLOAD_URL, "") ?: ""

    fun hasPersistedSession(context: Context): Boolean {
        val p = prefs(context)
        return p.getString(KEY_LAST_SETUP_API_KEY, "")?.isNotEmpty() == true
    }
}
