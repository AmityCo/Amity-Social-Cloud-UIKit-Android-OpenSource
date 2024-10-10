package com.amity.socialcloud.uikit.community.compose.paging.feed.global

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel.PostListState
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityEmptyNewsFeedComponent
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityPostAdView

fun LazyListScope.amityGlobalFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    globalPosts: LazyPagingItems<AmityListItem>,
    postListState: PostListState,
    onClick: (AmityPost) -> Unit,
    onCreateCommunityClicked: () -> Unit,
) {
    item {
        AmityNewsFeedDivider()
    }
    items(
        count = AmityPostComposerHelper.getCreatedPosts().size,
        key = { "temp_" + AmityPostComposerHelper.getCreatedPosts()[it].getPostId() }
    ) {
        val post = AmityPostComposerHelper.getCreatedPosts()[it]
        AmityPostContentComponent(
            post = post,
            style = AmityPostContentComponentStyle.FEED,
            hideMenuButton = false,
            onTapAction = {
                onClick(post)
            }
        )
        AmityNewsFeedDivider()
    }

    when (postListState) {
        PostListState.SUCCESS -> {
            items(
                count = globalPosts.itemCount,
                key = { "global_${(globalPosts[it] as? AmityListItem.PostItem)?.post?.getPostId() ?: it}" }
            ) { index ->
                when (val data = globalPosts[index]) {
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
                            post = post,
                            style = AmityPostContentComponentStyle.FEED,
                            hideMenuButton = false,
                            onTapAction = {
                                onClick(post)
                            },
                        )
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

                    else -> {
                        AmityPostShimmer()
                        AmityNewsFeedDivider()
                    }
                }
            }
        }

        PostListState.LOADING -> {
            items(4) {
                AmityPostShimmer()
                AmityNewsFeedDivider()
            }
        }

        PostListState.ERROR,
        PostListState.EMPTY -> {
            item {
                AmityEmptyNewsFeedComponent(
                    modifier = modifier,
                    pageScope = pageScope,
                    onExploreClicked = {},
                    onCreateClicked = onCreateCommunityClicked
                )
            }
        }
    }
}