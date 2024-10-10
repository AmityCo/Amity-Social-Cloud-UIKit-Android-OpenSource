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
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.community.AmityCommunityEmptyFeedView

fun LazyListScope.amityCommunityPinnedFeedLLS(
    modifier: Modifier = Modifier,
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

            if (isAnnouncement) {
                return@items
            }

            // TODO: 3/6/24 currently only support text, image, and video post
            when (post.getData()) {
                is AmityPost.Data.TEXT,
                is AmityPost.Data.IMAGE,
                is AmityPost.Data.VIDEO -> {
                }

                else -> return@items
            }

            if (post.isDeleted()) {
                return@items
            }
            AmityPostContentComponent(
                post = post,
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
                AmityCommunityEmptyFeedView()
            }
        }
    }
}