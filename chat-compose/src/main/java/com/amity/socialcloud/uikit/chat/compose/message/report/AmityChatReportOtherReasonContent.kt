package com.amity.socialcloud.uikit.chat.compose.message.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.delay
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityDisabledColor

@Composable
fun AmityChatReportOtherReasonContent(
    onBackClick: () -> Unit = {},
    onSubmitClick: (detail: String, onError: () -> Unit) -> Unit = { _, _ -> },
    onCloseClick: () -> Unit = {},
) {
    var customReason by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(100L)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
            .fillMaxWidth()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Icon(
                painter = painterResource(id = com.amity.socialcloud.uikit.chat.compose.R.drawable.amity_ic_chat_back),
                contentDescription = null,
                tint = AmityTheme.colors.base,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickableWithoutRipple { onBackClick() }
            )

            Text(
                text = amityChatString("chat.report.others"),
                style = AmityTheme.typography.titleBold,
                modifier = Modifier.align(Alignment.Center),
                color = AmityTheme.colors.base,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickableWithoutRipple { onCloseClick() }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_close3),
                    contentDescription = "cancel_report_button",
                    tint = AmityTheme.colors.base,
                )
            }
        }

        HorizontalDivider(color = AmityTheme.colors.baseShade4)

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = amityChatString("chat.report.other.reason.desc"),
                    style = AmityTheme.typography.titleBold,
                    color = AmityTheme.colors.base,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = amityChatString("chat.report.other.reason.optional"),
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade3,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${customReason.length}/300",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1,
                )
            }

            Spacer(Modifier.height(20.dp))

            BasicTextField(
                value = customReason,
                onValueChange = {
                    if (it.length <= 300) {
                        customReason = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = AmityTheme.typography.body.copy(
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start,
                ),
                cursorBrush = SolidColor(AmityTheme.colors.primary),
                decorationBox = { innerTextField ->
                    if (customReason.isEmpty()) {
                        Text(
                            text = amityChatString("chat.report.other.reason.placeholder"),
                            color = AmityTheme.colors.baseShade3,
                            style = AmityTheme.typography.body,
                        )
                    }
                    innerTextField()
                },
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = AmityTheme.colors.baseShade4)
        }

        HorizontalDivider(color = AmityTheme.colors.baseShade4)

        // Submit button
        Button(
            onClick = {
                setButtonEnabled(false)
                onSubmitClick(customReason) {
                    setButtonEnabled(true)
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
                disabledContainerColor = AmityTheme.colors.primary.copy(alpha = 0.3f),
            ),
        ) {
            Text(
                text = amityChatString("chat.report.submit"),
                style = AmityTheme.typography.bodyBold,
                color = if (isButtonEnabled) amityColorWhite else amityDisabledColor(amityColorWhite),
            )
        }
    }
}
