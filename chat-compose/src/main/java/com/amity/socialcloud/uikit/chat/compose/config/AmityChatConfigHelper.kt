package com.amity.socialcloud.uikit.chat.compose.config

import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController

object AmityChatConfigHelper {

    fun getComposerConfig(pageId: String): AmityChatComposerConfig {
        val config = AmityUIKitConfigController
            .getCustomizationConfig("$pageId/message_composer/*")
        return AmityChatComposerConfig.fromConfig(config)
    }

    /**
     * The row's config switch — and for Block, the `userRelationship` module
     * too. Report never reads the module (chat user action REQ-008a).
     */
    fun isConversationUserActionEnabled(actionName: String): Boolean {
        return AmityUIKitConfigController.isChatUserActionAvailable(actionName)
    }

    fun hasAnyEnabledChatUserAction(): Boolean {
        return AmityUIKitConfigController.hasAnyEnabledChatUserAction()
    }
}
