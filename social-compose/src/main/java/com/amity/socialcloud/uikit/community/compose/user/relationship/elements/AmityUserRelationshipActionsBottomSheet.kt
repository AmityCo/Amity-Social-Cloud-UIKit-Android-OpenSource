package com.amity.socialcloud.uikit.community.compose.user.relationship.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityUserRelationshipActionsBottomSheet(
    modifier: Modifier = Modifier,
    user: AmityUser,
    allowToBlock: Boolean = false,
    onDismiss: () -> Unit,
    onReportUser: (AmityUser) -> Unit,
    onUnreportUser: (AmityUser) -> Unit,
    onBlockUser: (AmityUser) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier
            .semantics {
                testTagsAsResourceId = true
            },
    ) {
        Column(
            modifier = modifier
                .background(AmityTheme.colors.background)
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
        ) {
            if (user.isFlaggedByMe()) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_unreport,
                    text = amitySocialString("amity_social_button_unreport_user"),
                    modifier = modifier,
                ) {
                    onDismiss()
                    onUnreportUser(user)
                }
            } else {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_report1,
                    text = amitySocialString("amity_social_button_report_user"),
                    modifier = modifier,
                ) {
                    onDismiss()
                    onReportUser(user)
                }
            }

            if (allowToBlock) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_blocked_user,
                    text = amitySocialString("amity_social_button_block_user"),
                    modifier = modifier,
                ) {
                    onDismiss()
                    onBlockUser(user)
                }
            }
        }
    }
}