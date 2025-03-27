package com.amity.socialcloud.uikit.community.compose.community.category

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityCategoryAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.bycategory.AmityCommunitiesByCategoryPageActivity
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityCategoryListShimmer


@Composable
fun AmityAllCategoriesPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityAllCategoriesPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val categories = viewModel.getCategories().collectAsLazyPagingItems()
    val categoryListState by viewModel.categoryListState.collectAsState()

    AmityBasePage(
        pageId = "all_categories_page",
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = "All Categories",
                onBackClick = {
                    context.closePageWithResult(
                        resultCode = Activity.RESULT_CANCELED
                    )
                },
            ) {

            }
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                AmityAllCategoriesPageViewModel.CategoryListState.from(
                    loadState = categories.loadState.refresh,
                    itemCount = categories.itemCount,
                ).let(viewModel::setCategoryListState)

                when (categoryListState) {
                    AmityAllCategoriesPageViewModel.CategoryListState.SUCCESS -> {
                        items(
                            count = categories.itemCount,
                            key = { index -> index }
                        ) { index ->
                            val category = categories[index] ?: return@items

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clickableWithoutRipple {
                                        val intent =
                                            AmityCommunitiesByCategoryPageActivity.newIntent(
                                                context = context,
                                                categoryId = category.getCategoryId()
                                            )
                                        context.startActivity(intent)
                                    }
                            ) {
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    componentScope = null,
                                    elementId = "category_avatar"
                                ) {
                                    AmityCategoryAvatarView(
                                        category = category,
                                        size = 40.dp,
                                        modifier = modifier.testTag(getAccessibilityId()),
                                    )
                                }

                                Column(
                                    verticalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AmityBaseElement(
                                            pageScope = getPageScope(),
                                            componentScope = null,
                                            elementId = "category_display_name"
                                        ) {
                                            Text(
                                                text = category.getName(),
                                                style = AmityTheme.typography.bodyLegacy.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = modifier.testTag(getAccessibilityId()),
                                            )
                                        }
                                    }
                                }

                                Icon(
                                    painter = painterResource(R.drawable.amity_ic_chevron_right),
                                    contentDescription = "more",
                                    tint = AmityTheme.colors.baseShade1,
                                    modifier = modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )

                            }

                            if (index < categories.itemCount - 1) {
                                HorizontalDivider(
                                    color = AmityTheme.colors.divider
                                )
                            }
                        }
                    }

                    AmityAllCategoriesPageViewModel.CategoryListState.LOADING -> {
                        item {
                            AmityCategoryListShimmer()
                        }
                    }

                    AmityAllCategoriesPageViewModel.CategoryListState.EMPTY -> {}
                    AmityAllCategoriesPageViewModel.CategoryListState.ERROR -> {}
                }
            }
        }

    }
}