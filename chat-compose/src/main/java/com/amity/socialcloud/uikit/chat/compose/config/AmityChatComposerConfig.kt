package com.amity.socialcloud.uikit.chat.compose.config

import com.google.gson.JsonObject

data class AmityChatComposerConfig(
    val messageLimit: Int,
    val placeholderText: String?,
) {
    companion object {
        const val DEFAULT_MESSAGE_LIMIT = 5000

        fun fromConfig(config: JsonObject): AmityChatComposerConfig {
            val messageLimit = try {
                config.get("message_limit")?.asInt ?: DEFAULT_MESSAGE_LIMIT
            } catch (e: Exception) {
                DEFAULT_MESSAGE_LIMIT
            }

            val placeholderText = try {
                config.get("placeholder_text")?.asString
            } catch (e: Exception) {
                null
            }

            return AmityChatComposerConfig(
                messageLimit = messageLimit,
                placeholderText = placeholderText,
            )
        }
    }
}
