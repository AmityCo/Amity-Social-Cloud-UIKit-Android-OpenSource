package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.isSupportedDataTypes
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityPostAdView
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.community.AmityCommunityEmptyFeedView

fun LazyListScope.amityCommunityFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    communityPosts: LazyPagingItems<AmityListItem>,
    pinPosts: LazyPagingItems<AmityPinnedPost>,
    announcementPosts: LazyPagingItems<AmityPinnedPost>,
    onClick: (AmityPost, AmityPostCategory) -> Unit,
) {
    items(
        count = communityPosts.itemCount,
        key = { "community_${(communityPosts[it] as? AmityListItem.PostItem)?.post?.getPostId() ?: it}" }
    ) { index ->
        when (val data = communityPosts[index]) {
            is AmityListItem.PostItem -> {
                val post = data.post
                val isAnnouncement = announcementPosts.itemSnapshotList.items
                    .map { it.postId }
                    .contains(post.getPostId())
                if (!post.isSupportedDataTypes() || isAnnouncement) {
                    return@items
                }

                val category = if (
                    pinPosts.itemSnapshotList.items.map { it.postId }
                        .contains(post.getPostId())
                ) {
                    AmityPostCategory.PIN
                } else {
                    AmityPostCategory.GENERAL
                }
                AmityPostContentComponent(
                    post = post,
                    pageScope = pageScope,
                    style = AmityPostContentComponentStyle.FEED,
                    category = category,
                    hideMenuButton = false,
                    hideTarget = true,
                    onTapAction = {
                        onClick(post, category)
                    }
                )
                AmityNewsFeedDivider()
            }

            is AmityListItem.AdItem -> {
                val ad = data.ad
                AmityPostAdView(
                    ad = ad,
                )
                AmityNewsFeedDivider()
            }

            else -> {
                AmityPostShimmer()
                AmityNewsFeedDivider()
            }
        }
    }

    if (announcementPosts.itemCount == 0 && communityPosts.itemCount == 0) {
        item {
            Box(
                modifier = Modifier
                    .height(480.dp),
            ) {
                AmityCommunityEmptyFeedView()
            }
        }
    }

}