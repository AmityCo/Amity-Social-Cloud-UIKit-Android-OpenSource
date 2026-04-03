package com.amity.socialcloud.uikit.community.compose.paging.feed.user

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityMediaPostShimmer
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.getProductTagCount
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityImageFeedContainer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityBlockedUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityEmptyUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityPrivateUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUnknownUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUserFeedType
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel.PostListState

// Helper function to get image data and product tag count from a post
// Handles both cases:
// 1. Parent posts with IMAGE children (from feed queries)
// 2. Child posts that are IMAGE directly (from getPosts with dataTypes filter)
private fun getImageDataAndProductTagCount(post: AmityPost?): Triple<AmityPost.Data.IMAGE?, String?, Int> {
    if (post == null) return Triple(null, null, 0)

    // Check if this post itself is an IMAGE post (child post case)
    val postData = post.getData()
    if (postData is AmityPost.Data.IMAGE) {
        val productTagCount = getProductTagCount(post)
        return Triple(postData, post.getPostId(), productTagCount)
    }

    // Otherwise, look for IMAGE in children (parent post case)
    val firstChild = post.getChildren().firstOrNull { it.getData() is AmityPost.Data.IMAGE }
    val imageData = firstChild?.getData() as? AmityPost.Data.IMAGE
    val childPostId = firstChild?.getPostId()
    val productTagCount = firstChild?.let { getProductTagCount(it) } ?: 0

    return Triple(imageData, childPostId, productTagCount)
}

// Helper function to get parentPostId
private fun getParentPostId(post: AmityPost?): String? {
    if (post == null) return null

    // If it's already a child post (IMAGE), get its parent
    if (post.getData() is AmityPost.Data.IMAGE) {
        return post.getParentPostId()
    }

    // Otherwise it's a parent post
    return post.getPostId()
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

fun LazyListScope.amityUserImageFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    imagePosts: LazyPagingItems<AmityPost>,
    postListState: PostListState,
    isBlockedByMe: Boolean,
    onPostClick: ((String) -> Unit)? = null,
) {
    if (isBlockedByMe) {
        item {
            AmityBaseComponent(
                pageScope = pageScope,
                componentId = "user_image_feed"
            ) {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityBlockedUserFeed(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        feedType = AmityUserFeedType.IMAGE,
                    )
                }
            }
        }
    } else {
        when (postListState) {
            PostListState.EMPTY -> {
                item {
                    AmityBaseComponent(
                        pageScope = pageScope,
                        componentId = "user_image_feed"
                    ) {
                        Box(modifier = Modifier.height(480.dp)) {
                            AmityEmptyUserFeed(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                feedType = AmityUserFeedType.IMAGE
                            )
                        }
                    }
                }
            }

            PostListState.ERROR -> {
                item {
                    val error = (imagePosts.loadState.mediator?.refresh as? LoadState.Error)
                    val amityError = AmityError.from(error?.error)

                    if (
                        amityError == AmityError.UNAUTHORIZED_ERROR ||
                        amityError == AmityError.PERMISSION_DENIED
                    ) {
                        AmityBaseComponent(
                            pageScope = pageScope,
                            componentId = "user_image_feed"
                        ) {
                            Box(modifier = Modifier.height(480.dp)) {
                                AmityPrivateUserFeed(
                                    pageScope = pageScope,
                                    componentScope = getComponentScope(),
                                    feedType = AmityUserFeedType.IMAGE
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.height(480.dp)) {
                            AmityUnknownUserFeed()
                        }
                    }
                }
            }

            PostListState.LOADING -> {
                item {
                    Spacer(modifier.height(16.dp))
                }

                items(4) {
                    AmityMediaPostShimmer()
                }
            }

            PostListState.SUCCESS -> {
                item {
                    // Debounced availablePostIds to prevent excessive updates
                    var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }

                    // Calculate current post IDs
                    val currentPostIds = (0 until imagePosts.itemCount).mapNotNull { index ->
                        imagePosts[index]?.getPostId()
                    }.toSet()

                    // Debounce the updates with a 300ms delay
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
                                        Log.e("UserImageFeed", "Error fetching products", error)
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

                    AmityImageFeedContainer(
                        availablePostIds = debouncedAvailablePostIds,
                        onProductTagClick = { clickedPostId ->
                            // When clicking from preview dialog, find the post by postId
                            val post = (0 until imagePosts.itemCount).mapNotNull { index ->
                                val item = imagePosts[index]
                                when {
                                    // Case 1: Direct IMAGE post (child post)
                                    item?.getPostId() == clickedPostId -> item
                                    // Case 2: Parent post containing IMAGE child
                                    item?.getChildren()?.any { child -> child.getPostId() == clickedPostId } == true -> item
                                    else -> null
                                }
                            }.firstOrNull()
                            post?.let { onProductTagClick(it) }
                        }
                    ) { openDialog ->
                        Column {
                            Spacer(modifier.height(12.dp))

                            /*  count calculation logic
                             *  showing 2 items in a row
                             *  check if the item count is even or odd
                             *  if even, show 2 items in a row
                             *  if odd, show last item in a new row
                             */
                            (0 until (imagePosts.itemCount / 2 + imagePosts.itemCount % 2)).forEach { index ->
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
                                    // Get image data, postId and product tag count
                                    val (firstImageData, firstPostId, firstProductTagCount) = getImageDataAndProductTagCount(firstPost)

                                    AmityProfileImageFeedItem(
                                        modifier = modifier.weight(1f),
                                        data = firstImageData,
                                        postId = firstPostId,
                                        parentPostId = getParentPostId(firstPost),
                                        isPostCreator = firstPost?.getCreatorId() == AmityCoreClient.getUserId(),
                                        productTagCount = firstProductTagCount,
                                        onPostClick = onPostClick,
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
                                    // Get image data, postId and product tag count
                                    val (secondImageData, secondPostId, secondProductTagCount) = getImageDataAndProductTagCount(secondPost)

                                    //  show image thumbnail if index is valid
                                    //  if not, it's one last item in a new row and show empty box
                                    if (isSecondImageIndexValid) {
                                        AmityProfileImageFeedItem(
                                            modifier = modifier.weight(1f),
                                            data = secondImageData,
                                            postId = secondPostId,
                                            parentPostId = getParentPostId(secondPost),
                                            isPostCreator = secondPost?.getCreatorId() == AmityCoreClient.getUserId(),
                                            productTagCount = secondProductTagCount,
                                            onPostClick = onPostClick,
                                            onProductTagClick = { secondPost?.let { onProductTagClick(it) } },
                                            openDialog = openDialog
                                        )
                                    } else {
                                        Box(modifier = modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}