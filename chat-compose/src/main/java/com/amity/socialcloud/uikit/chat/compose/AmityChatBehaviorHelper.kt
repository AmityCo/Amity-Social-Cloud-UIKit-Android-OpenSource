package com.amity.socialcloud.uikit.chat.compose

import com.amity.socialcloud.uikit.chat.compose.conversation.AmityChatPageBehavior
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageBehavior
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageBehavior
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior

object AmityChatBehaviorHelper {

    var globalBehavior: AmityGlobalBehavior =
        AmityGlobalBehavior()

    var chatHomePageBehavior: AmityChatHomePageBehavior =
        AmityChatHomePageBehavior()

    var conversationChatPageBehavior: AmityChatPageBehavior =
        AmityChatPageBehavior()

    var groupChatPageBehavior: AmityGroupChatPageBehavior =
        AmityGroupChatPageBehavior()
}