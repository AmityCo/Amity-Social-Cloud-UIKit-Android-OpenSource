package com.amity.socialcloud.uikit.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

@Composable
fun copyText(text: String) {
	val clipboardManager: ClipboardManager = LocalClipboardManager.current
	clipboardManager.setText(AnnotatedString((text)))
}