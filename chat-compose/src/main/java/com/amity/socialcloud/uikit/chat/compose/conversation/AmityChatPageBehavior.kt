package com.amity.socialcloud.uikit.chat.compose.conversation

import android.content.Context
import com.amity.socialcloud.uikit.chat.compose.message.AmityAvatarFullScreenPageActivity

open class AmityChatPageBehavior {

    /**
     * Tapping the peer avatar in the conversation header.
     *
     * Opens the full-screen avatar viewer by default; a peer with no avatar is a no-op.
     * Override to do something else instead — the default does not run when you do.
     */
    open fun onAvatarTap(
        context: Context,
        userId: String,
        avatarUrl: String?,
    ) {
        if (avatarUrl.isNullOrBlank()) return
        context.startActivity(AmityAvatarFullScreenPageActivity.newIntent(context, avatarUrl))
    }

    @Deprecated(
        message = "Never invoked by the UIKit, and it names a destination rather than the tap " +
                "that would reach it. Mention taps go through " +
                "AmityMessageBubbleBehavior.onMentionUserTap.",
    )
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
