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
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityPostAdView

fun LazyListScope.amityForYouFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    forYouPosts: LazyPagingItems<AmityListItem>,
    pinnedPosts: State<List<AmityPinnedPost>>,
    onClick: (AmityPost) -> Unit,
    // PDT-4735: every other feed forwards clip taps; this one did not, so the play button on a
    // clip in the For You feed did nothing.
    onClipClick: (AmityPost) -> Unit = {},
    refreshKey: Int = 0,
) {
    val createdPosts = AmityPostComposerHelper.getCreatedPosts()
    val pinnedPostIds = pinnedPosts.value.pinnedPostIds()
    val createdPostIds = createdPosts.postIds()

    // Newly created posts — appear immediately below the pinned section
    items(
        count = createdPosts.size,
        key = { idx -> "temp_" + createdPosts[idx].getPostId() }
    ) { idx ->
        val post = createdPosts[idx]
        val isFeatured = pinnedPosts.value.any { pinned -> pinned.postId == post.getPostId() }
        if (isFeatured) return@items

        AmityPostContentComponent(
            post = post,
            pageScope = pageScope,
            style = AmityPostContentComponentStyle.FEED,
            hideMenuButton = false,
            onClipClick = { childPost -> onClipClick(childPost) },
            onTapAction = { onClick(post) },
            refreshKey = refreshKey,
        )
        AmityNewsFeedDivider()
    }

    // Paginated For You items — ranked posts and injected ads, deduplicated
    items(
        count = forYouPosts.itemCount,
        key = { idx ->
            when (val item = forYouPosts.peek(idx)) {
                is AmityListItem.PostItem -> "foryou_${item.post.getPostId()}"
                is AmityListItem.AdItem -> "foryou_ad_${idx}_${item.ad.getAdId()}"
                else -> "foryou_$idx"
            }
        }
    ) { idx ->
        when (val item = forYouPosts[idx]) {
            is AmityListItem.PostItem -> {
                val post = item.post
                // Same predicate the empty-state count uses — see AmityFeedRenderability.
                if (!item.isRenderableFeedItem(pinnedPostIds, createdPostIds)) return@items

                AmityPostContentComponent(
                    post = post,
                    pageScope = pageScope,
                    style = AmityPostContentComponentStyle.FEED,
                    hideMenuButton = false,
                    onClipClick = { childPost -> onClipClick(childPost) },
                    onTapAction = { onClick(post) },
                    refreshKey = refreshKey,
                )
                AmityNewsFeedDivider()
            }

            is AmityListItem.AdItem -> {
                AmityPostAdView(ad = item.ad, modifier = modifier)
                AmityNewsFeedDivider()
            }

            else -> {
                AmityPostShimmer()
                AmityNewsFeedDivider()
            }
        }
    }
}
