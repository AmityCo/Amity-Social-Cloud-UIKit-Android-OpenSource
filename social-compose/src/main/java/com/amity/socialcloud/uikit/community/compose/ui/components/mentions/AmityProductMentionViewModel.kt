package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for product mention suggestions.
 *
 * Intentionally reuses the same search implementation as
 * [com.amity.socialcloud.uikit.community.compose.product.AmityProductSelectionViewModel].
 */
class AmityProductMentionViewModel : AmityBaseViewModel() {

    fun searchProducts(keyword: String): Flow<PagingData<AmityProduct>> {
        return AmityCoreClient
            .newProductRepository()
            .searchProduct(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
}
