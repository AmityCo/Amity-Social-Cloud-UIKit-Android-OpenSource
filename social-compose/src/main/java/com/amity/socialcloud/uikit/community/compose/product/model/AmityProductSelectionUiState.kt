package com.amity.socialcloud.uikit.community.compose.product.model

import com.amity.socialcloud.sdk.model.core.product.AmityProduct

/**
 * UI state for the Product Selection screen.
 *
 * Source of truth lives in [AmityProductSelectionViewModel]. The composable layer should only
 * render this state and forward user intents back to the ViewModel.
 */
data class AmityProductSelectionUiState(
    val keyword: String = "",
    val maxSelection: Int,
    val selectedProducts: List<AmityProduct> = emptyList(), // user currently select these products
    val savedProducts: List<AmityProduct> = emptyList() // user already select the products before opening this dialog
) {
    val canSelectMore: Boolean get() = selectedProducts.size < maxSelection
}
