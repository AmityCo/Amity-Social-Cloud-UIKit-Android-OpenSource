package com.amity.socialcloud.uikit.chat.compose.message.element.reaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.reaction.AmityMessageReactionListViewModel
import com.amity.socialcloud.uikit.common.reaction.AmityMessageReactionListViewModel.AmityMessageReactionListSheetUIState
import com.amity.socialcloud.uikit.common.reaction.AmityReactionListPageAction
import com.amity.socialcloud.uikit.common.reaction.AmityReactionListPageViewModel
import com.amity.socialcloud.uikit.common.reaction.AmityReactionRoot
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityChatMessageReactionSheet(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityMessageReactionListViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
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
        AmitySheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityMessageReactionListSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            contentWindowInsets = { WindowInsets.navigationBars },
            modifier = modifier
                .semantics {
                    testTagsAsResourceId = true
                },
        ) {
            when (sheetUIState) {
                is AmityMessageReactionListSheetUIState.OpenSheet -> {
                    val message =
                        (sheetUIState as AmityMessageReactionListSheetUIState.OpenSheet).message
                    reactionViewModel.onAction(
                        AmityReactionListPageAction.LoadData(
                            AmityReactionReferenceType.MESSAGE,
                            message.getMessageId()
                        )
                    )

                    // Pinned to half the screen so the sheet keeps its height when the list
                    // updates (e.g. the last reaction is removed while open).
                    Column(modifier = Modifier.fillMaxHeight(0.5f)) {
                        AmityReactionRoot(
                            state = reactionViewModel.state,
                            referenceType = AmityReactionReferenceType.MESSAGE,
                            onAction = {
                                reactionViewModel.onAction(it)
                                if (it is AmityReactionListPageAction.RemoveReaction) {
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
