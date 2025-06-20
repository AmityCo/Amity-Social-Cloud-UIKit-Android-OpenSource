package com.amity.socialcloud.uikit.community.compose.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun DeclineInvitationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AmityAlertDialog(
            title = stringResource(R.string.amity_v4_community_invitation_reject_dialog_title),
            subtitle = stringResource(R.string.amity_v4_community_invitation_reject_dialog_subtitle),
            dismissText = stringResource(R.string.amity_cancel),
            confirmText = stringResource(R.string.amity_decline),
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}

@Composable
fun AmityAlertDialog(
    title: String,
    subtitle: String,
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(0.dp),
        title = {
            Text(
                text = title,
                style = AmityTheme.typography.bodyBold,
                fontSize = 17.sp,
            )
        },
        text = {
            Text(
                text = subtitle,
                style = AmityTheme.typography.body,
                fontSize = 13.sp,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    confirmText,
                    color = AmityTheme.colors.alert,
                    fontSize = 17.sp,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    dismissText,
                    color = AmityTheme.colors.highlight,
                    fontSize = 17.sp,
                )
            }
        }
    )
}

