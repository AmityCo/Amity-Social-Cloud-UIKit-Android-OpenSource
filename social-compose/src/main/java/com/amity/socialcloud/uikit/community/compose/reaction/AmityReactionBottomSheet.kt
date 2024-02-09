package com.amity.socialcloud.uikit.community.compose.reaction

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityReactionListBottomSheet(
    modifier: Modifier = Modifier,
    referenceType: AmityReactionReferenceType,
    referenceId: String,
    onClose: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        AmityReactionListPage(
            referenceType = referenceType,
            referenceId = referenceId,
        )
    }
}

@Preview
@Composable
fun AmityReactionBottomSheetPreview() {
    AmityReactionListBottomSheet(
        referenceType = AmityReactionReferenceType.COMMENT,
        referenceId = "",
    )
}
