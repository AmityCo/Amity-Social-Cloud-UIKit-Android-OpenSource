package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColor
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.utils.formatCurrencyForLocale
import org.joda.time.DateTime


@Composable
fun LivestreamPinnedProductElement(
    modifier: Modifier = Modifier,
    product: AmityProduct,
    canManageProducts: Boolean = false,
    onUnpinClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onProductClick: (location: String) -> Unit = {},
    onRemoveClick: () -> Unit = {},
    pageScope: AmityComposePageScope? = null,
    onView: (String) -> Unit = {}
) {
    val isProductArchived = product.getStatus() == AmityProductStatus.ARCHIVED
    AmityBaseElement(
        elementId = "livestream_pinned_product",
        pageScope = pageScope
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    if (!isProductArchived) {
                        onProductClick(getElementScope().getConfigId())
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = AmityTheme.colors.background.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmityTheme.colors.base)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.getThumbnailUrl())
                            .build(),
                        contentDescription = product.getProductName(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (isProductArchived) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {}
                    }
                }

                // Product Information
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = product.getProductName(),
                        style = AmityTheme.typography.captionBold.copy(
                            color = if (product.isDeleted()) {
                                getConfig().getValue("base_shade1_color").asColor(AmityTheme.colors.baseShade1)
                            } else {
                                getConfig().getValue("base_shade4_color").asColor(AmityTheme.colors.baseShade4)
                            },
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isProductArchived) {
                            Text(
                                text = "Unlisted",
                                style = AmityTheme.typography.caption.copy(
                                    color = getConfig().getValue("base_shade2_color").asColor(AmityTheme.colors.baseShade2
                                    )
                                ),
                                maxLines = 1
                            )
                        } else {
                            val formattedPrice = formatCurrencyForLocale(
                                context = LocalContext.current,
                                amount = product.getPrice(),
                                currencyCode = product.getCurrency()
                            )
                            Text(
                                text = formattedPrice,
                                style = AmityTheme.typography.caption.copy(
                                    color = getConfig().getValue("base_shade2_color").asColor(AmityTheme.colors.baseShade2
                                    )
                                ),
                                maxLines = 1
                            )
                        }

                        if (canManageProducts) {
                            if (isProductArchived) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .border(
                                            width = 1.dp,
                                            color = AmityTheme.colors.secondaryShade3,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { onRemoveClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(18.dp),
                                        tint = getConfig().getValue("secondary_color").asColor( AmityTheme.colors.secondary)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .height(28.dp)
                                        .border(
                                            width = 1.dp,
                                            color = AmityTheme.colors.secondaryShade3,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { onUnpinClick() }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.amity_ic_product_tagging_pin_outlined),
                                            contentDescription = "Unpin",
                                            modifier = Modifier.size(20.dp),
                                            tint = AmityTheme.colors.base
                                        )
                                        Text(
                                            text = "Unpin",
                                            style = AmityTheme.typography.captionBold.copy(
                                                color = AmityTheme.colors.base
                                            )
                                        )
                                    }
                                }
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
                                    .clickable { onProductClick(getElementScope().getConfigId()) }
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
            if (!canManageProducts) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { onCloseClick() }
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_close),
                        contentDescription = "Close",
                        modifier = Modifier.size(12.dp),
                        tint = Color.White
                    )
                }
            }
        }
        onView.invoke(getElementScope().getConfigId())
    }
}

@Preview()
@Composable
fun LivestreamPinnedProductElementPreview() {
    val sampleProduct = AmityProduct(
        productId = "prod1",
        productName = "GlowLab Soft Glow Blush Stick Rose Nude",
        price = 24.0,
        currency = "$",
        thumbnailUrl = "https://via.placeholder.com/56",
        status = AmityProductStatus.ACTIVE,
        thumbnailMode = null,
        createdBy = "",
        updatedBy = null,
        isDeleted = false,
        createdAt = DateTime.now(),
        updatedAt = DateTime.now(),
        productUrl = ""
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LivestreamPinnedProductElement(
            product = sampleProduct,
            canManageProducts = true,
        )
        LivestreamPinnedProductElement(
            product = sampleProduct.copy(isDeleted = true),
            canManageProducts = true,
        )
        LivestreamPinnedProductElement(
            product = sampleProduct,
        )
        LivestreamPinnedProductElement(
            product = sampleProduct.copy(isDeleted = true),
            canManageProducts = false,
        )
    }
}
