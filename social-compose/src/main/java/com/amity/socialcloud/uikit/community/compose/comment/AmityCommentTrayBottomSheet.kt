package com.amity.socialcloud.uikit.community.compose.comment

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityStoryCommentBottomSheet(
    modifier: Modifier = Modifier,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    shouldAllowInteraction: Boolean,
    shouldAllowComment: Boolean,
    onClose: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier,
    ) {
        AmityCommentTrayComponent(
            referenceId = referenceId,
            referenceType = referenceType,
            shouldAllowInteraction = shouldAllowInteraction,
            shouldAllowCreation = shouldAllowComment,
        )
    }
}

@Preview
@Composable
fun AmityStoryCommentBottomSheetPreview() {
    AmityStoryCommentBottomSheet(
        referenceId = "",
        referenceType = AmityCommentReferenceType.STORY,
        shouldAllowInteraction = true,
        shouldAllowComment = false,
    )
}