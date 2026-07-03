package com.amity.socialcloud.uikit.chat.compose.report

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityDisabledColor

@Composable
fun AmityMessageReportPage(
    modifier: Modifier = Modifier,
    messageId: String,
) {
    val viewModel = remember { AmityMessageReportPageViewModel() }
    val context = LocalContext.current
    val reportSuccessMsg = amityChatString("chat.toast.message.reported")

    val reportReasons = remember { AmityContentFlagReason.list() }
    val (selectedReason, onReasonSelected) = remember { mutableStateOf<AmityContentFlagReason?>(null) }
    val (isSubmitting, setSubmitting) = remember { mutableStateOf(false) }

    AmityBasePage(pageId = "message_report_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            (context as? Activity)?.finish()
                        },
                    tint = AmityTheme.colors.base,
                )

                Text(
                    text = amityChatString("chat.report.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            // Description
            Text(
                text = amityChatString("chat.report.description"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    color = AmityTheme.colors.baseShade1,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )

            // Reason list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                items(reportReasons) { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .selectable(
                                selected = selectedReason == reason,
                                onClick = { onReasonSelected(reason) },
                                role = Role.RadioButton,
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                            ),
                            enabled = !isSubmitting,
                        )
                        Text(
                            text = reason.reason,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                }
            }

            // Submit button
            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            Button(
                onClick = {
                    selectedReason?.let { reason ->
                        setSubmitting(true)
                        viewModel.reportMessage(
                            messageId = messageId,
                            reason = reason,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    reportSuccessMsg,
                                    Toast.LENGTH_SHORT,
                                ).show()
                                (context as? Activity)?.finish()
                            },
                            onError = {
                                setSubmitting(false)
                            },
                        )
                    }
                },
                enabled = selectedReason != null && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary,
                    disabledContainerColor = AmityTheme.colors.primary.copy(alpha = 0.3f),
                    disabledContentColor = amityDisabledColor(amityColorWhite),
                ),
            ) {
                Text(
                    text = amityChatString("chat.report.submit"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedReason != null && !isSubmitting) amityColorWhite else amityDisabledColor(amityColorWhite),
                    ),
                )
            }
        }
    }
}
