package com.amity.socialcloud.uikit.chat.compose.config

import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.theme.AmityColors

object AmityChatConfigHelper {

    private var bubbleColorOverrides: Map<String, String> = emptyMap()

    fun getBubbleColors(pageId: String, themeColors: AmityColors? = null): AmityChatBubbleColors {
        val config = AmityUIKitConfigController
            .getCustomizationConfig("$pageId/message_bubble/*")

        // Apply programmatic overrides on top of JSON config
        bubbleColorOverrides.forEach { (key, value) ->
            config.addProperty(key, value)
        }

        val defaults = themeColors?.let { AmityChatBubbleColors.fromTheme(it) }
            ?: AmityChatBubbleColors.DEFAULTS
        return AmityChatBubbleColors.fromConfig(config, defaults)
    }

    fun getComposerConfig(pageId: String): AmityChatComposerConfig {
        val config = AmityUIKitConfigController
            .getCustomizationConfig("$pageId/message_composer/*")
        return AmityChatComposerConfig.fromConfig(config)
    }

    fun isConversationUserActionEnabled(actionName: String): Boolean {
        return AmityUIKitConfigController.isConversationUserActionEnabled(actionName)
    }

    fun hasAnyEnabledChatUserAction(): Boolean {
        return AmityUIKitConfigController.hasAnyEnabledChatUserAction()
    }

    fun overrideBubbleColors(
        leftBubbleColor: String? = null,
        leftBubblePressedColor: String? = null,
        leftBubbleTextColor: String? = null,
        leftBubbleSubtleTextColor: String? = null,
        leftBubblePreviewLinkColor: String? = null,
        rightBubbleColor: String? = null,
        rightBubblePressedColor: String? = null,
        rightBubbleTextColor: String? = null,
        rightBubbleSubtleTextColor: String? = null,
        rightBubblePreviewLinkColor: String? = null,
        bubbleDividerColor: String? = null,
    ) {
        val overrides = mutableMapOf<String, String>()
        leftBubbleColor?.let { overrides["left_bubble_color"] = it }
        leftBubblePressedColor?.let { overrides["left_bubble_pressed_color"] = it }
        leftBubbleTextColor?.let { overrides["left_bubble_text_color"] = it }
        leftBubbleSubtleTextColor?.let { overrides["left_bubble_subtle_text_color"] = it }
        leftBubblePreviewLinkColor?.let { overrides["left_bubble_preview_link_color"] = it }
        rightBubbleColor?.let { overrides["right_bubble_color"] = it }
        rightBubblePressedColor?.let { overrides["right_bubble_pressed_color"] = it }
        rightBubbleTextColor?.let { overrides["right_bubble_text_color"] = it }
        rightBubbleSubtleTextColor?.let { overrides["right_bubble_subtle_text_color"] = it }
        rightBubblePreviewLinkColor?.let { overrides["right_bubble_preview_link_color"] = it }
        bubbleDividerColor?.let { overrides["bubble_divider_color"] = it }
        bubbleColorOverrides = overrides
    }
}
