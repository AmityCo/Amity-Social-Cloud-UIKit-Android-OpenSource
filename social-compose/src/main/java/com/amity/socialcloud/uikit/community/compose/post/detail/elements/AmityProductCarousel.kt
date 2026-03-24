package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.core.engine.analytics.AnalyticsEventSourceType
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.common.utils.formatCurrencyForLocale
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus

private const val MAX_VISIBLE_PRODUCTS = 5

/**
 * Product carousel component for displaying tagged products in a horizontal list.
 * Shows up to 5 products with a "see more" button when there are more than 5 products.
 * Tracks product view and click analytics events.
 */
@Composable
fun AmityProductCarousel(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    post: AmityPost,
) {
    if (post.getChildren().any { it.getData() is AmityPost.Data.LIVE_STREAM || it.getData() is AmityPost.Data.ROOM }) return
    val postId = post.getPostId()
    // Collect all product IDs from parent and children posts
    val products = remember(post) {
        buildSet {
            // Add product tags from parent post
            post.getProductTags().forEach { tag ->
                tag.product?.let(::add)
            }
            // Add product tags from child posts
            post.getChildren().forEach { childPost ->
                childPost.getProductTags().forEach { tag ->
                    tag.product?.let(::add)
                }
            }
        }.toList()
    }

    // Skip rendering if no products
    if (products.isEmpty()) return

    var showAllProductsSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }

    // Skip rendering if no products fetched
    if (products.isEmpty()) return

    val visibleProducts = products.take(MAX_VISIBLE_PRODUCTS)
    val hasMoreProducts = products.size > MAX_VISIBLE_PRODUCTS

    val lazyRowState = rememberLazyListState()
    // rememberUpdatedState ensures the LaunchedEffect closure always sees the latest
    // visibleProducts without needing to restart the coroutine on recomposition.
    val currentVisibleProducts = rememberUpdatedState(visibleProducts)
    // Deduplicate view events — remember {} survives recomposition so this won't reset
    // on state changes like reaction counts, but will reset when the composable leaves and re-enters.
    val viewedProductIds = remember { mutableSetOf<String>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        // Header
        Text(
            text = "Products tagged",
            style = AmityTheme.typography.caption,
            color = AmityTheme.colors.baseShade1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // AmityBaseElement provides the element scope needed to call getConfigId()
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "product_tag"
        ) {
            // Track view events based on actual on-screen visibility, not composition
            LaunchedEffect(lazyRowState) {
                snapshotFlow { lazyRowState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        for (itemInfo in visibleItems) {
                            // index maps to visibleProducts; the "see more" button sits beyond that range
                            currentVisibleProducts.value.getOrNull(itemInfo.index)?.let { product ->
                                if (viewedProductIds.add(product.getProductId())) {
                                    product.analytics()
                                        .markAsViewed(
                                            sourceType = AnalyticsEventSourceType.POST,
                                            sourceId = postId,
                                            location = getConfigId()
                                        )
                                }
                            }
                        }
                    }
            }

            // Product carousel
            LazyRow(
                state = lazyRowState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(visibleProducts, key = { it.getProductId() }) { product ->
                    ProductCarouselCard(
                        product = product,
                        postId = postId,
                        pageScope = pageScope,
                        componentScope = componentScope,
                        onClick = {
                            selectedProduct = product
                        }
                    )
                }

                // Show "see more" button if there are more products
                if (hasMoreProducts) {
                    item {
                        SeeMoreButton(
                            onClick = { showAllProductsSheet = true }
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet for all products
    if (showAllProductsSheet) {
        AmityProductTagListComponent(
            pageScope = pageScope,
            productTags = products,
            postId = postId,
            onDismiss = { showAllProductsSheet = false },
            onProductClick = { product -> selectedProduct = product }
        )
    }

    // Bottom sheet for product web view
    selectedProduct?.let { product ->
        AmityProductWebViewBottomSheet(
            product = product,
            onDismiss = { selectedProduct = null }
        )
    }
}

/**
 * Card component for displaying a single product in the carousel.
 * Uses vertical layout with image on top and text below.
 * Tracks product view event when the card is displayed.
 */
@Composable
private fun ProductCarouselCard(
    product: AmityProduct,
    postId: String,
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    onClick: () -> Unit = {},
) {
    val cardWidth = 100.dp
    val imageSize = 100.dp
    val isUnavailable = product.isDeleted() || product.getStatus() == AmityProductStatus.ARCHIVED

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "product_tag"
    ) {

        Column(
            modifier = modifier
                .width(cardWidth)
                .then(
                    // Only make clickable if product is not archived/deleted
                    if (!isUnavailable) (Modifier.clickable {
                        // Track click event
                        product.analytics()
                            .markAsClicked(
                                sourceType = AnalyticsEventSourceType.POST,
                                sourceId = postId,
                                location = getConfigId()
                            )
                                onClick()
                    })
                    else Modifier
                ),
            horizontalAlignment = Alignment.Start
        ) {
            // Product thumbnail
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.getThumbnailUrl())
                        .build(),
                    contentDescription = product.getProductName(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
                    error = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
                    modifier = Modifier.fillMaxSize()
                )

                // Unavailable overlay
                if (isUnavailable) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xCCFFFFFF))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Unlisted label
            if (isUnavailable) {
                Text(
                    text = "Unlisted",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1,
                )
            }

            // Product name
            Text(
                text = product.getProductName(),
                style = AmityTheme.typography.bodyBold,
                color = if (isUnavailable) AmityTheme.colors.baseShade4
                        else AmityTheme.colors.base,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Price (hidden when unavailable)
            if (!isUnavailable) {
                Spacer(modifier = Modifier.height(2.dp))

                val formattedPrice = formatCurrencyForLocale(
                    context = LocalContext.current,
                    amount = product.getPrice(),
                    currencyCode = product.getCurrency()
                )

                Text(
                    text = formattedPrice,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
            }
        }
    }
}

/**
 * "See more" button displayed at the end of the carousel when there are more than 5 products.
 */
@Composable
private fun SeeMoreButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val imageSize = 100.dp
    val buttonSize = 32.dp

    Column(
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Container to align with product image height
        Box(
            modifier = Modifier
                .height(imageSize),
            contentAlignment = Alignment.BottomStart
        ) {
            // Circle button with arrow
            Box(
                modifier = Modifier
                    .size(buttonSize)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.baseShade4),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_chevron_right),
                    contentDescription = "See more products",
                    tint = AmityTheme.colors.base,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
