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
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyImageFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItem

fun LazyListScope.amityCommunityImageFeedLLS(
    modifier: Modifier = Modifier,
    imagePosts: LazyPagingItems<AmityPost>,
) {
    item { Spacer(modifier.height(12.dp)) }
    /*  count calculation logic
     *  showing 2 items in a row
     *  check if the item count is even or odd
     *  if even, show 2 items in a row
     *  if odd, show last item in a new row
     */
    items(
        count = imagePosts.itemCount / 2 + imagePosts.itemCount % 2,
        key = { "image_${imagePosts[it]?.getPostId() ?: it}" }
    ) { index ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .aspectRatio(2f)
        ) {
            //  check first image index is valid
            val isFirstImageIndexValid =
                index * 2 < imagePosts.itemCount && index >= 0
            val firstImage = if (isFirstImageIndexValid) {
                imagePosts[index * 2]
            } else {
                null
            }
            AmityProfileImageFeedItem(
                modifier = modifier.weight(1f),
                data = firstImage?.getData() as? AmityPost.Data.IMAGE
            )

            //  check second image index is valid
            val isSecondImageIndexValid =
                index * 2 + 1 < imagePosts.itemCount && index >= 0
            val secondImage = if (isSecondImageIndexValid) {
                imagePosts[index * 2 + 1]
            } else {
                null
            }

            //  show image thumbnail if index is valid
            //  if not, it's one last item in a new row and show empty box
            if (isSecondImageIndexValid) {
                AmityProfileImageFeedItem(
                    modifier = modifier.weight(1f),
                    data = secondImage?.getData() as? AmityPost.Data.IMAGE
                )
            } else {
                Box(modifier = modifier.weight(1f))
            }
        }
    }

    if (imagePosts.itemCount == 0) {
        item {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyImageFeed()
            }
        }
    }
}