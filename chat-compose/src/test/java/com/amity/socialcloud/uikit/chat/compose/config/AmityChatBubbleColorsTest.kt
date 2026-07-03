package com.amity.socialcloud.uikit.chat.compose.config

import androidx.compose.ui.graphics.Color
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class AmityChatBubbleColorsTest {

    @Test
    fun fromConfig_parsesAllColors() {
        val json = JsonObject().apply {
            addProperty("left_bubble_color", "#FF0000")
            addProperty("left_bubble_pressed_color", "#CC0000")
            addProperty("left_bubble_text_color", "#000000")
            addProperty("left_bubble_subtle_text_color", "#666666")
            addProperty("left_bubble_preview_link_color", "#FFFFFF")
            addProperty("right_bubble_color", "#0000FF")
            addProperty("right_bubble_pressed_color", "#0000CC")
            addProperty("right_bubble_text_color", "#FFFFFF")
            addProperty("right_bubble_subtle_text_color", "#AAAAFF")
            addProperty("right_bubble_preview_link_color", "#FFFFFF")
            addProperty("bubble_divider_color", "#C1C1C1")
        }

        val colors = AmityChatBubbleColors.fromConfig(json)

        assertEquals(Color(0xFFFF0000), colors.leftBubbleColor)
        assertEquals(Color(0xFFCC0000), colors.leftBubblePressedColor)
        assertEquals(Color(0xFF000000), colors.leftBubbleTextColor)
        assertEquals(Color(0xFF666666), colors.leftBubbleSubtleTextColor)
        assertEquals(Color(0xFFFFFFFF), colors.leftBubblePreviewLinkColor)
        assertEquals(Color(0xFF0000FF), colors.rightBubbleColor)
        assertEquals(Color(0xFF0000CC), colors.rightBubblePressedColor)
        assertEquals(Color(0xFFFFFFFF), colors.rightBubbleTextColor)
        assertEquals(Color(0xFFAAAAFF), colors.rightBubbleSubtleTextColor)
        assertEquals(Color(0xFFFFFFFF), colors.rightBubblePreviewLinkColor)
        assertEquals(Color(0xFFC1C1C1), colors.bubbleDividerColor)
    }

    @Test
    fun fromConfig_usesDefaultsForMissingKeys() {
        val json = JsonObject() // empty

        val colors = AmityChatBubbleColors.fromConfig(json)

        assertEquals(AmityChatBubbleColors.DEFAULTS.leftBubbleColor, colors.leftBubbleColor)
        assertEquals(AmityChatBubbleColors.DEFAULTS.rightBubbleColor, colors.rightBubbleColor)
    }

    @Test
    fun fromConfig_handlesInvalidHexGracefully() {
        val json = JsonObject().apply {
            addProperty("left_bubble_color", "not-a-color")
            addProperty("right_bubble_color", "#1054DE")
        }

        val colors = AmityChatBubbleColors.fromConfig(json)

        assertEquals(AmityChatBubbleColors.DEFAULTS.leftBubbleColor, colors.leftBubbleColor)
        assertEquals(Color(0xFF1054DE), colors.rightBubbleColor)
    }
}
