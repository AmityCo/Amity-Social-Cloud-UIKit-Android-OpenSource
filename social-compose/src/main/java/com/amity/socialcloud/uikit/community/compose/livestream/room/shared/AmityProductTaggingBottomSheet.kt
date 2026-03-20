package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.ManageProductTagListComponent


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AmityProductTaggingBottomSheet(
    onDismiss: () -> Unit,
    onPinProduct: (String?) -> Unit = {},
    onRemoveProduct: (String) -> Unit = {},
    onAddProducts: () -> Unit = {},
    onProductClick: (AmityProduct, String) -> Unit = { _, _ -> },
    onProductViewed: (AmityProduct, String) -> Unit = { _, _ -> },
    canManageProducts: Boolean = false,
    taggedProducts: List<AmityProduct>,
    pinnedProductId: String?,
    isPostLive: Boolean = false,
    pageScope: AmityComposePageScope?,
    isNetworkConnected: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
) {
    val productTaggingSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = productTaggingSheetState,
        containerColor = Color(0xFF191919),
        contentColor = Color.White,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            AmityBaseComponent(
                modifier = Modifier,
                pageScope = pageScope,
                componentId = "manage_product_tag_list",
                needScaffold = true
            ) {
                ManageProductTagListComponent(
                    taggedProducts = taggedProducts,
                    pinnedProductId = pinnedProductId,
                    onDismiss = onDismiss,
                    onRemoveProduct = onRemoveProduct,
                    onPinProduct = onPinProduct,
                    componentScope = getComponentScope(),
                    onAddProducts = onAddProducts,
                    modifier = Modifier.fillMaxWidth(),
                    onProductClick = onProductClick,
                    canManageProducts = canManageProducts,
                    isPostLive = isPostLive,
                    onProductViewed = onProductViewed
                )

                LaunchedEffect(isNetworkConnected) {
                    if (!isNetworkConnected) {
                        getComponentScope().showProgressSnackbar("Waiting for network...")
                    } else {
                        getComponentScope().dismissSnackbar()
                    }
                }
            }
        }
    }
}