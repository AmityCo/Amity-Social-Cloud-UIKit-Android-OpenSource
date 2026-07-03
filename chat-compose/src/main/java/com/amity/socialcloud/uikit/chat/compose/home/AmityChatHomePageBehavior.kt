package com.amity.socialcloud.uikit.chat.compose.home

import android.content.Context

open class AmityChatHomePageBehavior {

    open fun goToConversationChatPage(
        context: Context,
        channelId: String,
        jumpToMessageId: String? = null,
    ) {
        val intent = com.amity.socialcloud.uikit.chat.compose.conversation.AmityChatPageActivity
            .newIntent(context, channelId, jumpToMessageId)
        context.startActivity(intent)
    }

    open fun goToGroupChatPage(
        context: Context,
        channelId: String,
        jumpToMessageId: String? = null,
    ) {
        val intent = com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageActivity
            .newIntent(context, channelId, jumpToMessageId)
        context.startActivity(intent)
    }

    open fun goToCreateConversationPage(
        context: Context,
    ) {
        val intent = com.amity.socialcloud.uikit.chat.compose.create.AmityChannelCreateConversationPageActivity
            .newIntent(context)
        context.startActivity(intent)
    }

    open fun goToCreateGroupPage(
        context: Context,
    ) {
        val intent = com.amity.socialcloud.uikit.chat.compose.create.AmityCreateGroupChatPageActivity
            .newIntent(context)
        context.startActivity(intent)
    }

    open fun goToSearchPage(
        context: Context,
    ) {
        context.startActivity(
            com.amity.socialcloud.uikit.chat.compose.search.AmitySearchChannelPageActivity.newIntent(context)
        )
    }

    open fun goToArchivedChatPage(
        context: Context,
    ) {
        context.startActivity(
            com.amity.socialcloud.uikit.chat.compose.archive.AmityArchivedChatPageActivity.newIntent(context)
        )
    }
}
