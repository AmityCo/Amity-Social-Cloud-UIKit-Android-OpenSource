package com.amity.socialcloud.uikit.community.compose.comment

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityStoryCommentBottomSheet(
    modifier: Modifier = Modifier,
    storyId: String,
    shouldAllowInteraction: Boolean,
    shouldAllowComment: Boolean,
    onClose: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier,
    ) {
        AmityCommentTrayComponent(
            storyId = storyId,
            shouldAllowInteraction = shouldAllowInteraction,
            shouldAllowComment = shouldAllowComment,
        )
    }
}

@Preview
@Composable
fun AmityStoryCommentBottomSheetPreview() {
    AmityStoryCommentBottomSheet(
        storyId = "",
        shouldAllowInteraction = true,
        shouldAllowComment = false,
    )
}