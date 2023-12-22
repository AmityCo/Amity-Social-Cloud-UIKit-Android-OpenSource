package com.amity.socialcloud.uikit.common.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class AmityUIKitConfig(
    @SerializedName("global_theme")
    val globalTheme: GlobalTheme,
    @SerializedName("excludes")
    val excludes: JsonArray,
    @SerializedName("customizations")
    val customizations: JsonObject
) {
    data class GlobalTheme(
        @SerializedName("light_theme")
        val lightTheme: UIKitTheme,
    )

    data class UIKitTheme(
        @SerializedName("primary_color")
        val primaryColor: String,
        @SerializedName("secondary_color")
        val secondaryColor: String,
    )
}