package com.amity.socialcloud.uikit.community.compose.community.bycategory


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityJoinCommunityView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityCommunityListShimmer

@Composable
fun AmityCommunitiesByCategoryPage(
    modifier: Modifier = Modifier,
    categoryId: String,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.myCommunitiesComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunitiesByCategoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val communities = viewModel.getCommunities(categoryId).collectAsLazyPagingItems()
    val communityListState by viewModel.communityListState.collectAsState()
    val communityName by viewModel.getCategory(categoryId).collectAsState(initial = "")


    AmityBasePage(
        pageId = "communities_by_category_page",
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = communityName,
                onBackClick = {
                    context.closePageWithResult(
                        resultCode = Activity.RESULT_CANCELED
                    )
                },
            )

            AmityCommunitiesByCategoryPageViewModel.CommunityListState.from(
                loadState = communities.loadState.mediator?.refresh
                    ?: communities.loadState.refresh,
                itemCount = communities.itemCount,
            ).let(viewModel::setCommunityListState)

            when (communityListState) {
                AmityCommunitiesByCategoryPageViewModel.CommunityListState.EMPTY -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp, bottom = 60.dp, start = 24.dp, end = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_empty_community_list),
                            contentDescription = null,
                            tint = AmityTheme.colors.baseShade4,
                        )

                        Text(
                            text = "No community yet",
                            modifier = Modifier.padding(top = 8.dp),
                            style = AmityTheme.typography.titleLegacy.copy(
                                color = AmityTheme.colors.baseShade3
                            )
                        )
                    }
                }

                AmityCommunitiesByCategoryPageViewModel.CommunityListState.ERROR -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp, bottom = 60.dp, start = 24.dp, end = 24.dp)
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
                                color = AmityTheme.colors.baseShade3
                            )
                        )
                        Text(
                            text = "Please try again.",
                            modifier = Modifier.padding(top = 4.dp),
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = AmityTheme.colors.baseShade3
                            )
                        )
                    }
                }

                else -> {

                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = modifier.fillMaxSize()
                        ) {
                            when (communityListState) {
                                AmityCommunitiesByCategoryPageViewModel.CommunityListState.SUCCESS -> {
                                    items(
                                        count = communities.itemCount,
                                        key = { index -> index }
                                    ) { index ->
                                        val community = communities[index] ?: return@items

                                        AmityJoinCommunityView(
                                            modifier = modifier,
                                            pageScope = getPageScope(),
                                            community = community,
                                            onClick = {
                                                behavior.goToCommunityProfilePage(
                                                    context = context,
                                                    community = community,
                                                )
                                            }
                                        )
                                    }
                                }

                                AmityCommunitiesByCategoryPageViewModel.CommunityListState.LOADING -> {
                                    item {
                                        AmityCommunityListShimmer()
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }

    }
}