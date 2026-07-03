package com.amity.socialcloud.uikit.sample.login

enum class ApiRegion(val label: String, val envKey: String) {
    STAGING("staging", "sc-staging"),
    SG("SG", "production-sg"),
    EU("EU", "production-eu"),
    US("US", "production-us");
}

enum class UserType(val label: String) {
    SIGNED_IN("Signed-in"),
    VISITOR("Visitor");
}

enum class AppTheme(val label: String, val configValue: String) {
    DEFAULT("Default", "default"),
    LIGHT("Light", "light"),
    DARK("Dark", "dark");
}

data class LoginConfig(
    val userId: String = "",
    val displayName: String = "",
    val userType: UserType = UserType.SIGNED_IN,
    val apiRegion: ApiRegion = ApiRegion.STAGING,
    val apiKey: String = "",
    val uploadUrl: String = "",
    val secureMode: Boolean = false,
    val authSignatureUrl: String = "",
    val authSignatureExpiresAtMillis: Long = System.currentTimeMillis() + 3_600_000,
    val visitorCanViewClip: Boolean = false,
    val hideExplore: Boolean = false,
    val socialCommunityCreationButtonVisible: Boolean = true,
    val theme: AppTheme = AppTheme.DEFAULT,
    val syncNetworkConfig: Boolean = false,
)
