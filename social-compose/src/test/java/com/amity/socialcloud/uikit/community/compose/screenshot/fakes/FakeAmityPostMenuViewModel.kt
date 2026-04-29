package com.amity.socialcloud.uikit.community.compose.screenshot.fakes

import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuDialogUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Fake [AmityPostMenuViewModel] — default state: sheet closed, dialog closed, no permissions.
 */
class FakeAmityPostMenuViewModel(
    sheetState: AmityPostMenuSheetUIState = AmityPostMenuSheetUIState.CloseSheet,
    dialogState: AmityPostMenuDialogUIState = AmityPostMenuDialogUIState.CloseDialog,
) : AmityPostMenuViewModel() {

    override val sheetUIState: MutableStateFlow<AmityPostMenuSheetUIState> =
        MutableStateFlow(sheetState)

    override val dialogUIState: MutableStateFlow<AmityPostMenuDialogUIState> =
        MutableStateFlow(dialogState)

    override val hasDeleteCommunityPostPermission: StateFlow<Boolean> =
        MutableStateFlow(false).asStateFlow()

    override val hasDeleteUserFeedPostPermission: StateFlow<Boolean> =
        MutableStateFlow(false).asStateFlow()
}
