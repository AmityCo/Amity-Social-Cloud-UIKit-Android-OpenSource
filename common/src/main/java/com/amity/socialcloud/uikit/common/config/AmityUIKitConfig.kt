package com.amity.socialcloud.uikit.common.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class AmityUIKitConfig(
    @SerializedName("preferred_theme")
    val preferredTheme: String,
    @SerializedName("theme")
    val globalTheme: GlobalTheme,
    @SerializedName("excludes")
    val excludes: JsonArray,
    @SerializedName("customizations")
    val customizations: JsonObject
) {
    data class GlobalTheme(
        @SerializedName("light")
        val lightTheme: UIKitTheme,
        @SerializedName("dark")
        val darkTheme: UIKitTheme,
    )

    data class UIKitTheme(
        @SerializedName("primary_color")
        val primaryColor: String,
        @SerializedName("secondary_color")
        val secondaryColor: String,
        @SerializedName("base_color")
        val baseColor: String,
        @SerializedName("base_shade1_color")
        val baseShade1Color: String,
        @SerializedName("base_shade2_color")
        val baseShade2Color: String,
        @SerializedName("base_shade3_color")
        val baseShade3Color: String,
        @SerializedName("base_shade4_color")
        val baseShade4Color: String,
        @SerializedName("alert_color")
        val alertColor: String,
        @SerializedName("background_color")
        val backgroundColor: String
    )
}