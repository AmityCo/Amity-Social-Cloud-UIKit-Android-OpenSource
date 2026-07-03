package com.amity.socialcloud.uikit.community.compose.socialhome.components

import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityExploreComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    val pullRefreshState = rememberPullToRefreshState()

    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel =
        viewModel<AmityExploreComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val isEmpty by viewModel.isEmpty.collectAsState()

    val isError by viewModel.isError.collectAsState()


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.setRefreshing()
        },
        indicator = {
            PullToRefreshDefaults.Indicator(
                isRefreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
        modifier = modifier.fillMaxSize(),
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
                            amitySocialString("amity_social_empty_state_explore_empty")
                        } else {
                            amitySocialString("amity_social_label_no_community_yet")
                        }

                    val caption =
                        if (viewModel.getCategoryState() == AmityCommunityCategoriesViewModel.CategoryListState.EMPTY) {
                            amitySocialString("amity_social_empty_state_social_home_empty_description")
                        } else {
                            amitySocialString("amity_social_label_no_community_yet_description")
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
                                tint = amityColorWhite
                            )
                            Text(
                                modifier = Modifier.padding(end = 16.dp),
                                text = amitySocialString("amity_social_button_social_home_create_community"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = amityColorWhite,
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
                            text = amitySocialString("amity_social_label_something_went_wrong"),
                            modifier = Modifier.padding(top = 16.dp),
                            style = AmityTheme.typography.titleLegacy.copy(
                                color = AmityTheme.colors.baseShade3,
                            )
                        )
                        Text(
                            text = amitySocialString("amity_social_label_please_try_again"),
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
        }
    }
}