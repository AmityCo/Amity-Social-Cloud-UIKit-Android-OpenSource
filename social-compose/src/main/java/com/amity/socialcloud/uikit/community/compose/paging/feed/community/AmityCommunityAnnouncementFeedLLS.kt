package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle

fun LazyListScope.amityCommunityAnnouncementFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    announcementPosts: LazyPagingItems<AmityPinnedPost>,
    hasAnnouncementPin: Boolean,
    onClick: (AmityPost) -> Unit,
) {
    items(
        count = announcementPosts.itemCount,
        key = {
            "announcement_${announcementPosts[it]?.post?.getPostId() ?: it}"
        }
    ) { index ->
        announcementPosts[index]?.post?.let { post ->
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
                pageScope = pageScope,
                style = AmityPostContentComponentStyle.FEED,
                category = if (hasAnnouncementPin) {
                    AmityPostCategory.PIN_AND_ANNOUNCEMENT
                } else {
                    AmityPostCategory.ANNOUNCEMENT
                },
                hideMenuButton = false,
                hideTarget = true,
                onTapAction = {
                    onClick(post)
                }
            )
            AmityNewsFeedDivider()
        }
    }
}