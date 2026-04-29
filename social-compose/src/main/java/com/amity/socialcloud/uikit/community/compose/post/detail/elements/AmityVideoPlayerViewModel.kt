package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.orEmpty

class AmityVideoPlayerViewModel : AmityBaseViewModel() {

    private val _taggedProducts = MutableStateFlow<List<AmityProduct>?>(null)
    val taggedProducts = _taggedProducts.asStateFlow()

    private val _isProductCatalogueEnabled = MutableStateFlow(false)
    val isProductCatalogueEnabled = _isProductCatalogueEnabled.asStateFlow()

    fun fetchProductCatalogueSettings() {
        AmityCoreClient.getProductCatalogueSetting()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { settings ->
                _isProductCatalogueEnabled.update {
                    settings.enabled
                }
            }
            .subscribe()
            .let(::addDisposable)
    }

    fun addTaggedProducts(
        postId: String,
        currentProducts: List<AmityProduct>,
        newProducts: List<AmityProduct>,
    ) {
        val currentTags = currentProducts.map {
            AmityProductTag.Media(it.getProductId())
        }
        val newTags = newProducts.map {
            AmityProductTag.Media(it.getProductId())
        }

        AmitySocialClient.newPostRepository()
            .editPost(postId = postId)
            .taggedProducts(
                productTags = (currentTags + newTags).distinct(),
            )
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = "Failed to add product tags. Please try again."
                )
            }
            .doOnComplete {
                val expectedSize = currentTags.size + newProducts.size
                val actualSize = currentProducts.size + newProducts.size
                if (expectedSize > actualSize) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        offsetFromBottom = 50,
                        message = "Some products that you've tagged are no longer available."
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        offsetFromBottom = 50,
                        message = "Product tags added."
                    )
                }
                _taggedProducts.value = currentProducts + newProducts
            }
            .subscribe()
    }

    fun removeTaggedProduct(
        postId: String,
        productId: String,
        currentProducts: List<AmityProduct>,
    ) {
        val remainingTags = currentProducts
            .filter { it.getProductId() != productId }
            .map { AmityProductTag.Media(it.getProductId()) }

        AmitySocialClient.newPostRepository()
            .editPost(
                postId = postId,
            )
            .taggedProducts(
                productTags = remainingTags,
            )
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = "Failed to remove product tag. Please try again."
                )
            }
            .doOnComplete {
                _taggedProducts.value = currentProducts.filter { it.getProductId() != productId }
                AmityUIKitSnackbar.publishSnackbarMessage(
                    offsetFromBottom = 50,
                    message = "Product tag removed."
                )
            }
            .subscribe()
    }
}
