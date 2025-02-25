package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple


@Composable
fun AmityAlertDialog(
    dialogTitle: String,
    dialogText: String,
    confirmText: String,
    dismissText: String,
    confirmTextColor: Color = AmityTheme.colors.highlight,
    dismissTextColor: Color = AmityTheme.colors.baseShade1,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    AmityAlertDialog(
        dialogTitle = dialogTitle,
        dialogText = AnnotatedString(dialogText),
        confirmText = confirmText,
        dismissText = dismissText,
        confirmTextColor = confirmTextColor,
        dismissTextColor = dismissTextColor,
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun AmityAlertDialog(
    dialogTitle: String,
    dialogText: AnnotatedString,
    confirmText: String,
    dismissText: String,
    confirmTextColor: Color = AmityTheme.colors.highlight,
    dismissTextColor: Color = AmityTheme.colors.baseShade1,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            color = AmityTheme.colors.sheetBackground,
            modifier = Modifier.width(300.dp)
        ) {
            Column {
                AmityAlertDialogContentView(
                    title = dialogTitle,
                    message = dialogText,
                )

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    AmityAlertDialogActionButton(
                        text = dismissText,
                        color = dismissTextColor,
                        onClick = onDismissRequest
                    )

                    AmityAlertDialogActionButton(
                        text = confirmText,
                        color = confirmTextColor,
                        onClick = onConfirmation,
                    )
                }
            }
        }
    }
}

@Composable
fun AmityAlertDialog(
    dialogTitle: String,
    dialogText: String,
    dismissText: String,
    onDismissRequest: () -> Unit = {},
) {
    AmityAlertDialog(
        dialogTitle = dialogTitle,
        dialogText = AnnotatedString(dialogText),
        dismissText = dismissText,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
fun AmityAlertDialog(
    dialogTitle: String,
    dialogText: AnnotatedString,
    dismissText: String,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            color = AmityTheme.colors.sheetBackground,
            modifier = Modifier.width(300.dp)
        ) {
            Column {
                AmityAlertDialogContentView(
                    title = dialogTitle,
                    message = dialogText,
                )

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    AmityAlertDialogActionButton(
                        text = dismissText,
                        onClick = onDismissRequest
                    )
                }
            }
        }
    }
}

@Composable
fun AmityAlertDialog(
    dialogTitle: String,
    dialogText: String,
    dismissText: String,
    action1Text: String,
    action2Text: String,
    onAction1: () -> Unit,
    onAction2: () -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            color = AmityTheme.colors.sheetBackground,
            modifier = Modifier.width(300.dp)
        ) {
            Column {
                AmityAlertDialogContentView(
                    title = dialogTitle,
                    message = AnnotatedString(dialogText)
                )

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    AmityAlertDialogActionButton(
                        text = dismissText,
                        onClick = onDismissRequest,
                    )
                    AmityAlertDialogActionButton(
                        text = action1Text,
                        onClick = onAction1,
                    )
                    AmityAlertDialogActionButton(
                        text = action2Text,
                        onClick = onAction2,
                    )
                }
            }
        }
    }
}

@Composable
fun AmityAlertDialogContentView(
    modifier: Modifier = Modifier,
    title: String,
    message: AnnotatedString,
) {
    Spacer(modifier = modifier.height(22.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = AmityTheme.colors.base,
                ),
            )
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = AmityTheme.colors.baseShade2,
                )
            )
        }
    }
}

@Composable
fun AmityAlertDialogActionButton(
    text: String,
    color: Color = AmityTheme.colors.highlight,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color,
        ),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .clickableWithoutRipple { onClick() }
    )
}