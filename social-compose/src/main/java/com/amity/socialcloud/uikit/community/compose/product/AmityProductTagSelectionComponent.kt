package com.amity.socialcloud.uikit.community.compose.product

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import androidx.paging.LoadState
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfig
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.asColor
import com.amity.socialcloud.uikit.community.compose.product.AmityProductSelectionViewModel.Companion.MIN_KEYWORD_LENGTH

/**
 * Contract (page-level):
 * - UI-only component that renders the "Tag products" selection screen.
 * - State is hoisted: caller passes a list and selected list + callbacks.
 * - Bottom-sheet wrapper will be added later (as requested).
 */
@Composable
fun AmityProductTagSelectionComponent(
    modifier: Modifier = Modifier,
    title: String = "Tag products",
    selectedProductTags: List<AmityProduct>,
    maxSelection: Int = 5,
    pageScope: AmityComposePageScope? = null,
    onClose: (List<AmityProduct>) -> Unit,
    onDone: (List<AmityProduct>) -> Unit,
    onProductToggled: (AmityProduct) -> Unit = {},
    savedProducts: List<AmityProduct> = emptyList(),
    @DrawableRes placeholder: Int? = R.drawable.amity_ic_product_tagging_placeholder_light,
    discardConfirmationMessage: String? = null,
    showBottomBar: Boolean = false,
    instanceKey: String = "", // Unique key per instance to avoid ViewModel reuse issues
    onDiscardRequest: (() -> Unit)? = null, // When non-null, caller handles the discard dialog externally (e.g. outside a ModalBottomSheet)
    savedProductsLabel: String? = null, // Label shown on items that are already tagged (savedProducts)
    isNetworkConnected: Boolean = true, // When false, shows a non-dismissible "Waiting for network..." snackbar and disables interactions that require network (e.g. search, toggling products)
    requestFocus: Boolean = false,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityProductSelectionViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        key = "amity_product_selection_viewmodel_${maxSelection}_${instanceKey}",
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityProductSelectionViewModel(
                    selectedProducts = selectedProductTags,
                    maxSelection = maxSelection,
                    savedProducts = savedProducts
                ) as T
            }
        }
    )

    // Sync selectedProducts when component is shown (handles reopening with existing tags)
    LaunchedEffect(selectedProductTags) {
        viewModel.setSelectedProducts(selectedProductTags)
    }

    // Sync savedProducts when component is shown (handles reopening after media tags change)
    LaunchedEffect(savedProducts) {
        viewModel.setSavedProducts(savedProducts)
    }

    // Use the ViewModel as source-of-truth.
    val uiState by viewModel.uiState.collectAsState()
    val productsPagingItems = viewModel.searchedProducts.collectAsLazyPagingItems()

    // State for showing discard confirmation dialog
    var showDiscardDialog by remember { mutableStateOf(false) }

    // Check if there are unsaved changes
    val hasUnsavedChanges = uiState.selectedProducts.isNotEmpty() &&
        (uiState.selectedProducts.size != selectedProductTags.size ||
            !uiState.selectedProducts.containsAll(selectedProductTags))

    // Clear search keyword when component is disposed (but keep selectedProducts in parent state)
    DisposableEffect(viewModel) {
        onDispose {
            viewModel.clearKeyword()
        }
    }
    BackHandler(enabled = hasUnsavedChanges) {
        if (onDiscardRequest != null) onDiscardRequest() else showDiscardDialog = true
    }
    AmityBaseComponent(componentId = "product_tag_selection", pageScope = pageScope, needScaffold = true) {
        val theme = getComponentScope().getComponentTheme()
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(theme?.backgroundColor?.asColor() ?: AmityTheme.colors.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AmityProductSelectionTopBar(
                    title = title,
                    actionText = "Done",
                    subtitle = "${uiState.selectedProducts.size}/${uiState.maxSelection}",
                    onClose = {
                        if (hasUnsavedChanges) {
                            if (onDiscardRequest != null) onDiscardRequest() else showDiscardDialog =
                                true
                        } else {
                            onClose.invoke(uiState.selectedProducts)
                        }
                    },
                    onAction = { onDone(uiState.selectedProducts) },
                    isActionDisabled = (
                            uiState.selectedProducts.size == selectedProductTags.size
                            && uiState.selectedProducts.containsAll(selectedProductTags)
                        ),
                    theme = theme,
                    showTopAction = !showBottomBar,
                )

                HorizontalDivider(color = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.selectedProducts.isNotEmpty()) {
                    AmityTaggedProductsSection(
                        products = uiState.selectedProducts,
                        theme = theme,
                        placeholder = placeholder,
                        onRemove = {
                            viewModel.removeProduct(it)
                            onProductToggled(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Divider between selected product list and search bar (per design)
                    HorizontalDivider(color = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)

                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                AmitySearchBarView(
                    hint = "Search by product name",
                    requestFocus = requestFocus,
                    onSearch = {
                        viewModel.onKeywordChange(it)
                    },
                    height = 44.dp,
                    cornerRadius = 12.dp,
                    outerPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    searchIconTint = theme?.baseShade3Color?.asColor() ?: AmityTheme.colors.baseShade3,
                    innerHorizontalPadding = 14.dp,
                    textVerticalPadding = 12.dp,
                    clearIconContainerSize = 20.dp,
                    clearIconContainerColor = theme?.baseShade3Color?.asColor() ?: AmityTheme.colors.baseShade3,
                    clearIconPadding = 6.dp,
                    containerColor = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4,
                    clearIconTint = theme?.baseShade4Color?.asColor() ?: Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.weight(1f)) {
                    AmityProductSelectionPagingList(
                        keyword = uiState.keyword,
                        products = productsPagingItems,
                        selectedProductIds = uiState.selectedProducts.map { it.getProductId() }.toSet(),
                        savedProductIds = uiState.savedProducts.map { it.getProductId() },
                        maxSelection = maxSelection,
                        onToggle = {
                            viewModel.toggleProduct(it)
                            onProductToggled(it)
                        },
                        placeholder = placeholder,
                        modifier = Modifier.fillMaxSize(),
                        theme = theme,
                        savedProductsLabel = savedProductsLabel,
                    )

                    val isListEmpty = productsPagingItems.itemCount == 0
                    val isRefreshing = (productsPagingItems.loadState.refresh is LoadState.Loading) && uiState.keyword.length >= MIN_KEYWORD_LENGTH

                    val keyword = uiState.keyword.trim()

                    val showInitialEmpty = (keyword.length < MIN_KEYWORD_LENGTH) &&
                        isListEmpty &&
                        !isRefreshing

                    val showNoResults = keyword.isNotEmpty() &&
                        isListEmpty &&
                        !isRefreshing

                    if (showInitialEmpty) {
                        AmityProductSelectionEmptyOverlay(
                            theme = theme,
                            modifier = Modifier.fillMaxSize(),
                            message = "Start typing to search for products",
                        )
                    } else if (showNoResults) {
                        AmityProductSelectionEmptyOverlay(
                            theme = theme,
                            modifier = Modifier.fillMaxSize(),
                            iconRes = R.drawable.amity_ic_no_search_result,
                            message = "No results found",
                        )
                    }
                }

                if (showBottomBar) {
                    AmityProductSelectionBottomBar(
                        enabled = uiState.selectedProducts.isNotEmpty(),
                        buttonText = "Add products",
                        onClick = { onDone(uiState.selectedProducts) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(theme?.backgroundColor?.asColor() ?: AmityTheme.colors.background),
                        componentScope = getComponentScope()
                    )
                }
            }
        }

        // Discard confirmation dialog
        if (showDiscardDialog && onDiscardRequest == null) {
            AmityAlertDialog(
                dialogTitle = "Discard product tags",
                dialogText = discardConfirmationMessage ?: "You have tagged products that haven't been saved yet. If you leave now, your changes will be lost.",
                confirmText = "Discard",
                dismissText = "Keep editing",
                confirmTextColor = AmityTheme.colors.alert,
                dismissTextColor = AmityTheme.colors.primary,
                onConfirmation = {
                    showDiscardDialog = false
                    onClose.invoke(uiState.selectedProducts)
                },
                onDismissRequest = {
                    showDiscardDialog = false
                }
            )
        }



        if (!isNetworkConnected) {
            getComponentScope().showProgressSnackbar("Waiting for network...")
        } else {
            getComponentScope().dismissSnackbar()
        }
    }
}

@Composable
private fun AmityProductSelectionTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actionText: String,
    subtitle: String,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    onClose: () -> Unit,
    onAction: () -> Unit,
    isActionDisabled: Boolean = false,
    showTopAction: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.amity_ic_dismiss_preview),
            contentDescription = "Close",
            tint = theme?.baseColor?.asColor() ?: AmityTheme.colors.base,
            modifier = Modifier
                .size(24.dp)
                .align(if (showTopAction) Alignment.CenterStart else Alignment.CenterEnd)
                .clickableWithoutRipple { onClose() }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = title,
                style = AmityTheme.typography.titleLegacy.copy(
                    color = theme?.baseColor?.asColor() ?: AmityTheme.colors.base
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Selected count should be below title (per design)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = AmityTheme.typography.captionLegacy.copy(color = theme?.baseShade2Color?.asColor() ?: AmityTheme.colors.baseShade2),
                maxLines = 1,
            )
        }

        if (showTopAction) {
            Text(
                text = actionText,
                style = AmityTheme.typography.body.copy(
                    color = if (isActionDisabled) AmityTheme.colors.primaryShade2 else AmityTheme.colors.primary
                ),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickableWithoutRipple {
                        if (!isActionDisabled) {
                            onAction()
                        }
                    }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun AmityTaggedProductsSection(
    modifier: Modifier = Modifier,
    products: List<AmityProduct>,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    @DrawableRes placeholder: Int? = R.drawable.amity_ic_product_tagging_placeholder_light,
    onRemove: (AmityProduct) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Tagged products",
            style = AmityTheme.typography.bodyBold.copy(
                color = theme?.baseColor?.asColor() ?: AmityTheme.colors.base
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(products, key = { it.getProductId() }) { product ->
                AmityProductTagSelectedItemElement(
                    product = product,
                    theme = theme,
                    placeholder = placeholder,
                    onRemove = { onRemove(product) }
                )
            }
        }
    }
}

@Composable
private fun AmityProductTagSelectedItemElement(
    modifier: Modifier = Modifier,
    product: AmityProduct,
    @DrawableRes placeholder: Int? = R.drawable.amity_ic_product_tagging_placeholder_light,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    onRemove: () -> Unit,
) {
    Column(
        modifier = modifier.widthIn(max = 140.dp)
    ) {
        // Outer box is NOT clipped so the remove icon can overlap the thumbnail corner.
        Box(modifier = Modifier.size(96.dp)) {
            // Thumbnail box remains clipped.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    // Thin border around selected product thumbnail (per design)
                    .border(1.dp, theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4, RoundedCornerShape(12.dp))
                    .background(theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)
            ) {
                val painter = rememberAmityProductPainter(product.getThumbnailUrl())
                val painterState by painter.state.collectAsState()

                Image(
                    painter = painter,
                    contentDescription = product.getProductName(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (painterState !is AsyncImagePainter.State.Success) {
                    // Lightweight placeholder to avoid depending on new drawable.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)
                    ) {
                        placeholder?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Overlapping close icon (drawn above the thumbnail).
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    // Slightly outside the thumbnail to create the "overlap" look.
                    .offset(x = 6.dp, y = (-6).dp)
                    .zIndex(1f)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0x80000000))
                    .clickableWithoutRipple { onRemove() },
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_close),
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = product.getProductName(),
            style = AmityTheme.typography.captionLegacy.copy(
                color = theme?.baseColor?.asColor() ?: AmityTheme.colors.base
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(96.dp)
        )
    }
}

@Composable
private fun AmityProductSelectionPagingList(
    modifier: Modifier = Modifier,
    keyword: String,
    products: LazyPagingItems<AmityProduct>,
    selectedProductIds: Set<String>,
    savedProductIds: List<String>,
    maxSelection: Int,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    @DrawableRes placeholder: Int? = null,
    savedProductsLabel: String? = null,
    onToggle: (AmityProduct) -> Unit,
) {
    val isAtMaxSelection = selectedProductIds.size >= maxSelection

    val isRefreshing = (products.loadState.refresh is LoadState.Loading) && keyword.length >= MIN_KEYWORD_LENGTH

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isRefreshing) {
            // Overlay.png shows 3 skeleton cards.
            items(3) {
                AmityProductSelectionShimmerListItem(theme = theme)
            }
            return@LazyColumn
        }

        items(
            count = products.itemCount,
            key = products.itemKey { it.getProductId() }
        ) { index ->
            val product = products[index] ?: return@items
            val isSelected = selectedProductIds.contains(product.getProductId()) || savedProductIds.contains(product.getProductId())
            val itemEnabled = (isSelected || !isAtMaxSelection) && !savedProductIds.contains(product.getProductId())
            val isSaved = savedProductIds.contains(product.getProductId())

            AmityProductTagSelectionElement(
                product = product,
                isSelected = isSelected,
                enabled = itemEnabled,
                isSavedProduct = isSaved,
                savedProductsLabel = savedProductsLabel,
                theme = theme,
                placeholder = placeholder,
                onClick = { onToggle(product) }
            )
        }
    }
}

@Composable
private fun AmityProductSelectionShimmerListItem(
    modifier: Modifier = Modifier,
    theme: AmityUIKitConfig.UIKitTheme? = null,
) {
    // Overlay.png skeleton: card container, big left media block, single centered bar in right content.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4, RoundedCornerShape(12.dp))
    ) {
        // Left media block (~1/3 width)
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxSize()
                .shimmerBackground(
                    color = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
        )

        // Right content area (~2/3 width)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Single bar, centered vertically-ish.
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.70f)
                    .height(10.dp)
                    .shimmerBackground(
                        color = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Composable
private fun AmityProductTagSelectionElement(
    modifier: Modifier = Modifier,
    product: AmityProduct,
    isSelected: Boolean,
    enabled: Boolean = true,
    isSavedProduct: Boolean = false,
    savedProductsLabel: String? = null,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    @DrawableRes placeholder: Int? = null,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (isSavedProduct) 0.4f else 1f)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4, RoundedCornerShape(12.dp))
            .clickableWithoutRipple(enabled = enabled) { onClick() }
            .padding(end = 16.dp),
    ) {
        val painter = rememberAmityProductPainter(product.getThumbnailUrl())
        val painterState by painter.state.collectAsState()

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .background(theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)
        ) {
            Image(
                painter = painter,
                contentDescription = product.getProductName(),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (painterState !is AsyncImagePainter.State.Success) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4)
                ) {
                    placeholder?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = product.getProductName(),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = theme?.baseColor?.asColor() ?: AmityTheme.colors.base
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (isSavedProduct && savedProductsLabel != null) {
                Text(
                    text = savedProductsLabel,
                    style = AmityTheme.typography.caption,
                    color = theme?.baseShade2Color?.asColor() ?: AmityTheme.colors.baseShade2,
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        AmityCircleCheckIndicator(
            isChecked = isSelected,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
    }
}

@Composable
private fun AmityCircleCheckIndicator(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    enabled: Boolean = true,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    onClick: () -> Unit,
) {
    val indicatorSize = 20.dp
    val strokeWidth = 4.dp
    val checkedColor = theme?.primaryColor?.asColor() ?: AmityTheme.colors.primary
    val uncheckedColor = theme?.baseShade3Color?.asColor() ?: AmityTheme.colors.baseShade3
    val disabledUncheckedColor = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4

    val ringColor = when {
        isChecked -> checkedColor
        enabled -> uncheckedColor
        else -> disabledUncheckedColor
    }

    if (isChecked && !enabled) {
        Image(
            painter = painterResource(R.drawable.amity_ic_check_disabled),
            contentDescription = "Selected already",
        )
    } else {
        Box(
            modifier = modifier
                .size(indicatorSize)
                .clip(CircleShape)
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (isChecked) {
                    drawCircle(color = ringColor)
                } else {
                    drawCircle(
                        color = ringColor,
                        style = Stroke(width = strokeWidth.toPx())
                    )
                }
            }

            if (isChecked) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_check),
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


// For bottom bar action on the component
@Composable
private fun AmityProductSelectionBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    buttonText: String,
    onClick: () -> Unit,
    componentScope: AmityComposeComponentScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
    ) {
        HorizontalDivider(color = AmityTheme.colors.divider)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = componentScope.getComponentTheme()?.primaryColor?.asColor() ?: AmityTheme.colors.highlight,
                disabledContainerColor = componentScope.getComponentTheme()?.primaryColor?.asColor()?.copy(alpha = 0.3f) ?: AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
            ),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            enabled = enabled,
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = onClick
        ) {
            Text(
                text = buttonText,
                style = AmityTheme.typography.captionLegacy.copy(
                    if (enabled) Color.White
                    else Color.White.copy(alpha = 0.3f)
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun rememberAmityProductPainter(imageUrl: String?) = rememberAsyncImagePainter(
    model = ImageRequest
        .Builder(LocalContext.current)
        .data(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
)

@Composable
private fun AmityProductSelectionEmptyOverlay(
    modifier: Modifier = Modifier,
    theme: AmityUIKitConfig.UIKitTheme? = null,
    @DrawableRes iconRes: Int = R.drawable.amity_ic_start_search,
    message: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = theme?.baseShade4Color?.asColor() ?: AmityTheme.colors.baseShade4,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = AmityTheme.typography.bodyBold.copy(fontWeight = FontWeight.SemiBold),
                color = theme?.baseShade3Color?.asColor() ?: AmityTheme.colors.baseShade3,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
