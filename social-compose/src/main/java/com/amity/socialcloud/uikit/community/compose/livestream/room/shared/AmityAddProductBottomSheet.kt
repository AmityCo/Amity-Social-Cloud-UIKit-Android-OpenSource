package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.product.AmityProductTagSelectionComponent


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AmityAddProductBottomSheet(
    onDismiss: () -> Unit,
    onDone: (List<AmityProduct>) -> Unit = {},
    taggedProduct: List<AmityProduct>,
    isNetworkConnected: Boolean = true,
    pageScope: AmityComposePageScope?,
    requestFocus: Boolean = false,
) {
    val pendingSelectedProduct = remember { mutableStateListOf<AmityProduct>() }
    var showDiscardConfirmationDialog by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = bottomSheetState,
        containerColor = Color(0xFF191919),
        contentWindowInsets = { WindowInsets.navigationBars },
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        AmityProductTagSelectionComponent(
            selectedProductTags = emptyList(),
            onClose = {
                onDismiss.invoke()
            },
            onDone = onDone,
            maxSelection = 20 - taggedProduct.size,
            title = "Add products",
            pageScope = pageScope,
            showBottomBar = true,
            savedProducts = taggedProduct,
            isNetworkConnected = isNetworkConnected,
            onProductToggled = {
                pendingSelectedProduct.apply {
                    if (contains(it)) {
                        remove(it)
                    } else {
                        add(it)
                    }
                }
            },
            placeholder = R.drawable.amity_ic_product_tagging_placeholder,
            onDiscardRequest = {
                showDiscardConfirmationDialog = true
            },
            requestFocus = requestFocus
        )
    }

    if (showDiscardConfirmationDialog) {
        AmityAlertDialog(
            dialogTitle = "Discard product selection?",
            dialogText = "You have products selected that haven’t been added yet. If you close now, your selection will be lost.",
            confirmText = "Discard",
            dismissText = "Keep editing",
            onDismissRequest = {
                showDiscardConfirmationDialog = false
            },
            onConfirmation = {
                showDiscardConfirmationDialog = false
                onDismiss.invoke()
            },
        )
    }
}
