package com.amity.socialcloud.uikit.chat.compose.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Chat's confirmation dialog — the OS-native Material 3 [AlertDialog], matching iOS's native
 * UIAlertController.
 *
 * Every color is bound EXPLICITLY to the AlertDialog token family instead of
 * MaterialTheme.colorScheme: the UIKit sets no Material colorScheme, so an unstyled native
 * dialog would fall back to Material's default LIGHT scheme and render light-in-dark.
 *
 * Native conventions are kept deliberately: bottom-trailing action buttons (confirm rightmost),
 * Material shape/typography, and a scrim tap dismisses (treated as cancel).
 */
@Composable
fun AmityChatConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    confirmColor: Color = AmityTheme.token(AmityColorToken.TextBaseAlert),
    // null = single-button (informational "OK") dialog — no dismiss button is rendered.
    cancelLabel: String? = amityCommonString("amity_common_button_cancel"),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AmityTheme.token(AmityColorToken.SurfaceAlertDialogBackgroundDefault),
        titleContentColor = AmityTheme.token(AmityColorToken.TextAlertDialogHeaderTitleDefault),
        textContentColor = AmityTheme.token(AmityColorToken.TextAlertDialogBodyDefault),
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = confirmColor)
            }
        },
        dismissButton = cancelLabel?.let {
            {
                TextButton(onClick = onDismiss) {
                    Text(text = it, color = AmityTheme.token(AmityColorToken.TextBaseHighlight))
                }
            }
        },
    )
}
