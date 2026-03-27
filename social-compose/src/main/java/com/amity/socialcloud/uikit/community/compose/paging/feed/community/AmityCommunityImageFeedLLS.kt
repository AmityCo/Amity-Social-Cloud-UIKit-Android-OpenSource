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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyImageFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityImageFeedContainer
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.getProductTagCount
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet

// Helper function to get image data and product tag count from a post
// Handles both cases:
// 1. Parent posts with IMAGE children (from feed queries)
// 2. Child posts that are IMAGE directly (from getPosts with dataTypes filter)
private fun getImageDataAndProductTagCount(post: AmityPost?): Pair<AmityPost.Data.IMAGE?, Int> {
    if (post == null) return Pair(null, 0)

    // Check if this post itself is an IMAGE post (child post case)
    val postData = post.getData()
    if (postData is AmityPost.Data.IMAGE) {
        val productTagCount = getProductTagCount(post)
        return Pair(postData, productTagCount)
    }

    // Otherwise, look for IMAGE in children (parent post case)
    val firstChild = post.getChildren().firstOrNull { it.getData() is AmityPost.Data.IMAGE }
    val imageData = firstChild?.getData() as? AmityPost.Data.IMAGE
    val productTagCount = firstChild?.let { getProductTagCount(it) } ?: 0

    return Pair(imageData, productTagCount)
}

// Helper function to get productIds from a post
private fun getProductIdsFromPost(post: AmityPost?): List<String> {
    if (post == null) return emptyList()

    // Check if this post itself is an IMAGE post (child post case)
    val postData = post.getData()
    if (postData is AmityPost.Data.IMAGE) {
        return post.getProductTags()
            .filterIsInstance<AmityProductTag.Media>()
            .map { it.productId }
    }

    // Otherwise, look for IMAGE in children (parent post case)
    val firstChild = post.getChildren().firstOrNull { it.getData() is AmityPost.Data.IMAGE }
    return firstChild?.getProductTags()
        ?.filterIsInstance<AmityProductTag.Media>()
        ?.map { it.productId }
        ?: emptyList()
}

fun LazyListScope.amityCommunityImageFeedLLS(
    modifier: Modifier = Modifier,
    imagePosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
) {
    item {

        var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }

        val currentPostIds = (0 until imagePosts.itemCount).mapNotNull { index ->
            imagePosts[index]?.getPostId()
        }.toSet()

        LaunchedEffect(currentPostIds) {
            delay(300)
            debouncedAvailablePostIds = currentPostIds
        }

        // State for product tag bottom sheet
        var showProductTagSheet by remember { mutableStateOf(false) }
        var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
        var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }
        val disposables = remember { CompositeDisposable() }

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
                            Log.e("CommunityImageFeed", "Error fetching products", error)
                        }
                    )
                disposables.add(disposable)
            }
        }

        // Product tag bottom sheet
        if (showProductTagSheet && selectedProducts.isNotEmpty()) {
            AmityProductTagListComponent(
                productTags = selectedProducts,
                renderMode = RenderModeEnum.IMAGE,
                onDismiss = { showProductTagSheet = false },
                onProductClick = { product -> selectedProduct = product }
            )
        }

        selectedProduct?.let { product ->
            AmityProductWebViewBottomSheet(
                product = product,
                onDismiss = { selectedProduct = null }
            )
        }

        AmityImageFeedContainer(
            availablePostIds = debouncedAvailablePostIds,
            onProductTagClick = { clickedPostId ->
                // When clicking from preview dialog, we need to find the parent post
                // The dialog passes child image post ID, so we need to map back to parent post
                val post = (0 until imagePosts.itemCount).mapNotNull { index ->
                    val item = imagePosts[index]
                    // Check both child posts (direct IMAGE posts) and parent posts
                    when {
                        // Case 1: Direct IMAGE post (child post from filtered query)
                        item?.getPostId() == clickedPostId -> item
                        // Case 2: Parent post containing IMAGE child
                        item?.getChildren()?.any { child -> child.getPostId() == clickedPostId } == true -> item
                        else -> null
                    }
                }.firstOrNull()
                post?.let { onProductTagClick(it) }
            }
        ) { openDialog ->
            AmityCommunityImageFeedContent(
                modifier = modifier,
                imagePosts = imagePosts,
                onViewPost = onViewPost,
                openDialog = openDialog,
                onProductTagClick = onProductTagClick
            )
        }
    }
}

@Composable
private fun AmityCommunityImageFeedContent(
    modifier: Modifier = Modifier,
    imagePosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
    openDialog: (AmityImage?, String?, Int, onBottomSheetRequest: (() -> Unit)?) -> Unit,
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
        for (index in 0 until (imagePosts.itemCount / 2 + imagePosts.itemCount % 2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .aspectRatio(2f)
            ) {
                //  check first image index is valid
                val isFirstImageIndexValid =
                    index * 2 < imagePosts.itemCount && index >= 0
                val firstPost = if (isFirstImageIndexValid) {
                    imagePosts[index * 2]
                } else {
                    null
                }
                // Get image data and product tag count
                val (firstImageData, firstProductTagCount) = getImageDataAndProductTagCount(firstPost)
                // Determine postId and parentPostId based on whether it's a child or parent post
                val firstPostId = if (firstPost?.getData() is AmityPost.Data.IMAGE) {
                    firstPost.getPostId() // It's already a child post
                } else {
                    firstPost?.getChildren()?.firstOrNull()?.getPostId()
                }
                val firstParentPostId = if (firstPost?.getData() is AmityPost.Data.IMAGE) {
                    firstPost.getParentPostId() // Get parent from child post
                } else {
                    firstPost?.getPostId() // It's the parent post itself
                }

                AmityProfileImageFeedItem(
                    modifier = modifier.weight(1f),
                    data = firstImageData,
                    postId = firstPostId,
                    parentPostId = firstParentPostId,
                    isPostCreator = firstPost?.getCreatorId() == AmityCoreClient.getUserId(),
                    productTagCount = firstProductTagCount,
                    onPostClick = { postId ->
                        onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                    },
                    onProductTagClick = { firstPost?.let { onProductTagClick(it) } },
                    openDialog = openDialog
                )

                //  check second image index is valid
                val isSecondImageIndexValid =
                    index * 2 + 1 < imagePosts.itemCount && index >= 0
                val secondPost = if (isSecondImageIndexValid) {
                    imagePosts[index * 2 + 1]
                } else {
                    null
                }
                // Get image data and product tag count
                val (secondImageData, secondProductTagCount) = getImageDataAndProductTagCount(secondPost)
                // Determine postId and parentPostId based on whether it's a child or parent post
                val secondPostId = if (secondPost?.getData() is AmityPost.Data.IMAGE) {
                    secondPost.getPostId() // It's already a child post
                } else {
                    secondPost?.getChildren()?.firstOrNull()?.getPostId()
                }
                val secondParentPostId = if (secondPost?.getData() is AmityPost.Data.IMAGE) {
                    secondPost.getParentPostId() // Get parent from child post
                } else {
                    secondPost?.getPostId() // It's the parent post itself
                }

                //  show image thumbnail if index is valid
                //  if not, it's one last item in a new row and show empty box
                if (isSecondImageIndexValid) {
                    AmityProfileImageFeedItem(
                        modifier = modifier.weight(1f),
                        data = secondImageData,
                        postId = secondPostId,
                        parentPostId = secondParentPostId,
                        isPostCreator = secondPost?.getCreatorId() == AmityCoreClient.getUserId(),
                        productTagCount = secondProductTagCount,
                        onPostClick = { postId ->
                            onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                        },
                        onProductTagClick = { secondPost?.let { onProductTagClick(it) } },
                        openDialog = openDialog
                    )
                } else {
                    Box(modifier = modifier.weight(1f))
                }
            }
        }

        if (imagePosts.itemCount == 0) {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyImageFeed()
            }
        }
    }
}