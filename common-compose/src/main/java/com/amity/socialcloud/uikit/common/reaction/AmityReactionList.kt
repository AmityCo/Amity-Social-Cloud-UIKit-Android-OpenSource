package com.amity.socialcloud.uikit.common.reaction

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityReactionList(
    modifier: Modifier = Modifier,
    referenceType: AmityReactionReferenceType,
    referenceId: String,
    onClose: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val reactionViewModel = viewModel<AmityReactionListPageViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityReactionListPageViewModel() as T
            }
        },
    )
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background
    ) {
        reactionViewModel.onAction(AmityReactionListPageAction.LoadData(referenceType, referenceId))

        AmityReactionRoot(
            state = reactionViewModel.state,
            onAction = reactionViewModel::onAction,
            onUserClick = onUserClick,
        )
    }
}

@Preview
@Composable
fun AmityReactionBottomSheetPreview() {
    AmityReactionList(
        referenceType = AmityReactionReferenceType.COMMENT,
        referenceId = "",
    )
}
