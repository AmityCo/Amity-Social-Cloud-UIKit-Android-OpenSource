package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R.*
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityNoOutlineTextField
import kotlinx.coroutines.delay

@Composable
fun AmityReportOtherReasonScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSubmitClick: (detail: String, () -> Unit) -> Unit = { _, _ -> },
    onCloseSheetClick: () -> Unit = {},
) {
    // Custom reason input screen
    var customReason by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }

    // Request focus and show keyboard when composable enters the composition
    LaunchedEffect(Unit) {
        delay(100L) // Short delay to ensure UI is ready
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_back),
                contentDescription = null,
                tint = AmityTheme.colors.base,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickableWithoutRipple {
                        onBackClick()
                    }
            )

            Text(
                text = "Others",
                style = AmityTheme.typography.titleBold,
                modifier = Modifier.align(Alignment.Center),
                color = AmityTheme.colors.base
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickableWithoutRipple {
                        onCloseSheetClick()
                    }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(drawable.amity_ic_close3),
                    contentDescription = "cancel_report_button",
                    tint = AmityTheme.colors.base
                )
            }
        }

        HorizontalDivider(
            color = AmityTheme.colors.baseShade4,
        )

        Spacer(Modifier.height(24.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.amity_report_other_reason_desc),
                    style = AmityTheme.typography.titleBold,
                    color = AmityTheme.colors.base
                )
                Spacer(modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.amity_report_other_reason_optional),
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade3
                )
                Spacer(modifier.weight(1f))
                Text(
                    text = "${customReason.length}/300",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
            }

            Spacer(Modifier.height(20.dp))

            AmityNoOutlineTextField(
                value = customReason,
                onValueChange = {
                    customReason = it
                },
                maxCharLength = 300,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeHolder = {
                    Text(
                        text = stringResource(R.string.amity_report_text_placeholder),
                        color = AmityTheme.colors.baseShade3,
                        style = AmityTheme.typography.body
                    )
                },
                singleLine = false,
                cursorBrushColor = AmityTheme.colors.primary,
                textStyle = AmityTheme.typography.body.copy(
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start,
                ),
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = AmityTheme.colors.baseShade4)
        }

        HorizontalDivider(
            color = AmityTheme.colors.baseShade4,
        )

        // Submit button
        Button(
            onClick = {
                setButtonEnabled(false)
                onSubmitClick(customReason) {
                    // Re-enable button on error
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
                disabledContainerColor = AmityTheme.colors.primaryShade3
            ),
        ) {
            Text(
                text = stringResource(R.string.amity_report_submit_button),
                style = AmityTheme.typography.bodyBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultAmityCustomReasonScreenPreview() {
    AmityReportOtherReasonScreen()
}