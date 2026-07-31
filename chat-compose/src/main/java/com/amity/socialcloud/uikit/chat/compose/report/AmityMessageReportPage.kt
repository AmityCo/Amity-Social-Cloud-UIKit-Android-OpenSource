package com.amity.socialcloud.uikit.chat.compose.report

import android.app.Activity
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelection
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelectionVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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

    AmityBasePage(pageId = "message_report_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_chevron_left),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            (context as? Activity)?.finish()
                        },
                    tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                )

                Text(
                    text = amityChatString("chat.report.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            AmityDivider(variant = AmityDividerVariant.Post)

            // Description
            AmityBanner(
                hierarchy = AmityBannerHierarchy.DEFAULT,
                description = amityChatString("chat.report.description"),
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
                        AmitySelection(
                            variant = AmitySelectionVariant.RADIO,
                            isSelected = selectedReason == reason,
                            isDisabled = isSubmitting,
                            onChange = null,
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
            AmityDivider(variant = AmityDividerVariant.Post)

            AmityButton(
                variant = AmityButtonVariant.MAIN,
                label = amityChatString("chat.report.submit"),
                onClick = {
                    selectedReason?.let { reason ->
                        setSubmitting(true)
                        viewModel.reportMessage(
                            messageId = messageId,
                            reason = reason,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(reportSuccessMsg)
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
                    .padding(16.dp),
            )
        }
    }
}
