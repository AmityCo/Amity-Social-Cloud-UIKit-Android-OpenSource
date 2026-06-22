package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.NoRippleInteractionSource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R.*
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

private fun AmityContentFlagReason.toLocalizationKey(): String = when (this) {
    is AmityContentFlagReason.CommunityGuidelines -> "amity_social_label_report_reason_community_guidelines"
    is AmityContentFlagReason.FalseInformation -> "amity_social_label_report_reason_false_information"
    is AmityContentFlagReason.HarassmentOrBullying -> "amity_social_label_report_reason_harassment_or_bullying"
    is AmityContentFlagReason.SelfHarmOrSuicide -> "amity_social_label_report_reason_self_harm_or_suicide"
    is AmityContentFlagReason.SellingRestrictedItems -> "amity_social_label_report_reason_selling_restricted"
    is AmityContentFlagReason.SexualContentOrNudity -> "amity_social_label_report_reason_sexual_content_or_nudity"
    is AmityContentFlagReason.SpamOrScams -> "amity_social_label_report_reason_spam_or_scams"
    is AmityContentFlagReason.ViolenceOrThreateningContent -> "amity_social_label_report_reason_violence_or_threatening"
    else -> ""
}

@Composable
fun AmityReportReasonListScreen(
    modifier: Modifier = Modifier,
    onCloseSheetClick: () -> Unit = {},
    onOtherClick: () -> Unit = {},
    onSubmitClick: (AmityContentFlagReason, () -> Unit) -> Unit = { _, _ -> }, // Updated to include error callback
) {

    val (selectedReason, onReasonSelected) = remember { mutableStateOf<AmityContentFlagReason?>(null) }
    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }

    // Report post screen
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = amitySocialString("amity_social_button_report_reason"),
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
                    painter = painterResource(R.drawable.amity_ic_close3),
                    contentDescription = "cancel_report_button",
                    tint = AmityTheme.colors.base
                )
            }
        }

        HorizontalDivider(
            color = AmityTheme.colors.baseShade4,
        )



        // Scrollable content area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                // Description
                Text(
                    text = amitySocialString("amity_social_label_report_list_screen_description"),
                    style = AmityTheme.typography.body,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 12.dp),
                    color = AmityTheme.colors.baseShade1
                )
            }
            // Report reason selection items
            item {
                AmityReportReasonSelection(
                    selectedReason = selectedReason,
                    radioButtonEnable = isButtonEnabled,
                    onReasonSelected = onReasonSelected
                )
            }

            // Others option
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                        .clickableWithoutRipple {
                            onOtherClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = amitySocialString("amity_social_button_others"),
                        style = AmityTheme.typography.bodyBold,
                        modifier = Modifier,
                        color = AmityTheme.colors.base
                    )

                    Icon(
                        painterResource(R.drawable.amity_ic_chevron_right),
                        tint = AmityTheme.colors.base,
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .height(18.dp)
                    )
                }
            }
        }

        // Fixed bottom section
        HorizontalDivider(
            color = AmityTheme.colors.baseShade4,
        )

        Button(
            onClick = {
                selectedReason?.let {
                    setButtonEnabled(false)
                    onSubmitClick(it) {
                        // This is the callback that will be called if report fails
                        setButtonEnabled(true)
                    }
                }
            },
            enabled = selectedReason != null && isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
                disabledContainerColor = AmityTheme.colors.primary.copy(alpha = 0.3f)
            ),
        ) {
            Text(
                text = amitySocialString("amity_social_button_report_submit_button"),
                style = AmityTheme.typography.bodyBold,
                color = Color.White
            )
        }

    }
}

@Composable
fun AmityReportReasonSelection(
    modifier: Modifier = Modifier,
    radioButtonEnable: Boolean,
    selectedReason: AmityContentFlagReason?,
    onReasonSelected: (AmityContentFlagReason) -> Unit,
) {

    val radioOptions = AmityContentFlagReason.list().dropLast(1)    // Exclude "Others" option from the list

    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { reportReason ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .selectable(
                        selected = (reportReason == selectedReason),
                        interactionSource = NoRippleInteractionSource(),
                        indication = null,
                        onClick = { onReasonSelected(reportReason) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = amitySocialString(reportReason.toLocalizationKey()).takeIf { it.isNotBlank() }
                        ?: reportReason.reason,
                    style = AmityTheme.typography.bodyBold,
                    modifier = Modifier,
                    color = AmityTheme.colors.base
                )

                RadioButton(
                    selected = (reportReason == selectedReason),
                    onClick = null, // null recommended for accessibility with screen readers,
                    enabled = radioButtonEnable,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AmityTheme.colors.primary,
                        unselectedColor = AmityTheme.colors.baseShade3
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultAmityReportListScreenPreview() {
    AmityReportReasonListScreen()
}
