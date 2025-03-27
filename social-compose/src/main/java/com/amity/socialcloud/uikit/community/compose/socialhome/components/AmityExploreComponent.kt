package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.category.component.AmityCommunityCategoriesComponent
import com.amity.socialcloud.uikit.community.compose.community.category.component.AmityCommunityCategoriesViewModel
import com.amity.socialcloud.uikit.community.compose.community.recommending.AmityRecommendedCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.community.trending.AmityTrendingCommunitiesComponent
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityExploreCategoryShimmer
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityRecommendedCommunityShimmer
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityTrendingCommunityShimmer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AmityExploreComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {

    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel =
        viewModel<AmityExploreComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val isEmpty by viewModel.isEmpty.collectAsState()

    val isError by viewModel.isError.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            viewModel.setRefreshing()
        }
    )


    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (isRefreshing) {
            Column {
                AmityNewsFeedDivider()
                AmityExploreCategoryShimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp,
                            end = 0.dp
                        )
                )
                AmityRecommendedCommunityShimmer()
                AmityTrendingCommunityShimmer(modifier = Modifier.padding(start = 16.dp))
            }
        } else {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AmityNewsFeedDivider()
                AmityCommunityCategoriesComponent(
                    pageScope = pageScope,
                    onStateChanged = {
                        viewModel.setCategoryState(it)
                    }
                )
                if (isEmpty) {
                    val paddingTop =
                        if (viewModel.getCategoryState() == AmityCommunityCategoriesViewModel.CategoryListState.EMPTY) {
                            100.dp
                        } else {
                            40.dp
                        }

                    val title =
                        if (viewModel.getCategoryState() == AmityCommunityCategoriesViewModel.CategoryListState.EMPTY) {
                            "Your explore is empty"
                        } else {
                            "No community yet"
                        }

                    val caption =
                        if (viewModel.getCategoryState() == AmityCommunityCategoriesViewModel.CategoryListState.EMPTY) {
                            "Find community or create your own"
                        } else {
                            "Let's create your own communities.."
                        }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingTop, start = 24.dp, end = 24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_no_explore_communities),
                            contentDescription = null,
                        )

                        Text(
                            text = title,
                            modifier = Modifier.padding(top = 16.dp),
                            style = AmityTheme.typography.titleLegacy.copy(
                                color = AmityTheme.colors.baseShade3
                            )
                        )
                        Text(
                            text = caption,
                            modifier = Modifier.padding(top = 4.dp),
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = AmityTheme.colors.baseShade3
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier
                                .widthIn(min = 0.dp, max = 250.dp)
                                .padding(top = 26.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AmityTheme.colors.primary)
                                .height(40.dp)
                                .clickable {
                                    val intent = AmityCommunitySetupPageActivity.newIntent(
                                        context = context,
                                        mode = AmityCommunitySetupPageMode.Create
                                    )
                                    context.startActivity(intent)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(start = 12.dp),
                                tint = Color.White
                            )
                            Text(
                                modifier = Modifier.padding(end = 16.dp),
                                text = "Create community",
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                ),
                            )
                        }

                    }
                } else if (isError) {
                    val paddingTop =
                        if (viewModel.getCategoryState() == AmityCommunityCategoriesViewModel.CategoryListState.EMPTY) {
                            185.dp
                        } else {
                            135.dp
                        }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingTop, start = 24.dp, end = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_unable_to_load),
                            contentDescription = null,
                            tint = AmityTheme.colors.baseShade4,
                        )

                        Text(
                            text = "Something went wrong",
                            modifier = Modifier.padding(top = 16.dp),
                            style = AmityTheme.typography.titleLegacy.copy(
                                color = AmityTheme.colors.baseShade3,
                            )
                        )
                        Text(
                            text = "Please try again.",
                            modifier = Modifier.padding(top = 4.dp),
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = AmityTheme.colors.baseShade3,
                            )
                        )
                    }
                }

                AmityRecommendedCommunitiesComponent(
                    pageScope = pageScope,
                    onStateChanged = {
                        viewModel.setRecommendedState(it)
                    },
                )
                AmityTrendingCommunitiesComponent(
                    pageScope = pageScope,
                    onStateChanged = {
                        viewModel.setTrendingState(it)
                    },
                )
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}