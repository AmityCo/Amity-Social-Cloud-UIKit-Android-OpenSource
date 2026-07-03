package com.amity.socialcloud.uikit.chat.compose.localization

import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultAmityChatStringProviderTest {

    @Test
    fun getString_returnsOverrideWhenSet() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_chat_home_title" to "Messages"))

        assertEquals("Messages", provider.getString("amity_chat_home_title"))
    }

    @Test
    fun getString_returnsLocaleOverrideWhenSet() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())
        provider.setLocale("ja", mapOf("amity_chat_home_title" to "チャット"))

        assertEquals("チャット", provider.getString("amity_chat_home_title"))
    }

    @Test
    fun getString_overrideTakesPriorityOverLocale() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_chat_home_title" to "Messages"))
        provider.setLocale("ja", mapOf("amity_chat_home_title" to "チャット"))

        assertEquals("Messages", provider.getString("amity_chat_home_title"))
    }

    @Test
    fun getString_returnsKeyWhenNoOverrideOrResource() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())

        assertEquals("amity_chat_unknown_key", provider.getString("amity_chat_unknown_key"))
    }

    @Test
    fun getString_formatsArgumentsCorrectly() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_chat_replying_to" to "Replying to %1\$s"))

        assertEquals("Replying to Alice", provider.getString("amity_chat_replying_to", "Alice"))
    }

    @Test
    fun clearOverrides_removesAllOverrides() {
        val provider = DefaultAmityChatStringProvider(stringKeyToResId = emptyMap())
        provider.setOverrides(mapOf("amity_chat_home_title" to "Messages"))
        provider.clearOverrides()

        assertEquals("amity_chat_home_title", provider.getString("amity_chat_home_title"))
    }
}
