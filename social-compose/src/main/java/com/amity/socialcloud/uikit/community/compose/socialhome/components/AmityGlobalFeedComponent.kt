package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AmityGlobalFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    val context = LocalContext.current

    val behavior = remember {
        AmitySocialBehaviorHelper.globalFeedComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmitySocialHomePageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val posts = viewModel.getGlobalFeed().collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()
    val postListState by viewModel.postListState.collectAsState()


    val isRefreshing by viewModel.isGlobalFeedRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            viewModel.setGlobalFeedRefreshing()
            posts.refresh()
            AmityPostComposerHelper.clear()
        }
    )

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "global_feed_component"
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = modifier.fillMaxSize()
            ) {
                AmitySocialHomePageViewModel.PostListState.from(
                    loadState = posts.loadState.refresh,
                    itemCount = posts.itemCount,
                ).let(viewModel::setPostListState)

                item {
                    AmityStoryTabComponent(
                        type = AmityStoryTabComponentType.GlobalFeed
                    )
                }

                item {
                    AmityNewsFeedDivider()
                }

                items(
                    count = AmityPostComposerHelper.getCreatedPosts().size,
                    key = { AmityPostComposerHelper.getCreatedPosts()[it].getPostId() }
                ) {
                    val post = AmityPostComposerHelper.getCreatedPosts()[it]
                    AmityPostContentComponent(
                        modifier = modifier,
                        post = post,
                        style = AmityPostContentComponentStyle.FEED,
                        hideMenuButton = false,
                    ) {
                        behavior.goToPostDetailPage(
                            context = context,
                            id = post.getPostId()
                        )
                    }
                    AmityNewsFeedDivider()
                }

                when (postListState) {
                    AmitySocialHomePageViewModel.PostListState.SUCCESS -> {
                        items(
                            count = posts.itemCount,
                            key = {
                                (posts[it] as? AmityListItem.PostItem)?.post?.getPostId() ?: it
                            }
                        ) { index ->
                            when (val data = posts[index]) {
                                is AmityListItem.PostItem -> {
                                    val post = data.post
                                    // TODO: 3/6/24 currently only support text, image, and video post
                                    when (post.getData()) {
                                        is AmityPost.Data.TEXT,
                                        is AmityPost.Data.IMAGE,
                                        is AmityPost.Data.VIDEO -> {
                                        }

                                        else -> return@items
                                    }

                                    AmityPostContentComponent(
                                        modifier = modifier,
                                        post = post,
                                        style = AmityPostContentComponentStyle.FEED,
                                        hideMenuButton = false,
                                    ) {
                                        behavior.goToPostDetailPage(
                                            context = context,
                                            id = post.getPostId()
                                        )
                                    }
                                    AmityNewsFeedDivider()
                                }

                                is AmityListItem.AdItem -> {
                                    val ad = data.ad

                                    AmityPostAdView(
                                        ad = ad,
                                        modifier = modifier
                                    )
                                    AmityNewsFeedDivider()
                                }

                                else -> {}
                            }
                        }
                    }

                    AmitySocialHomePageViewModel.PostListState.LOADING -> {
                        items(4) {
                            AmityPostShimmer()
                            AmityNewsFeedDivider()
                        }
                    }

                    AmitySocialHomePageViewModel.PostListState.ERROR,
                    AmitySocialHomePageViewModel.PostListState.EMPTY -> {
                        item {
                            AmityEmptyNewsFeedComponent(
                                modifier = modifier,
                                pageScope = pageScope,
                                onExploreClicked = {},
                                onCreateClicked = {}
                            )
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}