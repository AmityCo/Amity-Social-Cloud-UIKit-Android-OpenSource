package com.amity.socialcloud.uikit.community.compose.community.category

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.category.element.AmityCommunityCategoryItem
import com.amity.socialcloud.uikit.community.compose.community.category.element.AmityCommunityCategoryList

@Composable
fun AmityCommunityAddCategoryPage(
    modifier: Modifier = Modifier,
    categories: List<AmityCommunityCategory>,
    onAddedAction: (List<AmityCommunityCategory>) -> Unit
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityAddCategoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val selectedCategories = remember(categories.size) {
        mutableStateListOf(*categories.toTypedArray())
    }

    val isEnabledSelection by remember {
        derivedStateOf {
            selectedCategories.size < 10
        }
    }

    val lazyPagingItems = remember {
        viewModel.getCommunityCategories()
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "community_add_category_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_close),
                        contentDescription = "Close",
                        modifier = modifier
                            .size(16.dp)
                            .clickableWithoutRipple {
                                context.closePageWithResult(Activity.RESULT_CANCELED)
                            }
                    )

                    Text(
                        text = "Select category",
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier.padding(vertical = 17.dp)
                    )

                    Text(
                        text = "${selectedCategories.size}/10",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.baseShade2
                        ),
                    )
                }

                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                )
                Spacer(modifier.height(24.dp))

                if (selectedCategories.isNotEmpty()) {
                    AmityCommunityCategoryList(
                        categories = selectedCategories,
                        onRemove = { category ->
                            selectedCategories.find {
                                it.getCategoryId() == category.getCategoryId()
                            }.let(selectedCategories::remove)

                            viewModel.updateSelectedItemState(
                                categoryId = category.getCategoryId(),
                                isSelected = false,
                            )
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier.height(24.dp))
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                    )
                    Spacer(modifier.height(16.dp))
                }

                LazyColumn {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { lazyPagingItems[it]?.getCategoryId() ?: it }
                    ) { index ->
                        val category = lazyPagingItems[index] ?: return@items
                        viewModel.updateSelectedItemState(
                            categoryId = category.getCategoryId(),
                            isSelected = selectedCategories.find { it.getCategoryId() == category.getCategoryId() } != null,
                        )

                        AmityCommunityCategoryItem(
                            viewModel = viewModel,
                            category = category,
                            isEnabled = isEnabledSelection,
                            onSelect = { categoryToAdd ->
                                val isAdded = selectedCategories.find {
                                    it.getCategoryId() == categoryToAdd.getCategoryId()
                                } != null

                                if (!isAdded) {
                                    selectedCategories.add(categoryToAdd)
                                }
                            },
                            onRemove = { categoryToRemove ->
                                selectedCategories.find {
                                    it.getCategoryId() == categoryToRemove.getCategoryId()
                                }.let(selectedCategories::remove)
                            }
                        )
                    }
                    item {
                        Spacer(modifier.height(80.dp))
                    }
                }
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
            ) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                )

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                        disabledContainerColor = AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    enabled = true,
                    modifier = modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onAddedAction(selectedCategories)
                    }
                ) {
                    Text(
                        text = "Add category",
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = Color.White,
                        ),
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommunityAddCategoryPagePreview() {
    AmityCommunityAddCategoryPage(
        categories = emptyList()
    ) {}
}
