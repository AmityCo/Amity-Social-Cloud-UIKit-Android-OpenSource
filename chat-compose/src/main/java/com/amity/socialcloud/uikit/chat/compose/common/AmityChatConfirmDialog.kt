package com.amity.socialcloud.uikit.chat.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityChatConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    confirmColor: Color = AmityTheme.colors.alert,
    cancelLabel: String = amityCommonString("amity_common_button_cancel"),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AmityTheme.colors.baseShade4)
                    .width(270.dp)
            ) {
                Column {
                    Column(
                        modifier = Modifier.padding(19.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                        Text(
                            text = message,
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                    }
                    HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
                    Row(
                        modifier = Modifier
                            .height(41.dp)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                text = cancelLabel,
                                fontSize = 17.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(600),
                                color = AmityTheme.colors.primary,
                            )
                        }
                        VerticalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
                        TextButton(
                            onClick = onConfirm,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                text = confirmLabel,
                                fontSize = 17.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(600),
                                color = confirmColor,
                            )
                        }
                    }
                }
            }
        }
    }
}
