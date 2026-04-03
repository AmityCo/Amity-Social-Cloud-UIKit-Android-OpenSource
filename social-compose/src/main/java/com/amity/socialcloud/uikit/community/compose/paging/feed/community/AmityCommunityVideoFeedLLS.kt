package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyVideoFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileVideoFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityVideoFeedContainer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay


fun LazyListScope.amityCommunityVideoFeedLLS(
    modifier: Modifier = Modifier,
    videoPosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
) {
    item {
        var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }

        val currentPostIds = (0 until videoPosts.itemCount).mapNotNull { index ->
            videoPosts[index]?.getPostId()
        }.toSet()

        LaunchedEffect(currentPostIds) {
            delay(300) // Wait 300ms before updating
            debouncedAvailablePostIds = currentPostIds
        }

        // State for product tag bottom sheet
        var showProductTagSheet by remember { mutableStateOf(false) }
        var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
        val disposables = remember { CompositeDisposable() }
        val context = LocalContext.current
        val behavior = AmitySocialBehaviorHelper.globalBehavior
        var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }

        DisposableEffect(Unit) {
            onDispose {
                disposables.clear()
            }
        }

        // Callback for product tag badge click
        val onProductTagClick: (AmityPost) -> Unit = { post ->
            val productIds = getProductIdsFromPost(post)
            if (productIds.isNotEmpty()) {
                val disposable = Observable.fromIterable(productIds)
                    .flatMapSingle { productId ->
                        AmityCoreClient.newProductRepository()
                            .getProduct(productId)
                            .firstOrError()
                    }
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { products ->
                            selectedProducts = products
                            showProductTagSheet = true
                        },
                        { error ->
                            Log.e("CommunityVideoFeed", "Error fetching products", error)
                        }
                    )
                disposables.add(disposable)
            }
        }

        // Product tag bottom sheet
        if (showProductTagSheet && selectedProducts.isNotEmpty()) {
            AmityProductTagListComponent(
                productTags = selectedProducts,
                renderMode = RenderModeEnum.VIDEO,
                onDismiss = { showProductTagSheet = false },
                onProductClick = { product ->
                    val handled = behavior.onPostProductTagClick(
                        AmityGlobalBehavior.Context(
                            pageContext = context,
                            product = product,
                        )
                    )
                    if (!handled) {
                        selectedProduct = product
                    }
                },
            )
        }

        // WebView bottom sheet opened via event bus
        selectedProduct?.let { p ->
            AmityProductWebViewBottomSheet(
                product = p,
                onDismiss = {
                    selectedProduct = null
                }
            )
        }

        AmityVideoFeedContainer(
            availablePostIds = debouncedAvailablePostIds
        ) { openDialog ->
            AmityCommunityVideoFeedContent(
                modifier = modifier,
                videoPosts = videoPosts,
                onViewPost = onViewPost,
                openDialog = openDialog,
                onProductTagClick = onProductTagClick
            )
        }
    }
}

@Composable
private fun AmityCommunityVideoFeedContent(
    modifier: Modifier = Modifier,
    videoPosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
    openDialog: (AmityPost, onBottomSheetRequest: (() -> Unit)?) -> Unit,
    onProductTagClick: (AmityPost) -> Unit = {}
) {
    Column {
        Spacer(modifier.height(12.dp))

        /*  count calculation logic
         *  showing 2 items in a row
         *  check if the item count is even or odd
         *  if even, show 2 items in a row
         *  if odd, show last item in a new row
         */
        for (index in 0 until (videoPosts.itemCount / 2 + videoPosts.itemCount % 2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .aspectRatio(2f)
            ) {
                val isFirstVideoIndexValid =
                    index * 2 < videoPosts.itemCount && index >= 0

                if (isFirstVideoIndexValid) {
                    val firstVideo = videoPosts[index * 2]
                    if (firstVideo == null) {
                        Box(modifier.weight(1f))
                    } else {
                        val productTagCount = firstVideo.getProductTags().size
                        AmityProfileVideoFeedItem(
                            modifier = modifier.weight(1f),
                            data = firstVideo.getData() as AmityPost.Data.VIDEO,
                            postId = firstVideo.getPostId(), // For deletion detection
                            parentPostId = firstVideo.getParentPostId(), // For navigation
                            productTagCount = productTagCount,
                            onPostClick = { postId ->
                                onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                            },
                            onProductTagClick = { firstVideo.let { onProductTagClick(it) } },
                            onClick = {
                                openDialog(firstVideo) {
                                    // View original post callback
                                    firstVideo.getParentPostId()?.let { parentPostId ->
                                        onViewPost?.invoke(parentPostId, AmityPostCategory.GENERAL)
                                    }
                                }
                            }
                        )
                    }
                } else {
                    Box(modifier.weight(1f))
                }

                val isSecondVideoIndexValid =
                    index * 2 + 1 < videoPosts.itemCount && index >= 0

                if (isSecondVideoIndexValid) {
                    val secondVideo = videoPosts.peek(index * 2 + 1)
                    if (secondVideo == null) {
                        Box(modifier = modifier.weight(1f))
                    } else {
                        val productTagCount = secondVideo.getProductTags().size
                        AmityProfileVideoFeedItem(
                            modifier = modifier.weight(1f),
                            data = secondVideo.getData() as AmityPost.Data.VIDEO,
                            postId = secondVideo.getPostId(), // For deletion detection
                            parentPostId = secondVideo.getParentPostId(), // For navigation
                            productTagCount = productTagCount,
                            onPostClick = { postId ->
                                onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                            },
                            onProductTagClick = { secondVideo.let { onProductTagClick(it) } },
                            onClick = {
                                openDialog(secondVideo) {
                                    // View original post callback
                                    secondVideo.getParentPostId()?.let { parentPostId ->
                                        onViewPost?.invoke(parentPostId, AmityPostCategory.GENERAL)
                                    }
                                }
                            }
                        )
                    }
                } else {
                    Box(modifier = modifier.weight(1f))
                }
            }
        }

        if (videoPosts.itemCount == 0) {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyVideoFeed()
            }
        }
    }
}

// Helper function to get product IDs from a post
private fun getProductIdsFromPost(post: AmityPost): List<String> {
    return post.getProductTags()
        .filterIsInstance<AmityProductTag.Media>()
        .map { it.productId }
}
