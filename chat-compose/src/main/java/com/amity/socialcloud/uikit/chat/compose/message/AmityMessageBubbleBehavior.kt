package com.amity.socialcloud.uikit.chat.compose.message

import android.content.Context

open class AmityMessageBubbleBehavior {

    /**
     * Tapping the sender avatar on a peer message bubble.
     *
     * Opens the full-screen avatar viewer by default; a user with no avatar is a no-op.
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

    /**
     * Tapping an @mention inside message text.
     *
     * No-op by default. A channel mention (@all) never reaches here; it carries no user.
     */
    open fun onMentionUserTap(
        context: Context,
        userId: String,
    ) {
        // Override to handle a mention tap
    }
}
