package com.amity.socialcloud.uikit.community.compose.paging.feed.global

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle


fun LazyListScope.amityGlobalPinnedFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    pinnedPosts: State<List<AmityPinnedPost>>,
    onClick: (AmityPost) -> Unit,
    onClipClicked: (AmityPost) -> Unit = {},
    refreshKey: Int = 0,
) {
    items(
        count = pinnedPosts.value.size,
        key = {
            "announcement_${pinnedPosts.value[it].post?.getPostId() ?: it}"
        }
    ) { index ->
        pinnedPosts.value[index].post?.let { post ->
            // Same predicate the empty-state count uses — see AmityFeedRenderability. Kept here as
            // well as at the call site so an external caller passing an unfiltered list still
            // cannot render a deleted or unsupported pinned post.
            if (!post.isRenderableInFeed()) {
                return@items
            }

            AmityPostContentComponent(
                post = post,
                pageScope = pageScope,
                style = AmityPostContentComponentStyle.FEED,
                category = AmityPostCategory.GLOBAL,
                hideMenuButton = false,
                hideTarget = false,
                onTapAction = {
                    onClick(post)
                },
                onClipClick = { childPost ->
                    onClipClicked(childPost)
                },
                refreshKey = refreshKey,
            )
            AmityNewsFeedDivider()
        }
    }
}