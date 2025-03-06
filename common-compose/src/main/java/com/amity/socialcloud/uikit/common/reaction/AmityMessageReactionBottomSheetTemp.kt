package com.amity.socialcloud.uikit.common.reaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.reaction.AmityMessageReactionListViewModel.AmityMessageReactionListSheetUIState
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityMessageReactionBottomSheetTemp(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityMessageReactionListViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityMessageReactionListSheetUIState.CloseSheet
        }
    }

    val reactionViewModel = viewModel<AmityReactionListPageViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityReactionListPageViewModel() as T
            }
        },
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityMessageReactionListSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = modifier
                .semantics {
                    testTagsAsResourceId = true
                },
        ) {
            when (sheetUIState) {
                is AmityMessageReactionListSheetUIState.OpenSheet -> {
                    val message =
                        (sheetUIState as AmityMessageReactionListSheetUIState.OpenSheet).message
                    reactionViewModel.onAction(AmityReactionListPageAction.LoadData(AmityReactionReferenceType.MESSAGE, message.getMessageId()))

                    Column {
                        AmityReactionRoot(
                            state = reactionViewModel.state,
                            onAction = {
                                reactionViewModel.onAction(it)
                                if(it is AmityReactionListPageAction.RemoveReaction) {
                                    viewModel.updateSheetUIState(AmityMessageReactionListSheetUIState.CloseSheet)
                                }
                            },
                        )
                    }
                }

                AmityMessageReactionListSheetUIState.CloseSheet -> {}
            }
        }
    }
}