package com.amity.socialcloud.uikit.community.compose.user.profile.elements

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityUserUnfollowBottomSheet(
    modifier: Modifier = Modifier,
    user: AmityUser,
    onDismiss: () -> Unit,
    onUnfollow: (AmityUser) -> Unit,
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
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_user_unfollow,
                text = "Unfollow",
                modifier = modifier,
            ) {
                onDismiss()
                onUnfollow(user)
            }
        }
    }
}