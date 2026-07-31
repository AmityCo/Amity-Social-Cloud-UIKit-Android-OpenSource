package com.amity.socialcloud.uikit.chat.compose.config

import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController

object AmityChatConfigHelper {

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
}
