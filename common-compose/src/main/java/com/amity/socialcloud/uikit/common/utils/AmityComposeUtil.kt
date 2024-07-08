package com.amity.socialcloud.uikit.common.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.common.toDp

@Composable
fun copyText(text: String) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    clipboardManager.setText(AnnotatedString((text)))
}

@Composable
fun isKeyboardVisible(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun getKeyboardHeight(): State<Dp> {
    val bottomHeight = WindowInsets.ime.getBottom(LocalDensity.current).toDp().dp
    return rememberUpdatedState(bottomHeight)
}