package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.isSupportedDataTypes
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.community.AmityCommunityEmptyFeedView
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.community.AmityCommunityEmptyPinnedFeedView

fun LazyListScope.amityCommunityPinnedFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    pinPosts: LazyPagingItems<AmityPinnedPost>,
    announcementPosts: LazyPagingItems<AmityPinnedPost>,
    onClick: (AmityPost) -> Unit,
) {
    items(
        count = pinPosts.itemCount,
        key = { "pin_${pinPosts[it]?.post?.getPostId() ?: it}" }
    ) { index ->
        pinPosts[index]?.post?.let { post ->
            val isAnnouncement = announcementPosts.itemSnapshotList.items
                .map { it.postId }
                .contains(post.getPostId())
            if (!post.isSupportedDataTypes() || post.isDeleted() || isAnnouncement) {
                return@items
            }

            AmityPostContentComponent(
                post = post,
                pageScope = pageScope,
                style = AmityPostContentComponentStyle.FEED,
                category = AmityPostCategory.PIN,
                hideMenuButton = false,
                hideTarget = true,
                onTapAction = {
                    onClick(post)
                }
            )
            AmityNewsFeedDivider()
        }
    }

    if (announcementPosts.itemCount == 0 && pinPosts.itemCount == 0) {
        item {
            Box(modifier = Modifier.height(480.dp)) {
                AmityCommunityEmptyPinnedFeedView()
            }
        }
    }
}