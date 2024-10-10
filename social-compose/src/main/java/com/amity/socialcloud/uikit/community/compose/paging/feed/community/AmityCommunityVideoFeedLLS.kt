package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyVideoFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileVideoFeedItem


fun LazyListScope.amityCommunityVideoFeedLLS(
    modifier: Modifier = Modifier,
    videoPosts: LazyPagingItems<AmityPost>,
) {
    item { Spacer(modifier.height(12.dp)) }
    /*  count calculation logic
     *  showing 2 items in a row
     *  check if the item count is even or odd
     *  if even, show 2 items in a row
     *  if odd, show last item in a new row
     */
    items(
        count = videoPosts.itemCount / 2 + videoPosts.itemCount % 2,
        key = { "video_${videoPosts[it]?.getPostId() ?: it}" }
    ) { index ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .aspectRatio(2f)
        ) {
            val isFirstVideoIndexValid =
                index * 2 < videoPosts.itemCount && index >= 0

            if (isFirstVideoIndexValid) {
                val firstVideo = videoPosts[index * 2]
                if (firstVideo == null) {
                    Box(modifier.weight(1f))
                } else {
                    AmityProfileVideoFeedItem(
                        modifier = modifier.weight(1f),
                        data = firstVideo.getData() as AmityPost.Data.VIDEO
                    )
                }
            } else {
                Box(modifier.weight(1f))
            }

            val isSecondVideoIndexValid =
                index * 2 + 1 < videoPosts.itemCount && index >= 0

            if (isSecondVideoIndexValid) {
                val secondVideo = videoPosts.peek(index * 2 + 1)
                if (secondVideo == null) {
                    Box(modifier = modifier.weight(1f))
                } else {
                    AmityProfileVideoFeedItem(
                        modifier = modifier.weight(1f),
                        data = secondVideo.getData() as AmityPost.Data.VIDEO
                    )
                }
            } else {
                Box(modifier = modifier.weight(1f))
            }
        }
    }

    if (videoPosts.itemCount == 0) {
        item {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyVideoFeed()
            }
        }
    }
}