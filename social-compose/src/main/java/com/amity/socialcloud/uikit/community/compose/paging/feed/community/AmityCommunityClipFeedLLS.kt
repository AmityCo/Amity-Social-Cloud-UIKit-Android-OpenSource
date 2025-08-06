package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileClipFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyClipFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyVideoFeed

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.amityCommunityClipFeedLLS(
    modifier: Modifier = Modifier,
    clipPosts: LazyPagingItems<AmityPost>,
    onClipClicked: (postId: String) -> Unit = { }
) {
    /*  count calculation logic
   *  showing 2 items in a row
   *  check if the item count is even or odd
   *  if even, show 2 items in a row
   *  if odd, show last item in a new row
   */
    items(
        count = clipPosts.itemCount / 2 + clipPosts.itemCount % 2,
        key = { "clip_${clipPosts[it]?.getPostId() ?: it}" }
    ) { index ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            val isFirstVideoIndexValid =
                index * 2 < clipPosts.itemCount && index >= 0

            if (isFirstVideoIndexValid) {
                val firstVideo = clipPosts[index * 2]
                if (firstVideo == null) {
                    Box(modifier.weight(1f))
                } else {
                    AmityProfileClipFeedItem(
                        modifier = modifier
                            .weight(1f)
                            .aspectRatio(9f/16f),
                        data = firstVideo.getData() as AmityPost.Data.CLIP,
                        onClipClick = {
                            onClipClicked(it)
                        }
                    )
                }
            } else {
                Box(modifier.weight(1f))
            }

            val isSecondVideoIndexValid =
                index * 2 + 1 < clipPosts.itemCount && index >= 0

            if (isSecondVideoIndexValid) {
                val secondVideo = clipPosts.peek(index * 2 + 1)
                if (secondVideo == null) {
                    Box(modifier = modifier.weight(1f))
                } else {
                    AmityProfileClipFeedItem(
                        modifier = modifier
                            .weight(1f)
                            .aspectRatio(9f/16f),
                        data = secondVideo.getData() as AmityPost.Data.CLIP,
                        onClipClick = {
                            onClipClicked(it)
                        }
                    )
                }
            } else {
                Box(modifier = modifier.weight(1f))
            }
        }
    }

    if (clipPosts.itemCount == 0) {
        item {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyClipFeed()
            }
        }
    }
}
