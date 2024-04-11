package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.runtime.Composable

data class AmityMessageAction(
	val onReply: (() -> Unit)? = null,
	val onDelete: (() -> Unit)? = null,
	val onCopy: @Composable (() -> Unit)? = null,
)