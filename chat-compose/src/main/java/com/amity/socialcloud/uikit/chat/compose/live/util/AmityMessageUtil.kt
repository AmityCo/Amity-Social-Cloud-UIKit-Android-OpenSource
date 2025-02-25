package com.amity.socialcloud.uikit.chat.compose.live.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage

fun getContent(message: AmityMessage): String {
	return if (message.isDeleted()) {
		"This message was deleted"
	} else {
		(message.getData() as? AmityMessage.Data.TEXT)?.getText() ?: "Unsupport message type"
	}
}