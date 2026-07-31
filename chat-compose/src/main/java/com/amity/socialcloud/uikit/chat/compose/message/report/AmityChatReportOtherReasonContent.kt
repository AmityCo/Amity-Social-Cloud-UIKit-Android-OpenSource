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
import androidx.compose.foundation.text.BasicTextField
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
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonColor
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.delay

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
                painter = painterResource(id = CommonR.drawable.amity_ic_chevron_left),
                contentDescription = null,
                tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickableWithoutRipple { onBackClick() }
            )

            Text(
                text = amityChatString("chat.report.others"),
                style = AmityTheme.typography.titleBold,
                modifier = Modifier.align(Alignment.Center),
                color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickableWithoutRipple { onCloseClick() }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(CommonR.drawable.amity_ic_cross_r),
                    contentDescription = "cancel_report_button",
                    tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                )
            }
        }

        AmityDivider(variant = AmityDividerVariant.Post)

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
                    color = AmityTheme.token(AmityColorToken.TextInputTextInputTitleDefault),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = amityChatString("chat.report.other.reason.optional"),
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.token(AmityColorToken.TextInputTextInputIndicatorDefault),
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${customReason.length}/300",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.token(AmityColorToken.TextInputTextInputTextCountDefault),
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
                    color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabledFilled),
                    textAlign = TextAlign.Start,
                ),
                cursorBrush = SolidColor(AmityTheme.token(AmityColorToken.TextInputTextInputTextCursorDefault)),
                decorationBox = { innerTextField ->
                    if (customReason.isEmpty()) {
                        Text(
                            text = amityChatString("chat.report.other.reason.placeholder"),
                            color = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabled),
                            style = AmityTheme.typography.body,
                        )
                    }
                    innerTextField()
                },
            )

            Spacer(Modifier.height(16.dp))

            AmityDivider(variant = AmityDividerVariant.Post)
        }

        AmityDivider(variant = AmityDividerVariant.Post)

        // Submit button
        AmityButton(
            variant = AmityButtonVariant.MAIN,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                setButtonEnabled(false)
                onSubmitClick(customReason) {
                    setButtonEnabled(true)
                }
            },
            color = AmityButtonColor.DEFAULT,
            hierarchy = AmityButtonHierarchy.PRIMARY,
            style = AmityButtonStyle.FILLED,
            mainSize = AmityMainButtonSize.LG,
            label = amityChatString("chat.report.submit"),
            enabled = isButtonEnabled,
        )
    }
}
