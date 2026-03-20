package com.amity.socialcloud.uikit.community.compose.livestream.room.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColor
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.formatCurrencyForLocale
import com.amity.socialcloud.uikit.community.compose.R
import org.joda.time.DateTime
import kotlin.math.max

@Composable
fun ManageProductTagListComponent(
    modifier: Modifier = Modifier,
    taggedProducts: List<AmityProduct>,
    maxCount: Int = 20,
    pinnedProductId: String? = null,
    componentScope: AmityComposeComponentScope? = null,
    onDismiss: () -> Unit,
    onRemoveProduct: (String) -> Unit,
    onPinProduct: (String?) -> Unit = {},
    onAddProducts: () -> Unit = {},
    onProductClick: (AmityProduct, String) -> Unit = { _, _ -> },
    onProductViewed: (AmityProduct, String) -> Unit = { _, _ -> },
    canManageProducts: Boolean = false,
    isPostLive: Boolean = false
) {
    val componentTheme = remember { componentScope?.getComponentTheme() }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = if (canManageProducts) "Tagged products" else "Products tagged",
                    color = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                    style = AmityTheme.typography.titleBold,
                )
                Spacer(Modifier.height(2.dp))
                if (canManageProducts) {
                    Text(
                        text = "${taggedProducts.size}/$maxCount",
                        color = componentTheme?.baseShade1Color?.asColor()
                            ?: AmityTheme.colors.baseShade1,
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.amity_ic_dismiss_preview),
                contentDescription = "Close",
                tint = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickableWithoutRipple {
                        onDismiss()
                    }
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = componentTheme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            taggedProducts.isEmpty() -> {
                TaggedProductsEmpty(
                    modifier = modifier,
                    onAddProducts = onAddProducts,
                    componentTheme = componentTheme
                )
            }

            else -> {
                TaggedProductsFilled(
                    taggedProducts = taggedProducts,
                    maxProductCount = maxCount,
                    pinnedProductId = pinnedProductId,
                    componentTheme = componentTheme,
                    onRemoveProduct = onRemoveProduct,
                    onPinProduct = onPinProduct,
                    onAddProducts = onAddProducts,
                    onProductClick = onProductClick,
                    canManageProducts = canManageProducts,
                    isPostLive = isPostLive,
                    onProductViewed = onProductViewed,
                    componentScope = componentScope
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TaggedProductsEmpty(
    modifier: Modifier,
    onAddProducts: () -> Unit,
    componentTheme: AmityUIKitConfig.UIKitTheme?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_no_tagged_product),
            contentDescription = null,
        )

        Spacer(modifier = modifier.size(8.dp))

        Text(
            text = stringResource(R.string.amity_v4_tagged_products_empty_title),
            color = componentTheme?.baseShade2Color?.asColor() ?: AmityTheme.colors.baseShade2,
            style = AmityTheme.typography.body.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.amity_v4_tagged_products_empty_desc),
            color = componentTheme?.baseShade2Color?.asColor() ?: AmityTheme.colors.baseShade2,
            style = AmityTheme.typography.body
        )

        Spacer(modifier = Modifier.height(16.dp))

        AddProductsButton(
            onAddProducts = onAddProducts,
            componentTheme = componentTheme,
        )
    }
}

@Composable
fun TaggedProductsFilled(
    componentTheme: AmityUIKitConfig.UIKitTheme?,
    taggedProducts: List<AmityProduct>,
    maxProductCount: Int,
    pinnedProductId: String? = null,
    onRemoveProduct: (String) -> Unit,
    onPinProduct: (String?) -> Unit,
    onAddProducts: () -> Unit,
    onProductClick: (AmityProduct, String) -> Unit = { _, _ -> },
    onProductViewed: (AmityProduct, String) -> Unit = { _, _ -> },
    canManageProducts: Boolean,
    isPostLive: Boolean,
    componentScope: AmityComposeComponentScope?
) {
    val pinnedProduct = if (isPostLive) null
    else pinnedProductId?.let { pinnedId ->
        taggedProducts.firstOrNull { it.getProductId() == pinnedId }
    }
    val unpinnedProducts = taggedProducts.filter { isPostLive || it.getProductId() != pinnedProductId }
    val productsListState = rememberLazyListState()

    LaunchedEffect(productsListState) {
        snapshotFlow { productsListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                // Triggered whenever the set of visible items changes
                for (itemInfo in visibleItems) {
                    // mark product as viewed
                    taggedProducts.getOrNull(itemInfo.index)?.let { product ->
                        componentScope?.let {
                            onProductViewed.invoke(product, componentScope.getConfigId().replaceFirst("*", "manage_product_tag"))
                        }
                    }
                }
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.Start,
            state = productsListState
        ) {
            // Pinned Product Section
            pinnedProduct?.let { product ->
                item {
                    Text(
                        text = stringResource(R.string.amity_v4_pinned_product_label),
                        style = AmityTheme.typography.titleBold,
                        color = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                item {
                    val product = taggedProducts.first { it.getProductId() == pinnedProductId }
                    ProductTagCard(
                        product = product,
                        onRemoveClick = { onRemoveProduct(product.getProductId()) },
                        isPinned = true,
                        onPinClick = { togglePinProduct(product.getProductId(), pinnedProductId, onPinProduct) },
                        componentTheme = componentTheme,
                        onProductClick = onProductClick,
                        canManageProducts = canManageProducts,
                        componentScope = componentScope,
                        isProductArchived = product.getStatus() == AmityProductStatus.ARCHIVED,
                    )
                }

                // Other Products Section - only show if there are unpinned products
                if (unpinnedProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.amity_v4_other_products_label),
                            style = AmityTheme.typography.titleBold,
                            color = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }

            // Unpinned Products List
            items(unpinnedProducts, key = { it.getProductId() }) { product ->
                ProductTagCard(
                    modifier = Modifier.padding(bottom = 10.dp),
                    product = product,
                    onRemoveClick = { onRemoveProduct(product.getProductId()) },
                    isPinned = false,
                    onPinClick = { togglePinProduct(product.getProductId(), pinnedProductId, onPinProduct) },
                    componentTheme = componentTheme,
                    onProductClick = { product, location ->
                        if (product.getStatus() != AmityProductStatus.ARCHIVED) {
                            onProductClick.invoke(product, location)
                        }
                    },
                    canManageProducts = canManageProducts,
                    isPostLive = isPostLive,
                    componentScope = componentScope,
                    isProductArchived = product.getStatus() == AmityProductStatus.ARCHIVED,
                )
            }
        }

        if (canManageProducts) {
            AddProductsButton(
                onAddProducts = onAddProducts,
                componentTheme = componentTheme,
                isEnabled = taggedProducts.size < maxProductCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 24.dp)
            )
        }
    }
}

@Composable
private fun ProductTagCard(
    modifier: Modifier = Modifier,
    componentTheme: AmityUIKitConfig.UIKitTheme?,
    product: AmityProduct,
    isPinned: Boolean,
    onRemoveClick: () -> Unit,
    onPinClick: () -> Unit,
    onProductClick: (AmityProduct, String) -> Unit = { _, _ -> },
    canManageProducts: Boolean,
    componentScope: AmityComposeComponentScope?,
    isPostLive: Boolean = false,
    isProductArchived: Boolean = false,
) {
    AmityBaseElement(componentScope = componentScope, elementId = "manage_product_tag") {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(!canManageProducts) {
                    onProductClick.invoke(product, getElementScope().getConfigId())
                },
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = componentTheme?.baseShade4Color?.asColor()
                            ?: AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.getThumbnailUrl())
                        .placeholder(R.drawable.amity_ic_product_tagging_placeholder)
                        .fallback(R.drawable.amity_ic_product_tagging_placeholder)
                        .error(R.drawable.amity_ic_product_tagging_placeholder)
                        .build(),
                    contentDescription = product.getProductName(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onProductClick(product, getElementScope().getConfigId())
                        }
                )
                if (isPinned) {
                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_product_tagging_pin_filled),
                            contentDescription = "pinned",
                            tint = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                            modifier = Modifier
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = "Pinned",
                            style = AmityTheme.typography.captionBold,
                            color = componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                        )
                    }
                }
                if (isProductArchived) {
                    // deleted overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {}
                }
            }

            // Information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    if (isProductArchived) {
                        Text(
                            text = stringResource(R.string.amity_v4_tagged_products_archived_info),
                            style = AmityTheme.typography.caption,
                            color = componentTheme?.baseShade2Color?.asColor()
                                ?: AmityTheme.colors.baseShade2,
                        )
                    }
                    // Product Name
                    Text(
                        text = product.getProductName(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.bodyBold,
                        color = if (isProductArchived) componentTheme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4
                            else componentTheme?.baseColor?.asColor() ?: AmityTheme.colors.base,
                        modifier = Modifier.clickable {
                            onProductClick(product, getElementScope().getConfigId())
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                    // Price and Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Price
                        if (product.getPrice() > 0.0) {
                            val formattedPrice = formatCurrencyForLocale(
                                context = LocalContext.current,
                                amount = product.getPrice(),
                                currencyCode = product.getCurrency()
                            )
                            Text(
                                text = formattedPrice,
                                style = AmityTheme.typography.body,
                                color = componentTheme?.baseShade1Color?.asColor()
                                    ?: AmityTheme.colors.baseShade3,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Box(Modifier.weight(1f)) {}
                        }

                        if (canManageProducts) {
                            DeleteProductButton(onRemoveClick = onRemoveClick)

                            // Pin Button is not visible for deleted products and in post-live state
                            if (!isProductArchived && !isPostLive) {
                                ProductActionButton(
                                    isPinned = isPinned,
                                    onPinClick = onPinClick
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(64.dp)
                                    .background(
                                        color = AmityTheme.colors.primary.copy(if (isProductArchived) 0.3f else 1f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable { onProductClick.invoke(product, getElementScope().getConfigId()) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "View",
                                    style = AmityTheme.typography.captionBold.copy(
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteProductButton(onRemoveClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .border(
                width = 1.dp,
                color = AmityTheme.colors.secondaryShade3,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onRemoveClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.amity_ic_delete_trash),
            contentDescription = "Delete product",
            modifier = Modifier.size(16.dp),
            colorFilter = ColorFilter.tint(Color(0xFFEBECEF))
        )
    }
}

@Composable
private fun ProductActionButton(
    isPinned: Boolean,
    onPinClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(6.dp)
            )
            .wrapContentWidth()
            .sizeIn(minWidth = 64.dp, minHeight = 28.dp, maxHeight = 28.dp)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onPinClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.amity_ic_product_tagging_pin_outlined),
            contentDescription = "pin toggle",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = if (isPinned) stringResource(R.string.amity_v4_unpin_label)
                   else stringResource(R.string.amity_v4_pin_label),
            style = AmityTheme.typography.captionBold,
            color = Color.White,
        )
    }
}

@Composable
private fun AddProductsButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onAddProducts: () -> Unit,
    componentTheme: AmityUIKitConfig.UIKitTheme?
) {
    val borderColor = if (isEnabled) {
        componentTheme?.baseInverseColor?.asColor() ?: AmityTheme.colors.baseInverse
    } else {
        componentTheme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4
    }
    val textColor = if (isEnabled) {
        componentTheme?.baseInverseColor?.asColor() ?: AmityTheme.colors.baseInverse
    } else {
        componentTheme?.baseShade3Color?.asColor() ?: AmityTheme.colors.baseShade3
    }
    OutlinedButton(
        modifier = modifier,
        onClick = onAddProducts,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
          borderColor
        ),
        enabled = isEnabled
    ) {
        Text(
            text = stringResource(R.string.amity_v4_tagged_products_empty_action),
            style = AmityTheme.typography.bodyBold.copy(
                color = textColor
            ),
        )
    }
}

private fun togglePinProduct(
    productId: String,
    currentPinnedProductId: String?,
    onPinProduct: (String?) -> Unit
) {
    val newPinnedId = if (productId == currentPinnedProductId) null else productId
    onPinProduct(newPinnedId)
}

@Preview()
@Composable
fun TaggedProductsSheetEmptyPreview() {
    ManageProductTagListComponent(
        taggedProducts = emptyList(),
        onDismiss = {},
        onRemoveProduct = {},
    )
}

/**
 * Livestream Product Tag Card - Viewer POV (Compact card displayed as overlay on livestream)
 * Shows pinned product with close button and view CTA button
 * Semi-transparent white background with blur effect
 * Figma: https://www.figma.com/design/VvkwVh7tAwxXeVu23zrEld/Live-Stream-Product-Tagging?node-id=475-38310
 */

@Preview()
@Composable
fun TaggedProductsSheetFilledPreview() {
    val sampleProducts = listOf(
        AmityProduct(
            productId = "prod1",
            productName = "Sample Product 1",
            price = 29.99,
            currency = "$",
            thumbnailUrl = "https://via.placeholder.com/150",
            status = AmityProductStatus.ACTIVE,
            thumbnailMode = null,
            createdBy = "",
            updatedBy = null,
            isDeleted = false,
            createdAt = DateTime.now(),
            updatedAt = DateTime.now(),
            productUrl = ""
        ),
        AmityProduct(
            productId = "prod2",
            productName = "Sample Product 2",
            price = 49.99,
            currency = "$",
            thumbnailUrl = "https://via.placeholder.com/150",
            status = AmityProductStatus.ARCHIVED,
            thumbnailMode = null,
            createdBy = "",
            updatedBy = null,
            isDeleted = false,
            createdAt = DateTime.now(),
            updatedAt = DateTime.now(),
            productUrl = ""
        ),
        AmityProduct(
            productId = "prod2",
            productName = "Sample Product 2",
            price = 49.99,
            currency = "$",
            thumbnailUrl = "https://via.placeholder.com/150",
            status = AmityProductStatus.ARCHIVED,
            thumbnailMode = null,
            createdBy = "",
            updatedBy = null,
            isDeleted = true,
            createdAt = DateTime.now(),
            updatedAt = DateTime.now(),
            productUrl = ""
        ),
    )
    ManageProductTagListComponent(
        taggedProducts = sampleProducts,
        onDismiss = {},
        onRemoveProduct = {},
        pinnedProductId = "prod1",
    )
}
