package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.flow.flowOf

private const val MIN_SEARCH_LENGTH = 2

@Composable
fun AmityProductMentionSuggestionView(
    modifier: Modifier = Modifier,
    keyword: String = "",
    heightIn: Dp = 200.dp,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    alreadyTaggedProductIds: Set<String> = emptySet(),
    onProductSelected: (AmityProduct) -> Unit = {},
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmityProductMentionViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // Only search if keyword length >= MIN_SEARCH_LENGTH
    val shouldSearch = keyword.length >= MIN_SEARCH_LENGTH

    val suggestions = remember(keyword, shouldSearch) {
        if (shouldSearch) {
            viewModel.searchProducts(keyword)
        } else {
            flowOf(PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    val isInitialState = keyword.length < MIN_SEARCH_LENGTH
    val isLoading = suggestions.loadState.refresh is LoadState.Loading
    val isNoResultsState = shouldSearch &&
            suggestions.loadState.refresh is LoadState.NotLoading &&
            suggestions.itemCount == 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = heightIn)
            .clip(shape)
            .background(AmityTheme.colors.background)
    ) {
        when {
            isInitialState -> {
                // Initial state: keyword length < MIN_SEARCH_LENGTH
                ProductMentionPlaceholder(
                    message = "Start typing to search for products.",
                    modifier = Modifier.height(heightIn)
                )
            }
            isLoading -> {
                // Loading state - show shimmer items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(3) {
                        ProductMentionShimmerItem()
                    }
                }
            }
            isNoResultsState -> {
                // No results state: API returned empty
                ProductMentionPlaceholder(
                    message = "No results found",
                    modifier = Modifier.height(heightIn)
                )
            }
            else -> {
                // Has results: show product list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(
                        count = suggestions.itemCount,
                        key = { index -> suggestions.itemSnapshotList[index]?.getProductId() ?: index }
                    ) {
                        val product = suggestions[it] ?: return@items
                        val isDuplicated = alreadyTaggedProductIds.contains(product.getProductId())

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .alpha(if (isDuplicated) 0.4f else 1f)
                                .clickableWithoutRipple(enabled = !isDuplicated) { onProductSelected(product) },
                        ) {
                            ProductThumbnail(url = product.getThumbnailUrl())

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    maxLines = 2,
                                    text = product.getProductName(),
                                    overflow = TextOverflow.Ellipsis,
                                    style = AmityTheme.typography.bodyLegacy,
                                )
                                if (isDuplicated) {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(R.string.amity_v4_product_already_tagged),
                                        style = AmityTheme.typography.caption,
                                        color = AmityTheme.colors.baseShade2,
                                    )
                                }
                            }
                        }
                    }
                    
                    // Handle append loading state for pagination
                    if (suggestions.loadState.append is LoadState.Loading) {
                        item {
                            ProductMentionShimmerItem()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductMentionPlaceholder(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.amity_ic_search_placeholder),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )
        Text(
            text = message,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
            color = AmityTheme.colors.baseShade3,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
private fun ProductMentionShimmerItem(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        // Shimmer thumbnail
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Shimmer text line
        Box(
            modifier = Modifier
                .width(123.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}

@Composable
private fun ProductThumbnail(
    url: String?,
    productName: String? = null,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build(),
        contentDescription = productName,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
        error = painterResource(R.drawable.amity_ic_product_tagging_placeholder_light),
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp)),
    )
}
