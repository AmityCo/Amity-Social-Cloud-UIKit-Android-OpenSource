package com.amity.socialcloud.uikit.chat.compose.config

import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AmityChatComposerConfigTest {

    @Test
    fun fromConfig_parsesAllFields() {
        val json = JsonObject().apply {
            addProperty("message_limit", 200)
            addProperty("placeholder_text", "Write a message")
        }

        val config = AmityChatComposerConfig.fromConfig(json)

        assertEquals(200, config.messageLimit)
        assertEquals("Write a message", config.placeholderText)
    }

    @Test
    fun fromConfig_usesDefaultsForMissingKeys() {
        val json = JsonObject() // empty

        val config = AmityChatComposerConfig.fromConfig(json)

        assertEquals(AmityChatComposerConfig.DEFAULT_MESSAGE_LIMIT, config.messageLimit)
        assertNull(config.placeholderText)
    }

    @Test
    fun fromConfig_handlesInvalidMessageLimitGracefully() {
        val json = JsonObject().apply {
            addProperty("message_limit", "not-a-number")
        }

        val config = AmityChatComposerConfig.fromConfig(json)

        assertEquals(AmityChatComposerConfig.DEFAULT_MESSAGE_LIMIT, config.messageLimit)
    }
}
