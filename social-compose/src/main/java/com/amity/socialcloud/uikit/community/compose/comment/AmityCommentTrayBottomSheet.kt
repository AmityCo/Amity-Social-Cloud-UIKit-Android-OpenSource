package com.amity.socialcloud.uikit.community.compose.comment

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityStoryCommentBottomSheet(
    modifier: Modifier = Modifier,
    reference: AmityComment.Reference,
    shouldAllowInteraction: Boolean,
    shouldAllowComment: Boolean,
    onClose: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        windowInsets = WindowInsets(top = 54.dp),
        modifier = modifier,
    ) {
        AmityCommentTrayComponent(
            reference = reference,
            shouldAllowInteraction = shouldAllowInteraction,
            shouldAllowComment = shouldAllowComment,
        )
    }
}

@Preview
@Composable
fun AmityStoryCommentBottomSheetPreview() {
    AmityStoryCommentBottomSheet(
        reference = AmityComment.Reference.STORY(""),
        shouldAllowInteraction = true,
        shouldAllowComment = false,
    )
}