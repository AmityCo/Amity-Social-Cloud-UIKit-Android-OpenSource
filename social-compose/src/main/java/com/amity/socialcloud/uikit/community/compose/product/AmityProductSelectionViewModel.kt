package com.amity.socialcloud.uikit.community.compose.product

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.product.model.AmityProductSelectionUiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

open class AmityProductSelectionViewModel(
    selectedProducts: List<AmityProduct> = emptyList(),
    savedProducts: List<AmityProduct> = emptyList(), // To filtered out equal name on livestream tagging
    maxSelection: Int,
) : AmityBaseViewModel() {

    private val _uiState = MutableStateFlow(AmityProductSelectionUiState(
        maxSelection = maxSelection,)
    )
    val uiState: StateFlow<AmityProductSelectionUiState> get() = _uiState.asStateFlow()

    private val keywordFlow = MutableStateFlow("")

    /**
     * Paging flow used by the UI to render search results.
     *
     * This updates automatically whenever [onKeywordChange] is called.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedProducts: Flow<PagingData<AmityProduct>> = keywordFlow
        .flatMapLatest { keyword ->
            if (keyword.length >= MIN_KEYWORD_LENGTH) {
                searchProducts(keyword)
            } else {
                // Return empty results if keyword is too short.
                // Emit flow of empty paging data
                flow {
                    emit(PagingData.empty())
                }
            }
        }

    init {
        val initialKeyword = _uiState.value.keyword
        keywordFlow.value = initialKeyword

        _uiState.update { old ->
            old.copy(
                keyword = initialKeyword,
                selectedProducts = selectedProducts,
                maxSelection = maxSelection,
                savedProducts = savedProducts
            )
        }
    }

    /**
     * Sets selection from an external source (e.g. editing an existing post).
     *
     * Note: this overwrites current selection.
     */
    fun setSelectedProducts(products: List<AmityProduct>) {
        _uiState.update { old ->
            old.copy(
                selectedProducts = products,
            )
        }
    }

    fun setSavedProducts(products: List<AmityProduct>) {
        _uiState.update { old ->
            old.copy(savedProducts = products)
        }
    }

    fun onKeywordChange(keyword: String) {
        _uiState.update { current ->
            current.copy(keyword = keyword)
        }
        keywordFlow.value = keyword.trim()
    }

    fun toggleProduct(product: AmityProduct) {
        val id = product.getProductId()

        _uiState.update { old ->
            val nextProducts = if (old.selectedProducts.firstOrNull { it.getProductId() == id } != null) {
                old.selectedProducts.filterNot { it.getProductId() == id }
            } else {
                // Enforce max-selection.
                if (old.selectedProducts.size >= old.maxSelection) return
                old.selectedProducts.toMutableList()
                    .apply {
                        add(product)
                    }
            }
            old.copy(
                selectedProducts = nextProducts,
            )
        }
    }

    fun removeProduct(product: AmityProduct) {
        val id = product.getProductId()
        _uiState.update { old ->
            old.copy(selectedProducts = old.selectedProducts.filterNot { it.getProductId() == id })
        }
    }

    fun clearState() {
        _uiState.update { old ->
            old.copy(
                keyword = "",
                selectedProducts = emptyList()
            )
        }
        keywordFlow.value = ""
    }

    /**
     * Clears only the search keyword, preserving selected products.
     * Used when dismissing the component to reset search state.
     */
    fun clearKeyword() {
        _uiState.update { old ->
            old.copy(keyword = "")
        }
        keywordFlow.value = ""
    }

    private fun searchProducts(keyword: String): Flow<PagingData<AmityProduct>> {
        return searchProductsFlowable(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    private fun searchProductsFlowable(keyword: String): Flowable<PagingData<AmityProduct>> {
        return if(keyword.length >= MIN_KEYWORD_LENGTH) {
            AmityCoreClient
                .newProductRepository()
                .searchProduct(keyword)
        } else {
            Flowable.just(PagingData.empty())
        }
    }

    companion object {
        const val MIN_KEYWORD_LENGTH = 2
    }
}
