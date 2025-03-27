package com.amity.socialcloud.uikit.community.compose.community.category.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.bycategory.AmityCommunitiesByCategoryPageActivity
import com.amity.socialcloud.uikit.community.compose.community.category.AmityAllCategoriesPageActivity
import com.amity.socialcloud.uikit.community.compose.community.category.element.AmityCommunityCategoryElement
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityExploreCategoryShimmer

@Composable
fun AmityCommunityCategoriesComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onStateChanged: (AmityCommunityCategoriesViewModel.CategoryListState) -> Unit = {}
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityCategoriesViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val categories = remember {
        viewModel.getCommunityCategories()
    }.collectAsLazyPagingItems()

    val categoryListState by viewModel.categoryListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "community_categories"
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            modifier = modifier.padding(
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            AmityCommunityCategoriesViewModel.CategoryListState.from(
                loadState = categories.loadState.refresh,
                itemCount = categories.itemCount,
            ).let(viewModel::setCategoryListState)
            onStateChanged(categoryListState)
            when (categoryListState) {
                AmityCommunityCategoriesViewModel.CategoryListState.SUCCESS -> {
                    val itemCount = categories.itemCount
                    val maxItemCount = 5
                    if (itemCount > 0) {
                        items(
                            count = minOf(itemCount, maxItemCount),
                            key = { index -> index }
                        ) { index ->
                            categories[index]?.let { item ->
                                AmityCommunityCategoryElement(
                                    category = item,
                                    onRemove = null,
                                    onClick = {
                                        val intent =
                                            AmityCommunitiesByCategoryPageActivity.newIntent(
                                                context,
                                                it.getCategoryId()
                                            )
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                        if (categories.itemCount > maxItemCount) {
                            item {
                                AmityBaseElement(
                                    pageScope = pageScope,
                                    componentScope = getComponentScope(),
                                    elementId = "category"
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(color = Color.Transparent)
                                            .border(
                                                border = BorderStroke(
                                                    1.dp,
                                                    AmityTheme.colors.baseShade4
                                                ),
                                                shape = RoundedCornerShape(24.dp)
                                            )
                                            .clickableWithoutRipple {
                                                val intent =
                                                    AmityAllCategoriesPageActivity.newIntent(context)
                                                context.startActivity(intent)
                                            }
                                            .padding(4.dp)
                                    ) {
                                        Spacer(
                                            modifier
                                                .height(28.dp)
                                                .width(8.dp)
                                        )

                                        Text(
                                            text = "See more",
                                            style = AmityTheme.typography.bodyLegacy.copy(fontWeight = FontWeight.SemiBold)
                                        )
                                        Spacer(modifier.width(4.dp))
                                        Icon(
                                            painter = painterResource(R.drawable.amity_ic_chevron_right),
                                            contentDescription = "more",
                                            tint = AmityTheme.colors.baseShade1,
                                            modifier = modifier
                                                .size(14.dp)
                                                .padding(top = 2.dp)

                                        )
                                        Spacer(modifier.width(4.dp))

                                    }
                                }
                            }
                        }
                    }
                }

                AmityCommunityCategoriesViewModel.CategoryListState.LOADING -> {
                    item {
                        AmityExploreCategoryShimmer()
                    }
                }

                AmityCommunityCategoriesViewModel.CategoryListState.EMPTY -> {

                }

                AmityCommunityCategoriesViewModel.CategoryListState.ERROR -> {

                }
            }
        }
    }

}