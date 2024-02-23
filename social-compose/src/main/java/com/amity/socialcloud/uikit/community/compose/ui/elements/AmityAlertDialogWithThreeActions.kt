package com.amity.socialcloud.uikit.community.compose.ui.elements

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple

@Composable
fun AmityAlertDialogWithThreeActions(
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
            color = Color.White,
            modifier = Modifier.width(280.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(22.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = dialogTitle,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = AmityTheme.colors.base,
                        ),
                    )
                    Text(
                        text = dialogText,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = AmityTheme.colors.baseShade2,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    Text(
                        text = dismissText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = AmityTheme.colors.primary
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .clickableWithoutRipple { onDismissRequest() }
                    )

                    Text(
                        text = action1Text,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = AmityTheme.colors.primary
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .clickableWithoutRipple { onAction1() }
                    )

                    Text(
                        text = action2Text,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = AmityTheme.colors.primary
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .clickableWithoutRipple { onAction2() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AmityAlertDialogWithThreeActionsPreview() {
    AmityAlertDialogWithThreeActions(
        dialogTitle = "Failed to upload story",
        dialogText = "Would you like to discard or retry uploading?",
        dismissText = "CANCEL",
        action1Text = "DISCARD",
        action2Text = "RETRY",
        onAction1 = {},
        onAction2 = {}
    )
}