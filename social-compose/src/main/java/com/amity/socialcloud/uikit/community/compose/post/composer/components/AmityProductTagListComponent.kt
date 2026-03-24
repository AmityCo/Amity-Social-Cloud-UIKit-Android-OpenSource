package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.utils.formatCurrencyForLocale

enum class RenderModeEnum {
    POST, IMAGE , VIDEO, LIVESTREAM
}

/**
 * Bottom sheet to display all tagged products in read-only mode.
 * Used in post composer to show aggregated product tags from text and media.
 * Design follows AmityProductTaggingBottomSheet but without management functions (add/remove/pin).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityProductTagListComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    productTags: List<AmityProduct>,
    postId: String? = null,
    renderMode: RenderModeEnum = RenderModeEnum.POST,
    onDismiss: () -> Unit,
    onProductClick: (AmityProduct) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        modifier = modifier.statusBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        AmityBaseComponent(
            pageScope = pageScope,
            componentId = "product_tag_list",
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = when (renderMode) {
                            RenderModeEnum.IMAGE -> "Products tagged in this photo"
                            RenderModeEnum.VIDEO -> "Products tagged in this video"
                            RenderModeEnum.POST -> "Products tagged in this post"
                            RenderModeEnum.LIVESTREAM -> "Products tagged"
                        },
                        color = AmityTheme.colors.base,
                        style = AmityTheme.typography.titleBold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    if (renderMode == RenderModeEnum.LIVESTREAM) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_dismiss_preview),
                            contentDescription = "Close",
                            tint = AmityTheme.colors.base,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickableWithoutRipple {
                                    onDismiss()
                                }
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = AmityTheme.colors.baseShade4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product list
                val listState = rememberLazyListState()
                val currentProductTags = rememberUpdatedState(productTags)
                val viewedProductIds = remember { mutableSetOf<String>() }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "product_tag"
                ) {
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                            .collect { visibleItems ->
                                for (itemInfo in visibleItems) {
                                    currentProductTags.value.getOrNull(itemInfo.index)?.let { product ->
                                        if (viewedProductIds.add(product.getProductId())) {
                                            postId?.let { pid ->
                                                product.analytics().markAsViewed(
                                                    sourceType = AnalyticsEventSourceType.POST,
                                                    sourceId = pid,
                                                    location = getConfigId()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(productTags, key = { it.getProductId() }) { product ->
                            AmityProductTagElement(
                                product = product,
                                postId = postId,
                                location = getConfigId(),
                                onProductClick = onProductClick,
                                renderMode = renderMode
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AmityProductTagElement(
    product: AmityProduct,
    modifier: Modifier = Modifier,
    postId: String? = null,
    location: String = "",
    renderMode: RenderModeEnum,
    onProductClick: (AmityProduct) -> Unit = {},
) {
    val isUnavailable = product.isDeleted() || product.getStatus() == AmityProductStatus.ARCHIVED

    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                if (!isUnavailable) {
                    postId?.let { pid ->
                        product.analytics().markAsClicked(
                            sourceType = AnalyticsEventSourceType.POST,
                            sourceId = pid,
                            location = location
                        )
                    }
                }
                onProductClick(product)
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
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(product.getThumbnailUrl())
                    .build(),
                contentDescription = product.getProductName(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
                error = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
                modifier = Modifier.fillMaxSize()
            )

            if (isUnavailable) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xCCFFFFFF))
                )
            }
        }

        // Information
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (isUnavailable) {
                Text(
                    text = "Unlisted",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1,
                )
            }
            Text(
                text = product.getProductName(),
                style = AmityTheme.typography.bodyBold,
                color = if (isUnavailable) AmityTheme.colors.baseShade4 else AmityTheme.colors.base,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!isUnavailable) {
                Spacer(Modifier.height(8.dp))
                val formattedPrice = formatCurrencyForLocale(
                    context = context,
                    amount = product.getPrice(),
                    currencyCode = product.getCurrency()
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedPrice,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.body,
                        color = AmityTheme.colors.baseShade3,
                        modifier = Modifier.weight(1f)
                    )
                    if (renderMode == RenderModeEnum.LIVESTREAM) {
                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .width(64.dp)
                                .background(
                                    color = AmityTheme.colors.primary,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { onProductClick(product) }
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
