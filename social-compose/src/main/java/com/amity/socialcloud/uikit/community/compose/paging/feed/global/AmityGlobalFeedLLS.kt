package com.amity.socialcloud.uikit.community.compose.paging.feed.global

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.isSupportedDataTypes
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
    pinnedPosts: State<List<AmityPinnedPost>>,
    postListState: PostListState,
    onClick: (AmityPost) -> Unit,
    onCreateCommunityClicked: () -> Unit,
    onExploreCommunityClicked: () -> Unit,
) {
    val createdPosts = AmityPostComposerHelper.getCreatedPosts();

    items(
        count = createdPosts.size,
        key = { "temp_" + AmityPostComposerHelper.getCreatedPosts()[it].getPostId() }
    ) { it ->
        val post = createdPosts[it]

        val isFeatured = pinnedPosts.value
            .map { pinned -> pinned.postId }
            .contains(post.getPostId())

        if (isFeatured) {
            return@items
        }
        AmityPostContentComponent(
            post = post,
            pageScope = pageScope,
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
                        val isFeatured = pinnedPosts.value
                            .map { pinned -> pinned.postId }
                            .contains(post.getPostId())
                        val isIncludedInCreatedList = createdPosts
                            .map { pinned -> pinned.getPostId() }
                            .contains(post.getPostId())
                        if (!post.isSupportedDataTypes() || isFeatured || isIncludedInCreatedList) {
                            return@items
                        }

                        AmityPostContentComponent(
                            post = post,
                            pageScope = pageScope,
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
                    onExploreClicked = onExploreCommunityClicked,
                    onCreateClicked = onCreateCommunityClicked
                )
            }
        }
    }
}