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
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.ManageProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider


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
    isHost: Boolean = false
) {
    val productTaggingSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    // Establish the manage_product_tag_list component theme around the sheet itself so that
    // the ModalBottomSheet's containerColor (which paints behind the drag handle) resolves to
    // the same background as the content. Without this the container color falls back to the
    // caller's theme, showing a white drag-handle strip over the dark sheet.
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "manage_product_tag_list",
    ) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = onDismiss,
            sheetState = productTaggingSheetState,
            containerColor = AmityTheme.colors.background,
            contentColor = AmityTheme.colors.baseInverse,
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
                        onProductViewed = onProductViewed,
                        isHost = isHost
                    )

                    LaunchedEffect(isNetworkConnected) {
                        if (!isNetworkConnected) {
                            getComponentScope().showProgressSnackbar(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_waiting_network_toast"))
                        } else {
                            getComponentScope().dismissSnackbar()
                        }
                    }
                }
            }
        }
    }
}