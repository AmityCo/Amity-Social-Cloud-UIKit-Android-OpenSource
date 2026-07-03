package com.amity.socialcloud.uikit.common.config

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class AmityUIKitConfig(
    @SerializedName("preferred_theme")
    var preferredTheme: String,
    @SerializedName("theme")
    val globalTheme: GlobalTheme,
    @SerializedName("excludes")
    val excludes: JsonArray,
    @SerializedName("message_reactions")
    val messageReactions: JsonArray,
    @SerializedName("social_reactions")
    val socialReactions: JsonArray,
    @SerializedName("customizations")
    val customizations: JsonObject,
    @SerializedName("feature_flags")
    val featureFlags: FeatureFlags = FeatureFlags(),
) {
    data class GlobalTheme(
        @SerializedName("light")
        var lightTheme: UIKitTheme,
        @SerializedName("dark")
        var darkTheme: UIKitTheme,
    )

    data class FeatureFlags(
        @SerializedName("post")
        val post: PostFeatureFlags = PostFeatureFlags(),
        @SerializedName("chat")
        val chat: ChatFeatureFlags = ChatFeatureFlags(),
    )

    data class PostFeatureFlags(
        @SerializedName("clip")
        val clip: JsonObject = JsonObject(),
    )

    data class ChatFeatureFlags(
        @SerializedName("enabled_channel_types")
        val enabledChannelTypes: List<String> = listOf("conversation", "community"),
        @SerializedName("conversation_chat_user_actions")
        val conversationChatUserActions: JsonArray = JsonArray(),
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
        @SerializedName("base_inverse_color")
        val baseInverseColor: String,
        @SerializedName("alert_color")
        val alertColor: String,
        @SerializedName("background_color")
        val backgroundColor: String,
        @SerializedName("background_shade1_color")
        val backgroundShade1Color: String,
        @SerializedName("toast_background_color")
        val toastBackgroundColor: String? = null,
        @SerializedName("highlight_color")
        val highlightColor: String? = null,
        @SerializedName("secondary_shade2_color")
        val secondaryShade2: String? = null,
        @SerializedName("secondary_shade3_color")
        val secondaryShade3: String? = null,
        @SerializedName("secondary_shade4_color")
        val secondaryShade4: String? = null,
    )
}