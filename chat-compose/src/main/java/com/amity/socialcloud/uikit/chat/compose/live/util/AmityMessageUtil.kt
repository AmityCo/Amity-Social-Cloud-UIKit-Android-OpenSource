package com.amity.socialcloud.uikit.chat.compose.live.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider

fun getContent(message: AmityMessage): String {
	return if (message.isDeleted()) {
		DefaultAmityChatStringProvider.getInstance().getString("chat.message.deleted")
	} else {
		(message.getData() as? AmityMessage.Data.TEXT)?.getText() ?: DefaultAmityChatStringProvider.getInstance().getString("chat.message.deleted")
	}
}