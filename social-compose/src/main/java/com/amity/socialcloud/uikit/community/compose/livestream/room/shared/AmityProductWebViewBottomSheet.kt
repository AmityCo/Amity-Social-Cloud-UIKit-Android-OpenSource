package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.sdk.model.core.product.AmityProduct


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AmityProductWebViewBottomSheet(
    product: AmityProduct,
    onDismiss: () -> Unit,
) {
    val productUrl = product.getProductUrl()
    if (productUrl.isBlank()) {
        onDismiss()
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        containerColor = Color.White,
        contentWindowInsets = { WindowInsets.navigationBars },
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        AmityProductWebViewComponent(
            product = product,
            onDismiss = onDismiss,
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}