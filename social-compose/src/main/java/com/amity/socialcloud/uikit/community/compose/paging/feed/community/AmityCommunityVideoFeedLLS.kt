package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.delay
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyVideoFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileVideoFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityVideoFeedContainer
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory


fun LazyListScope.amityCommunityVideoFeedLLS(
    modifier: Modifier = Modifier,
    videoPosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
) {
    item {
        var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }
        
        val currentPostIds = (0 until videoPosts.itemCount).mapNotNull { index ->
            videoPosts[index]?.getPostId()
        }.toSet()
        
        LaunchedEffect(currentPostIds) {
            delay(300) // Wait 300ms before updating
            debouncedAvailablePostIds = currentPostIds
        }
        
        AmityVideoFeedContainer(
            availablePostIds = debouncedAvailablePostIds
        ) { openDialog ->
            AmityCommunityVideoFeedContent(
                modifier = modifier,
                videoPosts = videoPosts,
                onViewPost = onViewPost,
                openDialog = openDialog
            )
        }
    }
}

@Composable
private fun AmityCommunityVideoFeedContent(
    modifier: Modifier = Modifier,
    videoPosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
    openDialog: (AmityPost.Data.VIDEO, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit
) {
    Column {
        Spacer(modifier.height(12.dp))
        
        /*  count calculation logic
         *  showing 2 items in a row
         *  check if the item count is even or odd
         *  if even, show 2 items in a row
         *  if odd, show last item in a new row
         */
        for (index in 0 until (videoPosts.itemCount / 2 + videoPosts.itemCount % 2)) {
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
                            data = firstVideo.getData() as AmityPost.Data.VIDEO,
                            postId = firstVideo.getPostId(), // For deletion detection
                            parentPostId = firstVideo.getParentPostId(), // For navigation
                            onPostClick = { postId ->
                                onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                            },
                            openDialog = openDialog
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
                            data = secondVideo.getData() as AmityPost.Data.VIDEO,
                            postId = secondVideo.getPostId(), // For deletion detection
                            parentPostId = secondVideo.getParentPostId(), // For navigation
                            onPostClick = { postId ->
                                onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                            },
                            openDialog = openDialog
                        )
                    }
                } else {
                    Box(modifier = modifier.weight(1f))
                }
            }
        }

        if (videoPosts.itemCount == 0) {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyVideoFeed()
            }
        }
    }
}