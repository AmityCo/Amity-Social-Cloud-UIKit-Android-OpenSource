package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponent
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalDialogUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityStoryModalBottomSheet(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityStoryModalSheetUIState.CloseSheet
        }
    }

    LaunchedEffect(showSheet) {
        delay(100)
        viewModel.handleSegmentTimer(shouldPause = showSheet)

        if (!showSheet) {
            scope.launch { sheetState.hide() }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityStoryModalSheetUIState.CloseSheet)
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
                is AmityStoryModalSheetUIState.OpenCommentTraySheet -> {
                    val data = sheetUIState as AmityStoryModalSheetUIState.OpenCommentTraySheet
                    AmityCommentTrayComponent(
                        modifier = modifier,
                        referenceId = data.storyId,
                        referenceType = AmityCommentReferenceType.STORY,
                        community = data.community,
                        shouldAllowInteraction = data.shouldAllowInteraction,
                        shouldAllowCreation = data.shouldAllowComment,
                    )
                }

                is AmityStoryModalSheetUIState.OpenOverflowMenuSheet -> {
                    val data = sheetUIState as AmityStoryModalSheetUIState.OpenOverflowMenuSheet
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                    ) {
                        AmityBottomSheetActionItem(
                            icon = R.drawable.amity_ic_delete_story,
                            text = "Delete story",
                            modifier = modifier.testTag("bottom_sheet_delete_button"),
                        ) {
                            viewModel.updateSheetUIState(AmityStoryModalSheetUIState.CloseSheet)
                            viewModel.updateDialogUIState(
                                AmityStoryModalDialogUIState.OpenConfirmDeleteDialog(data.storyId)
                            )
                        }
                    }
                }

                AmityStoryModalSheetUIState.CloseSheet -> {}
            }
        }
    }
}