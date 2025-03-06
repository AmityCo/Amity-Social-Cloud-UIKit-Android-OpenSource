package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel


@Composable
fun AmityPostSearchResultComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityGlobalSearchViewModel,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postSearchResultComponentBehavior
    }

    val keyword by viewModel.keyword.collectAsState("")

    val posts = remember(keyword) {
        viewModel.searchPosts()
    }.collectAsLazyPagingItems()

    val loadState by viewModel.postListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "post_search_result"
    ) {
        if (keyword.isNotEmptyOrBlank()) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                AmityGlobalSearchViewModel.PostListState.from(
                    loadState = posts.loadState.refresh,
                    itemCount = posts.itemCount,
                ).let(viewModel::setPostListState)

                when (loadState) {
                    AmityGlobalSearchViewModel.PostListState.EMPTY -> {
                        item {
                            AmityEmptySearchResultComponent(modifier)
                        }
                    }

                    AmityGlobalSearchViewModel.PostListState.LOADING -> {
                        items(4) {
                            AmityPostShimmer()
                            AmityNewsFeedDivider()
                        }
                    }

                    AmityGlobalSearchViewModel.PostListState.SUCCESS -> {
                        items(
                            count = posts.itemCount,
                            key = { index ->
                                posts[index]?.getPostId() ?: index
                            }
                        ) { index ->
                            val post = posts[index] ?: return@items

                            AmityPostContentComponent(
                                post = post,
                                pageScope = pageScope,
                                style = AmityPostContentComponentStyle.FEED,
                                boldedText = keyword,
                                hideMenuButton = false,
                                hideTarget = false,
                                onTapAction = {
                                    behavior.goToPostDetailPage(
                                        context = context,
                                        id = post.getPostId(),
                                    )
                                }
                            )
                            AmityNewsFeedDivider()
                        }
                    }
                }
            }
        }
    }
}
