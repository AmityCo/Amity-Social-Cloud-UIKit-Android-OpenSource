package com.amity.socialcloud.uikit.chat.compose.config

import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.ui.theme.AmityColors
import com.google.gson.JsonObject

data class AmityChatBubbleColors(
    val leftBubbleColor: Color,
    val leftBubblePressedColor: Color,
    val leftBubbleTextColor: Color,
    val leftBubbleSubtleTextColor: Color,
    val leftBubblePreviewLinkColor: Color,
    val rightBubbleColor: Color,
    val rightBubblePressedColor: Color,
    val rightBubbleTextColor: Color,
    val rightBubbleSubtleTextColor: Color,
    val rightBubblePreviewLinkColor: Color,
    val bubbleDividerColor: Color,
) {
    companion object {
        // Pure-hex fallback used in tests and when no AmityColors context is available.
        val DEFAULTS = AmityChatBubbleColors(
            leftBubbleColor = Color(0xFFEBECEF),
            leftBubblePressedColor = Color(0xFFA5A9B5),
            leftBubbleTextColor = Color(0xFF292B32),
            leftBubbleSubtleTextColor = Color(0xFF636878),
            leftBubblePreviewLinkColor = Color(0xFFFFFFFF),
            rightBubbleColor = Color(0xFF1054DE),
            rightBubblePressedColor = Color(0xFF1A4499),
            rightBubbleTextColor = Color(0xFFFFFFFF),
            rightBubbleSubtleTextColor = Color(0xFFA9C4F9),
            rightBubblePreviewLinkColor = Color(0xFFFFFFFF),
            bubbleDividerColor = Color(0xFFC1C1C1),
        )

        // Runtime defaults that honour the active AmityColors theme.
        fun fromTheme(colors: AmityColors) = AmityChatBubbleColors(
            leftBubbleColor = colors.baseShade4,   // base_shade4_color
            leftBubblePressedColor = colors.baseShade3,      // base_shade3_color
            leftBubbleTextColor = colors.base,               // base_color
            leftBubbleSubtleTextColor = colors.baseShade1,   // base_shade1_color
            leftBubblePreviewLinkColor = colors.background,  // background_color
            rightBubbleColor = colors.primary,         // primary_color
            rightBubblePressedColor = colors.primary,  // no darker shade in system; same as bg
            rightBubbleTextColor = colors.baseInverse,       // base_inverse_color
            rightBubbleSubtleTextColor = colors.primaryShade2, // lighter primary tint
            rightBubblePreviewLinkColor = colors.baseInverse,
            bubbleDividerColor = colors.baseShade3,
        )

        fun fromConfig(config: JsonObject, themeDefaults: AmityChatBubbleColors = DEFAULTS): AmityChatBubbleColors {
            return AmityChatBubbleColors(
                leftBubbleColor = config.parseColor("left_bubble_color", themeDefaults.leftBubbleColor),
                leftBubblePressedColor = config.parseColor("left_bubble_pressed_color", themeDefaults.leftBubblePressedColor),
                leftBubbleTextColor = config.parseColor("left_bubble_text_color", themeDefaults.leftBubbleTextColor),
                leftBubbleSubtleTextColor = config.parseColor("left_bubble_subtle_text_color", themeDefaults.leftBubbleSubtleTextColor),
                leftBubblePreviewLinkColor = config.parseColor("left_bubble_preview_link_color", themeDefaults.leftBubblePreviewLinkColor),
                rightBubbleColor = config.parseColor("right_bubble_color", themeDefaults.rightBubbleColor),
                rightBubblePressedColor = config.parseColor("right_bubble_pressed_color", themeDefaults.rightBubblePressedColor),
                rightBubbleTextColor = config.parseColor("right_bubble_text_color", themeDefaults.rightBubbleTextColor),
                rightBubbleSubtleTextColor = config.parseColor("right_bubble_subtle_text_color", themeDefaults.rightBubbleSubtleTextColor),
                rightBubblePreviewLinkColor = config.parseColor("right_bubble_preview_link_color", themeDefaults.rightBubblePreviewLinkColor),
                bubbleDividerColor = config.parseColor("bubble_divider_color", themeDefaults.bubbleDividerColor),
            )
        }

        private fun JsonObject.parseColor(key: String, default: Color): Color {
            val hex = get(key)?.asString ?: return default
            return try {
                parseHexColor(hex)
            } catch (e: Exception) {
                default
            }
        }

        private fun parseHexColor(hex: String): Color {
            val cleanHex = hex.removePrefix("#")
            if (cleanHex.length !in listOf(6, 8)) {
                throw IllegalArgumentException("Invalid hex color: $hex")
            }

            val argb = if (cleanHex.length == 6) {
                // Add full opacity for 6-digit hex
                "FF$cleanHex"
            } else {
                cleanHex
            }

            val colorInt = argb.toLong(16).toInt()
            return Color(colorInt)
        }
    }
}
