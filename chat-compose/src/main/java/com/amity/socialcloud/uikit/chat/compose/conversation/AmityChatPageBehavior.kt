package com.amity.socialcloud.uikit.chat.compose.conversation

import android.content.Context

open class AmityChatPageBehavior {

    open fun goToUserProfile(
        context: Context,
        userId: String,
    ) {
        // Override to navigate to user profile
    }

    open fun goToMessageReport(
        context: Context,
        messageId: String,
    ) {
        context.startActivity(
            com.amity.socialcloud.uikit.chat.compose.report.AmityMessageReportPageActivity.newIntent(context, messageId)
        )
    }

    open fun goToNotificationPreference(
        context: Context,
        channelId: String,
    ) {
        context.startActivity(
            com.amity.socialcloud.uikit.chat.compose.notification.AmityGroupNotificationPreferencePageActivity.newIntent(context, channelId)
        )
    }
}
