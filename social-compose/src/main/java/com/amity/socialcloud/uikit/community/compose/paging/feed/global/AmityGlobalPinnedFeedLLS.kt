package com.amity.socialcloud.uikit.community.compose.paging.feed.global

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.isSupportedDataTypes
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle


fun LazyListScope.amityGlobalPinnedFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    pinnedPosts: State<List<AmityPinnedPost>>,
    onClick: (AmityPost) -> Unit,
) {
    items(
        count = pinnedPosts.value.size,
        key = {
            "announcement_${pinnedPosts.value[it].post?.getPostId() ?: it}"
        }
    ) { index ->
        pinnedPosts.value[index].post?.let { post ->
            if (!post.isSupportedDataTypes() || post.isDeleted()) {
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
                }
            )
            AmityNewsFeedDivider()
        }
    }
}