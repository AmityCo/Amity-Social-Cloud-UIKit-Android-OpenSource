package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage

data class AmityMessageAction(
	val onReply: (() -> Unit)? = null,
	val onDelete: (() -> Unit)? = null,
	val onCopy: @Composable (() -> Unit)? = null,
	val onFlag: (() -> Unit)? = null,
	val onUnFlag: (() -> Unit)? = null,
	val onOpenReactions: (AmityMessage) -> Unit = { message ->  },
	val onAddReaction: (AmityMessage, String) -> Unit = { message, reactionName -> },
	val onRemoveReaction: (AmityMessage, String) -> Unit =  { message, reactionName -> },
)